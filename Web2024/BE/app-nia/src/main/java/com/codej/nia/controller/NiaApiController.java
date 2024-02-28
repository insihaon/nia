package com.codej.nia.controller;

import java.io.IOException;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codej.base.dto.response.BaseResponse;
import com.codej.base.provider.JwtTokenProvider;
import com.codej.base.utils.CryptUtil;
import com.codej.base.utils.JsonUtil;
import com.codej.nia.service.NiaService;
import com.codej.web.controller.AbsDataController;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.Buffer;

@Slf4j
@RestController
@RequestMapping(value = { "/dh" })

public class NiaApiController extends AbsDataController {

    // @Override
    // public Boolean canAccess() {
    // return true;
    // } // 임시 인증 코드 처리 

    @Value("${myconf.dhub.auth-url:http://127.0.0.1:8070/dh/profile}")
    private String dhubAuthUrl;

    @Value("${myconf.api-server.hub-api:http://127.0.0.1:8070/dh}")
    private String hubUrl;

    private static final OkHttpClient client = new OkHttpClient();

    @Autowired(required = false)
    protected NiaService niaService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    protected Object getService() {
        return niaService;
    }

    @PostMapping(value = "/SELECT_BLACK_AUTHORITY_BY_MENU") 
    public BaseResponse auth( @RequestBody HashMap<String, Object> param) throws Exception {
        return responseService.createSingleResponse(getAuth(param));
    }

    public ResponseEntity<String> getAuth(@RequestBody HashMap<String, Object> param) throws Exception {
        return postAuth(param);
    }

    // curl -X POST -H "Content-Type: application/json" -d '{
    // "USER_ID": "T021239112 ",
    // "MENU_ID": "O0000183"
    // }' http://localhost:8080/dh/postAuth
    @PostMapping(value = "/postAuth") 

    public ResponseEntity<String> postAuth(@RequestBody HashMap<String, Object> param) throws Exception {
        // log.info("AUTH_URL=" + AUTH_URL);

        // log.info("jsonRequestBody=" + jsonRequestBody);
        HashMap<String, Object> reqParam = new HashMap<String, Object>(){{
            put("REQ", param);
        }};
        
        ResponseEntity<String> res = post(dhubAuthUrl, reqParam);
        String uid = (String)param.get("USER_ID");
        
        String responseBody = res.getBody();
        log.info("<<<<< RESPONSE body={}", responseBody);
        
        // Jackson ObjectMapper를 사용하여 JSON 문자열을 파싱합니다.
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        log.info("jsonNode={}", jsonNode.toString());

        // List<String> roles = Arrays.asList(new String[]{"ROLE_ADMIN", "ROLE_USER"});
        // String token = jwtTokenProvider.createToken(uid,roles, null);
        // if(token != null && jsonNode != null) {
        //     ObjectNode objectNode = (ObjectNode) jsonNode;
        //     objectNode.put("accessToken", token);
        // }  

        String jsonString = jsonNode.toString();
        // '{"RES":{"USER_ID":"T021239112","MENU_ID":"O0000183","USER_NM":"이창재","LOGIN_ID":"91187283","MOBILE_PHONE":"010-8625-6681","EMAIL":"91187283@ktfriend.com","RowCnt":8,"NDH_PROFILE_LIST":[{"BLACK_DTL_AUTH_CLS_CD":"2","BLACK_DTL_AUTH_NM":"btnAdd"},{"BLACK_DTL_AUTH_CLS_CD":"2","BLACK_DTL_AUTH_NM":"btnTemplate"},{"BLACK_DTL_AUTH_CLS_CD":"2","BLACK_DTL_AUTH_NM":"btnManualAdd"},{"BLACK_DTL_AUTH_CLS_CD":"2","BLACK_DTL_AUTH_NM":"btnReceiver"},{"BLACK_DTL_AUTH_CLS_CD":"2","BLACK_DTL_AUTH_NM":"btnExceptAdd"},{"BLACK_DTL_AUTH_CLS_CD":"2","BLACK_DTL_AUTH_NM":"btnAnalysis"},{"BLACK_DTL_AUTH_CLS_CD":"2","BLACK_DTL_AUTH_NM":"btnDelete"},{"BLACK_DTL_AUTH_CLS_CD":"2","BLACK_DTL_AUTH_NM":"btnManualDelete"}]}}'

        return new ResponseEntity<String>(jsonString, HttpStatus.OK);
    }

    @PostMapping(value = "/profile") 
    public ResponseEntity<String> profile( @RequestBody HashMap<String, Object> param) throws Exception {
        return getAuthDev(param);
    }

