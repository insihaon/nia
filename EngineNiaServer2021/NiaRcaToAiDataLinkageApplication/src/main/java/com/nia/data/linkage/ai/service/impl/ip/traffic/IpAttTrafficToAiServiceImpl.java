package com.nia.data.linkage.ai.service.impl.ip.traffic;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.nia.data.linkage.ai.common.LoggerPrint;
import com.nia.data.linkage.ai.common.SFTPSession;
import com.nia.data.linkage.ai.common.UtlDateHelper;
import com.nia.data.linkage.ai.mapper.common.CommonMapper;
import com.nia.data.linkage.ai.mapper.ip.IpDataMapper;
import com.nia.data.linkage.ai.service.ip.traffic.IpAttTrafficToAiService;
import com.nia.data.linkage.ai.vo.ip.traffic.AttTrafficListVo;
import com.nia.data.linkage.ai.vo.ip.traffic.AttTrafficVo;
import com.nia.data.linkage.ai.vo.ip.traffic.NttSflowListVo;
import com.nia.data.linkage.ai.vo.ip.traffic.NttSflowVo;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.awt.peer.LabelPeer;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

@Service("IpAttTrafficToAiService")
public class IpAttTrafficToAiServiceImpl implements IpAttTrafficToAiService {

    private static final Logger LOGGER = LoggerFactory.getLogger(IpAttTrafficToAiService.class);

    @Autowired
    private CommonMapper commonMapper;

    @Autowired
    private IpDataMapper ipDataMapper;

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<AttTrafficListVo> sflowListVoObjectFactory;

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
    @EventListener(ApplicationReadyEvent.class)
    public void sendAttTrafficData() {
// 코드제이 -> 제로원
        LOGGER.info("==========>[IpAttTrafficToAiService] sendAttTrafficData <==============");
        SFTPSession sftpSession;

        String dataKey = null;
        String jsonData;
        String ftpUpdatePath = uploadPath+"att_traffic_log/";
        long fileSize;

        ArrayList<AttTrafficVo> attTrafficVoList = null;
        HashMap<String, String> strHashMap;

        ObjectMapper mapper;
        File putFile = null;
        File folder = new File(ftpUpdatePath);

        AttTrafficListVo sflowListVo;
        ArrayList<AttTrafficVo> allListVo;

        try {
            Thread.sleep(120*1000);



            attTrafficVoList = ipDataMapper.selectAttTrafficList();

            if (attTrafficVoList.size() == 0){
                LOGGER.info("==========>[IpAttTrafficToAiService] sendAttTrafficData attTrafficVoList( list size : 0 ) <==============");
            }

            if(attTrafficVoList != null && attTrafficVoList.size() > 0) {
                LOGGER.info("==========>[IpAttTrafficToAiService] sendAttTrafficData attTrafficVoList("+attTrafficVoList.size() +") <==============");

                sflowListVo = sflowListVoObjectFactory.getObject();
                sflowListVo.setData(attTrafficVoList);

                mapper = new ObjectMapper();
                jsonData = mapper.writeValueAsString(sflowListVo);

                putFile = createJsonFile("att_traffic_log", jsonData, attTrafficVoList.get(attTrafficVoList.size()-1).getMeasured_datetime(), ftpUpdatePath);
//
                sftpSession = sftpSessionObjectFactory.getObject();

                if(!"codej".equals(profiles)) {
                    try {
                        sftpSession.init(host1, port, user, pw);

                        if (putFile != null) {
                            if(!folder.exists()){
                                folder.mkdirs();

                            }

                            sftpSession.upload(ftpUpdatePath, putFile);
                            LOGGER.info("=====> [IpAttTrafficToAiService] sendAttTrafficData upload(" + host1.split("\\.")[3] + ") : " + ftpUpdatePath + putFile.getName() + "<=====");
                            LOGGER.info("sendAttTrafficData Success <<<<<<<<");
                        }

                        sftpSession.disconnection();
                    } catch (Exception e1) {
                        LOGGER.error("=====> [IpAttTrafficToAiService] sendAttTrafficData upload(" + host1.split("\\.")[3] + ") error() " + ExceptionUtils.getStackTrace(e1) + "<=====");
                    }

                    try {
                        sftpSession.init(host2, port, user, pw);

                        if (putFile != null) {
                            if(!folder.exists()){
                                folder.mkdirs();
                            }

                            sftpSession.upload(ftpUpdatePath, putFile);
                            LOGGER.info("=====> [IpAttTrafficToAiService] sendAttTrafficData upload(" + host2.split("\\.")[3] + ") : " + ftpUpdatePath + putFile.getName() + "<=====");
                            LOGGER.info("sendAttTrafficData Success <<<<<<<<");
                        }

                        sftpSession.disconnection();

                        putFile.delete();
                    } catch (Exception e1) {
                        LOGGER.error("=====> [IpAttTrafficToAiService] sendAttTrafficData upload(" + host2.split("\\.")[3] + ") error() " + ExceptionUtils.getStackTrace(e1) + "<=====");
                    }
                }

            }
        }catch (Exception e){
            LOGGER.error("=====> [IpAttTrafficToAiService] sendAttTrafficData error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
        }

    }

    @Override
    public File createJsonFile(String eventType, String jsonData, Timestamp dataKey, String ftpUpdatePath) {
        LOGGER.info(">>>>>>>>>>[IpAttTrafficToAiService] createJsonFile(" + eventType + ") <<<<<<<<<<<<<<<<<");
        File putFile = null;
        File folder = new File(localUploadPath+"/"+eventType);

        BufferedWriter output;
        PrintWriter pw;

        try{

//            if(dataKey.contains("+")){
//                dataKey = dataKey.substring(0,dataKey.indexOf("+"));
//            }

            if(!folder.exists()){
                folder.mkdirs();
            }

//            putFile = new File(folder.getPath()+"/"+eventType+"_240401~240630"+".json");
            putFile = new File(folder.getPath()+"/"+eventType+"_"+dataKey+""+".json");

            if(!putFile.isFile()){
                putFile.createNewFile();
            }

            output  = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(putFile), StandardCharsets.UTF_8));
            pw = new PrintWriter(output,true);
            pw.write(jsonData);
            pw.flush();

            LOGGER.info(">>>>>>>>>>[IpAttTrafficToAiService] createJsonFile(" + (putFile != null ? putFile.getPath() : null) + ") <<<<<<<<<<<<<<<<<");

            if (pw != null) {
                pw.close();
            }
        }catch (Exception e){
            LOGGER.error("=====> [IpAttTrafficToAiService] createJsonFile error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
        }
        return putFile;
        }
    }
