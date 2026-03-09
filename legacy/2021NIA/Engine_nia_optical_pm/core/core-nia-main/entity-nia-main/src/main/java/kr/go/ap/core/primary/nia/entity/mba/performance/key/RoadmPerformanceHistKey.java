package kr.go.ap.core.primary.nia.entity.mba.performance.key;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;


@Data
@Embeddable
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoadmPerformanceHistKey {

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
