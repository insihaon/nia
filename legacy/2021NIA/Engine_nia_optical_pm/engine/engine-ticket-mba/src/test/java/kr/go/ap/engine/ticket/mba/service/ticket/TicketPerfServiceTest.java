package kr.go.ap.engine.ticket.mba.service.ticket;

import kr.go.ap.core.primary.nia.entity.mba.performance.RoadmPerformanceHistEntity;
import kr.go.ap.core.primary.nia.entity.mba.performance.key.RoadmPerformanceHistKey;
import kr.go.ap.core.primary.nia.entity.mba.ticket.RoadmLowOpticalPerformanceEntity;
import kr.go.ap.core.primary.nia.entity.mba.ticket.TicketPerformanceDataCheckEntity;
import kr.go.ap.core.repository.primary.jpa.mba.performance.RoadmPerformanceHistRepository;
import kr.go.ap.core.repository.primary.jpa.mba.ticket.RoadmLowOpticalPerformanceRepository;
import kr.go.ap.core.repository.primary.jpa.mba.ticket.TicketPerformanceDataCheckRepository;
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
import org.springframework.data.domain.Sort;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("TicketPerfService 단위 테스트")
class TicketPerfServiceTest {

    @Mock
    private RoadmLowOpticalPerformanceRepository lowOptPerfRepository;

    @Mock
    private RoadmPerformanceHistRepository perfHistRepository;

    @Mock
    private TicketPerformanceDataCheckRepository ticketPerfDataCheckRepository;

    @InjectMocks
    private TicketPerfService ticketPerfService;

    @Captor
    private ArgumentCaptor<RoadmLowOpticalPerformanceEntity> lowOptPerfCaptor;

    private TicketPerformanceDataCheckEntity createTicketPerfDataCheckEntity(String ticketId) {
        return TicketPerformanceDataCheckEntity.builder()
                .ticketId(ticketId)
                .ocrTime(Timestamp.valueOf(LocalDateTime.of(2024, 1, 15, 10, 30, 0)))
                .trunkId("TRUNK-001")
                .direction("UP")
                .build();
    }

    private RoadmPerformanceHistEntity createPerfHistEntity(String tid) {
        RoadmPerformanceHistKey key = RoadmPerformanceHistKey.builder()
                .trunkId("TRUNK-001")
                .tid(tid)
                .ptpname("PTP-1")
                .port("PORT-1")
                .ocrtime(Timestamp.valueOf(LocalDateTime.of(2024, 1, 15, 10, 45, 0)))
                .inOut("IN")
                .build();

        return RoadmPerformanceHistEntity.builder()
                .roadmPerformanceHistKey(key)
                .trunkName("TestTrunk")
                .sysname("SYS-001")
                .roadmCode("TERMINAL")
                .valueCur(7.5f)
                .valueMax(10.0f)
                .valueMin(5.0f)
                .direction("UP")
                .routenum(1)
                .build();
    }

    @Nested
    @DisplayName("addPerfData 메서드")
    class AddPerfDataTest {

        @Test
        @DisplayName("티켓 데이터가 없으면 아무 처리도 하지 않는다")
        void shouldDoNothingWhenNoTicketData() {
            // Given
            given(ticketPerfDataCheckRepository.findAll(any(Sort.class)))
                    .willReturn(Collections.emptyList());

            // When
            ticketPerfService.addPerfData();

            // Then
            verify(lowOptPerfRepository, never()).save(any());
            verify(ticketPerfDataCheckRepository, never()).deleteByTicketId(anyString());
        }

        @Test
        @DisplayName("티켓 데이터가 있고 히스토리 데이터가 있으면 처리한다")
        void shouldProcessWhenTicketAndHistoryDataExist() {
            // Given
            TicketPerformanceDataCheckEntity ticketEntity = createTicketPerfDataCheckEntity("TICKET-001");
            List<TicketPerformanceDataCheckEntity> ticketList = List.of(ticketEntity);

            given(ticketPerfDataCheckRepository.findAll(any(Sort.class)))
                    .willReturn(ticketList);

            RoadmPerformanceHistEntity histEntity = createPerfHistEntity("TID-001");
            given(perfHistRepository.findHistByTrunkIdAndDirectionAndOcrTime(
                    eq("TRUNK-001"),
                    eq("UP"),
                    any(Timestamp.class)))
                    .willReturn(Optional.of(List.of(histEntity)));

            // When
            ticketPerfService.addPerfData();

            // Then
            verify(lowOptPerfRepository, atLeastOnce()).save(any(RoadmLowOpticalPerformanceEntity.class));
            verify(ticketPerfDataCheckRepository).deleteByTicketId("TICKET-001");
        }

