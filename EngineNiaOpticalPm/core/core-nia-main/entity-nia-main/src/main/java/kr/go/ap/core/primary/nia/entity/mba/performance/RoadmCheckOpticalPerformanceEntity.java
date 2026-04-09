package kr.go.ap.core.primary.nia.entity.mba.performance;

import jakarta.persistence.*;
import kr.go.ap.core.primary.nia.entity.mba.performance.key.RoadmCheckOpticalPerformanceKey;
import lombok.*;
import org.springframework.data.domain.Persistable;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "TB_ROADM_CHECK_OPTICAL_PERFORMANCE", schema = "MBA")
public class RoadmCheckOpticalPerformanceEntity implements Persistable<RoadmCheckOpticalPerformanceKey> {

    @EmbeddedId
    private RoadmCheckOpticalPerformanceKey roadmCheckOpticalPerformanceKey;

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
    private int routenum;


    @Transient
    private boolean isNew = true;

    @Override
    public RoadmCheckOpticalPerformanceKey getId() {
        return roadmCheckOpticalPerformanceKey;
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
//
//    public static RoadmCheckOpticalPerformanceEntity fromDto(OpticalPerformanceDto perfDto) {
//
//        RoadmCheckOpticalPerformanceKey key = RoadmCheckOpticalPerformanceKey.builder()
//                .trunkId(perfDto.getTrunkId())
//                .tid(perfDto.getTid())
//                .port(perfDto.getPort())
//                .ocrtime(perfDto.getOcrtime())
//                .ptpname(perfDto.getPtpname())
//                .inOut(perfDto.getInOut())
//                .build();
//
//        return RoadmCheckOpticalPerformanceEntity.builder()
//                .roadmCheckOpticalPerformanceKey(key)
//                .trunkName(perfDto.getTrunkName())
//                .sysname(perfDto.getSysname())
//                .roadmCode(perfDto.getRoadmCode())
//                .valueCur(perfDto.getValueCur())
//                .valueMax(perfDto.getValueMax())
//                .valueMin(perfDto.getValueMin())
//                .direction(perfDto.getDirection())
//                .routenum(perfDto.getRouteNum())
//                .build();
//    }


}
