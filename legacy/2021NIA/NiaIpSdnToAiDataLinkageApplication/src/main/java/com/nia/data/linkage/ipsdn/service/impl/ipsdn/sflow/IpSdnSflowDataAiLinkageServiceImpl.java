package com.nia.data.linkage.ipsdn.service.impl.ipsdn.sflow;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nia.data.linkage.ipsdn.common.SFTPSession;
import com.nia.data.linkage.ipsdn.mapper.common.CommonMapper;
import com.nia.data.linkage.ipsdn.mapper.ipsdn.IpsdnDataMapper;
import com.nia.data.linkage.ipsdn.service.ipsdn.sflow.IpSdnSflowLinkageService;
import com.nia.data.linkage.ipsdn.vo.ipsdn.sflow.SflowDataListVo;
import com.nia.data.linkage.ipsdn.vo.ipsdn.sflow.SflowDataVo;
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
import java.util.Comparator;
import java.util.HashMap;
import java.util.NoSuchElementException;

@Service("IpSdnSflowDataAiLinkageService")
public class IpSdnSflowDataAiLinkageServiceImpl implements IpSdnSflowLinkageService {
    private static final Logger LOGGER = LoggerFactory.getLogger(IpSdnSflowDataAiLinkageServiceImpl.class);

    @Autowired
    private IpsdnDataMapper ipsdnDataMapper;

    @Autowired
    private CommonMapper commonMapper;

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<SflowDataListVo> ipSflowDataListVoObjectFactory;




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


    public void sendSflowData() {
        LOGGER.info("==========>[IpSdnSflowLinkageService] sendSflowData <==============");
        SFTPSession sftpSession;

        String dataKey = null;
        String jsonData;
        String ftpUpdatePath = uploadPath+"ipSdnSflow/";
        ArrayList<SflowDataVo> sflowDataVoList;
        long fileSize;

        ObjectMapper mapper;
        File putFile = null;
        File folder = new File(ftpUpdatePath);

        HashMap<String, String> strHashMap;

        SflowDataListVo sflowDataListVo;
        SflowDataVo maxSflowVo;


        try {
            Thread.sleep(15*1000);

            dataKey = commonMapper.selectLinkageYdKey("aiIpSdnSflowKey");
            LOGGER.info("==========>[IpSdnSflowLinkageService] sendSflowData dataKey : "+dataKey+" <==============");

            if(StringUtils.isNotEmpty(dataKey)){
                sflowDataVoList = ipsdnDataMapper.selectSflowData(Integer.parseInt(dataKey));

                if(sflowDataVoList != null && sflowDataVoList.size() > 0) {
                    LOGGER.info("==========>[IpSdnSflowLinkageService] sendSflowData sflowVoList("+sflowDataVoList.size() +") <==============");

                    sflowDataListVo = ipSflowDataListVoObjectFactory.getObject();
                    sflowDataListVo.setData(sflowDataVoList);
                    mapper = new ObjectMapper();
                    jsonData = mapper.writeValueAsString(sflowDataListVo);

                    putFile = createJsonFile("ipSdnSflow", jsonData, sflowDataVoList.get(sflowDataVoList.size()-1).getCollectSeq()+"", ftpUpdatePath);

                    sftpSession = sftpSessionObjectFactory.getObject();

                    try {
                        sftpSession.init(host1, port, user, pw);

                        if (putFile != null) {
                            if(!folder.exists()){
                                folder.mkdirs();
                            }

                            sftpSession.upload(ftpUpdatePath, putFile);
                            LOGGER.info("=====> [IpSdnSflowLinkageService] sendSflowData upload(" + host1.split("\\.")[3] + ") : " + ftpUpdatePath + putFile.getName() + "<=====");
                        }

                        sftpSession.disconnection();
                    } catch (Exception e1) {
                        LOGGER.error("=====> [IpSdnSflowLinkageService] sendSflowData upload(" + host1.split("\\.")[3] + ") error() " + ExceptionUtils.getStackTrace(e1) + "<=====");
                    }

                    try {
                        sftpSession.init(host2, port, user, pw);

                        if (putFile != null) {
                            if(!folder.exists()){
                                folder.mkdirs();
                            }

                            sftpSession.upload(ftpUpdatePath, putFile);
                            LOGGER.info("=====> [IpSdnSflowLinkageService] sendSflowData upload(" + host2.split("\\.")[3] + ") : " + ftpUpdatePath + putFile.getName() + "<=====");
                        }

                        sftpSession.disconnection();
                    } catch (Exception e1) {
                        LOGGER.error("=====> [IpSdnSflowLinkageService] sendSflowData upload(" + host2.split("\\.")[3] + ") error() " + ExceptionUtils.getStackTrace(e1) + "<=====");
                    }

                    Comparator<SflowDataVo> comparatorById  = Comparator.comparingInt(SflowDataVo::getCollectSeq);
                    maxSflowVo = sflowDataVoList.stream()
                            .max(comparatorById)
                            .orElseThrow(NoSuchElementException::new);

                    strHashMap = new HashMap<>();
                    strHashMap.put("key", "aiIpSdnSflowKey");
                    strHashMap.put("value", maxSflowVo.getCollectSeq()+"");
                    commonMapper.updateLinkageYdKey(strHashMap);



                    if(putFile.exists()){
                        fileSize = (putFile.length()) / 1024;

                        strHashMap = new HashMap<>();
                        strHashMap.put("key", "aiIpSdnSflowKey");
                        strHashMap.put("fileName", putFile.getName());
                        strHashMap.put("fileSize", fileSize+"");
                        strHashMap.put("rowCnt", sflowDataVoList.size()+"");

                        commonMapper.insertLinkageHist(strHashMap);

                        putFile.delete();
                    }
                }
            }
        }catch (Exception e){
            LOGGER.error("=====> [IpSdnSflowLinkageService] sendSflowData error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
        }
    }


    @Override
    public File createJsonFile(String eventType, String jsonData,String dataKey, String ftpUpdatePath) {
        LOGGER.info(">>>>>>>>>>[IpSdnSflowLinkageService] createJsonFile(" + eventType + ") <<<<<<<<<<<<<<<<<");
        File putFile = null;
        File folder = new File(localUploadPath+eventType);

        BufferedWriter output;
        PrintWriter pw;

        try{
            if(!folder.exists()){
                folder.mkdirs();
            }

            putFile = new File(folder.getPath()+"/"+eventType+"_"+dataKey+".json");

            if(!putFile.isFile()){
                putFile.createNewFile();
            }

            output  = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(putFile), StandardCharsets.UTF_8));
            pw = new PrintWriter(output,true);
            pw.write(jsonData);
            pw.flush();

            LOGGER.info(">>>>>>>>>>[IpSdnSflowLinkageService] createJsonFile(" + (putFile != null ? putFile.getPath() : null) + ") <<<<<<<<<<<<<<<<<");

            if (pw != null) {
                pw.close();
            }
        }catch (Exception e){
            LOGGER.error("=====> [IpSdnSflowLinkageService] createJsonFile error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
        }
        return putFile;
    }




}
