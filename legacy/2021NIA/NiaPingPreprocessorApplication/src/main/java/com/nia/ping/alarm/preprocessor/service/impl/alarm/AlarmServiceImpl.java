package com.nia.ping.alarm.preprocessor.service.impl.alarm;


import com.nia.ping.alarm.preprocessor.amqp.ClusterPrdAmqp;
import com.nia.ping.alarm.preprocessor.amqp.EngineClearPrdAmqp;
import com.nia.ping.alarm.preprocessor.common.NiaCodeInfo;
import com.nia.ping.alarm.preprocessor.common.UtlDateHelper;
import com.nia.ping.alarm.preprocessor.mapper.AlarmMapper;
import com.nia.ping.alarm.preprocessor.mapper.EquipmentMapper;
import com.nia.ping.alarm.preprocessor.service.alarm.AlarmService;
import com.nia.ping.alarm.preprocessor.vo.alarm.BasicAlarmVo;
import com.nia.ping.alarm.preprocessor.vo.alarm.PingAlarmCntVo;
import com.nia.ping.alarm.preprocessor.vo.alarm.PingAlarmVo;
import com.nia.ping.alarm.preprocessor.vo.alarm.PingPolicyVo;
import com.nia.ping.alarm.preprocessor.vo.euqipment.NodeInfoVo;
import com.nia.ping.alarm.preprocessor.vo.ping.PingRowDataVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;

@Service("AlarmService")
public class AlarmServiceImpl implements AlarmService {
	private final static Logger LOGGER = Logger.getLogger(AlarmServiceImpl.class);

	@Autowired
	private ClusterPrdAmqp clusterPrdAmqp;

	@Autowired
	private EngineClearPrdAmqp engineClearPrdAmqp;

	@Autowired
	private AlarmMapper alarmMapper;

	@Autowired
	private EquipmentMapper equipmentMapper;

	@Autowired
	private org.springframework.beans.factory.ObjectFactory<PingAlarmVo> pingAlarmVoObjectFactory;

	@Autowired
	private org.springframework.beans.factory.ObjectFactory<BasicAlarmVo> basicAlarmVoObjectFactory;

	@Override
	public void pingAlarmCheck(PingAlarmVo pPingAlarmVo) {
		LOGGER.info(">>>>>>>>>>[AlarmService] pingAlarmCheck : " + pPingAlarmVo.toString() + " <<<<<<<<<<<<<<<<<");

		HashMap<String, String> hashMap;
		PingPolicyVo pingPolicyVo;
		PingAlarmVo pingAlarmVo = pPingAlarmVo;
		PingAlarmVo checkPingAlarmVo;
		BasicAlarmVo basicAlarmVo;
		BasicAlarmVo pingAlarmCheck;
		NodeInfoVo nodeInfoVo;

		try {
			basicAlarmVo = basicAlarmVoObjectFactory.getObject();
			pingPolicyVo = alarmMapper.selectPingPolicy();

			basicAlarmVo.setAlarmno("P"+alarmMapper.selectPingAlarmKey());
			basicAlarmVo.setAlarmtime(pingAlarmVo.getAlarmtime());
			basicAlarmVo.setReceivetime(pingAlarmVo.getAlarmtime());
			basicAlarmVo.setIpAddr(pingAlarmVo.getUrl());
			basicAlarmVo.setAlarmloc("ipAddr="+pingAlarmVo.getUrl());
			basicAlarmVo.setAlarmmsg("PING_UNREACHABLE");
			basicAlarmVo.setAlarmCode("port down");

			hashMap = new HashMap<String, String>();
			hashMap.put("ipAddr", basicAlarmVo.getIpAddr());
			nodeInfoVo = equipmentMapper.selectNodeMst(hashMap);

			if(nodeInfoVo != null){
				basicAlarmVo.setEquipCode(nodeInfoVo.getNodeNum());
				basicAlarmVo.setSysname(nodeInfoVo.getNodeId());
				basicAlarmVo.setEquiptype(nodeInfoVo.getModelNm());
			}

			if(pingPolicyVo.getCrFaultAvg() != 0 && (pingAlarmVo.getPercentPacketLoss() >= pingPolicyVo.getCrFaultAvg())){
				basicAlarmVo.setAlarmlevel(NiaCodeInfo.ALARM_LEVEL_CRITICAL);
			} else if(pingPolicyVo.getMjFaultAvg() != 0 && (pingAlarmVo.getPercentPacketLoss() >= pingPolicyVo.getMjFaultAvg())){
				basicAlarmVo.setAlarmlevel(NiaCodeInfo.ALARM_LEVEL_MAJOR);
			} else if(pingPolicyVo.getMiFaultAvg() != 0 && (pingAlarmVo.getPercentPacketLoss() >= pingPolicyVo.getMiFaultAvg())) {
				basicAlarmVo.setAlarmlevel(NiaCodeInfo.ALARM_LEVEL_MINOR);
			} else if(pingPolicyVo.getWrFaultAvg() != 0 && (pingAlarmVo.getPercentPacketLoss() >= pingPolicyVo.getWrFaultAvg())){
				basicAlarmVo.setAlarmlevel(NiaCodeInfo.ALARM_LEVEL_WARNING);
			}else if(pingAlarmVo.getPercentPacketLoss() == 0){
				basicAlarmVo.setAlarmlevel(NiaCodeInfo.ALARM_LEVEL_CLEAR);
			}

			LOGGER.info(">>>>>>>>>>[AlarmService] pingAlarmCheck alarmLvl: " + basicAlarmVo.getAlarmloc() + "("+basicAlarmVo.getAlarmlevel()+") <<<<<<<<<<<<<<<<<");

			hashMap = new HashMap<String, String>();
			hashMap.put("url", basicAlarmVo.getAlarmloc());
			pingAlarmCheck = alarmMapper.selectPingAlarmCheck(hashMap);

			if(pingAlarmCheck == null){
				switch (basicAlarmVo.getAlarmlevel()){
					case NiaCodeInfo.ALARM_LEVEL_CRITICAL:
					case NiaCodeInfo.ALARM_LEVEL_MAJOR:
					case NiaCodeInfo.ALARM_LEVEL_MINOR:
					case NiaCodeInfo.ALARM_LEVEL_WARNING:
						LOGGER.info(">>>>>>>>>>[AlarmService] pingAlarmCheck insertPingAlarm("+basicAlarmVo.getAlarmno()+") : " + basicAlarmVo.getAlarmloc() + "("+basicAlarmVo.getAlarmlevel()+") <<<<<<<<<<<<<<<<<");
//						alarmMapper.insertPingAlarm(pingAlarmVo);
						clusterPrdAmqp.sendMessageCmd(basicAlarmVo);
						break;
				}
			}else{
				switch (basicAlarmVo.getAlarmlevel()){
					case NiaCodeInfo.ALARM_LEVEL_CRITICAL:
					case NiaCodeInfo.ALARM_LEVEL_MAJOR:
					case NiaCodeInfo.ALARM_LEVEL_MINOR:
					case NiaCodeInfo.ALARM_LEVEL_WARNING:
						if(!pingAlarmCheck.getAlarmlevel().equals(basicAlarmVo.getAlarmlevel())){
							if(Integer.parseInt(pingAlarmCheck.getAlarmlevel()) < Integer.parseInt(basicAlarmVo.getAlarmlevel())){
								LOGGER.info(">>>>>>>>>>[AlarmService] pingAlarmCheck insertPingAlarm("+basicAlarmVo.getAlarmno()+") : " + basicAlarmVo.getAlarmloc() + "("+basicAlarmVo.getAlarmlevel()+") <<<<<<<<<<<<<<<<<");
//								alarmMapper.insertPingAlarm(pingAlarmVo);
								clusterPrdAmqp.sendMessageCmd(basicAlarmVo);
							}
						}
						break;
					case NiaCodeInfo.ALARM_LEVEL_CLEAR:
						LOGGER.info(">>>>>>>>>>[AlarmService] pingAlarmCheck updatePingAlarmClear() url : " + basicAlarmVo.getAlarmloc() + "("+basicAlarmVo.getAlarmlevel()+") <<<<<<<<<<<<<<<<<");
						alarmMapper.fcSetClearAlPool(basicAlarmVo);
						engineClearPrdAmqp.sendMessageCmd(basicAlarmVo.getAlarmno());
						break;
					default:
						break;
				}
			}
			alarmMapper.insertPingCollect(pingAlarmVo);
		}catch (Exception e){
			LOGGER.error("=====> [AlarmService] pingAlarmCheck error "+ ExceptionUtils.getStackTrace(e)+ "<=====");
		}
	}

