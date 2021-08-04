package com.nia.engine.service.impl;

import com.nia.engine.common.RcaCodeInfo;
import com.nia.engine.common.UtlDateHelper;
import com.nia.engine.data.DataShareBean;
import com.nia.engine.service.AlarmService;
import com.nia.engine.service.RcaSystemConfigService;
import com.nia.engine.singleton.SingletoneEngineData;
import com.nia.engine.vo.BasicAlarmVo;
import com.nia.engine.vo.RCATicket;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class EngineSchedulerJobServiceImpl extends QuartzJobBean{
	private final Logger LOGGER = Logger.getLogger(EngineSchedulerJobServiceImpl.class);

    @Autowired
    @Qualifier("RcaSystemConfigService")
    private RcaSystemConfigService rcaSystemConfigService;

    @Autowired
    @Qualifier("AlarmService")
    private AlarmService alarmService;

    @Autowired
    private DataShareBean dataShareBean;

    private HashMap<String, String> parameterMap;

    private SingletoneEngineData singletoneEngineData = SingletoneEngineData.getInstance();

    @Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
        removeTicket();
       // removeClearAlarm();
	}

//    public void removeClearAlarm(){
//        Iterator<BasicAlarmVo> itr;
//        BasicAlarmVo basicAlarmVo;
//        long resultTime;
//        String maxDay;
//        try{
//            parameterMap = new  HashMap<String, String>();
//            parameterMap.put("factor", "clear_al_max_time");
//            maxDay = rcaSystemConfigService.selectSystemConfig(parameterMap);
//            if(singletoneEngineData.getClearAlarmList().size() > 0){
//                itr = singletoneEngineData.getClearAlarmList().iterator();
//                while( itr.hasNext() ) {
//                    basicAlarmVo = itr.next();
//                    resultTime = (UtlDateHelper.getCurrentTime().getTime()-basicAlarmVo.getReceivetime().getTime());
//                    if((resultTime/(1000*60*60*24)) > Integer.parseInt(maxDay)){
//                        alarmService.deleteClearAlarmPool(basicAlarmVo.getAlarmno());
//                        itr.remove();
//                    }
//                }
//            }
//        }catch (Exception e){
//            LOGGER.error("=====> [EngineSchedulerJobService] removeClearAlarm error "+ExceptionUtils.getStackTrace(e)+" <=====");
//        }
//    }

    public void removeTicket(){
        long resultTime;
        String maxDay;
        Iterator<RCATicket> itr;
        RCATicket rcaTicket;
        try {
            // maxDay일 이상 지난 티켓은 서버 리스트에서 삭제
            parameterMap = new  HashMap<String, String>();
            parameterMap.put("factor", "ticket_max_day");
            maxDay = rcaSystemConfigService.selectSystemConfig(parameterMap);
            if(((ArrayList<RCATicket>)dataShareBean.getData(RcaCodeInfo.DATA_SHARE_NAME_TICKET_LIST)).size() > 0){
                itr = ((ArrayList<RCATicket>)dataShareBean.getData(RcaCodeInfo.DATA_SHARE_NAME_TICKET_LIST)).iterator();
                while( itr.hasNext() ) {
                    rcaTicket = itr.next();
                    resultTime = (UtlDateHelper.getCurrentTime().getTime()-rcaTicket.getTicketGenerationTime().getTime());
                    if((resultTime/(1000*60*60*24)) > Integer.parseInt(maxDay)){
                        LOGGER.info("=====> [EngineSchedulerJobService] removeTicket("+rcaTicket.getTicketId()+") <=====");
                        itr.remove();
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("=====> [EngineSchedulerJobService] removeTicket error "+ExceptionUtils.getStackTrace(e)+" <=====");
        }
    }
}