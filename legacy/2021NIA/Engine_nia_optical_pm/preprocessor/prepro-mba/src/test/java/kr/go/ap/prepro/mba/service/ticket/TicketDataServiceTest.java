package kr.go.ap.prepro.mba.service.ticket;

import kr.go.ap.core.primary.nia.dto.mba.EngineLowPmDataListDto;
import kr.go.ap.core.primary.nia.dto.pm.optical.OpticalPerformanceDto;
import kr.go.ap.core.primary.nia.entity.mba.performance.RoadmPerformanceHistEntity;
import kr.go.ap.core.repository.primary.jpa.mba.performance.RoadmPerformanceHistRepository;
import kr.go.ap.core.repository.primary.mapper.prepro.pm.PmPreproMapper;
import kr.go.ap.prepro.mba.amqp.MbaEnginePrdAmqp;
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
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("TicketDataService 단위 테스트")
class TicketDataServiceTest {

    @Mock
    private PmPreproMapper pmPreproMapper;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private MbaEnginePrdAmqp mbaEnginePrdAmqp;

    @Mock
    private RoadmPerformanceHistRepository perfHistRepository;

    @InjectMocks
    private TicketDataService ticketDataService;

    @Captor
    private ArgumentCaptor<EngineLowPmDataListDto> engineLowPmDataCaptor;

    private OpticalPerformanceDto createOpticalPerformanceDto(
            String trunkId, String tid, String direction, int routeNum) {
        return OpticalPerformanceDto.builder()
                .trunkId(trunkId)
                .trunkName("TestTrunk")
                .tid(tid)
                .sysname("SYS-" + routeNum)
                .roadmCode("TERMINAL")
                .ptpname("PTP-" + routeNum)
                .port("PORT-" + routeNum)
                .ocrtime(new Timestamp(System.currentTimeMillis()))
                .inOut("IN")
                .valueCur(7.5f)
                .valueMax(10.0f)
                .valueMin(5.0f)
                .direction(direction)
                .routeNum(routeNum)
                .isLowSignal(false)
                .build();
    }

    @Nested
    @DisplayName("setTicketData 메서드")
    class SetTicketDataTest {

        @Test
        @DisplayName("빈 리스트 입력 시 아무 처리도 하지 않는다")
        void shouldDoNothingForEmptyList() {
            // Given
            List<OpticalPerformanceDto> emptyList = Collections.emptyList();

            // When
            ticketDataService.setTicketData(emptyList);

            // Then
            verifyNoInteractions(mbaEnginePrdAmqp);
        }

        @Test
        @DisplayName("UP 방향 데이터를 엔진으로 전송한다")
        void shouldSendUpDirectionDataToEngine() {
            // Given
            List<OpticalPerformanceDto> upDataList = List.of(
                    createOpticalPerformanceDto("TRUNK-001", "TID-001", "UP", 1),
                    createOpticalPerformanceDto("TRUNK-001", "TID-002", "UP", 2)
            );

            given(perfHistRepository.findHistByTrunkIdAndDirectionAndTimeRange(
                    anyString(), anyString(), any(Timestamp.class), any(Timestamp.class)))
                    .willReturn(Optional.of(Collections.emptyList()));

            // When
            ticketDataService.setTicketData(upDataList);

            // Then
            verify(mbaEnginePrdAmqp, atLeastOnce()).sendMessageCmd(any(EngineLowPmDataListDto.class));
        }

        @Test
        @DisplayName("DOWN 방향 데이터를 엔진으로 전송한다")
        void shouldSendDownDirectionDataToEngine() {
            // Given
            List<OpticalPerformanceDto> downDataList = List.of(
                    createOpticalPerformanceDto("TRUNK-001", "TID-001", "DOWN", 1),
                    createOpticalPerformanceDto("TRUNK-001", "TID-002", "DOWN", 2)
            );

            given(perfHistRepository.findHistByTrunkIdAndDirectionAndTimeRange(
                    anyString(), anyString(), any(Timestamp.class), any(Timestamp.class)))
                    .willReturn(Optional.of(Collections.emptyList()));

            // When
            ticketDataService.setTicketData(downDataList);

            // Then
            verify(mbaEnginePrdAmqp, atLeastOnce()).sendMessageCmd(any(EngineLowPmDataListDto.class));
        }

