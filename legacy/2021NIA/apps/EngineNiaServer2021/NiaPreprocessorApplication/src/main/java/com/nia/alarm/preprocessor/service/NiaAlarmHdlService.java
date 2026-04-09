package com.nia.alarm.preprocessor.service;

import com.nia.alarm.preprocessor.vo.alarm.BasicAlarmVo;

/**

 * @author
 *
 */
public interface NiaAlarmHdlService {

	void niaAlarmHdlProcessor(BasicAlarmVo basicAlarmVo) throws Exception;
}
