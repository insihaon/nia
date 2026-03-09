package com.nia.ai.performance.send.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nia.ai.performance.send.common.NiaAiPerSendLinkageCodeInfo;
import com.nia.ai.performance.send.common.UtlDateHelper;
import com.nia.ai.performance.send.mapper.PerformanceMapper;
import com.nia.ai.performance.send.service.PerformanceSendService;
import com.nia.ai.performance.send.vo.performance.PerformaceListVo;
import com.nia.ai.performance.send.vo.performance.PerformaceVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

@Service("PerformanceSendService")
public class PerformanceSendServiceImpl implements PerformanceSendService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PerformanceSendServiceImpl.class);

    @Autowired
    private PerformanceMapper performanceMapper;

    @Autowired
    @Qualifier("AiPerSend_RestTemplate")
    private RestTemplate restTemplate;

    @Value("${spring.ai.api.url}")
    private String aiSendUrl;

    @Override
    public void sendPerformanceData(String date) {
        LOGGER.info("==========>[PerformanceSendService] sendPerformanceData <==============");
        ResponseEntity<String> responseEntity;
        String requestData;
        HashMap<String, String> parameterMap = null;
        HttpEntity<String> request;
        HttpHeaders httpHeaders;

        try {
//            yyyyMMddHH = (UtlDateHelper.getCurrentDateTime()+"").substring(0,14);
//
//            if(UtlDateHelper.stringToTimestamp(yyyyMMddHH+"00:00").getTime() <= UtlDateHelper.getCurrentDateTime().getTime()
//                    && UtlDateHelper.stringToTimestamp(yyyyMMddHH+"15:00").getTime() > UtlDateHelper.getCurrentDateTime().getTime()){
//                updateTime = UtlDateHelper.stringToTimestamp(yyyyMMddHH+"00:00");
//            }else if(UtlDateHelper.stringToTimestamp(yyyyMMddHH+"15:00").getTime() <= UtlDateHelper.getCurrentDateTime().getTime()
//                    && UtlDateHelper.stringToTimestamp(yyyyMMddHH+"30:00").getTime() > UtlDateHelper.getCurrentDateTime().getTime()){
//                updateTime = UtlDateHelper.stringToTimestamp(yyyyMMddHH+"15:00");
//            }else if(UtlDateHelper.stringToTimestamp(yyyyMMddHH+"30:00").getTime() <= UtlDateHelper.getCurrentDateTime().getTime()
//                    && UtlDateHelper.stringToTimestamp(yyyyMMddHH+"45:00").getTime() > UtlDateHelper.getCurrentDateTime().getTime()){
//                updateTime = UtlDateHelper.stringToTimestamp(yyyyMMddHH+"30:00");
//            }else if(UtlDateHelper.stringToTimestamp(yyyyMMddHH+"45:00").getTime() <= UtlDateHelper.getCurrentDateTime().getTime()){
//                updateTime = UtlDateHelper.stringToTimestamp(yyyyMMddHH+"45:00");
//            }
//
//            if(updateTime != null){
//                checkUpdateTime = performanceMapper.selectUpdateTime();
//                if(StringUtils.isNotEmpty(checkUpdateTime)){
//                    LOGGER.info((UtlDateHelper.stringToTimestamp(checkUpdateTime))+"");
//                    if((UtlDateHelper.stringToTimestamp(checkUpdateTime)).getTime() < updateTime.getTime()){
//                        parameterMap = new HashMap<String, String>();
//                        parameterMap.put("updateTime", updateTime+"");
//                        performaceVoList = performanceMapper.selectPerformanceDataList(parameterMap);
//
//                        if(performaceVoList != null && performaceVoList.size() > 0){
//                            performaceListVo = performaceListVoObjectFactory.getObject();
//                            performaceListVo.setData(performaceVoList);
//                        }
//                    }
//                }
//            }
//            parameterMap = new HashMap<String, String>();
//            parameterMap.put("updateTime", updateTime+"");
//            performanceMapper.updatePerformanceDataSendTime(parameterMap);

            parameterMap = new HashMap<String, String>();
            parameterMap.put("ocrtime", date);
            List<PerformaceVo> performaceVoList = performanceMapper.selectPerformanceDataList(parameterMap);

            if(performaceVoList != null && performaceVoList.size() > 0){
                ObjectMapper mapper = new ObjectMapper();
                requestData = mapper.writeValueAsString(performaceVoList);

                httpHeaders = new HttpHeaders();
                httpHeaders.setContentType(MediaType.APPLICATION_JSON);
                httpHeaders.add("Content-Type","application/json");

                request = new HttpEntity<String>(requestData, httpHeaders);

                restTemplate.getMessageConverters()
                            .add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));

                try {
                    LOGGER.info("==========>[PerformanceSendService] sendPerformanceData("+date+") size: "+ performaceVoList.size() +"<==============");
                    responseEntity = restTemplate.postForEntity(aiSendUrl+"/"+ NiaAiPerSendLinkageCodeInfo.LINKAGE_PER_SEND_SERVICE_NAME+"/",request, String.class);
                }catch (ResourceAccessException rae){
                    Thread.sleep(3000);
                    restTemplate.getMessageConverters()
                                .add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));

                    responseEntity = restTemplate.postForEntity(aiSendUrl+"/"+ NiaAiPerSendLinkageCodeInfo.LINKAGE_PER_SEND_SERVICE_NAME+"/", requestData, String.class);
                    LOGGER.info("==========>[PerformanceSendService] sendPerformanceData result: "+ responseEntity.getBody() +"<==============");
                }
            }

//            if(performaceListVo != null){
//                if(performaceListVo.getData() != null && performaceListVo.getData().size() > 0){
//
//                    ObjectMapper mapper = new ObjectMapper();
//                    requestData = mapper.writeValueAsString(performaceListVo);
//
//                    restTemplate.getMessageConverters()
//                                .add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
//
//                    try {
//                        responseEntity = restTemplate.postForEntity(aiSendUrl+"/"+ NiaAiPerSendLinkageCodeInfo.LINKAGE_PER_SEND_SERVICE_NAME,requestData,String.class);
//                        LOGGER.info("==========>[PerformanceSendService] sendPerformanceData result: "+ responseEntity.getBody() +"<==============");
//                    }catch (ResourceAccessException rae){
//                        Thread.sleep(3000);
//                        restTemplate.getMessageConverters()
//                                    .add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
//
//                        responseEntity = restTemplate.postForEntity(aiSendUrl+"/"+ NiaAiPerSendLinkageCodeInfo.LINKAGE_PER_SEND_SERVICE_NAME, requestData, String.class);
//                        LOGGER.info("==========>[PerformanceSendService] sendPerformanceData result: "+ responseEntity.getBody() +"<==============");
//                    }
//                }
//            }
        }catch (Exception e){
            LOGGER.error("=====> [PerformanceSendService] sendPerformanceData error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
        }
    }
}
