package com.nia.data.linkage.ipsdn.service.impl.ipsdn.factor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nia.data.linkage.ipsdn.common.SFTPSession;
import com.nia.data.linkage.ipsdn.mapper.common.CommonMapper;
import com.nia.data.linkage.ipsdn.mapper.ipsdn.IpsdnDataMapper;
import com.nia.data.linkage.ipsdn.service.ipsdn.factor.IpSdnFactorLinkageService;
import com.nia.data.linkage.ipsdn.vo.ipsdn.factor.NodeFactorListVo;
import com.nia.data.linkage.ipsdn.vo.ipsdn.factor.NodeFactorVo;
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

@Service("IpSdnFactorLinkageService")
public class IpSdnFactorLinkageServiceImpl implements IpSdnFactorLinkageService {
    private static final Logger LOGGER = LoggerFactory.getLogger(IpSdnFactorLinkageService.class);

    @Autowired
    private CommonMapper commonMapper;

    @Autowired
    private IpsdnDataMapper ipsdnDataMapper;

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<NodeFactorListVo> nodeFactorListVoObjectFactory;

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

    public void sendFactorData() {
        LOGGER.info("==========>[IpSdnFactorLinkageService] sendFactorData <==============");
        SFTPSession sftpSession;

        String dataKey = null;
        String jsonData;
        String ftpUpdatePath = uploadPath+"nodefactor/";
        ArrayList<NodeFactorVo> nodeFactorVoList;
        long fileSize;

        ObjectMapper mapper;
        File putFile = null;
        File folder = new File(ftpUpdatePath);

        HashMap<String, String> strHashMap;

        NodeFactorListVo nodeFactorListVo;
        NodeFactorVo maxNodeFactorVo;


        try {
            Thread.sleep(15*1000);

            dataKey = commonMapper.selectLinkageYdKey("aiIpSdnNodeFactorKey");
            LOGGER.info("==========>[IpSdnFactorLinkageService] sendFactorData dataKey : "+dataKey+" <==============");

            if(StringUtils.isNotEmpty(dataKey)){
                nodeFactorVoList = ipsdnDataMapper.selectNodeFactorLink(Integer.parseInt(dataKey));

                if(nodeFactorVoList != null && nodeFactorVoList.size() > 0) {
                    LOGGER.info("==========>[IpSdnFactorLinkageService] sendFactorData perfVoList("+nodeFactorVoList.size() +") <==============");

                    nodeFactorListVo = nodeFactorListVoObjectFactory.getObject();
                    nodeFactorListVo.setData(nodeFactorVoList);
         mapper = new ObjectMapper();
                    jsonData = mapper.writeValueAsString(nodeFactorListVo);

                    putFile = createJsonFile("nodefactor", jsonData, nodeFactorVoList.get(nodeFactorVoList.size()-1).getMeasured_datetime().getTime()+"", ftpUpdatePath);

                    sftpSession = sftpSessionObjectFactory.getObject();

                        try {
                            sftpSession.init(host1, port, user, pw);

                            if (putFile != null) {
                                if(!folder.exists()){
                                    folder.mkdirs();
                                }

                                sftpSession.upload(ftpUpdatePath, putFile);
                                LOGGER.info("=====> [IpSdnFactorLinkageService] sendFactorData upload(" + host1.split("\\.")[3] + ") : " + ftpUpdatePath + putFile.getName() + "<=====");
                            }

                            sftpSession.disconnection();
                        } catch (Exception e1) {
                            LOGGER.error("=====> [IpSdnFactorLinkageService] sendFactorData upload(" + host1.split("\\.")[3] + ") error() " + ExceptionUtils.getStackTrace(e1) + "<=====");
                        }

                        try {
                            sftpSession.init(host2, port, user, pw);

                            if (putFile != null) {
                                if(!folder.exists()){
                                    folder.mkdirs();
                                }

                                sftpSession.upload(ftpUpdatePath, putFile);
                                LOGGER.info("=====> [IpSdnFactorLinkageService] sendFactorData upload(" + host2.split("\\.")[3] + ") : " + ftpUpdatePath + putFile.getName() + "<=====");
                            }

                            sftpSession.disconnection();
                        } catch (Exception e1) {
                            LOGGER.error("=====> [IpSdnFactorLinkageService] sendFactorData upload(" + host2.split("\\.")[3] + ") error() " + ExceptionUtils.getStackTrace(e1) + "<=====");
                        }

                    Comparator<NodeFactorVo> comparatorById  = Comparator.comparingInt(NodeFactorVo::getSdn_node_id);
                    maxNodeFactorVo = nodeFactorVoList.stream()
                            .max(comparatorById)
                            .orElseThrow(NoSuchElementException::new);

                    strHashMap = new HashMap<>();
                    strHashMap.put("key", "aiIpSdnNodeFactorKey");
                    strHashMap.put("value", maxNodeFactorVo.getSdn_node_id()+"");
                    commonMapper.updateLinkageYdKey(strHashMap);



                    if(putFile.exists()){
                        fileSize = (putFile.length()) / 1024;

                        strHashMap = new HashMap<>();
                        strHashMap.put("key", "aiIpSdnNodeFactorKey");
                        strHashMap.put("fileName", putFile.getName());
                        strHashMap.put("fileSize", fileSize+"");
                        strHashMap.put("rowCnt", nodeFactorVoList.size()+"");

                        commonMapper.insertLinkageHist(strHashMap);

                        putFile.delete();
                    }
                }
            }
        }catch (Exception e){
            LOGGER.error("=====> [IpSdnFactorLinkageService] sendPerfLogData error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
        }
    }

    @Override
    public File createJsonFile(String eventType, String jsonData, String perfKey, String ftpUpdatePath) {
        LOGGER.info(">>>>>>>>>>[IpSdnFactorLinkageService] createJsonFile(" + eventType + ") <<<<<<<<<<<<<<<<<");
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

            LOGGER.info(">>>>>>>>>>[IpSdnFactorLinkageService] createJsonFile(" + (putFile != null ? putFile.getPath() : null) + ") <<<<<<<<<<<<<<<<<");

            if (pw != null) {
                pw.close();
            }
        }catch (Exception e){
            LOGGER.error("=====> [IpSdnFactorLinkageService] createJsonFile error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
        }
        return putFile;
    }
}
