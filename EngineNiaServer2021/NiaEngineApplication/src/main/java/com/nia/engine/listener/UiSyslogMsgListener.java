//package com.nia.engine.listener;
//
//import com.nia.engine.common.RcaCodeInfo;
//import com.nia.engine.common.UtlCommon;
//import com.nia.engine.service.SyslogAlarmHandlingService;
//import com.nia.engine.vo.RCATicketHandlingStatus;
//import com.nia.engine.vo.SyslogAlarmHandlingStatus;
//import com.rabbitmq.client.Channel;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.amqp.core.Message;
//import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Service
//public class UiSyslogMsgListener  implements ChannelAwareMessageListener {
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(UiSyslogMsgListener.class);
//
//    @Autowired
//    private org.springframework.beans.factory.ObjectFactory<SyslogAlarmHandlingStatus> syslogAlarmHandlingStatusObjectFactory;
//
//    @Autowired
//    private SyslogAlarmHandlingService syslogAlarmHandlingService;
//
//    @Override
//    public void onMessage(Message message, Channel channel){
//        LOGGER.info("==========>[UITicketMsgListener] onMessage : "+new String(message.getBody())+"<==============");
//
//        try {
//            Object obj;
//            String msg = new String(message.getBody());
//            SyslogAlarmHandlingStatus syslogAlarmHandlingStatus = syslogAlarmHandlingStatusObjectFactory.getObject();
//            obj = UtlCommon.jsonToObject(syslogAlarmHandlingStatus, msg);
//            syslogAlarmHandlingStatus = (SyslogAlarmHandlingStatus)obj;
//
//
//            if (RcaCodeInfo.SYSLOG_STATUS_FIN.equals(syslogAlarmHandlingStatus.getStatus())){
//                syslogAlarmHandlingService.syslogAlarmStatusModify(syslogAlarmHandlingStatus);
//            }
//            LOGGER.info("==========>[UITicketMsgListener] rcaTicketHandlingStatus : "+syslogAlarmHandlingStatus+"<==============");
//
//
//
//        }catch (Exception e){
//            LOGGER.error("==========>[UiSyslogMsgListener] onMessage error "+e.getMessage()+" <==============");
//        }
//
//
//
//    }
//}
