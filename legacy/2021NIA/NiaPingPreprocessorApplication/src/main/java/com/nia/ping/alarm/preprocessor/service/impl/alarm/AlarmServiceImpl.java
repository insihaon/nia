package com.nia.ping.alarm.preprocessor.service.impl.alarm;


import com.nia.ping.alarm.preprocessor.common.NiaCodeInfo;
import com.nia.ping.alarm.preprocessor.common.UtlDateHelper;
import com.nia.ping.alarm.preprocessor.mapper.AlarmMapper;
import com.nia.ping.alarm.preprocessor.service.alarm.AlarmService;
import com.nia.ping.alarm.preprocessor.vo.alarm.PingAlarmCntVo;
import com.nia.ping.alarm.preprocessor.vo.alarm.PingAlarmVo;
import com.nia.ping.alarm.preprocessor.vo.alarm.PingPolicyVo;
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
	private AlarmMapper alarmMapper;

	@Autowired
	private org.springframework.beans.factory.ObjectFactory<PingAlarmVo> pingAlarmVoObjectFactory;

	@Override
	public void pingAlarmCheck(PingAlarmVo pPingAlarmVo) {
		LOGGER.info(">>>>>>>>>>[AlarmService] pingAlarmCheck : " + pPingAlarmVo.toString() + " <<<<<<<<<<<<<<<<<");

		HashMap<String, String> hashMap;
		PingPolicyVo pingPolicyVo;
		PingAlarmVo pingAlarmVo = pPingAlarmVo;
		PingAlarmVo checkPingAlarmVo;

		pingPolicyVo = alarmMapper.selectPingPolicy();

		if(pingPolicyVo.getCrFaultAvg() != 0 && (pingAlarmVo.getPercentPacketLoss() >= pingPolicyVo.getCrFaultAvg())){
			pingAlarmVo.setAlarmno(alarmMapper.selectPingAlarmKey());
			pingAlarmVo.setAlarmlvl(NiaCodeInfo.ALARM_LEVEL_CRITICAL);
		} else if(pingPolicyVo.getMjFaultAvg() != 0 && (pingAlarmVo.getPercentPacketLoss() >= pingPolicyVo.getMjFaultAvg())){
			pingAlarmVo.setAlarmno(alarmMapper.selectPingAlarmKey());
			pingAlarmVo.setAlarmlvl(NiaCodeInfo.ALARM_LEVEL_MAJOR);
		} else if(pingPolicyVo.getMiFaultAvg() != 0 && (pingAlarmVo.getPercentPacketLoss() >= pingPolicyVo.getMiFaultAvg())) {
			pingAlarmVo.setAlarmno(alarmMapper.selectPingAlarmKey());
			pingAlarmVo.setAlarmlvl(NiaCodeInfo.ALARM_LEVEL_MINOR);
		} else if(pingPolicyVo.getWrFaultAvg() != 0 && (pingAlarmVo.getPercentPacketLoss() >= pingPolicyVo.getWrFaultAvg())){
			pingAlarmVo.setAlarmno(alarmMapper.selectPingAlarmKey());
			pingAlarmVo.setAlarmlvl(NiaCodeInfo.ALARM_LEVEL_WARNING);
		}else if(pingAlarmVo.getPercentPacketLoss() == 0){
			pingAlarmVo.setAlarmlvl(NiaCodeInfo.ALARM_LEVEL_CLEAR);
		}

		LOGGER.info(">>>>>>>>>>[AlarmService] pingAlarmCheck alarmLvl: " + pPingAlarmVo.getUrl() + "("+pPingAlarmVo.getAlarmlvl()+") <<<<<<<<<<<<<<<<<");

		hashMap = new HashMap<String, String>();
		hashMap.put("url", pingAlarmVo.getUrl());
		checkPingAlarmVo = alarmMapper.selectPingAlarmCheck(hashMap);

		if(checkPingAlarmVo == null){
			switch (pingAlarmVo.getAlarmlvl()){
				case NiaCodeInfo.ALARM_LEVEL_CRITICAL:
				case NiaCodeInfo.ALARM_LEVEL_MAJOR:
				case NiaCodeInfo.ALARM_LEVEL_MINOR:
				case NiaCodeInfo.ALARM_LEVEL_WARNING:
					LOGGER.info(">>>>>>>>>>[AlarmService] pingAlarmCheck insertPingAlarm("+pPingAlarmVo.getAlarmno()+") : " + pPingAlarmVo.getUrl() + "("+pPingAlarmVo.getAlarmlvl()+") <<<<<<<<<<<<<<<<<");
					alarmMapper.insertPingAlarm(pingAlarmVo);
					break;
			}
		}else{
			switch (pingAlarmVo.getAlarmlvl()){
				case NiaCodeInfo.ALARM_LEVEL_CRITICAL:
				case NiaCodeInfo.ALARM_LEVEL_MAJOR:
				case NiaCodeInfo.ALARM_LEVEL_MINOR:
				case NiaCodeInfo.ALARM_LEVEL_WARNING:
					if(!checkPingAlarmVo.getAlarmlvl().equals(pingAlarmVo.getAlarmlvl())){
						if(Integer.parseInt(checkPingAlarmVo.getAlarmlvl()) < Integer.parseInt(pingAlarmVo.getAlarmlvl())){
							LOGGER.info(">>>>>>>>>>[AlarmService] pingAlarmCheck insertPingAlarm("+pPingAlarmVo.getAlarmno()+") : " + pPingAlarmVo.getUrl() + "("+pPingAlarmVo.getAlarmlvl()+") <<<<<<<<<<<<<<<<<");
							alarmMapper.insertPingAlarm(pingAlarmVo);
						}
					}
					break;
				case NiaCodeInfo.ALARM_LEVEL_CLEAR:
					pingAlarmVo.setCleartime(UtlDateHelper.getCurrentTime());

					hashMap = new HashMap<String, String>();
					hashMap.put("url", pingAlarmVo.getUrl());
					hashMap.put("clearTime", UtlDateHelper.getCurrentTime()+"");

					LOGGER.info(">>>>>>>>>>[AlarmService] pingAlarmCheck updatePingAlarmClear() url : " + pPingAlarmVo.getUrl() + "("+pPingAlarmVo.getAlarmlvl()+") <<<<<<<<<<<<<<<<<");
					alarmMapper.updatePingAlarmClear(hashMap);
					break;
			}
		}

		alarmMapper.insertPingCollect(pingAlarmVo);
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

		}
		return pingAlarmVo;
	}
}
