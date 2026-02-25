package kr.go.ap.engine.ticket.mba.amqp;

import kr.go.ap.core.primary.nia.dto.rca.engine.ui.RcaEngineResultDto;
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
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("EngineToUiTicketPrdAmqp 테스트")
class EngineToUiTicketPrdAmqpTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private EngineToUiTicketPrdAmqp engineToUiTicketPrdAmqp;

    @Captor
    private ArgumentCaptor<RcaEngineResultDto> resultCaptor;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(engineToUiTicketPrdAmqp, "rabbitTemplate", rabbitTemplate);
    }

    @Nested
    @DisplayName("sendMessageCmd 메서드")
    class SendMessageCmdTest {

        @Test
        @DisplayName("RcaEngineResultDto를 RabbitMQ로 전송한다")
        void shouldSendRcaEngineResultDtoToRabbitMq() {
            // Given
            RcaEngineResultDto dto = RcaEngineResultDto.builder()
                    .ticketId("TICKET-001")
                    .eventType("TICKET_NEW")
                    .ticketType("PF")
                    .build();

            // When
            engineToUiTicketPrdAmqp.sendMessageCmd(dto);

            // Then
            verify(rabbitTemplate).convertAndSend(resultCaptor.capture());
            RcaEngineResultDto captured = resultCaptor.getValue();

            assertThat(captured.getTicketId()).isEqualTo("TICKET-001");
            assertThat(captured.getEventType()).isEqualTo("TICKET_NEW");
            assertThat(captured.getTicketType()).isEqualTo("PF");
        }

        @Test
        @DisplayName("TICKET_UPDATE 이벤트 타입을 전송할 수 있다")
        void shouldSendTicketUpdateEventType() {
            // Given
            RcaEngineResultDto dto = RcaEngineResultDto.builder()
                    .ticketId("TICKET-002")
                    .eventType("TICKET_UPDATE")
                    .ticketType("PF")
                    .build();

            // When
            engineToUiTicketPrdAmqp.sendMessageCmd(dto);

            // Then
            verify(rabbitTemplate).convertAndSend(resultCaptor.capture());
            assertThat(resultCaptor.getValue().getEventType()).isEqualTo("TICKET_UPDATE");
        }

        @Test
        @DisplayName("RabbitTemplate 예외 시 안전하게 처리된다")
        void shouldHandleRabbitTemplateException() {
            // Given
            RcaEngineResultDto dto = RcaEngineResultDto.builder()
                    .ticketId("TICKET-003")
                    .build();

            doThrow(new RuntimeException("RabbitMQ Connection Error"))
                    .when(rabbitTemplate).convertAndSend(any(RcaEngineResultDto.class));

            // When & Then
            assertThatCode(() -> engineToUiTicketPrdAmqp.sendMessageCmd(dto))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("여러 번 연속 호출이 가능하다")
        void shouldHandleMultipleConsecutiveCalls() {
            // Given
            RcaEngineResultDto dto1 = RcaEngineResultDto.builder()
                    .ticketId("TICKET-001")
                    .eventType("TICKET_NEW")
                    .build();

            RcaEngineResultDto dto2 = RcaEngineResultDto.builder()
                    .ticketId("TICKET-002")
                    .eventType("TICKET_NEW")
                    .build();

            // When
            engineToUiTicketPrdAmqp.sendMessageCmd(dto1);
            engineToUiTicketPrdAmqp.sendMessageCmd(dto2);

            // Then
            verify(rabbitTemplate, times(2)).convertAndSend(any(RcaEngineResultDto.class));
        }
    }
}
