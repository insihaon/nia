package kr.go.ap.core.primary.nia.dto.linkage.pm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PmDto implements Serializable {
    private String sysname;
    private String port;
    private String unit;
    private String tmper;
    private double rxCur;
    private double rxMin;
    private double rxMax;
    private double rxAve;
    private double txCur;
    private double txMin;
    private double txMax;
    private double txAve;
    private Timestamp ocrtime;

    public void setOcrtime(Timestamp ocrtime) {
        this.ocrtime = ocrtime;
    }
}
