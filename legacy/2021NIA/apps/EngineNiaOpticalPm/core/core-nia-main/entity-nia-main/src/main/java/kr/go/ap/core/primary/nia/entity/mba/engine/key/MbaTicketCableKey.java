package kr.go.ap.core.primary.nia.entity.mba.engine.key;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MbaTicketCableKey {

    @Column(name = "ticket_id")
    private String ticketId;
    @Column(name = "ticket_cable_id")
    private String ticketCableId;
}
