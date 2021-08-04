package com.nia.alarm.preprocessor.mapper;

import com.nia.alarm.preprocessor.vo.alarm.AlNormalizerVo;
import com.nia.alarm.preprocessor.vo.alarm.BasicAlarmVo;
import com.nia.alarm.preprocessor.vo.alarm.ClearAlarmVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;

@Mapper
public interface AlarmMapper{
	String selectAlarmNoCheck(HashMap<String, String> parameterMap);
	Boolean fc_set_clear_al_pool(BasicAlarmVo basicAlarmVo);
	String selectEquiptype(String sysname);
	AlNormalizerVo selectAlNormalizerInfo(HashMap<String, String> map);
}
