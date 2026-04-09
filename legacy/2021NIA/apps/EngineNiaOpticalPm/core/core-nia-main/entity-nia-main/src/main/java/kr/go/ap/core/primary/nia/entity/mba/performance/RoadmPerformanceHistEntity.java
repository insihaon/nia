package kr.go.ap.core.primary.nia.entity.mba.performance;

import jakarta.persistence.*;
import kr.go.ap.core.primary.nia.entity.mba.performance.key.RoadmPerformanceHistKey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Persistable;

import java.sql.Timestamp;


@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TB_ROADM_OPTICAL_PERFORMANCE_HIST", schema = "MBA")
public class RoadmPerformanceHistEntity implements Persistable<RoadmPerformanceHistKey> {

    @EmbeddedId
    private RoadmPerformanceHistKey roadmPerformanceHistKey;
    @Column(name = "trunk_name")
    private String trunkName;
    @Column(name = "roadm_code")
    private String roadmCode;
    @Column(name = "sysname")
    private String sysname;

    @Column(name = "value_cur")
    private float valueCur;
    @Column(name = "value_max")
    private float valueMax;
    @Column(name = "value_min")
    private float valueMin;
    @Column(name = "direction")
    private String direction;
    @Column(name = "routenum")
    private int routenum;


    @Transient
    private boolean isLowSignal;

    @Transient
    private boolean isNew = true;

    @Transient
    public void setLowSignal(boolean lowSignal) {
        this.isLowSignal = lowSignal;
    }


    @Override
    public RoadmPerformanceHistKey getId() {
        return roadmPerformanceHistKey;
    }

    @Override
    public boolean isNew() {
        return isNew;
    }

    @PrePersist
    @PostLoad
    public void markNotNew() {
        this.isNew = false;
    }


    public String getInOut() {
        return roadmPerformanceHistKey.getInOut();
    }

    public String getTrunkId() {
        return roadmPerformanceHistKey.getTrunkId();
    }

    public Timestamp getOcrtime() {
        return roadmPerformanceHistKey.getOcrtime();
    }

    public String getPtpname() {
        return roadmPerformanceHistKey.getPtpname();
    }

    public String getPort() {
        return roadmPerformanceHistKey.getPort();
    }

    public String getTid() {
        return roadmPerformanceHistKey.getTid();
    }


}
