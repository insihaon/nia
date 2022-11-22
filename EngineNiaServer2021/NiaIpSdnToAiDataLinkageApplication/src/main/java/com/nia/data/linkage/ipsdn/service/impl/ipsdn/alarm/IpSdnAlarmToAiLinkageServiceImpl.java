package com.nia.data.linkage.ipsdn.service.impl.ipsdn.alarm;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nia.data.linkage.ipsdn.common.SFTPSession;
import com.nia.data.linkage.ipsdn.mapper.common.CommonMapper;
import com.nia.data.linkage.ipsdn.mapper.ipsdn.IpsdnDataMapper;
import com.nia.data.linkage.ipsdn.service.ipsdn.alarm.IpSdnAlarmToAiLinkageService;
import com.nia.data.linkage.ipsdn.vo.ipsdn.alarm.AlarmDataListVo;
import com.nia.data.linkage.ipsdn.vo.ipsdn.alarm.AlarmDataVo;
import com.nia.data.linkage.ipsdn.vo.ipsdn.syslog.SyslogDataVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.NoSuchElementException;

@Service("IpSdnAlarmToAiLinkageService")
public class IpSdnAlarmToAiLinkageServiceImpl implements IpSdnAlarmToAiLinkageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(IpSdnAlarmToAiLinkageServiceImpl.class);

    @Autowired
    private IpsdnDataMapper ipsdnDataMapper;

    @Autowired
    private CommonMapper commonMapper;

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<AlarmDataListVo> ipSdnAlarmListVoObjectFactory;

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<SFTPSession> sftpSessionObjectFactory;

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

    @Value("${spring.profiles}")
    private String profiles;

    @Override
    public void sendAlarmData(){
        LOGGER.info("==========>[IpSdnAlarmToAiLinkageService] sendSyslogData <==============");
        SFTPSession sftpSession;

        String dataKey = null;
        String jsonData;
        String ftpUpdatePath = uploadPath+"ipSdnSyslogAlarm/";
        ArrayList<AlarmDataVo> alarmDataVoList;
        long fileSize;

        ObjectMapper mapper;
        File putFile = null;
        File folder = new File(ftpUpdatePath);

        HashMap<String, String> strHashMap;

        AlarmDataListVo alarmDataListVo;
        AlarmDataVo maxAlarmDataVo;
        try {
            Thread.sleep(15*1000);

            dataKey = commonMapper.selectLinkageYdKey("aiIpSdnSyslogAlarmKey");
            LOGGER.info("==========>[IpSdnAlarmToAiLinkageService] sendAlarmData dataKey : "+dataKey+" <==============");

            if (StringUtils.isNotEmpty(dataKey)){
                alarmDataVoList = ipsdnDataMapper.selectAlarmList(Integer.parseInt(dataKey));

                if (alarmDataVoList != null && alarmDataVoList.size() > 0){
                    LOGGER.info("==========>[IpSdnAlarmToAiLinkageService] sendAlarmData alarmDataVoList("+alarmDataVoList.size() +") <==============");

                    alarmDataListVo = ipSdnAlarmListVoObjectFactory.getObject();
                    alarmDataListVo.setData(alarmDataVoList);
                    mapper = new ObjectMapper();
                    jsonData = mapper.writeValueAsString(alarmDataListVo);

                    putFile = createJsonFile("ipSdnSyslogAlarm", jsonData, alarmDataVoList.get(alarmDataVoList.size()-1).getAlarmno()+"", ftpUpdatePath);

                    sftpSession = sftpSessionObjectFactory.getObject();

                    try {
                        sftpSession.init(host1, port, user, pw);

                        if (putFile != null) {
                            if(!folder.exists()){
                                folder.mkdirs();
                            }

                            sftpSession.upload(ftpUpdatePath, putFile);
                            LOGGER.info("=====> [IpSdnAlarmToAiLinkageService] sendAlarmData upload(" + host1.split("\\.")[3] + ") : " + ftpUpdatePath + putFile.getName() + "<=====");
                        }

                    }catch (Exception e){
                        LOGGER.error("=====> [IpSdnAlarmToAiLinkageService] sendAlarmData upload(" + host1.split("\\.")[3] + ") error() " + ExceptionUtils.getStackTrace(e) + "<=====");
                    }


                    try {
                        sftpSession.init(host2, port, user, pw);

                        if (putFile != null) {
                            if(!folder.exists()){
                                folder.mkdirs();
                            }

                            sftpSession.upload(ftpUpdatePath, putFile);
                            LOGGER.info("=====> [IpSdnAlarmToAiLinkageService] sendAlarmData upload(" + host2.split("\\.")[3] + ") : " + ftpUpdatePath + putFile.getName() + "<=====");
                        }

                        sftpSession.disconnection();
                    } catch (Exception e1) {
                        LOGGER.error("=====> [IpSdnAlarmToAiLinkageService] sendAlarmData upload(" + host2.split("\\.")[3] + ") error() " + ExceptionUtils.getStackTrace(e1) + "<=====");
                    }

                    Comparator<AlarmDataVo> comparatorById = Comparator.comparingInt(AlarmDataVo::getAlarmno);
                    maxAlarmDataVo = alarmDataVoList.stream()
                            .max(comparatorById)
                            .orElseThrow(NoSuchElementException::new);


                    strHashMap = new HashMap<>();
                    strHashMap.put("key", "aiIpSdnSyslogAlarmKey");
                    strHashMap.put("value", maxAlarmDataVo.getAlarmno()+"");
                    commonMapper.updateLinkageYdKey(strHashMap);


                    if(putFile.exists()){
                        fileSize = (putFile.length()) / 1024;

                        strHashMap = new HashMap<>();
                        strHashMap.put("key", "aiIpSdnSyslogAlarmKey");
                        strHashMap.put("fileName", putFile.getName());
                        strHashMap.put("fileSize", fileSize+"");
                        strHashMap.put("rowCnt", alarmDataVoList.size()+"");

                        commonMapper.insertLinkageHist(strHashMap);

                        putFile.delete();
                    }

                }
            }





        }catch (Exception e){
            LOGGER.error("=====> [IpSdnAlarmToAiLinkageService] sendAlarmData error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
        }
    }
    public File createJsonFile(String eventType, String jsonData, String dataKey, String ftpUpdatePath){
        LOGGER.info(">>>>>>>>>>[IpSdnAlarmToAiLinkageService] createJsonFile(" + eventType + ") <<<<<<<<<<<<<<<<<");
        File putFile = null;
        File folder = new File(localUploadPath+eventType);

        BufferedWriter output;
        PrintWriter pw;

        try{
            if(!folder.exists()){
                folder.mkdirs();
            }

            putFile = new File(folder.getPath()+"/"+eventType+"_"+dataKey+""+".json");

            if(!putFile.isFile()){
                putFile.createNewFile();
            }

            output  = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(putFile), StandardCharsets.UTF_8));
            pw = new PrintWriter(output,true);
            pw.write(jsonData);
            pw.flush();

            LOGGER.info(">>>>>>>>>>[IpSdnAlarmToAiLinkageService] createJsonFile(" + (putFile != null ? putFile.getPath() : null) + ") <<<<<<<<<<<<<<<<<");

            if (pw != null) {
                pw.close();
            }
        }catch (Exception e){
            LOGGER.error("=====> [IpSdnAlarmToAiLinkageService] createJsonFile error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
        }
        return putFile;
    }
}
