package kr.go.ap.core.primary.nia.entity.mba.ticket;

import jakarta.persistence.*;
import kr.go.ap.core.primary.nia.entity.mba.ticket.key.RoadmLowOpticalPerformanceKey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Persistable;

@Entity
@Builder
@Getter
@AllArgsConstructor
@Table(name = "TB_ROADM_LOW_OPTICAL_PERFORMANCE", schema = "MBA")
public class RoadmLowOpticalPerformanceEntity implements Persistable<RoadmLowOpticalPerformanceKey> {

    public RoadmLowOpticalPerformanceEntity() {
        this.roadmLowOpticalPerformanceKey = new RoadmLowOpticalPerformanceKey();
    }

    @EmbeddedId
    RoadmLowOpticalPerformanceKey roadmLowOpticalPerformanceKey;

    @Column(name = "trunk_name")
    private String trunkName;

    @Column(name = "sysname")
    private String sysname;

    @Column(name = "roadm_code")
    private String roadmCode;

    @Column(name = "value_cur")
    private float valueCur;

    @Column(name = "value_max")
    private float valueMax;

    @Column(name = "value_min")
    private float valueMin;

    @Column(name = "direction")
    private String direction;

    @Column(name = "routenum")
    private int routeNum;

    @Column(name = "is_low_signal")
    private boolean lowSignal;

    @Transient
    private boolean isNew = true;

    @Override
    public RoadmLowOpticalPerformanceKey getId() {
        return roadmLowOpticalPerformanceKey;
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

    public void setTicketId(String ticketId) {
        this.roadmLowOpticalPerformanceKey.setTicketId(ticketId);
    }
}