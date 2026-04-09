package com.nia.engine.service.impl;

import com.nia.engine.common.RcaCodeInfo;
import com.nia.engine.data.DataShareBean;
import com.nia.engine.service.*;
//import com.nia.engine.thread.impl.TicketClearServiceThreadImpl;
import com.nia.engine.thread.impl.TicketClearServiceThreadImpl;
import com.nia.engine.vo.*;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service("EngineBootSettingService")
public class EngineBootSettingServiceImpl implements EngineBootSettingService {
	private final Logger LOGGER = Logger.getLogger(EngineBootSettingServiceImpl.class);

    @Autowired
    @Qualifier("TicketService")
    private TicketService ticketService;

    @Autowired
    @Qualifier("AlarmService")
    private AlarmService alarmService;
    
//    @Autowired
//    @Qualifier("TopologyService")
//	private TopologyService topologyService;

    @Autowired
    @Qualifier("RcaSystemConfigService")
    private RcaSystemConfigService rcaSystemConfigService;

    @Autowired
    @Qualifier("TicketClearServiceThread")
    private TicketClearServiceThreadImpl ticketClearServiceThreadImpl;

    @Autowired
    private DataShareBean dataShareBean;

    @Value("${spring.profiles}")
    private String profiles;

    @Override
    public void engineInit(){
        dataShareBean.putData(RcaCodeInfo.DATA_SHARE_NAME_TICKET_LIST,new ArrayList<RCATicket>());
        dataShareBean.putData(RcaCodeInfo.DATA_SHARE_NAME_CLEAR_AL_LIST,new ConcurrentLinkedQueue<String>());
        dataShareBean.putData(RcaCodeInfo.DATA_SHARE_NAME_CLEAR_TICKET_LIST,new ConcurrentLinkedQueue<RCATicket>());
        dataShareBean.putData(RcaCodeInfo.DATA_SHARE_NAME_IS_START, false);
        new Thread(ticketClearServiceThreadImpl).start();
        setTicket();
    }

    @Override
    public void setTicket(){
        LOGGER.info("################################################################################################");
        LOGGER.info("######################## [EngineBootSettingService] setTicket Start ########################");
        LOGGER.info("################################################################################################");
        List<RCATicket> ticketList;
        List<RCATicketAl> ticketAlList;
        HashMap<String, String> parameterMap;
        String loadDay;
        List<Integer> tcSeqList;
        boolean isStart;
        try {
            isStart = false;
            LOGGER.info("==========>[EngineBootSettingServiceImpl] setTicket isStart : " + isStart + "<==============");
            dataShareBean.putData(RcaCodeInfo.DATA_SHARE_NAME_IS_START, isStart);

            if("real".equals(profiles)) {
                parameterMap = new HashMap<String, String>();
                parameterMap.put("factor", "ticket_load_day");
                loadDay = rcaSystemConfigService.selectSystemConfig(parameterMap);

                ticketList = ticketService.selectRcaTicketList(loadDay + " DAY");
                LOGGER.info("######################## [EngineBootSettingService] ticketList size(" + ticketList.size() + ") ########################");

                if (ticketList != null && ticketList.size() > 0) {
                    for (RCATicket ticket : ticketList) {

                        ticketAlList = ticketService.selectRcaTicketAlList(ticket.getTicketId());

                        if (ticketAlList != null && ticketAlList.size() > 0) {

                            ticket.setTicketAlList(ticketAlList);

                            for (RCATicketAl ticketAl : ticketAlList) {
                                if (ticketAl.getRootCauseAlarmNoA() != null) {
                                    ticketAl.setRootCauseAlarmInfoA(alarmService.selectAlarmInfo(ticketAl.getRootCauseAlarmNoA()));
                                }

                                if (ticketAl.getRootCauseAlarmNoZ() != null) {
                                    ticketAl.setRootCauseAlarmInfoZ(alarmService.selectAlarmInfo(ticketAl.getRootCauseAlarmNoZ()));
                                }

//                                if (ticket.getTicketId().equals(ticketAl.getTicketId())) {
//                                    ticket.getTicketAlList().add(ticketAl);
//                                }
                            }
                        }
                        LOGGER.info("######################## [EngineBootSettingService] ticketList add(" + ticket.getTicketId() + ") ########################");
                        ((ArrayList<RCATicket>) dataShareBean.getData(RcaCodeInfo.DATA_SHARE_NAME_TICKET_LIST)).add(ticket);
                    }
                }
                LOGGER.info("###############################################################################################################");
                LOGGER.info("######################## [EngineBootSettingService] setTicket end ticket size(" + ((ArrayList<RCATicket>) dataShareBean.getData(RcaCodeInfo.DATA_SHARE_NAME_TICKET_LIST)).size() + ") ########################");
                LOGGER.info("###############################################################################################################");
            }

            isStart = true;
            LOGGER.info("==========>[EngineBootSettingServiceImpl] setTicket isStart : " + isStart + "<==============");
            dataShareBean.putData(RcaCodeInfo.DATA_SHARE_NAME_IS_START, isStart);
        }catch (Exception e){
            LOGGER.error(">>>>>>>>>>[EngineBootSettingService] ticketSetting() error : " + ExceptionUtils.getStackTrace(e) +" <<<<<<<<<<<<<<<<<");
        }
    }

//    @Override
//    public void setClearAlarmPool(){
//        try{
//          //  singletoneEngineData.getClearAlarmList().addAll(alarmService.selectClearAlarmPool());
//            LOGGER.info("###############################################################################################################");
//            LOGGER.info("######################## [EngineBootSettingService] setClearAlarmPool ClearAlarmPool size("+singletoneEngineData.getClearAlarmList().size()+") ########################");
//            LOGGER.info("###############################################################################################################");
//        }catch (Exception e){
//            LOGGER.error(">>>>>>>>>>[EngineBootSettingService] setClearAlarmPool() error : " + ExceptionUtils.getStackTrace(e) +" <<<<<<<<<<<<<<<<<");
//        }
//    }
}
