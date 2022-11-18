package com.nia.data.linkage.ipsdn.service.impl.ipsdn.traffic;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nia.data.linkage.ipsdn.common.SFTPSession;
import com.nia.data.linkage.ipsdn.mapper.common.CommonMapper;
import com.nia.data.linkage.ipsdn.mapper.ipsdn.IpsdnDataMapper;
import com.nia.data.linkage.ipsdn.service.ipsdn.factor.IpSdnFactorLinkageService;
import com.nia.data.linkage.ipsdn.service.ipsdn.traffic.IpSdnTrafficLinkageService;
import com.nia.data.linkage.ipsdn.vo.ipsdn.traffic.LinkTrafficListVo;
import com.nia.data.linkage.ipsdn.vo.ipsdn.traffic.LinkTrafficVo;
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

@Service("IpSdnTrafficLinkageService")
public class IpSdnTrafficLinkageServiceImpl implements IpSdnTrafficLinkageService {
    private static final Logger LOGGER = LoggerFactory.getLogger(IpSdnFactorLinkageService.class);

    @Autowired
    private CommonMapper commonMapper;

    @Autowired
    private IpsdnDataMapper ipsdnDataMapper;

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<LinkTrafficListVo> linkTrafficListVoObjectFactory;

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

    public void sendTrafficData() {
        LOGGER.info("==========>[IpSdnTrafficLinkageService] sendTrafficLinkData <==============");
        SFTPSession sftpSession;

        String dataKey = null;
        String jsonData;
        String ftpUpdatePath = uploadPath+"linktraffic/";
        ArrayList<LinkTrafficVo> linkTrafficVoList;
        long fileSize;

        ObjectMapper mapper;
        File putFile = null;
        File folder = new File(ftpUpdatePath);

        HashMap<String, String> strHashMap;

        LinkTrafficVo maxLinkTrafficVo;//max값 받는 변수
        LinkTrafficListVo linkTrafficListVo;


        try {
            Thread.sleep(15*1000);
            //15초

            dataKey = commonMapper.selectLinkageYdKey("aiIpSdnTrafficeKey");

            LOGGER.info("==========>[IpSdnTrafficLinkageService] sendTrafficData dataKey : "+dataKey+" <==============");

            if(StringUtils.isNotEmpty(dataKey)){
                linkTrafficVoList = ipsdnDataMapper.selectTrafficList(Integer.parseInt(dataKey));

                if(linkTrafficVoList != null && linkTrafficVoList.size() > 0) {
                    LOGGER.info("==========>[IpSdnTrafficLinkageService] sendTrafficData linkTrafficVoList("+linkTrafficVoList.size() +") <==============");

                    linkTrafficListVo = linkTrafficListVoObjectFactory.getObject();
                    linkTrafficListVo.setData(linkTrafficVoList);

                    mapper = new ObjectMapper();
                    jsonData = mapper.writeValueAsString(linkTrafficListVo);

                    putFile = createJsonFile("linktraffic", jsonData, linkTrafficVoList.get(linkTrafficVoList.size()-1).getMeasured_datetime().getTime()+"", ftpUpdatePath);

                    sftpSession = sftpSessionObjectFactory.getObject();

                        try {
                            sftpSession.init(host1, port, user, pw);

                            if (putFile != null) {
                                if(!folder.exists()){
                                    folder.mkdirs();
                                }

                                sftpSession.upload(ftpUpdatePath, putFile);
                                LOGGER.info("=====> [IpSdnTrafficLinkageService] sendTrafficData upload(" + host1.split("\\.")[3] + ") : " + ftpUpdatePath + putFile.getName() + "<=====");
                            }

                            sftpSession.disconnection();
                        } catch (Exception e1) {
                            LOGGER.error("=====> [IpSdnTrafficLinkageService] sendTrafficData upload(" + host1.split("\\.")[3] + ") error() " + ExceptionUtils.getStackTrace(e1) + "<=====");
                        }

                        try {
                            sftpSession.init(host2, port, user, pw);

                            if (putFile != null) {
                                if(!folder.exists()){
                                    folder.mkdirs();
                                }

                                sftpSession.upload(ftpUpdatePath, putFile);
                                LOGGER.info("=====> [IpSdnTrafficLinkageService] sendTrafficData upload(" + host2.split("\\.")[3] + ") : " + ftpUpdatePath + putFile.getName() + "<=====");
                            }

                            sftpSession.disconnection();
                        } catch (Exception e1) {
                            LOGGER.error("=====> [IpSdnTrafficLinkageService] sendTrafficData upload(" + host2.split("\\.")[3] + ") error() " + ExceptionUtils.getStackTrace(e1) + "<=====");
                        }


                    Comparator<LinkTrafficVo> comparatorById  = Comparator.comparingInt(LinkTrafficVo::getSdn_node_id);
                    maxLinkTrafficVo = linkTrafficVoList.stream()
                            .max(comparatorById)
                            .orElseThrow(NoSuchElementException::new);

                    strHashMap = new HashMap<>();
                    strHashMap.put("key", "aiIpSdnTrafficeKey");
                    strHashMap.put("value", maxLinkTrafficVo.getSdn_node_id()+"");
                    commonMapper.updateLinkageYdKey(strHashMap);

                    if(putFile.exists()){
                        fileSize = (putFile.length()) / 1024;

                        strHashMap = new HashMap<>();
                        strHashMap.put("key", "ipSdnTrafficeKey");
                        strHashMap.put("fileName", putFile.getName());
                        strHashMap.put("fileSize", fileSize+"");
                        strHashMap.put("rowCnt", linkTrafficVoList.size()+"");

                        commonMapper.insertLinkageHist(strHashMap);

                        putFile.delete();
                    }
                }
            }
        }catch (Exception e){
            LOGGER.error("=====> [IpSdnTrafficLinkageService] sendTrafficData error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
        }
    }

    @Override
    public File createJsonFile(String eventType, String jsonData, String perfKey, String ftpUpdatePath) {
        LOGGER.info(">>>>>>>>>>[IpSdnTrafficLinkageService] createJsonFile(" + eventType + ") <<<<<<<<<<<<<<<<<");
        File putFile = null;
        File folder = new File(localUploadPath+eventType);

        BufferedWriter output;
        PrintWriter pw;

        try{
            if(!folder.exists()){
                folder.mkdirs();
            }

            putFile = new File(folder.getPath()+"/"+eventType + "_" + perfKey+".json");
            //          perfKey >>  UtlDateHelper.getCurrentDate()

            if(!putFile.isFile()){
                putFile.createNewFile();
            }

            output  = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(putFile), StandardCharsets.UTF_8));
            pw = new PrintWriter(output,true);
            pw.write(jsonData);
            pw.flush();

            LOGGER.info(">>>>>>>>>>[IpSdnTrafficLinkageService] createJsonFile(" + (putFile != null ? putFile.getPath() : null) + ") <<<<<<<<<<<<<<<<<");

            if (pw != null) {
                pw.close();
            }
        }catch (Exception e){
            LOGGER.error("=====> [IpSdnTrafficLinkageService] createJsonFile error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
        }
        return putFile;
    }
}
