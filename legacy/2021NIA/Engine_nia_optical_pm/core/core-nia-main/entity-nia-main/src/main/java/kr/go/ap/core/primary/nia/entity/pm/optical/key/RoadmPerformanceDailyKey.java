package kr.go.ap.core.primary.nia.entity.pm.optical.key;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.time.LocalDate;

@Embeddable
@Data
public class RoadmPerformanceDailyKey {

    //@TODO 키값 확인

    @Column(name = "collection_date")
    private LocalDate collectionDate;

    @Column(name = "trunk_id")
    private String trunkId;

    @Column(name = "trunk_name")
    private String trunkName;

    @Column(name = "tid")
    private String tid;

    @Column(name = "sysname")
    private String sysname;

    @Column(name = "in_out")
    private String inOut;

    @Column(name = "routenum")
    private Integer routenum;

    @Column(name = "direction")
    private String direction;

}
