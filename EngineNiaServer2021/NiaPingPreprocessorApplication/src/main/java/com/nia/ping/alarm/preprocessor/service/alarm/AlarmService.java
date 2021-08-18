package com.nia.ping.alarm.preprocessor.service.alarm;


import com.nia.ping.alarm.preprocessor.vo.alarm.PingAlarmVo;
import com.nia.ping.alarm.preprocessor.vo.ping.PingRowDataVo;

/**

 * @author
 *
 */
public interface AlarmService{

    void pingAlarmCheck(PingAlarmVo pPingAlarmVo);

    PingAlarmVo convertAlarmObj(PingRowDataVo pingRowDataVo);

}
