package kr.go.ap.core.repository.primary.jpa.mba.engine;

import kr.go.ap.core.primary.nia.entity.mba.engine.MbaTicketCurEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MbaTicketCurRepository extends JpaRepository<MbaTicketCurEntity, String> {

    @Query(
            value = "SELECT nextval('mba.seq_ticket')::varchar",
            nativeQuery = true
    )
    String nextTicketId();

}
