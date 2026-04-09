package kr.go.ap.pm.manager.service.pm;

import kr.go.ap.core.repository.primary.mapper.predictive.pm.PmPreMapper;
import kr.go.ap.pm.manager.service.model.AiPmDataSendService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.stereotype.Service;
@Slf4j
@Service
@RequiredArgsConstructor
public class PmDailyService {

    private final AiPmDataSendService aiPmDataSendService;
    private final PmPreMapper pmPreMapper;

    public void performanceDailyHdl(){
        log.info("performanceDailyHdl start");

        try {
            createPmDailyData();
        }catch (PersistenceException e){
            log.error("createPmDailyData : ",e);
        }

        try {
            createPmDailyNtdData();
        }catch (PersistenceException e){
            log.error("createPmDailyNtdData : ",e);
        }

        try {
            createPmDailyReferencedData();
        }catch (PersistenceException e){
            log.error("createPmDailyReferencedData : ",e);
        }

        aiPmDataSendService.sendPmData();
        log.info("performanceDailyHdl end");
    }

    private void createPmDailyData() throws PersistenceException {
        log.info("createPmDailyData");

        pmPreMapper.callSetPerformanceDaily();
    }

    private void createPmDailyNtdData() throws PersistenceException{
        log.info("createPmDailyNtdData");

        pmPreMapper.callSetPerformanceDailyNtd();
    }

    private void createPmDailyReferencedData() throws PersistenceException{
        log.info("createPmDailyReferencedData");

        pmPreMapper.callSetPerformanceDailyReference();
    }
}