    public ResponseEntity<String> getAuthDev(HashMap<String, Object> param) {
        String jsonString = "{\"RES\":{\"USER_ID\":\"T021239112\",\"MENU_ID\":\"O0000183\",\"USER_NM\":\"이창재\",\"LOGIN_ID\":\"91187283\",\"MOBILE_PHONE\":\"010-8625-6681\",\"EMAIL\":\"91187283@ktfriend.com\",\"RowCnt\":8,\"NDH_PROFILE_LIST\":[{\"BLACK_DTL_AUTH_CLS_CD\":\"2\",\"BLACK_DTL_AUTH_NM\":\"btnAdd\"},{\"BLACK_DTL_AUTH_CLS_CD\":\"2\",\"BLACK_DTL_AUTH_NM\":\"btnTemplate\"},{\"BLACK_DTL_AUTH_CLS_CD\":\"2\",\"BLACK_DTL_AUTH_NM\":\"btnManualAdd\"},{\"BLACK_DTL_AUTH_CLS_CD\":\"2\",\"BLACK_DTL_AUTH_NM\":\"btnReceiver\"},{\"BLACK_DTL_AUTH_CLS_CD\":\"2\",\"BLACK_DTL_AUTH_NM\":\"btnExceptAdd\"},{\"BLACK_DTL_AUTH_CLS_CD\":\"2\",\"BLACK_DTL_AUTH_NM\":\"btnAnalysis\"},{\"BLACK_DTL_AUTH_CLS_CD\":\"2\",\"BLACK_DTL_AUTH_NM\":\"btnDelete\"},{\"BLACK_DTL_AUTH_CLS_CD\":\"2\",\"BLACK_DTL_AUTH_NM\":\"btnManualDelete\"}]}}";
        return new ResponseEntity<String>(jsonString, HttpStatus.OK);
    }

    public ResponseEntity<String> post(String url, HashMap<String, Object> param) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequestBody = objectMapper.writeValueAsString(param);

        // Create an okhttp3.RequestBody from the JSON string
        okhttp3.RequestBody body = okhttp3.RequestBody.create(
            okhttp3.MediaType.parse("application/json; charset=utf-8"),
            jsonRequestBody
        );
    
        // Create the request
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Content-Type", "application/json")  
                .build();


        logCurlCommand(request);

        // Execute the request
        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();
        // log.info("responseBody=" + responseBody);

