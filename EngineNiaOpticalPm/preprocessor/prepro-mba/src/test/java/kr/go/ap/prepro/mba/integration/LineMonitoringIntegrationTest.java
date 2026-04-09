package kr.go.ap.prepro.mba.integration;

import kr.go.ap.core.primary.nia.dto.linkage.model.ModelResultDto;
import kr.go.ap.core.primary.nia.dto.pm.optical.OpticalPerformanceDto;
import kr.go.ap.core.primary.nia.entity.mba.performance.RoadmCheckOpticalPerformanceEntity;
import kr.go.ap.core.primary.nia.entity.mba.performance.key.RoadmCheckOpticalPerformanceKey;
import kr.go.ap.core.repository.primary.jpa.mba.performance.RoadmCheckOpticalPerformanceRepository;
import kr.go.ap.core.utility.map.OptPerfStructMapper;
import kr.go.ap.prepro.mba.service.LineMonitoringHdlService;
import kr.go.ap.prepro.mba.service.ticket.TicketDataService;
import org.apache.ibatis.exceptions.PersistenceException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("LineMonitoring 통합 테스트")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class LineMonitoringIntegrationTest {

    @Mock
    private TicketDataService ticketDataService;

    @Mock
    private RoadmCheckOpticalPerformanceRepository checkOptPerfRepo;

    private ModelMapper modelMapper;

    @Mock
    private OptPerfStructMapper optPerfStructMapper;

    private LineMonitoringHdlService lineMonitoringHdlService;

    @BeforeEach
    void setUp() {
        modelMapper = new ModelMapper();
        lineMonitoringHdlService = new LineMonitoringHdlService(
                ticketDataService,
                checkOptPerfRepo,
                modelMapper,
                optPerfStructMapper
        );
    }

    @Nested
    @DisplayName("광신호 모니터링 시나리오 테스트")
    class OpticalSignalMonitoringScenarioTest {

        @Test
        @Order(1)
        @DisplayName("시나리오 1: AI 모델 결과 수신 후 신호 저하 감지 및 저장")
        void scenario1_receiveAiResultAndDetectLowSignal() {
            // Given: AI 모델로부터 신호 저하 데이터가 수신됨
            List<ModelResultDto> aiResults = createAiModelResults();

            RoadmCheckOpticalPerformanceEntity entity = createTestEntity();
            given(optPerfStructMapper.toCheckOptPerfEntity(any(OpticalPerformanceDto.class)))
                    .willReturn(entity);

            // When: 라인 모니터링 프로세서가 데이터를 처리
            lineMonitoringHdlService.lineMonitoringHdlProcessor(aiResults);

            // Then: 신호 저하 데이터가 저장되고 티켓 서비스가 호출됨
            verify(checkOptPerfRepo).saveAll(anyList());
            verify(ticketDataService).setTicketData(anyList());
        }

        @Test
        @Order(2)
        @DisplayName("시나리오 2: 정상 신호 데이터는 저장하지 않음")
        void scenario2_normalSignalNotSaved() {
            // Given: AI 모델로부터 정상 신호 데이터가 수신됨
            List<ModelResultDto> normalResults = createNormalSignalResults();

            // When: 라인 모니터링 프로세서가 데이터를 처리
            lineMonitoringHdlService.lineMonitoringHdlProcessor(normalResults);

            // Then: 저장되지 않음
            verify(checkOptPerfRepo, never()).saveAll(anyList());
            verify(ticketDataService, never()).setTicketData(anyList());
        }

        @Test
        @Order(3)
        @DisplayName("시나리오 3: 혼합 데이터에서 신호 저하만 필터링")
        void scenario3_filterOnlyLowSignalFromMixedData() {
            // Given: 정상 신호와 저하 신호가 혼합된 데이터
            List<ModelResultDto> mixedResults = createMixedSignalResults();

            RoadmCheckOpticalPerformanceEntity entity = createTestEntity();
            given(optPerfStructMapper.toCheckOptPerfEntity(any(OpticalPerformanceDto.class)))
                    .willReturn(entity);

            // When
            lineMonitoringHdlService.lineMonitoringHdlProcessor(mixedResults);

            // Then: 저하 신호만 저장됨
            verify(checkOptPerfRepo).saveAll(anyList());
            verify(ticketDataService).setTicketData(anyList());
        }

        @Test
        @Order(4)
        @DisplayName("시나리오 4: 다중 트렁크에서 동시 신호 저하 감지")
        void scenario4_detectLowSignalFromMultipleTrunks() {
            // Given: 여러 트렁크에서 신호 저하가 감지됨
            List<ModelResultDto> multiTrunkResults = createMultiTrunkLowSignalResults();

            RoadmCheckOpticalPerformanceEntity entity = createTestEntity();
            given(optPerfStructMapper.toCheckOptPerfEntity(any(OpticalPerformanceDto.class)))
                    .willReturn(entity);

            // When
            lineMonitoringHdlService.lineMonitoringHdlProcessor(multiTrunkResults);

            // Then
            verify(checkOptPerfRepo).saveAll(anyList());
            verify(ticketDataService).setTicketData(anyList());
        }

        @Test
        @Order(5)
        @DisplayName("시나리오 5: 경계값(3dB)에서 신호 저하 감지")
        void scenario5_detectAtBoundaryValue() {
            // Given: 정확히 3dB 차이나는 데이터
            ModelResultDto boundaryResult = ModelResultDto.builder()
                    .trunkId("TRUNK-BOUNDARY")
                    .tid("TID-BOUNDARY")
                    .sysname("SYS-BOUNDARY")
                    .ptpname("PTP-BOUNDARY")
                    .valueMax(8.0f)
                    .valueMin(5.0f)  // 차이: 3.0
                    .ocrtime(new Timestamp(System.currentTimeMillis()))
                    .direction("UP")
                    .routeNum(1)
                    .build();

            RoadmCheckOpticalPerformanceEntity entity = createTestEntity();
            given(optPerfStructMapper.toCheckOptPerfEntity(any(OpticalPerformanceDto.class)))
                    .willReturn(entity);

            // When
            lineMonitoringHdlService.lineMonitoringHdlProcessor(List.of(boundaryResult));

            // Then: 경계값에서도 저장됨
            verify(checkOptPerfRepo).saveAll(anyList());
        }
    }

    @Nested
    @DisplayName("예외 처리 시나리오 테스트")
    class ExceptionHandlingScenarioTest {

        @Test
        @DisplayName("데이터베이스 저장 실패 시 예외 처리")
        void shouldHandleDatabaseSaveFailure() {
            // Given
            List<ModelResultDto> results = createAiModelResults();

            given(optPerfStructMapper.toCheckOptPerfEntity(any(OpticalPerformanceDto.class)))
                    .willReturn(createTestEntity());
            doThrow(new PersistenceException("DB Error"))
                    .when(checkOptPerfRepo).saveAll(anyList());

            // When & Then: 예외가 발생해도 크래시하지 않음
            org.junit.jupiter.api.Assertions.assertDoesNotThrow(() ->
                lineMonitoringHdlService.lineMonitoringHdlProcessor(results)
            );
        }

        @Test
        @DisplayName("TicketDataService 호출 실패 시 예외 처리")
        void shouldHandleTicketServiceFailure() {
            // Given
            List<ModelResultDto> results = createAiModelResults();

            given(optPerfStructMapper.toCheckOptPerfEntity(any(OpticalPerformanceDto.class)))
                    .willReturn(createTestEntity());
            doThrow(new NullPointerException("Ticket Service Error"))
                    .when(ticketDataService).setTicketData(anyList());

            // When & Then
            org.junit.jupiter.api.Assertions.assertDoesNotThrow(() ->
                lineMonitoringHdlService.lineMonitoringHdlProcessor(results)
            );
        }
    }

    // Helper methods
    private List<ModelResultDto> createAiModelResults() {
        List<ModelResultDto> results = new ArrayList<>();
        results.add(ModelResultDto.builder()
                .trunkId("TRUNK-001")
                .tid("TID-001")
                .sysname("SYS-001")
                .ptpname("PTP-001")
                .valueMax(10.0f)
                .valueMin(5.0f)  // 차이: 5.0 >= 3
                .ocrtime(new Timestamp(System.currentTimeMillis()))
                .direction("UP")
                .routeNum(1)
                .build());
        return results;
    }

    private List<ModelResultDto> createNormalSignalResults() {
        List<ModelResultDto> results = new ArrayList<>();
        results.add(ModelResultDto.builder()
                .trunkId("TRUNK-001")
                .tid("TID-001")
                .valueMax(6.0f)
                .valueMin(5.0f)  // 차이: 1.0 < 3
                .ocrtime(new Timestamp(System.currentTimeMillis()))
                .build());
        return results;
    }

    private List<ModelResultDto> createMixedSignalResults() {
        List<ModelResultDto> results = new ArrayList<>();
        // 신호 저하
        results.add(ModelResultDto.builder()
                .trunkId("TRUNK-001")
                .tid("TID-001")
                .valueMax(10.0f)
                .valueMin(5.0f)
                .ocrtime(new Timestamp(System.currentTimeMillis()))
                .build());
        // 정상 신호
        results.add(ModelResultDto.builder()
                .trunkId("TRUNK-002")
                .tid("TID-002")
                .valueMax(6.0f)
                .valueMin(5.0f)
                .ocrtime(new Timestamp(System.currentTimeMillis()))
                .build());
        return results;
    }

    private List<ModelResultDto> createMultiTrunkLowSignalResults() {
        List<ModelResultDto> results = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            results.add(ModelResultDto.builder()
                    .trunkId("TRUNK-" + String.format("%03d", i))
                    .tid("TID-" + i)
                    .sysname("SYS-" + i)
                    .valueMax(10.0f + i)
                    .valueMin(5.0f)
                    .ocrtime(new Timestamp(System.currentTimeMillis()))
                    .direction(i % 2 == 0 ? "UP" : "DOWN")
                    .build());
        }
        return results;
    }

    private RoadmCheckOpticalPerformanceEntity createTestEntity() {
        return RoadmCheckOpticalPerformanceEntity.builder()
                .roadmCheckOpticalPerformanceKey(
                        RoadmCheckOpticalPerformanceKey.builder()
                                .trunkId("TRUNK-001")
                                .tid("TID-001")
                                .port("PORT-001")
                                .ocrtime(new Timestamp(System.currentTimeMillis()))
                                .ptpname("PTP-001")
                                .inOut("IN")
                                .build()
                )
                .trunkName("TestTrunk")
                .sysname("SYS-001")
                .valueCur(7.5f)
                .valueMax(10.0f)
                .valueMin(5.0f)
                .direction("UP")
                .routenum(1)
                .build();
    }
}
