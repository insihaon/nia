package kr.go.ap.core.repository.primary.mapper.pm;

import kr.go.ap.core.primary.nia.dto.pm.ticket.PmTicketDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.exceptions.PersistenceException;

import java.util.List;

@Mapper
public interface PmTicketMapper {
    List<PmTicketDto> selectTicketCheckList(String reason) throws PersistenceException;
    String selectPmTicketKey() throws PersistenceException;
    void insertPmTicket(PmTicketDto pmTicketDto) throws PersistenceException;
    void updateTicketClear(PmTicketDto pmTicketDto) throws PersistenceException;
}
