package com.nia.data.linkage.ai.service.impl.trans.equip;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nia.data.linkage.ai.common.SFTPSession;
import com.nia.data.linkage.ai.common.UtlDateHelper;
import com.nia.data.linkage.ai.common.UtlFileReaderWriter;
import com.nia.data.linkage.ai.mapper.ip.IpDataMapper;
import com.nia.data.linkage.ai.mapper.trans.TransDataMapper;
import com.nia.data.linkage.ai.service.ip.equip.IpEquipTableDataAiLinkageService;
import com.nia.data.linkage.ai.service.trans.equip.TransEquipTableDataAiLinkageService;
import com.nia.data.linkage.ai.vo.ip.equip.*;
import com.nia.data.linkage.ai.vo.trans.equip.*;
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

@Service("TransEquipTableDataAiLinkageService")
public class TransEquipTableDataAiLinkageServiceImpl implements TransEquipTableDataAiLinkageService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransEquipTableDataAiLinkageService.class);

    @Autowired
    private TransDataMapper transDataMapper;

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<SFTPSession> sftpSessionObjectFactory;

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<EquipInfoListVo> equipInfoListVoObjectFactory;

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<EquipPortListVo> equipPortListVoObjectFactory;

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<EquipSlotListVo> equipSlotListVoObjectFactory;

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<NniTopologyListVo> nniTopologyListVoObjectFactory;

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<UniTopologyListVo> uniTopologyListVoObjectFactory;

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<RoadmRepeaterRouteListVo> roadmRepeaterRouteListVoObjectFactory;

    @Value("${spring.ftp.file-path}")
    private String uploadPath;

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

    @Override
    public void sendEquipMstData() {
        LOGGER.info("==========>[TransEquipTableDataAiLinkageService] sendEquipMstData <==============");
        SFTPSession sftpSession;
        String jsonData;
        String ftpUpdatePath = uploadPath+"tb_equip_mst/";

        ArrayList<EquipInfoVo> equipInfoVoList;

        ObjectMapper mapper;
        File putFile = null;

        EquipInfoListVo equipInfoListVo;

        try {
            equipInfoVoList = transDataMapper.selectEquipList();

            if(equipInfoVoList != null && equipInfoVoList.size() > 0) {
                LOGGER.info("==========>[TransEquipTableDataAiLinkageService] sendEquipMstData equipInfoVoList("+equipInfoVoList.size() +") <==============");

                equipInfoListVo = equipInfoListVoObjectFactory.getObject();
                equipInfoListVo.setData(equipInfoVoList);

                mapper = new ObjectMapper();
                jsonData = mapper.writeValueAsString(equipInfoListVo);

                putFile = createJsonFile("tb_equip_mst", jsonData, ftpUpdatePath);

                sftpSession = sftpSessionObjectFactory.getObject();

                try {
                    sftpSession.init(host1, port, user, pw);

                    if(putFile != null){
                        sftpSession.upload(ftpUpdatePath, putFile);
                        LOGGER.info("=====> [TransEquipTableDataAiLinkageService] sendEquipMstData upload("+host1+") : " + ftpUpdatePath+putFile.getName()+ "<=====");
                    }

                    sftpSession.disconnection();
                }catch (Exception e1){
                    LOGGER.error("=====> [TransEquipTableDataAiLinkageService] sendEquipMstData upload("+host1+") error() "+ ExceptionUtils.getStackTrace(e1)+ "<=====");
                }

                try {
                    sftpSession.init(host2, port, user, pw);

                    if(putFile != null){
                        sftpSession.upload(ftpUpdatePath, putFile);
                        LOGGER.info("=====> [TransEquipTableDataAiLinkageService] sendEquipMstData upload("+host2+") : " + ftpUpdatePath+putFile.getName()+ "<=====");
                    }

                    sftpSession.disconnection();
                }catch (Exception e1){
                    LOGGER.error("=====> [TransEquipTableDataAiLinkageService] sendEquipMstData upload("+host2+") error() "+ ExceptionUtils.getStackTrace(e1)+ "<=====");
                }

                if(putFile.exists()){
                    putFile.delete();
                }
            }
        }catch (Exception e){
            LOGGER.error("=====> [TransEquipTableDataAiLinkageService] sendEquipMstData error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
        }
    }

    @Override
    public void sendEquipPortData() {
        LOGGER.info("==========>[TransEquipTableDataAiLinkageService] sendEquipPortData <==============");

        SFTPSession sftpSession;
        String jsonData;
        String ftpUpdatePath = uploadPath+"tb_equip_port/";

        ArrayList<EquipPortVo> equipPortVoList;

        ObjectMapper mapper;
        File putFile = null;

        EquipPortListVo equipPortListVo;

        try {
            equipPortVoList = transDataMapper.selectEquipPortList();

            if(equipPortVoList != null && equipPortVoList.size() > 0) {
                LOGGER.info("==========>[TransEquipTableDataAiLinkageService] sendEquipPortData equipPortVoList("+equipPortVoList.size() +") <==============");

                equipPortListVo = equipPortListVoObjectFactory.getObject();
                equipPortListVo.setData(equipPortVoList);

                mapper = new ObjectMapper();
                jsonData = mapper.writeValueAsString(equipPortListVo);

                putFile = createJsonFile("tb_equip_port", jsonData, ftpUpdatePath);

                sftpSession = sftpSessionObjectFactory.getObject();

                try {
                    sftpSession.init(host1, port, user, pw);

                    if(putFile != null){
                        sftpSession.upload(ftpUpdatePath, putFile);
                        LOGGER.info("=====> [TransEquipTableDataAiLinkageService] sendEquipPortData upload("+host1+") : " + ftpUpdatePath+putFile.getName()+ "<=====");
                    }

                    sftpSession.disconnection();
                }catch (Exception e1){
                    LOGGER.error("=====> [TransEquipTableDataAiLinkageService] sendEquipPortData upload("+host1+") error() "+ ExceptionUtils.getStackTrace(e1)+ "<=====");
                }

                try {
                    sftpSession.init(host2, port, user, pw);

                    if(putFile != null){
                        sftpSession.upload(ftpUpdatePath, putFile);
                        LOGGER.info("=====> [TransEquipTableDataAiLinkageService] sendEquipPortData upload("+host2+") : " + ftpUpdatePath+putFile.getName()+ "<=====");
                    }

                    sftpSession.disconnection();
                }catch (Exception e1){
                    LOGGER.error("=====> [TransEquipTableDataAiLinkageService] sendEquipPortData upload("+host2+") error() "+ ExceptionUtils.getStackTrace(e1)+ "<=====");
                }

                if(putFile.exists()){
                    putFile.delete();
                }
            }
        }catch (Exception e){
            LOGGER.error("=====> [TransEquipTableDataAiLinkageService] sendEquipPortData error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
        }
    }

    @Override
    public void sendEquipSlotData() {
        LOGGER.info("==========>[TransEquipTableDataAiLinkageService] sendEquipSlotData <==============");

        SFTPSession sftpSession;
        String jsonData;
        String ftpUpdatePath = uploadPath+"tb_equip_slot/";

        ArrayList<EquipSlotVo> equipSlotVoList;

        ObjectMapper mapper;
        File putFile = null;

        EquipSlotListVo equipSlotListVo;

        try {
            equipSlotVoList = transDataMapper.selectEquipSlotList();

            if(equipSlotVoList != null && equipSlotVoList.size() > 0) {
                LOGGER.info("==========>[TransEquipTableDataAiLinkageService] sendEquipSlotData equipSlotVoList("+equipSlotVoList.size() +") <==============");

                equipSlotListVo = equipSlotListVoObjectFactory.getObject();
                equipSlotListVo.setData(equipSlotVoList);

                mapper = new ObjectMapper();
                jsonData = mapper.writeValueAsString(equipSlotListVo);

                putFile = createJsonFile("tb_equip_slot", jsonData, ftpUpdatePath);

                sftpSession = sftpSessionObjectFactory.getObject();

                try {
                    sftpSession.init(host1, port, user, pw);

                    if(putFile != null){
                        sftpSession.upload(ftpUpdatePath, putFile);
                        LOGGER.info("=====> [TransEquipTableDataAiLinkageService] sendEquipSlotData upload("+host1+") : " + ftpUpdatePath+putFile.getName()+ "<=====");
                    }

                    sftpSession.disconnection();
                }catch (Exception e1){
                    LOGGER.error("=====> [TransEquipTableDataAiLinkageService] sendEquipSlotData upload("+host1+") error() "+ ExceptionUtils.getStackTrace(e1)+ "<=====");
                }

                try {
                    sftpSession.init(host2, port, user, pw);

                    if(putFile != null){
                        sftpSession.upload(ftpUpdatePath, putFile);
                        LOGGER.info("=====> [TransEquipTableDataAiLinkageService] sendEquipSlotData upload("+host2+") : " + ftpUpdatePath+putFile.getName()+ "<=====");
                    }

                    sftpSession.disconnection();
                }catch (Exception e1){
                    LOGGER.error("=====> [TransEquipTableDataAiLinkageService] sendEquipSlotData upload("+host2+") error() "+ ExceptionUtils.getStackTrace(e1)+ "<=====");
                }

                if(putFile.exists()){
                    putFile.delete();
                }
            }
        }catch (Exception e){
            LOGGER.error("=====> [TransEquipTableDataAiLinkageService] sendEquipSlotData error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
        }
    }

    @Override
    public void sendNniTopologyData() {
        LOGGER.info("==========>[TransEquipTableDataAiLinkageService] sendNniTopologyData <==============");

        SFTPSession sftpSession;
        String jsonData;
        String ftpUpdatePath = uploadPath+"tb_topology/";

        ArrayList<NNiTopologyVo> nniTopologyVoList;

        ObjectMapper mapper;
        File putFile = null;

        NniTopologyListVo nniTopologyListVo;

        try {
            nniTopologyVoList = transDataMapper.selectNniTopologyList();

            if(nniTopologyVoList != null && nniTopologyVoList.size() > 0) {
                LOGGER.info("==========>[TransEquipTableDataAiLinkageService] sendNniTopologyData niTopologyVoList("+nniTopologyVoList.size() +") <==============");

                nniTopologyListVo = nniTopologyListVoObjectFactory.getObject();
                nniTopologyListVo.setData(nniTopologyVoList);

                mapper = new ObjectMapper();
                jsonData = mapper.writeValueAsString(nniTopologyListVo);

                putFile = createJsonFile("tb_topology", jsonData, ftpUpdatePath);

                sftpSession = sftpSessionObjectFactory.getObject();

                try {
                    sftpSession.init(host1, port, user, pw);

                    if(putFile != null){
                        sftpSession.upload(ftpUpdatePath, putFile);
                        LOGGER.info("=====> [TransEquipTableDataAiLinkageService] sendNniTopologyData upload("+host1+") : " + ftpUpdatePath+putFile.getName()+ "<=====");
                    }

                    sftpSession.disconnection();
                }catch (Exception e1){
                    LOGGER.error("=====> [TransEquipTableDataAiLinkageService] sendNniTopologyData upload("+host1+") error() "+ ExceptionUtils.getStackTrace(e1)+ "<=====");
                }

                try {
                    sftpSession.init(host2, port, user, pw);

                    if(putFile != null){
                        sftpSession.upload(ftpUpdatePath, putFile);
                        LOGGER.info("=====> [TransEquipTableDataAiLinkageService] sendNniTopologyData upload("+host2+") : " + ftpUpdatePath+putFile.getName()+ "<=====");
                    }

                    sftpSession.disconnection();
                }catch (Exception e1){
                    LOGGER.error("=====> [TransEquipTableDataAiLinkageService] sendNniTopologyData upload("+host2+") error() "+ ExceptionUtils.getStackTrace(e1)+ "<=====");
                }

                if(putFile.exists()){
                    putFile.delete();
                }
            }
        }catch (Exception e){
            LOGGER.error("=====> [TransEquipTableDataAiLinkageService] sendNniTopologyData error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
        }
    }

    @Override
    public void sendUniTopologyData() {
        LOGGER.info("==========>[TransEquipTableDataAiLinkageService] sendUniTopologyData <==============");

        SFTPSession sftpSession;
        String jsonData;
        String ftpUpdatePath = uploadPath+"tb_uni_topology/";

        ArrayList<UniTopologyVo> uniTopologyVoList;

        ObjectMapper mapper;
        File putFile = null;

        UniTopologyListVo uniTopologyListVo;

        try {
            uniTopologyVoList = transDataMapper.selectUniTopologyList();

            if(uniTopologyVoList != null && uniTopologyVoList.size() > 0) {
                LOGGER.info("==========>[TransEquipTableDataAiLinkageService] sendUniTopologyData uniTopologyVoList("+uniTopologyVoList.size() +") <==============");

                uniTopologyListVo = uniTopologyListVoObjectFactory.getObject();
                uniTopologyListVo.setData(uniTopologyVoList);

                mapper = new ObjectMapper();
                jsonData = mapper.writeValueAsString(uniTopologyListVo);

                putFile = createJsonFile("tb_uni_topology", jsonData, ftpUpdatePath);

                sftpSession = sftpSessionObjectFactory.getObject();

                try {
                    sftpSession.init(host1, port, user, pw);

                    if(putFile != null){
                        sftpSession.upload(ftpUpdatePath, putFile);
                        LOGGER.info("=====> [TransEquipTableDataAiLinkageService] sendUniTopologyData upload("+host1+") : " + ftpUpdatePath+putFile.getName()+ "<=====");
                    }

                    sftpSession.disconnection();
                }catch (Exception e1){
                    LOGGER.error("=====> [TransEquipTableDataAiLinkageService] sendUniTopologyData upload("+host1+") error() "+ ExceptionUtils.getStackTrace(e1)+ "<=====");
                }

                try {
                    sftpSession.init(host2, port, user, pw);

                    if(putFile != null){
                        sftpSession.upload(ftpUpdatePath, putFile);
                        LOGGER.info("=====> [TransEquipTableDataAiLinkageService] sendUniTopologyData upload("+host2+") : " + ftpUpdatePath+putFile.getName()+ "<=====");
                    }

                    sftpSession.disconnection();
                }catch (Exception e1){
                    LOGGER.error("=====> [TransEquipTableDataAiLinkageService] sendUniTopologyData upload("+host2+") error() "+ ExceptionUtils.getStackTrace(e1)+ "<=====");
                }

                if(putFile.exists()){
                    putFile.delete();
                }
            }
        }catch (Exception e){
            LOGGER.error("=====> [TransEquipTableDataAiLinkageService] sendUniTopologyData error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
        }
    }

    @Override
    public void sendRoadmTrunkData() {
        LOGGER.info("==========>[TransEquipTableDataAiLinkageService] sendRoadmTrunkData <==============");

        SFTPSession sftpSession;
        String jsonData;
        String ftpUpdatePath = uploadPath+"tb_roadm_trunk_repeater/";

        ArrayList<RoadmRepeaterRouteVo> roadmRepeaterRouteVoList;

        ObjectMapper mapper;
        File putFile = null;

        RoadmRepeaterRouteListVo roadmRepeaterRouteListVo;

        try {

            roadmRepeaterRouteVoList = transDataMapper.selectRoadmTrunkList();

            if(roadmRepeaterRouteVoList != null && roadmRepeaterRouteVoList.size() > 0) {
                LOGGER.info("==========>[TransEquipTableDataAiLinkageService] sendRoadmTrunkData roadmRepeaterRouteVoList("+roadmRepeaterRouteVoList.size() +") <==============");

                roadmRepeaterRouteListVo = roadmRepeaterRouteListVoObjectFactory.getObject();
                roadmRepeaterRouteListVo.setData(roadmRepeaterRouteVoList);

                mapper = new ObjectMapper();
                jsonData = mapper.writeValueAsString(roadmRepeaterRouteListVo);

                putFile = createJsonFile("tb_roadm_trunk_repeater", jsonData, ftpUpdatePath);

                sftpSession = sftpSessionObjectFactory.getObject();

                try {
                    sftpSession.init(host1, port, user, pw);

                    if(putFile != null){
                        sftpSession.upload(ftpUpdatePath, putFile);
                        LOGGER.info("=====> [TransEquipTableDataAiLinkageService] sendRoadmTrunkData upload("+host1+") : " + ftpUpdatePath+putFile.getName()+ "<=====");
                    }

                    sftpSession.disconnection();
                }catch (Exception e1){
                    LOGGER.error("=====> [TransEquipTableDataAiLinkageService] sendRoadmTrunkData upload("+host1+") error() "+ ExceptionUtils.getStackTrace(e1)+ "<=====");
                }

                try {
                    sftpSession.init(host2, port, user, pw);

                    if(putFile != null){
                        sftpSession.upload(ftpUpdatePath, putFile);
                        LOGGER.info("=====> [TransEquipTableDataAiLinkageService] sendRoadmTrunkData upload("+host2+") : " + ftpUpdatePath+putFile.getName()+ "<=====");
                    }

                    sftpSession.disconnection();
                }catch (Exception e1){
                    LOGGER.error("=====> [TransEquipTableDataAiLinkageService] sendRoadmTrunkData upload("+host2+") error() "+ ExceptionUtils.getStackTrace(e1)+ "<=====");
                }

                if(putFile.exists()){
                    putFile.delete();
                }
            }
        }catch (Exception e){
            LOGGER.error("=====> [TransEquipTableDataAiLinkageService] sendRoadmTrunkData error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
        }
    }

    @Override
    public File createJsonFile(String eventType, String jsonData, String ftpUpdatePath) {
        LOGGER.info(">>>>>>>>>>[IpEquipTableDataAiLinkageService] createJsonFile(" + eventType + ") <<<<<<<<<<<<<<<<<");
        File putFile = null;
        BufferedWriter output;
        PrintWriter pw;

        try{
            putFile = new File(ftpUpdatePath+eventType);

            if(!putFile.exists()){
                putFile.mkdir();
            }

            putFile = new File(ftpUpdatePath+eventType+"_"+UtlDateHelper.getCurrentDate()+".json");

            if(!putFile.isFile()){
                putFile.createNewFile();
            }

            output  = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(putFile), StandardCharsets.UTF_8));
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
