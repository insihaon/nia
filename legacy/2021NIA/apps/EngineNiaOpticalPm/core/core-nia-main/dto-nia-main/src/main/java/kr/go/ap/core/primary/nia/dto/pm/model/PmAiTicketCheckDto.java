package kr.go.ap.core.primary.nia.dto.pm.model;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

@Builder
@Getter
public class PmAiTicketCheckDto implements Serializable {
    private String trunkName;
    private String tid;
    private String ptpname;
    private String inOut;
    private String sysname;
    private BigDecimal warningValueMax;
    private String direction;
    private Date collectionDate;
    private Date warningDate;
}
