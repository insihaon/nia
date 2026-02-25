package kr.go.ap.core.primary.nia.entity.mba.engine;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.sql.Timestamp;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "TB_MBA_TICKET_CURRENT", schema = "MBA")
//@Table(name = "TB_RCA_TICKET_TMP", schema = "MBA")
public class MbaTicketCurEntity {

    @Id
    @Column(name = "ticket_id")
    private String ticketId;

    @Column(name = "cluster_no")
    private String clusterNo;

    @Column(name = "ticket_type")
    private String ticketType;

    @Column(name = "ticket_generation_time")
    private Timestamp ticketGenerationTime;

    @Column(name = "fault_time")
    private Timestamp faultTime;

    @Column(name = "root_cause_type")
    private String rootCauseType;

    @Column(name = "root_cause_code")
    private String rootCauseCode;

    @Column(name = "root_cause_domain")
    private String rootCauseDomain;

    @Column(name = "total_related_alarm_cnt")
    private int totalRelatedAlarmCnt;

    @Column(name = "status")
    private String status;

    @Column(name = "parent_ticket_id")
    private String parentTicketId;

    @Column(name = "ticket_rca_result_code")
    private String ticketRcaResultCode;

    @Column(name = "ticket_rca_result_dtl_code")
    private String ticketRcaResultDtlCode;

    @Column(name = "ticket_rca_result_orig_dtl_code")
    private String ticketRcaResultOrigDtlCode;


    @Column(name = "direction")
    private String direction;


    @Column(name = "occur")
    private boolean occur;


    @Column(name = "trunk_id")
    private String trunkId;

    @Column(name = "trunk_name")
    private String trunkName;
}