	@Override
	public PingAlarmVo convertAlarmObj(PingRowDataVo pingRowDataVo) {
		PingAlarmVo pingAlarmVo = null;

		String yyyyMMddHH = null;
		String mm = null;
		String currentTime = null;

		Calendar cal = Calendar.getInstance();
		Timestamp time = null;

		try {
			pingAlarmVo = pingAlarmVoObjectFactory.getObject();

			if(pingRowDataVo.getPingRowTagsVo() != null){
				if(StringUtils.isNotEmpty(pingRowDataVo.getPingRowTagsVo().getHost())){
					pingAlarmVo.setHost(pingRowDataVo.getPingRowTagsVo().getHost());
				}

				if(StringUtils.isNotEmpty(pingRowDataVo.getPingRowTagsVo().getUrl())){
					pingAlarmVo.setUrl(pingRowDataVo.getPingRowTagsVo().getUrl());
				}
			}

			if(pingRowDataVo.getPingRowFiledsVo() != null){
				if(StringUtils.isNotEmpty(pingRowDataVo.getPingRowFiledsVo().getPacketsReceived())){
					pingAlarmVo.setPacketsReceived(pingRowDataVo.getPingRowFiledsVo().getPacketsReceived());
				}

				if(StringUtils.isNotEmpty(pingRowDataVo.getPingRowFiledsVo().getPacketsTransmitted())){
					pingAlarmVo.setPacketsTransmitted(pingRowDataVo.getPingRowFiledsVo().getPacketsTransmitted());
				}

				pingAlarmVo.setPercentPacketLoss(pingRowDataVo.getPingRowFiledsVo().getPercentPacketLoss());

				if(StringUtils.isNotEmpty(pingRowDataVo.getPingRowFiledsVo().getResultCode())){
					pingAlarmVo.setResultCode(pingRowDataVo.getPingRowFiledsVo().getResultCode());
				}
			}

			cal.setTimeInMillis(UtlDateHelper.getCurrentTime().getTime());

//                    cal.add(Calendar.HOUR, -10);
			time = new Timestamp(cal.getTime().getTime());

			yyyyMMddHH = (time+"").substring(0,14);
			mm = (time+"").substring(14,16);

			mm = (Math.round(Integer.parseInt(mm)/5)*5)+"";

			currentTime = yyyyMMddHH+mm+":00";


			pingAlarmVo.setAlarmtime(UtlDateHelper.stringToTimestamp(currentTime));
		}catch (Exception e){
			LOGGER.error("=====> [AlarmService] convertAlarmObj error "+ ExceptionUtils.getStackTrace(e)+ "<=====");
		}
		return pingAlarmVo;
	}
}
