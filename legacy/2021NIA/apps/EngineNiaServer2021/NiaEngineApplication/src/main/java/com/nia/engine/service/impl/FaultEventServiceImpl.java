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
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service("FaultEventService")
@Profile("real")
public class FaultEventServiceImpl implements FaultEventService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FaultEventService.class);

    @Autowired
	private FaultEventMapper faultEventMapper;

    @Autowired
	private SFTPSession sftpSession;

    @Value("${spring.ftp.file-path}")
    private String uploadPath;

    @Value("${spring.ftp.local-file-path}")
    private String localUploadPath;

    @Value("${spring.ftp.host1}")
    private String host1 = null;

    @Value("${spring.ftp.host2}")
    private String host2 = null;

    @Value("${spring.ftp.port}")
    private int port = 0;

    @Value("${spring.ftp.user}")
    private String user = null;

    @Value("${spring.ftp.password}")
    private String pw = null;

    private HashMap<String, String> parameterMap;

    @Override
    public void jsonObjToFile(String faultEventKey) {
        LOGGER.info(">>>>>>>>>>[FaultEventService] jsonObjToFile(" + faultEventKey + ") <<<<<<<<<<<<<<<<<");

        String ftpUpdatePath = uploadPath+"faultEvent/";

        FaultEventVo faultEventVo;
        List<FaultEventAlarmDataVo> alarmVoList = null;
        List<FaultEventPerformanceDataVo> performanceVoList = null;
        List<FaultEventNniTopologyDataVo> nniTopologyVoList = null;
        List<FaultEventUniTopologyDataVo> uniTopologyVoList = null;

        List<FaultEventIpAlarmVo> ipAlarmList = null;
        List<FaultEventPerfVo> ipPerfList = null;
        List<FaultEventSflowLogVo> ipSflowLogList = null;
        List<FaultEventIpCvnmsResourceVo> ipResourceList = null;
        List<FaultEventIpCvnmsResourceIfVo> ipResourceIfList = null;
        ObjectMapper mapper;
        String jsonData;
        File putFile = null;
        File folder = new File(ftpUpdatePath);

        try {

            faultEventVo = faultEventMapper.selectFaultEvent(faultEventKey);

            if(faultEventVo != null){
                switch (faultEventVo.getEventGb()){
                    case "test" :
                        alarmVoList = faultEventMapper.selectFaultEventAlarm(faultEventKey);
                        performanceVoList = faultEventMapper.selectFaultEventPerformance(faultEventKey);
                        ipAlarmList = faultEventMapper.selectFaultEventXeCvnmsError(faultEventKey);
                        ipPerfList = faultEventMapper.selectFaultEventXeCvnmsPerfIf(faultEventKey);
                        ipSflowLogList = faultEventMapper.selectFaultEventXeSflowLog(faultEventKey);

                        break;
                    case "fault" :
                        alarmVoList = faultEventMapper.selectFaultEventAlarm(faultEventKey);
                        ipAlarmList = faultEventMapper.selectFaultEventXeCvnmsError(faultEventKey);

                        break;
                    case "anomalous" :
                    case "ticket-att" :
                    case "ticket-att2" :
                        ipPerfList = faultEventMapper.selectFaultEventXeCvnmsPerfIf(faultEventKey);
//                        ipSflowLogList = faultEventMapper.selectFaultEventXeSflowLog(faultEventKey);

                        break;

                    case "noxious" :
                    case "ticket-ntt" :
//                        ipPerfList = faultEventMapper.selectFaultEventXeCvnmsPerfIf(faultEventKey);
                        ipSflowLogList = faultEventMapper.selectFaultEventXeSflowLog(faultEventKey);

                        break;
                    case "perf" :
                        performanceVoList = faultEventMapper.selectFaultEventPerformance(faultEventKey);

                        break;
                    case "resources" :
                        nniTopologyVoList = faultEventMapper.selectFaultEventNniTopology(faultEventKey);
                        uniTopologyVoList = faultEventMapper.selectFaultEventUniTopology(faultEventKey);
                        ipResourceList = faultEventMapper.selectFaultEventXeCvnmsResource(faultEventKey);
                        ipResourceIfList = faultEventMapper.selectFaultEventCvnmsResourceIf(faultEventKey);

                        break;
                    default:
                        break;
                }

//                if(!"etc".equals(faultEventVo.getEventGb())){
//                    nniTopologyVoList = faultEventMapper.selectFaultEventNniTopology(faultEventKey);
//                    uniTopologyVoList = faultEventMapper.selectFaultEventUniTopology(faultEventKey);
//                    ipResourceList = faultEventMapper.selectFaultEventXeCvnmsResource(faultEventKey);
//                    ipResourceIfList = faultEventMapper.selectFaultEventCvnmsResourceIf(faultEventKey);
//                }


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

                if(ipAlarmList != null && ipAlarmList.size() > 0){
                    faultEventVo.setFaultEventCvnmsErrorList(ipAlarmList);
                }

                if(ipPerfList != null && ipPerfList.size() > 0){
                    faultEventVo.setFaultEventCvnmsPerfList(ipPerfList);
                }

                if(ipSflowLogList != null && ipSflowLogList.size() > 0){
                    faultEventVo.setFaultEventCvnmsSflowList(ipSflowLogList);
                }

                if(ipResourceList != null && ipResourceList.size() > 0){
                    faultEventVo.setFaultEventCvnmsResourceList(ipResourceList);
                }

                if(ipResourceIfList != null && ipResourceIfList.size() > 0){
                    faultEventVo.setFaultEventCvnmsResourceIfList(ipResourceIfList);
                }

                mapper = new ObjectMapper();
                jsonData = mapper.writeValueAsString(faultEventVo);

                putFile = createJsonFile("faultEvent", jsonData, faultEventKey, faultEventVo.getEventGb());

                try {
                    sftpSession.init(host1, port, user, pw);

                    if (putFile != null) {
                        if(!folder.exists()){
                            folder.mkdirs();
                        }

                        sftpSession.upload(ftpUpdatePath, putFile);
                        LOGGER.info("=====> [IpSflowToAiLinkageService] faultEvent upload(" + host1.split("\\.")[3] + ") : " + ftpUpdatePath + putFile.getName() + "<=====");
                    }

                    sftpSession.disconnection();
                } catch (Exception e1) {
                    LOGGER.error("=====> [IpSflowToAiLinkageService] faultEvent upload(" + host1.split("\\.")[3] + ") error() " + ExceptionUtils.getStackTrace(e1) + "<=====");
                }

                try {
                    sftpSession.init(host2, port, user, pw);

                    if (putFile != null) {
                        if(!folder.exists()){
                            folder.mkdirs();
                        }

                        sftpSession.upload(ftpUpdatePath, putFile);
                        LOGGER.info("=====> [FaultEventService] faultEvent upload(" + host2.split("\\.")[3] + ") : " + ftpUpdatePath + putFile.getName() + "<=====");
                    }

                    sftpSession.disconnection();
                } catch (Exception e1) {
                    LOGGER.error("=====> [FaultEventService] faultEvent upload(" + host2.split("\\.")[3] + ") error() " + ExceptionUtils.getStackTrace(e1) + "<=====");
                }

                if(putFile.exists()){
                    putFile.delete();
                }
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
    public File createJsonFile(String eventType, String jsonData, String dataKey, String titleName) {
        LOGGER.info(">>>>>>>>>>[FaultEventService] createJsonFile(" + eventType + ") <<<<<<<<<<<<<<<<<");
        File putFile = null;
        File folder = new File(localUploadPath+"/"+eventType);

        BufferedWriter output;
        PrintWriter pw;

        try{

            if(dataKey.contains("+")){
                dataKey = dataKey.substring(0,dataKey.indexOf("+"));
            }

            if(!folder.exists()){
                folder.mkdirs();
            }

            putFile = new File(folder.getPath()+"/"+eventType+"_"+UtlDateHelper.getCurrentDate()+"_"+titleName+"_"+dataKey+".json");

            if(!putFile.isFile()){
                putFile.createNewFile();
            }

            output  = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(putFile), StandardCharsets.UTF_8));
            pw = new PrintWriter(output,true);
            pw.write(jsonData);
            pw.flush();

            LOGGER.info(">>>>>>>>>>[FaultEventService] createJsonFile(" + (putFile != null ? putFile.getPath() : null) + ") <<<<<<<<<<<<<<<<<");

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
	public void insertFaultEvent(String startTime, String endTime, String title, String eventGb){
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
                parameterMap.put("eventGb", eventGb);

                try {
                    faultEventMapper.insertFaultEvent(parameterMap);
                }catch (Exception e){
                    LOGGER.error("=====> [FaultEventService] insertFaultEvent(insertFaultEvent) error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
                }

                switch (eventGb){
                    case "test" :
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
                            faultEventMapper.insertFaultEventXeCvnmsError(parameterMap);
                        }catch (Exception e){
                            LOGGER.error("=====> [FaultEventService] insertFaultEvent(insertFaultEventXeCvnmsError) error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
                        }

                        try {
                            faultEventMapper.insertFaultEventXeCvnmsPerfIf(parameterMap);
                        }catch (Exception e){
                            LOGGER.error("=====> [FaultEventService] insertFaultEvent(insertFaultEventXeCvnmsPerfIf) error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
                        }

                        try {
                            faultEventMapper.insertFaultEventXeSflowLog(parameterMap);
                        }catch (Exception e){
                            LOGGER.error("=====> [FaultEventService] insertFaultEvent(insertFaultEventXeSflowLog) error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
                        }

                        break;
                    case "fault" :
                        try {
                            faultEventMapper.insertFaultEventAlarmCurMst(parameterMap); //
                        }catch (Exception e){
                            LOGGER.error("=====> [FaultEventService] insertFaultEvent(insertFaultEventAlarmCurMst) error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
                        }

                        try {
                            faultEventMapper.insertFaultEventXeCvnmsError(parameterMap); //
                        }catch (Exception e){
                            LOGGER.error("=====> [FaultEventService] insertFaultEvent(insertFaultEventXeCvnmsError) error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
                        }

                        break;
                    case "anomalous" :
                    case "ticket-att" :
                    case "ticket-att2" :
                        try {
                            faultEventMapper.insertFaultEventXeCvnmsPerfIf(parameterMap); //
                        }catch (Exception e){
                            LOGGER.error("=====> [FaultEventService] insertFaultEvent(insertFaultEventXeCvnmsPerfIf) error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
                        }
//
//                        try {
//                            faultEventMapper.insertFaultEventXeSflowLog(parameterMap);
//                        }catch (Exception e){
//                            LOGGER.error("=====> [FaultEventService] insertFaultEvent(insertFaultEventXeSflowLog) error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
//                        }

                        break;
                    case "noxious" :
                    case "ticket-ntt" :
//                        try {
//                            faultEventMapper.insertFaultEventXeCvnmsPerfIf(parameterMap);
//                        }catch (Exception e){
//                            LOGGER.error("=====> [FaultEventService] insertFaultEvent(insertFaultEventXeCvnmsPerfIf) error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
//                        }

                        try {
                            faultEventMapper.insertFaultEventXeSflowLog(parameterMap); //
                        }catch (Exception e){
                            LOGGER.error("=====> [FaultEventService] insertFaultEvent(insertFaultEventXeSflowLog) error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
                        }

                        break;
                    case "perf" :
                        try {
                            faultEventMapper.insertFaultEventAiPerformanceMst(parameterMap); // RCA.TB_FAULT_EVENT_PERFORMACE_MST
                        }catch (Exception e){
                            LOGGER.error("=====> [FaultEventService] insertFaultEvent(insertFaultEventAiPerformanceMst) error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
                        }

                        break;
                    case "resources" :
                        try {
                            faultEventMapper.insertFaultEventXeCvnmsResource(parameterMap); // RCA.TB_FAULT_EVENT_XE_CVNMS_RESOURCE
                        }catch (Exception e){
                            LOGGER.error("=====> [FaultEventService] insertFaultEvent(insertFaultEventXeCvnmsResource) error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
                        }

                        try {
                            faultEventMapper.insertFaultEventCvnmsResourceIf(parameterMap); // RCA.TB_FAULT_EVENT_XE_CVNMS_RESOURCE_IF
                        }catch (Exception e){
                            LOGGER.error("=====> [FaultEventService] insertFaultEvent(insertFaultEventCvnmsResourceIf) error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
                        }

                        try {
                            faultEventMapper.insertFaultEventNniTopology(parameterMap); // RCA.TB_FAULT_EVENT_NNI_TOPOLOGY
                        }catch (Exception e){
                            LOGGER.error("=====> [FaultEventService] insertFaultEvent(insertFaultEventNniTopology) error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
                        }

                        try {
                            faultEventMapper.insertFaultEventUniTopology(parameterMap); // RCA.TB_FAULT_EVENT_UNI_TOPOLOGY
                        }catch (Exception e){
                            LOGGER.error("=====> [FaultEventService] insertFaultEvent(insertFaultEventUniTopology) error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
                        }

                        break;
                    default:
                        break;
                }

//                if(!"etc".equals(eventGb)){
//                    try {
//                        faultEventMapper.insertFaultEventXeCvnmsResource(parameterMap);
//                    }catch (Exception e){
//                        LOGGER.error("=====> [FaultEventService] insertFaultEvent(insertFaultEventXeCvnmsResource) error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
//                    }
//
//                    try {
//                        faultEventMapper.insertFaultEventCvnmsResourceIf(parameterMap);
//                    }catch (Exception e){
//                        LOGGER.error("=====> [FaultEventService] insertFaultEvent(insertFaultEventCvnmsResourceIf) error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
//                    }
//
//                    try {
//                        faultEventMapper.insertFaultEventNniTopology(parameterMap);
//                    }catch (Exception e){
//                        LOGGER.error("=====> [FaultEventService] insertFaultEvent(insertFaultEventNniTopology) error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
//                    }
//
//                    try {
//                        faultEventMapper.insertFaultEventUniTopology(parameterMap);
//                    }catch (Exception e){
//                        LOGGER.error("=====> [FaultEventService] insertFaultEvent(insertFaultEventUniTopology) error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
//                    }
//                }

                jsonObjToFile(eventNo);
            }
        }catch (Exception e){
            LOGGER.error("=====> [FaultEventService] insertFaultEvent error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
        }
	}
}
