package com.nia.ip.sdn.linkage.service.impl;

import com.nia.ip.sdn.linkage.mapper.linkage.LinkageLinkTrafficeMapper;
import com.nia.ip.sdn.linkage.mapper.nia.NiaLinkTrafficMapper;
import com.nia.ip.sdn.linkage.mapper.nia.NiaLinkageHistMapper;
import com.nia.ip.sdn.linkage.service.LinkTrafficeService;
import com.nia.ip.sdn.linkage.vo.LinkTrafficVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.mybatis.spring.SqlSessionTemplate; // SqlSessionTemplate 임포트 추가
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier; // Qualifier 임포트 추가
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("LinkTrafficeService")
public class LinkTrafficeServiceImpl implements LinkTrafficeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LinkTrafficeServiceImpl.class);

    // 1. SIMPLE Executor Mapper (SELECT 전용)
    private final LinkageLinkTrafficeMapper linkageLinkTrafficeMapper;
    private final NiaLinkageHistMapper niaLinkageHistMapper;

    // 2. BATCH Executor Mapper (INSERT/UPDATE 전용)
    private final NiaLinkTrafficMapper batchNiaLinkTrafficMapper;
    private final NiaLinkageHistMapper batchNiaLinkageHistMapper;


    // ✨ 생성자 주입 (Autowired 대신 권장) 및 BATCH Mapper 인스턴스화
    // 모든 주입은 생성자를 통해 이루어지므로 @Autowired 필드를 제거합니다.
    public LinkTrafficeServiceImpl(
            // SIMPLE Executor Mapper 주입
            LinkageLinkTrafficeMapper linkageLinkTrafficeMapper,
            NiaLinkageHistMapper niaLinkageHistMapper,

            // BATCH 전용 SqlSessionTemplate 주입 (Configuration에서 정의한 이름 사용)
            @Qualifier("NiaBatchSqlSessionTemplate") SqlSessionTemplate batchSqlSessionTemplate) {

        // SIMPLE Mapper 초기화
        this.linkageLinkTrafficeMapper = linkageLinkTrafficeMapper; // SELECT, Hist Key SELECT
        this.niaLinkageHistMapper = niaLinkageHistMapper; // Hist Key SELECT (SIMPLE 사용)

        // BATCH SqlSessionTemplate을 사용하여 BATCH 전용 Mapper 인스턴스 생성
        this.batchNiaLinkTrafficMapper = batchSqlSessionTemplate.getMapper(NiaLinkTrafficMapper.class); // INSERT
        this.batchNiaLinkageHistMapper = batchSqlSessionTemplate.getMapper(NiaLinkageHistMapper.class); // UPDATE
    }

    @Override
    public void getLinkTrafficeData() {
        LOGGER.info("==========>[LinkTrafficeService] getLinkTrafficeData <==============");

        String key;

        try {
            // SIMPLE Mapper 사용 (SELECT 쿼리는 BATCH Executor를 사용할 필요가 없음)
            key = niaLinkageHistMapper.selectLinkageKey("ipSdnTrafficeKey");

            LOGGER.info("==========>[LinkTrafficeService] getLinkTrafficeData key : "+key+" <==============");

            if(StringUtils.isNotEmpty(key)){
                // SIMPLE Mapper 사용
                LinkTrafficVo maxIdVo = linkageLinkTrafficeMapper.selectMaxId();

                if(maxIdVo != null){
                    int currentKey = Integer.parseInt(key);
                    int maxId = maxIdVo.getId();

                    if(currentKey < maxId){
                        int startKey = currentKey;

                        while (startKey < maxId) {
                            // SIMPLE Mapper 사용 (SELECT는 BATCH를 사용하지 않음)
                            List<LinkTrafficVo> currentChunkList = linkageLinkTrafficeMapper.selectLinkTrafficVoList(startKey);

                            if (CollectionUtils.isEmpty(currentChunkList)) {
                                LOGGER.warn("==========>[LinkTrafficeService] No more data found starting from key: " + startKey + " <==============");
                                break;
                            }

                            // 2. 데이터 처리: BATCH Executor를 사용하는 메서드 호출
                            processChunkAndCommit(currentChunkList);

                            // 다음 루프의 시작 키 설정
                            int lastProcessedId = currentChunkList.get(currentChunkList.size() - 1).getId();
                            startKey = lastProcessedId;

                            if (startKey >= maxId) {
                                break;
                            }
                        }
                    }
                }
            }
        }catch (Exception e){
            LOGGER.error("=====> [LinkTrafficeService] getLinkTrafficeData error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
        }
    }

    @Transactional
    public void processChunkAndCommit(List<LinkTrafficVo> currentChunkList) {
        if (CollectionUtils.isEmpty(currentChunkList)) {
            return;
        }

        LOGGER.info("==========>[LinkTrafficeService] Processing chunk size: " + currentChunkList.size() + " <==============");

        // 1. 배치 INSERT: BATCH 전용 Mapper 사용
        HashMap<String, Object> objectHashMap = new HashMap<>();
        objectHashMap.put("linkTrafficVoList", currentChunkList);
        batchNiaLinkTrafficMapper.insertLinkTraffic(objectHashMap); // ✨ BATCH Mapper 사용

        // 2. Hist 테이블 업데이트: BATCH 전용 Mapper 사용
        int lastProcessedId = currentChunkList.get(currentChunkList.size() - 1).getId();

        HashMap<String, String> strHashMap = new HashMap<>();
        strHashMap.put("key", "ipSdnTrafficeKey");
        strHashMap.put("value", String.valueOf(lastProcessedId));
        batchNiaLinkageHistMapper.updateLinkageHist(strHashMap); // ✨ BATCH Mapper 사용

        LOGGER.info("==========>[LinkTrafficeService] Committed up to ID: " + lastProcessedId + " <==============");
    }
}