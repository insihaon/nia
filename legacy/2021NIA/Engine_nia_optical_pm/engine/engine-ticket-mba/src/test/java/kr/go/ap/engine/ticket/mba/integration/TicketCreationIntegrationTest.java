package kr.go.ap.engine.ticket.mba.integration;

import kr.go.ap.core.primary.nia.dto.mba.EngineLowPmDataListDto;
import kr.go.ap.core.primary.nia.dto.pm.optical.OpticalPerformanceDto;
import kr.go.ap.core.primary.nia.dto.rca.engine.ui.RcaEngineResultDto;
import kr.go.ap.core.primary.nia.entity.mba.engine.MbaTicketCurEntity;
import kr.go.ap.core.primary.nia.entity.mba.ticket.RoadmLowOpticalPerformanceEntity;
import kr.go.ap.core.primary.nia.entity.mba.engine.MbaTicketCableEntity;
import kr.go.ap.core.repository.primary.jpa.mba.ticket.RoadmLowOpticalPerformanceRepository;
import kr.go.ap.core.repository.primary.jpa.mba.ticket.TicketPerformanceDataCheckRepository;
import kr.go.ap.core.repository.primary.jpa.mba.engine.MbaTicketCableRepository;
import kr.go.ap.core.repository.primary.jpa.mba.engine.MbaTicketCurRepository;
import kr.go.ap.core.utility.map.OptPerfStructMapper;
import kr.go.ap.engine.ticket.mba.amqp.EngineToUiTicketPrdAmqp;
import kr.go.ap.engine.ticket.mba.service.ticket.TicketCreateService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("티켓 생성 통합 테스트")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TicketCreationIntegrationTest {

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

    @Captor
    private ArgumentCaptor<MbaTicketCurEntity> ticketCaptor;

    @Captor
    private ArgumentCaptor<List<MbaTicketCableEntity>> cableListCaptor;

    @Captor
    private ArgumentCaptor<RcaEngineResultDto> engineResultCaptor;

    private TicketCreateService ticketCreateService;

    @BeforeEach
    void setUp() {
        ticketCreateService = new TicketCreateService(
                optPerfStructMapper,
                ticketPerfDataCheckRepository,
                ticketCableRepository,
                ticketRepository,
                lowOptPerfRepository,
                engineToUiTicketPrdAmqp
        );
    }

    @Nested
    @DisplayName("티켓 생성 시나리오")
    class TicketCreationScenarioTest {

        @Test
        @Order(1)
        @DisplayName("시나리오 1: 광케이블 저신호 감지 후 티켓 생성 및 UI 알림")
        void scenario1_createTicketAndNotifyUi() {
            // Given: 전처리기로부터 저신호 데이터가 전달됨
            EngineLowPmDataListDto lowPmData = createLowPmDataWithTwoPoints();

            given(ticketRepository.nextTicketId()).willReturn("TICKET-2024-001");
            given(ticketCableRepository.nextTicketCableId()).willReturn("CABLE-001");

            // When: 티켓 생성 서비스가 호출됨
            ticketCreateService.createMbaTicket(lowPmData);

            // Then: 티켓이 생성되고 UI로 알림이 전송됨
            verify(ticketRepository).save(ticketCaptor.capture());
            verify(ticketCableRepository).saveAll(cableListCaptor.capture());
            verify(engineToUiTicketPrdAmqp).sendMessageCmd(engineResultCaptor.capture());

            MbaTicketCurEntity savedTicket = ticketCaptor.getValue();
            assertThat(savedTicket.getTicketId()).isEqualTo("TICKET-2024-001");
            assertThat(savedTicket.getTicketType()).isEqualTo("PF");
            assertThat(savedTicket.getStatus()).isEqualTo("INIT");

            RcaEngineResultDto sentResult = engineResultCaptor.getValue();
            assertThat(sentResult.getEventType()).isEqualTo("TICKET_NEW");
            assertThat(sentResult.getTicketType()).isEqualTo("PF");
        }

        @Test
        @Order(2)
        @DisplayName("시나리오 2: 4개 지점의 저신호로 2개의 케이블 엔티티 생성")
        void scenario2_createTwoCableEntitiesForFourPoints() {
            // Given: 4개 지점에서 저신호가 감지됨 (A-Z 쌍 2개)
            EngineLowPmDataListDto lowPmData = createLowPmDataWithFourPoints();

            given(ticketRepository.nextTicketId()).willReturn("TICKET-2024-002");
            given(ticketCableRepository.nextTicketCableId())
                    .willReturn("CABLE-001")
                    .willReturn("CABLE-002");

            // When
            ticketCreateService.createMbaTicket(lowPmData);

            // Then: 2개의 케이블 엔티티가 생성됨
            verify(ticketCableRepository).saveAll(cableListCaptor.capture());
            List<MbaTicketCableEntity> cables = cableListCaptor.getValue();

            assertThat(cables).hasSize(2);
        }

        @Test
        @Order(3)
        @DisplayName("시나리오 3: 히스토리 데이터가 있으면 LowOpticalPerformance에 저장")
        void scenario3_saveLowOpticalPerformanceWithHistory() {
            // Given
            EngineLowPmDataListDto lowPmData = createLowPmDataWithHistory();


            given(ticketRepository.nextTicketId()).willReturn("TICKET-2024-003");
            given(ticketCableRepository.nextTicketCableId()).willReturn("CABLE-001");
            given(optPerfStructMapper.toLowOptPerfEntity(any(OpticalPerformanceDto.class)))
                    .willReturn(new RoadmLowOpticalPerformanceEntity());

            // When
            ticketCreateService.createMbaTicket(lowPmData);

            // Then: 히스토리 데이터가 저장됨
            verify(lowOptPerfRepository, atLeastOnce()).save(any(RoadmLowOpticalPerformanceEntity.class));
        }

        @Test
        @Order(4)
        @DisplayName("시나리오 4: 티켓 생성 후 성능 데이터 체크 엔티티 저장")
        void scenario4_savePerformanceDataCheckEntity() {
            // Given
            EngineLowPmDataListDto lowPmData = createLowPmDataWithTwoPoints();

            given(ticketRepository.nextTicketId()).willReturn("TICKET-2024-004");
            given(ticketCableRepository.nextTicketCableId()).willReturn("CABLE-001");

            // When
            ticketCreateService.createMbaTicket(lowPmData);

            // Then
            verify(ticketPerfDataCheckRepository).save(any());
        }
    }

    @Nested
    @DisplayName("엣지 케이스 테스트")
    class EdgeCaseTest {

        @Test
        @DisplayName("데이터 1개인 경우 티켓 생성하지 않음")
        void shouldNotCreateTicketForSingleDataPoint() {
            // Given
            EngineLowPmDataListDto singlePointData = createLowPmDataWithSinglePoint();

            // When
            ticketCreateService.createMbaTicket(singlePointData);

            // Then
            verifyNoInteractions(ticketRepository);
            verifyNoInteractions(engineToUiTicketPrdAmqp);
        }

        @Test
        @DisplayName("빈 리스트인 경우 티켓 생성하지 않음")
        void shouldNotCreateTicketForEmptyList() {
            // Given
            EngineLowPmDataListDto emptyData = EngineLowPmDataListDto.builder()
                    .lowPmDataDtoList(new ArrayList<>())
                    .build();

            // When
            ticketCreateService.createMbaTicket(emptyData);

            // Then
            verifyNoInteractions(ticketRepository);
        }

        @Test
        @DisplayName("null 히스토리 데이터로도 정상 동작")
        void shouldWorkWithNullHistoryData() {
            // Given
            EngineLowPmDataListDto dataWithNullHistory = EngineLowPmDataListDto.builder()
                    .lowPmDataDtoList(createTwoPointList())
                    .lowPmHistDataDtoList(null)
                    .build();

            given(ticketRepository.nextTicketId()).willReturn("TICKET-2024-005");
            given(ticketCableRepository.nextTicketCableId()).willReturn("CABLE-001");

            // When & Then
            Assertions.assertDoesNotThrow(() ->
                    ticketCreateService.createMbaTicket(dataWithNullHistory)
            );

            verify(ticketRepository).save(any());
        }
    }

    @Nested
    @DisplayName("티켓 필드 검증 테스트")
    class TicketFieldValidationTest {

        @Test
        @DisplayName("티켓의 RCA 결과 코드가 올바르게 설정됨")
        void shouldSetCorrectRcaResultCode() {
            // Given
            EngineLowPmDataListDto lowPmData = createLowPmDataWithTwoPoints();

            given(ticketRepository.nextTicketId()).willReturn("TICKET-001");
            given(ticketCableRepository.nextTicketCableId()).willReturn("CABLE-001");

            // When
            ticketCreateService.createMbaTicket(lowPmData);

            // Then
            verify(ticketRepository).save(ticketCaptor.capture());
            MbaTicketCurEntity ticket = ticketCaptor.getValue();

            assertThat(ticket.getTicketRcaResultCode()).isEqualTo("OPTICAL_CABLE_LOW_SIGNAL");
            assertThat(ticket.getTicketRcaResultDtlCode()).isEqualTo("광레벨 순단 저하");
            assertThat(ticket.getRootCauseDomain()).isEqualTo("ROADM");
            assertThat(ticket.getRootCauseType()).isEqualTo("MomentaryBreakoff");
        }

        @Test
        @DisplayName("totalRelatedAlarmCnt가 데이터 개수 + 1로 설정됨")
        void shouldCalculateTotalRelatedAlarmCnt() {
            // Given
            List<OpticalPerformanceDto> threePoints = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                threePoints.add(createOpticalPerformanceDto("TID-" + i, i + 1));
            }

            EngineLowPmDataListDto lowPmData = EngineLowPmDataListDto.builder()
                    .lowPmDataDtoList(threePoints)
                    .lowPmHistDataDtoList(new ArrayList<>())
                    .build();

            given(ticketRepository.nextTicketId()).willReturn("TICKET-001");
            given(ticketCableRepository.nextTicketCableId()).willReturn("CABLE-001");

            // When
            ticketCreateService.createMbaTicket(lowPmData);

            // Then
            verify(ticketRepository).save(ticketCaptor.capture());
            assertThat(ticketCaptor.getValue().getTotalRelatedAlarmCnt()).isEqualTo(4); // 3 + 1
        }
    }

    // Helper methods
    private EngineLowPmDataListDto createLowPmDataWithTwoPoints() {
        return EngineLowPmDataListDto.builder()
                .lowPmDataDtoList(createTwoPointList())
                .lowPmHistDataDtoList(new ArrayList<>())
                .build();
    }

    private List<OpticalPerformanceDto> createTwoPointList() {
        List<OpticalPerformanceDto> list = new ArrayList<>();
        list.add(createOpticalPerformanceDto("TID-001", 1));
        list.add(createOpticalPerformanceDto("TID-002", 2));
        return list;
    }

    private EngineLowPmDataListDto createLowPmDataWithFourPoints() {
        List<OpticalPerformanceDto> list = new ArrayList<>();
        for (int i = 1; i <= 4; i++) {
            list.add(createOpticalPerformanceDto("TID-" + String.format("%03d", i), i));
        }
        return EngineLowPmDataListDto.builder()
                .lowPmDataDtoList(list)
                .lowPmHistDataDtoList(new ArrayList<>())
                .build();
    }

    private EngineLowPmDataListDto createLowPmDataWithSinglePoint() {
        List<OpticalPerformanceDto> list = new ArrayList<>();
        list.add(createOpticalPerformanceDto("TID-001", 1));
        return EngineLowPmDataListDto.builder()
                .lowPmDataDtoList(list)
                .build();
    }

    private EngineLowPmDataListDto createLowPmDataWithHistory() {
        List<OpticalPerformanceDto> histList = new ArrayList<>();
        histList.add(createOpticalPerformanceDto("TID-HIST-001", 1));

        return EngineLowPmDataListDto.builder()
                .lowPmDataDtoList(createTwoPointList())
                .lowPmHistDataDtoList(histList)
                .build();
    }

    private OpticalPerformanceDto createOpticalPerformanceDto(String tid, int routeNum) {
        return OpticalPerformanceDto.builder()
                .trunkId("TRUNK-001")
                .trunkName("TestTrunk")
                .tid(tid)
                .sysname("SYS-" + routeNum)
                .ptpname("PTP-" + routeNum)
                .ocrtime(new Timestamp(System.currentTimeMillis()))
                .direction("UP")
                .routeNum(routeNum)
                .valueMax(10.0f)
                .valueMin(5.0f)
                .build();
    }
}
