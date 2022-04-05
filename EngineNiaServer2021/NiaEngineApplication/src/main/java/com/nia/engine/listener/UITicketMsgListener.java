package com.nia.engine.listener;

import com.nia.engine.amqp.EngineToUiTicketPrdAmqp;
import com.nia.engine.data.DataShareBean;
import com.nia.engine.service.*;
import com.nia.engine.service.impl.SingleDomainRcaServiceImpl;
import com.nia.engine.vo.*;
import com.nia.engine.vo.aiTraffic.anomalous.AnomalousTrafficListVo;
import com.nia.engine.vo.aiTraffic.noxious.NoxiousTrafficListVo;
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
import com.rabbitmq.client.Channel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;


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

	@Autowired
	@Qualifier("FaultEventService")
	private FaultEventService faultEventService;

	@Value("${spring.profiles}")
	private String profiles;

    private HashMap<String, String> parameterMap;

	@Autowired
	@Qualifier("RcaTrafficTicketService")
	private RcaTrafficTicketService rcaTrafficTicketService;

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

			AnomalousTrafficListVo anomalousTrafficListVo;
			NoxiousTrafficListVo noxiousTrafficListVo;

//			if("reset".equals(rcaTicketHandlingStatus.getStatus())){
//				((ArrayList<RCATicket>)dataShareBean.getData(RcaCodeInfo.DATA_SHARE_NAME_TICKET_LIST)).clear();
//				LOGGER.info("==========>[UITicketMsgListener] size : "+((ArrayList<RCATicket>)dataShareBean.getData(RcaCodeInfo.DATA_SHARE_NAME_TICKET_LIST)).size()+"<==============");
//
//				rcaEngineResult.setEventType("RESET");
//				engineToUiTicketPrdAmqp.sendMessageCmd(rcaEngineResult);
//
//				Thread.sleep( 1000);
//
//				msg = "{\"pattern\":\"\",\"data\":[{\"strifid\":\"1625035212164\",\"inttimestamp\":1639549388,\"fltbpsin\":0,\"fltbpsin_predict\":\"0.0\",\"fltbpsin_threshold_upper\":\"3998.0\",\"fltbpsin_threshold_lower\":\"0.0\",\"fltbpsin_anomaly\":false,\"fltbpsout\":3229,\"fltbpsout_predict\":\"3155.0\",\"fltbpsout_threshold_upper\":\"7836.0\",\"fltbpsout_threshold_lower\":\"912.0\",\"fltbpsout_anomaly\":false},{\"strifid\":\"1625035212180\",\"inttimestamp\":1639549388,\"fltbpsin\":11619,\"fltbpsin_predict\":\"10826.0\",\"fltbpsin_threshold_upper\":\"44678.0\",\"fltbpsin_threshold_lower\":\"0.0\",\"fltbpsin_anomaly\":false,\"fltbpsout\":11839,\"fltbpsout_predict\":\"11008.0\",\"fltbpsout_threshold_upper\":\"35592.0\",\"fltbpsout_threshold_lower\":\"255.0\",\"fltbpsout_anomaly\":false}]}";
//				anomalousTrafficListVo = perfListVoObjectFactory.getObject();
//
//				obj = UtlCommon.jsonToObject(anomalousTrafficListVo, msg);
//				anomalousTrafficListVo = (AnomalousTrafficListVo)obj;
//
//				rcaTrafficTicketService.createAnomalousTrafficTicket(anomalousTrafficListVo);
//
//				msg = "{\"pattern\":\"\",\"data\":[{\"strresip\":\"116.89.161.14\",\"strresname\":\"seoul-n9k\",\"strs_ip\":\"116.89.161.55\",\"strs_port\":\"21746\",\"strd_ip\":\"116.89.161.16\",\"strd_port\":\"4789\",\"strs_mac\":\"00-A3-8E-07-95-67\",\"strd_mac\":\"70-DB-98-D9-CF-E7\",\"strprotocol\":\"17\",\"stripv4tos\":\"0\",\"strchannel\":\"Other UDP\",\"strsenderip\":\"116.89.161.14\",\"strin_interface\":\"CORE_Et1/2_서울-대전_10G\",\"strout_interface\":\"(436225024)Ethernet1/35\",\"strbytes_col\":\"11386880\",\"strcounts\":\"2\",\"dateregdate\":\"2021-12-15 15:19:53\",\"anomaly\":\"0\",\"id\":\"248009\"},{\"strresip\":\"116.89.161.14\",\"strresname\":\"seoul-n9k\",\"strs_ip\":\"142.250.196.110\",\"strs_port\":\"443\",\"strd_ip\":\"203.255.253.194\",\"strd_port\":\"60339\",\"strs_mac\":\"A8-0C-0D-52-D1-B7\",\"strd_mac\":\"70-DB-98-D9-CF-E7\",\"strprotocol\":\"17\",\"stripv4tos\":\"0\",\"strchannel\":\"Other UDP\",\"strsenderip\":\"116.89.161.14\",\"strin_interface\":\"Et1/1 인터링크 ASR9K 10G\",\"strout_interface\":\"NREN_Et1/23_강남세브란스병원(본원)_10G\",\"strbytes_col\":\"15675392\",\"strcounts\":\"3\",\"dateregdate\":\"2021-12-15 15:19:53\",\"anomaly\":\"0\",\"id\":\"248010\"},{\"strresip\":\"116.89.161.14\",\"strresname\":\"seoul-n9k\",\"strs_ip\":\"61.252.53.162\",\"strs_port\":\"80\",\"strd_ip\":\"59.7.3.105\",\"strd_port\":\"32960\",\"strs_mac\":\"70-DB-98-D9-CF-E7\",\"strd_mac\":\"A8-0C-0D-52-D1-B7\",\"strprotocol\":\"6\",\"stripv4tos\":\"0\",\"strchannel\":\"HTTP\",\"strsenderip\":\"116.89.161.14\",\"strin_interface\":\"CORE_Et1/4_서울-판교_10G\",\"strout_interface\":\"Et1/1 인터링크 ASR9K 10G\",\"strbytes_col\":\"12288000\",\"strcounts\":\"2\",\"dateregdate\":\"2021-12-15 15:19:53\",\"anomaly\":\"0\",\"id\":\"248011\"}]}";
//				noxiousTrafficListVo = noxiousTrafficListVoObjectFactory.getObject();
//
//				obj = UtlCommon.jsonToObject(noxiousTrafficListVo, msg);
//				noxiousTrafficListVo = (NoxiousTrafficListVo)obj;
//
//				rcaTrafficTicketService.createNoxiousTrfficTicket(noxiousTrafficListVo);
//
//				Thread.sleep( 1000);
//				String tmpClusterNo;
//				TopologyObject topologyObject;
//				SingleDomainRcaServiceImpl singleDomainRcaService;
//
//				List<ClusterObject> clusterObjectList = new ArrayList<>();
//				List<BasicAlarmVo> basicAlarmVoList = new ArrayList<>();
//
//				tmpClusterNo = "25092";
//				tmpClusterNo = tmpClusterNo.replaceAll("\"", "");
//
//				clusterObjectList = clusterService.selectClusterData(tmpClusterNo);
//				LOGGER.info(">>>>>>>>>>[EngineAlarmMsgListener] clusterObjectList : " + clusterObjectList + " <<<<<<<<<<<<<<<<<");
//
//				for (ClusterObject clusterObject : clusterObjectList) {
//					if (clusterObject != null && clusterObject.getClusterNo() != null) {
//						basicAlarmVoList = alarmService.selectAlarmMstList(clusterObject.getClusterNo());
//
//						for (BasicAlarmVo basicAlarmVo : basicAlarmVoList) {
//							topologyObject = alarmService.selectAlTopology(basicAlarmVo.getAlarmno());
//							basicAlarmVo.setTopology(topologyObject);
//						}
//
//						clusterObject.setBasicAlarmtVoList(basicAlarmVoList);
//
//						singleDomainRcaService = singleDomainRcaServiceFactory.getObject();
//						singleDomainRcaService.singleDomainHdlProcessor(clusterObject);
//					}
//				}
//
//				Thread.sleep( 1000);
//
//				clusterObjectList = new ArrayList<>();
//				basicAlarmVoList = new ArrayList<>();
//
//				tmpClusterNo = "25093";
//				tmpClusterNo = tmpClusterNo.replaceAll("\"", "");
//
//				clusterObjectList = clusterService.selectClusterData(tmpClusterNo);
//				LOGGER.info(">>>>>>>>>>[EngineAlarmMsgListener] clusterObjectList : " + clusterObjectList + " <<<<<<<<<<<<<<<<<");
//
//				for (ClusterObject clusterObject : clusterObjectList) {
//					if (clusterObject != null && clusterObject.getClusterNo() != null) {
//						basicAlarmVoList = alarmService.selectAlarmMstList(clusterObject.getClusterNo());
//
//						for (BasicAlarmVo basicAlarmVo : basicAlarmVoList) {
//							topologyObject = alarmService.selectAlTopology(basicAlarmVo.getAlarmno());
//							basicAlarmVo.setTopology(topologyObject);
//						}
//
//						clusterObject.setBasicAlarmtVoList(basicAlarmVoList);
//
//						singleDomainRcaService = singleDomainRcaServiceFactory.getObject();
//						singleDomainRcaService.singleDomainHdlProcessor(clusterObject);
//					}
//				}
//
//				Thread.sleep(1000);
//
//				clusterObjectList = new ArrayList<>();
//				basicAlarmVoList = new ArrayList<>();
//
//				tmpClusterNo = "25041";
//				tmpClusterNo = tmpClusterNo.replaceAll("\"", "");
//
//				clusterObjectList = clusterService.selectClusterData(tmpClusterNo);
//				LOGGER.info(">>>>>>>>>>[EngineAlarmMsgListener] clusterObjectList : " + clusterObjectList + " <<<<<<<<<<<<<<<<<");
//
//				for (ClusterObject clusterObject : clusterObjectList) {
//					if (clusterObject != null && clusterObject.getClusterNo() != null) {
//						basicAlarmVoList = alarmService.selectAlarmMstList(clusterObject.getClusterNo());
//
//						for (BasicAlarmVo basicAlarmVo : basicAlarmVoList) {
//							topologyObject = alarmService.selectAlTopology(basicAlarmVo.getAlarmno());
//							basicAlarmVo.setTopology(topologyObject);
//						}
//
//						clusterObject.setBasicAlarmtVoList(basicAlarmVoList);
//
//						singleDomainRcaService = singleDomainRcaServiceFactory.getObject();
//						singleDomainRcaService.singleDomainHdlProcessor(clusterObject);
//					}
//				}
//			}
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
