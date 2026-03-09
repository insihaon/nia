package kr.go.ap.prepro.mba.service;

import kr.go.ap.core.primary.nia.dto.linkage.model.ModelResultDto;
import kr.go.ap.core.primary.nia.dto.pm.optical.OpticalPerformanceDto;
import kr.go.ap.core.primary.nia.entity.mba.performance.RoadmCheckOpticalPerformanceEntity;
import kr.go.ap.core.repository.primary.jpa.mba.performance.RoadmCheckOpticalPerformanceRepository;
import kr.go.ap.core.utility.map.OptPerfStructMapper;
import kr.go.ap.prepro.mba.service.ticket.TicketDataService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("LineMonitoringHdlService 단위 테스트")
class LineMonitoringHdlServiceUnitTest {

    @Mock
    private TicketDataService ticketDataService;

    @Mock
    private RoadmCheckOpticalPerformanceRepository checkOptPerfRepo;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private OptPerfStructMapper optPerfStructMapper;

    @InjectMocks
    private LineMonitoringHdlService lineMonitoringHdlService;

    @Captor
    private ArgumentCaptor<List<RoadmCheckOpticalPerformanceEntity>> entityListCaptor;

    @Captor
    private ArgumentCaptor<List<OpticalPerformanceDto>> dtoListCaptor;

    private ModelResultDto createModelResultDto(String trunkId, float valueMax, float valueMin) {
        return ModelResultDto.builder()
                .trunkId(trunkId)
                .tid("TEST-TID")
                .sysname("TEST-SYSNAME")
                .ptpname("TEST-PTPNAME")
                .valueMax(valueMax)
                .valueMin(valueMin)
                .ocrtime(new Timestamp(System.currentTimeMillis()))
                .direction("UP")
                .routeNum(1)
                .build();
    }

    private OpticalPerformanceDto createOpticalPerformanceDto(String trunkId, float valueMax, float valueMin) {
        return OpticalPerformanceDto.builder()
                .trunkId(trunkId)
                .tid("TEST-TID")
                .sysname("TEST-SYSNAME")
                .ptpname("TEST-PTPNAME")
                .valueMax(valueMax)
                .valueMin(valueMin)
                .ocrtime(new Timestamp(System.currentTimeMillis()))
                .direction("UP")
                .routeNum(1)
                .build();
    }

    @Nested
    @DisplayName("lineMonitoringHdlProcessor 메서드")
    class LineMonitoringHdlProcessorTest {

        @Test
        @DisplayName("빈 리스트 입력 시 아무 처리 없이 반환한다")
        void shouldReturnEarlyForEmptyList() {
            // Given
            List<ModelResultDto> emptyList = Collections.emptyList();

            // When
            lineMonitoringHdlService.lineMonitoringHdlProcessor(emptyList);

            // Then
            verifyNoInteractions(checkOptPerfRepo);
            verifyNoInteractions(ticketDataService);
        }

        @Test
        @DisplayName("null 입력 시 아무 처리 없이 반환한다")
        void shouldReturnEarlyForNullList() {
            // Given
            List<ModelResultDto> nullList = null;

            // When
            lineMonitoringHdlService.lineMonitoringHdlProcessor(nullList);

            // Then
            verifyNoInteractions(checkOptPerfRepo);
            verifyNoInteractions(ticketDataService);
        }

        @Test
        @DisplayName("신호 저하 감지 시(valueMax - valueMin >= 3) 저장 및 티켓 데이터 설정")
        void shouldProcessLowSignalData() {
            // Given
            ModelResultDto modelResultDto = createModelResultDto("TRUNK-001", 10.0f, 5.0f); // 차이: 5 >= 3
            List<ModelResultDto> inputList = List.of(modelResultDto);

            OpticalPerformanceDto optPerfDto = createOpticalPerformanceDto("TRUNK-001", 10.0f, 5.0f);
            RoadmCheckOpticalPerformanceEntity entity = new RoadmCheckOpticalPerformanceEntity();

            given(modelMapper.map(any(ModelResultDto.class), eq(OpticalPerformanceDto.class)))
                    .willReturn(optPerfDto);
            given(optPerfStructMapper.toCheckOptPerfEntity(any(OpticalPerformanceDto.class)))
                    .willReturn(entity);

            // When
            lineMonitoringHdlService.lineMonitoringHdlProcessor(inputList);

            // Then
            verify(checkOptPerfRepo).saveAll(entityListCaptor.capture());
            verify(ticketDataService).setTicketData(dtoListCaptor.capture());

            assertThat(entityListCaptor.getValue()).hasSize(1);
            assertThat(dtoListCaptor.getValue()).hasSize(1);
        }

        @Test
        @DisplayName("신호 저하가 없는 경우(valueMax - valueMin < 3) 저장하지 않음")
        void shouldNotProcessNormalSignalData() {
            // Given
            ModelResultDto modelResultDto = createModelResultDto("TRUNK-001", 5.0f, 4.0f); // 차이: 1 < 3
            List<ModelResultDto> inputList = List.of(modelResultDto);

            OpticalPerformanceDto optPerfDto = createOpticalPerformanceDto("TRUNK-001", 5.0f, 4.0f);

            given(modelMapper.map(any(ModelResultDto.class), eq(OpticalPerformanceDto.class)))
                    .willReturn(optPerfDto);

            // When
            lineMonitoringHdlService.lineMonitoringHdlProcessor(inputList);

            // Then
            verify(checkOptPerfRepo, never()).saveAll(anyList());
            verify(ticketDataService, never()).setTicketData(anyList());
        }

