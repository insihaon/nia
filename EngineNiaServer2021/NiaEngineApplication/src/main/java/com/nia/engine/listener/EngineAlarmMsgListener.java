package com.nia.engine.listener;

import com.nia.engine.service.*;
import com.nia.engine.service.impl.SingleDomainRcaServiceImpl;
import com.nia.engine.vo.*;
import com.rabbitmq.client.Channel;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("EngineAlarmMsgListener")
public class EngineAlarmMsgListener implements ChannelAwareMessageListener {
	private static final Logger LOGGER = LoggerFactory.getLogger(EngineAlarmMsgListener.class);

	@Autowired
	private org.springframework.beans.factory.ObjectFactory<SingleDomainRcaServiceImpl> singleDomainRcaServiceFactory;

	@Autowired
    @Qualifier("ClusterService")
	private ClusterService clusterService;

	@Autowired
    @Qualifier("AlarmService")
	private AlarmService alarmService;

	@Autowired
	@Qualifier("TopologyService")
	private TopologyService topologyService;

	@Autowired
	@Qualifier("SingleRuleAsyncService")
	private SingleRuleAsyncService singleRuleAsyncService;

	private String tmpClusterNo;

	@Value("${spring.profiles}")
	private String profiles;

	@Override
	public void onMessage(Message message, Channel channel) {
		try {
			LOGGER.info(">>>>>>>>>>[EngineAlarmMsgListener] onMessage : " + message.toString() + " <<<<<<<<<<<<<<<<<");

			List<ClusterObject> clusterList;
			List<BasicAlarmVo> alarmList;
			TmpClusterObject tmpClusterObject;
			TopologyObject topologyObject;

			tmpClusterNo = new String(message.getBody());
			tmpClusterNo = tmpClusterNo.replaceAll("\"", "");


			List<ClusterObject> clusterObjectList = new ArrayList<>();
			List<BasicAlarmVo> basicAlarmVoList = new ArrayList<>();

			String tmpClusterNo = new String(message.getBody());
			tmpClusterNo = tmpClusterNo.replaceAll("\"", "");

			clusterObjectList = clusterService.selectClusterData(tmpClusterNo);
			LOGGER.info(">>>>>>>>>>[EngineAlarmMsgListener] clusterObjectList : " + clusterObjectList + " <<<<<<<<<<<<<<<<<");

			for (ClusterObject clusterObject : clusterObjectList) {
				if (clusterObject != null && clusterObject.getClusterNo() != null) {
					basicAlarmVoList = alarmService.selectAlarmMstList(clusterObject.getClusterNo());

					for(BasicAlarmVo basicAlarmVo : basicAlarmVoList){
						topologyObject = alarmService.selectAlTopology(basicAlarmVo.getAlarmno());
						basicAlarmVo.setTopology(topologyObject);
					}

					clusterObject.setBasicAlarmtVoList(basicAlarmVoList);

					sendSingleDomainHdlProcessor(clusterObject);
				}
			}
		}catch (Exception e) {
			LOGGER.error(">>>>>>>>>>[EngineAlarmMsgListener] onMessage error : " + ExceptionUtils.getStackTrace(e) + " <<<<<<<<<<<<<<<<<");
		}
	}

    public void sendSingleDomainHdlProcessor(ClusterObject clusterObject){
		SingleDomainRcaServiceImpl singleDomainRcaService;
        try{
        	singleDomainRcaService = singleDomainRcaServiceFactory.getObject();
        	singleRuleAsyncService.run(()->singleDomainRcaService.singleDomainHdlProcessor(clusterObject));
        }catch (Exception e){
            LOGGER.error(">>>>>>>>>>[EngineAlarmMsgListener] sendSingleDomainHdlProcessor error : " + ExceptionUtils.getStackTrace(e) + " <<<<<<<<<<<<<<<<<");
        }
    }
}
