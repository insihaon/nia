package com.nia.engine.listener;

import com.nia.engine.amqp.EngineToUiTicketPrdAmqp;
import com.nia.engine.data.DataShareBean;
import com.nia.engine.service.*;
import com.nia.engine.vo.RCATicket;
import com.nia.engine.vo.RcaEngineResult;
import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.json.simple.parser.JSONParser;

import com.nia.engine.common.RcaCodeInfo;
import com.nia.engine.common.UtlCommon;
import com.nia.engine.vo.RCATicketHandlingStatus;
import com.rabbitmq.client.Channel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;


@Service
public class UITicketMsgListener implements ChannelAwareMessageListener {
	private static final Logger LOGGER = LoggerFactory.getLogger(UITicketMsgListener.class);

	@Autowired
	private org.springframework.beans.factory.ObjectFactory<RCATicketHandlingStatus> rcaTicketHandlingStatusFactory;

	@Autowired
	private org.springframework.beans.factory.ObjectFactory<RcaEngineResult> rcaEngineResultObjectFactory;

	@Autowired
    @Qualifier("RcaTicketHandlingService")
    private RcaTicketHandlingService rcaTicketHandlingService;

	@Autowired
	private EngineToUiTicketPrdAmqp engineToUiTicketPrdAmqp;

	@Autowired
    private DataShareBean dataShareBean;

	@Autowired
	@Qualifier("FaultEventService")
	private FaultEventService faultEventService;

	@Value("${spring.profiles}")
	private String profiles;

    private HashMap<String, String> parameterMap;

	@Override
	public void onMessage(Message message, Channel channel) {
		LOGGER.info("==========>[UITicketMsgListener] onMessage : "+new String(message.getBody())+"<==============");

		try {
			RcaEngineResult rcaEngineResult = rcaEngineResultObjectFactory.getObject();
			Object obj;
			String msg = new String(message.getBody());
			RCATicketHandlingStatus rcaTicketHandlingStatus = rcaTicketHandlingStatusFactory.getObject();
			obj = UtlCommon.jsonToObject(rcaTicketHandlingStatus, msg);
			rcaTicketHandlingStatus = (RCATicketHandlingStatus)obj;

			if("reset".equals(rcaTicketHandlingStatus.getStatus())){
				((ArrayList<RCATicket>)dataShareBean.getData(RcaCodeInfo.DATA_SHARE_NAME_TICKET_LIST)).clear();
				LOGGER.info("==========>[UITicketMsgListener] size : "+((ArrayList<RCATicket>)dataShareBean.getData(RcaCodeInfo.DATA_SHARE_NAME_TICKET_LIST)).size()+"<==============");

				rcaEngineResult.setEventType("RESET");
				engineToUiTicketPrdAmqp.sendMessageCmd(rcaEngineResult);
			}
			LOGGER.info("==========>[UITicketMsgListener] rcaTicketHandlingStatus : "+rcaTicketHandlingStatus+"<==============");

			if(RcaCodeInfo.UI_REQUEST_DATA_SNAPSHOT.equals(rcaTicketHandlingStatus.getEventType())) {
				faultEventService.insertFaultEvent(String.valueOf(rcaTicketHandlingStatus.getStartTime()), String.valueOf(rcaTicketHandlingStatus.getEndTime()), rcaTicketHandlingStatus.getTitle());
			} else {
				if (RcaCodeInfo.TICKET_STATUS_FIN.equals(rcaTicketHandlingStatus.getStatus())) {

					rcaTicketHandlingService.ticketStatusModify(rcaTicketHandlingStatus);

					rcaTicketHandlingService.removeRcaTicket(rcaTicketHandlingStatus.getTicketId());

					parameterMap = new HashMap<String, String>();
					parameterMap.put("ticketId", rcaTicketHandlingStatus.getTicketId());
					parameterMap.put("ticketType", rcaTicketHandlingStatus.getTicketType());
					parameterMap.put("status", RcaCodeInfo.TICKET_STATUS_FIN);

				} else {
					rcaTicketHandlingService.ticketStatusModify(rcaTicketHandlingStatus);
				}
			}
		} catch (Exception e) {
			LOGGER.error("==========>[EngineClearAlarmMsgListener] onMessage error "+e.getMessage()+" <==============");
		}
	}
}
