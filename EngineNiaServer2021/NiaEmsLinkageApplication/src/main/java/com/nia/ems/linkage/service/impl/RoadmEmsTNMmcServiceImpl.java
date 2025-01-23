package com.nia.ems.linkage.service.impl;

import com.nia.ems.linkage.amqp.PerformanceToAiPrdAmqp;
import com.nia.ems.linkage.common.UtlDateHelper;
import com.nia.ems.linkage.data.DataShareBean;
import com.nia.ems.linkage.mapper.EquipmentMapper;
import com.nia.ems.linkage.service.RoadmEmsTNMmcService;
import com.nia.ems.linkage.vo.equipment.EquipSipcVo;
import com.nia.ems.linkage.vo.equipment.EquipVo;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

@Service("RoadmEmsTNMmcService")
@RequiredArgsConstructor
public class RoadmEmsTNMmcServiceImpl implements RoadmEmsTNMmcService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoadmEmsTNMmcServiceImpl.class);

    private RoadmEmsTNClient roadmEmsTNClient;

    @Autowired
    private ObjectFactory<RoadmEmsTNClient> roadmEmsTNClientObjectFactory;

    @Autowired
    private EquipmentMapper equipmentMapper;
    @Autowired
    private PerformanceToAiPrdAmqp performanceToAiPrdAmqp;
    @Autowired
    private DataShareBean dataShareBean;
    @Value("${spring.ems.id}")
    private String emsId;

    @Value("${spring.ems.pw}")
    private String emsPw;


    @Override
    public void roadmSipcMMC () {
        LOGGER.info("=====> [RoadmEmsTNMmcService] roadmSipcMMC <=====");
        List<EquipVo> equipList = null;
        String mmc = null;
        boolean isWrnEmsLogin = false;

        try {
            equipList = equipmentMapper.selectEquipList();

            if (equipList != null && equipList.size() > 0) {
                isWrnEmsLogin = wrnEmsLogin();
                if (isWrnEmsLogin) {
//                    roadmEmsTNClient.initD();

                    for (EquipVo equip : equipList) {
                        try {

                            mmc = "RTRV-SIPC:" + equip.getIp() + "-SH1::::;\r";
//                            roadmEmsTNClient.sendCommand(mmc);
                            //    roadmMmcMsgPasing(mmcResult, "RTRV-SIPC");
                        } catch (Exception e) {
                            LOGGER.error("=====> [RoadmEmsTNMmcService] roadmSipcMMC error() " + ExceptionUtils.getStackTrace(e) + "<=====");
                            break;
                        }
                        Thread.sleep(1500);
                    }
                    try {
                        roadmEmsTNClient.disconnect();
                    } catch (Exception e) {
                        LOGGER.error("=====> [RoadmEmsTNMmcService] roadmSipcMMC error() " + ExceptionUtils.getStackTrace(e) + "<=====");
                    }
                }
            }
        } catch (Exception e) {
            if (roadmEmsTNClient != null) {
                roadmEmsTNClient.disconnect();
            }
            LOGGER.error("=====> [RoadmEmsTNMmcService] roadmSipcMMC error() " + ExceptionUtils.getStackTrace(e) + "<=====");
        } finally {
            if (roadmEmsTNClient != null) {
                roadmEmsTNClient.disconnect();
            }
        }
    }

    @Override
    public void roadmPmMMC () {
        LOGGER.info("=====> [RoadmEmsTNMmcService] roadmPmMMC TEST <=====");
        List<EquipSipcVo> equipSipcVoList;
        String mmc = null;
        String yyyyMMddHH;
        Timestamp ocrTime = null;
        Boolean isUpdateOk = true;
        boolean isWrnEmsLogin = false;
        String cmdResult;

        try {
//            Thread.sleep(120 * 1000);

            equipSipcVoList = equipmentMapper.selectEquipSipcList();

            if (equipSipcVoList != null && equipSipcVoList.size() > 0) {
                roadmEmsTNClient = roadmEmsTNClientObjectFactory.getObject();
                roadmEmsTNClient.initD();
                LOGGER.info("=====> [RoadmEmsTNMmcService] init success <=====");
                isWrnEmsLogin = wrnEmsLogin();
                if (isWrnEmsLogin) {
                    LOGGER.info("=====> [RoadmEmsTNMmcService] login success <=====");
                    for (EquipSipcVo equipSipc : equipSipcVoList) {
                        try {
                            mmc = "RTRV-PM:" + equipSipc.getSysname() + "-" + equipSipc.getSysnameName() + "::::SIGNAL=AMPPWR,TYPE=15M,INTERVAL=CURR;\r";
                            try {
                                LOGGER.info("=====> [RoadmEmsTNMmcService] sendCommand");
                                cmdResult = roadmEmsTNClient.sendCommand(mmc);
                                isUpdateOk = true;
                                LOGGER.info("=====> [RoadmEmsTNMmcService] cmd Result : " + cmdResult);
                            } catch (Exception e) {
                                LOGGER.info("=====> [RoadmEmsTNMmcService] roadmPmMMC login fail <=====");
                                isUpdateOk = false;
                                break;
                            }
//                                roadmMmcMsgPasing(mmcResult, "RTRV-PM");
                        } catch (Exception e) {
                            LOGGER.info("=====> [RoadmEmsTNMmcService] roadmPmMMC login fail <=====");
                            isUpdateOk = false;
                            break;
                        }
//                        Thread.sleep(1500);
                    }

                    yyyyMMddHH = (UtlDateHelper.getCurrentDateTime() + "").substring(0, 14);

                    if (UtlDateHelper.stringToTimestamp(yyyyMMddHH + "00:00").getTime() <= UtlDateHelper.getCurrentDateTime().getTime()
                            && UtlDateHelper.stringToTimestamp(yyyyMMddHH + "15:00").getTime() > UtlDateHelper.getCurrentDateTime().getTime()) {
                        ocrTime = UtlDateHelper.stringToTimestamp(yyyyMMddHH + "00:00");
                    } else if (UtlDateHelper.stringToTimestamp(yyyyMMddHH + "15:00").getTime() <= UtlDateHelper.getCurrentDateTime().getTime()
                            && UtlDateHelper.stringToTimestamp(yyyyMMddHH + "30:00").getTime() > UtlDateHelper.getCurrentDateTime().getTime()) {
                        ocrTime = UtlDateHelper.stringToTimestamp(yyyyMMddHH + "15:00");
                    } else if (UtlDateHelper.stringToTimestamp(yyyyMMddHH + "30:00").getTime() <= UtlDateHelper.getCurrentDateTime().getTime()
                            && UtlDateHelper.stringToTimestamp(yyyyMMddHH + "45:00").getTime() > UtlDateHelper.getCurrentDateTime().getTime()) {
                        ocrTime = UtlDateHelper.stringToTimestamp(yyyyMMddHH + "30:00");
                    } else if (UtlDateHelper.stringToTimestamp(yyyyMMddHH + "45:00").getTime() <= UtlDateHelper.getCurrentDateTime().getTime()) {
                        ocrTime = UtlDateHelper.stringToTimestamp(yyyyMMddHH + "45:00");
                    }

                    if (this.roadmEmsTNClient != null) {
                        this.roadmEmsTNClient.disconnect();

                        if (isUpdateOk) {
//                            Thread.sleep(5 * 1000);
                            LOGGER.info("=====> [RoadmEmsTNMmcService] MQ SEND  >>>>> " + ocrTime + " <=====");
                            performanceToAiPrdAmqp.sendMessageCmd(ocrTime + "");
                            //     performaceMapper.updatePerformanceUpdateTime(ocrTime+"");
                        }
                    }
                } else {
                    if (this.roadmEmsTNClient != null) {
                        this.roadmEmsTNClient.disconnect();
                    }
                    LOGGER.info("=====> [RoadmEmsTNMmcService] roadmPmMMC login fail <=====");
                }
            }
        } catch (Exception e) {
            this.roadmEmsTNClient.disconnect();
            LOGGER.error("=====> [RoadmEmsTNMmcService] roadmPmMMC error() " + ExceptionUtils.getStackTrace(e) + "<=====");
        } finally {
            this.roadmEmsTNClient.disconnect();
        }
    }

    public Boolean wrnEmsLogin () {
        String mmcResult;
        boolean isLogin = false;
        int loginCnt = 0;

        try {
//            roadmEmsTNClient.write("\r\n");

            byte[] enterBytes = {13, 10}; // \r\n
            roadmEmsTNClient.write(new String(enterBytes, "ASCII"));

            mmcResult = roadmEmsTNClient.readUntil("LOGIN:");
            roadmEmsTNClient.write(emsId + "\r\n");

            mmcResult = roadmEmsTNClient.readUntil("PASSWORD:");
            roadmEmsTNClient.write(emsPw + "\r\n");
            isLogin = true; // 로그인 성공
            LOGGER.info("[wrnEmsLogin] isLogin true <<<<<");

        } catch (NullPointerException | IllegalArgumentException e) {
            LOGGER.error(ExceptionUtils.getMessage(e));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return isLogin;
    }
}
