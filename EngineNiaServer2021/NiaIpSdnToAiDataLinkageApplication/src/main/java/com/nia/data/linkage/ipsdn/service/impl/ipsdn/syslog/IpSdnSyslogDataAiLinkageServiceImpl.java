package com.nia.data.linkage.ipsdn.service.impl.ipsdn.syslog;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nia.data.linkage.ipsdn.common.SFTPSession;
import com.nia.data.linkage.ipsdn.mapper.common.CommonMapper;
import com.nia.data.linkage.ipsdn.mapper.ipsdn.IpsdnDataMapper;
import com.nia.data.linkage.ipsdn.service.ipsdn.syslog.IpSdnSyslogLinkageService;
import com.nia.data.linkage.ipsdn.vo.ipsdn.syslog.SyslogDataListVo;
import com.nia.data.linkage.ipsdn.vo.ipsdn.syslog.SyslogDataVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service("IpSdnSyslogDataAiLinkageService")
public class IpSdnSyslogDataAiLinkageServiceImpl implements IpSdnSyslogLinkageService {
    private static final Logger LOGGER = LoggerFactory.getLogger(IpSdnSyslogDataAiLinkageServiceImpl.class);

    @Autowired
    private IpsdnDataMapper ipsdnDataMapper;

    @Autowired
    private CommonMapper commonMapper;

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<SyslogDataListVo> ipSyslogListVoObjectFactory;




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


    public void sendSyslogData() {
        LOGGER.info("==========>[IpSdnSyslogLinkageService] sendSyslogData <==============");
        SFTPSession sftpSession;

        String dataKey = null;
        String jsonData;
        String ftpUpdatePath = uploadPath+"ipSdnSyslog/";
        ArrayList<SyslogDataVo> syslogDataVoList;
        long fileSize;

        ObjectMapper mapper;
        File putFile = null;
        File folder = new File(ftpUpdatePath);

        HashMap<String, String> strHashMap;

        SyslogDataListVo syslogDataListVo;
        SyslogDataVo maxSyslogVo;


        try {
            Thread.sleep(15*1000);

            dataKey = commonMapper.selectLinkageYdKey("aiIpSdnSyslogKey");
            LOGGER.info("==========>[IpSdnSyslogLinkageService] sendSyslogData dataKey : "+dataKey+" <==============");

            if(StringUtils.isNotEmpty(dataKey)){
                syslogDataVoList = ipsdnDataMapper.selectSyslogData(Integer.parseInt(dataKey));

                if(syslogDataVoList != null && syslogDataVoList.size() > 0) {
                    LOGGER.info("==========>[IpSdnSyslogLinkageService] sendSyslogData syslogVoList("+syslogDataVoList.size() +") <==============");

                    syslogDataListVo = ipSyslogListVoObjectFactory.getObject();
                    syslogDataListVo.setData(syslogDataVoList);
                    mapper = new ObjectMapper();
                    jsonData = mapper.writeValueAsString(syslogDataListVo);

                    putFile = createJsonFile("ipSdnSyslog", jsonData, syslogDataVoList.get(syslogDataVoList.size()-1).getCollectSeq()+"", ftpUpdatePath);

                    sftpSession = sftpSessionObjectFactory.getObject();

                    try {
                        sftpSession.init(host1, port, user, pw);

                        if (putFile != null) {
                            if(!folder.exists()){
                                folder.mkdirs();
                            }

                            sftpSession.upload(ftpUpdatePath, putFile);
                            LOGGER.info("=====> [IpSdnSyslogLinkageService] sendSyslogData upload(" + host1.split("\\.")[3] + ") : " + ftpUpdatePath + putFile.getName() + "<=====");
                        }

                        sftpSession.disconnection();
                    } catch (Exception e1) {
                        LOGGER.error("=====> [IpSdnSyslogLinkageService] sendSyslogData upload(" + host1.split("\\.")[3] + ") error() " + ExceptionUtils.getStackTrace(e1) + "<=====");
                    }

                    try {
                        sftpSession.init(host2, port, user, pw);

                        if (putFile != null) {
                            if(!folder.exists()){
                                folder.mkdirs();
                            }

                            sftpSession.upload(ftpUpdatePath, putFile);
                            LOGGER.info("=====> [IpSdnSyslogLinkageService] sendSyslogData upload(" + host2.split("\\.")[3] + ") : " + ftpUpdatePath + putFile.getName() + "<=====");
                        }

                        sftpSession.disconnection();
                    } catch (Exception e1) {
                        LOGGER.error("=====> [IpSdnSyslogLinkageService] sendSyslogData upload(" + host2.split("\\.")[3] + ") error() " + ExceptionUtils.getStackTrace(e1) + "<=====");
                    }

                    Comparator<SyslogDataVo> comparatorById  = Comparator.comparingInt(SyslogDataVo::getCollectSeq);
                    maxSyslogVo = syslogDataVoList.stream()
                            .max(comparatorById)
                            .orElseThrow(NoSuchElementException::new);

                    strHashMap = new HashMap<>();
                    strHashMap.put("key", "aiIpSdnSyslogKey");
                    strHashMap.put("value", maxSyslogVo.getCollectSeq()+"");
                    commonMapper.updateLinkageYdKey(strHashMap);



                    if(putFile.exists()){
                        fileSize = (putFile.length()) / 1024;

                        strHashMap = new HashMap<>();
                        strHashMap.put("key", "aiIpSdnSyslogKey");
                        strHashMap.put("fileName", putFile.getName());
                        strHashMap.put("fileSize", fileSize+"");
                        strHashMap.put("rowCnt", syslogDataVoList.size()+"");

                        commonMapper.insertLinkageHist(strHashMap);

                        putFile.delete();
                    }
                }
            }
        }catch (Exception e){
            LOGGER.error("=====> [IpSdnSyslogLinkageService] sendSyslogData error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
        }
    }


    @Override
    public File createJsonFile(String eventType, String jsonData,String perfKey, String ftpUpdatePath) {
        LOGGER.info(">>>>>>>>>>[IpSdnSyslogLinkageService] createJsonFile(" + eventType + ") <<<<<<<<<<<<<<<<<");
        File putFile = null;
        File folder = new File(localUploadPath+eventType);

        BufferedWriter output;
        PrintWriter pw;

        try{
            if(!folder.exists()){
                folder.mkdirs();
            }

            putFile = new File(folder.getPath()+"/"+eventType+"_"+perfKey+".json");

            if(!putFile.isFile()){
                putFile.createNewFile();
            }

            output  = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(putFile), StandardCharsets.UTF_8));
            pw = new PrintWriter(output,true);
            pw.write(jsonData);
            pw.flush();

            LOGGER.info(">>>>>>>>>>[IpSdnSyslogLinkageService] createJsonFile(" + (putFile != null ? putFile.getPath() : null) + ") <<<<<<<<<<<<<<<<<");

            if (pw != null) {
                pw.close();
            }
        }catch (Exception e){
            LOGGER.error("=====> [IpSdnSyslogLinkageService] createJsonFile error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
        }
        return putFile;
    }
}
