package com.nia.ping.alarm.preprocessor.service;

import com.nia.ping.alarm.preprocessor.vo.ping.PingRowDataVo;

/**

 * @author
 *
 */
public interface NiaAlarmHdlService {

	void niaAlarmHdlProcessor(PingRowDataVo pingRowDataVo) throws Exception;
}
