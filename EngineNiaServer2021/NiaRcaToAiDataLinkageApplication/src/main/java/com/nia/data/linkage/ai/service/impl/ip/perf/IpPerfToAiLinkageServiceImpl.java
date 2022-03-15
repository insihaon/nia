package com.nia.data.linkage.ai.service.impl.ip.perf;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nia.data.linkage.ai.common.SFTPSession;
import com.nia.data.linkage.ai.common.UtlFileReaderWriter;
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

import java.io.*;
import java.nio.charset.StandardCharsets;
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
    public void sendPerfLogData() {
        LOGGER.info("==========>[IpPerfToAiLinkageService] sendPerfLogData <==============");
        SFTPSession sftpSession;

        String dataKey = null;
        String jsonData;
        String ftpUpdatePath = uploadPath+"xe_cvnms_perf_if/";

        ArrayList<PerfVo> perfVoList;
        HashMap<String, String> strHashMap;

        ObjectMapper mapper;
        File putFile = null;
        File folder = new File(ftpUpdatePath);

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

                    putFile = createJsonFile("xe_cvnms_perf_if", jsonData, perfVoList.get(perfVoList.size()-1).getInttimestamp()+"", ftpUpdatePath);

                    sftpSession = sftpSessionObjectFactory.getObject();

                    if(!"codej".equals(profiles)) {
                        try {
                            sftpSession.init(host1, port, user, pw);

                            if (putFile != null) {
                                if(!folder.exists()){
                                    folder.mkdirs();
                                }

                                sftpSession.upload(ftpUpdatePath, putFile);
                                LOGGER.info("=====> [IpPerfToAiLinkageService] sendPerfLogData upload(" + host1.split("\\.")[3] + ") : " + ftpUpdatePath + putFile.getName() + "<=====");
                            }

                            sftpSession.disconnection();
                        } catch (Exception e1) {
                            LOGGER.error("=====> [IpPerfToAiLinkageService] sendPerfLogData upload(" + host1.split("\\.")[3] + ") error() " + ExceptionUtils.getStackTrace(e1) + "<=====");
                        }

                        try {
                            sftpSession.init(host2, port, user, pw);

                            if (putFile != null) {
                                if(!folder.exists()){
                                    folder.mkdirs();
                                }

                                sftpSession.upload(ftpUpdatePath, putFile);
                                LOGGER.info("=====> [IpPerfToAiLinkageService] sendPerfLogData upload(" + host2.split("\\.")[3] + ") : " + ftpUpdatePath + putFile.getName() + "<=====");
                            }

                            sftpSession.disconnection();
                        } catch (Exception e1) {
                            LOGGER.error("=====> [IpPerfToAiLinkageService] sendPerfLogData upload(" + host2.split("\\.")[3] + ") error() " + ExceptionUtils.getStackTrace(e1) + "<=====");
                        }
                    }

                    if("codej".equals(profiles)){
                        try {
                            sftpSession.init("10.81.192.18", 22, "aifactory", "dpdldkdl12!@");

                            if(putFile != null){
                                sftpSession.upload("/home/aifactory/inference/raw/xe_cvnms_perf_if/", putFile);
                                LOGGER.info("=====> [IpPerfToAiLinkageService] sendPerfLogData upload("+host2+") : " + "/home/aifactory/inference/raw/xe_cvnms_perf_if/"+putFile.getName()+ "<=====");
                            }

                            sftpSession.disconnection();
                        }catch (Exception e1){
                            LOGGER.error("=====> [IpPerfToAiLinkageService] sendPerfLogData upload("+host2+") error() "+ ExceptionUtils.getStackTrace(e1)+ "<=====");
                        }

                        try {
                            sftpSession.init("10.81.192.18", 22, "aifactory", "dpdldkdl12!@");

                            if(putFile != null){
                                sftpSession.upload("/home/aifactory/zerooneai/data/xe_cvnms_perf_if/", putFile);
                                LOGGER.info("=====> [IpPerfToAiLinkageService] sendPerfLogData upload("+host2+") : " + "/home/aifactory/zerooneai/data/xe_cvnms_perf_if/"+putFile.getName()+ "<=====");
                            }

                            sftpSession.disconnection();
                        }catch (Exception e1){
                            LOGGER.error("=====> [IpPerfToAiLinkageService] sendPerfLogData upload("+host2+") error() "+ ExceptionUtils.getStackTrace(e1)+ "<=====");
                        }
                    }

                    if(putFile.exists()){
                        putFile.delete();
                    }

                    strHashMap = new HashMap<>();
                    strHashMap.put("key", "aiIpPerfKey");
                    strHashMap.put("value", perfVoList.get(perfVoList.size()-1).getInttimestamp()+"");
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
        File folder = new File(localUploadPath+eventType);

        BufferedWriter output;
        PrintWriter pw;

        try{
            if(!folder.exists()){
                folder.mkdirs();
            }

            putFile = new File(folder.getPath()+"/"+eventType + "_" + perfKey+".json");

            if(!putFile.isFile()){
                putFile.createNewFile();
            }

            output  = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(putFile), StandardCharsets.UTF_8));
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