        @Test
        @DisplayName("경계값 테스트: 정확히 3dB 차이일 때 저장")
        void shouldProcessExactlyThresholdValue() {
            // Given
            ModelResultDto modelResultDto = createModelResultDto("TRUNK-001", 8.0f, 5.0f); // 차이: 3 == 3
            List<ModelResultDto> inputList = List.of(modelResultDto);

            OpticalPerformanceDto optPerfDto = createOpticalPerformanceDto("TRUNK-001", 8.0f, 5.0f);
            RoadmCheckOpticalPerformanceEntity entity = new RoadmCheckOpticalPerformanceEntity();

            given(modelMapper.map(any(ModelResultDto.class), eq(OpticalPerformanceDto.class)))
                    .willReturn(optPerfDto);
            given(optPerfStructMapper.toCheckOptPerfEntity(any(OpticalPerformanceDto.class)))
                    .willReturn(entity);

            // When
            lineMonitoringHdlService.lineMonitoringHdlProcessor(inputList);

            // Then
            verify(checkOptPerfRepo).saveAll(anyList());
            verify(ticketDataService).setTicketData(anyList());
        }

        @Test
        @DisplayName("경계값 테스트: 3dB 미만 차이일 때 저장하지 않음")
        void shouldNotProcessJustBelowThreshold() {
            // Given
            ModelResultDto modelResultDto = createModelResultDto("TRUNK-001", 7.99f, 5.0f); // 차이: 2.99 < 3
            List<ModelResultDto> inputList = List.of(modelResultDto);

            OpticalPerformanceDto optPerfDto = createOpticalPerformanceDto("TRUNK-001", 7.99f, 5.0f);

            given(modelMapper.map(any(ModelResultDto.class), eq(OpticalPerformanceDto.class)))
                    .willReturn(optPerfDto);

            // When
            lineMonitoringHdlService.lineMonitoringHdlProcessor(inputList);

            // Then
            verify(checkOptPerfRepo, never()).saveAll(anyList());
            verify(ticketDataService, never()).setTicketData(anyList());
        }

        @Test
        @DisplayName("여러 데이터 중 일부만 신호 저하인 경우 해당 데이터만 처리")
        void shouldProcessOnlyLowSignalDataFromMixedList() {
            // Given
            ModelResultDto lowSignal = createModelResultDto("TRUNK-001", 10.0f, 5.0f); // 저하
            ModelResultDto normalSignal = createModelResultDto("TRUNK-002", 5.0f, 4.0f); // 정상

            List<ModelResultDto> inputList = List.of(lowSignal, normalSignal);

            OpticalPerformanceDto lowOptPerfDto = createOpticalPerformanceDto("TRUNK-001", 10.0f, 5.0f);
            OpticalPerformanceDto normalOptPerfDto = createOpticalPerformanceDto("TRUNK-002", 5.0f, 4.0f);
            RoadmCheckOpticalPerformanceEntity entity = new RoadmCheckOpticalPerformanceEntity();

            given(modelMapper.map(eq(lowSignal), eq(OpticalPerformanceDto.class)))
                    .willReturn(lowOptPerfDto);
            given(modelMapper.map(eq(normalSignal), eq(OpticalPerformanceDto.class)))
                    .willReturn(normalOptPerfDto);
            given(optPerfStructMapper.toCheckOptPerfEntity(any(OpticalPerformanceDto.class)))
                    .willReturn(entity);

            // When
            lineMonitoringHdlService.lineMonitoringHdlProcessor(inputList);

            // Then
            verify(checkOptPerfRepo).saveAll(entityListCaptor.capture());
            assertThat(entityListCaptor.getValue()).hasSize(1);

            verify(ticketDataService).setTicketData(dtoListCaptor.capture());
            assertThat(dtoListCaptor.getValue()).hasSize(1);
        }

        @Test
        @DisplayName("여러 신호 저하 데이터 모두 처리")
        void shouldProcessAllLowSignalData() {
            // Given
            List<ModelResultDto> inputList = new ArrayList<>();
            List<OpticalPerformanceDto> expectedDtoList = new ArrayList<>();

            for (int i = 0; i < 5; i++) {
                ModelResultDto dto = createModelResultDto("TRUNK-" + i, 10.0f + i, 5.0f);
                inputList.add(dto);

                OpticalPerformanceDto optPerfDto = createOpticalPerformanceDto("TRUNK-" + i, 10.0f + i, 5.0f);
                expectedDtoList.add(optPerfDto);

                given(modelMapper.map(eq(dto), eq(OpticalPerformanceDto.class)))
                        .willReturn(optPerfDto);
            }

            given(optPerfStructMapper.toCheckOptPerfEntity(any(OpticalPerformanceDto.class)))
                    .willReturn(new RoadmCheckOpticalPerformanceEntity());

            // When
            lineMonitoringHdlService.lineMonitoringHdlProcessor(inputList);

            // Then
            verify(checkOptPerfRepo).saveAll(entityListCaptor.capture());
            assertThat(entityListCaptor.getValue()).hasSize(5);

            verify(ticketDataService).setTicketData(dtoListCaptor.capture());
            assertThat(dtoListCaptor.getValue()).hasSize(5);
        }
    }
}
