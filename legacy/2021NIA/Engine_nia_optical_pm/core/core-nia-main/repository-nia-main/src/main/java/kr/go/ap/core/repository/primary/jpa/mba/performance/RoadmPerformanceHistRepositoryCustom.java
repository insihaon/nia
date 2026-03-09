package kr.go.ap.core.repository.primary.jpa.mba.performance;

import kr.go.ap.core.primary.nia.entity.mba.performance.RoadmPerformanceHistEntity;


import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface RoadmPerformanceHistRepositoryCustom {

    Optional<List<RoadmPerformanceHistEntity>> findHistByTrunkIdAndDirectionAndTimeRange(
            String trunkId,
            String direction,
            Timestamp fromTime,
            Timestamp toTime
    );

    Optional<List<RoadmPerformanceHistEntity>> findHistByTrunkIdAndDirectionAndOcrTime(
            String trunkId,
            String direction,
            Timestamp ocrtime
    );

    Optional<List<RoadmPerformanceHistEntity>> findHistByTrunkIdAndOcrTmeAndRouteNum(
            String trunkId,
            Timestamp ocrtime,
            int routenum
    );

}
