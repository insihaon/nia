package com.nia.ip.sdn.linkage.service.impl;

import com.nia.ip.sdn.linkage.mapper.linkage.LinkageNodeFactorMapper;
import com.nia.ip.sdn.linkage.mapper.nia.NiaLinkageHistMapper;
import com.nia.ip.sdn.linkage.mapper.nia.NiaNodeFactorMapper;
import com.nia.ip.sdn.linkage.service.NodeFactorService;
import com.nia.ip.sdn.linkage.vo.NodeFactorVo;
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

@Service("NodeFactorService")
public class NodeFactorServiceImpl implements NodeFactorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NodeFactorServiceImpl.class);

    // 1. SIMPLE Executor Mapper (SELECT 전용)
    private final LinkageNodeFactorMapper linkageNodeFactorMapper;
    private final NiaLinkageHistMapper niaLinkageHistMapper;

    // 2. BATCH Executor Mapper (INSERT/UPDATE 전용)
    private final NiaNodeFactorMapper batchNiaNodeFactorMapper;
    private final NiaLinkageHistMapper batchNiaLinkageHistMapper;


    // ✨ 생성자 주입 및 BATCH Mapper 인스턴스화 (Autowired 필드 제거)
    public NodeFactorServiceImpl(
            // SIMPLE Executor Mapper 주입
            LinkageNodeFactorMapper linkageNodeFactorMapper,
            NiaLinkageHistMapper niaLinkageHistMapper,

            // BATCH 전용 SqlSessionTemplate 주입
            @Qualifier("NiaBatchSqlSessionTemplate") SqlSessionTemplate batchSqlSessionTemplate) {

        // SIMPLE Mapper 초기화
        this.linkageNodeFactorMapper = linkageNodeFactorMapper; // SELECT, Hist Key SELECT
        this.niaLinkageHistMapper = niaLinkageHistMapper; // Hist Key SELECT (SIMPLE 사용)

        // BATCH SqlSessionTemplate을 사용하여 BATCH 전용 Mapper 인스턴스 생성
        this.batchNiaNodeFactorMapper = batchSqlSessionTemplate.getMapper(NiaNodeFactorMapper.class); // INSERT
        this.batchNiaLinkageHistMapper = batchSqlSessionTemplate.getMapper(NiaLinkageHistMapper.class); // UPDATE
    }

    @Override
    public void getNodeFactorData() {
        LOGGER.info("==========>[NodeFactorService] getNodeFactorData <==============");

        String key;
        final String HIST_KEY_NAME = "ipSdnNodeFactorKey";

        try {
            // 1. 현재 처리된 마지막 ID (시작 키) 조회 - SIMPLE Mapper
            key = niaLinkageHistMapper.selectLinkageKey(HIST_KEY_NAME);

            LOGGER.info("==========>[NodeFactorService] getNodeFactorData key : " + key + " <==============");

            if (StringUtils.isNotEmpty(key)) {
                // 2. 소스 DB의 최대 ID 조회 - SIMPLE Mapper
                NodeFactorVo maxIdVo = linkageNodeFactorMapper.selectMaxId();

                if (maxIdVo != null) {
                    int currentKey = Integer.parseInt(key);
                    int maxId = maxIdVo.getId();

                    if (currentKey < maxId) {
                        int startKey = currentKey;

                        // 3. 청크 단위 반복 처리
                        while (startKey < maxId) {
                            // SIMPLE Mapper 사용 (SELECT는 BATCH를 사용하지 않음)
                            // 1000개 단위로 조회하는 쿼리로 가정하고 구현
                            List<NodeFactorVo> currentChunkList = linkageNodeFactorMapper.selectNodeFactorList(startKey);

                            if (CollectionUtils.isEmpty(currentChunkList)) {
                                LOGGER.warn("==========>[NodeFactorService] No more data found starting from key: " + startKey + " <==============");
                                break;
                            }

                            // 4. 데이터 처리: BATCH Executor를 사용하는 메서드 호출
                            // 트랜잭션과 커밋을 포함하여 이전 로직과 동일하게 구현
                            processChunkAndCommit(currentChunkList, HIST_KEY_NAME);

                            // 다음 루프의 시작 키 설정
                            int lastProcessedId = currentChunkList.get(currentChunkList.size() - 1).getId();
                            startKey = lastProcessedId; // 마지막 처리 ID를 다음 시작 키로 사용

                            if (startKey >= maxId) {
                                break;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("=====> [NodeFactorService] getNodeFactorData error() " + ExceptionUtils.getStackTrace(e) + "<=====");
        }
    }

    /**
     * 청크 데이터를 BATCH INSERT/UPDATE 처리하고 트랜잭션을 커밋합니다.
     */
    @Transactional
    public void processChunkAndCommit(List<NodeFactorVo> currentChunkList, String histKeyName) {
        if (CollectionUtils.isEmpty(currentChunkList)) {
            return;
        }

        LOGGER.info("==========>[NodeFactorService] Processing chunk size: " + currentChunkList.size() + " <==============");

        // 1. 배치 INSERT: BATCH 전용 Mapper 사용
        HashMap<String, Object> objectHashMap = new HashMap<>();
        objectHashMap.put("nodeFactorVoList", currentChunkList);
        batchNiaNodeFactorMapper.insertNodeFactor(objectHashMap); // ✨ BATCH Mapper 사용

        // 2. Hist 테이블 업데이트: BATCH 전용 Mapper 사용
        int lastProcessedId = currentChunkList.get(currentChunkList.size() - 1).getId();

        HashMap<String, String> strHashMap = new HashMap<>();
        strHashMap.put("key", histKeyName);
        strHashMap.put("value", String.valueOf(lastProcessedId));
        batchNiaLinkageHistMapper.updateLinkageHist(strHashMap); // ✨ BATCH Mapper 사용

        LOGGER.info("==========>[NodeFactorService] Committed up to ID: " + lastProcessedId + " <==============");
    }
}