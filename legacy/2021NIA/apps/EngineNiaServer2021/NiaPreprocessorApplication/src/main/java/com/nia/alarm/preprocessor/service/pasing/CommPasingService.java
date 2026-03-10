package com.nia.alarm.preprocessor.service.pasing;

import com.nia.alarm.preprocessor.vo.alarm.BasicAlarmVo;

public interface CommPasingService {
    BasicAlarmVo alarmPasing(BasicAlarmVo basicAlarmVo) throws Exception;

    void setPortPasing(BasicAlarmVo basicAlarmVo) throws Exception;

    void setTopology(BasicAlarmVo basicAlarmVo) throws Exception;
}
