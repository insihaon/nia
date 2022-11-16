package com.nia.ip.sdn.syslog.linkage.service;

import com.nia.ip.sdn.syslog.linkage.amqp.SyslogDataPrdAmqp;
import com.nia.ip.sdn.syslog.linkage.common.LoggerPrint;
import com.nia.ip.sdn.syslog.linkage.mapper.SyslogMapper;
import com.nia.ip.sdn.syslog.linkage.vo.syslog.SyslogCollectVo;
import com.nia.ip.sdn.syslog.linkage.vo.syslog.SyslogDataVo;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("IpSdnSyslogService")
public class IpSdnSyslogService {

    @Autowired
    private SyslogDataPrdAmqp syslogDataPrdAmqp;

    @Autowired
    private SyslogMapper syslogMapper;

    @Autowired
    private SyslogCollectVo syslogCollectVo;

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<SyslogCollectVo> syslogCollectVoObjectFactory;


    public void syslogDataHdlProcessor(SyslogDataVo syslogDataVo){

        int collectSeq;

        try{
            collectSeq = syslogMapper.selectSyslogSeq();

            syslogDataVo.setCollectSeq(collectSeq);

            syslogCollectVo = syslogCollectVoObjectFactory.getObject();
            syslogCollectVo.setSyslogCollectVo(syslogDataVo);

            syslogMapper.insertSyslogData(syslogCollectVo);

            if(syslogDataVo.getFields().getSeverityCode() < 4){
                syslogDataPrdAmqp.sendMessageCmd(syslogDataVo);
            }
        }catch (Exception e){
            LoggerPrint.errorLog(e);
        }
    }
}
