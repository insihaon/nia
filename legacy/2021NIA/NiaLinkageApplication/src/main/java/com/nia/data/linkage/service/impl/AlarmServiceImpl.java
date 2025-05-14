package com.nia.data.linkage.service.impl;

import com.nia.data.linkage.common.UtlCommon;
import com.nia.data.linkage.common.UtlDateHelper;
import com.nia.data.linkage.mapper.AlarmMapper;
import com.nia.data.linkage.service.AlarmService;
import com.nia.data.linkage.vo.alarm.AlarmVo;
import com.nia.data.linkage.vo.alarm.json.AlarmHitsDataVo;
import com.nia.data.linkage.vo.alarm.json.AlarmHitsHitsDataVo;
import com.nia.data.linkage.vo.alarm.json.AlarmHitsVo;
import com.nia.data.linkage.vo.topology.TopologyVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

@Service("AlarmService")
public class AlarmServiceImpl implements AlarmService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AlarmServiceImpl.class);

    @Autowired
    @Qualifier("NIA_RestTemplate")
    private RestTemplate restTemplate;

    @Autowired
    private AlarmMapper alarmMapper;
//
//    @Autowired
//    private AlarmPrdAmqp alarmPrdAmqp;

    @Value("${spring.nia.api.alarm-url}")
    private String alarmUrl;

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<AlarmHitsDataVo> alarmHitsDataVoFactory;

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<AlarmVo> alarmVoFactory;

    @Override
    public void getAlarmData() {
        LOGGER.info("==========>[AlarmService] getAlarmData <==============");
        ResponseEntity<String> responseEntity;
        String requestData;
        Object obj;
        String msg;
        HttpHeaders httpHeaders;
        AlarmHitsDataVo alarmHitsDataVo;
        String fromDate;
        String toDate;

        try {
            fromDate = UtlDateHelper.getCurrentDate();

            Timestamp original = new Timestamp(UtlDateHelper.getCurrentTime().getTime());
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(original.getTime());
            cal.add(Calendar.DATE, 1);

            SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-MM-dd");
		    toDate = dayTime.format(cal.getTime());

            LOGGER.info("==========>[AlarmService] restTemplate check : " + restTemplate);

            List<HttpMessageConverter<?>> converters = restTemplate.getMessageConverters();
            for (HttpMessageConverter<?> converter : converters) {
                System.out.println(">>> Converter check: " + (converter == null ? "NULL" : converter.getClass().getName()));
            }

            restTemplate.getMessageConverters()
                        .add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));

            try {
                // 쿼리에서 특수 문자를 인코딩
                String query = URLEncoder.encode("date:[" + fromDate + " TO " + toDate + "] AND almType:\"alm\"", "UTF-8");

                // 완성된 URL
                String fullUrl = alarmUrl + "_search?q=" + query + "&size=1000&pretty";

                // 요청 전송
                responseEntity = restTemplate.exchange(fullUrl, HttpMethod.GET, null, String.class);

//                responseEntity = restTemplate.exchange(alarmUrl+"_search?q=(date["+fromDate+"+TO+"+toDate+"]) AND almType:\"alm\"&size=1000&pretty", HttpMethod.GET, null, String.class);

//                LOGGER.info("==========>[AlarmService] getAlarmData result: "+ responseEntity.getBody() +"<==============");
            }catch (ResourceAccessException rae){
                Thread.sleep(3000);
                restTemplate.getMessageConverters()
                            .add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));

                responseEntity = restTemplate.exchange(alarmUrl, HttpMethod.GET, null, String.class);
            }

            msg = new String(responseEntity.getBody());
            alarmHitsDataVo = alarmHitsDataVoFactory.getObject();

            obj = UtlCommon.jsonToObject(alarmHitsDataVo, msg);

            alarmHitsDataVo = (AlarmHitsDataVo)obj;
            if(alarmHitsDataVo != null){
                AlarmHitsHitsDataVo hists = alarmHitsDataVo.getAlarmHitsHitsDataVo();
                if(hists != null){
                    List<AlarmHitsVo> histList = hists.getAlarmHitsVo();
                    if(histList != null){
                        LOGGER.info("==========>[AlarmService] histList size : " + histList.size());
                        setAlarmData(histList);
                    }else{
                        LOGGER.info("==========>[AlarmService] histList null ");
                    }
                }else{
                    LOGGER.info("==========>[AlarmService] hists null ");
                }
            }else{
                LOGGER.info("==========>[AlarmService] alarmHitsDataVo null ");
            }


        }catch (Exception e){
            LOGGER.error("=====> [AlarmService] getAlarmData error "+ ExceptionUtils.getStackTrace(e)+ "<=====");
        }
    }

    @Override
    public void setAlarmData(List<AlarmHitsVo> histList) {
        LOGGER.info("==========>[AlarmService] setAlarmData <==============");
        AlarmVo alarmVo;
        List<AlarmVo> alarmVoList = null;
        HashMap<String, Object> paramMap;
        String elkAlarmId = null;

        try {
            if(histList != null && histList.size() > 0){
                alarmVoList = new ArrayList<AlarmVo>();
                for(AlarmHitsVo alarmHitsVo : histList){
                    switch (alarmHitsVo.getAlarmVo().getAlmType()){
                       // case "alm_eqpt":
                        case "alm" :

                            elkAlarmId = alarmMapper.selectElkAlarmCheck(alarmHitsVo.getId());

                            if(StringUtils.isEmpty(elkAlarmId)){
                                alarmVo = alarmVoFactory.getObject();
                                alarmVo.setElkAlarmId(alarmHitsVo.getId());
                                alarmVo.setAlarmlevel(setAlarmLvl(alarmHitsVo.getAlarmVo().getSev()));
                                alarmVo.setAlarmloc(alarmHitsVo.getAlarmVo().getAid());
                                alarmVo.setAlarmmsg(alarmHitsVo.getAlarmVo().getLogType());
//                                switch (alarmHitsVo.getAlarmVo().getAlmType()) {
////                                    case "alm_eqpt":
////                                        alarmVo.setAlarmtime(UtlDateHelper.stringToTimestamp(alarmHitsVo.getAlarmVo().getOcrdat() + " " + alarmHitsVo.getAlarmVo().getOcrtm()));
////                                        alarmVo.setUnit(alarmHitsVo.getAlarmVo().getBid());
////                                        break;
//                                    case "alm":
//                                        alarmVo.setReceivetime(UtlDateHelper.stringToTimestamp(alarmHitsVo.getAlarmVo().getDate()));
//                                        alarmVo.setAlarmtime(UtlDateHelper.stringToTimestamp(alarmHitsVo.getAlarmVo().getDate()));
//                                        alarmVo.setUnit(alarmHitsVo.getAlarmVo().getUnit());
//                                        break;
//                                }
                                alarmVo.setReceivetime(UtlDateHelper.stringToTimestamp(alarmHitsVo.getAlarmVo().getDate()));
                                alarmVo.setAlarmtime(UtlDateHelper.stringToTimestamp(alarmHitsVo.getAlarmVo().getDate()));
                                alarmVo.setUnit(alarmHitsVo.getAlarmVo().getUnit());
                                alarmVo.setDescription(alarmHitsVo.getAlarmVo().getDescription());
                                alarmVo.setSysname(alarmHitsVo.getAlarmVo().getSource());

                                if(alarmHitsVo.getAlarmVo().getProviderId().contains("potn")){
                                    alarmVo.setEquiptype("POTN");
                                }

                                LOGGER.info("==========>[AlarmService] insert alarmVO : " + alarmVo);
                                alarmVoList.add(alarmVo);
                                break;
                            }
                    }
                }
            }

            if(alarmVoList != null && alarmVoList.size() > 0){
                paramMap = new HashMap<String, Object>();
                paramMap.put("alarmVoList", alarmVoList);
                alarmMapper.insertAlarm(paramMap);
                alarmMapper.insertAlarmCheck(paramMap);
            }
        }catch (Exception e){
            LOGGER.error("=====> [AlarmService] setAlarmData error "+ ExceptionUtils.getStackTrace(e)+ "<=====");
        }
    }

    @Override
    public String setAlarmLvl(String level) {
        String resultLevel = "0";

        switch (level){
            case "CL" :
                resultLevel = "1";
                break;
            case "WN" :
                resultLevel = "3";
                break;
            case "MN" :
                resultLevel = "4";
                break;
            case "MJ" :
                resultLevel = "5";
                break;
            case "CR" :
                resultLevel = "7";
                break;
        }
        return resultLevel;
    }
}
