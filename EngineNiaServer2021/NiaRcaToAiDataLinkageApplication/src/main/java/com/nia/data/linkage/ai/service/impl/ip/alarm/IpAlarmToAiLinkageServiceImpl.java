package com.nia.data.linkage.ai.service.impl.ip.alarm;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcraft.jsch.JSchException;
import com.nia.data.linkage.ai.common.SFTPSession;
import com.nia.data.linkage.ai.common.UtlDateHelper;
import com.nia.data.linkage.ai.mapper.common.CommonMapper;
import com.nia.data.linkage.ai.mapper.ip.IpDataMapper;
import com.nia.data.linkage.ai.service.ip.alarm.IpAlarmToAiLinkageService;
import com.nia.data.linkage.ai.service.ip.sflow.IpSflowToAiLinkageService;
import com.nia.data.linkage.ai.vo.ip.alarm.IpAlarmListVo;
import com.nia.data.linkage.ai.vo.ip.alarm.IpAlarmVo;
import com.nia.data.linkage.ai.vo.ip.sflow.SflowListVo;
import com.nia.data.linkage.ai.vo.ip.sflow.SflowLogVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

@Service("IpAlarmToAiLinkageService")
public class IpAlarmToAiLinkageServiceImpl implements IpAlarmToAiLinkageService {
    private static final Logger LOGGER = LoggerFactory.getLogger(IpSflowToAiLinkageService.class);

    @Autowired
    private CommonMapper commonMapper;

    @Autowired
    private IpDataMapper ipDataMapper;

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<IpAlarmListVo> ipAlarmListVoObjectFactory;

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
    public void sendAlarmData() {
        LOGGER.info("==========>[IpAlarmToAiLinkageService] sendAlarmData <==============");
        SFTPSession sftpSession;

        String dataKey = null;
        String jsonData;
        String ftpUpdatePath = uploadPath+"xe_cvnms_error/";
        long fileSize;

        ArrayList<IpAlarmVo> ipAlarmVoList = null;
        HashMap<String, String> strHashMap;

        ObjectMapper mapper;
        File putFile = null;
        File folder = new File(ftpUpdatePath);

        IpAlarmListVo ipAlarmListVo;

        try {

            dataKey = commonMapper.selectLinkageYdKey("aiIpAlarmKey");

            LOGGER.info("==========>[IpAlarmToAiLinkageService] sendAlarmData dataKey : "+dataKey+" <==============");

            if(StringUtils.isNotEmpty(dataKey)){
                ipAlarmVoList = ipDataMapper.selectAlarmList(Integer.parseInt(dataKey));


                if(ipAlarmVoList != null && ipAlarmVoList.size() > 0) {
                    LOGGER.info("==========>[IpAlarmToAiLinkageService] sendAlarmData ipAlarmVoList("+ipAlarmVoList.size() +") <==============");

                    ipAlarmListVo = ipAlarmListVoObjectFactory.getObject();
                    ipAlarmListVo.setData(ipAlarmVoList);

                    mapper = new ObjectMapper();
                    jsonData = mapper.writeValueAsString(ipAlarmListVo);

                    putFile = createJsonFile("xe_cvnms_error", jsonData, ipAlarmVoList.get(ipAlarmVoList.size()-1).getInterridx()+"", ftpUpdatePath);

                    sftpSession = sftpSessionObjectFactory.getObject();

                    if(!"codej".equals(profiles)) {
                        try {
                            sftpSession.init(host1, port, user, pw);

                            if (putFile != null) {
                                if(!folder.exists()){
                                    folder.mkdirs();
                                }
                                sftpSession.upload(ftpUpdatePath, putFile);
                                LOGGER.info("=====> [IpAlarmToAiLinkageService] sendAlarmData upload(" + host1.split("\\.")[3] + ") : " + ftpUpdatePath + putFile.getName() + "<=====");
                            }

                            sftpSession.disconnection();
                        } catch (Exception e1) {
                            LOGGER.error("=====> [IpAlarmToAiLinkageService] sendAlarmData upload(" + host1.split("\\.")[3] + ") error() " + ExceptionUtils.getStackTrace(e1) + "<=====");
                        }

                        try {
                            sftpSession.init(host2, port, user, pw);

                            if (putFile != null) {
                                if(!folder.exists()){
                                    folder.mkdirs();
                                }

                                sftpSession.upload(ftpUpdatePath, putFile);
                                LOGGER.info("=====> [IpAlarmToAiLinkageService] sendAlarmData upload(" + host2.split("\\.")[3] + ") : " + ftpUpdatePath + putFile.getName() + "<=====");
                            }

                            sftpSession.disconnection();
                        } catch (Exception e1) {
                            LOGGER.error("=====> [IpAlarmToAiLinkageService] sendAlarmData upload(" + host2.split("\\.")[3] + ") error() " + ExceptionUtils.getStackTrace(e1) + "<=====");
                        }
                    }

                    if("codej".equals(profiles)){
                          try {
                            sftpSession.init("10.81.192.18", 22, "aifactory", "dpdldkdl12!@");

                            if(putFile != null){
                                sftpSession.upload("/home/aifactory/zerooneai/data/xe_cvnms_error/", putFile);
                                LOGGER.info("=====> [IpAlarmToAiLinkageService] sendAlarmData upload(10.81.192.18) : " + ftpUpdatePath+putFile.getName()+ "<=====");
                            }

                            sftpSession.disconnection();
                        }catch (Exception e1){
                            LOGGER.error("=====> [IpAlarmToAiLinkageService] sendAlarmData upload(10.81.192.18) error() "+ ExceptionUtils.getStackTrace(e1)+ "<=====");
                        }
                    }

                    strHashMap = new HashMap<>();
                    strHashMap.put("key", "aiIpAlarmKey");
                    strHashMap.put("value", ipAlarmVoList.get(ipAlarmVoList.size()-1).getInterridx()+"");
                    commonMapper.updateLinkageYdKey(strHashMap);

                    if(putFile.exists()){
                        fileSize = (putFile.length()) / 1024;

                        strHashMap = new HashMap<>();
                        strHashMap.put("key", "aiIpAlarmKey");
                        strHashMap.put("fileName", putFile.getName());
                        strHashMap.put("fileSize", fileSize+"");
                        strHashMap.put("rowCnt", ipAlarmVoList.size()+"");

                        commonMapper.insertLinkageHist(strHashMap);

                        putFile.delete();
                    }
                }
            }
        }catch (Exception e){
            LOGGER.error("=====> [IpAlarmToAiLinkageService] sendAlarmData error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
        }
    }

    @Override
    public File createJsonFile(String eventType, String jsonData, String dataKey, String ftpUpdatePath) {
        LOGGER.info(">>>>>>>>>>[IpAlarmToAiLinkageService] createJsonFile(" + eventType + ") <<<<<<<<<<<<<<<<<");
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

            LOGGER.info(">>>>>>>>>>[IpAlarmToAiLinkageService] createJsonFile(" + (putFile != null ? putFile.getPath() : null) + ") <<<<<<<<<<<<<<<<<");

            if (pw != null) {
                pw.close();
            }
        }catch (Exception e){
            LOGGER.error("=====> [IpAlarmToAiLinkageService] createJsonFile error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
        }
        return putFile;
    }
}
