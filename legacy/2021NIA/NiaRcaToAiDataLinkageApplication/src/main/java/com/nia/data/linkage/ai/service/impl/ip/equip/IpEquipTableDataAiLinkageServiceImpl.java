package com.nia.data.linkage.ai.service.impl.ip.equip;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nia.data.linkage.ai.common.SFTPSession;
import com.nia.data.linkage.ai.common.UtlDateHelper;
import com.nia.data.linkage.ai.common.UtlFileReaderWriter;
import com.nia.data.linkage.ai.mapper.ip.IpDataMapper;
import com.nia.data.linkage.ai.service.ip.equip.IpEquipTableDataAiLinkageService;
import com.nia.data.linkage.ai.vo.ip.equip.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;

@Service("IpEquipTableDataAiLinkageService")
public class IpEquipTableDataAiLinkageServiceImpl extends UtlFileReaderWriter implements IpEquipTableDataAiLinkageService {
    private static final Logger LOGGER = LoggerFactory.getLogger(IpEquipTableDataAiLinkageService.class);

    @Autowired
    private IpDataMapper ipDataMapper;

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<IpBackboneLinkListVo> ipBackboneLinkListVoObjectFactory;

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<IpNodeListVo> ipNodeListVoObjectFactory;

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<IpPortListVo> ipPortListVoObjectFactory;

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<SFTPSession> sftpSessionObjectFactory;

    @Value("${spring.ftp.file-path}")
    private String uploadPath;

