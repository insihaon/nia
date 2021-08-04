package com.nia.engine.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nia.engine.common.SFTPSession;
import com.nia.engine.common.UtlDateHelper;
import com.nia.engine.mapper.FaultEventMapper;
import com.nia.engine.service.FaultEventService;
import com.nia.engine.vo.RcaEngineResult;
import com.nia.engine.vo.falutEvent.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;

@Service("FaultEventService")
public class FaultEventServiceImpl implements FaultEventService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FaultEventService.class);

    @Autowired
	private FaultEventMapper faultEventMapper;

    @Autowired
	private SFTPSession sftpSession;

    @Value("${spring.ftp.nia-file-path}")
    private String path;

    @Value("${spring.ftp.file-path}")
    private String uploadPath;

    private HashMap<String, String> parameterMap;

    @Override
    public void jsonObjToFile(String faultEventKey) {
        LOGGER.info(">>>>>>>>>>[FaultEventService] jsonObjToFile(" + faultEventKey + ") <<<<<<<<<<<<<<<<<");

        FaultEventVo faultEventVo;
        List<FaultEventAlarmDataVo> alarmVoList;
        List<FaultEventPerformanceDataVo> performanceVoList;
        List<FaultEventNniTopologyDataVo> nniTopologyVoList;
        List<FaultEventUniTopologyDataVo> uniTopologyVoList;
        ObjectMapper mapper;
        String jsonData;
        File putFile = null;

        try {

            faultEventVo = faultEventMapper.selectFaultEvent(faultEventKey);

            if(faultEventVo != null){
                alarmVoList = faultEventMapper.selectFaultEventAlarm(faultEventKey);
                performanceVoList = faultEventMapper.selectFaultEventPerformance(faultEventKey);
                nniTopologyVoList = faultEventMapper.selectFaultEventNniTopology(faultEventKey);
                uniTopologyVoList = faultEventMapper.selectFaultEventUniTopology(faultEventKey);

                if(alarmVoList != null && alarmVoList.size() > 0){
                    faultEventVo.setFaultEventAlarmList(alarmVoList);
                }

                if(performanceVoList != null && performanceVoList.size() > 0){
                    faultEventVo.setFaultEventPerformanceList(performanceVoList);
                }

                if(nniTopologyVoList != null && nniTopologyVoList.size() > 0){
                    faultEventVo.setFaultEventNniTopologyList(nniTopologyVoList);
                }

                if(uniTopologyVoList != null && uniTopologyVoList.size() > 0){
                    faultEventVo.setFaultEventUniTopologyList(uniTopologyVoList);
                }

                mapper = new ObjectMapper();
                jsonData = mapper.writeValueAsString(faultEventVo);

                putFile = createJsonFile("faultEvent", jsonData, faultEventKey);

                sftpSession.init();

                if(putFile != null){
                    sftpSession.upload(uploadPath, putFile);
                }

                sftpSession.disconnection();
            }

            LOGGER.info(">>>>>>>>>>[FaultEventService] jsonObjToFile putFile(" + (putFile != null ? putFile.getPath() : null) + ") <<<<<<<<<<<<<<<<<");

        }catch (Exception e){
            LOGGER.error("=====> [FaultEventService] jsonObjToFile error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
            if(sftpSession != null){
                sftpSession.disconnection();
            }
        }
    }

    @Override
    public File createJsonFile(String eventType, String jsonData, String faultEventKey) {
        LOGGER.info(">>>>>>>>>>[FaultEventService] createJsonFile(" + eventType + ") <<<<<<<<<<<<<<<<<");
        File putFile = null;
        BufferedWriter output;
        PrintWriter pw;
        String createFilePath = null;

        try{
            putFile = new File(path+eventType+"_"+ UtlDateHelper.getCurrentDate()+"_"+faultEventKey+".json");

            if(!putFile.isFile()){
				putFile.createNewFile();
			}

			output  = new BufferedWriter(new FileWriter(putFile,true));
			pw = new PrintWriter(output,true);
			pw.write(jsonData);
			pw.flush();

			LOGGER.info(">>>>>>>>>>[FaultEventService] createJsonFile putFile(" + (putFile != null ? putFile.getPath() : null) + ") <<<<<<<<<<<<<<<<<");

			if (pw != null) {
				pw.close();
			}
        }catch (Exception e){
            LOGGER.error("=====> [FaultEventService] createJsonFile error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
        }
        return putFile;
    }

    /**
	 * 장애 이벤트 데이터 저장
	 * @param
	 * @return
	 */
	@Override
	public void insertFaultEvent(String startTime, String endTime, String title){
	    LOGGER.info(">>>>>>>>>>[FaultEventService] insertFaultEvent startTime : "+startTime+" / endTime : "+endTime+" / title : "+title+" <<<<<<<<<<<<<<<<<");
	    String eventNo;

        try {
            if(StringUtils.isNotEmpty(startTime) && StringUtils.isNotEmpty(endTime)){
                parameterMap = new HashMap<String, String>();
                eventNo = faultEventMapper.selectFaultEventKey();

                parameterMap.put("eventno", eventNo);
                parameterMap.put("title", title);
                parameterMap.put("startTime", startTime);
                parameterMap.put("endTime", endTime);

                try {
                    faultEventMapper.insertFaultEvent(parameterMap);
                }catch (Exception e){
                    LOGGER.error("=====> [FaultEventService] insertFaultEvent(insertFaultEvent) error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
                }

                try {
                    faultEventMapper.insertFaultEventAlarmCurMst(parameterMap);
                }catch (Exception e){
                    LOGGER.error("=====> [FaultEventService] insertFaultEvent(insertFaultEventAlarmCurMst) error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
                }

                try {
                    faultEventMapper.insertFaultEventAiPerformanceMst(parameterMap);
                }catch (Exception e){
                    LOGGER.error("=====> [FaultEventService] insertFaultEvent(insertFaultEventAiPerformanceMst) error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
                }

                try {
                    faultEventMapper.insertFaultEventNniTopology(parameterMap);
                }catch (Exception e){
                    LOGGER.error("=====> [FaultEventService] insertFaultEvent(insertFaultEventNniTopology) error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
                }

                try {
                    faultEventMapper.insertFaultEventUniTopology(parameterMap);
                }catch (Exception e){
                    LOGGER.error("=====> [FaultEventService] insertFaultEvent(insertFaultEventUniTopology) error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
                }

                jsonObjToFile(eventNo);
            }
        }catch (Exception e){
            LOGGER.error("=====> [FaultEventService] insertFaultEvent error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
        }
	}
}
