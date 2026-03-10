// EncryptResponseAspect 사용으로 대체함

// package com.codej.base.advice;

// import java.util.HashMap;
// import java.util.Map;

// import javax.servlet.http.HttpServletRequest;

// import org.springframework.core.MethodParameter;
// import org.springframework.http.MediaType;
// import org.springframework.http.server.ServerHttpRequest;
// import org.springframework.http.server.ServerHttpResponse;
// import org.springframework.web.bind.annotation.ControllerAdvice;
// import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

// import com.codej.base.utils.EncryptUtil;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.google.gson.Gson;
// import com.google.gson.GsonBuilder;

// @ControllerAdvice
// public class EncryptResponseAdvice implements ResponseBodyAdvice<Object> {

//     private final HttpServletRequest request;

//     // HttpServletRequest를 주입받아 파라미터 확인
//     public EncryptResponseAdvice(HttpServletRequest request) {
//         this.request = request;
//     }

//     @Override
//     public boolean supports(MethodParameter returnType, Class converterType) {
//         // POST 요청만 지원
//         // return "POST".equalsIgnoreCase(request.getMethod());
//         return true;
//     }

//     @Override
//     public Object beforeBodyWrite(
//             Object body,
//             MethodParameter returnType,
//             MediaType selectedContentType,
//             Class selectedConverterType,
//             ServerHttpRequest request,
//             ServerHttpResponse response) {

//         // POST 요청의 Body에서 encrypt 값 확인
//         Boolean encrypt = getEncrypt();

//         // encrypt=true이면 암호화 처리
//         if (encrypt) {
//             Map<String, String> map = new HashMap<>();
//             map.put("data", encrypt(body));
//             return map;
//         }

//         // encrypt=false인 경우 평문 응답
//         return body;
//     }

//     private boolean getEncrypt() {
//         try {
//             // 요청 Body를 JSON으로 파싱
//             ObjectMapper objectMapper = new ObjectMapper();
//             Map<String, Object> bodyMap = objectMapper.readValue(
//                     this.request.getInputStream(),
//                     Map.class);

//             // encrypt 값 추출 (true로 변환 가능한 경우 true, 없거나 false인 경우 false)
//             return bodyMap.getOrDefault("encrypt", false) instanceof Boolean &&
//                     (Boolean) bodyMap.get("encrypt");
//         } catch (Exception e) {
//             // 예외 발생 시 encrypt 기본값 false 반환
//             return false;
//         }
//     }

//     // 암호화 로직 구현
//     public static String encrypt(Object result) {
//         Gson gson = new GsonBuilder()
//                 .setDateFormat("yyyy-MM-dd HH:mm:ss") // 원하는 형식 설정
//                 .create();
//         String json = gson.toJson(result);
//         return EncryptUtil.encrypt(json);
//     }
// }
