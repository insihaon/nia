package kr.go.ap.core.primary.nia.entity.pm.ticket;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TB_ROADM_PREDICTIVE_MAINTENANCE_TICKET", schema = "MBA")
@ToString
public class PmTicketEntity {


    @Id
    @Column(name = "ticketno")
    private String ticketno;

    @Column(name = "issue_date")
    private Date issueDate;
    @Column(name = "clear_date")
    private Date clearDate;

    @Column(name = "warning_date")
    private Date warningDate;

    @Column(name = "reason", length = 50)
    private String reason;

    @Column(name = "trunk_name", length = 100)
    private String trunkName;

    @Column(name = "direction", length = 12)
    private String direction;

    @Column(name = "target_sysname")
    private String targetSysname;

    @Transient
    private boolean isNew = true;

//
//    @Override
//    public PmTicketKey getId() {
//        return pmTicketKey;
//    }
//
//    @Override
//    public boolean isNew() {
//        return isNew;
//    }

    @PrePersist
    @PostLoad
    public void markNotNew() {
        this.isNew = false;
    }

    public void setClearDate(Date clearDate) {
        this.clearDate = clearDate;
    }

}

