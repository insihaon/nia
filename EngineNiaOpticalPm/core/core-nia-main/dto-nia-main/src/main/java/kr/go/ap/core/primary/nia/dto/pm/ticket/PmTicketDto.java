package kr.go.ap.core.primary.nia.dto.pm.ticket;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.io.Serializable;
import java.sql.Date;

@Getter
@Builder
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
public class PmTicketDto implements Serializable {

    private String ticketno;
    private Date issueDate;
    private Date clearDate;
    private Date warningDate;
    private String reason;
    private String trunkName;
    private String direction;
    private String targetSysname;
    private boolean isNew = true;

    public void setClearDate(Date clearDate) {
        this.clearDate = clearDate;
    }
}
