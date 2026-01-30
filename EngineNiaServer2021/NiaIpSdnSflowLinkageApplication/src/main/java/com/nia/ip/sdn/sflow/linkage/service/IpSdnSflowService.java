package com.nia.ip.sdn.sflow.linkage.service;

import com.nia.ip.sdn.sflow.linkage.common.LoggerPrint;
import com.nia.ip.sdn.sflow.linkage.mapper.SflowMapper;
import com.nia.ip.sdn.sflow.linkage.vo.sflow.SflowCollectVo;
import com.nia.ip.sdn.sflow.linkage.vo.sflow.SflowDataVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service("IpSdnSflowService")
public class IpSdnSflowService {
    private static final Logger LOGGER = LoggerFactory.getLogger(IpSdnSflowService.class);

    @Autowired
    private SflowMapper sflowMapper;

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<SflowCollectVo> sflowCollectVoObjectFactory;

    // @Transactional 어노테이션을 추가하여 Batch 작업 중 오류 발생 시 전체 롤백되도록 설정합니다.
    @Transactional
    public void sflowDataHdlProcessor(List<SflowDataVo> sflowDataVoList){
        LOGGER.info(">>>>>>>>>>[IpSdnSflowService] sflowDataHdlProcessor (Batch) <<<<<<<<<<<<<<<<<");
        HashMap<String, String> strHashMap;


        try {
            List<SflowCollectVo> sflowCollectVoList = new ArrayList<>(sflowDataVoList.size());

            // 2. 데이터 전처리 (DB와의 통신 없이 메모리에서 객체 변환만 수행)
            for(SflowDataVo sflowDataVo : sflowDataVoList){
                // 건별 시퀀스 생성/설정 로직 제거! Bulk Insert의 효율을 위해 DB에서 처리해야 합니다.
                SflowCollectVo sflowCollectVo = sflowCollectVoObjectFactory.getObject();
                sflowCollectVo.setSflowCollectVo(sflowDataVo);
                sflowCollectVoList.add(sflowCollectVo);
            }

            // 3. Bulk Insert 실행: 리스트 전체를 한 번에 Mapper에 전달합니다. (핵심)
            sflowMapper.insertSflowDataBatch(sflowCollectVoList);

            long collectSeq = sflowMapper.selectCurrentSflowSeq();
            LoggerPrint.infoLog("Sflow Max Seq ===> " + collectSeq);
            strHashMap = new HashMap<>();
            strHashMap.put("key", "ipSdnSflowKey");
            strHashMap.put("value", String.valueOf(collectSeq));

            // updateLinkageHist는 마지막 시퀀스 기준으로 한 번만 실행합니다.
            sflowMapper.updateLinkageHist(strHashMap);

        }catch (Exception e){
            LoggerPrint.errorLog(e);
            // 트랜잭션 롤백을 위해 예외를 다시 던집니다.
            throw new RuntimeException(e);
        }
    }
}
