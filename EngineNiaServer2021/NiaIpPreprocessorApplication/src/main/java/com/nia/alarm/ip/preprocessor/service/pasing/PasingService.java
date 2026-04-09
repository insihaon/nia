package com.nia.alarm.ip.preprocessor.service.pasing;

import com.nia.alarm.ip.preprocessor.vo.alarm.AlNormalizerVo;
import com.nia.alarm.ip.preprocessor.vo.alarm.BasicAlarmVo;

public interface PasingService {

	void alarmPasing(BasicAlarmVo basicAlarmVo) throws Exception;

}