        @Test
        @DisplayName("히스토리 데이터가 비어있으면 저장하지 않는다")
        void shouldNotSaveWhenNoHistoryData() {
            // Given
            TicketPerformanceDataCheckEntity ticketEntity = createTicketPerfDataCheckEntity("TICKET-001");
            List<TicketPerformanceDataCheckEntity> ticketList = List.of(ticketEntity);

            given(ticketPerfDataCheckRepository.findAll(any(Sort.class)))
                    .willReturn(ticketList);

            // Optional.of(빈 리스트) 반환 - orElseThrow()는 통과하지만 리스트가 비어있음
            given(perfHistRepository.findHistByTrunkIdAndDirectionAndOcrTime(
                    anyString(),
                    anyString(),
                    any(Timestamp.class)))
                    .willReturn(Optional.of(Collections.emptyList()));

            // When
            ticketPerfService.addPerfData();

            // Then
            verify(lowOptPerfRepository, never()).save(any());
            verify(ticketPerfDataCheckRepository, never()).deleteByTicketId(anyString());
        }

        @Test
        @DisplayName("여러 티켓 데이터를 순차적으로 처리한다")
        void shouldProcessMultipleTicketsSequentially() {
            // Given
            TicketPerformanceDataCheckEntity ticket1 = createTicketPerfDataCheckEntity("TICKET-001");
            TicketPerformanceDataCheckEntity ticket2 = createTicketPerfDataCheckEntity("TICKET-002");
            List<TicketPerformanceDataCheckEntity> ticketList = List.of(ticket1, ticket2);

            given(ticketPerfDataCheckRepository.findAll(any(Sort.class)))
                    .willReturn(ticketList);

            RoadmPerformanceHistEntity histEntity = createPerfHistEntity("TID-001");
            given(perfHistRepository.findHistByTrunkIdAndDirectionAndOcrTime(
                    anyString(),
                    anyString(),
                    any(Timestamp.class)))
                    .willReturn(Optional.of(List.of(histEntity)));

            // When
            ticketPerfService.addPerfData();

            // Then
            verify(ticketPerfDataCheckRepository).deleteByTicketId("TICKET-001");
            verify(ticketPerfDataCheckRepository).deleteByTicketId("TICKET-002");
        }

        @Test
        @DisplayName("NullPointerException 발생 시 안전하게 처리된다")
        void shouldHandleNullPointerExceptionSafely() {
            // Given - NullPointerException은 서비스에서 catch됨
            given(ticketPerfDataCheckRepository.findAll(any(Sort.class)))
                    .willThrow(new NullPointerException("Null value"));

            // When & Then
            assertThatCode(() -> ticketPerfService.addPerfData())
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("RuntimeException 발생 시 예외가 전파된다")
        void shouldPropagateRuntimeException() {
            // Given - RuntimeException은 서비스에서 catch되지 않음
            given(ticketPerfDataCheckRepository.findAll(any(Sort.class)))
                    .willThrow(new RuntimeException("DB Error"));

            // When & Then
            assertThatCode(() -> ticketPerfService.addPerfData())
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("DB Error");
        }

        @Test
        @DisplayName("15분 후 시간으로 히스토리 데이터를 조회한다")
        void shouldQueryHistoryDataWithPlus15Minutes() {
            // Given
            LocalDateTime baseTime = LocalDateTime.of(2024, 1, 15, 10, 30, 0);
            TicketPerformanceDataCheckEntity ticketEntity = TicketPerformanceDataCheckEntity.builder()
                    .ticketId("TICKET-001")
                    .ocrTime(Timestamp.valueOf(baseTime))
                    .trunkId("TRUNK-001")
                    .direction("UP")
                    .build();

            given(ticketPerfDataCheckRepository.findAll(any(Sort.class)))
                    .willReturn(List.of(ticketEntity));

            ArgumentCaptor<Timestamp> timestampCaptor = ArgumentCaptor.forClass(Timestamp.class);
            given(perfHistRepository.findHistByTrunkIdAndDirectionAndOcrTime(
                    eq("TRUNK-001"),
                    eq("UP"),
                    timestampCaptor.capture()))
                    .willReturn(Optional.of(Collections.emptyList()));

            // When
            ticketPerfService.addPerfData();

            // Then
            Timestamp capturedTime = timestampCaptor.getValue();
            LocalDateTime expectedTime = baseTime.plusMinutes(15);
            assertThat(capturedTime.toLocalDateTime()).isEqualTo(expectedTime);
        }
    }
}
