package com.nia.data.linkage.ai.service.impl.ip.traffic;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nia.data.linkage.ai.common.SFTPSession;
import com.nia.data.linkage.ai.common.UtlDateHelper;
import com.nia.data.linkage.ai.mapper.common.CommonMapper;
import com.nia.data.linkage.ai.mapper.ip.IpDataMapper;
import com.nia.data.linkage.ai.service.ip.traffic.IpNttSflowToAiService;
import com.nia.data.linkage.ai.vo.ip.traffic.NttSflowListVo;
import com.nia.data.linkage.ai.vo.ip.traffic.NttSflowVo;
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


@Service("IpNttSflowToAiService")
public class IpNttSflowToAiServiceImpl implements IpNttSflowToAiService {

    private static final Logger LOGGER = LoggerFactory.getLogger(IpNttSflowToAiService.class);

    @Autowired
    private CommonMapper commonMapper;

    @Autowired
    private IpDataMapper ipDataMapper;

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<NttSflowListVo> sflowListVoObjectFactory;

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
    public void sendNttSflowData() {
        // 코드제이 -> 제로원
        LOGGER.info("==========>[IpNttSflowToAiService] sendNttSflowData <==============");
        SFTPSession sftpSession;

        String dataKey = null;
        String jsonData;
        String ftpUpdatePath = uploadPath+"ntt_sflow_log/";
        long fileSize;

        ArrayList<NttSflowVo> nttSflowVoList = null;

        ObjectMapper mapper;
        File putFile = null;
        File folder = new File(ftpUpdatePath);

        NttSflowListVo sflowListVo;
        ArrayList<NttSflowVo> allListVo;

        try {
            Thread.sleep(120*1000);



                nttSflowVoList = ipDataMapper.selectNttSflowList();


                if(nttSflowVoList != null && nttSflowVoList.size() > 0) {
                    LOGGER.info("==========>[IpNttSflowToAiService] sendNttSflowData nttSflowVoList("+nttSflowVoList.size() +") <==============");

                    sflowListVo = sflowListVoObjectFactory.getObject();
                    sflowListVo.setData(nttSflowVoList);

                    mapper = new ObjectMapper();
                    jsonData = mapper.writeValueAsString(sflowListVo);

                    putFile = createJsonFile("ntt_sflow_log", jsonData, nttSflowVoList.get(nttSflowVoList.size()-1).getDateregdate()+"", ftpUpdatePath);

                    sftpSession = sftpSessionObjectFactory.getObject();

                    if(!"codej".equals(profiles)) {
                        try {
                            sftpSession.init(host1, port, user, pw);

                            if (putFile != null) {
                                if(!folder.exists()){
                                    folder.mkdirs();
                                }

                                sftpSession.upload(ftpUpdatePath, putFile);
                                LOGGER.info("=====> [IpNttSflowToAiService] sendNttSflowData upload(" + host1.split("\\.")[3] + ") : " + ftpUpdatePath + putFile.getName() + "<=====");
                                LOGGER.info("sendNttSflowData Success <<<<<<<<");
                            }

                            sftpSession.disconnection();
                        } catch (Exception e1) {
                            LOGGER.error("=====> [IpNttSflowToAiService] sendNttSflowData upload(" + host1.split("\\.")[3] + ") error() " + ExceptionUtils.getStackTrace(e1) + "<=====");
                        }

                        try {
                            sftpSession.init(host2, port, user, pw);

                            if (putFile != null) {
                                if(!folder.exists()){
                                    folder.mkdirs();
                                }

                                sftpSession.upload(ftpUpdatePath, putFile);
                                LOGGER.info("=====> [IpNttSflowToAiService] sendNttSflowData upload(" + host2.split("\\.")[3] + ") : " + ftpUpdatePath + putFile.getName() + "<=====");
                                LOGGER.info("sendNttSflowData Success <<<<<<<<");
                            }

                            sftpSession.disconnection();
                        } catch (Exception e1) {
                            LOGGER.error("=====> [IpNttSflowToAiService] sendNttSflowData upload(" + host2.split("\\.")[3] + ") error() " + ExceptionUtils.getStackTrace(e1) + "<=====");
                        }
                    }


//                    if(putFile.exists()){
//                        fileSize = (putFile.length()) / 1024;
//
//
//                        putFile.delete();
//                    }
                }
        }catch (Exception e){
            LOGGER.error("=====> [IpNttSflowToAiService] sendNttSflowData error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
        }

    }

    @Override
    public File createJsonFile(String eventType, String jsonData, String dataKey, String ftpUpdatePath) {
        LOGGER.info(">>>>>>>>>>[IpNttSflowToAiService] createJsonFile(" + eventType + ") <<<<<<<<<<<<<<<<<");
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

            LOGGER.info(">>>>>>>>>>[IpNttSflowToAiService] createJsonFile(" + (putFile != null ? putFile.getPath() : null) + ") <<<<<<<<<<<<<<<<<");

            if (pw != null) {
                pw.close();
            }
        }catch (Exception e){
            LOGGER.error("=====> [IpNttSflowToAiService] createJsonFile error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
        }
        return putFile;
        }
    }

