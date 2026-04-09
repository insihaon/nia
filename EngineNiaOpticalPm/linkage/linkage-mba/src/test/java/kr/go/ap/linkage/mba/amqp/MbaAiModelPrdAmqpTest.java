package kr.go.ap.linkage.mba.amqp;

import kr.go.ap.core.primary.nia.dto.linkage.model.ModelSendDto;
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
@DisplayName("MbaAiModelPrdAmqp AMQP Producer 테스트")
class MbaAiModelPrdAmqpTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private MbaAiModelPrdAmqp mbaAiModelPrdAmqp;

    @Captor
    private ArgumentCaptor<ModelSendDto> modelSendDtoCaptor;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(mbaAiModelPrdAmqp, "rabbitTemplate", rabbitTemplate);
    }

    @Nested
    @DisplayName("sendMessageCmd 메서드")
    class SendMessageCmdTest {

        @Test
        @DisplayName("ModelSendDto를 RabbitMQ로 전송한다")
        void shouldSendModelSendDtoToRabbitMq() {
            // Given
            ModelSendDto dto = ModelSendDto.builder()
                    .ocrTime("2024-01-15 10:30:00")
                    .build();

            // When
            mbaAiModelPrdAmqp.sendMessageCmd(dto);

            // Then
            verify(rabbitTemplate).convertAndSend(modelSendDtoCaptor.capture());
            ModelSendDto capturedDto = modelSendDtoCaptor.getValue();

            assertThat(capturedDto.getOcrTime()).isEqualTo("2024-01-15 10:30:00");
        }

        @Test
        @DisplayName("null DTO를 전송해도 예외가 발생하지 않는다")
        void shouldHandleNullDtoGracefully() {
            // Given
            ModelSendDto nullDto = null;

            // When & Then
            assertThatCode(() -> mbaAiModelPrdAmqp.sendMessageCmd(nullDto))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("RabbitTemplate에서 예외가 발생해도 안전하게 처리된다")
        void shouldHandleRabbitTemplateException() {
            // Given
            ModelSendDto dto = ModelSendDto.builder()
                    .ocrTime("2024-01-15 10:30:00")
                    .build();

            doThrow(new RuntimeException("RabbitMQ Connection Error"))
                    .when(rabbitTemplate).convertAndSend(any(ModelSendDto.class));

            // When & Then
            assertThatCode(() -> mbaAiModelPrdAmqp.sendMessageCmd(dto))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("빈 ocrTime이 있는 DTO도 전송된다")
        void shouldSendDtoWithEmptyOcrTime() {
            // Given
            ModelSendDto dto = ModelSendDto.builder()
                    .ocrTime("")
                    .build();

            // When
            mbaAiModelPrdAmqp.sendMessageCmd(dto);

            // Then
            verify(rabbitTemplate).convertAndSend(modelSendDtoCaptor.capture());
            assertThat(modelSendDtoCaptor.getValue().getOcrTime()).isEmpty();
        }
    }
}
