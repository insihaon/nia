package com.nia.data.linkage.ai.service.impl.ip.sflow;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nia.data.linkage.ai.common.SFTPSession;
import com.nia.data.linkage.ai.common.UtlDateHelper;
import com.nia.data.linkage.ai.common.UtlFileReaderWriter;
import com.nia.data.linkage.ai.mapper.common.CommonMapper;
import com.nia.data.linkage.ai.mapper.ip.IpDataMapper;
import com.nia.data.linkage.ai.service.ip.sflow.IpSflowToAiLinkageService;
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
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

@Service("IpSflowToAiLinkageService")
public class IpSflowToAiLinkageServiceImpl implements IpSflowToAiLinkageService {
    private static final Logger LOGGER = LoggerFactory.getLogger(IpSflowToAiLinkageService.class);

    @Autowired
    private CommonMapper commonMapper;

    @Autowired
    private IpDataMapper ipDataMapper;

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<SflowListVo> sflowListVoObjectFactory;

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<SFTPSession> sftpSessionObjectFactory;

    @Value("${spring.ftp.file-path}")
    private String uploadPath;

    @Override
    public void sendSflowLogData() {
        LOGGER.info("==========>[IpSflowToAiLinkageService] sendSflowLogData <==============");
        SFTPSession sftpSession;

        String dataKey = null;
        String jsonData;
        String ftpUpdatePath = uploadPath+"xe_sflow_log/";

        ArrayList<SflowLogVo> sflowVoList = null;
        HashMap<String, String> strHashMap;

        ObjectMapper mapper;
        File putFile = null;

        SflowListVo sflowListVo;

        try {
            Thread.sleep(180*1000);

            dataKey = commonMapper.selectLinkageYdKey("aiIpSfolwLogKey");

            LOGGER.info("==========>[IpSflowToAiLinkageService] sendSflowLogData dataKey : "+dataKey+" <==============");

            if(StringUtils.isNotEmpty(dataKey)){
                sflowVoList = ipDataMapper.selectSflowLogList(dataKey);


                if(sflowVoList != null && sflowVoList.size() > 0) {
                    LOGGER.info("==========>[IpSflowToAiLinkageService] sendSflowLogData perfVoList("+sflowVoList.size() +") <==============");

                    sflowListVo = sflowListVoObjectFactory.getObject();
                    sflowListVo.setData(sflowVoList);

                    mapper = new ObjectMapper();
                    jsonData = mapper.writeValueAsString(sflowListVo);

                    putFile = createJsonFile("xe_sflow_log", jsonData, sflowVoList.get(sflowVoList.size()-1).getDateregdate()+"", ftpUpdatePath);

                    sftpSession = sftpSessionObjectFactory.getObject();
                    sftpSession.init();

                    if(putFile != null){
                        sftpSession.upload(ftpUpdatePath, putFile);
                        LOGGER.info("=====> [IpSflowToAiLinkageService] sendSflowLogData upload : " + ftpUpdatePath+putFile.getName()+ "<=====");
                    }

                    sftpSession.disconnection();

                    if(putFile.exists()){
                        putFile.delete();
                    }

                    strHashMap = new HashMap<>();
                    strHashMap.put("key", "aiIpSfolwLogKey");
                    strHashMap.put("value", sflowVoList.get(sflowVoList.size()-1).getDateregdate()+"");
                    commonMapper.updateLinkageYdKey(strHashMap);
                }
            }
        }catch (Exception e){
            LOGGER.error("=====> [IpSflowToAiLinkageService] sendSflowLogData error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
        }
    }

    @Override
    public File createJsonFile(String eventType, String jsonData, String dataKey, String ftpUpdatePath) {
        LOGGER.info(">>>>>>>>>>[IpSflowToAiLinkageService] createJsonFile(" + eventType + ") <<<<<<<<<<<<<<<<<");
        File putFile = null;
        BufferedWriter output;
        PrintWriter pw;

        try{

            if(dataKey.contains("+")){
                dataKey = dataKey.substring(0,dataKey.indexOf("+"));
            }
            putFile = new File(ftpUpdatePath+eventType+"_"+(UtlDateHelper.stringToTimestamp(dataKey).getTime())+""+".json");

            if(!putFile.isFile()){
                putFile.createNewFile();
            }

            output  = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(putFile), StandardCharsets.UTF_8));
            pw = new PrintWriter(output,true);
            pw.write(jsonData);
            pw.flush();

            LOGGER.info(">>>>>>>>>>[IpSflowToAiLinkageService] createJsonFile(" + (putFile != null ? putFile.getPath() : null) + ") <<<<<<<<<<<<<<<<<");

            if (pw != null) {
                pw.close();
            }
        }catch (Exception e){
            LOGGER.error("=====> [IpSflowToAiLinkageService] createJsonFile error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
        }
        return putFile;
    }
}
