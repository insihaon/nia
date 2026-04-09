package kr.go.ap.engine.ticket.mba.service.ticket;

import kr.go.ap.core.primary.nia.dto.mba.EngineLowPmDataListDto;
import kr.go.ap.core.primary.nia.dto.pm.optical.OpticalPerformanceDto;
import kr.go.ap.core.primary.nia.dto.rca.engine.ui.RcaEngineResultDto;
import kr.go.ap.core.primary.nia.entity.mba.engine.MbaTicketCurEntity;
import kr.go.ap.core.primary.nia.entity.mba.ticket.RoadmLowOpticalPerformanceEntity;
import kr.go.ap.core.primary.nia.entity.mba.ticket.TicketPerformanceDataCheckEntity;
import kr.go.ap.core.primary.nia.entity.mba.ticket.key.RoadmLowOpticalPerformanceKey;
import kr.go.ap.core.primary.nia.entity.mba.engine.MbaTicketCableEntity;
import kr.go.ap.core.repository.primary.jpa.mba.ticket.RoadmLowOpticalPerformanceRepository;
import kr.go.ap.core.repository.primary.jpa.mba.ticket.TicketPerformanceDataCheckRepository;
import kr.go.ap.core.repository.primary.jpa.mba.engine.MbaTicketCableRepository;
import kr.go.ap.core.repository.primary.jpa.mba.engine.MbaTicketCurRepository;
import kr.go.ap.core.utility.map.OptPerfStructMapper;
import kr.go.ap.engine.ticket.mba.amqp.EngineToUiTicketPrdAmqp;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("TicketCreateService 단위 테스트")
class TicketCreateServiceTest {

    @Mock
    private OptPerfStructMapper optPerfStructMapper;

    @Mock
    private TicketPerformanceDataCheckRepository ticketPerfDataCheckRepository;

    @Mock
    private MbaTicketCableRepository ticketCableRepository;

    @Mock
    private MbaTicketCurRepository ticketRepository;

    @Mock
    private RoadmLowOpticalPerformanceRepository lowOptPerfRepository;

    @Mock
    private EngineToUiTicketPrdAmqp engineToUiTicketPrdAmqp;

    @InjectMocks
    private TicketCreateService ticketCreateService;

    @Captor
    private ArgumentCaptor<MbaTicketCurEntity> ticketEntityCaptor;

    @Captor
    private ArgumentCaptor<List<MbaTicketCableEntity>> ticketCableListCaptor;

    @Captor
    private ArgumentCaptor<RcaEngineResultDto> engineResultCaptor;

    @Captor
    private ArgumentCaptor<TicketPerformanceDataCheckEntity> perfCheckCaptor;

    private OpticalPerformanceDto createOpticalPerformanceDto(String tid, String sysname, String direction, int routeNum) {
        return OpticalPerformanceDto.builder()
                .trunkId("TRUNK-001")
                .trunkName("TestTrunk")
                .tid(tid)
                .sysname(sysname)
                .ptpname("PTP-" + routeNum)
                .ocrtime(new Timestamp(System.currentTimeMillis()))
                .direction(direction)
                .routeNum(routeNum)
                .valueMax(10.0f)
                .valueMin(5.0f)
                .build();
    }

    @Nested
    @DisplayName("createMbaTicket 메서드")
    class CreateMbaTicketTest {

        @Test
        @DisplayName("빈 데이터 리스트는 티켓을 생성하지 않는다")
        void shouldNotCreateTicketForEmptyList() {
            // Given
            EngineLowPmDataListDto dto = EngineLowPmDataListDto.builder()
                    .lowPmDataDtoList(Collections.emptyList())
                    .build();

            // When
            ticketCreateService.createMbaTicket(dto);

            // Then
            verifyNoInteractions(ticketRepository);
            verifyNoInteractions(ticketCableRepository);
            verifyNoInteractions(engineToUiTicketPrdAmqp);
        }

        @Test
        @DisplayName("데이터가 1개일 때는 티켓을 생성하지 않는다 (2개 이상 필요)")
        void shouldNotCreateTicketForSingleDataItem() {
            // Given
            List<OpticalPerformanceDto> singleItemList = List.of(
                    createOpticalPerformanceDto("TID-001", "SYS-001", "UP", 1)
            );

            EngineLowPmDataListDto dto = EngineLowPmDataListDto.builder()
                    .lowPmDataDtoList(singleItemList)
                    .build();

            // When
            ticketCreateService.createMbaTicket(dto);

            // Then
            verifyNoInteractions(ticketRepository);
            verifyNoInteractions(ticketCableRepository);
        }

