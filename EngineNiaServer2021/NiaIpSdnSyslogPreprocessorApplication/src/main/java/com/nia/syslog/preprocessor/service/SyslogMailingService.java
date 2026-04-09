package com.nia.syslog.preprocessor.service;

import com.nia.syslog.preprocessor.vo.alarm.SysLogAlarmVo;

public interface SyslogMailingService {

    boolean syslogSendMail(SysLogAlarmVo sysLogAlarmVo);

}
