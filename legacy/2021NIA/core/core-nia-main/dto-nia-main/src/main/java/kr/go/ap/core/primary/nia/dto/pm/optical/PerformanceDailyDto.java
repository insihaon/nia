package kr.go.ap.core.primary.nia.dto.pm.optical;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.math.BigInteger;
import java.sql.Date;

@Builder
@Getter
public class PerformanceDailyDto implements Serializable {

    private Date collectionDate;
    private String trunkName;
    private String sysname;
    private String direction;
    private BigInteger cnt;
}
