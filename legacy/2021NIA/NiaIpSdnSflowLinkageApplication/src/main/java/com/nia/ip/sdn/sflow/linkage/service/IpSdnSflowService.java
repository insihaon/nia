package com.nia.ip.sdn.sflow.linkage.service;

import com.nia.ip.sdn.sflow.linkage.common.LoggerPrint;
import com.nia.ip.sdn.sflow.linkage.listener.NiaSflowDataMsgListener;
import com.nia.ip.sdn.sflow.linkage.mapper.SflowMapper;
import com.nia.ip.sdn.sflow.linkage.vo.sflow.SflowCollectVo;
import com.nia.ip.sdn.sflow.linkage.vo.sflow.SflowDataVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("IpSdnSflowService")
public class IpSdnSflowService {
    private static final Logger LOGGER = LoggerFactory.getLogger(IpSdnSflowService.class);


    @Autowired
    private SflowMapper sflowMapper;

    @Autowired
    private SflowCollectVo sflowCollectVo;

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<SflowCollectVo> sflowCollectVoObjectFactory;

    public void sflowDataHdlProcessor(SflowDataVo sflowDataVo){
//        LOGGER.info(">>>>>>>>>>[IpSdnSflowService] sflowDataHdlProcessor <<<<<<<<<<<<<<<<<");

        long collectSeq;

        try {
            collectSeq = sflowMapper.selectSflowSeq();

            sflowDataVo.setCollectSeq(collectSeq);
            LoggerPrint.infoLog("Sflow Seq ===> " + collectSeq);

            sflowCollectVo = sflowCollectVoObjectFactory.getObject();
            sflowCollectVo.setSflowCollectVo(sflowDataVo);

            sflowMapper.insertSflowData(sflowCollectVo);
        }catch (Exception e){
            LoggerPrint.errorLog(e);
        }
    }
}
