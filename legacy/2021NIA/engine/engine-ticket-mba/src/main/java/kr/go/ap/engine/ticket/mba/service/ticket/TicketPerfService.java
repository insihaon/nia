package kr.go.ap.engine.ticket.mba.service.ticket;

import kr.go.ap.core.primary.nia.entity.mba.performance.RoadmPerformanceHistEntity;
import kr.go.ap.core.primary.nia.entity.mba.ticket.RoadmLowOpticalPerformanceEntity;
import kr.go.ap.core.primary.nia.entity.mba.ticket.TicketPerformanceDataCheckEntity;
import kr.go.ap.core.primary.nia.entity.mba.ticket.key.RoadmLowOpticalPerformanceKey;
import kr.go.ap.core.repository.primary.jpa.mba.performance.RoadmPerformanceHistRepository;
import kr.go.ap.core.repository.primary.jpa.mba.ticket.RoadmLowOpticalPerformanceRepository;
import kr.go.ap.core.repository.primary.jpa.mba.ticket.TicketPerformanceDataCheckRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TicketPerfService {

    private final RoadmLowOpticalPerformanceRepository lowOptPerfRepository;
    private final RoadmPerformanceHistRepository perfHistRepository;
    private final TicketPerformanceDataCheckRepository ticketPerfDataCheckRepository;

    //    private final MbaTicketMapper mbaTicketMapper;
//    private final PmEngineMapper pmEngineMapper;
    @Transactional
    public void addPerfData() {

        log.info("[Ticket Perf Service] addPerfData");
        List<RoadmPerformanceHistEntity> optPerfHistEntityList = null;
        HashMap<String, Object> objectHashMap;
        List<TicketPerformanceDataCheckEntity> ticketPerfDataCheckEntityList = null;
//
        try {
            ticketPerfDataCheckEntityList = ticketPerfDataCheckRepository.findAll(Sort.by("ticketId"));

            if (!ticketPerfDataCheckEntityList.isEmpty()) {
                for (TicketPerformanceDataCheckEntity ticketPerfDataCheckEntity : ticketPerfDataCheckEntityList) {
                    optPerfHistEntityList = setRowSignalDataHist(ticketPerfDataCheckEntity);

                    if (!optPerfHistEntityList.isEmpty()) {
                        optPerfHistEntityList.forEach(optPerfEntity -> {
                            lowOptPerfRepository.save(RoadmLowOpticalPerformanceEntity.builder()
                                    .roadmLowOpticalPerformanceKey(RoadmLowOpticalPerformanceKey.builder()
                                            .ticketId(ticketPerfDataCheckEntity.getTicketId())
                                            .inOut(optPerfEntity.getInOut())
                                            .tid(optPerfEntity.getTid())
                                            .port(optPerfEntity.getPort())
                                            .ocrtime(optPerfEntity.getOcrtime())
                                            .ptpname(optPerfEntity.getPtpname())
                                            .trunkId(optPerfEntity.getTrunkId())
                                            .build())
                                    .trunkName(optPerfEntity.getTrunkName())
                                    .sysname(optPerfEntity.getSysname())
                                    .roadmCode(optPerfEntity.getRoadmCode())
                                    .valueCur(optPerfEntity.getValueCur())
                                    .valueMax(optPerfEntity.getValueMax())
                                    .valueMin(optPerfEntity.getValueMin())
                                    .direction(optPerfEntity.getDirection())
                                    .routeNum(optPerfEntity.getRoutenum())
//                                    @TODO: low signal 확인 필요
                                    .lowSignal(true)
                                    .build());
                        });

                        ticketPerfDataCheckRepository.deleteByTicketId(ticketPerfDataCheckEntity.getTicketId());
                    }

                }
            }

        } catch (NullPointerException | IllegalArgumentException | PersistenceException e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }
    }

    private List<RoadmPerformanceHistEntity> setRowSignalDataHist(TicketPerformanceDataCheckEntity ticketPerfDataCheckEntity) {

        LocalDateTime curTime = null;
        LocalDateTime plusTime = null;
        List<RoadmPerformanceHistEntity> optPerfHistEntitiyList = null;
        HashMap<String, String> hashMap = null;

        try {
            curTime = ticketPerfDataCheckEntity.getOcrTime().toLocalDateTime();
            plusTime = curTime.plusMinutes(15);
            optPerfHistEntitiyList = perfHistRepository.findHistByTrunkIdAndDirectionAndOcrTime(ticketPerfDataCheckEntity.getTrunkId(), ticketPerfDataCheckEntity.getDirection(), Timestamp.valueOf(plusTime)).orElseThrow();
        } catch (NullPointerException | IllegalArgumentException | PersistenceException e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }

        return optPerfHistEntitiyList;
    }
}
