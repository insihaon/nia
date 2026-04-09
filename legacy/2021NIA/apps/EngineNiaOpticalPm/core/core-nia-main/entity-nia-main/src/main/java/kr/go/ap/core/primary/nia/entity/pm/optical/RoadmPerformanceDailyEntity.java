package kr.go.ap.core.primary.nia.entity.pm.optical;


import jakarta.persistence.*;
import kr.go.ap.core.primary.nia.entity.pm.optical.key.RoadmPerformanceDailyKey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Persistable;

import java.math.BigDecimal;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tb_roadm_optical_performance_daily")
public class RoadmPerformanceDailyEntity implements Persistable<RoadmPerformanceDailyKey> {

    @EmbeddedId
    private RoadmPerformanceDailyKey roadmPerformanceDailyKey;

    @Column(name = "ptpname")
    private String ptpname;

    @Column(name = "value_max")
    private float valueMax;

    @Column(name = "span_loss")
    private BigDecimal spanLoss;

    @Column(name = "node_gain")
    private BigDecimal nodeGain;

    @Column(name = "node_total_deviation")
    private BigDecimal nodeTotalDeviation;

    @Column(name = "ntd_variance")
    private BigDecimal ntdVariance;

    @Transient
    private boolean isNew = true;

    @Override
    public RoadmPerformanceDailyKey getId() {
        return roadmPerformanceDailyKey;
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
}
