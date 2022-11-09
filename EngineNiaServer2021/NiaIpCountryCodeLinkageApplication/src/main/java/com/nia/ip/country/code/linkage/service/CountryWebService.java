package com.nia.ip.country.code.linkage.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nia.ip.country.code.linkage.common.LoggerPrint;
import com.nia.ip.country.code.linkage.common.UtlCommon;
import com.nia.ip.country.code.linkage.mapper.CountryMapper;
import com.nia.ip.country.code.linkage.vo.country.CountryVo;
import com.nia.ip.country.code.linkage.vo.web.ResponseVo;
import com.nia.ip.country.code.linkage.vo.web.WebResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

@Service("CountryWebService")
public class CountryWebService {

    @Autowired
    private CountryMapper countryMapper;
    @Autowired
    private org.springframework.beans.factory.ObjectFactory<WebResultVo> webResultVoObjectFactory;

    @Autowired
    @Qualifier("RestTemplate")
    private RestTemplate restTemplate;

    public void sendData(String ip){
        LoggerPrint.infoLog(" ip : " + ip.split("\\.")[3]);
        String url;
        String serviceKey;
        String msg;
        ResponseEntity<String> responseEntity = null;
        HttpEntity<String> request = null;
        HttpHeaders httpHeaders = null;
        URI uri = null;
        Map<String, String> paramMap;
        Object obj;
        WebResultVo webResultVo;

        try {
            serviceKey = "gkODhBXK7ccFDlrEyXMUleY7tkLmU0ruF8PQ0BDVRwVc9gLLqPgxb3tgFaAb32cxtq%2BJYYPEUx6gLV1%2BCmMLTw%3D%3D";
            url = "http://apis.data.go.kr/B551505/whois/ipas_country_code";

            httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            httpHeaders.add("Content-Type","application/json");

            request = new HttpEntity<String>(httpHeaders);

            restTemplate.getMessageConverters()
                    .add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));

            paramMap = new HashMap<>();
            paramMap.put("serviceKey", serviceKey);
            paramMap.put("query", ip);
            paramMap.put("answer", "json");

            url = url+"?"+ UtlCommon.mapToUrlParam(paramMap);

            uri = new URI(url);

            responseEntity = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    request,
                    String.class
            );

            msg = responseEntity.getBody();
            webResultVo = webResultVoObjectFactory.getObject();

            obj = UtlCommon.jsonToObject(webResultVo, msg);

            webResultVo = (WebResultVo)obj;

            countryMapper.updateIpCountryCode(webResultVo.getResponseVo().getWhoisVo());
        }catch (Exception e){
            LoggerPrint.errorLog(e);
        }
    }
}
