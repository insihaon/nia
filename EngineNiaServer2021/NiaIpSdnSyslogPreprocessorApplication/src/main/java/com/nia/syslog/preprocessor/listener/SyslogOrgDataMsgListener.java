package com.nia.syslog.preprocessor.listener;

import com.nia.syslog.preprocessor.common.LoggerPrint;
import com.nia.syslog.preprocessor.common.UtlCommon;
import com.nia.syslog.preprocessor.service.NiaSyslogAlarmHdlService;
import com.nia.syslog.preprocessor.vo.syslog.SyslogDataVo;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class SyslogOrgDataMsgListener implements ChannelAwareMessageListener {
	
	@Autowired
    @Qualifier("NiaSyslogAlarmHdlService")
	private NiaSyslogAlarmHdlService niaSyslogAlarmHdlService;
	
	@Autowired
	private SyslogDataVo syslogDataVo;

	@Autowired
	private org.springframework.beans.factory.ObjectFactory<SyslogDataVo> syslogDataVoObjectFactory;

	@Override
	public void onMessage(Message message, Channel channel) {

		try {
			LoggerPrint.infoLog(message.toString());

			Object obj;
			String msg = new String(message.getBody());
			syslogDataVo = syslogDataVoObjectFactory.getObject();

		    obj = UtlCommon.jsonToObject(syslogDataVo, msg);
			syslogDataVo = (SyslogDataVo)obj;

			niaSyslogAlarmHdlService.niaSyslogHdlProcessor(syslogDataVo);
		} catch (Exception e) {
			LoggerPrint.errorLog(e);
		}
	}
}
