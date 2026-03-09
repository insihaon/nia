package kr.go.ap.core.repository.primary.jpa.mba.engine;

import kr.go.ap.core.primary.nia.entity.mba.engine.MbaTicketCableEntity;
import kr.go.ap.core.primary.nia.entity.mba.engine.key.MbaTicketCableKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MbaTicketCableRepository extends JpaRepository<MbaTicketCableEntity, MbaTicketCableKey> {

    @Query(
            value = "SELECT nextval('mba.seq_ticket_cable')::varchar",
            nativeQuery = true
    )
    String nextTicketCableId();
}
