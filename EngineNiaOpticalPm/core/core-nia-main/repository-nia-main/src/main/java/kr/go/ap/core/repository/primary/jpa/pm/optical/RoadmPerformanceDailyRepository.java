package kr.go.ap.core.repository.primary.jpa.pm.optical;

import kr.go.ap.core.primary.nia.entity.pm.optical.RoadmPerformanceDailyEntity;
import kr.go.ap.core.primary.nia.entity.pm.optical.key.RoadmPerformanceDailyKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoadmPerformanceDailyRepository extends JpaRepository<RoadmPerformanceDailyEntity, RoadmPerformanceDailyKey> {
}
