package com.nia.alarm.ip.preprocessor.service.pasing;

import com.nia.alarm.ip.preprocessor.vo.alarm.BasicAlarmVo;

public interface CommPasingService {
    BasicAlarmVo alarmPasing(BasicAlarmVo basicAlarmVo) throws Exception;

    void setTopology(BasicAlarmVo basicAlarmVo) throws Exception;
}
