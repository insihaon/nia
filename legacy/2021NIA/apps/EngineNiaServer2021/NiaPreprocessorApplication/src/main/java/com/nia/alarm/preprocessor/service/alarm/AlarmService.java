package com.nia.alarm.preprocessor.service.alarm;

import com.nia.alarm.preprocessor.vo.alarm.AlNormalizerVo;
import com.nia.alarm.preprocessor.vo.alarm.BasicAlarmVo;

/**

 * @author
 *
 */
public interface AlarmService{

    AlNormalizerVo selectAlNormalizerInfo(BasicAlarmVo basicAlarmVo);
    void clearAlarmSendMessage(BasicAlarmVo basicAlarmVo);

}
