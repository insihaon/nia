package kr.go.ap.core.primary.nia.dto.rca.engine.ticket;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@Builder
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TicketDto implements Serializable {

    private String ticketId;
    private String clusterNo;
    private String ticketType;
    private Timestamp ticketGenerationTime;
    private Timestamp faultTime;
    private String rootCauseType;
    private String rootCauseCode;
    private String rootCauseDomain;
    private String status;
    private String ticketRcaResultCode;
    private String ticketRcaResultDtlCode;
    private String ticketRcaResultOrigDtlCode;
    private Timestamp ticketUpdateTime;
    private boolean occur;
    private int totalRelatedAlarmCnt;
    private String trunkId;
    private String trunkName;
    private String direction;

    private List<TicketCableDto> ticketCableDtoList;
}
