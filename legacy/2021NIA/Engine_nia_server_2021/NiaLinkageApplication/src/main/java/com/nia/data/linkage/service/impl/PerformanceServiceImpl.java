package com.nia.data.linkage.service.impl;

import com.nia.data.linkage.common.UtlCommon;
import com.nia.data.linkage.common.UtlDateHelper;
import com.nia.data.linkage.mapper.PerformaceMapper;
import com.nia.data.linkage.service.PerformanceService;
import com.nia.data.linkage.vo.performance.PerformaceCollectTimeVo;
import com.nia.data.linkage.vo.performance.PerformaceVo;
import com.nia.data.linkage.vo.performance.json.PerformaceHitsListVo;
import com.nia.data.linkage.vo.performance.json.PerformaceHitsVo;
import com.nia.data.linkage.vo.performance.json.PerformaceJonDataVo;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service("PerformanceService")
public class PerformanceServiceImpl implements PerformanceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PerformanceServiceImpl.class);

    @Autowired
    private PerformaceMapper performaceMapper;

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<PerformaceJonDataVo> performaceJonDataVoObjectFactory;

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<PerformaceVo> performaceVoFactory;

    @Autowired
    @Qualifier("NIA_RestTemplate")
    private RestTemplate restTemplate;

    @Value("${spring.nia.api.performance-url}")
    private String performanceUrl;

    @Override
    public void getPerformanceData() {
        LOGGER.info("==========>[PerformanceService] getPerformanceData() <==============");
        ResponseEntity<String> responseEntity;
        Object obj;
        String msg;
        PerformaceJonDataVo performaceJonDataVo;
        PerformaceCollectTimeVo performaceCollectTimeVo;
        String curDate;
        String fromDate;
        String toDate;
        String time;

        try {
            Thread.sleep(1000*120);

            curDate = UtlDateHelper.getCurrentDate();
           // fromDate = "2020-10-15";
           // toDate = "2020-10-15";
           // time = "09:00:00";

            performaceCollectTimeVo = performaceMapper.selectPerformaceCollectTime();

            time = performaceCollectTimeVo.getCollectTime();

            restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));

            try {
               // responseEntity = restTemplate.exchange(performanceUrl+"_search?q=(logTime["+toDate+"+TO+"+toDate+"])&size=10000&pretty", HttpMethod.GET, null, String.class);
                responseEntity = restTemplate.exchange(performanceUrl+"_search?q=(logTime["+curDate+"+TO+"+curDate+"]) AND ocrtm:\""+time+"\" AND almType:\"pm_amppwr\"&size=10000&pretty"
                                                         , HttpMethod.GET, null, String.class);

                LOGGER.info("==========>[TopologyService] getTopologyData result: "+ responseEntity.getBody() +"<==============");
            }catch (ResourceAccessException rae){
                Thread.sleep(3000);
                restTemplate.getMessageConverters()
                            .add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));

                responseEntity = restTemplate.exchange(performanceUrl, HttpMethod.GET, null, String.class);
            }

            msg = new String(responseEntity.getBody());
            performaceJonDataVo = performaceJonDataVoObjectFactory.getObject();

            obj = UtlCommon.jsonToObject(performaceJonDataVo, msg);

            performaceJonDataVo = (PerformaceJonDataVo)obj;
            LOGGER.info(performaceJonDataVo.toString());

            performaceCollectTimeVo.setCollectDateTime(UtlDateHelper.stringToTimestamp(curDate+" "+ performaceCollectTimeVo.getCollectTime()));
            setPerformanceData(performaceJonDataVo, performaceCollectTimeVo);
        }catch (Exception e){
            LOGGER.error("=====> [PerformanceService] getPerformanceData() error "+ ExceptionUtils.getStackTrace(e)+ "<=====");
        }
    }

    @Override
    public void setPerformanceData(PerformaceJonDataVo performaceJonDataVo, PerformaceCollectTimeVo performaceCollectTimeVo) {
        PerformaceVo performaceVo;
        List<PerformaceVo> performaceVoList = null;
        HashMap<String, Object> paramMap;
        int totalCycle = 0;
        List<PerformaceVo> tmpPerformaceList;
		int i = 1;

        try {
            if(performaceJonDataVo != null){
                if(performaceJonDataVo.getPerformaceHitsVo() != null){
                    if(performaceJonDataVo.getPerformaceHitsVo().getPerformaceHitsListVoList() != null
                        && performaceJonDataVo.getPerformaceHitsVo().getPerformaceHitsListVoList().size() > 0){
                        performaceVoList = new ArrayList<PerformaceVo>();

                        for(PerformaceHitsListVo performaceHitsListVo : performaceJonDataVo.getPerformaceHitsVo().getPerformaceHitsListVoList()){
                            performaceVo = performaceVoFactory.getObject();
                            performaceVo.setPerformaceid(performaceHitsListVo.getId());
                            performaceVo.setSysname(performaceHitsListVo.getPerformaceSourceVo().getDeviceName());
                            performaceVo.setOcrtime(UtlDateHelper.stringToTimestamp(performaceHitsListVo.getPerformaceSourceVo().getOcrdat() + " " + performaceHitsListVo.getPerformaceSourceVo().getOcrtm()));
                            performaceVo.setPort(performaceHitsListVo.getPerformaceSourceVo().getAid());
                            performaceVo.setUnit(performaceHitsListVo.getPerformaceSourceVo().getBid());
                            performaceVo.setTmper(performaceHitsListVo.getPerformaceSourceVo().getTmper());
                            performaceVo.setRxAve(performaceHitsListVo.getPerformaceSourceVo().getRxAve());
                            performaceVo.setRxCur(performaceHitsListVo.getPerformaceSourceVo().getRxCur());
                            performaceVo.setRxMax(performaceHitsListVo.getPerformaceSourceVo().getRxMax());
                            performaceVo.setRxMin(performaceHitsListVo.getPerformaceSourceVo().getRxMin());
                            performaceVo.setTxAve(performaceHitsListVo.getPerformaceSourceVo().getTxAve());
                            performaceVo.setTxCur(performaceHitsListVo.getPerformaceSourceVo().getTxCur());
                            performaceVo.setTxMax(performaceHitsListVo.getPerformaceSourceVo().getTxMax());
                            performaceVo.setTxMin(performaceHitsListVo.getPerformaceSourceVo().getTxMin());

                            performaceVoList.add(performaceVo);
                        }
                    }
                }
            }

            if(performaceVoList != null && performaceVoList.size() > 0){
                tmpPerformaceList = new ArrayList<PerformaceVo>();
                paramMap = new HashMap<String, Object>();

                if(performaceVoList.size() > 100){

                    if((performaceVoList.size() % 100) == 0){
                        totalCycle = performaceVoList.size() / 100;
                    }else{
                        totalCycle = ((int)Math.floor(performaceVoList.size() / 100))+1;
                    }

                    for(PerformaceVo performace : performaceVoList){
                        tmpPerformaceList.add(performace);

                        if(i == totalCycle){
                            continue;
                        }else{
                            if(tmpPerformaceList.size() == 100){
                                paramMap.put("performaceVoList", tmpPerformaceList);

                                try {
                                    performaceMapper.insertPerformace(paramMap);
                                }catch (Exception e){
                                    LOGGER.error(">>>>>>>>>>>> PerformanceService setPerformanceData() error : "+ ExceptionUtils.getStackTrace(e)+" <<<<<<<<<<<<<<<<");
                                }

                                tmpPerformaceList.clear();
                                paramMap.clear();
                                i++;
                            }
                        }
                    }

                    if(tmpPerformaceList != null && tmpPerformaceList.size() > 0){
                        paramMap.put("performaceVoList", tmpPerformaceList);

                        try {
                            performaceMapper.insertPerformace(paramMap);
                        }catch (Exception e){
                            LOGGER.error(">>>>>>>>>>>> PerformanceService setPerformanceData() error : "+ ExceptionUtils.getStackTrace(e)+" <<<<<<<<<<<<<<<<");
                        }
                    }
                }else{
                    paramMap.put("performaceVoList", performaceVoList);

                    try {
                        performaceMapper.insertPerformace(paramMap);
                    }catch (Exception e){
                        LOGGER.error(">>>>>>>>>>>> PerformanceService setPerformanceData() error : "+ ExceptionUtils.getStackTrace(e)+" <<<<<<<<<<<<<<<<");
                    }
                }
                tmpPerformaceList.clear();
                paramMap.clear();

                performaceMapper.updatePerformaceCollectTime(performaceCollectTimeVo);
            }
        }catch (Exception e){
            LOGGER.error("=====> [PerformanceService] setPerformanceData error "+ ExceptionUtils.getStackTrace(e)+ "<=====");
        }
    }
}
