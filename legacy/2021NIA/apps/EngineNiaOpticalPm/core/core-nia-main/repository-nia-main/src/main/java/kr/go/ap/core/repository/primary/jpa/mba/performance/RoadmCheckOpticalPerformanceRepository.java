package kr.go.ap.core.repository.primary.jpa.mba.performance;

import kr.go.ap.core.primary.nia.entity.mba.performance.RoadmCheckOpticalPerformanceEntity;
import kr.go.ap.core.primary.nia.entity.mba.performance.key.RoadmCheckOpticalPerformanceKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoadmCheckOpticalPerformanceRepository extends JpaRepository<RoadmCheckOpticalPerformanceEntity, RoadmCheckOpticalPerformanceKey> {
}
