package kr.go.ap.core.primary.nia.entity.linkage.pm.key;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Embeddable
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PmKey {

    @Column(name = "sysname")
    private String sysname;
    @Column(name = "port")
    private String port;
    @Column(name="ocrtime")
    private Timestamp ocrtime;


    public void setOcrtime(Timestamp ocrtime) {
        this.ocrtime = ocrtime;
    }
}
