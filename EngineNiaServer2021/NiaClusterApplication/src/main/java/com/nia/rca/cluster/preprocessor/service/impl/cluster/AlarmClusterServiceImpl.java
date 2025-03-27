package com.nia.rca.cluster.preprocessor.service.impl.cluster;

import com.nia.rca.cluster.preprocessor.amqp.EnginePrdAmqp;
import com.nia.rca.cluster.preprocessor.common.RcaCodeInfo;
import com.nia.rca.cluster.preprocessor.common.UtlDateHelper;
import com.nia.rca.cluster.preprocessor.data.DataShareBean;
import com.nia.rca.cluster.preprocessor.service.alarm.AlarmService;
import com.nia.rca.cluster.preprocessor.service.cluster.AlarmClusterService;
import com.nia.rca.cluster.preprocessor.service.cluster.ClusterService;
import com.nia.rca.cluster.preprocessor.vo.BasicAlarmVo;
import com.nia.rca.cluster.preprocessor.vo.ClusterInfoVo;
import com.nia.rca.cluster.preprocessor.vo.ClusterObject;
import com.nia.rca.cluster.preprocessor.vo.TmpClusterObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

@Service("AlarmClusterService")
@Scope(value = "prototype")
public class AlarmClusterServiceImpl implements AlarmClusterService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AlarmClusterServiceImpl.class);

    @Autowired
    private EnginePrdAmqp enginePrdAmqp;

    @Autowired
    @Qualifier("ClusterService")
    private ClusterService clusterService;

    @Autowired
    @Qualifier("AlarmService")
    private AlarmService alarmService;

    @Autowired
    private TmpClusterObject tmpClusterObject;

    @Autowired
    private DataShareBean dataShareBean;

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<ClusterInfoVo> clusterInfoVoObjectFactory;

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<ClusterObject> clusterObjectFactory;

    private Timestamp startTime;
    private Timestamp endTime;

    private Timer timer;
    private TimerTask task;

    @Value("${spring.cluster.timeCondition}")
    private long timeCondition;

    @Value("${spring.cluster.delay}")
    private long delay;
    private List<ClusterObject> clusterObjectList;

    private String tmpClusterNo;

    @Override
    public AlarmClusterServiceImpl getInstance(BasicAlarmVo basicAlarmVo, TmpClusterObject tmpClusterObject) {
        try {
            this.tmpClusterObject = tmpClusterObject;
            tmpClusterNo = tmpClusterObject.getTmpClusterSeq();
            clusterObjectList = new ArrayList<ClusterObject>();
            createCluster(basicAlarmVo);
            clusterTimerStart();
        } catch (Exception e) {
            LOGGER.error(">>>>>>>>>>[AlarmClusterService] getInstance() error : " + ExceptionUtils.getStackTrace(e) + " tmpClusterKey : " + tmpClusterObject.getTmpClusterSeq() + " <<<<<<<<<<<<<<<<<");
        }
        return this;
    }

    /*
     * 같은 sysName 또는 ocaSeq를 가지고 있는 알람끼리 클러스터 구성
     */
    @Override
    public void clustering(BasicAlarmVo basicAlarmVo) {

        boolean isSysNameFind = false;
        boolean isTrunkIdFind = false;
        boolean isOppSysNameFind = false;
        boolean isAddAlarm = false;

        try {
            if (endTime.getTime() <= basicAlarmVo.getReceivetime().getTime()) {
                endTime = basicAlarmVo.getReceivetime();
            }

            if (startTime.getTime() > basicAlarmVo.getReceivetime().getTime()) {
                startTime = basicAlarmVo.getReceivetime();
            }

            StringBuffer strLog = new StringBuffer();
            strLog.append("=====> [AlarmClusterService] clustering <=====\n");
            strLog.append("tmpClusterno : " + basicAlarmVo.getTmpClusterno() + "\n");
            strLog.append("startTime : " + startTime + "\n");
            strLog.append("endTime : " + endTime + "\n");
            strLog.append("alarmNo : " + basicAlarmVo.getAlarmno() + "\n");
            strLog.append("alarmtime : " + basicAlarmVo.getAlarmtime() + "\n");
            strLog.append("receiveTime : " + basicAlarmVo.getReceivetime() + "\n");
            strLog.append("equipType : " + basicAlarmVo.getEquiptype() + "\n");
            strLog.append("sysname : " + basicAlarmVo.getSysname() + "\n");
            if (basicAlarmVo.getTopologyObject() != null) {
                strLog.append("oppSysname : " + basicAlarmVo.getTopologyObject().getOppSysname() + "\n");
            }
            strLog.append("---------------------------------------------------------------");
            LOGGER.info(strLog.toString());

            clusterTimerStop();

            if (clusterObjectList.size() > 0) {
                for (ClusterObject clusterObject : clusterObjectList) {
                    if (basicAlarmVo.getTopologyObject() != null) {
                        //                        if(StringUtils.isNotEmpty(basicAlarmVo.getTopologyObject()
                        //                        .getTrunkId())){
                        //                            isTrunkIdFind = clusterObject.findTrunkId(basicAlarmVo
                        //                            .getTopologyObject().getTrunkId());
                        //                        }

                        isSysNameFind = clusterObject.findSysName(basicAlarmVo.getSysname());

                        if (! StringUtils.isEmpty(basicAlarmVo.getTopologyObject().getOppSysname())) {
                            isOppSysNameFind =
                                    clusterObject.findSysName(basicAlarmVo.getTopologyObject().getOppSysname());
                        }
                    } else {
                        isSysNameFind = clusterObject.findSysName(basicAlarmVo.getSysname());
                    }

                    if (isSysNameFind || isOppSysNameFind || isTrunkIdFind) {
                        clusterObject.addEquipList(basicAlarmVo.getSysname());

                        if (basicAlarmVo.getTopologyObject() != null && ! StringUtils.isEmpty(basicAlarmVo.getTopologyObject().getOppSysname())) {
                            clusterObject.addEquipList(basicAlarmVo.getTopologyObject().getOppSysname());
                        }

                        basicAlarmVo.setClusterno(clusterObject.getClusterNo());
                        clusterObject.addAlarmNoList(basicAlarmVo);
                        isAddAlarm = true;
                        break;
                    }
                    isSysNameFind = false;
                    isOppSysNameFind = false;
                    isTrunkIdFind = false;
                }

                if (isAddAlarm) {
                    basicAlarmVo.setTmpClusterno(tmpClusterNo);
                    alarmService.insertAlarm(basicAlarmVo);
                } else {
                    createCluster(basicAlarmVo);
                }
                isAddAlarm = false;
            }

            clusterMerge();

            clusterTimerStart();
        } catch (Exception e) {
            LOGGER.error(">>>>>>>>>>[AlarmClusterService] clustering() error : " + ExceptionUtils.getStackTrace(e) +
                    " tmpClusterKey : " + tmpClusterObject.getTmpClusterSeq() + " <<<<<<<<<<<<<<<<<");
        }
    }

    @Override
    public void clusterMerge() {
        HashSet<Integer> clusterNoSet = null;
        String minClusterNo = null;
        List<ClusterObject> tmpClusterList;
        HashMap<String, String> parameterMap;
        Iterator<ClusterObject> clItr;
        ClusterObject tmpClusterObject;
        LinkedHashSet<String> linkedHashSet;

        try {
            if (clusterObjectList.size() > 0) {
                clusterNoSet = new HashSet<Integer>();

                for (ClusterObject clusterObject1 : clusterObjectList) {
                    for (ClusterObject clusterObject2 : clusterObjectList) {
                        if (! clusterObject1.getClusterNo().equals(clusterObject2.getClusterNo())) {
                            if(clusterObject1.getEquipList() != null && clusterObject2.getEquipList() != null){
                                for (String sysname : clusterObject2.getEquipList()) {
                                    if (clusterObject1.getEquipList().contains(sysname)) {
                                        clusterNoSet.add(Integer.parseInt(clusterObject1.getClusterNo()));
                                    }
                                }
                            }

                            for (String trunkId : clusterObject2.getTrunkIdList()) {
                                if (clusterObject1.getTrunkIdList().contains(trunkId)) {
                                    clusterNoSet.add(Integer.parseInt(clusterObject1.getClusterNo()));
                                }
                            }
                        }
                    }
                }

                if (clusterNoSet.size() > 1) {
                    tmpClusterList = new ArrayList<ClusterObject>();
                    minClusterNo = Integer.toString(((int) Collections.min(clusterNoSet)));

                    //   LOGGER.info(">>>>>>>>>>> [AlarmClusterThreadImpl] clusterMerge minClusterNo
                    //   ("+minClusterNo+") <<<<<<<<<<<<<<<<<<");
                    for (int tmpClusterNo : clusterNoSet) {
                        clItr = clusterObjectList.iterator();
                        while (clItr.hasNext()) {
                            tmpClusterObject = clItr.next();

                            if (tmpClusterNo == Integer.parseInt(tmpClusterObject.getClusterNo())) {
                                //  LOGGER.info(">>>>>>>>>>> [AlarmClusterThreadImpl] clusterMerge
                                //  ("+clusterObjectList.get(i).getClusterNo()+") <<<<<<<<<<<<<<<<<<");
                                tmpClusterList.add(tmpClusterObject);
                                clItr.remove();
                            }
                        }
                    }

                    if (tmpClusterList.size() > 0) {
                        for (ClusterObject clusterObject : tmpClusterList) {
                            if (clusterObject.getClusterNo().equals(minClusterNo)) {
                                for (ClusterObject nextClusterObject : tmpClusterList) {
                                    if (! clusterObject.getClusterNo().equals(nextClusterObject.getClusterNo())) {
                                        if (clusterObject.getEquipList() != null && clusterObject.getEquipList().size() > 0) {
                                            clusterObject.getEquipList().addAll(nextClusterObject.getEquipList());

                                            linkedHashSet = new LinkedHashSet<>(clusterObject.getEquipList());
                                            clusterObject.setEquipList(new ArrayList<>(linkedHashSet));
                                        }

                                        if (clusterObject.getAlarmTypeList() != null && clusterObject.getAlarmTypeList().size() > 0) {
                                            clusterObject.getAlarmTypeList().addAll(nextClusterObject.getAlarmTypeList());

                                            linkedHashSet = new LinkedHashSet<>(clusterObject.getAlarmTypeList());
                                            clusterObject.setAlarmTypeList(new ArrayList<>(linkedHashSet));
                                        }

                                        if (clusterObject.getTrunkIdList() != null && clusterObject.getTrunkIdList().size() > 0) {
                                            clusterObject.getTrunkIdList().addAll(nextClusterObject.getTrunkIdList());

                                            linkedHashSet = new LinkedHashSet<>(clusterObject.getTrunkIdList());
                                            clusterObject.setTrunkIdList(new ArrayList<>(linkedHashSet));
                                        }
                                    }
                                }
                            } else {
                                parameterMap = new HashMap<String, String>();
                                parameterMap.put("clusterno", clusterObject.getClusterNo());
                                parameterMap.put("newClusterno", minClusterNo);
                                alarmService.updateAlarmClusterNo(parameterMap);
                            }
                            clusterObject.setClusterNo(minClusterNo);
                            clusterObjectList.add(clusterObject);
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error(">>>>>>>>>>[AlarmClusterService] clusterMerge() error : " + ExceptionUtils.getStackTrace(e) + " tmpClusterKey : " + this.tmpClusterObject.getTmpClusterSeq() + " <<<<<<<<<<<<<<<<<");
        }
    }

    @Override
    public void clusterTimerStart() {
        try {
            //LOGGER.info(">>>>>>>>>>[AlarmClusterThreadImpl] clusterTimerStart() timer run clusterKey : " +
            // clusterKey + " <<<<<<<<<<<<<<<<<");
            timer = new Timer();
            task = new TimerTask() {
                @Override
                public void run() {
                    try {
                        //singletoneAlarmData.addAlarmTmpClusterList(tmpClusterObject);

                        tmpClusterObject.setEndTime(endTime);
                        if (! tmpClusterObject.isStop()) {
                            tmpClusterObject.setClusterEndTime(UtlDateHelper.getCurrentTime());
                        }
                        clusterService.insertTmpCluster(tmpClusterObject);

                        if (clusterObjectList != null && clusterObjectList.size() > 0) {
                            for (ClusterObject clusterObject : clusterObjectList) {
                                if (! StringUtils.isEmpty(clusterObject.getTmpClusterNo())) {
                                    clusterObject.setTmpClusterNo(tmpClusterNo);
                                }
                                clusterService.insertCluster(clusterObject);
                            }
                            //	singletoneAlarmData.addInspectoerAlQue(tmpClusterNo);
                            tmpClusterObject.setClusterList(clusterObjectList);
                        }
                        enginePrdAmqp.sendMessageCmd(tmpClusterNo);
                        //  showText();
                        LOGGER.info(">>>>>>>>>>[AlarmClusterService] clusterTimerStart() sendMessage : " + tmpClusterObject.getTmpClusterSeq() + " <<<<<<<<<<<<<<<<<");

                        clusterThreadStop();
                        clusterTimerStop();
                        Thread.sleep(100);
                        //isSendMessage = true;
                    } catch (Exception e) {
                        LOGGER.error(">>>>>>>>>>[AlarmClusterService] clusterTimerStart() run error : " + ExceptionUtils.getStackTrace(e) + " <<<<<<<<<<<<<<<<<");
                    }
                }
            };
            timer.schedule(task, delay);
        } catch (Exception e) {
            LOGGER.error(">>>>>>>>>>[AlarmClusterService] clusterTimerStart() error(" + tmpClusterObject.getTmpClusterSeq() + ") : " + ExceptionUtils.getStackTrace(e) + " <<<<<<<<<<<<<<<<<");
        }
    }

    @Override
    public void createCluster(BasicAlarmVo basicAlarmVo) {
        try {
            //  LOGGER.info(">>>>>>>>>>[AlarmClusterThreadImpl] createCluster() basicAlarmVo : " + basicAlarmVo
            //  .getAlarmno() + " <<<<<<<<<<<<<<<<<");
            String clusterNo = null;
            ClusterInfoVo clusterInfoVo = null;
            ClusterObject clusterObject = null;
            clusterNo = clusterService.selectClusterKey();
            clusterObject = clusterObjectFactory.getObject();
            clusterObject.setTmpClusterNo(tmpClusterNo);
            clusterObject.setClusterNo(clusterNo);
            if(basicAlarmVo.getSysname() != null){
                clusterObject.addEquipList(basicAlarmVo.getSysname());
            }

            basicAlarmVo.setClusterno(clusterNo);
            clusterInfoVo = clusterInfoVoObjectFactory.getObject();

            if (basicAlarmVo.getTopologyObject() != null) {
                if (StringUtils.isNotEmpty(basicAlarmVo.getTopologyObject().getOppSysname())) {
                    clusterObject.addEquipList(basicAlarmVo.getTopologyObject().getOppSysname());
                }

                if (StringUtils.isNotEmpty(basicAlarmVo.getTopologyObject().getTrunkId())) {
                    clusterObject.addTrunkIdList(basicAlarmVo.getTopologyObject().getTrunkId());
                }
                clusterInfoVo.setTmpClusterNo(tmpClusterNo);
                clusterInfoVo.setClusterNo(clusterNo);
            } else {
                clusterInfoVo.setTmpClusterNo(tmpClusterNo);
                clusterInfoVo.setClusterNo(clusterNo);
            }

            if (clusterInfoVo != null) {
                clusterObject.setClusterInfoVo(clusterInfoVo);
            }

            clusterObject.setClusterInfoVo(clusterInfoVo);
            clusterObjectList.add(clusterObject);
            endTime = basicAlarmVo.getReceivetime();
            startTime = basicAlarmVo.getReceivetime();
            basicAlarmVo.setTmpClusterno(tmpClusterNo);
            clusterObject.addAlarmNoList(basicAlarmVo);
            alarmService.insertAlarm(basicAlarmVo);
        } catch (Exception e) {
            LOGGER.error(">>>>>>>>>>[AlarmClusterService] createCluster error(" + tmpClusterObject.getTmpClusterSeq() + ") : " + ExceptionUtils.getStackTrace(e) + " <<<<<<<<<<<<<<<<<");
        }
    }

    @Override
    public void clusterThreadStop() {
        try {
            //singletoneAlarmData.addStopClusterQue(tmpClusterObject.getTmpClusterSeq());
            ((Queue<String>) dataShareBean.getData(RcaCodeInfo.DATA_SHARE_NAME_STOP_CL_QUE)).offer(tmpClusterObject.getTmpClusterSeq());
            //singletoneAlarmData.alarmTmpClusterListModify("D",null,tmpClusterKey);
            //singletoneAlarmData.removeAlarmTmpClusterList(tmpClusterKey);
        } catch (Exception e) {
            LOGGER.error(">>>>>>>>>>[AlarmClusterService] clusterThreadStop error(" + tmpClusterObject.getTmpClusterSeq() + ") : " + ExceptionUtils.getStackTrace(e) + " <<<<<<<<<<<<<<<<<");
        }
    }

    @Override
    public void clusterTimerStop() {
        try {
            if (timer != null) {
                timer.cancel();
                task.cancel();
            }
        } catch (Exception e) {
            LOGGER.error(">>>>>>>>>>[AlarmClusterService] clusterTimerStop(" + tmpClusterObject.getTmpClusterSeq() +
                    ") error : " + ExceptionUtils.getStackTrace(e) + " <<<<<<<<<<<<<<<<<");
        }
    }

    public void onMessage(BasicAlarmVo basicAlarmVo) {
        if (this.tmpClusterObject.getTmpClusterSeq().equals(basicAlarmVo.getTmpClusterno())) {
            clustering(basicAlarmVo);

            //	LOGGER.info("clustering "+singletoneAlarmData.getCnt());
            //	clustering(basicAlarmVo);
            //			alarmQue.offer(basicAlarmVo);
            tmpClusterObject.setClusterEndTime(UtlDateHelper.getCurrentTime());
            //run();
        }
    }

    @Override
    public String getTmpClusterKey() {
        return tmpClusterObject.getTmpClusterSeq();
    }
}


