package com.nia.ems.linkage.service.impl;

import com.nia.ems.linkage.amqp.PerformanceToAiPrdAmqp;
import com.nia.ems.linkage.common.LinkageCodeInfo;
import com.nia.ems.linkage.common.UtlDateHelper;
import com.nia.ems.linkage.config.TelnetMmc;
import com.nia.ems.linkage.data.DataShareBean;
import com.nia.ems.linkage.mapper.EquipmentMapper;
import com.nia.ems.linkage.mapper.PerformaceMapper;
import com.nia.ems.linkage.service.RoadmEmsMmcService;
import com.nia.ems.linkage.vo.equipment.EquipInfoVo;
import com.nia.ems.linkage.vo.equipment.EquipSipcVo;
import com.nia.ems.linkage.vo.equipment.EquipVo;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service("RoadmEmsMmcService")
public class RoadmEmsMmcServiceImpl implements RoadmEmsMmcService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoadmEmsMmcServiceImpl.class);

    @Autowired
    private EquipmentMapper equipmentMapper;

    @Autowired
    private PerformaceMapper performaceMapper;

    @Autowired
    private PerformanceToAiPrdAmqp performanceToAiPrdAmqp;

    @Autowired
	private org.springframework.beans.factory.ObjectFactory<RoadmEmsTL1Client> roadmEmsTL1ClientObjectFactory;

    @Autowired
    private DataShareBean dataShareBean;

    @Override
    public void roadmSipcMMC() {
        LOGGER.info("=====> [RoadmEmsMmcService] roadmSipcMMC <=====");
        List<EquipVo> equipList = null;
        String mmc = null;
        RoadmEmsTL1Client roadmEmsTL1Client = null;

        try {
            roadmEmsTL1Client = roadmEmsTL1ClientObjectFactory.getObject();
            equipList = equipmentMapper.selectEquipList();

            if(equipList != null && equipList.size() > 0){
                if(roadmEmsTL1Client.login("D")){
                    for(EquipVo equip : equipList){
                        if(roadmEmsTL1Client.isConnected()){
                            try {
                                mmc = "RTRV-SIPC:"+equip.getIp()+"-SH1::::;\r";
                                roadmEmsTL1Client.sendCommand(mmc, false);
                            //    roadmMmcMsgPasing(mmcResult, "RTRV-SIPC");
                            }catch (Exception e){
                                LOGGER.error("=====> [RoadmEmsMmcService] roadmSipcMMC error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
                                break;
                            }
                        }
                    }
                    if(roadmEmsTL1Client.isConnected()){
                        try {
                            roadmEmsTL1Client.logout();
                        }catch (Exception e){
                            LOGGER.error("=====> [RoadmEmsMmcService] roadmSipcMMC error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
                        }
                    }
                }
            }
        }catch (Exception e){
            if(roadmEmsTL1Client != null){
                roadmEmsTL1Client.logout();
            }
            LOGGER.error("=====> [RoadmEmsMmcService] roadmSipcMMC error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
        }finally {
            if(roadmEmsTL1Client != null){
                roadmEmsTL1Client.logout();
            }
        }
    }

    @Override
    public void roadmPmMMC() {
        LOGGER.info("=====> [RoadmEmsMmcService] roadmPmMMC <=====");
        List<EquipSipcVo> equipSipcVoList;
        String mmc = null;
        String yyyyMMddHH;
        Timestamp ocrTime = null;
        Boolean isUpdateOk = true;
        RoadmEmsTL1Client roadmEmsTL1Client = null;

        try {
            equipSipcVoList = equipmentMapper.selectEquipSipcList();

            if(equipSipcVoList != null && equipSipcVoList.size() > 0){
                roadmEmsTL1Client = roadmEmsTL1ClientObjectFactory.getObject();

                if(roadmEmsTL1Client.login("D")){
                    for(EquipSipcVo equipSipc : equipSipcVoList){
                        try {
                            if(roadmEmsTL1Client.isConnected()){
                                mmc = "RTRV-PM:"+equipSipc.getSysname()+"-"+equipSipc.getSysnameName()+"::::SIGNAL=AMPPWR,TYPE=15M,INTERVAL=CURR;\r";

                                try {
                                    roadmEmsTL1Client.sendCommand(mmc, false);
                                    isUpdateOk = true;
                                }catch (Exception e){
                                    LOGGER.info("=====> [RoadmEmsMmcService] roadmPmMMC login fail <=====");
                                    isUpdateOk = false;
                                    break;
                                }
                            //    roadmMmcMsgPasing(mmcResult, "RTRV-PM");
                            }else{
                                isUpdateOk = false;
                            }
                        }catch (Exception e){
                            LOGGER.info("=====> [RoadmEmsMmcService] roadmPmMMC login fail <=====");
                            isUpdateOk = false;
                            break;
                        }
                        Thread.sleep(500);
                    }

                    yyyyMMddHH = (UtlDateHelper.getCurrentDateTime()+"").substring(0,14);

                    if(UtlDateHelper.stringToTimestamp(yyyyMMddHH+"00:00").getTime() <= UtlDateHelper.getCurrentDateTime().getTime()
                            && UtlDateHelper.stringToTimestamp(yyyyMMddHH+"15:00").getTime() > UtlDateHelper.getCurrentDateTime().getTime()){
                        ocrTime = UtlDateHelper.stringToTimestamp(yyyyMMddHH+"00:00");
                    }else if(UtlDateHelper.stringToTimestamp(yyyyMMddHH+"15:00").getTime() <= UtlDateHelper.getCurrentDateTime().getTime()
                            && UtlDateHelper.stringToTimestamp(yyyyMMddHH+"30:00").getTime() > UtlDateHelper.getCurrentDateTime().getTime()){
                        ocrTime = UtlDateHelper.stringToTimestamp(yyyyMMddHH+"15:00");
                    }else if(UtlDateHelper.stringToTimestamp(yyyyMMddHH+"30:00").getTime() <= UtlDateHelper.getCurrentDateTime().getTime()
                            && UtlDateHelper.stringToTimestamp(yyyyMMddHH+"45:00").getTime() > UtlDateHelper.getCurrentDateTime().getTime()){
                        ocrTime = UtlDateHelper.stringToTimestamp(yyyyMMddHH+"30:00");
                    }else if(UtlDateHelper.stringToTimestamp(yyyyMMddHH+"45:00").getTime() <= UtlDateHelper.getCurrentDateTime().getTime()){
                        ocrTime = UtlDateHelper.stringToTimestamp(yyyyMMddHH+"45:00");
                    }

                    if(roadmEmsTL1Client != null && roadmEmsTL1Client.isConnected()){
                        roadmEmsTL1Client.logout();

                        if(isUpdateOk){
                            performanceToAiPrdAmqp.sendMessageCmd(ocrTime+"");
                            performaceMapper.updatePerformanceUpdateTime(ocrTime+"");
                        }
                    }
                }else{
                    if(roadmEmsTL1Client != null){
                        if(roadmEmsTL1Client.isConnected()){
                            roadmEmsTL1Client.logout();
                        }
                    }
                    LOGGER.info("=====> [RoadmEmsMmcService] roadmPmMMC login fail <=====");
                }
            }
        }catch (Exception e){
            if(roadmEmsTL1Client != null){
                roadmEmsTL1Client.logout();
            }
            LOGGER.error("=====> [RoadmEmsMmcService] roadmPmMMC error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
        }finally {
            if(roadmEmsTL1Client != null){
                roadmEmsTL1Client.logout();
            }
        }
    }

    @Override
    public void roadmNetWorkMmc() {
        LOGGER.info("=====> [RoadmEmsMmcService] roadmNetWorkMmc <=====");
        List<EquipVo> equipVoList;
        String mmc = null;
        int nniInsertCnt = 0;
        RoadmEmsTL1Client roadmEmsTL1Client = null;

        try {
            equipmentMapper.deleteEquipNetWorkYd();
            equipVoList = equipmentMapper.selectEquipList();

            if(equipVoList != null && equipVoList.size() > 0){
                roadmEmsTL1Client = roadmEmsTL1ClientObjectFactory.getObject();

                if(roadmEmsTL1Client.login("D")){
                    for(EquipVo equipVo : equipVoList){
                        try {
                            if(roadmEmsTL1Client.isConnected()){
                                mmc = "RTRV-NETWORK:"+equipVo.getIp()+"::123;\r";
                                try {
                                    roadmEmsTL1Client.sendCommand(mmc, false);
                                }catch (Exception e){
                                    LOGGER.info("=====> [RoadmEmsMmcService] roadmNetWorkMmc login fail <=====");
                                    break;
                                }
                            }
                        }catch (Exception e){
                            LOGGER.info("=====> [RoadmEmsMmcService] roadmNetWorkMmc login fail <=====");
                            break;
                        }
                        Thread.sleep(3000);
                    }
                    if(roadmEmsTL1Client.isConnected()){
                        roadmEmsTL1Client.logout();

                        equipmentMapper.deleteNniTopologyTmp();
                        nniInsertCnt = equipmentMapper.insertNniTopologyTmp();

                        if(nniInsertCnt >= 54){
                            equipmentMapper.deleteNniTopology();
                            equipmentMapper.insertNniTopology();
                        }
                    }
                }else{
                    if(roadmEmsTL1Client != null){
                        roadmEmsTL1Client.logout();
                    }
                    LOGGER.info("=====> [RoadmEmsMmcService] roadmNetWorkMmc login fail <=====");
                }
            }
        }catch (Exception e){
            if(roadmEmsTL1Client != null){
                roadmEmsTL1Client.logout();
            }
            LOGGER.error("=====> [RoadmEmsMmcService] roadmNetWorkMmc error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
        }finally {
            if(roadmEmsTL1Client != null){
                roadmEmsTL1Client.logout();
            }
        }
    }

    @Override
    public void roadmAlarmMMC() {
        LOGGER.info("=====> [RoadmEmsMmcService] roadmAlarmMMC <=====");
        List<EquipSipcVo> equipSipcVoList;
        String mmc = null;
        RoadmEmsTL1Client roadmEmsTL1Client = null;

        try {
            equipSipcVoList = equipmentMapper.selectEquipSipcList();

            if(equipSipcVoList != null && equipSipcVoList.size() > 0){
                roadmEmsTL1Client = roadmEmsTL1ClientObjectFactory.getObject();

                if(roadmEmsTL1Client.login("D")){
                    for(EquipSipcVo equipSipc : equipSipcVoList){
                        if(roadmEmsTL1Client.isConnected()){
                            mmc = "RTRV-ALM:"+equipSipc.getSysname()+"-"+equipSipc.getSysnameName()+"::123;\r";

                            roadmEmsTL1Client.sendCommand(mmc, false);

                        }
                        Thread.sleep(500);
                    }
                    if(roadmEmsTL1Client.isConnected()){
                        roadmEmsTL1Client.logout();
                    }
                }else{
                    if(roadmEmsTL1Client != null){
                        roadmEmsTL1Client.logout();
                    }
                    LOGGER.info("=====> [RoadmEmsMmcService] roadmAlarmMMC login fail <=====");
                }
            }
        }catch (Exception e){
            if(roadmEmsTL1Client != null){
                if(roadmEmsTL1Client.isConnected()){
                    roadmEmsTL1Client.logout();
                }
            }
            LOGGER.error("=====> [RoadmEmsMmcService] roadmAlarmMMC error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
        }finally {
            if(roadmEmsTL1Client != null){
                if(roadmEmsTL1Client.isConnected()){
                    roadmEmsTL1Client.logout();
                }
            }
        }
    }

    @Override
    public void roadmSlotMMC() {
        LOGGER.info("=====> [RoadmEmsMmcService] roadmSlotMMC <=====");
        List<EquipSipcVo> equipSipcVoList;
        String mmc = null;
        RoadmEmsTL1Client roadmEmsTL1Client = null;

        try {
            equipSipcVoList = equipmentMapper.selectEquipSipcList();

            if(equipSipcVoList != null && equipSipcVoList.size() > 0){
                roadmEmsTL1Client = roadmEmsTL1ClientObjectFactory.getObject();

                if(roadmEmsTL1Client.login("D")){
                    for(EquipSipcVo equipSipc : equipSipcVoList){
                        if(roadmEmsTL1Client.isConnected()){
                            mmc = "RTRV-SLOT:"+equipSipc.getSysname()+"-"+equipSipc.getSysnameName()+"::123;\r";

                            roadmEmsTL1Client.sendCommand(mmc, false);

                        }
                        Thread.sleep(500);
                    }
                    if(roadmEmsTL1Client.isConnected()){
                        roadmEmsTL1Client.logout();
                    }
                }else{
                    if(roadmEmsTL1Client != null){
                        if(roadmEmsTL1Client.isConnected()){
                            roadmEmsTL1Client.logout();
                        }
                    }
                    LOGGER.info("=====> [RoadmEmsMmcService] roadmSlotMMC login fail <=====");
                }
            }
        }catch (Exception e){
            if(roadmEmsTL1Client != null){
                if(roadmEmsTL1Client.isConnected()){
                    roadmEmsTL1Client.logout();
                }
            }
            LOGGER.error("=====> [RoadmEmsMmcService] roadmSlotMMC error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
        }finally {
            if(roadmEmsTL1Client != null){
                if(roadmEmsTL1Client.isConnected()){
                    roadmEmsTL1Client.logout();
                }
            }
        }
    }

    @Override
    public void createRoadmUniTopology() {
        LOGGER.info("=====> [RoadmEmsMmcService] roadmOpcMMC <=====");
        List<EquipSipcVo> equipSipcVoList;
        String mmc = null;
        int uniInsertCnt = 0;
        RoadmEmsTL1Client roadmEmsTL1Client = null;

        try {
            equipmentMapper.deleteEquipOpcYd();
            equipSipcVoList = equipmentMapper.selectEquipSipcList();

            if(equipSipcVoList != null && equipSipcVoList.size() > 0){
                roadmEmsTL1Client = roadmEmsTL1ClientObjectFactory.getObject();

                if(roadmEmsTL1Client.login("D")){
                    for(EquipSipcVo equipSipc : equipSipcVoList){
                        if(equipSipc.getSysname().startsWith("192")){
                            if(roadmEmsTL1Client.isConnected()){
                                mmc = "RTRV-OPC:"+equipSipc.getSysname()+"-"+equipSipc.getSysnameName()+"::123;\r";
                                roadmEmsTL1Client.sendCommand(mmc, false);
                            }
                            Thread.sleep(1000);
                        }
                    }
                    if(roadmEmsTL1Client.isConnected()){
                        roadmEmsTL1Client.logout();

                        equipmentMapper.deleteEquipOpc();
                        equipmentMapper.insertEquipOpc();
                        equipmentMapper.deleteUniTopologyTmp();
                        uniInsertCnt = equipmentMapper.insertUniTopologyTmp();

                        if(uniInsertCnt >= 59){
                            equipmentMapper.deleteUniTopology();
                            equipmentMapper.insertUniTopology();
                        }
                    }
                }else{
                    if(roadmEmsTL1Client != null){
                        if(roadmEmsTL1Client.isConnected()){
                            roadmEmsTL1Client.logout();
                        }
                    }
                    LOGGER.info("=====> [RoadmEmsMmcService] roadmOpcMMC login fail <=====");
                }
            }
        }catch (Exception e){
            if(roadmEmsTL1Client != null){
                if(roadmEmsTL1Client.isConnected()){
                    roadmEmsTL1Client.logout();
                }
            }
            LOGGER.error("=====> [RoadmEmsMmcService] roadmOpcMMC error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
        }finally {
            if(roadmEmsTL1Client != null){
                if(roadmEmsTL1Client.isConnected()){
                    roadmEmsTL1Client.logout();
                }
            }
        }
    }

    @Override
    public void getSystemList() {
        String mmc = null;
        RoadmEmsTL1Client roadmEmsTL1Client = null;

        try {
            roadmEmsTL1Client = roadmEmsTL1ClientObjectFactory.getObject();

            if(roadmEmsTL1Client.login("D")){
                if(roadmEmsTL1Client.isConnected()){
                    mmc = "RTRV-NET:::;\r";
                    roadmEmsTL1Client.sendCommand(mmc, false);
                }
                Thread.sleep(1000);

                if(roadmEmsTL1Client.isConnected()){
                    roadmEmsTL1Client.logout();
                }
            }else{
                if(roadmEmsTL1Client != null){
                    if(roadmEmsTL1Client.isConnected()){
                        roadmEmsTL1Client.logout();
                    }
                }
                LOGGER.info("=====> [RoadmEmsMmcService] getSystemList login fail <=====");
            }
        }catch (Exception e){
            LOGGER.error("=====> [RoadmEmsMmcService] getSystemList error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
        }finally {
            if(roadmEmsTL1Client != null){
                if(roadmEmsTL1Client.isConnected()){
                    roadmEmsTL1Client.logout();
                }
            }
        }
    }

    @Override
    public void updateSystemInfo() {
        String mmc = null;
        ArrayList<String> tidList;
        ArrayList<EquipInfoVo> equipInfoList;
        RoadmEmsTL1Client roadmEmsTL1Client = null;

        try {
            tidList = ((ArrayList<String>)dataShareBean.getData(LinkageCodeInfo.DATA_SHARE_NAME_TID_LIST));

            if(tidList != null && tidList.size() > 0){
                roadmEmsTL1Client = roadmEmsTL1ClientObjectFactory.getObject();

                if(roadmEmsTL1Client.login("D")){
                    for(String tid : tidList){
                        if(roadmEmsTL1Client.isConnected()){
                            mmc = "RTRV-SYS:"+tid+"::123;\r";
                            roadmEmsTL1Client.sendCommand(mmc, false);
                        }
                        Thread.sleep(1000);
                    }

                    if(roadmEmsTL1Client.isConnected()){
                        roadmEmsTL1Client.logout();
                    }
                    ((ArrayList<String>)dataShareBean.getData(LinkageCodeInfo.DATA_SHARE_NAME_TID_LIST)).clear();

                    equipInfoList = equipmentMapper.selectEquipUpdateList();

                    if(equipInfoList != null && equipInfoList.size() > 0){
                        for(EquipInfoVo equipInfoVo : equipInfoList){
                            equipmentMapper.updateEquipMst(equipInfoVo);
                        }
                    }

                    equipmentMapper.deleteEquipMstYd();
                }else{
                    if(roadmEmsTL1Client != null){
                        if(roadmEmsTL1Client.isConnected()){
                            roadmEmsTL1Client.logout();
                        }
                    }
                    LOGGER.info("=====> [RoadmEmsMmcService] updateSystemInfo login fail <=====");
                }
            }
        }catch (Exception e){
            LOGGER.error("=====> [RoadmEmsMmcService] updateSystemInfo error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
            equipmentMapper.deleteEquipMstYd();
        }finally {
            if(roadmEmsTL1Client != null){
                if(roadmEmsTL1Client.isConnected()){
                    roadmEmsTL1Client.logout();
                }
            }
        }
    }
}
