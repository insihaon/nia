package kr.go.ap.core.repository.primary.jpa.mba.performance;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.Column;
import kr.go.ap.core.primary.nia.entity.mba.performance.QRoadmPerformanceHistEntity;
import kr.go.ap.core.primary.nia.entity.mba.performance.RoadmPerformanceHistEntity;
import lombok.RequiredArgsConstructor;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class RoadmPerformanceHistRepositoryImpl implements RoadmPerformanceHistRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<List<RoadmPerformanceHistEntity>> findHistByTrunkIdAndDirectionAndTimeRange(String trunkId, String direction, Timestamp fromTime, Timestamp toTime) {

        QRoadmPerformanceHistEntity hist = QRoadmPerformanceHistEntity.roadmPerformanceHistEntity;

        return Optional.ofNullable(queryFactory
                .selectFrom(hist)
                .where(
                        hist.roadmPerformanceHistKey.trunkId.eq(trunkId),
                        hist.direction.eq(direction),
                        hist.roadmPerformanceHistKey.ocrtime.between(fromTime, toTime)
                )
                .orderBy(hist.roadmPerformanceHistKey.trunkId.asc(), hist.direction.asc(), hist.routenum.asc(), hist.roadmPerformanceHistKey.inOut.asc())
                .fetch());

    }

    @Override
    public Optional<List<RoadmPerformanceHistEntity>> findHistByTrunkIdAndDirectionAndOcrTime(String trunkId, String direction, Timestamp ocrtime) {
        QRoadmPerformanceHistEntity hist = QRoadmPerformanceHistEntity.roadmPerformanceHistEntity;

        return Optional.ofNullable(queryFactory
                .selectFrom(hist)
                .where(
                        hist.roadmPerformanceHistKey.trunkId.eq(trunkId),
                        hist.direction.eq(direction),
                        hist.roadmPerformanceHistKey.ocrtime.eq(ocrtime)
                )
                .orderBy(hist.roadmPerformanceHistKey.trunkId.asc(), hist.direction.asc(), hist.routenum.asc(), hist.roadmPerformanceHistKey.inOut.asc())
                .fetch());
    }

    @Override
    public Optional<List<RoadmPerformanceHistEntity>> findHistByTrunkIdAndOcrTmeAndRouteNum(String trunkId, Timestamp ocrtime, int routenum) {
        QRoadmPerformanceHistEntity hist = QRoadmPerformanceHistEntity.roadmPerformanceHistEntity;

        return Optional.ofNullable( queryFactory
                .selectFrom(hist)
                .where(
                        hist.roadmPerformanceHistKey.trunkId.eq(trunkId),
                        hist.roadmPerformanceHistKey.ocrtime.eq(ocrtime),
                        hist.routenum.eq(routenum)
                )
                .fetch());
    }
}


