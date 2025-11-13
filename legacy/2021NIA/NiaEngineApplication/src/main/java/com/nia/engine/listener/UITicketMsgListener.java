package com.nia.engine.listener;

import com.nia.engine.amqp.EngineToUiTicketPrdAmqp;
import com.nia.engine.common.RcaCodeInfo;
import com.nia.engine.common.UtlCommon;
import com.nia.engine.data.DataShareBean;
import com.nia.engine.service.*;
import com.nia.engine.service.impl.SingleDomainRcaServiceImpl;
import com.nia.engine.vo.RCATicketHandlingStatus;
import com.nia.engine.vo.RcaEngineResult;
import com.nia.engine.vo.aiTraffic.anomalous.AnomalousTrafficListVo;
import com.nia.engine.vo.aiTraffic.noxious.NoxiousTrafficListVo;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;


@Service
public class UITicketMsgListener implements ChannelAwareMessageListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(UITicketMsgListener.class);

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<RCATicketHandlingStatus> rcaTicketHandlingStatusFactory;

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<RcaEngineResult> rcaEngineResultObjectFactory;

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<AnomalousTrafficListVo> perfListVoObjectFactory;

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<NoxiousTrafficListVo> noxiousTrafficListVoObjectFactory;

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<SingleDomainRcaServiceImpl> singleDomainRcaServiceFactory;

    @Autowired
    @Qualifier("ClusterService")
    private ClusterService clusterService;

    @Autowired
    @Qualifier("AlarmService")
    private AlarmService alarmService;

    @Autowired
    @Qualifier("RcaTicketHandlingService")
    private RcaTicketHandlingService rcaTicketHandlingService;

    @Autowired
    private EngineToUiTicketPrdAmqp engineToUiTicketPrdAmqp;

    @Autowired
    private DataShareBean dataShareBean;


    @Autowired(required = false)
    @Qualifier("FaultEventService")
    private FaultEventService faultEventService;

    private HashMap<String, String> parameterMap;

    @Autowired
    @Qualifier("RcaTrafficTicketService")
    private RcaTrafficTicketService rcaTrafficTicketService;

    @Override
    public void onMessage (Message message, Channel channel) {
        LOGGER.info("==========>[UITicketMsgListener] onMessage : " + new String(message.getBody()) + "<==============");

        try {
            RcaEngineResult rcaEngineResult = rcaEngineResultObjectFactory.getObject();
            Object obj;
            String msg = new String(message.getBody());
            RCATicketHandlingStatus rcaTicketHandlingStatus = rcaTicketHandlingStatusFactory.getObject();
            obj = UtlCommon.jsonToObject(rcaTicketHandlingStatus, msg);
            rcaTicketHandlingStatus = (RCATicketHandlingStatus) obj;

            LOGGER.info("==========>[UITicketMsgListener] rcaTicketHandlingStatus : " + rcaTicketHandlingStatus + "<==============");
            if (RcaCodeInfo.UI_REQUEST_DATA_SNAPSHOT.equals(rcaTicketHandlingStatus.getEventType())) {
                faultEventService.insertFaultEvent(String.valueOf(rcaTicketHandlingStatus.getStartTime()), String.valueOf(rcaTicketHandlingStatus.getEndTime()), rcaTicketHandlingStatus.getTitle(), rcaTicketHandlingStatus.getDetail());
            } else if (RcaCodeInfo.UI_REQUEST_DATA_TICKET.equals(rcaTicketHandlingStatus.getEventType())) {
                LOGGER.info("==========>[UITicketMsgListener] rcaTicketHandlingStatus TICKET <==============");

                if (RcaCodeInfo.TICKET_STATUS_FIN.equals(rcaTicketHandlingStatus.getStatus())) {
                    LOGGER.info("==========>[UITicketMsgListener] rcaTicketHandlingStatus FIN <==============");
                    LOGGER.info("==========> [rcaTicketHandlingStatus] :: " + rcaTicketHandlingStatus.toString());

                    rcaTicketHandlingService.ticketStatusModify(rcaTicketHandlingStatus);
                    rcaTicketHandlingService.removeRcaTicket(rcaTicketHandlingStatus.getTicketId());

                    parameterMap = new HashMap<String, String>();
                    parameterMap.put("ticketId", rcaTicketHandlingStatus.getTicketId());
                    parameterMap.put("ticketType", rcaTicketHandlingStatus.getTicketType());
                    parameterMap.put("status", RcaCodeInfo.TICKET_STATUS_FIN);
                } else {
                    rcaTicketHandlingService.ticketStatusModify(rcaTicketHandlingStatus);
                }
            } else if (RcaCodeInfo.UI_REQUEST_DATA_SYSLOG.equals(rcaTicketHandlingStatus.getEventType())) {
                LOGGER.info("==========>[UITicketMsgListener] rcaTicketHandlingStatus SYSLOG <==============");
                rcaTicketHandlingService.syslogAlarmStatusModify(rcaTicketHandlingStatus);
                rcaTicketHandlingService.syslogSopSave(rcaTicketHandlingStatus);

                LOGGER.info("==========>[UITicketMsgListener] rcaTicketHandlingStatus SYSLOG : " + rcaTicketHandlingStatus + "<==============");
            }
        } catch (Exception e) {
            LOGGER.error("==========>[EngineClearAlarmMsgListener] onMessage error " + e.getMessage() + " <==============");
        }
    }
}
