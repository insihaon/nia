package kr.go.ap.engine.ticket.mba.service.ticket;

import kr.go.ap.core.primary.nia.dto.mba.EngineLowPmDataListDto;
import kr.go.ap.core.primary.nia.dto.pm.optical.OpticalPerformanceDto;
import kr.go.ap.core.primary.nia.dto.rca.engine.ui.RcaEngineResultDto;
import kr.go.ap.core.primary.nia.entity.mba.engine.MbaTicketCurEntity;
import kr.go.ap.core.primary.nia.entity.mba.ticket.RoadmLowOpticalPerformanceEntity;
import kr.go.ap.core.primary.nia.entity.mba.ticket.TicketPerformanceDataCheckEntity;
import kr.go.ap.core.primary.nia.entity.mba.engine.MbaTicketCableEntity;
import kr.go.ap.core.primary.nia.entity.mba.engine.key.MbaTicketCableKey;
import kr.go.ap.core.repository.primary.jpa.mba.ticket.RoadmLowOpticalPerformanceRepository;
import kr.go.ap.core.repository.primary.jpa.mba.ticket.TicketPerformanceDataCheckRepository;
import kr.go.ap.core.repository.primary.jpa.mba.engine.MbaTicketCableRepository;
import kr.go.ap.core.repository.primary.jpa.mba.engine.MbaTicketCurRepository;
import kr.go.ap.core.utility.map.OptPerfStructMapper;
import kr.go.ap.engine.ticket.mba.amqp.EngineToUiTicketPrdAmqp;
import kr.go.ap.utility.common.UtlDateHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TicketCreateService {

    private final OptPerfStructMapper optPerfStructMapper;
    private final TicketPerformanceDataCheckRepository ticketPerfDataCheckRepository;
    //    private final MbaTicketMapper mbaTicketMapper;
    private final MbaTicketCableRepository ticketCableRepository;
    private final MbaTicketCurRepository ticketRepository;
    private final RoadmLowOpticalPerformanceRepository lowOptPerfRepository;
    private final EngineToUiTicketPrdAmqp engineToUiTicketPrdAmqp;

    public void createMbaTicket(EngineLowPmDataListDto engineLowPmDataListDto) {
        log.info("createMbaTicket : " + engineLowPmDataListDto.getLowPmDataDtoList().toString());
        String ticketId = null;
//        String ticketCableId = null;

        List<OpticalPerformanceDto> lowPmDataDtoList = null;
        RcaEngineResultDto rcaEngineResultDto = null;
        TicketPerformanceDataCheckEntity ticketPerfCheckDataEntity = null;

        MbaTicketCurEntity ticketEntity = null;
        MbaTicketCableEntity ticketCableEntity = null;
        List<MbaTicketCableEntity> ticketCableEntityList = null;


        try {
            if (!engineLowPmDataListDto.getLowPmDataDtoList().isEmpty()
                    && engineLowPmDataListDto.getLowPmDataDtoList().size() > 1) {

                ticketId = ticketRepository.nextTicketId();
                ticketCableEntityList = new ArrayList<>();
                ticketEntity = createTicketEntity(engineLowPmDataListDto, ticketId);
                ticketCableEntity = createTicketCableEntity(engineLowPmDataListDto, ticketCableEntity, ticketCableEntityList, ticketId);


                if (ticketCableEntity != null) {

                    log.info("ticket save : " + ticketEntity.toString());
                    ticketRepository.save(ticketEntity);

                    log.info("ticketCable save : " + ticketCableEntity.toString());
                    ticketCableRepository.saveAll(ticketCableEntityList);

                    lowPmDataDtoList = engineLowPmDataListDto.getLowPmHistDataDtoList();

                    if (lowPmDataDtoList!=null && !lowPmDataDtoList.isEmpty()) {
                        for (OpticalPerformanceDto lowPmDataDto : lowPmDataDtoList) {
                            RoadmLowOpticalPerformanceEntity lowOptPerfEntity = optPerfStructMapper.toLowOptPerfEntity(lowPmDataDto);
                            lowOptPerfEntity.setTicketId(ticketEntity.getTicketId());
                            lowOptPerfRepository.save(lowOptPerfEntity);
                        }
                    }


                    ticketPerfCheckDataEntity = TicketPerformanceDataCheckEntity.builder()
                            .ticketId(ticketEntity.getTicketId())
                            .ocrTime(ticketEntity.getFaultTime())
                            .trunkId(ticketEntity.getTrunkId())
                            .direction(ticketEntity.getDirection())
                            .build();

                    ticketPerfDataCheckRepository.save(ticketPerfCheckDataEntity);

                    rcaEngineResultDto = RcaEngineResultDto.builder()
                            .ticketId(ticketEntity.getTicketId())
                            .eventType("TICKET_NEW")
                            .ticketType("PF")
                            .build();

                    engineToUiTicketPrdAmqp.sendMessageCmd(rcaEngineResultDto);
                }
            }
        } catch (NullPointerException | PersistenceException | IndexOutOfBoundsException e) {
            log.error("createMbaTicket error " + ExceptionUtils.getStackTrace(e));
        }
    }

    private MbaTicketCableEntity createTicketCableEntity(EngineLowPmDataListDto engineLowPmDataListDto, MbaTicketCableEntity ticketCableEntity, List<MbaTicketCableEntity> ticketCableEntityList, String ticketId) {
        boolean isStop = true;
        int i = 1;
        String ticketCableId;
        while (isStop) {


            // Ticket Cable 생성

            if (i - 1 >= engineLowPmDataListDto.getLowPmDataDtoList().size() - 1) {
                isStop = false;
            }


            OpticalPerformanceDto optPerfDto = engineLowPmDataListDto.getLowPmDataDtoList().get(i - 1);

//
//            String tid = optPerfDto.getTid();
//            String ptpname = optPerfDto.getPtpname();
//            String[] split = ptpname.split("-", 2);
//            String sysname = tid + "-" + split[0];
//            String port = split[1];

            if ((i % 2) == 0) {
                // set Z

                ticketCableEntity.setTidz(optPerfDto.getTid());
                ticketCableEntity.setSysnamez(optPerfDto.getSysname());
                ticketCableEntity.setPtpnamez(optPerfDto.getPtpname());
                ticketCableEntityList.add(ticketCableEntity);

            } else {
                // set A
                ticketCableId = ticketCableRepository.nextTicketCableId();
                ticketCableEntity = MbaTicketCableEntity.builder()
                        .mbaTicketCableKey(MbaTicketCableKey.builder()
                                .ticketId(ticketId)
                                .ticketCableId(ticketCableId)
                                .build())
                        .tida(optPerfDto.getTid())
                        .sysnamea(optPerfDto.getSysname())
                        .ptpnamea(optPerfDto.getPtpname())
                        .build();
            }
            i++;
        }
        return ticketCableEntity;
    }

    private MbaTicketCurEntity createTicketEntity(EngineLowPmDataListDto engineLowPmDataListDto, String ticketId) {
        MbaTicketCurEntity ticketEntity;
        ticketEntity = MbaTicketCurEntity.builder()
                .ticketId(ticketId)
                .ticketType("PF")
                .ticketGenerationTime(UtlDateHelper.getCurrentTime())
//                        .ticketUpdateTime(UtlDateHelper.getCurrentTime())
                .ticketRcaResultCode("OPTICAL_CABLE_LOW_SIGNAL")
                .ticketRcaResultDtlCode("광레벨 순단 저하")
                .faultTime(engineLowPmDataListDto.getLowPmDataDtoList().get(0).getOcrtime())
                .rootCauseDomain("ROADM")
                .rootCauseType("MomentaryBreakoff")
                .totalRelatedAlarmCnt(engineLowPmDataListDto.getLowPmDataDtoList().size() + 1)
                .trunkId(engineLowPmDataListDto.getLowPmDataDtoList().get(0).getTrunkId())
                .trunkName(engineLowPmDataListDto.getLowPmDataDtoList().get(0).getTrunkName())
                .direction(engineLowPmDataListDto.getLowPmDataDtoList().get(0).getDirection())
                .status("INIT")
                .occur(true)
                .build();
        return ticketEntity;
    }
}
