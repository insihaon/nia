package com.codej.web.aop;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;

import com.codej.base.dto.AppDto;
import com.codej.base.exception.CServiceException;
import com.codej.base.exception.CServiceIncorrectUse;
import com.codej.base.exception.CUntrustedRequestException;
import com.codej.base.property.GlobalConstants;
import com.codej.base.utils.EncryptUtil;
import com.codej.base.utils.JsonUtil;
import com.codej.web.cached.RequestBodyHolder;
import com.codej.web.vo.BaseVo;
import com.codej.web.vo.EncryptedVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class EncryptResponseAspect {
    @Autowired
    private AppDto appDto;

    @Autowired
    private HttpServletRequest request;

    private static ObjectMapper objectMapper;

    public EncryptResponseAspect() {
        objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        objectMapper.setDateFormat(new StdDateFormat()); // 기본 ISO-8601 형식 사용
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false); // 타임스탬프 대신 문자열 사용
    }

    // @Around("execution(* com.kt.ipms.legacy..*(..)) && @annotation(com.codej.web.annotation.EncryptResponse))")
    // public Object handleEncryption(ProceedingJoinPoint joinPoint) throws Throwable {
    //     Object response = joinPoint.proceed();
    //     return response;
    // }

    @SuppressWarnings("unused")
    @Around("(execution(* com.kt.ipms.legacy..*(..)) || execution(* com.codej.nia.controller..*(..))) && @annotation(com.codej.web.annotation.EncryptResponse)")
    public Object handleEncryption(ProceedingJoinPoint joinPoint) throws Throwable {

        String requestBody = RequestBodyHolder.get();
 
        // 요청 Body에서 encrypt 값을 확인
        Boolean encrypt = isEncrypt(requestBody);

        RequestBodyHolder.clear();

        Object result = null;
        if(encrypt && requestBody != null) {
             // 메서드 시그니처 가져오기
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();

            // 메서드 파라미터와 어노테이션 확인
            Object[] args = joinPoint.getArgs();
            Annotation[][] parameterAnnotations = method.getParameterAnnotations();

            for (int i = 0; i < parameterAnnotations.length; i++) {
                for (Annotation annotation : parameterAnnotations[i]) {
                    if (annotation instanceof RequestBody) { // @RequestBody 어노테이션 체크
                        Object param = args[i];
                       
                        Map<String, Object> mapBody = JsonUtil.convertJsonToClass(requestBody, Map.class);
                        Object value = mapBody.get(GlobalConstants.Common.DATA);
                        if (value == null) {
                            throw new NullPointerException();
                        }

                        args[i] = JsonUtil.convertJsonToClass(EncryptUtil.decryptText((String)value), param.getClass());
                    }
                }
            }

            // 변경된 인자를 사용하여 메서드 실행
            result = joinPoint.proceed(args);
        } else {
            // 요청 처리
            result = joinPoint.proceed();
        }

        
        if (encrypt || encrypt == null) {
            Class<?> clazz = null;
            try {
                clazz = result.getClass();
            } catch (Exception e) {
                clazz = null;
            }

            if (result == null) {
                // 결과가 void 타입 일 때
                return null;
            }
            else if (result instanceof String) {
                // 결과가 String 타입 일 때
            }
            else if (result instanceof List) {
                // 결과가 List 타입 일 때
            }
            else if (result instanceof ResponseEntity) {
                ResponseEntity<?> responseEntity = (ResponseEntity<?>) result;
                Object body = responseEntity.getBody();
                HttpHeaders headers = new HttpHeaders(responseEntity.getHeaders()); // 기존 헤더 복사
                HttpStatus status = responseEntity.getStatusCode();
                String contentType = headers.getContentType().toString();
                if(headers.getContentType().includes(MediaType.APPLICATION_OCTET_STREAM) || contentType.contains(MediaType.TEXT_PLAIN_VALUE)) {
                    return responseEntity;
                }
                // responseEntity.getHeaders().forEach((key, value) -> {
                //     if (!key.equalsIgnoreCase(HttpHeaders.CONTENT_TYPE)) { // Content-Type 제외
                //         headers.put(key, value); // 키와 값을 새로운 headers2에 추가
                //     }
                // });
                // headers.setContentType(MediaType.APPLICATION_JSON);

                ModelMap resultModel = new ModelMap();
                resultModel.addAttribute(GlobalConstants.Common.ENCRYPT, true);
                resultModel.addAttribute(GlobalConstants.Common.RESULT, encrypt(body));

                result = new ResponseEntity<>(resultModel, headers, status);
            }
            else if (result instanceof ModelMap) {
                ModelMap resultModel = new ModelMap();
                resultModel.addAttribute(GlobalConstants.Common.ENCRYPT, true);
                resultModel.addAttribute(GlobalConstants.Common.RESULT, encrypt(result));
                result = resultModel;
            }
            else if (result instanceof Map) {
                /*** 중요 ModelMap 이후에 처리해야 함 ***/
                Map<String,Object> resultMap = new HashMap<String, Object>();
                resultMap.put(GlobalConstants.Common.ENCRYPT, true);
                HashMap<String, Object> result_result = new HashMap<String, Object>();
                result_result.put("result",encrypt(result));
                resultMap.put(GlobalConstants.Common.RESULT, result_result);
                result = resultMap;
            }
            else if (result instanceof BaseVo) {
                return new EncryptedVo(true, encrypt(result));
            } 
            
            if (result.getClass().equals(clazz) == false) {
                throw new CServiceException("@EncryptResponse 에러");
            }
        }

        // 원본 응답 
        return result;
    }

    public static String encrypt(Object result) throws JsonProcessingException {
        // Gson gson = new GsonBuilder()
        //     .setDateFormat("yyyy-MM-dd HH:mm:ss") // 원하는 형식 설정
        //     .create();
        // String json = gson.toJson(result);
        // return EncryptUtil.encrypt(json);

        String json = objectMapper.writeValueAsString(result);
        return EncryptUtil.encrypt(json);
    }
    
    @SuppressWarnings("unchecked")
    private Boolean isEncrypt(String requestBody) {
        Boolean encrypt = true;
        try {
            if(appDto.isDevProfile() == false) {
                // 타임스탬프 보안 체크
                long now = new Date().getTime();
                String tsHeader = request.getHeader("_t");
                String ts = EncryptUtil.decryptText(tsHeader);
    
                if (ts == null || Math.abs((now - Long.valueOf(ts)) / 1000) > 10) {
                    throw new CUntrustedRequestException();
                }
            }

            if (requestBody == null) {
                throw new CServiceIncorrectUse("요청 데이터가 비어 있습니다. ");
            }

            Map<String, Object> mapBody = JsonUtil.convertJsonToClass(requestBody, Map.class);
            Object value = mapBody.get(GlobalConstants.Common.ENCRYPT);
            if (value == null) {
                throw new NullPointerException();
            }

            String decryptText = EncryptUtil.decryptText((String) value);
            if (decryptText == null) {
                throw new CServiceIncorrectUse("요청 데이터가 규격에 맞지 안습니다.");
            }
            encrypt = "true".equals(decryptText);
        } catch (Exception e) {
            String msg = "REQUEST ENCRYPT 위변조";
            log.error(msg, e);
            throw new CUntrustedRequestException(msg);
        }
        return encrypt;
    }
}
