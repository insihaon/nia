package kr.go.ap.core.primary.nia.entity.mba.performance.key;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Embeddable
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoadmCheckOpticalPerformanceKey {

    @Column(name = "trunk_id")
    private String trunkId;
    @Column(name = "tid")
    private String tid;
    @Column(name = "port")
    private String port;
    @Column(name = "ocrtime")
    private Timestamp ocrtime;
    @Column(name = "ptpname")
    private String ptpname;
    @Column(name = "in_out")
    private String inOut;
}

