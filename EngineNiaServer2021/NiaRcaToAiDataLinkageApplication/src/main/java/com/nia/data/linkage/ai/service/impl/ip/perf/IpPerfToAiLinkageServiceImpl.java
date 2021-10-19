package com.nia.data.linkage.ai.service.impl.ip.perf;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nia.data.linkage.ai.common.SFTPSession;
import com.nia.data.linkage.ai.mapper.common.CommonMapper;
import com.nia.data.linkage.ai.mapper.ip.IpDataMapper;
import com.nia.data.linkage.ai.service.ip.perf.IpPerfToAiLinkageService;
import com.nia.data.linkage.ai.vo.ip.perf.AiPerfIfListVo;
import com.nia.data.linkage.ai.vo.ip.perf.PerfVo;
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

@Service("IpPerfToAiLinkageService")
public class IpPerfToAiLinkageServiceImpl implements IpPerfToAiLinkageService {
    private static final Logger LOGGER = LoggerFactory.getLogger(IpPerfToAiLinkageService.class);

    @Autowired
    private CommonMapper commonMapper;

    @Autowired
    private IpDataMapper ipDataMapper;

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<AiPerfIfListVo> aiPerfIfListVoObjectFactory;

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<SFTPSession> sftpSessionObjectFactory;

    @Value("${spring.ftp.file-path}")
    private String uploadPath;

    @Override
    public void sendPerfLogData() {
        LOGGER.info("==========>[IpPerfToAiLinkageService] sendPerfLogData <==============");
        SFTPSession sftpSession;

        String dataKey = null;
        String jsonData;
        String ftpUpdatePath = uploadPath+"xeCvnmsPerfIf/";

        ArrayList<PerfVo> perfVoList;
        HashMap<String, String> strHashMap;

        ObjectMapper mapper;
        File putFile = null;

        AiPerfIfListVo aiPerfIfListVo;

        try {

            dataKey = commonMapper.selectLinkageYdKey("aiIpPerfKey");

            LOGGER.info("==========>[IpPerfToAiLinkageService] sendPerfLogData dataKey : "+dataKey+" <==============");

            if(StringUtils.isNotEmpty(dataKey)){
                perfVoList = ipDataMapper.selectPerfList(Integer.parseInt(dataKey));

                if(perfVoList != null && perfVoList.size() > 0) {
                    LOGGER.info("==========>[IpPerfToAiLinkageService] sendPerfLogData perfVoList("+perfVoList.size() +") <==============");

                    aiPerfIfListVo = aiPerfIfListVoObjectFactory.getObject();
                    aiPerfIfListVo.setData(perfVoList);

                    mapper = new ObjectMapper();
                    jsonData = mapper.writeValueAsString(aiPerfIfListVo);

                    putFile = createJsonFile("xeCvnmsPerfIf", jsonData, perfVoList.get(perfVoList.size()-1).getIntTimestamp()+"", ftpUpdatePath);

                    sftpSession = sftpSessionObjectFactory.getObject();
                    sftpSession.init();

                    if(putFile != null){
                        sftpSession.upload(ftpUpdatePath, putFile);
                        LOGGER.info("=====> [IpPerfToAiLinkageService] sendPerfLogData upload : " + ftpUpdatePath+"/"+putFile.getName()+ "<=====");
                    }

                    sftpSession.disconnection();

                    if(putFile.exists()){
                        putFile.delete();
                    }

                    strHashMap = new HashMap<>();
                    strHashMap.put("key", "aiIpPerfKey");
                    strHashMap.put("value", perfVoList.get(perfVoList.size()-1).getIntTimestamp()+"");
                    commonMapper.updateLinkageYdKey(strHashMap);
                }
            }
        }catch (Exception e){
            LOGGER.error("=====> [IpPerfToAiLinkageService] sendPerfLogData error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
        }
    }

    @Override
    public File createJsonFile(String eventType, String jsonData, String perfKey, String ftpUpdatePath) {
        LOGGER.info(">>>>>>>>>>[IpPerfToAiLinkageService] createJsonFile(" + eventType + ") <<<<<<<<<<<<<<<<<");
        File putFile = null;
        BufferedWriter output;
        PrintWriter pw;

        try{
            putFile = new File(ftpUpdatePath+eventType + "_" + perfKey+".json");

            if(!putFile.isFile()){
                putFile.createNewFile();
            }

            output  = new BufferedWriter(new FileWriter(putFile,true));
            pw = new PrintWriter(output,true);
            pw.write(jsonData);
            pw.flush();

            LOGGER.info(">>>>>>>>>>[IpPerfToAiLinkageService] createJsonFile(" + (putFile != null ? putFile.getPath() : null) + ") <<<<<<<<<<<<<<<<<");

            if (pw != null) {
                pw.close();
            }
        }catch (Exception e){
            LOGGER.error("=====> [IpPerfToAiLinkageService] createJsonFile error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
        }
        return putFile;
    }
}
