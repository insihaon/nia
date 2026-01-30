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

import java.util.ArrayList;
import java.util.List;

@Service
public class NiaSyslogDataMsgListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(NiaSyslogDataMsgListener.class);

    @Autowired
    @Qualifier("IpSdnSyslogService")
    private IpSdnSyslogService ipSdnSyslogService;

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<SyslogDataVo> syslogDataVoObjectFactory;

    @KafkaListener(topics = "telegraf-syslog", groupId = "syslogData")
    public void onMessege(List<String> messages){
        List<SyslogDataVo> syslogDataVoList = new ArrayList<>();

        try {
            LOGGER.info(">>>>>>>>>>[NiaSyslogDataMsgListener] Batch size: " + messages.size() + " <<<<<<<<<<<<<<<<<");

            // 1. 메시지 리스트를 순회하며 개별 객체로 변환하고 리스트에 추가
            for (String message : messages) {
                SyslogDataVo syslogDataVo = syslogDataVoObjectFactory.getObject();
                Object obj = UtlCommon.jsonToObject(syslogDataVo, message);
                syslogDataVo = (SyslogDataVo)obj;

                // 객체 리스트에 추가
                syslogDataVoList.add(syslogDataVo);
            }

            ipSdnSyslogService.syslogDataHdlProcessor(syslogDataVoList);
        }catch (Exception e){
            LoggerPrint.errorLog(e);

        }
    }

}
