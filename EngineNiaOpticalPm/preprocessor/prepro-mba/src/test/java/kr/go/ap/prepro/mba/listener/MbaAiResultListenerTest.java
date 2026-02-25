package kr.go.ap.prepro.mba.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.go.ap.core.primary.nia.dto.linkage.model.ModelResultDto;
import kr.go.ap.prepro.mba.service.LineMonitoringHdlService;
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
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;

import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("MbaAiResultListener 단위 테스트")
class MbaAiResultListenerTest {

    @Mock
    private LineMonitoringHdlService lineMonitoringHdlService;

    @InjectMocks
    private MbaAiResultListener mbaAiResultListener;

    @Captor
    private ArgumentCaptor<List<ModelResultDto>> modelResultListCaptor;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    private Message createMessage(String jsonContent) {
        MessageProperties properties = new MessageProperties();
        properties.setContentType("application/json");
        return new Message(jsonContent.getBytes(StandardCharsets.UTF_8), properties);
    }

    @Nested
    @DisplayName("onMessage 메서드")
    class OnMessageTest {

        @Test
        @DisplayName("유효한 JSON 메시지를 파싱하여 서비스에 전달한다")
        void shouldParseValidJsonAndCallService() throws JsonProcessingException {
            // Given
            ModelResultDto dto = ModelResultDto.builder()
                    .trunkId("TRUNK-001")
                    .tid("TID-001")
                    .sysname("SYS-001")
                    .ptpname("PTP-001")
                    .valueMax(10.0f)
                    .valueMin(5.0f)
                    .ocrtime(new Timestamp(System.currentTimeMillis()))
                    .direction("UP")
                    .routeNum(1)
                    .build();

            String json = objectMapper.writeValueAsString(List.of(dto));
            Message message = createMessage(json);

            // When
            mbaAiResultListener.onMessage(message);

            // Then
            verify(lineMonitoringHdlService).lineMonitoringHdlProcessor(modelResultListCaptor.capture());
            List<ModelResultDto> capturedList = modelResultListCaptor.getValue();

            assertThat(capturedList).hasSize(1);
            assertThat(capturedList.get(0).getTrunkId()).isEqualTo("TRUNK-001");
        }

        @Test
        @DisplayName("여러 개의 ModelResultDto를 포함한 메시지를 처리한다")
        void shouldProcessMultipleModelResults() throws JsonProcessingException {
            // Given
            List<ModelResultDto> dtoList = List.of(
                    ModelResultDto.builder()
                            .trunkId("TRUNK-001")
                            .tid("TID-001")
                            .valueMax(10.0f)
                            .valueMin(5.0f)
                            .build(),
                    ModelResultDto.builder()
                            .trunkId("TRUNK-002")
                            .tid("TID-002")
                            .valueMax(8.0f)
                            .valueMin(6.0f)
                            .build()
            );

            String json = objectMapper.writeValueAsString(dtoList);
            Message message = createMessage(json);

            // When
            mbaAiResultListener.onMessage(message);

            // Then
            verify(lineMonitoringHdlService).lineMonitoringHdlProcessor(modelResultListCaptor.capture());
            List<ModelResultDto> capturedList = modelResultListCaptor.getValue();

            assertThat(capturedList).hasSize(2);
        }

        @Test
        @DisplayName("빈 배열 메시지를 처리한다")
        void shouldProcessEmptyArrayMessage() {
            // Given
            String json = "[]";
            Message message = createMessage(json);

            // When
            mbaAiResultListener.onMessage(message);

            // Then
            verify(lineMonitoringHdlService).lineMonitoringHdlProcessor(modelResultListCaptor.capture());
            assertThat(modelResultListCaptor.getValue()).isEmpty();
        }

        @Test
        @DisplayName("잘못된 JSON 형식의 메시지는 예외를 처리하고 서비스를 호출하지 않는다")
        void shouldHandleInvalidJsonGracefully() {
            // Given
            String invalidJson = "{ invalid json }";
            Message message = createMessage(invalidJson);

            // When & Then
            assertThatCode(() -> mbaAiResultListener.onMessage(message))
                    .doesNotThrowAnyException();

            verify(lineMonitoringHdlService, never()).lineMonitoringHdlProcessor(anyList());
        }

        @Test
        @DisplayName("알 수 없는 필드가 있는 JSON도 정상적으로 파싱한다")
        void shouldIgnoreUnknownFieldsInJson() {
            // Given
            String jsonWithUnknownField = "[{\"trunk_id\":\"TRUNK-001\",\"unknown_field\":\"value\"}]";
            Message message = createMessage(jsonWithUnknownField);

            // When
            mbaAiResultListener.onMessage(message);

            // Then
            verify(lineMonitoringHdlService).lineMonitoringHdlProcessor(modelResultListCaptor.capture());
            List<ModelResultDto> capturedList = modelResultListCaptor.getValue();

            assertThat(capturedList).hasSize(1);
            assertThat(capturedList.get(0).getTrunkId()).isEqualTo("TRUNK-001");
        }

        @Test
        @DisplayName("UTF-8 인코딩된 메시지를 올바르게 처리한다")
        void shouldHandleUtf8EncodedMessage() throws JsonProcessingException {
            // Given
            ModelResultDto dto = ModelResultDto.builder()
                    .trunkId("한글트렁크")
                    .tid("TID-한글")
                    .build();

            String json = objectMapper.writeValueAsString(List.of(dto));
            Message message = createMessage(json);

            // When
            mbaAiResultListener.onMessage(message);

            // Then
            verify(lineMonitoringHdlService).lineMonitoringHdlProcessor(modelResultListCaptor.capture());
            List<ModelResultDto> capturedList = modelResultListCaptor.getValue();

            assertThat(capturedList.get(0).getTrunkId()).isEqualTo("한글트렁크");
        }

        @Test
        @DisplayName("서비스에서 예외가 발생해도 리스너가 종료되지 않는다")
        void shouldNotCrashWhenServiceThrowsException() throws JsonProcessingException {
            // Given
            ModelResultDto dto = ModelResultDto.builder()
                    .trunkId("TRUNK-001")
                    .build();

            String json = objectMapper.writeValueAsString(List.of(dto));
            Message message = createMessage(json);

            doThrow(new NullPointerException("Service Error"))
                    .when(lineMonitoringHdlService).lineMonitoringHdlProcessor(anyList());

            // When & Then
            assertThatCode(() -> mbaAiResultListener.onMessage(message))
                    .doesNotThrowAnyException();
        }
    }
}
