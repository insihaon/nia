package kr.go.ap.core.repository.primary.jpa.mba.performance;

import kr.go.ap.core.primary.nia.entity.mba.performance.RoadmPerformanceHistEntity;
import kr.go.ap.core.primary.nia.entity.mba.performance.key.RoadmPerformanceHistKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoadmPerformanceHistRepository extends JpaRepository<RoadmPerformanceHistEntity, RoadmPerformanceHistKey> ,RoadmPerformanceHistRepositoryCustom{

}
