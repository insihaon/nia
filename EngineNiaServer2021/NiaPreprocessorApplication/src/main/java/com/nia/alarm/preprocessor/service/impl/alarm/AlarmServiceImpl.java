package com.nia.alarm.preprocessor.service.impl.alarm;


import com.nia.alarm.preprocessor.amqp.EngineClearPrdAmqp;
import com.nia.alarm.preprocessor.mapper.AlarmMapper;
import com.nia.alarm.preprocessor.service.alarm.AlarmService;
import com.nia.alarm.preprocessor.vo.alarm.AlNormalizerVo;
import com.nia.alarm.preprocessor.vo.alarm.BasicAlarmVo;
import com.nia.alarm.preprocessor.vo.alarm.ClearAlarmVo;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service("AlarmService")
public class AlarmServiceImpl implements AlarmService {
	private final static Logger LOGGER = Logger.getLogger(AlarmServiceImpl.class);


    @Autowired
    private EngineClearPrdAmqp engineClearPrdAmqp;

    @Autowired
    private AlarmMapper alarmMapper;

    @Override
	public AlNormalizerVo selectAlNormalizerInfo(BasicAlarmVo basicAlarmVo) {

        HashMap<String, String> parameterMap = null;
        AlNormalizerVo alNormalizerVo = null;

        try{
            parameterMap = new HashMap<String, String>();
            parameterMap.put("alarmMsg", basicAlarmVo.getAlarmmsgOriginal());
            parameterMap.put("unit", basicAlarmVo.getUnit());
            parameterMap.put("equiptype", basicAlarmVo.getEquiptype());

            alNormalizerVo = alarmMapper.selectAlNormalizerInfo(parameterMap);
        }catch (Exception e){
            LOGGER.error(">>>>>>>>>>[AlarmService] selectAlNormalizerInfo("+parameterMap.toString()+") error : " + ExceptionUtils.getStackTrace(e) + " <<<<<<<<<<<<<<<<<");
        }

		return alNormalizerVo;
	}

    @Override
    public void clearAlarmSendMessage(BasicAlarmVo basicAlarmVo){
        LOGGER.info(">>>>>>>>>>[AlarmFilter] clearAlarmSendMessage( "+ basicAlarmVo.getAlarmno() +" ) <<<<<<<<<<<<<<<<<");
        Boolean isClearInsert;

        try {
            isClearInsert = alarmMapper.fc_set_clear_al_pool(basicAlarmVo);

            if(isClearInsert){
                engineClearPrdAmqp.sendMessageCmd(basicAlarmVo.getAlarmno());
            }
        } catch (Exception e) {
            LOGGER.error(">>>>>>>>>>[AlarmFilter] clearAlarmSendMessage("+ basicAlarmVo.getAlarmno() +") error : " +ExceptionUtils.getStackTrace(e)+ " <<<<<<<<<<<<<<<<<");
        }
    }
}
