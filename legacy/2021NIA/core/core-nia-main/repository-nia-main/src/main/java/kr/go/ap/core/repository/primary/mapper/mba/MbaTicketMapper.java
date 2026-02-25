package kr.go.ap.core.repository.primary.mapper.mba;

import kr.go.ap.core.primary.nia.dto.rca.engine.ticket.TicketDto;

import kr.go.ap.core.primary.nia.dto.rca.engine.ticket.TicketPerCheckDataDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.exceptions.PersistenceException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper
public interface MbaTicketMapper {
    String selectTicketKey() throws PersistenceException;
    String selectTicketCableKey() throws PersistenceException;
    List<TicketPerCheckDataDto> selectTicketPerDataCheckList() throws PersistenceException;
    void insertRcaTicket(TicketDto ticketDto) throws PersistenceException;
    void insertTicketCableDtoList(Map<String, Object> objectMap) throws PersistenceException;
    void insertTicketPerformanceDataCheck(TicketPerCheckDataDto ticketPerCheckDataDto) throws PersistenceException;
    void insertLowPmDataDtoList(Map<String, Object> objectMap) throws PersistenceException;
    void insertTicketPrefData(HashMap<String, Object> hashMap) throws PersistenceException;
    void deleteCheckOpticalPerformance(String ticketId) throws PersistenceException;
}
