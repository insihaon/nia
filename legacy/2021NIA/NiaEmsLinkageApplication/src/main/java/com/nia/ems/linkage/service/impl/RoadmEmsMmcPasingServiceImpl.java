package com.nia.ems.linkage.service.impl;

import com.nia.ems.linkage.amqp.PerformanceToAiPrdAmqp;
import com.nia.ems.linkage.common.LinkageCodeInfo;
import com.nia.ems.linkage.common.UtlCommon;
import com.nia.ems.linkage.common.UtlDateHelper;
import com.nia.ems.linkage.data.DataShareBean;
import com.nia.ems.linkage.mapper.AlarmMapper;
import com.nia.ems.linkage.mapper.EquipmentMapper;
import com.nia.ems.linkage.mapper.PerformaceMapper;
import com.nia.ems.linkage.service.AlarmService;
import com.nia.ems.linkage.service.RoadmEmsMmcPasingService;
import com.nia.ems.linkage.vo.equipment.*;
import com.nia.ems.linkage.vo.alarm.AlarmVo;
import com.nia.ems.linkage.vo.performance.PerformaceVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service("RoadmEmsMmcPasingService")
public class RoadmEmsMmcPasingServiceImpl  implements RoadmEmsMmcPasingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoadmEmsMmcPasingService.class);

    @Autowired
    private EquipmentMapper equipmentMapper;

    @Autowired
    private PerformaceMapper performaceMapper;

    @Autowired
    private AlarmMapper alarmMapper;

    @Autowired
    @Qualifier("AlarmService")
    private AlarmService alarmService;

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<AlarmVo> alarmVoFactory;

    @Autowired
	private org.springframework.beans.factory.ObjectFactory<EquipSipcVo> equipSipcVoObjectFactory;

    @Autowired
	private org.springframework.beans.factory.ObjectFactory<EquipSlotVo> equipSlotVoObjectFactory;

    @Autowired
	private org.springframework.beans.factory.ObjectFactory<EquipOpcVo> equipOpcVoObjectFactory;

    @Autowired
	private org.springframework.beans.factory.ObjectFactory<PerformaceVo> performaceVoObjectFactory;

    @Autowired
	private org.springframework.beans.factory.ObjectFactory<EquipNetWorkVo> equipNetWorkVoObjectFactory;

    @Autowired
	private org.springframework.beans.factory.ObjectFactory<EquipInfoVo> equipInfoVoObjectFactory;

    @Autowired
    private DataShareBean dataShareBean;

    @Override
    public void roadmMmcMsgPasing(String mmcResult, String mmc) {
        String[] mmcMsgLineArr = null;
        String[] mmcMsgLineDataArr = null;
        EquipSipcVo equipSipcVo;
        EquipSlotVo equipSlotVo;
        EquipOpcVo equipOpcVo;
        EquipNetWorkVo equipNetWorkVo;
        PerformaceVo performaceVo;
        List<EquipSipcVo> equipSipcVoList;
        List<EquipSlotVo> equipSlotVoList;
        List<EquipOpcVo> equipOpcVoList;
        List<EquipNetWorkVo> equipNetWorkVoList;
        List<PerformaceVo> performaceVoList;
        HashMap<String, Object> paramObjMap;
        HashMap<String, String> paramStrMap;
        String yyyyMMddHH;
        Timestamp ocrTime = null;

        String sysnamea = null;

        AlarmVo alarmVo;
        String[] mmcMsgInfoArr = null;

        int index = 0;
        String sysname = null;

        switch (mmc){
            case "RTRV-SIPC" :
                LOGGER.info("=====> [RoadmEmsMmcPasingService] roadmMmcMsgPasing RTRV-SIPC <=====");

                try {
                    equipSipcVoList = new ArrayList<EquipSipcVo>();

                    if(mmcResult.contains("COMPLD")){
                        mmcMsgLineArr = mmcResult.split("\n");
                        if(mmcMsgLineArr != null && mmcMsgLineArr.length > 0){
                            for(String mmcStr : mmcMsgLineArr){
                                if(mmcStr.contains("SH")){
                                    mmcMsgLineDataArr = mmcStr.split(",");
                                    if("ENABLE".equals(mmcMsgLineDataArr[0].split(":")[1])){
                                        if(StringUtils.isNotEmpty(mmcMsgLineDataArr[1])){
                                            if(mmcMsgLineDataArr[1].contains("READY")){
                                                equipSipcVo = equipSipcVoObjectFactory.getObject();
                                                equipSipcVo.setSysname(UtlCommon.lTrim(mmcMsgLineArr[3].replaceAll("\"","")).split("\\s")[0].split("-")[0]);
                                                equipSipcVo.setSysnameName(mmcMsgLineDataArr[0].split(":")[0].replaceAll("\"","").replaceAll("\\s",""));

                                                equipSipcVoList.add(equipSipcVo);
                                            }
                                        }
                                    }
                                }
                            }

                            equipSipcVo = equipSipcVoObjectFactory.getObject();
                            equipSipcVo.setSysname(UtlCommon.lTrim(mmcMsgLineArr[3].replaceAll("\"","")).split("\\s")[0].split("-")[0]);
                            equipSipcVo.setSysnameName("SH1");

                            equipSipcVoList.add(equipSipcVo);
                        }
                    }else {
                        equipSipcVo = equipSipcVoObjectFactory.getObject();
                        equipSipcVo.setSysname(UtlCommon.lTrim(mmcMsgLineArr[3].replaceAll("\"","")).split("\\s")[0].split("-")[0]);
                        equipSipcVo.setSysnameName("SH1");

                        equipSipcVoList.add(equipSipcVo);
                    }

                    if(equipSipcVoList != null && equipSipcVoList.size() > 0){
                        paramObjMap = new HashMap<String, Object>();
                        paramObjMap.put("equipSipcVoList", equipSipcVoList);
                        paramStrMap = new HashMap<String, String>();
                        paramStrMap.put("equipId", equipSipcVoList.get(0).getSysname());
                        equipmentMapper.deleteEquipSipc(paramStrMap);
                        LOGGER.info("=====> [RoadmEmsMmcPasingService] roadmMmcMsgPasing RTRV-SIPC size: "+equipSipcVoList.size()+"<=====");
                        equipmentMapper.insertEquipSipc(paramObjMap);
                    }
                }catch (Exception e){
                    LOGGER.error("=====> [RoadmEmsMmcPasingService] roadmMmcMsgPasing(RTRV-SIPC) error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
                }
                break;
            case "RTRV-PM" :
                LOGGER.info("=====> [RoadmEmsMmcPasingService] roadmMmcMsgPasing RTRV-PM <=====");

                try {
                    if(mmcResult.contains("COMPLD")) {
                        performaceVoList = new ArrayList<PerformaceVo>();

                        mmcMsgLineArr = mmcResult.split("\n");

                        if(mmcMsgLineArr != null && mmcMsgLineArr.length > 0) {

                            yyyyMMddHH = (UtlDateHelper.getCurrentDateTime()+"").substring(0,14);

                            for (String mmcStr : mmcMsgLineArr) {
                                if(mmcStr.contains("S.")){
                                    mmcMsgLineDataArr = mmcStr.split(",");

                                    performaceVo = performaceVoObjectFactory.getObject();
                                    performaceVo.setSysname(UtlCommon.lTrim(mmcMsgLineArr[3].replaceAll("\"","")).split("\\s")[0]);
                                    performaceVo.setPort(mmcMsgLineDataArr[0].replaceAll("\"","").replaceAll("\\s",""));
                                    performaceVo.setUnit(mmcMsgLineDataArr[1].split(":")[0]);
                                    performaceVo.setTmper(mmcMsgLineDataArr[2]);
                                    performaceVo.setRxCur(Double.parseDouble(mmcMsgLineDataArr[3]));
                                    performaceVo.setRxMin(Double.parseDouble(mmcMsgLineDataArr[4]));
                                    performaceVo.setRxMax(Double.parseDouble(mmcMsgLineDataArr[5]));
                                    performaceVo.setRxAve(Double.parseDouble(mmcMsgLineDataArr[6]));
                                    performaceVo.setTxCur(Double.parseDouble(mmcMsgLineDataArr[7]));
                                    performaceVo.setTxMin(Double.parseDouble(mmcMsgLineDataArr[8]));
                                    performaceVo.setTxMax(Double.parseDouble(mmcMsgLineDataArr[9]));
                                    performaceVo.setTxAve(Double.parseDouble(mmcMsgLineDataArr[10]));

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
                                    performaceVo.setOcrtime(ocrTime);
                                    performaceVoList.add(performaceVo);
                                }
                            }

                            if(performaceVoList != null && performaceVoList.size() > 0){
                                paramObjMap = new HashMap<String, Object>();
                                paramObjMap.put("performaceVoList", performaceVoList);
                                performaceMapper.insertPerformace(paramObjMap);
                                LOGGER.info("=====> [RoadmEmsMmcPasingService] roadmMmcMsgPasing RTRV-PM size: "+performaceVoList.size()+"<=====");
                            }
                        }
                    }
                }catch (Exception e){
                    LOGGER.error("=====> [RoadmEmsMmcPasingService] roadmMmcMsgPasing(RTRV-PM) error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
                }
                break;
            case "RTRV-ALM" :
                LOGGER.info("=====> [RoadmEmsMmcPasingService] roadmMmcMsgPasing RTRV-ALM <=====");

                index = 0;
                sysname = null;

                try {
                    if(mmcResult.contains("COMPLD")) {
                        mmcMsgLineArr = mmcResult.split("\n");

                        if(mmcMsgLineArr != null && mmcMsgLineArr.length > 0) {
                            for (String mmcStr : mmcMsgLineArr) {
                                if(mmcStr != null && mmcStr.length() > 0){
                                    mmcStr = mmcStr.replaceAll("\"","");

                                    if(index == 3){
                                        mmcStr = UtlCommon.lTrim(mmcStr);
                                        sysname = mmcStr.split("\\s")[0];
                                    }

                                    mmcStr = mmcStr.replaceAll("\\s","");

                                    if(mmcStr.contains("S.")){
                                        mmcMsgInfoArr = mmcStr.split(",");

                                        alarmVo = alarmVoFactory.getObject();
                                        alarmVo.setSysname(sysname);
                                        alarmVo.setReceivetime(UtlDateHelper.stringToTimestamp(mmcMsgInfoArr[4] + " " + mmcMsgInfoArr[5]));
                                        alarmVo.setAlarmloc(mmcMsgInfoArr[0]);

                                        if(mmcMsgInfoArr.length > 6){
                                            if(StringUtils.isNotEmpty(mmcMsgInfoArr[6])){
                                                alarmVo.setLineName(mmcMsgInfoArr[6]);
                                            }
                                        }

                                        if(mmcMsgInfoArr[1].contains(":")){
                                            alarmVo.setUnit(mmcMsgInfoArr[1].split(":")[0]);
                                            alarmVo.setAlarmlevel(alarmService.setAlarmLvl(mmcMsgInfoArr[2]));
                                        }

                                        alarmVo.setAlarmmsg(mmcMsgInfoArr[1].split(":")[1]);
                                        alarmVo.setAlarmtime(UtlDateHelper.stringToTimestamp(mmcMsgInfoArr[4] + " " + mmcMsgInfoArr[5].replaceAll("\"","")));
                                        alarmMapper.insertAlarm(alarmVo);
                                        LOGGER.info(alarmVo.toString());
                                    }
                                }
                                index++;
                            }
                        }
                    }
                }catch (Exception e){
                    LOGGER.error("=====> [RoadmEmsMmcPasingService] roadmMmcMsgPasing(RTRV-ALM) error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
                }
                break;
            case "RTRV-SLOT" :
                LOGGER.info("=====> [RoadmEmsMmcPasingService] roadmMmcMsgPasing RTRV-SLOT <=====");

                index = 0;
                sysname = null;

                try {
                    if(mmcResult.contains("COMPLD")) {
                        mmcMsgLineArr = mmcResult.split("\n");

                        if(mmcMsgLineArr != null && mmcMsgLineArr.length > 0) {

                            equipSlotVoList = new ArrayList<EquipSlotVo>();

                            for (String mmcStr : mmcMsgLineArr) {
                                if(mmcStr != null && mmcStr.length() > 0){
                                    mmcStr = mmcStr.replaceAll("\"","@");

                                    if(index == 3){
                                        mmcStr = UtlCommon.lTrim(mmcStr);
                                        sysname = mmcStr.split("\\s")[0];
                                    }

                                    mmcStr = mmcStr.replaceAll("\\s","");

                                    if(mmcStr.startsWith("@")){
                                        mmcMsgInfoArr = mmcStr.replaceAll("@","").split(",");

                                        equipSlotVo = equipSlotVoObjectFactory.getObject();
                                        equipSlotVo.setSysname(sysname);

                                        if(mmcMsgInfoArr[0].contains(":")){
                                            equipSlotVo.setSlot(mmcMsgInfoArr[0].split(":")[0]);
                                            equipSlotVo.setPUnit(mmcMsgInfoArr[0].split(":")[1]);
                                        }

                                        equipSlotVo.setPPorts(mmcMsgInfoArr[1]);
                                        equipSlotVo.setAct(mmcMsgInfoArr[2]);
                                        equipSlotVo.setState(mmcMsgInfoArr[3]);
                                        equipSlotVo.setRxDegree(mmcMsgInfoArr[4]);
                                        equipSlotVo.setTxDegree(mmcMsgInfoArr[5]);
                                        equipSlotVo.setAdSide(mmcMsgInfoArr[6]);
                                        equipSlotVo.setCurrentUnit(mmcMsgInfoArr[7]);
                                        equipSlotVo.setCurrentPorts(mmcMsgInfoArr[8]);
                                        equipSlotVo.setPairSlot(mmcMsgInfoArr[9]);
                                        equipSlotVo.setNetType(mmcMsgInfoArr[10]);

                                        equipSlotVoList.add(equipSlotVo);

                                        LOGGER.info(equipSlotVo.toString());
                                    }
                                }
                                index++;
                            }

                            if(equipSlotVoList != null && equipSlotVoList.size() > 0){
                                paramObjMap = new HashMap<String, Object>();
                                paramObjMap.put("equipSlotVoList", equipSlotVoList);
                                equipmentMapper.insertEquipSlot(paramObjMap);
                                LOGGER.info("=====> [RoadmEmsMmcPasingService] roadmMmcMsgPasing RTRV-SLOT size: "+equipSlotVoList.size()+"<=====");
                            }
                        }
                    }
                }catch (Exception e){
                    LOGGER.error("=====> [RoadmEmsMmcPasingService] roadmMmcMsgPasing(RTRV-SLOT) error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
                }
                break;
            case "RTRV-OPC" :
                LOGGER.info("=====> [RoadmEmsMmcPasingService] roadmMmcMsgPasing RTRV-OPC <=====");

                index = 0;
                sysname = null;

                try {
                    if(mmcResult.contains("COMPLD")) {
                        mmcMsgLineArr = mmcResult.split("\n");

                        if(mmcMsgLineArr != null && mmcMsgLineArr.length > 0) {

                            equipOpcVoList = new ArrayList<EquipOpcVo>();

                            for (String mmcStr : mmcMsgLineArr) {
                                if(mmcStr.contains(",255,,")){
                                    continue;
                                }

                                if(mmcStr != null && mmcStr.length() > 0){
                                    mmcStr = mmcStr.replaceAll("\"","@");

                                    if(index == 3){
                                        mmcStr = UtlCommon.lTrim(mmcStr);
                                        sysname = mmcStr.split("\\s")[0];
                                    }

                                    mmcStr = mmcStr.replaceAll("\\s","");

                                    if(mmcStr.startsWith("@")){
                                        mmcMsgInfoArr = mmcStr.replaceAll("@","").split(",");

                                        equipOpcVo = equipOpcVoObjectFactory.getObject();
                                        equipOpcVo.setSysname(sysname);
                                        equipOpcVo.setMrsaSlot(mmcMsgInfoArr[0]);

                                        if(mmcMsgInfoArr[1].contains(":")){
                                            equipOpcVo.setUnit(mmcMsgInfoArr[1].split(":")[0]);
                                            equipOpcVo.setLambda(mmcMsgInfoArr[1].split(":")[1]);
                                        }
                                        equipOpcVo.setPort(mmcMsgInfoArr[2]);
                                        equipOpcVo.setAddSource(mmcMsgInfoArr[3]);
                                        equipOpcVo.setDropAddress(mmcMsgInfoArr[4]);

                                        equipOpcVoList.add(equipOpcVo);

                                        LOGGER.info(equipOpcVo.toString());
                                    }
                                }
                                index++;
                            }

                            if(equipOpcVoList != null && equipOpcVoList.size() > 0){
                                paramObjMap = new HashMap<String, Object>();
                                paramObjMap.put("equipOpcVoList", equipOpcVoList);
                                equipmentMapper.insertEquipOpcYd(paramObjMap);
                                LOGGER.info("=====> [RoadmEmsMmcPasingService] roadmMmcMsgPasing RTRV-OPC size: "+equipOpcVoList.size()+"<=====");
                            }
                        }
                    }
                }catch (Exception e){
                    LOGGER.error("=====> [RoadmEmsMmcPasingService] roadmMmcMsgPasing(RTRV-OPC) error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
                }
                break;
            case "RTRV-NETWORK" :
                LOGGER.info("=====> [RoadmEmsMmcPasingService] roadmMmcMsgPasing RTRV-NETWORK <=====");

                try {
                    if(mmcResult.contains("COMPLD")) {
                        mmcMsgLineArr = mmcResult.split("\n");

                        if(mmcMsgLineArr != null && mmcMsgLineArr.length > 0) {

                            equipNetWorkVoList = new ArrayList<EquipNetWorkVo>();

                            for (String mmcStr : mmcMsgLineArr) {
                                if(mmcStr != null && mmcStr.length() > 0){
                                    mmcStr = UtlCommon.lTrim(mmcStr);
                                    mmcStr = mmcStr.replaceAll("\"","");

                                    if(mmcStr.contains("N:")){
                                        mmcMsgInfoArr = mmcStr.split(",");

                                        if(mmcMsgInfoArr.length > 0){
                                            sysnamea = mmcMsgInfoArr[0].replaceAll("N:", "");
                                        }
                                    }else if(mmcStr.startsWith("I:")){
                                        if(mmcStr.contains(",DIS,")){
                                            continue;
                                        }
                                        mmcMsgInfoArr = mmcStr.split(",");
                                        equipNetWorkVo = equipNetWorkVoObjectFactory.getObject();
                                        equipNetWorkVo.setSid(sysnamea);
                                        equipNetWorkVo.setAidBau(mmcMsgInfoArr[2]);
                                        equipNetWorkVo.setAidPau(mmcMsgInfoArr[3]);
                                        equipNetWorkVo.setPSid(mmcMsgInfoArr[5]);
                                        equipNetWorkVo.setPAidBau(mmcMsgInfoArr[8]);
                                        equipNetWorkVo.setPAidPau(mmcMsgInfoArr[9]);

                                        equipNetWorkVoList.add(equipNetWorkVo);
                                    }
                                }
                            }

                            if(equipNetWorkVoList != null && equipNetWorkVoList.size() > 0){
                                paramObjMap = new HashMap<String, Object>();
                                paramObjMap.put("equipNetWorkVoList", equipNetWorkVoList);
                                equipmentMapper.insertEquipNetWorkYd(paramObjMap);
                                LOGGER.info("=====> [RoadmEmsMmcPasingService] roadmMmcMsgPasing RTRV-NETWORK size: "+equipNetWorkVoList.size()+"<=====");
                            }
                        }
                    }
                }catch (Exception e){
                    LOGGER.error("=====> [RoadmEmsMmcPasingService] roadmMmcMsgPasing(RTRV-NETWORK) error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
                }
                break;
            case "RTRV-NET" :
                LOGGER.info("=====> [RoadmEmsMmcPasingService] roadmMmcMsgPasing RTRV-NET <=====");

                Pattern pattern;
                Matcher matcher;
                String data = null;

                try {
                    if(mmcResult.contains("COMPLD")) {
                        mmcMsgLineArr = mmcResult.split("\n");

                        if(mmcMsgLineArr != null && mmcMsgLineArr.length > 0) {

                            for (String mmcStr : mmcMsgLineArr) {
                                if(mmcStr != null && mmcStr.length() > 0){
                                    mmcStr = UtlCommon.lTrim(mmcStr);
                                    mmcStr = mmcStr.replaceAll("\"","");

                                    pattern = Pattern.compile("[^,.]");
                                    matcher = pattern.matcher(mmcStr);
                                    data = matcher.replaceAll("");

                                    if(data.contains("...,,")){
                                        mmcMsgInfoArr = mmcStr.replaceAll("\n","").split(",");
                                        ((ArrayList<String>)dataShareBean.getData(LinkageCodeInfo.DATA_SHARE_NAME_TID_LIST)).add(mmcMsgInfoArr[0]);
                                    }
                                }
                            }
                        }
                    }
                }catch (Exception e){
                    LOGGER.error("=====> [RoadmEmsMmcPasingService] roadmMmcMsgPasing(RTRV-NET) error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
                }
                break;
            case "RTRV-SYS" :
                LOGGER.info("=====> [RoadmEmsMmcPasingService] roadmMmcMsgPasing RTRV-SYS <=====");

                EquipInfoVo equipInfoVo = null;
                ArrayList<EquipInfoVo> equipInfoList = null;

                try {
                    if(mmcResult.contains("COMPLD")) {
                        mmcMsgLineArr = mmcResult.split("\n");

                        if(mmcMsgLineArr != null && mmcMsgLineArr.length > 0) {
                            equipInfoList = new ArrayList<EquipInfoVo>();

                            for (String mmcStr : mmcMsgLineArr) {
                                if(mmcStr != null && mmcStr.length() > 0){
                                    mmcStr = UtlCommon.lTrim(mmcStr);
                                    mmcStr = mmcStr.replaceAll("\"","");

                                    if(mmcStr.startsWith(":")){
                                        mmcMsgInfoArr = mmcStr.replaceAll("\n","").split(",");

                                        if(mmcStr.contains("-H3")){  // 중계기
                                            equipInfoVo = equipInfoVoObjectFactory.getObject();
                                            equipInfoVo.setTid(mmcMsgInfoArr[3].split("-")[0]);
                                            equipInfoVo.setSysname(mmcMsgInfoArr[0].replaceAll(":",""));
                                            equipInfoVo.setPsu(mmcMsgInfoArr[1]);
                                            equipInfoVo.setModel(mmcMsgInfoArr[2]);
                                            equipInfoVo.setSid(mmcMsgInfoArr[3].split("-")[0]);
                                            equipInfoVo.setSwVer(mmcMsgInfoArr[4]);
                                            equipInfoVo.setSwChksum(mmcMsgInfoArr[5]);
                                            equipInfoVo.setSwDate(mmcMsgInfoArr[6]);
                                            equipInfoVo.setSwTime(mmcMsgInfoArr[7]);
                                        }else{ // 단국
                                            equipInfoVo = equipInfoVoObjectFactory.getObject();
                                            equipInfoVo.setTid(mmcMsgInfoArr[2].split("-")[0]);
                                            equipInfoVo.setSysname(mmcMsgInfoArr[0].replaceAll(":",""));
                                            equipInfoVo.setModel(mmcMsgInfoArr[1]);
                                            equipInfoVo.setSid(mmcMsgInfoArr[2].split("-")[0]);
                                            equipInfoVo.setSwVer(mmcMsgInfoArr[3]);
                                            equipInfoVo.setSwChksum(mmcMsgInfoArr[4]);
                                            equipInfoVo.setSwDate(mmcMsgInfoArr[5]);
                                            equipInfoVo.setSwTime(mmcMsgInfoArr[6]);
                                        }
                                    }
                                }
                            }

                            if(equipInfoVo != null){
                                equipmentMapper.insertEquipMstYd(equipInfoVo);
                            }
                        }
                    }
                }catch (Exception e){
                    LOGGER.error("=====> [RoadmEmsMmcPasingService] roadmMmcMsgPasing(RTRV-SYS) error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
                }
                break;
            default:
                break;
        }
    }
}
