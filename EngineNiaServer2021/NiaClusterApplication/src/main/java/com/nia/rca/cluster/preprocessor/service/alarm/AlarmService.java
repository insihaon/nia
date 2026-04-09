package com.nia.rca.cluster.preprocessor.service.alarm;


import com.nia.rca.cluster.preprocessor.vo.BasicAlarmVo;

import java.util.HashMap;

/**

 * @author
 *
 */
public interface AlarmService{

	void insertAlarm(BasicAlarmVo basicAlarmVo) throws Exception;

	void updateAlarmClusterNo(HashMap<String, String> paramMap) throws Exception;

}
