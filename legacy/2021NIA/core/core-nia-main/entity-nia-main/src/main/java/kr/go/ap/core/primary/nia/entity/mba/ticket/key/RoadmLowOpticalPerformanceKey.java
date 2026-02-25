package kr.go.ap.core.primary.nia.entity.mba.ticket.key;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Embeddable
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class RoadmLowOpticalPerformanceKey {
    @Column(name = "ticket_id")
    private String ticketId;
    @Column(name = "trunk_id")
    private String trunkId;
    @Column(name = "tid")
    private String tid;
    @Column(name = "ptpname")
    private String ptpname;
    @Column(name = "port")
    private String port;
    @Column(name = "ocrtime")
    private Timestamp ocrtime;
    @Column(name = "in_out")
    private String inOut;


    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

}
