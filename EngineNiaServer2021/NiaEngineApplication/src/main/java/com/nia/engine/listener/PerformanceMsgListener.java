package com.nia.engine.listener;

import com.nia.engine.common.RcaCodeInfo;
import com.nia.engine.common.UtlCommon;
import com.nia.engine.data.DataShareBean;
import com.nia.engine.service.PerformanceTicketService;
import com.nia.engine.service.RcaTicketHandlingService;
import com.nia.engine.service.RcaTicketManagerService;
import com.nia.engine.service.TicketService;
import com.nia.engine.vo.PerformanceClusterVo;
import com.nia.engine.vo.RCATicketHandlingStatus;
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
import java.util.concurrent.ConcurrentLinkedQueue;


@Service
public class PerformanceMsgListener implements ChannelAwareMessageListener {
	private static final Logger LOGGER = LoggerFactory.getLogger(PerformanceMsgListener.class);

	@Autowired
	private org.springframework.beans.factory.ObjectFactory<PerformanceClusterVo> performanceClusterVoObjectFactory;

	@Autowired
    @Qualifier("RcaTicketHandlingService")
    private RcaTicketHandlingService rcaTicketHandlingService;

	@Autowired
    private DataShareBean dataShareBean;

	@Autowired
	@Qualifier("TicketService")
	private TicketService ticketService;

	@Value("${spring.profiles}")
	private String profiles;

    private HashMap<String, String> parameterMap;

	@Autowired
	@Qualifier("RcaTicketManagerService")
	private RcaTicketManagerService rcaTicketManagerService;

	@Override
	public void onMessage(Message message, Channel channel) {
		LOGGER.info("==========>[UITicketMsgListener] onMessage : "+new String(message.getBody())+"<==============");
		try {
			Object obj;
			String msg = new String(message.getBody());
			PerformanceClusterVo performanceClusterVo = performanceClusterVoObjectFactory.getObject();
			obj = UtlCommon.jsonToObject(performanceClusterVo, msg);
			performanceClusterVo = (PerformanceClusterVo)obj;

			rcaTicketManagerService.createPerformanceTicket(performanceClusterVo);

		} catch (Exception e) {
			LOGGER.error("==========>[EngineClearAlarmMsgListener] onMessage error "+e.getMessage()+" <==============");
		}
	}
}