        @Test
        @DisplayName("동일 트렁크/방향 기준 다른 routenum을 가진 2개 이상의 데이터가 있을 때 티켓을 생성한다")
        void shouldCreateTicketForMultipleDataItems() {
            // Given
            List<OpticalPerformanceDto> dataList = List.of(
                    createOpticalPerformanceDto("TID-001", "SYS-001", "UP", 1),
                    createOpticalPerformanceDto("TID-002", "SYS-002", "UP", 2)
            );

            EngineLowPmDataListDto dto = EngineLowPmDataListDto.builder()
                    .lowPmDataDtoList(dataList)
                    .lowPmHistDataDtoList(new ArrayList<>())
                    .build();

            given(ticketRepository.nextTicketId()).willReturn("TICKET-001");
            given(ticketCableRepository.nextTicketCableId()).willReturn("CABLE-001");

            // When
            ticketCreateService.createMbaTicket(dto);

            // Then
            verify(ticketRepository).save(ticketEntityCaptor.capture());
            MbaTicketCurEntity savedTicket = ticketEntityCaptor.getValue();

            assertThat(savedTicket.getTicketId()).isEqualTo("TICKET-001");
            assertThat(savedTicket.getTicketType()).isEqualTo("PF");
            assertThat(savedTicket.getStatus()).isEqualTo("INIT");
            assertThat(savedTicket.isOccur()).isTrue();
            assertThat(savedTicket.getRootCauseDomain()).isEqualTo("ROADM");
            assertThat(savedTicket.getTicketRcaResultCode()).isEqualTo("OPTICAL_CABLE_LOW_SIGNAL");
        }

        @Test
        @DisplayName("티켓 생성 후 AMQP 메시지를 전송한다")
        void shouldSendAmqpMessageAfterTicketCreation() {
            // Given
            List<OpticalPerformanceDto> dataList = List.of(
                    createOpticalPerformanceDto("TID-001", "SYS-001", "UP", 1),
                    createOpticalPerformanceDto("TID-002", "SYS-002", "UP", 2)
            );

            EngineLowPmDataListDto dto = EngineLowPmDataListDto.builder()
                    .lowPmDataDtoList(dataList)
                    .lowPmHistDataDtoList(new ArrayList<>())
                    .build();

            given(ticketRepository.nextTicketId()).willReturn("TICKET-001");
            given(ticketCableRepository.nextTicketCableId()).willReturn("CABLE-001");

            // When
            ticketCreateService.createMbaTicket(dto);

            // Then
            verify(engineToUiTicketPrdAmqp).sendMessageCmd(engineResultCaptor.capture());
            RcaEngineResultDto result = engineResultCaptor.getValue();

            assertThat(result.getTicketId()).isEqualTo("TICKET-001");
            assertThat(result.getEventType()).isEqualTo("TICKET_NEW");
            assertThat(result.getTicketType()).isEqualTo("PF");
        }

        @Test
        @DisplayName("티켓 케이블 엔티티가 올바르게 생성된다")
        void shouldCreateTicketCableEntitiesCorrectly() {
            // Given
            List<OpticalPerformanceDto> dataList = List.of(
                    createOpticalPerformanceDto("TID-001", "SYS-001", "UP", 1),
                    createOpticalPerformanceDto("TID-002", "SYS-002", "UP", 2)
            );

            EngineLowPmDataListDto dto = EngineLowPmDataListDto.builder()
                    .lowPmDataDtoList(dataList)
                    .lowPmHistDataDtoList(new ArrayList<>())
                    .build();

            given(ticketRepository.nextTicketId()).willReturn("TICKET-001");
            given(ticketCableRepository.nextTicketCableId()).willReturn("CABLE-001");

            // When
            ticketCreateService.createMbaTicket(dto);

            // Then
            verify(ticketCableRepository).saveAll(ticketCableListCaptor.capture());
            List<MbaTicketCableEntity> cableEntities = ticketCableListCaptor.getValue();

            assertThat(cableEntities).hasSize(1);
            MbaTicketCableEntity cableEntity = cableEntities.get(0);
//            assertThat(cableEntity.getTida()).isEqualTo("TID-001");
//            assertThat(cableEntity.getTidz()).isEqualTo("TID-002");
        }

