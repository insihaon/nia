package kr.go.ap.core.repository.primary.jpa.pm.ticket;

import kr.go.ap.core.primary.nia.entity.pm.ticket.PmTicketEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PmTicketRepository extends JpaRepository<PmTicketEntity, String> {
    List<PmTicketEntity> findByReasonAndClearDateIsNull(String reason);


    @Query(
            value = "SELECT NEXTVAL('mba.seq_pm_ticket')::varchar",
            nativeQuery = true
    )
    String nextPmTicketKey();

}
