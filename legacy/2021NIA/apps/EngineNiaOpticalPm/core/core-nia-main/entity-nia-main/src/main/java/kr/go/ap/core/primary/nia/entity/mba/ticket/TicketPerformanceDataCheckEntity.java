package kr.go.ap.core.primary.nia.entity.mba.ticket;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Table(name = "TB_MBA_TICKET_PERFORMANCE_DATA_CHECK", schema = "MBA")
public class TicketPerformanceDataCheckEntity {

    @Id
    @Column(name = "ticket_id")
    private String ticketId;
    @Column(name = "ocrtime")
    private Timestamp ocrTime;
    @Column(name = "trunk_id")
    private String trunkId;
    @Column(name = "direction")
    private String direction;
}
