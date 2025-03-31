package com.nia.rca.cluster.preprocessor.service.impl.cluster;

import com.nia.rca.cluster.preprocessor.common.UtlDateHelper;
import com.nia.rca.cluster.preprocessor.mapper.ClusterMapper;
import com.nia.rca.cluster.preprocessor.service.cluster.AlarmClusterService;
import com.nia.rca.cluster.preprocessor.service.cluster.ClusterService;
import com.nia.rca.cluster.preprocessor.vo.BasicAlarmVo;
import com.nia.rca.cluster.preprocessor.vo.ClusterObject;
import com.nia.rca.cluster.preprocessor.vo.TmpClusterObject;
import lombok.Synchronized;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service("ClusterService")
public class ClusterServiceImpl implements ClusterService {
	private final static Logger LOGGER = Logger.getLogger(ClusterServiceImpl.class);

    @Autowired
    private ClusterMapper clusterMapper;

    @Autowired
    @Qualifier("AlarmClusterService")
    private AlarmClusterService alarmClusterService;

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<TmpClusterObject> tmpClusterObjectFactory;

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<AlarmClusterServiceImpl> AlarmClusterServiceFactory;

    private List<TmpClusterObject> tmpClusterList = new  ArrayList<TmpClusterObject>();
    private List<AlarmClusterService> alarmClusterServiceImplList = new ArrayList<AlarmClusterService>();

    @Value("${spring.cluster.timeCondition}")
    private long timeCondition;

    @Override
    @Synchronized
    public void startThreadPool(BasicAlarmVo basicAlarmVo){
        boolean insertionDone = false;
        long startTimeDiff;
        long endTimeDiff;

        try {
            if (tmpClusterList.size() > 0) {
                        for(TmpClusterObject tmpClusterObject : tmpClusterList){
                            if(!tmpClusterObject.isClusterMaxTime() && !tmpClusterObject.isStop()){
                                startTimeDiff = basicAlarmVo.getReceivetime().getTime() - tmpClusterObject.getStartTime().getTime();
                        endTimeDiff = basicAlarmVo.getReceivetime().getTime() - tmpClusterObject.getEndTime().getTime();

                        if (startTimeDiff < (timeCondition * -1)) {
                            if(tmpClusterObject.getCreateTime().getTime() > basicAlarmVo.getReceivetime().getTime()+timeCondition){
                                continue;
                            }
                            createCluster(basicAlarmVo);
                            insertionDone = true;
                            break;
                        }else {
                            if (endTimeDiff <= timeCondition) {
                                if(tmpClusterObject.getEndTime().getTime() < basicAlarmVo.getReceivetime().getTime()){
                                    tmpClusterObject.setEndTime(basicAlarmVo.getReceivetime());
                                }

                                if(tmpClusterObject.getStartTime().getTime() > basicAlarmVo.getReceivetime().getTime()){
                                    tmpClusterObject.setStartTime(basicAlarmVo.getReceivetime());
                                }
                                basicAlarmVo.setTmpClusterno(tmpClusterObject.getTmpClusterSeq());

                                for(AlarmClusterService alarmClusterService : alarmClusterServiceImplList){
                                    alarmClusterService.onMessage(basicAlarmVo);
                                }
                                insertionDone = true;
                                break;
                            }
                        }
                    }
                }

                if (!insertionDone) {
                    createCluster(basicAlarmVo);
                }
            }else {
                createCluster(basicAlarmVo);
            }

         //   Thread.sleep(50);
        }catch( Exception e ) {
            LOGGER.error("=====> [ClusterService] startThreadPool("+basicAlarmVo.getAlarmno()+") error : "+ ExceptionUtils.getStackTrace(e)+" <=====");
        }
    }

