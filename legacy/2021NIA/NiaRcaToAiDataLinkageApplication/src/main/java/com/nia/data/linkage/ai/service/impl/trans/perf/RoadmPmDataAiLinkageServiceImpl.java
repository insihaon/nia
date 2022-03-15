package com.nia.data.linkage.ai.service.impl.trans.perf;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nia.data.linkage.ai.common.SFTPSession;
import com.nia.data.linkage.ai.common.UtlDateHelper;
import com.nia.data.linkage.ai.common.UtlFileReaderWriter;
import com.nia.data.linkage.ai.mapper.common.CommonMapper;
import com.nia.data.linkage.ai.mapper.trans.TransDataMapper;
import com.nia.data.linkage.ai.service.trans.perf.RoadmPmDataAiLinkageService;
import com.nia.data.linkage.ai.vo.trans.perf.PerfDataAiLinkageVo;
import com.nia.data.linkage.ai.vo.trans.perf.PerformaceVo;
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

@Service("RoadmPmDataAiLinkageService")
public class RoadmPmDataAiLinkageServiceImpl implements RoadmPmDataAiLinkageService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RoadmPmDataAiLinkageService.class);

    @Autowired
    private CommonMapper commonMapper;

    @Autowired
    private TransDataMapper transDataMapper;

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

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<PerfDataAiLinkageVo> perfDataAiLinkageVoObjectFactory;

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<SFTPSession> sftpSessionObjectFactory;

    @Value("${spring.profiles}")
    private String profiles;

    @Override
    public void sendRoadmPmData() {
        LOGGER.info("=====> [RoadmPmDataAiLinkageService] sendRoadmPmData <=====");
        SFTPSession sftpSession;

        String ftpUpdatePath = uploadPath+"tb_performace_mst/";
        String dataKey = null;
        String jsonData;
        List<PerformaceVo> performaceVoList;
        HashMap<String, String> strHashMap;
        ObjectMapper mapper;
        File putFile = null;
        File folder = new File(ftpUpdatePath);

        PerfDataAiLinkageVo perfDataAiLinkageVo;

        try {
            Thread.sleep(300*1000);

            dataKey = commonMapper.selectLinkageYdKey("aiRoadmPerfKey");

            LOGGER.info("==========>[RoadmPmDataAiLinkageService] sendRoadmPmData dataKey : "+dataKey+" <==============");

            if(StringUtils.isNotEmpty(dataKey)){
                performaceVoList = transDataMapper.selectAiSendPerformanceData(dataKey);

                LOGGER.info("=====> [RoadmPmDataAiLinkageService] sendRoadmPmData performaceVoList size: " + performaceVoList.size() + "<=====");

                if(performaceVoList != null && performaceVoList.size() > 0) {

                    perfDataAiLinkageVo = perfDataAiLinkageVoObjectFactory.getObject();
                    perfDataAiLinkageVo.setData(performaceVoList);

                    mapper = new ObjectMapper();
                    jsonData = mapper.writeValueAsString(perfDataAiLinkageVo);

                    putFile = createJsonFile("tb_performace_mst", jsonData, performaceVoList.get(performaceVoList.size() - 1).getOcrtime() + "", ftpUpdatePath);

                    sftpSession = sftpSessionObjectFactory.getObject();

                    if(!"codej".equals(profiles)) {
                        try {
                            sftpSession.init(host1, port, user, pw);

                            if (putFile != null) {
                                if(!folder.exists()){
                                    folder.mkdirs();
                                }

                                sftpSession.upload(ftpUpdatePath, putFile);
                                LOGGER.info("=====> [RoadmPmDataAiLinkageService] sendRoadmPmData upload(" + host1.split("\\.")[3] + ") : " + ftpUpdatePath + putFile.getName() + "<=====");
                            }

                            sftpSession.disconnection();
                        } catch (Exception e1) {
                            LOGGER.error("=====> [RoadmPmDataAiLinkageService] sendRoadmPmData upload(" + host1.split("\\.")[3] + ") error() " + ExceptionUtils.getStackTrace(e1) + "<=====");
                        }

                        try {
                            sftpSession.init(host2, port, user, pw);

                            if (putFile != null) {
                                if(!folder.exists()){
                                    folder.mkdirs();
                                }

                                sftpSession.upload(ftpUpdatePath, putFile);
                                LOGGER.info("=====> [RoadmPmDataAiLinkageService] sendRoadmPmData upload(" + host2.split("\\.")[3] + ") : " + ftpUpdatePath + putFile.getName() + "<=====");
                            }

                            sftpSession.disconnection();
                        } catch (Exception e1) {
                            LOGGER.error("=====> [RoadmPmDataAiLinkageService] sendRoadmPmData upload(" + host2.split("\\.")[3] + ") error() " + ExceptionUtils.getStackTrace(e1) + "<=====");
                        }
                    }

                    if("codej".equals(profiles)){
                        try {
                            sftpSession.init("10.81.192.18", 22, "aifactory", "dpdldkdl12!@");

                            if(putFile != null){
                                sftpSession.upload("/home/aifactory/zerooneai/data/tb_performace_mst/", putFile);
                                LOGGER.info("=====> [RoadmPmDataAiLinkageService] sendRoadmPmData upload(10.81.192.18) : " + ftpUpdatePath+putFile.getName()+ "<=====");
                            }

                            sftpSession.disconnection();
                        }catch (Exception e1){
                            LOGGER.error("=====> [RoadmPmDataAiLinkageService] sendRoadmPmData upload(10.81.192.18) error() "+ ExceptionUtils.getStackTrace(e1)+ "<=====");
                        }
                    }

                    if(putFile.exists()){
                        putFile.delete();
                    }

                    strHashMap = new HashMap<>();
                    strHashMap.put("key", "aiRoadmPerfKey");
                    strHashMap.put("value", performaceVoList.get(performaceVoList.size()-1).getOcrtime()+"");
                    commonMapper.updateLinkageYdKey(strHashMap);
                }
            }
        }catch (Exception e){
            LOGGER.error("=====> [RoadmPmDataAiLinkageService] sendRoadmPmData error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
        }
    }

    @Override
    public File createJsonFile(String eventType, String jsonData, String ocrTime, String ftpUpdatePath) {
        LOGGER.info(">>>>>>>>>>[RoadmPmDataAiLinkageService] createJsonFile(" + eventType + ") <<<<<<<<<<<<<<<<<");
        File putFile = null;
        File folder = new File(localUploadPath+eventType);

        BufferedWriter output;
        PrintWriter pw;

        try{
            if(ocrTime.contains("+")){
                ocrTime = ocrTime.substring(0,ocrTime.indexOf("+"));
            }

            if(!folder.exists()){
                folder.mkdir();
            }

            putFile = new File(folder.getPath()+"/"+eventType+"_"+(UtlDateHelper.stringToTimestamp(ocrTime).getTime())+""+".json");

            if(!putFile.isFile()){
                putFile.createNewFile();
            }

            output  = new BufferedWriter(new FileWriter(putFile,true));
            pw = new PrintWriter(output,true);
            pw.write(jsonData);
            pw.flush();

            LOGGER.info(">>>>>>>>>>[RoadmPmDataAiLinkageService] createJsonFile(" + (putFile != null ? putFile.getPath() : null) + ") <<<<<<<<<<<<<<<<<");

            if (pw != null) {
                pw.close();
            }
        }catch (Exception e){
            LOGGER.error("=====> [RoadmPmDataAiLinkageService] createJsonFile error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
        }
        return putFile;
    }
}
