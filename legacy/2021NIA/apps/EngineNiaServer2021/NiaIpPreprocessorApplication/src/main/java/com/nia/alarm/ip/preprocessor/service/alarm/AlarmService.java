package com.nia.alarm.ip.preprocessor.service.alarm;

import com.nia.alarm.ip.preprocessor.vo.alarm.AlNormalizerVo;
import com.nia.alarm.ip.preprocessor.vo.alarm.AlarmVo;
import com.nia.alarm.ip.preprocessor.vo.alarm.BasicAlarmVo;

/**

 * @author
 *
 */
public interface AlarmService{

    AlNormalizerVo selectAlNormalizerInfo(BasicAlarmVo basicAlarmVo);
    BasicAlarmVo convertAlarmObj(AlarmVo alarmVo);
    void clearAlarmSendMessage(BasicAlarmVo basicAlarmVo);

}
