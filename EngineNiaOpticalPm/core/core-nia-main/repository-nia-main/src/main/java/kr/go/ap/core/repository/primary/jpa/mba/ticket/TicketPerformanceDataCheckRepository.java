package kr.go.ap.core.repository.primary.jpa.mba.ticket;


import kr.go.ap.core.primary.nia.entity.mba.ticket.TicketPerformanceDataCheckEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketPerformanceDataCheckRepository extends JpaRepository<TicketPerformanceDataCheckEntity, String> {
    void deleteByTicketId(String ticketId);
//    void deleteAllByTicketIdInBatch(String ticketId);

}
