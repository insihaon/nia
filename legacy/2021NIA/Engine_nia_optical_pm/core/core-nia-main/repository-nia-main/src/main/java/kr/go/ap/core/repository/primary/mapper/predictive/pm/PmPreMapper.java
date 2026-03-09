package kr.go.ap.core.repository.primary.mapper.predictive.pm;


import kr.go.ap.core.primary.nia.dto.pm.model.PmAiTicketCheckDto;
import kr.go.ap.core.primary.nia.dto.pm.model.PmModelSendDataDto;
import kr.go.ap.core.primary.nia.dto.pm.optical.PerformanceDailyDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.exceptions.PersistenceException;

import java.util.List;

@Mapper
public interface PmPreMapper {
	List<PmModelSendDataDto> selectPmModelSendDataList() throws PersistenceException;
    List<PmAiTicketCheckDto> selectWarningDateCheckList(String updateYn) throws PersistenceException;
    List<PerformanceDailyDto> selectPmAlarmCheckList() throws PersistenceException;
    String callSetPerformanceDaily() throws PersistenceException;
    String callSetPerformanceDailyNtd() throws PersistenceException;
    String callSetPerformanceDailyReference() throws PersistenceException;
    void updateWarningDate(PmAiTicketCheckDto pmAiTicketCheckDto) throws PersistenceException;
}
