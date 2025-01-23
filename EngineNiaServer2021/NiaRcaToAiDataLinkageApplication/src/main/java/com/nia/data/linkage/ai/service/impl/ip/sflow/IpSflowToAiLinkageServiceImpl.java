package com.nia.data.linkage.ai.service.impl.ip.sflow;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nia.data.linkage.ai.common.SFTPSession;
import com.nia.data.linkage.ai.common.UtlDateHelper;
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
    public void sendSflowLogData() {
        // 기존 xe_Sflow_log 데이터에서 통계 데이터로 용도 변경함.
        LOGGER.info("==========>[IpSflowToAiLinkageService] sendSflowLogData <==============");
        SFTPSession sftpSession;

        String dataKey = null;
        String jsonData;
        String ftpUpdatePath = uploadPath + "xe_sflow_log/";
        long fileSize;

        ArrayList<SflowLogVo> sflowVoList = null;
        HashMap<String, String> strHashMap;

        ObjectMapper mapper;
        File putFile = null;
        File folder = new File(ftpUpdatePath);

        SflowListVo sflowListVo;

        try {
            Thread.sleep(15 * 1000);

            dataKey = commonMapper.selectLinkageYdKey("aiIpSfolwLogKey");

            LOGGER.info("==========>[IpSflowToAiLinkageService] sendSflowLogData dataKey : " + dataKey + " <==============");

            if (StringUtils.isNotEmpty(dataKey)) {
                sflowVoList = ipDataMapper.selectSflowLogList(dataKey);


                if (sflowVoList != null && sflowVoList.size() > 0) {
                    LOGGER.info("==========>[IpSflowToAiLinkageService] sendSflowLogData xeSflowLogList(" + sflowVoList.size() + ") <==============");

                    sflowListVo = sflowListVoObjectFactory.getObject();
                    sflowListVo.setData(sflowVoList);

                    mapper = new ObjectMapper();
                    jsonData = mapper.writeValueAsString(sflowListVo);

                    putFile = createJsonFile("xe_sflow_log", jsonData, sflowVoList.get(sflowVoList.size() - 1).getDateregdate() + "", ftpUpdatePath);

                    sftpSession = sftpSessionObjectFactory.getObject();

                    if (!"codej".equals(profiles)) {
                        try {
                            sftpSession.init(host1, port, user, pw);

                            if (putFile != null) {
                                if (!folder.exists()) {
                                    folder.mkdirs();
                                }

                                sftpSession.upload(ftpUpdatePath, putFile);
                                LOGGER.info("=====> [IpSflowToAiLinkageService] sendSflowLogData upload(" + host1.split("\\.")[3] + ") : " + ftpUpdatePath + putFile.getName() + "<=====");
                            }

                            sftpSession.disconnection();
                        } catch (Exception e1) {
                            LOGGER.error("=====> [IpSflowToAiLinkageService] sendSflowLogData upload(" + host1.split("\\.")[3] + ") error() " + ExceptionUtils.getStackTrace(e1) + "<=====");
                        }

                        try {
                            sftpSession.init(host2, port, user, pw);

                            if (putFile != null) {
                                if (!folder.exists()) {
                                    folder.mkdirs();
                                }

                                sftpSession.upload(ftpUpdatePath, putFile);
                                LOGGER.info("=====> [IpSflowToAiLinkageService] sendSflowLogData upload(" + host2.split("\\.")[3] + ") : " + ftpUpdatePath + putFile.getName() + "<=====");
                            }

                            sftpSession.disconnection();
                        } catch (Exception e1) {
                            LOGGER.error("=====> [IpSflowToAiLinkageService] sendSflowLogData upload(" + host2.split("\\.")[3] + ") error() " + ExceptionUtils.getStackTrace(e1) + "<=====");
                        }
                    }

                    if ("codej".equals(profiles)) {
                        try {
                            sftpSession.init("10.81.192.18", 22, "aifactory", "dpdldkdl12!@");

                            if (putFile != null) {
                                sftpSession.upload("/home/aifactory/inference/raw/xe_sflow_log/", putFile);
                                LOGGER.info("=====> [IpSflowToAiLinkageService] sendSflowLogData upload(10.81.192.18) : " + ftpUpdatePath + putFile.getName() + "<=====");
                            }

                            sftpSession.disconnection();
                        } catch (Exception e1) {
                            LOGGER.error("=====> [IpSflowToAiLinkageService] sendSflowLogData upload(10.81.192.18) error() " + ExceptionUtils.getStackTrace(e1) + "<=====");
                        }

                        try {
                            sftpSession.init("10.81.192.18", 22, "aifactory", "dpdldkdl12!@");

                            if (putFile != null) {
                                sftpSession.upload("/home/aifactory/zerooneai/data/xe_sflow_log/", putFile);
                                LOGGER.info("=====> [IpSflowToAiLinkageService] sendSflowLogData upload(10.81.192.18) : " + ftpUpdatePath + putFile.getName() + "<=====");
                            }

                            sftpSession.disconnection();
                        } catch (Exception e1) {
                            LOGGER.error("=====> [IpSflowToAiLinkageService] sendSflowLogData upload(10.81.192.18) error() " + ExceptionUtils.getStackTrace(e1) + "<=====");
                        }
                    }

                    strHashMap = new HashMap<>();
                    strHashMap.put("key", "aiIpSfolwLogKey");
                    strHashMap.put("value", sflowVoList.get(sflowVoList.size() - 1).getDateregdate() + "");
                    commonMapper.updateLinkageYdKey(strHashMap);

                    if (putFile.exists()) {
                        fileSize = (putFile.length()) / 1024;

                        strHashMap = new HashMap<>();
                        strHashMap.put("key", "aiIpSfolwLogKey");
                        strHashMap.put("fileName", putFile.getName());
                        strHashMap.put("fileSize", fileSize + "");
                        strHashMap.put("rowCnt", sflowVoList.size() + "");

                        commonMapper.insertLinkageHist(strHashMap);

                        putFile.delete();
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("=====> [IpSflowToAiLinkageService] sendSflowLogData error() " + ExceptionUtils.getStackTrace(e) + "<=====");
        }
    }

    @Override
    public File createJsonFile(String eventType, String jsonData, String dataKey, String ftpUpdatePath) {
        LOGGER.info(">>>>>>>>>>[IpSflowToAiLinkageService] createJsonFile(" + eventType + ") <<<<<<<<<<<<<<<<<");
        File putFile = null;
        File folder = new File(localUploadPath + "/" + eventType);

        BufferedWriter output;
        PrintWriter pw;

        try {

            if (dataKey.contains("+")) {
                dataKey = dataKey.substring(0, dataKey.indexOf("+"));
            }

            if (!folder.exists()) {
                folder.mkdirs();
            }

            putFile = new File(folder.getPath() + "/" + eventType + "_" + (UtlDateHelper.stringToTimestamp(dataKey).getTime()) + "" + ".json");

            if (!putFile.isFile()) {
                putFile.createNewFile();
            }

            output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(putFile), StandardCharsets.UTF_8));
            pw = new PrintWriter(output, true);
            pw.write(jsonData);
            pw.flush();

            LOGGER.info(">>>>>>>>>>[IpSflowToAiLinkageService] createJsonFile(" + (putFile != null ? putFile.getPath() : null) + ") <<<<<<<<<<<<<<<<<");

            if (pw != null) {
                pw.close();
            }
        } catch (Exception e) {
            LOGGER.error("=====> [IpSflowToAiLinkageService] createJsonFile error() " + ExceptionUtils.getStackTrace(e) + "<=====");
        }
        return putFile;
    }
}
