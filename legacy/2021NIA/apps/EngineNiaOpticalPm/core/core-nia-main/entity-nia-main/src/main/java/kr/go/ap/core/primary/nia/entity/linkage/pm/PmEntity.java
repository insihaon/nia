package kr.go.ap.core.primary.nia.entity.linkage.pm;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import kr.go.ap.core.primary.nia.entity.linkage.pm.key.PmKey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Table(name = "TB_PERFORMACE_MST", schema = "NIA")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PmEntity {

    @EmbeddedId
    private PmKey pmKey;

    @Column(name="unit")
    private String unit;
    @Column(name="tmper")
    private String tmper;
    @Column(name="rxcur")
    private double rxCur;
    @Column(name="rxmin")
    private double rxMin;
    @Column(name="rxmax")
    private double rxMax;
    @Column(name="rxave")
    private double rxAve;
    @Column(name="txcur")
    private double txCur;
    @Column(name="txmin")
    private double txMin;
    @Column(name="txmax")
    private double txMax;
    @Column(name="txave")
    private double txAve;
    @Column(name="ocrtime")
    private Timestamp ocrtime;

    public void setOcrtime(Timestamp ocrtime) {
        this.ocrtime = ocrtime;
    }

}
