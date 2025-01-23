package com.nia.data.linkage.ai.service.impl.ip.sop;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nia.data.linkage.ai.common.SFTPSession;
import com.nia.data.linkage.ai.common.UtlDateHelper;
import com.nia.data.linkage.ai.mapper.common.CommonMapper;
import com.nia.data.linkage.ai.mapper.ip.IpDataMapper;
import com.nia.data.linkage.ai.service.ip.sop.IpSopToAiService;
import com.nia.data.linkage.ai.vo.sop.SopListVo;
import com.nia.data.linkage.ai.vo.sop.SopVo;
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

@Service("IpSopToAiService")
public class IpSopToAiServiceImpl implements IpSopToAiService {

    private static final Logger LOGGER = LoggerFactory.getLogger(IpSopToAiService.class);

    @Autowired
    private CommonMapper commonMapper;

    @Autowired
    private IpDataMapper ipDataMapper;

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<SFTPSession> sftpSessionObjectFactory;

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<SopListVo> sopVoObjectFactory;

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
    public void sendSopData() {

        LOGGER.info("==========>[IpSopToAiService] sendSopData <==============");
        SFTPSession sftpSession;

        String dataKey = null;
        String jsonData;
        String ftpUpdatePath = uploadPath+"tb_sop/";
        long fileSize;

        ArrayList<SopVo> sopVoList = null;
        HashMap<String, String> strHashMap;

        ObjectMapper mapper;
        File putFile = null;
        File folder = new File(ftpUpdatePath);

        SopListVo sopListVo;

        try {
            Thread.sleep(120*1000);

            dataKey = commonMapper.selectLinkageYdKey("aiIpSopKey");
            LOGGER.info("==========>[IpSopToAiService] sendSopData dataKey : "+dataKey+" <==============");
            if(StringUtils.isNotEmpty(dataKey)){
                sopVoList = ipDataMapper.selectSopList(dataKey);
                if (sopVoList != null && sopVoList.size()>0){
                    LOGGER.info("==========>[IpSopToAiService] sendSopData sopVoList("+sopVoList.size() +") <==============");

                    sopListVo = sopVoObjectFactory.getObject();
                    sopListVo.setData(sopVoList);

                    mapper = new ObjectMapper();
                    jsonData = mapper.writeValueAsString(sopListVo);

                    putFile = createJsonFile("tb_sop", jsonData, sopVoList.get(sopVoList.size()-1).getRequest_time()+"", ftpUpdatePath);
                    sftpSession = sftpSessionObjectFactory.getObject();

                    if(!"codej".equals(profiles)) {
                        try {
                            sftpSession.init(host1, port, user, pw);

                            if (putFile != null) {
                                if(!folder.exists()){
                                    folder.mkdirs();
                                }

                                sftpSession.upload(ftpUpdatePath, putFile);
                                LOGGER.info("=====> [IpSopToAiService] sendSopData upload(" + host1.split("\\.")[3] + ") : " + ftpUpdatePath + putFile.getName() + "<=====");
                            }

                            sftpSession.disconnection();
                        } catch (Exception e1) {
                            LOGGER.error("=====> [IpSopToAiService] sendSopData upload(" + host1.split("\\.")[3] + ") error() " + ExceptionUtils.getStackTrace(e1) + "<=====");
                        }

                        try {
                            sftpSession.init(host2, port, user, pw);

                            if (putFile != null) {
                                if(!folder.exists()){
                                    folder.mkdirs();
                                }

                                sftpSession.upload(ftpUpdatePath, putFile);
                                LOGGER.info("=====> [IpSopToAiService] sendSopData upload(" + host2.split("\\.")[3] + ") : " + ftpUpdatePath + putFile.getName() + "<=====");
                            }

                            sftpSession.disconnection();
                        } catch (Exception e1) {
                            LOGGER.error("=====> [IpSopToAiService] sendSopData upload(" + host2.split("\\.")[3] + ") error() " + ExceptionUtils.getStackTrace(e1) + "<=====");
                        }
                    }

                    if("codej".equals(profiles)){
                        try {
                            sftpSession.init("10.81.192.18", 22, "aifactory", "dpdldkdl12!@");

                            if(putFile != null){
                                sftpSession.upload("/home/aifactory/inference/raw/tb_sop/", putFile);
                                LOGGER.info("=====> [IpSopToAiService] sendSopData upload(10.81.192.18) : " + ftpUpdatePath+putFile.getName()+ "<=====");
                            }

                            sftpSession.disconnection();
                        }catch (Exception e1){
                            LOGGER.error("=====> [IpSopToAiService] sendSopData upload(10.81.192.18) error() "+ ExceptionUtils.getStackTrace(e1)+ "<=====");
                        }

                        try {
                            sftpSession.init("10.81.192.18", 22, "aifactory", "dpdldkdl12!@");

                            if(putFile != null){
                                sftpSession.upload("/home/aifactory/zerooneai/data/tb_sop/", putFile);
                                LOGGER.info("=====> [IpSopToAiService] sendSopData upload(10.81.192.18) : " + ftpUpdatePath+putFile.getName()+ "<=====");
                            }

                            sftpSession.disconnection();
                        }catch (Exception e1){
                            LOGGER.error("=====> [IpSopToAiService] sendSopData upload(10.81.192.18) error() "+ ExceptionUtils.getStackTrace(e1)+ "<=====");
                        }
                    }

                    strHashMap = new HashMap<>();
                    strHashMap.put("key", "aiIpSopKey");
                    strHashMap.put("value", sopVoList.get(sopVoList.size()-1).getSop_id()+"");
                    commonMapper.updateLinkageYdKey(strHashMap);

                    if(putFile.exists()){
                        fileSize = (putFile.length()) / 1024;

                        strHashMap = new HashMap<>();
                        strHashMap.put("key", "aiIpSopKey");
                        strHashMap.put("fileName", putFile.getName());
                        strHashMap.put("fileSize", fileSize+"");
                        strHashMap.put("rowCnt", sopVoList.size()+"");

                        commonMapper.insertLinkageHist(strHashMap);

                        putFile.delete();
                    }
                }
            }

        }catch (Exception e){
            LOGGER.error("=====> [IpSopToAiService] sendSopData error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
        }
    }

    @Override
    public File createJsonFile(String eventType, String jsonData, String dataKey, String ftpUpdatePath) {
        LOGGER.info(">>>>>>>>>>[IpSopToAiService] createJsonFile(" + eventType + ") <<<<<<<<<<<<<<<<<");
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

            putFile = new File(folder.getPath()+"/"+eventType+"_"+(UtlDateHelper.stringToTimestamp(dataKey).getTime())+""+".json");

            if(!putFile.isFile()){
                putFile.createNewFile();
            }

            output  = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(putFile), StandardCharsets.UTF_8));
            pw = new PrintWriter(output,true);
            pw.write(jsonData);
            pw.flush();

            LOGGER.info(">>>>>>>>>>[IpSopToAiService] createJsonFile(" + (putFile != null ? putFile.getPath() : null) + ") <<<<<<<<<<<<<<<<<");

            if (pw != null) {
                pw.close();
            }
        }catch (Exception e){
            LOGGER.error("=====> [IpSopToAiService] createJsonFile error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
        }
        return putFile;
    }
}
