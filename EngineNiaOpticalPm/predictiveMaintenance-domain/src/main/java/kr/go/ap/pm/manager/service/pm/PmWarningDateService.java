package kr.go.ap.pm.manager.service.pm;

import kr.go.ap.core.primary.nia.dto.pm.model.PmAiTicketCheckDto;
import kr.go.ap.core.repository.primary.mapper.predictive.pm.PmPreMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class PmWarningDateService {

    private final PmPreMapper pmPreMapper;

    public void warningDateCheck(){
        log.info("warningDateCheck");

        List<PmAiTicketCheckDto> pmAiTicketCheckDtoList;

        try {
            pmAiTicketCheckDtoList = pmPreMapper.selectWarningDateCheckList("Y");

            if(!CollectionUtils.isEmpty(pmAiTicketCheckDtoList)){
                for(PmAiTicketCheckDto pmAiTicketCheckDto : pmAiTicketCheckDtoList){
                    pmPreMapper.updateWarningDate(pmAiTicketCheckDto);
                }
            }
        }catch (NullPointerException | IndexOutOfBoundsException | PersistenceException e){
            log.error(ExceptionUtils.getMessage(e));
        }
    }
}