    @Override
    @Synchronized
    public void createCluster(BasicAlarmVo basicAlarmVo){
        String tmpClusterNo;
        TmpClusterObject tmpClusterObject;
        StringBuffer strLog;
        try {
            tmpClusterNo = selectTmpClusterKey();
            tmpClusterObject = tmpClusterObjectFactory.getObject();
            tmpClusterObject.setTmpClusterSeq(tmpClusterNo);
            tmpClusterObject.setStartTime(basicAlarmVo.getReceivetime());
            tmpClusterObject.setEndTime(basicAlarmVo.getReceivetime());
            tmpClusterObject.setClusterStartTime(UtlDateHelper.getCurrentTime());
            tmpClusterObject.setCreateTime(basicAlarmVo.getReceivetime());
            tmpClusterList.add(tmpClusterObject);

            basicAlarmVo.setTmpClusterno(tmpClusterNo);

            alarmClusterService = AlarmClusterServiceFactory.getObject();
            alarmClusterService.getInstance(basicAlarmVo,tmpClusterObject);
            alarmClusterServiceImplList.add(alarmClusterService);

          //  new Thread(alarmClusterThread).start();

            strLog = new StringBuffer();
            strLog.append("=====> [ClusterService] createCluster <=====\n");
            strLog.append("alarmno : " + basicAlarmVo.getAlarmno()+"\n");
            strLog.append("alarmtime : " + basicAlarmVo.getAlarmtime()+"\n");
            strLog.append("receivetime : " + basicAlarmVo.getReceivetime()+"\n");
            strLog.append("tmpClusterSeq : " + tmpClusterObject.getTmpClusterSeq()+"\n");
            strLog.append("cluStartTime : " + tmpClusterObject.getStartTime()+"\n");
            strLog.append("cluEndTime : " + tmpClusterObject.getEndTime()+"\n");
            strLog.append("---------------------------------------------------------------");
            LOGGER.info(strLog.toString());
        //    Thread.sleep(50);
        }catch( Exception e ) {
            LOGGER.error("=====> [ClusterService] createCluster error "+ ExceptionUtils.getStackTrace(e)+ "<=====");
        }
    }

    @Override
    @Synchronized
    public void removeCluster(String tmpClusterNo){
        try{
            LOGGER.info("=====> [ClusterService] removeCluster tmpClusterObject("+tmpClusterNo+") <=====");
            Iterator<TmpClusterObject> tmpItr;
            Iterator<AlarmClusterService> actItr;
            TmpClusterObject tmpClusterObject;
            if(tmpClusterList.size() > 0){
                tmpItr = tmpClusterList.iterator();
                while( tmpItr.hasNext() ) {
                    tmpClusterObject = tmpItr.next();
                    if(tmpClusterObject.getTmpClusterSeq().equals(tmpClusterNo)){
                        tmpItr.remove();
                        //tmpClusterList.remove(tmpItr);
                    }
                }
            }

            if(alarmClusterServiceImplList.size() > 0){
                actItr = alarmClusterServiceImplList.iterator();
                while( actItr.hasNext() ) {
                    alarmClusterService = actItr.next();
                    if(alarmClusterService.getTmpClusterKey().equals(tmpClusterNo)){
                        actItr.remove();
                        //alarmClusterServiceImplList.remove(actItr);
                    }
                }
            }
        }catch (Exception e) {
            LOGGER.error(">>>>>>>>>>[ClusterService] removeCluster error() : " + ExceptionUtils.getStackTrace(e) + " <<<<<<<<<<<<<<<<<");
        }
    }
	
	/**
	 * 클러스터 key 생성
	 * @param
	 * @return
	 */
	@Override
	public String selectTmpClusterKey(){
		return clusterMapper.selectTmpClusterKey();
	}
	
	/**
	 * 클러스터 key 생성
	 * @param
	 * @return
	 */
    @Override
	public String selectClusterKey(){
		return clusterMapper.selectClusterKey();
	}

	/**
	 * TMP 클러스터 저장
	 * @param tmpClusterObject
	 * @return
	 */
    @Override
	public void insertTmpCluster(TmpClusterObject tmpClusterObject){
		clusterMapper.insertTmpCluster(tmpClusterObject);
	}
	
	/**
	 * 클러스터 저장
	 * @param clusterObject
	 * @return
	 */
    @Override
	public void insertCluster(ClusterObject clusterObject) {
		Map<String, Object> paramMap;
		try{

            StringBuffer strLog = new StringBuffer();
            strLog.append("=====> [ClusterService] insertCluster <=====\n");
            strLog.append("ClusterNo : " + clusterObject.getClusterNo()+"\n");

            try{
                clusterMapper.insertCluster(clusterObject);
            }catch (Exception e1){
                LOGGER.error(">>>>>>>>>>>> ClusterService insertCluster("+clusterObject.getClusterNo()+"/"+clusterObject.getTmpClusterNo()+") error : "+ ExceptionUtils.getStackTrace(e1)+" <<<<<<<<<<<<<<<<");
            }
		}catch (Exception e) {
			LOGGER.error(">>>>>>>>>>>> ClusterService insertCluster("+clusterObject.getClusterNo()+") error : "+ ExceptionUtils.getStackTrace(e)+" <<<<<<<<<<<<<<<<");
		}
		
	}

}
