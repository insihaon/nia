package com.nia.ip.sdn.syslog.linkage.listener;

import com.nia.ip.sdn.syslog.linkage.common.LoggerPrint;
import com.nia.ip.sdn.syslog.linkage.common.UtlCommon;
import com.nia.ip.sdn.syslog.linkage.service.IpSdnSyslogService;
import com.nia.ip.sdn.syslog.linkage.vo.syslog.SyslogDataVo;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NiaSyslogDataMsgListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(NiaSyslogDataMsgListener.class);

    @Autowired
    @Qualifier("IpSdnSyslogService")
    private IpSdnSyslogService ipSdnSyslogService;

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<SyslogDataVo> syslogDataVoObjectFactory;

    @KafkaListener(topics = "telegraf-syslog", groupId = "syslogData")
    public void onMessege(String message){
        SyslogDataVo syslogDataVo;

        try {
            LOGGER.info(">>>>>>>>>>[NiaSyslogDataMsgListener] onMessage : " + message.toString() + " <<<<<<<<<<<<<<<<<");

            Object obj;
            syslogDataVo = syslogDataVoObjectFactory.getObject();

            obj = UtlCommon.jsonToObject(syslogDataVo, message);
            syslogDataVo = (SyslogDataVo) obj;

            ipSdnSyslogService.syslogDataHdlProcessor(syslogDataVo);

        }catch (Exception e){
            LoggerPrint.errorLog(e);

        }
    }

}
