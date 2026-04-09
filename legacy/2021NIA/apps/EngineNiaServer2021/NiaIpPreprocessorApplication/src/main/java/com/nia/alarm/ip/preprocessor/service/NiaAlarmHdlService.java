package com.nia.alarm.ip.preprocessor.service;

import com.nia.alarm.ip.preprocessor.vo.alarm.AlarmVo;
import com.nia.alarm.ip.preprocessor.vo.alarm.BasicAlarmVo;

/**

 * @author
 *
 */
public interface NiaAlarmHdlService {

	void niaAlarmHdlProcessor(AlarmVo alarmVo) throws Exception;
}
