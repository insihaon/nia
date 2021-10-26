package com.nia.data.linkage.ip.sflow.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nia.data.linkage.ip.sflow.common.SFTPSession;
import com.nia.data.linkage.ip.sflow.common.UtlDateHelper;
import com.nia.data.linkage.ip.sflow.mapper.nia.NiaSflowMapper;
import com.nia.data.linkage.ip.sflow.service.SflowToAiLinkageService;
import com.nia.data.linkage.ip.sflow.vo.ai.AiSflowLogListVo;
import com.nia.data.linkage.ip.sflow.vo.sflow.SflowLogVo;
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
import java.util.ArrayList;
import java.util.HashMap;

@Service("SflowToAiLinkageService")
public class SflowToAiLinkageServiceImpl implements SflowToAiLinkageService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SflowLogServiceImpl.class);

    @Autowired
    private NiaSflowMapper niaAlarmMapper;

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<AiSflowLogListVo> aiSflowLogListVoObjectFactory;

    @Autowired
    private SFTPSession sftpSession;

    @Value("${spring.ftp.nia-file-path}")
    private String path;

    @Value("${spring.ftp.file-path}")
    private String uploadPath;

    @Override
    public void sendSflowLogData() {
        LOGGER.info("==========>[SflowToAiLinkageService] sendSflowLogData <==============");

        String dateRegDate = null;
        String jsonData;

        ArrayList<SflowLogVo> sflowLogVoList;
        HashMap<String, String> strHashMap;

        ObjectMapper mapper;
        File putFile = null;

        AiSflowLogListVo aiSflowLogListVo;

        try {
            dateRegDate = niaAlarmMapper.selectSflowYdKey("aiSflowLogKey");

            if(StringUtils.isNotEmpty(dateRegDate)){
                sflowLogVoList = niaAlarmMapper.selectSflowLogList(dateRegDate);

                if(sflowLogVoList != null && sflowLogVoList.size() > 0) {
                    LOGGER.info("==========>[SflowToAiLinkageService] sendSflowLogData sflowLogVoList("+sflowLogVoList.size() +") <==============");

                    aiSflowLogListVo = aiSflowLogListVoObjectFactory.getObject();
                    aiSflowLogListVo.setData(sflowLogVoList);

                    mapper = new ObjectMapper();
                    jsonData = mapper.writeValueAsString(aiSflowLogListVo);

                    putFile = createJsonFile("sflowLog", jsonData, sflowLogVoList.get(sflowLogVoList.size()-1).getDateRegDate()+"");

                    sftpSession.init();

                    if(putFile != null){
                        sftpSession.upload(uploadPath, putFile);
                    }

                    sftpSession.disconnection();

                    strHashMap = new HashMap<>();
                    strHashMap.put("key", "aiSflowLogKey");
                    strHashMap.put("value", sflowLogVoList.get(sflowLogVoList.size()-1).getDateRegDate()+"");
                    niaAlarmMapper.updateSflowYdKey(strHashMap);
                }
            }
        }catch (Exception e){
            LOGGER.error("=====> [SflowToAiLinkageService] sendSflowLogData error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
        }
    }

    @Override
    public File createJsonFile(String eventType, String jsonData, String interrIdx) {
        LOGGER.info(">>>>>>>>>>[SflowToAiLinkageService] createJsonFile(" + eventType + ") <<<<<<<<<<<<<<<<<");
        File putFile = null;
        BufferedWriter output;
        PrintWriter pw;

        try{
            putFile = new File(path+eventType+"_"+ UtlDateHelper.getCurrentDate()+"_"+interrIdx+".json");

            if(!putFile.isFile()){
                putFile.createNewFile();
            }

            output  = new BufferedWriter(new FileWriter(putFile,true));
            pw = new PrintWriter(output,true);
            pw.write(jsonData);
            pw.flush();

            LOGGER.info(">>>>>>>>>>[SflowToAiLinkageService] createJsonFile putFile(" + (putFile != null ? putFile.getPath() : null) + ") <<<<<<<<<<<<<<<<<");

            if (pw != null) {
                pw.close();
            }
        }catch (Exception e){
            LOGGER.error("=====> [SflowToAiLinkageService] createJsonFile error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
        }
        return putFile;
    }
}