        return new ResponseEntity<String>(responseBody, HttpStatus.OK);
    }
    
    @PostMapping(value = "/api-test/{uri}")
    public BaseResponse postHubApi(
            @PathVariable("uri") String uri,
            @RequestBody HashMap<String, Object> param) throws Exception {

        String url = String.format("%s/%s", hubUrl, uri);

        param = CryptUtil.decryptToMap(param);

        ResponseEntity<String> res = post(url, param);
        String responseBody = res.getBody();

        // Jackson ObjectMapper를 사용하여 JSON 문자열을 파싱합니다.
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        if(jsonNode != null) {
            try {
                return responseService.createSingleResponse(jsonNode);
            } catch (Exception e) {
                // 예외 처리
            }
        }    
        return responseService.createFailResponse(String.format("서비스 응답 실패 : %s", jsonNode));
    }

    @PostMapping(value = "/REQ_RESEND")
    public BaseResponse postHubResendApi(
        @RequestBody HashMap<String, Object> param) throws Exception {

        String url = String.format("%s/%s", hubUrl, "RESEND" );

        log.info("/REQ_RESEND " );
        log.info(">>>>> REQUEST url={}, body={}", url, JsonUtil.convertClassToJsonString(param));
        ResponseEntity<String> res = post(url, param);

        String responseBody = res.getBody();
        log.info("<<<<< RESPONSE body={}", responseBody);

        // Jackson ObjectMapper를 사용하여 JSON 문자열을 파싱합니다.
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        if(jsonNode == null){
            throw new Exception("서비스 실패 : jsonNode Null");
        }
        
        log.info("jsonNode={}", jsonNode.toString());

        if(jsonNode != null) {
            try {
                return responseService.createSingleResponse(jsonNode);
            } catch (Exception e) {
                // 예외 처리
            }
        }    
        return responseService.createFailResponse(String.format("서비스 응답 실패 : %s", jsonNode));
    }


    @PostMapping(value = "/RESEND")
    public ResponseEntity<String> postHubResendTest(
        @RequestBody HashMap<String, Object> param) throws Exception {
        log.info("/RESEND " );
        log.info(">>>>> REQUEST body={}", JsonUtil.convertClassToJsonString(param));
        return new ResponseEntity<String>("{}", HttpStatus.OK);
    }

    @PostMapping(value = "/WS_000")
    public Object ws000( @RequestBody String strData) throws Exception {
        String retrunParam = strData;
        log.info("WS_000 >>>>> {}", strData);
        return retrunParam;
    }


    @PostMapping(value = "/WS_008")
    public Object ws008( @RequestBody String strData) throws Exception {
        String jsonString = "{\"res_msg\":\"null\",\"data\":\"[{\\\"ttid\\\":\\\"NMS_TT_20230906_91-230906-0107\\\",\\\"ttgb\\\":null,\\\"ttStatus\\\":\\\"1\\\",\\\"officeCd\\\":\\\"R00567\\\",\\\"nesCode\\\":\\\"F37652502\\\",\\\"raNesCode\\\":null,\\\"createTime\\\":\\\"20230906134149\\\",\\\"closeTime\\\":\\\"20230906151800\\\",\\\"operCnt\\\":0,\\\"custCnt\\\":0,\\\"raOperCnt\\\":0,\\\"raCustCnt\\\":null,\\\"mpDetail\\\":null,\\\"ttCauseD\\\":null,\\\"restore1Cd\\\":\\\"E\\\",\\\"restore2Cd\\\":null,\\\"returnVal\\\":null},{\\\"ttid\\\":\\\"NMS_TT_20230906_91-230906-0108\\\",\\\"ttgb\\\":null,\\\"ttStatus\\\":\\\"1\\\",\\\"officeCd\\\":\\\"R00429\\\",\\\"nesCode\\\":\\\"SLGJ07564\\\",\\\"raNesCode\\\":\\\"SLGJ07564\\\",\\\"createTime\\\":\\\"20230906134555\\\",\\\"closeTime\\\":\\\"20230906171800\\\",\\\"operCnt\\\":14,\\\"custCnt\\\":35,\\\"raOperCnt\\\":14,\\\"raCustCnt\\\":null,\\\"mpDetail\\\":null,\\\"ttCauseD\\\":null,\\\"restore1Cd\\\":\\\"F\\\",\\\"restore2Cd\\\":\\\"E\\\",\\\"returnVal\\\":null},{\\\"ttid\\\":\\\"NMS_TT_20230906_91-230906-0109\\\",\\\"ttgb\\\":null,\\\"ttStatus\\\":\\\"1\\\",\\\"officeCd\\\":\\\"R00962\\\",\\\"nesCode\\\":\\\"BSSY02771\\\",\\\"raNesCode\\\":\\\"BSSY02771\\\",\\\"createTime\\\":\\\"20230906134756\\\",\\\"closeTime\\\":\\\"20230906140200\\\",\\\"operCnt\\\":8,\\\"custCnt\\\":13,\\\"raOperCnt\\\":8,\\\"raCustCnt\\\":null,\\\"mpDetail\\\":null,\\\"ttCauseD\\\":null,\\\"restore1Cd\\\":\\\"J\\\",\\\"restore2Cd\\\":\\\"K\\\",\\\"returnVal\\\":null},{\\\"ttid\\\":\\\"NMS_TT_20230906_91-230906-0110\\\",\\\"ttgb\\\":null,\\\"ttStatus\\\":\\\"1\\\",\\\"officeCd\\\":\\\"R03006\\\",\\\"nesCode\\\":\\\"JNJHS1029\\\",\\\"raNesCode\\\":\\\"JNJHS1029\\\",\\\"createTime\\\":\\\"20230906134757\\\",\\\"closeTime\\\":\\\"20230906135900\\\",\\\"operCnt\\\":29,\\\"custCnt\\\":29,\\\"raOperCnt\\\":29,\\\"raCustCnt\\\":null,\\\"mpDetail\\\":null,\\\"ttCauseD\\\":null,\\\"restore1Cd\\\":null,\\\"restore2Cd\\\":null,\\\"returnVal\\\":null}]\",\"res_code\":\"200\"}";
        return jsonString;
    }   

    private void logCurlCommand(Request request) throws IOException {
        StringBuilder curlCommand = new StringBuilder("curl -X ");
        curlCommand.append(request.method()).append(" ").append(request.url()).append(" ");
    
        Headers headers = request.headers();
        for (String name : headers.names()) {
            curlCommand.append("-H \"").append(name).append(": ").append(headers.get(name)).append("\" ");
        }
    
        if (request.method().equalsIgnoreCase("POST") || request.method().equalsIgnoreCase("PUT")) {
            okhttp3.RequestBody requestBody = request.body();
            if (requestBody != null) {
                Buffer buffer = new Buffer();
                requestBody.writeTo(buffer);
                curlCommand.append("-d '").append(buffer.readUtf8()).append("'");
            }
        }
    
        log.info("$ " + curlCommand.toString());
    }
}