    @Override
    public void sendBackBoneLinkData() {
        LOGGER.info("==========>[IpEquipTableDataAiLinkageService] sendBackBoneLinkData <==============");
        SFTPSession sftpSession;

        String dataKey = null;
        String jsonData;
        String ftpUpdatePath = uploadPath+"tb_backbone_link/";

        ArrayList<IpBackboneLinkVo> ipBackboneLinkVoList;

        ObjectMapper mapper;
        File putFile = null;

        IpBackboneLinkListVo ipBackboneLinkListVo;

        try {
            ipBackboneLinkVoList = ipDataMapper.selectBackboneLink();

            if(ipBackboneLinkVoList != null && ipBackboneLinkVoList.size() > 0) {
                LOGGER.info("==========>[IpEquipTableDataAiLinkageService] sendBackBoneLinkData ipBackboneLinkVoList("+ipBackboneLinkVoList.size() +") <==============");

                ipBackboneLinkListVo = ipBackboneLinkListVoObjectFactory.getObject();
                ipBackboneLinkListVo.setData(ipBackboneLinkVoList);

                mapper = new ObjectMapper();
                jsonData = mapper.writeValueAsString(ipBackboneLinkListVo);

                putFile = createJsonFile("tb_backbone_link", jsonData, ftpUpdatePath);

                sftpSession = sftpSessionObjectFactory.getObject();
                sftpSession.init();

                if(putFile != null){
                    sftpSession.upload(ftpUpdatePath, putFile);
                    LOGGER.info("=====> [IpEquipTableDataAiLinkageService] sendBackBoneLinkData upload : " + ftpUpdatePath+putFile.getName()+ "<=====");
                }

                sftpSession.disconnection();

                if(putFile.exists()){
                    putFile.delete();
                }
            }
        }catch (Exception e){
            LOGGER.error("=====> [IpEquipTableDataAiLinkageService] sendBackBoneLinkData error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
        }
    }

    @Override
    public void sendNodeData() {
        LOGGER.info("==========>[IpEquipTableDataAiLinkageService] sendNodeData <==============");

        SFTPSession sftpSession;
        String jsonData;
        String ftpUpdatePath = uploadPath+"tb_node_mst/";

        ArrayList<IpNodeInfoVo> ipNodeInfoVoList;

        ObjectMapper mapper;
        File putFile = null;

        IpNodeListVo ipNodeListVo;

        try {
            ipNodeInfoVoList = ipDataMapper.selectNodeMst();

            if(ipNodeInfoVoList != null && ipNodeInfoVoList.size() > 0) {
                LOGGER.info("==========>[IpEquipTableDataAiLinkageService] sendNodeData ipNodeInfoVoList("+ipNodeInfoVoList.size() +") <==============");

                ipNodeListVo = ipNodeListVoObjectFactory.getObject();
                ipNodeListVo.setData(ipNodeInfoVoList);

                mapper = new ObjectMapper();
                jsonData = mapper.writeValueAsString(ipNodeListVo);

                putFile = createJsonFile("tb_node_mst", jsonData, ftpUpdatePath);

                sftpSession = sftpSessionObjectFactory.getObject();
                sftpSession.init();

                if(putFile != null){
                    sftpSession.upload(ftpUpdatePath, putFile);
                    LOGGER.info("=====> [IpEquipTableDataAiLinkageService] sendNodeData upload : " + ftpUpdatePath+putFile.getName()+ "<=====");
                }

                sftpSession.disconnection();
            }
        }catch (Exception e){
            LOGGER.error("=====> [IpEquipTableDataAiLinkageService] sendNodeData error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
        }
    }

    @Override
    public void sendPortData() {
        LOGGER.info("==========>[IpEquipTableDataAiLinkageService] sendPortData <==============");

        SFTPSession sftpSession;
        String jsonData;
        String ftpUpdatePath = uploadPath+"tb_port_mst/";

        ArrayList<IpPortMstVo> ipPortMstVoList;

        ObjectMapper mapper;
        File putFile = null;

        IpPortListVo ipPortListVo;

        try {
            ipPortMstVoList = ipDataMapper.selectPortMst();

            if(ipPortMstVoList != null && ipPortMstVoList.size() > 0) {
                LOGGER.info("==========>[IpEquipTableDataAiLinkageService] sendPortData ipNodeInfoVoList("+ipPortMstVoList.size() +") <==============");

                ipPortListVo = ipPortListVoObjectFactory.getObject();
                ipPortListVo.setData(ipPortMstVoList);

                mapper = new ObjectMapper();
                jsonData = mapper.writeValueAsString(ipPortListVo);

                putFile = createJsonFile("tb_port_mst", jsonData, ftpUpdatePath);

                sftpSession = sftpSessionObjectFactory.getObject();
                sftpSession.init();

                if(putFile != null){
                    sftpSession.upload(ftpUpdatePath, putFile);
                    LOGGER.info("=====> [IpEquipTableDataAiLinkageService] sendPortData upload : " + ftpUpdatePath+putFile.getName()+ "<=====");
                }

                sftpSession.disconnection();
            }
        }catch (Exception e){
            LOGGER.error("=====> [IpEquipTableDataAiLinkageService] sendPortData error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
        }
    }

    @Override
    public File createJsonFile(String eventType, String jsonData, String ftpUpdatePath) {
        LOGGER.info(">>>>>>>>>>[IpEquipTableDataAiLinkageService] createJsonFile(" + eventType + ") <<<<<<<<<<<<<<<<<");
        File putFile = null;
        BufferedWriter output;
        PrintWriter pw;

        try{
            putFile = new File(ftpUpdatePath+eventType+"_"+UtlDateHelper.getCurrentDate()+".json");

            if(!putFile.isFile()){
                putFile.createNewFile();
            }

            output  = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(putFile), "euc-kr"));
            pw = new PrintWriter(output,true);
            pw.write(jsonData);
            pw.flush();

            LOGGER.info(">>>>>>>>>>[IpEquipTableDataAiLinkageService] createJsonFile(" + (putFile != null ? putFile.getPath() : null) + ") <<<<<<<<<<<<<<<<<");

            if (pw != null) {
                pw.close();
            }
        }catch (Exception e){
            LOGGER.error("=====> [IpEquipTableDataAiLinkageService] createJsonFile error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
        }
        return putFile;
    }
}
