package com.nia.ip.sdn.syslog.linkage.service;

import com.nia.ip.sdn.syslog.linkage.amqp.SyslogDataPrdAmqp;
import com.nia.ip.sdn.syslog.linkage.common.LoggerPrint;
import com.nia.ip.sdn.syslog.linkage.mapper.CommonMapper;
import com.nia.ip.sdn.syslog.linkage.mapper.SyslogMapper;
import com.nia.ip.sdn.syslog.linkage.vo.syslog.SyslogCollectVo;
import com.nia.ip.sdn.syslog.linkage.vo.syslog.SyslogDataVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service("IpSdnSyslogService")
public class IpSdnSyslogService {
    private static final Logger LOGGER = LoggerFactory.getLogger(IpSdnSyslogService.class);

    @Autowired
    private SyslogDataPrdAmqp syslogDataPrdAmqp;

    @Autowired
    private SyslogMapper syslogMapper;

    @Autowired
    private CommonMapper commonMapper;

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<SyslogCollectVo> syslogCollectVoObjectFactory;

    @Transactional
    public void syslogDataHdlProcessor(List<SyslogDataVo> syslogDataVoList){
        LOGGER.info(">>>>>>>>>>[IpSdnSflowService] sflowDataHdlProcessor (Batch) <<<<<<<<<<<<<<<<<");
        HashMap<String, String> strHashMap;

        // 1. 전처리 및 AMQP 대상 순번 수집
        List<Integer> numberArray = new ArrayList<>(); // AMQP 메시지 전송 대상의 순번(1부터 시작)
        int cnt = 0; // 전체 레코드의 순번 (1부터 시작)

        List<SyslogCollectVo> syslogCollectVoList = new ArrayList<>(syslogDataVoList.size());

        for(SyslogDataVo syslogDataVo : syslogDataVoList){
            SyslogCollectVo syslogCollectVo = syslogCollectVoObjectFactory.getObject();
            syslogCollectVo.setSyslogCollectVo(syslogDataVo);
            syslogCollectVoList.add(syslogCollectVo);

            cnt++; // 순번 증가 (1, 2, 3, ...)

            if(syslogDataVo.getFields().getSeverityCode() <= 4){
                numberArray.add(cnt); // AMQP 보낼 대상의 '1부터 시작하는 순번' 저장
            }
        }

        // 2. Bulk Insert 실행
        // successCnt는 INSERT된 실제 행의 개수 (DB 오류 시 Exception 발생, 이 값은 나오지 않음)
        Integer successCnt = syslogMapper.insertSyslogDataBatch(syslogCollectVoList);

        // 3. 후처리 및 ID 계산
        long collectSeq = syslogMapper.selectCurrentSyslogSeq(); // 가장 마지막에 INSERT된 ID
        LoggerPrint.infoLog("Syslog Max Seq ===> " + collectSeq);

        strHashMap = new HashMap<>();
        strHashMap.put("key", "ipSdnSyslogKey");
        strHashMap.put("value", String.valueOf(collectSeq));
        commonMapper.updateLinkageYdKey(strHashMap);

        // 4. AMQP 메시지 전송 (정확한 ID 계산 적용)
        // startSeq = 최종 ID - 성공 개수 + 1
        long startSeq = collectSeq - successCnt + 1;

        for(Integer 순번_i : numberArray){ // 순번_i는 1부터 시작

            // 1. SyslogDataVoList에서 데이터 가져오기 (List Index는 순번 - 1)
            // ⚠️ 치명적 오류 수정: get(i-1) 사용!
            SyslogDataVo syslogDataVo = syslogDataVoList.get(순번_i - 1);

            // 2. 현재 레코드의 COLLECT_SEQ 계산: 시작 ID + (순번 i - 1)
            long actualCollectSeq = startSeq + (순번_i - 1);

            if(syslogDataVo.getFields().getSeverityCode() >5){
                // 에러 조기 탐지용
                LOGGER.error(">>>>>>>>>>[IpSdnSflowService] syslogDataVo.getFields().getSeverityCode() : " + syslogDataVo.getFields().getSeverityCode());
                LOGGER.error(">>>>>>>>>>[IpSdnSflowService] actualCollectSeq : " + actualCollectSeq);
                LOGGER.error(">>>>>>>>>>[IpSdnSflowService] syslogDataVo : " + syslogDataVo);
            }

            // 3. AMQP 메시지 전송
            syslogDataVo.setCollectSeq(actualCollectSeq);
            syslogDataPrdAmqp.sendMessageCmd(syslogDataVo);
        }
    }

//    @Transactional
//    public void syslogDataHdlProcessor(List<SyslogDataVo> syslogDataVoList){
//        LOGGER.info(">>>>>>>>>>[IpSdnSyslogService] syslogDataHdlProcessor (Batch) <<<<<<<<<<<<<<<<<");
//        HashMap<String, String> strHashMap;
//
//        try{
//            List<Integer> numberArray = new ArrayList<Integer>();
//            Integer cnt = 0;
//
//            List<SyslogCollectVo> syslogCollectVoList = new ArrayList<>(syslogDataVoList.size());
//
//            // 2. 데이터 전처리 (DB와의 통신 없이 메모리에서 객체 변환만 수행)
//            for(SyslogDataVo syslogDataVo : syslogDataVoList){
//                // 건별 시퀀스 생성/설정 로직 제거! Bulk Insert의 효율을 위해 DB에서 처리해야 합니다.
//                SyslogCollectVo syslogCollectVo = syslogCollectVoObjectFactory.getObject();
//                syslogCollectVo.setSyslogCollectVo(syslogDataVo);
//                syslogCollectVoList.add(syslogCollectVo);
//                cnt++;
//
//                if(syslogDataVo.getFields().getSeverityCode() <= 4){
//                    numberArray.add(cnt);
//                }
//            }
//
//            // 3. Bulk Insert 실행: 리스트 전체를 한 번에 Mapper에 전달합니다. (핵심)
//            Integer successCnt = syslogMapper.insertSyslogDataBatch(syslogCollectVoList);
//
//            long collectSeq = syslogMapper.selectCurrentSyslogSeq();
//            LoggerPrint.infoLog("Syslog Max Seq ===> " + collectSeq);
//            strHashMap = new HashMap<>();
//            strHashMap.put("key", "ipSdnSyslogKey");
//            strHashMap.put("value", String.valueOf(collectSeq));
//            commonMapper.updateLinkageYdKey(strHashMap);
//
//            for(Integer i : numberArray){
//                SyslogDataVo syslogDataVo = syslogDataVoList.get(i);
//                syslogDataVo.setCollectSeq(collectSeq - successCnt + i);
//                syslogDataPrdAmqp.sendMessageCmd(syslogDataVo);
//            }
//        }catch (Exception e){
//            LoggerPrint.errorLog(e);
//        }
//    }
}
