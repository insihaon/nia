//package com.nia.rca.test.simulator.service;
//
//import java.util.ArrayList;
//
//import com.nia.rca.test.simulator.amqp.AlarmPrdAmqp;
//import com.nia.rca.test.simulator.mapper.RcaResetMapper;
//import org.apache.commons.lang.exception.ExceptionUtils;
//import org.apache.log4j.Logger;
//import org.quartz.DisallowConcurrentExecution;
//import org.quartz.JobExecutionContext;
//import org.quartz.JobExecutionException;
//import org.quartz.PersistJobDataAfterExecution;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.quartz.QuartzJobBean;
//
//import com.nia.rca.test.simulator.mapper.AlarmMapper;
//import com.nia.rca.test.simulator.singleton.SingletoneAlarmData;
//import com.nia.rca.test.simulator.vo.AlarmVo;
//
//
//@PersistJobDataAfterExecution
//@DisallowConcurrentExecution
//public class AlarmSchedulerJobService extends QuartzJobBean{
//	private final Logger LOGGER = Logger.getLogger(AlarmSchedulerJobService.class);
//
//	@Autowired
//	private AlarmMapper alarmMapper;
//
//	private ArrayList<AlarmVo> alarmList;
//
//	private SingletoneAlarmData singletoneAlarmData = SingletoneAlarmData.getInstance();
//
//	@Override
//	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
//		try {
//            alarmList = new ArrayList<AlarmVo>();
//			alarmList = alarmMapper.selectAlarmList();
//            StringBuffer strLog;
//			if(alarmList.size() > 0){
////                monitorMapper.updateMonitoring("yd001");
//				for(int i=0; i<alarmList.size(); i++){
//                    strLog = new StringBuffer();
//                    strLog.append("=====> [AlarmSchedulerJobService] insertAlarm <=====\n");
//                    strLog.append("alarmNo : " + alarmList.get(i).getAlarmno()+"\n");
//                    strLog.append("receivetime : " + alarmList.get(i).getReceivetime()+"\n");
//                    strLog.append("---------------------------------------------------------------");
//                    LOGGER.info(strLog);
//                    singletoneAlarmData.alarmQueOffer(alarmList.get(i));
//                //    singletoneAlarmData.alarmListModify("I",alarmList.get(i),null);
//                }
//			}
//		} catch (Exception e) {
//			LOGGER.error("=====> [AlarmSchedulerJobService] error "+ExceptionUtils.getStackTrace(e)+" <=====");
//		}
//	}
//
//}
