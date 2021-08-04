package com.nia.tsdn.service.result.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nia.tsdn.service.result.service.AsyncSendService;
import com.nia.tsdn.service.result.vo.ReservedResultVo;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

@Service("AsyncSendService")
public class AsyncSendServiceImpl implements AsyncSendService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncSendServiceImpl.class);

    @Autowired
    @Qualifier("ServiceResult_RestTemplate")
    private RestTemplate restTemplate;

    @Override
    public void sendServiceResult(ReservedResultVo reservedResultVo) {
        LOGGER.info("==========>[AsyncSendService] sendServiceResult <==============");
        ResponseEntity<String> responseEntity;
        String requestData;
        Object obj;
        String msg;

        try {
            ObjectMapper mapper = new ObjectMapper();
            requestData = mapper.writeValueAsString(reservedResultVo);

            restTemplate.getMessageConverters()
                        .add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));

            try {
                responseEntity = restTemplate.postForEntity(reservedResultVo.getRequestUrl(),requestData,String.class);
                LOGGER.info("==========>[AsyncSendService] AsyncSendService result: "+ responseEntity.getBody() +"<==============");
            }catch (ResourceAccessException rae){
                Thread.sleep(3000);
                restTemplate.getMessageConverters()
                            .add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));

                responseEntity = restTemplate.postForEntity(reservedResultVo.getRequestUrl(), requestData, String.class);
                LOGGER.info("==========>[AsyncSendService] AsyncSendService result: "+ responseEntity.getBody() +"<==============");
            }
        }catch (Exception e){
            LOGGER.error("=====> [AsyncSendService] AsyncSendService error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
        }
    }
}
