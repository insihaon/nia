package com.nia.engine.thread.impl;

import com.nia.engine.common.RcaCodeInfo;
import com.nia.engine.data.DataShareBean;
import com.nia.engine.service.ClearAsyncService;
import com.nia.engine.service.impl.TicketClearService;
import com.nia.engine.thread.EngineThread;
import com.nia.engine.vo.RCATicket;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentLinkedQueue;

@Service("TicketClearServiceThread")
public class TicketClearServiceThreadImpl implements EngineThread {

    private final Logger LOGGER = Logger.getLogger(TicketClearServiceThreadImpl.class);

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<TicketClearService> ticketClearServiceFactory;

    @Autowired
    private DataShareBean dataShareBean;

    @Autowired
    private ClearAsyncService clearAsyncService;

    @Override
    public void run() {
        try {
            while (true){
                if(!((ConcurrentLinkedQueue<String>)dataShareBean.getData(RcaCodeInfo.DATA_SHARE_NAME_CLEAR_AL_LIST)).isEmpty()){
                    clearHdlProcessor("alarmNo"
                            , ((ConcurrentLinkedQueue<String>)dataShareBean.getData(RcaCodeInfo.DATA_SHARE_NAME_CLEAR_AL_LIST)).poll()
                            , null
                    );
                }

                if(!((ConcurrentLinkedQueue<RCATicket>)dataShareBean.getData(RcaCodeInfo.DATA_SHARE_NAME_CLEAR_TICKET_LIST)).isEmpty()){
                    clearHdlProcessor("ticket"
                            , null
                            , ((ConcurrentLinkedQueue<RCATicket>)dataShareBean.getData(RcaCodeInfo.DATA_SHARE_NAME_CLEAR_TICKET_LIST)).poll()
                    );
                }
                Thread.sleep(100);
            }
        }catch (Exception e){
            LOGGER.error(">>>>>>>>>>[TicketClearThreadImpl] run() error : " + ExceptionUtils.getStackTrace(e) +" <<<<<<<<<<<<<<<<<");
        }
    }

    public void clearHdlProcessor(String clearGb, String alarmNo, RCATicket rcaTicket){
		TicketClearService ticketClearService;

        try{
        	ticketClearService = ticketClearServiceFactory.getObject();

        	if("ticket".equals(clearGb)){
                clearAsyncService.run(()->ticketClearService.clearTicketCheck(rcaTicket));
            }else if("alarmNo".equals(clearGb)){
        	    clearAsyncService.run(()->ticketClearService.clearAlarm(alarmNo));
            }
        }catch (Exception e){
            LOGGER.error(">>>>>>>>>>[TicketClearServiceThread] clearHdlProcessor error : " + ExceptionUtils.getStackTrace(e) + " <<<<<<<<<<<<<<<<<");
        }
    }

}


