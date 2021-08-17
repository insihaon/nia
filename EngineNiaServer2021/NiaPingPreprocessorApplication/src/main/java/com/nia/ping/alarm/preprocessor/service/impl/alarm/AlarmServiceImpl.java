package com.nia.ping.alarm.preprocessor.service.impl.alarm;


import com.nia.ping.alarm.preprocessor.common.NiaCodeInfo;
import com.nia.ping.alarm.preprocessor.common.UtlDateHelper;
import com.nia.ping.alarm.preprocessor.mapper.AlarmMapper;
import com.nia.ping.alarm.preprocessor.service.alarm.AlarmService;
import com.nia.ping.alarm.preprocessor.vo.alarm.PingAlarmCntVo;
import com.nia.ping.alarm.preprocessor.vo.alarm.PingAlarmVo;
import com.nia.ping.alarm.preprocessor.vo.alarm.PingPolicyVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service("AlarmService")
public class AlarmServiceImpl implements AlarmService {
	private final static Logger LOGGER = Logger.getLogger(AlarmServiceImpl.class);

	@Autowired
	private AlarmMapper alarmMapper;

	@Override
	public void pingAlarmCheck(PingAlarmVo pPingAlarmVo) {
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

		hashMap = new HashMap<String, String>();
		hashMap.put("url", pingAlarmVo.getUrl());
		checkPingAlarmVo = alarmMapper.selectPingAlarmCheck(hashMap);

		if(checkPingAlarmVo == null){
			switch (pingAlarmVo.getAlarmlvl()){
				case NiaCodeInfo.ALARM_LEVEL_CRITICAL:
				case NiaCodeInfo.ALARM_LEVEL_MAJOR:
				case NiaCodeInfo.ALARM_LEVEL_MINOR:
				case NiaCodeInfo.ALARM_LEVEL_WARNING:
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
							alarmMapper.insertPingAlarm(pingAlarmVo);
						}
					}
					break;
				case NiaCodeInfo.ALARM_LEVEL_CLEAR:
					pingAlarmVo.setCleartime(UtlDateHelper.getCurrentTime());

					hashMap = new HashMap<String, String>();
					hashMap.put("url", pingAlarmVo.getUrl());
					hashMap.put("clearTime", UtlDateHelper.getCurrentTime()+"");
					alarmMapper.updatePingAlarmClear(hashMap);
					break;
			}
		}

		alarmMapper.insertPingCollect(pingAlarmVo);
	}
}
