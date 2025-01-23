package com.nia.data.linkage.ipsdn.service.impl.ipsdn.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nia.data.linkage.ipsdn.common.SFTPSession;
import com.nia.data.linkage.ipsdn.common.UtlDateHelper;
import com.nia.data.linkage.ipsdn.mapper.ipsdn.IpsdnDataMapper;
import com.nia.data.linkage.ipsdn.service.ipsdn.resource.IpSdnResourceLinkageService;
import com.nia.data.linkage.ipsdn.vo.ipsdn.resource.*;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

@Service("IpSdnResourceLinkageService")
public class IpSdnResourceLinkageServiceImpl implements IpSdnResourceLinkageService {
    private static final Logger LOGGER = LoggerFactory.getLogger(IpSdnResourceLinkageServiceImpl.class);

    @Autowired
    private IpsdnDataMapper ipsdnDataMapper;

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<NodeListVo> ipNodeLinkListVoObjectFactory;

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<InterfaceListVo> ipInterfaceListVoObjectFactory;

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<LinkListVo> ipLinkListVoObjectFactory;

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

    public void sendNodeData() {
        LOGGER.info("==========>[IpSdnResourceLinkageService] sendNodeData <==============");
        SFTPSession sftpSession;

        String dataKey = null;
        String jsonData;
//        String ftpUpdatePath = uploadPath+"tb_backbone_link/";
        String ftpUpdatePath = uploadPath+"node/";
        ArrayList<NodeVo> nodeVoList;

        ObjectMapper mapper;
        File putFile = null;
        File folder = new File(ftpUpdatePath);

        NodeListVo nodeListVo;

        try {
            nodeVoList = ipsdnDataMapper.selectNodeLink();

            if(nodeVoList != null && nodeVoList.size() > 0) {
                LOGGER.info("==========>[IpSdnResourceLinkageService] sendNodeData nodeListVo("+nodeVoList.size() +") <==============");

                nodeListVo = ipNodeLinkListVoObjectFactory.getObject();
                nodeListVo.setData(nodeVoList);

                mapper = new ObjectMapper();
                jsonData = mapper.writeValueAsString(nodeListVo);

                putFile = createJsonFile("node", jsonData, ftpUpdatePath);
                sftpSession = sftpSessionObjectFactory.getObject();

                    try {
                        sftpSession.init(host1, port, user, pw);

                        if (putFile != null) {
                            if(!folder.exists()){
                                folder.mkdirs();
                            }

                            sftpSession.upload(ftpUpdatePath, putFile);
                            LOGGER.info("=====> [IpSdnResourceLinkageService] sendNodeData upload(" + host1.split("\\.")[3] + ") : " + ftpUpdatePath + putFile.getName() + "<=====");
                        }

                        sftpSession.disconnection();
                    } catch (Exception e1) {
                        LOGGER.error("=====> [IpSdnResourceLinkageService] sendNodeData upload(" + host1.split("\\.")[3] + ") error() " + ExceptionUtils.getStackTrace(e1) + "<=====");
                    }

                    try {
                        sftpSession.init(host2, port, user, pw);

                        if (putFile != null) {
                            sftpSession.upload(ftpUpdatePath, putFile);
                            LOGGER.info("=====> [IpSdnResourceLinkageService] sendNodeData upload(" + host2.split("\\.")[3] + ") : " + ftpUpdatePath + putFile.getName() + "<=====");
                        }

                        sftpSession.disconnection();
                    } catch (Exception e1) {
                        LOGGER.error("=====> [IpSdnResourceLinkageService] sendNodeData upload(" + host2.split("\\.")[3] + ") error() " + ExceptionUtils.getStackTrace(e1) + "<=====");
                    }


                if(putFile.exists()){
                    putFile.delete();
                }
            }
        }catch (Exception e){
            LOGGER.error("=====> [IpSdnResourceLinkageService] sendNodeData error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
        }
    }



    @Override
    public void sendInterfaceData() {
        LOGGER.info("==========>[IpSdnResourceLinkageService] sendInterfaceData <==============");
        SFTPSession sftpSession;
        String jsonData;
        String ftpUpdatePath = uploadPath+"interface/";

        ArrayList<InterfaceVo> interfaceVoList;

        ObjectMapper mapper;
        File putFile = null;
        File folder = new File(ftpUpdatePath);

        InterfaceListVo interfaceListVo;

        try {
            interfaceVoList = ipsdnDataMapper.selectInterfaceLink();

            if(interfaceVoList != null && interfaceVoList.size() > 0) {
                LOGGER.info("==========>[IpSdnResourceLinkageService] sendInterfaceData interfaceVoList("+interfaceVoList.size() +") <==============");

                interfaceListVo = ipInterfaceListVoObjectFactory.getObject();
                interfaceListVo.setData(interfaceVoList);

                mapper = new ObjectMapper();
                jsonData = mapper.writeValueAsString(interfaceListVo);

                putFile = createJsonFile("interface", jsonData, ftpUpdatePath);

                sftpSession = sftpSessionObjectFactory.getObject();


                try {
                    sftpSession.init(host1, port, user, pw);

                    if (putFile != null) {
                        if(!folder.exists()){
                            folder.mkdirs();
                        }
                            sftpSession.upload(ftpUpdatePath, putFile);
                        LOGGER.info("=====> [IpSdnResourceLinkageService] sendInterfaceData upload(" + host1.split("\\.")[3] + ") : " + ftpUpdatePath + putFile.getName() + "<=====");
                    }

                    sftpSession.disconnection();
                } catch (Exception e1) {
                    LOGGER.error("=====> [IpSdnResourceLinkageService] sendInterfaceData upload(" + host1.split("\\.")[3] + ") error() " + ExceptionUtils.getStackTrace(e1) + "<=====");
                }

                try {
                    sftpSession.init(host2, port, user, pw);

                    if (putFile != null) {
                            sftpSession.upload(ftpUpdatePath, putFile);
                        LOGGER.info("=====> [IpSdnResourceLinkageService] sendInterfaceData upload(" + host2.split("\\.")[3] + ") : " + ftpUpdatePath + putFile.getName() + "<=====");
                    }

                    sftpSession.disconnection();
                } catch (Exception e1) {
                    LOGGER.error("=====> [IpSdnResourceLinkageService] sendInterfaceData upload(" + host2.split("\\.")[3] + ") error() " + ExceptionUtils.getStackTrace(e1) + "<=====");
                }



                if(putFile.exists()){
                    putFile.delete();
                }
            }
        }catch (Exception e){
            LOGGER.error("=====> [IpSdnResourceLinkageService] sendInterfaceData error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
        }
    }

    @Override
    public void sendLinkData() {
        LOGGER.info("==========>[IpSdnResourceLinkageService] sendLinkData <==============");

        SFTPSession sftpSession;
        String jsonData;
        String ftpUpdatePath = uploadPath+"link/";

        ArrayList<LinkVo> linkVoList;

        ObjectMapper mapper;
        File putFile = null;
        File folder = new File(ftpUpdatePath);

        LinkListVo linkListVo;

        try {
            linkVoList = ipsdnDataMapper.selectLink();

            if(linkVoList != null && linkVoList.size() > 0) {
                LOGGER.info("==========>[IpSdnResourceLinkageService] sendLinkData linkVoList("+linkVoList.size() +") <==============");

                linkListVo = ipLinkListVoObjectFactory.getObject();
                linkListVo.setData(linkVoList);

                mapper = new ObjectMapper();
                jsonData = mapper.writeValueAsString(linkListVo);

                putFile = createJsonFile("link", jsonData, ftpUpdatePath);

                sftpSession = sftpSessionObjectFactory.getObject();

                try {
                    sftpSession.init(host1, port, user, pw);

                    if (putFile != null) {
                        if(!folder.exists()){
                            folder.mkdirs();
                        }

                            sftpSession.upload(ftpUpdatePath, putFile);
                        LOGGER.info("=====> [IpSdnResourceLinkageService] sendLinkData upload(" + host1.split("\\.")[3] + ") : " + ftpUpdatePath + putFile.getName() + "<=====");
                    }

                    sftpSession.disconnection();
                } catch (Exception e1) {
                    LOGGER.error("=====> [IpSdnResourceLinkageService] sendLinkData upload(" + host1.split("\\.")[3] + ") error() " + ExceptionUtils.getStackTrace(e1) + "<=====");
                }

                try {
                    sftpSession.init(host2, port, user, pw);

                    if (putFile != null) {
                            sftpSession.upload(ftpUpdatePath, putFile);
                        LOGGER.info("=====> [IpSdnResourceLinkageService] sendLinkData upload(" + host2.split("\\.")[3] + ") : " + ftpUpdatePath + putFile.getName() + "<=====");
                    }

                    sftpSession.disconnection();
                } catch (Exception e1) {
                    LOGGER.error("=====> [IpSdnResourceLinkageService] sendLinkData upload(" + host2.split("\\.")[3] + ") error() " + ExceptionUtils.getStackTrace(e1) + "<=====");
                }


                if(putFile.exists()){
                    putFile.delete();
                }
            }
        }catch (Exception e){
            LOGGER.error("=====> [IpSdnResourceLinkageService] sendLinkData error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
        }
    }


    @Override
    public File createJsonFile(String eventType, String jsonData, String ftpUpdatePath) {
        LOGGER.info(">>>>>>>>>>[IpSdnResourceLinkageService] createJsonFile(" + eventType + ") <<<<<<<<<<<<<<<<<");
        File putFile = null;
        File folder = new File(localUploadPath+eventType);

        BufferedWriter output;
        PrintWriter pw;

        try{
            if(!folder.exists()){
                folder.mkdirs();
            }

            putFile = new File(folder.getPath()+"/"+eventType+"_"+ UtlDateHelper.getCurrentDate()+".json");

            if(!putFile.isFile()){
                putFile.createNewFile();
            }

            output  = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(putFile), StandardCharsets.UTF_8));
            pw = new PrintWriter(output,true);
            pw.write(jsonData);
            pw.flush();

            LOGGER.info(">>>>>>>>>>[IpSdnResourceLinkageService] createJsonFile(" + (putFile != null ? putFile.getPath() : null) + ") <<<<<<<<<<<<<<<<<");

            if (pw != null) {
                pw.close();
            }
        }catch (Exception e){
            LOGGER.error("=====> [IpSdnResourceLinkageService] createJsonFile error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
        }
        return putFile;
    }
}
