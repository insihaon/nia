package kr.go.ap.core.primary.nia.dto.rca.engine.ticket;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.io.Serializable;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TicketCableDto implements Serializable {
    private String ticketId;
    private String ticketCableId;
    private Boolean isRootRowSignalLine;
    private String ptpnamea;
    private String ptpnamez;
    private String tida;
    private String sysnamea;
    private String tidz;
    private String sysnamez;
}