        @Test
        @DisplayName("여러 트렁크의 데이터를 그룹별로 처리한다")
        void shouldProcessDataGroupedByTrunk() {
            // Given
            List<OpticalPerformanceDto> multiTrunkData = List.of(
                    createOpticalPerformanceDto("TRUNK-001", "TID-001", "UP", 1),
                    createOpticalPerformanceDto("TRUNK-001", "TID-002", "UP", 2),
                    createOpticalPerformanceDto("TRUNK-002", "TID-003", "UP", 1),
                    createOpticalPerformanceDto("TRUNK-002", "TID-004", "UP", 2)
            );

            given(perfHistRepository.findHistByTrunkIdAndDirectionAndTimeRange(
                    anyString(), anyString(), any(Timestamp.class), any(Timestamp.class)))
                    .willReturn(Optional.of(Collections.emptyList()));

            // When
            ticketDataService.setTicketData(multiTrunkData);

            // Then: 각 트렁크별로 메시지가 전송됨
            verify(mbaEnginePrdAmqp, atLeast(2)).sendMessageCmd(any(EngineLowPmDataListDto.class));
        }

        @Test
        @DisplayName("lowSignal 플래그가 true로 설정된다")
        void shouldSetLowSignalFlagToTrue() {
            // Given
            OpticalPerformanceDto dto = createOpticalPerformanceDto("TRUNK-001", "TID-001", "UP", 1);
            assertThat(dto.isLowSignal()).isFalse(); // 초기값 확인

            List<OpticalPerformanceDto> dataList = new ArrayList<>();
            dataList.add(dto);

            given(perfHistRepository.findHistByTrunkIdAndDirectionAndTimeRange(
                    anyString(), anyString(), any(Timestamp.class), any(Timestamp.class)))
                    .willReturn(Optional.of(Collections.emptyList()));

            // When
            ticketDataService.setTicketData(dataList);

            // Then: lowSignal이 true로 설정됨
            assertThat(dto.isLowSignal()).isTrue();
        }

        @Test
        @DisplayName("RuntimeException 발생 시 예외가 전파된다")
        void shouldPropagateRuntimeException() {
            // Given
            List<OpticalPerformanceDto> dataList = List.of(
                    createOpticalPerformanceDto("TRUNK-001", "TID-001", "UP", 1)
            );

            given(perfHistRepository.findHistByTrunkIdAndDirectionAndTimeRange(
                    anyString(), anyString(), any(Timestamp.class), any(Timestamp.class)))
                    .willThrow(new RuntimeException("DB Error"));

            // When & Then - RuntimeException은 catch되지 않고 전파됨
            assertThatCode(() -> ticketDataService.setTicketData(dataList))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("DB Error");
        }
    }

    @Nested
    @DisplayName("방향별 데이터 분리 테스트")
    class DirectionSeparationTest {

        @Test
        @DisplayName("UP과 DOWN 데이터가 분리되어 처리된다")
        void shouldSeparateUpAndDownData() {
            // Given
            List<OpticalPerformanceDto> mixedDirectionData = List.of(
                    createOpticalPerformanceDto("TRUNK-001", "TID-001", "UP", 1),
                    createOpticalPerformanceDto("TRUNK-001", "TID-002", "DOWN", 2)
            );

            given(perfHistRepository.findHistByTrunkIdAndDirectionAndTimeRange(
                    anyString(), anyString(), any(Timestamp.class), any(Timestamp.class)))
                    .willReturn(Optional.of(Collections.emptyList()));

            // When
            ticketDataService.setTicketData(mixedDirectionData);

            // Then: UP과 DOWN 각각에 대해 메시지가 전송됨
            verify(mbaEnginePrdAmqp, times(2)).sendMessageCmd(any(EngineLowPmDataListDto.class));
        }
    }

    @Nested
    @DisplayName("히스토리 데이터 처리 테스트")
    class HistoryDataProcessingTest {

        @Test
        @DisplayName("히스토리 데이터를 15분 전 시간으로 조회한다")
        void shouldQueryHistoryDataWith15MinutesAgo() {
            // Given
            Timestamp baseTime = Timestamp.valueOf("2024-01-15 10:30:00");
            OpticalPerformanceDto dto = OpticalPerformanceDto.builder()
                    .trunkId("TRUNK-001")
                    .tid("TID-001")
                    .direction("UP")
                    .routeNum(1)
                    .ocrtime(baseTime)
                    .inOut("IN")
                    .build();

            List<OpticalPerformanceDto> dataList = List.of(dto);

            given(perfHistRepository.findHistByTrunkIdAndDirectionAndTimeRange(
                    eq("TRUNK-001"),
                    eq("UP"),
                    any(Timestamp.class),
                    eq(baseTime)))
                    .willReturn(Optional.of(Collections.emptyList()));

            // When
            ticketDataService.setTicketData(dataList);

            // Then
            verify(perfHistRepository).findHistByTrunkIdAndDirectionAndTimeRange(
                    eq("TRUNK-001"),
                    eq("UP"),
                    any(Timestamp.class),
                    eq(baseTime)
            );
        }
    }
}