        @Test
        @DisplayName("히스토리 데이터가 있으면 LowOpticalPerformance 엔티티를 저장한다")
        void shouldSaveLowOpticalPerformanceWithHistData() {
            // Given
            List<OpticalPerformanceDto> dataList = List.of(
                    createOpticalPerformanceDto("TID-001", "SYS-001", "UP", 1),
                    createOpticalPerformanceDto("TID-002", "SYS-002", "UP", 2)
            );

            List<OpticalPerformanceDto> histDataList = List.of(
                    createOpticalPerformanceDto("TID-HIST-001", "SYS-HIST-001", "UP", 1)
            );

            EngineLowPmDataListDto dto = EngineLowPmDataListDto.builder()
                    .lowPmDataDtoList(dataList)
                    .lowPmHistDataDtoList(histDataList)
                    .build();

            given(ticketRepository.nextTicketId()).willReturn("TICKET-001");
            given(ticketCableRepository.nextTicketCableId()).willReturn("CABLE-001");

            // setTicketId() 호출 시 NPE 방지를 위해 키가 초기화된 엔티티 반환
            RoadmLowOpticalPerformanceEntity lowOptPerfEntity = RoadmLowOpticalPerformanceEntity.builder()
                    .roadmLowOpticalPerformanceKey(RoadmLowOpticalPerformanceKey.builder().build())
                    .build();
            given(optPerfStructMapper.toLowOptPerfEntity(any(OpticalPerformanceDto.class)))
                    .willReturn(lowOptPerfEntity);

            // When
            ticketCreateService.createMbaTicket(dto);

            // Then
            verify(lowOptPerfRepository, times(1)).saveAll(anyList());
        }

        @Test
        @DisplayName("Performance 데이터 체크 엔티티가 저장된다")
        void shouldSavePerformanceDataCheckEntity() {
            // Given
            List<OpticalPerformanceDto> dataList = List.of(
                    createOpticalPerformanceDto("TID-001", "SYS-001", "UP", 1),
                    createOpticalPerformanceDto("TID-002", "SYS-002", "UP", 2)
            );

            EngineLowPmDataListDto dto = EngineLowPmDataListDto.builder()
                    .lowPmDataDtoList(dataList)
                    .lowPmHistDataDtoList(new ArrayList<>())
                    .build();

            given(ticketRepository.nextTicketId()).willReturn("TICKET-001");
            given(ticketCableRepository.nextTicketCableId()).willReturn("CABLE-001");

            // When
            ticketCreateService.createMbaTicket(dto);

            // Then
            verify(ticketPerfDataCheckRepository).save(perfCheckCaptor.capture());
            TicketPerformanceDataCheckEntity perfCheck = perfCheckCaptor.getValue();

            assertThat(perfCheck.getTicketId()).isEqualTo("TICKET-001");
            assertThat(perfCheck.getTrunkId()).isEqualTo("TRUNK-001");
            assertThat(perfCheck.getDirection()).isEqualTo("UP");
        }

        @Test
        @DisplayName("4개 데이터로 2개의 케이블 엔티티가 생성된다")
        void shouldCreateTwoCableEntitiesForFourDataItems() {
            // Given
            List<OpticalPerformanceDto> dataList = List.of(
                    createOpticalPerformanceDto("TID-001", "SYS-001", "UP", 1),
                    createOpticalPerformanceDto("TID-002", "SYS-002", "UP", 2),
                    createOpticalPerformanceDto("TID-003", "SYS-003", "UP", 3),
                    createOpticalPerformanceDto("TID-004", "SYS-004", "UP", 4)
            );

            EngineLowPmDataListDto dto = EngineLowPmDataListDto.builder()
                    .lowPmDataDtoList(dataList)
                    .lowPmHistDataDtoList(new ArrayList<>())
                    .build();

            given(ticketRepository.nextTicketId()).willReturn("TICKET-001");
            given(ticketCableRepository.nextTicketCableId())
                    .willReturn("CABLE-001")
                    .willReturn("CABLE-002");

            // When
            ticketCreateService.createMbaTicket(dto);

            // Then
            verify(ticketCableRepository).saveAll(ticketCableListCaptor.capture());
            List<MbaTicketCableEntity> cableEntities = ticketCableListCaptor.getValue();

            assertThat(cableEntities).hasSize(2);
        }

        @Test
        @DisplayName("totalRelatedAlarmCnt가 올바르게 계산된다")
        void shouldCalculateTotalRelatedAlarmCntCorrectly() {
            // Given
            List<OpticalPerformanceDto> dataList = List.of(
                    createOpticalPerformanceDto("TID-001", "SYS-001", "UP", 1),
                    createOpticalPerformanceDto("TID-002", "SYS-002", "UP", 2),
                    createOpticalPerformanceDto("TID-003", "SYS-003", "UP", 3)
            );

            EngineLowPmDataListDto dto = EngineLowPmDataListDto.builder()
                    .lowPmDataDtoList(dataList)
                    .lowPmHistDataDtoList(new ArrayList<>())
                    .build();

            given(ticketRepository.nextTicketId()).willReturn("TICKET-001");
            given(ticketCableRepository.nextTicketCableId()).willReturn("CABLE-001");

            // When
            ticketCreateService.createMbaTicket(dto);

            // Then
            verify(ticketRepository).save(ticketEntityCaptor.capture());
            MbaTicketCurEntity savedTicket = ticketEntityCaptor.getValue();

            // totalRelatedAlarmCnt = dataList.size() + 1 = 4
            assertThat(savedTicket.getTotalRelatedAlarmCnt()).isEqualTo(4);
        }
    }
}
