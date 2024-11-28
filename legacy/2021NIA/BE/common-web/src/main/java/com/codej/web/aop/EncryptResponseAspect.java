package com.codej.web.aop;

import java.io.BufferedReader;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;

import com.codej.base.exception.CServiceException;
import com.codej.base.exception.CServiceIncorrectUse;
import com.codej.base.exception.CUntrustedRequestException;
import com.codej.base.property.GlobalConstants;
import com.codej.base.utils.EncryptUtil;
import com.codej.base.utils.JsonUtil;
import com.codej.web.vo.BaseVo;
import com.codej.web.vo.EncryptedVo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class EncryptResponseAspect {
    private final HttpServletRequest request;

    public EncryptResponseAspect(HttpServletRequest request) {
        this.request = request;
    }

    // @Around("execution(* com.kt.ipms.legacy..*(..)) && @annotation(com.codej.web.annotation.EncryptResponse))")
    // public Object handleEncryption(ProceedingJoinPoint joinPoint) throws Throwable {
    //     Object response = joinPoint.proceed();
    //     return response;
    // }

    @Around("(execution(* com.kt.ipms.legacy..*(..)) || execution(* com.codej.nia.controller..*(..))) && @annotation(com.codej.web.annotation.EncryptResponse)")
    public Object handleEncryption(ProceedingJoinPoint joinPoint) throws Throwable {
        // 요청 Body에서 encrypt 값을 확인
        Boolean encrypt = isEncrypt();

        // 요청 처리
        Object result = joinPoint.proceed();
        Class<?> clazz = result.getClass();
        if (encrypt || encrypt == null) {
            if (result == null) {
                // 결과가 void 타입 일 때
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

                ModelMap resultModel = new ModelMap();
                resultModel.addAttribute(GlobalConstants.Common.ENCRYPT, true);
                resultModel.addAttribute(GlobalConstants.Common.RESULT, encrypt(body));
                result = new ResponseEntity<>(resultModel, HttpStatus.OK);
            }
            else if (result instanceof ModelMap) {
                ModelMap resultModel = new ModelMap();
                resultModel.addAttribute(GlobalConstants.Common.ENCRYPT, true);
                resultModel.addAttribute(GlobalConstants.Common.RESULT, encrypt(result));
                result = resultModel;
            }
            else if (result instanceof Map) {
                /* *** 중요 ModelMap 이후에 처리해야 함 *** */
                Map<String,Object> resultMap = new HashMap<String, Object>();
                resultMap.put(GlobalConstants.Common.ENCRYPT, true);
                resultMap.put(GlobalConstants.Common.RESULT, encrypt(result));
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

    public static String encrypt(Object result) {
        Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss") // 원하는 형식 설정
            .create();
        String json = gson.toJson(result);
        return EncryptUtil.encrypt(json);
    }

    private Boolean isEncrypt() {
        Boolean encrypt = true;
        try {
            // 타임스탬프 보안 체크
            long now = new Date().getTime();
            String tsHeader = request.getHeader("_t");
            String ts = EncryptUtil.decryptText(tsHeader);

            if (ts == null || Math.abs((now - Long.valueOf(ts)) / 1000) > 10) {
                throw new CUntrustedRequestException();
            }

            // 요청 Body를 바로 읽기
            StringBuilder body = new StringBuilder();
            BufferedReader reader = request.getReader();
            String line;
            while ((line = reader.readLine()) != null) {
                body.append(line);
            }

            // JSON 데이터를 Map으로 변환
            Map<String, Object> requestBody = JsonUtil.convertJsonToClass(body.toString(), Map.class);
            Object value = requestBody.get(GlobalConstants.Common.ENCRYPT);
            if (value == null) {
                throw new NullPointerException();
            }

            String decryptText = EncryptUtil.decryptText((String) value);
            if (decryptText == null) {
                throw new CServiceIncorrectUse("데이터 형식을 알 수 없습니다.");
            }
            encrypt = "true".equals(decryptText);
        } catch (Exception e) {
            log.error("REQUEST ENCRYPT 위변조", e);
            throw new CUntrustedRequestException();
        }
        return encrypt;
    }
}
