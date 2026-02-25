package kr.go.ap.core.primary.nia.dto.rca.engine.ticket;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.io.Serializable;
import java.sql.Timestamp;

@Builder
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TicketPerCheckDataDto implements Serializable {
    private String ticketId;
    private String trunkId;
    private String direction;
    private Timestamp ocrtime;

}
