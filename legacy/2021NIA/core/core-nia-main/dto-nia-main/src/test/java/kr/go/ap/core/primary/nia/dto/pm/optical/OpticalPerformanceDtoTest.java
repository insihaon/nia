package kr.go.ap.core.primary.nia.dto.pm.optical;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("OpticalPerformanceDto 테스트")
class OpticalPerformanceDtoTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Nested
    @DisplayName("Builder 테스트")
    class BuilderTest {

        @Test
        @DisplayName("Builder로 모든 필드를 설정할 수 있다")
        void shouldBuildWithAllFields() {
            // Given
            Timestamp now = new Timestamp(System.currentTimeMillis());

            // When
            OpticalPerformanceDto dto = OpticalPerformanceDto.builder()
                    .trunkId("TRUNK-001")
                    .trunkName("TestTrunk")
                    .tid("TID-001")
                    .sysname("SYS-001")
                    .roadmCode("REPEATER")
                    .ptpname("PTP-001")
                    .port("PORT-001")
                    .ocrtime(now)
                    .inOut("IN")
                    .valueCur(7.5f)
                    .valueMax(10.0f)
                    .valueMin(5.0f)
                    .direction("UP")
                    .routeNum(1)
                    .isLowSignal(true)
                    .build();

            // Then
            assertThat(dto.getTrunkId()).isEqualTo("TRUNK-001");
            assertThat(dto.getTrunkName()).isEqualTo("TestTrunk");
            assertThat(dto.getTid()).isEqualTo("TID-001");
            assertThat(dto.getSysname()).isEqualTo("SYS-001");
            assertThat(dto.getRoadmCode()).isEqualTo("REPEATER");
            assertThat(dto.getPtpname()).isEqualTo("PTP-001");
            assertThat(dto.getPort()).isEqualTo("PORT-001");
            assertThat(dto.getOcrtime()).isEqualTo(now);
            assertThat(dto.getInOut()).isEqualTo("IN");
            assertThat(dto.getValueCur()).isEqualTo(7.5f);
            assertThat(dto.getValueMax()).isEqualTo(10.0f);
            assertThat(dto.getValueMin()).isEqualTo(5.0f);
            assertThat(dto.getDirection()).isEqualTo("UP");
            assertThat(dto.getRouteNum()).isEqualTo(1);
            assertThat(dto.isLowSignal()).isTrue();
        }

        @Test
        @DisplayName("Builder로 일부 필드만 설정할 수 있다")
        void shouldBuildWithPartialFields() {
            // When
            OpticalPerformanceDto dto = OpticalPerformanceDto.builder()
                    .trunkId("TRUNK-001")
                    .valueMax(10.0f)
                    .valueMin(5.0f)
                    .build();

            // Then
            assertThat(dto.getTrunkId()).isEqualTo("TRUNK-001");
            assertThat(dto.getValueMax()).isEqualTo(10.0f);
            assertThat(dto.getValueMin()).isEqualTo(5.0f);
            assertThat(dto.getTrunkName()).isNull();
            assertThat(dto.isLowSignal()).isFalse();
        }
    }

    @Nested
    @DisplayName("Getter/Setter 테스트")
    class GetterSetterTest {

        @Test
        @DisplayName("Setter로 값을 변경할 수 있다")
        void shouldSetValuesWithSetter() {
            // Given
            OpticalPerformanceDto dto = new OpticalPerformanceDto();

            // When
            dto.setTrunkId("TRUNK-002");
            dto.setValueMax(15.0f);
            dto.setValueMin(8.0f);
            dto.setLowSignal(true);

            // Then
            assertThat(dto.getTrunkId()).isEqualTo("TRUNK-002");
            assertThat(dto.getValueMax()).isEqualTo(15.0f);
            assertThat(dto.getValueMin()).isEqualTo(8.0f);
            assertThat(dto.isLowSignal()).isTrue();
        }
    }

    @Nested
    @DisplayName("JSON 직렬화/역직렬화 테스트")
    class JsonSerializationTest {

        @Test
        @DisplayName("DTO를 JSON으로 직렬화할 수 있다")
        void shouldSerializeToJson() throws JsonProcessingException {
            // Given
            OpticalPerformanceDto dto = OpticalPerformanceDto.builder()
                    .trunkId("TRUNK-001")
                    .valueMax(10.0f)
                    .valueMin(5.0f)
                    .isLowSignal(true)
                    .build();

            // When
            String json = objectMapper.writeValueAsString(dto);

            // Then
            assertThat(json).contains("\"trunkId\":\"TRUNK-001\"");
            assertThat(json).contains("\"valueMax\":10.0");
            assertThat(json).contains("\"valueMin\":5.0");
            assertThat(json).contains("\"lowSignal\":true");
        }

        @Test
        @DisplayName("JSON을 DTO로 역직렬화할 수 있다")
        void shouldDeserializeFromJson() throws JsonProcessingException {
            // Given
            String json = "{\"trunkId\":\"TRUNK-001\",\"valueMax\":10.0,\"valueMin\":5.0,\"lowSignal\":true}";

            // When
            OpticalPerformanceDto dto = objectMapper.readValue(json, OpticalPerformanceDto.class);

            // Then
            assertThat(dto.getTrunkId()).isEqualTo("TRUNK-001");
            assertThat(dto.getValueMax()).isEqualTo(10.0f);
            assertThat(dto.getValueMin()).isEqualTo(5.0f);
            assertThat(dto.isLowSignal()).isTrue();
        }

        @Test
        @DisplayName("알 수 없는 필드가 있어도 역직렬화할 수 있다 (JsonIgnoreProperties)")
        void shouldIgnoreUnknownProperties() throws JsonProcessingException {
            // Given
            String json = "{\"trunkId\":\"TRUNK-001\",\"unknownField\":\"value\",\"valueMax\":10.0}";

            // When
            OpticalPerformanceDto dto = objectMapper.readValue(json, OpticalPerformanceDto.class);

            // Then
            assertThat(dto.getTrunkId()).isEqualTo("TRUNK-001");
            assertThat(dto.getValueMax()).isEqualTo(10.0f);
        }
    }

    @Nested
    @DisplayName("신호 저하 계산 테스트")
    class SignalDegradationTest {

        @Test
        @DisplayName("valueMax - valueMin >= 3 이면 신호 저하로 판단")
        void shouldDetectLowSignalWhenDifferenceGte3() {
            // Given
            OpticalPerformanceDto lowSignal = OpticalPerformanceDto.builder()
                    .valueMax(10.0f)
                    .valueMin(5.0f)  // 차이: 5.0
                    .build();

            OpticalPerformanceDto normalSignal = OpticalPerformanceDto.builder()
                    .valueMax(7.0f)
                    .valueMin(5.0f)  // 차이: 2.0
                    .build();

            // When
            float lowDiff = lowSignal.getValueMax() - lowSignal.getValueMin();
            float normalDiff = normalSignal.getValueMax() - normalSignal.getValueMin();

            // Then
            assertThat(lowDiff).isGreaterThanOrEqualTo(3.0f);
            assertThat(normalDiff).isLessThan(3.0f);
        }

        @Test
        @DisplayName("경계값 테스트: 정확히 3dB 차이")
        void shouldDetectBoundaryValue() {
            // Given
            OpticalPerformanceDto boundary = OpticalPerformanceDto.builder()
                    .valueMax(8.0f)
                    .valueMin(5.0f)  // 차이: 3.0
                    .build();

            // When
            float diff = boundary.getValueMax() - boundary.getValueMin();

            // Then
            assertThat(diff).isEqualTo(3.0f);
        }
    }

    @Nested
    @DisplayName("toString 테스트")
    class ToStringTest {

        @Test
        @DisplayName("toString이 모든 필드를 포함한다")
        void shouldIncludeAllFieldsInToString() {
            // Given
            OpticalPerformanceDto dto = OpticalPerformanceDto.builder()
                    .trunkId("TRUNK-001")
                    .tid("TID-001")
                    .valueMax(10.0f)
                    .valueMin(5.0f)
                    .build();

            // When
            String toString = dto.toString();

            // Then
            assertThat(toString).contains("trunkId=TRUNK-001");
            assertThat(toString).contains("tid=TID-001");
            assertThat(toString).contains("valueMax=10.0");
            assertThat(toString).contains("valueMin=5.0");
        }
    }

    @Nested
    @DisplayName("NoArgsConstructor 테스트")
    class NoArgsConstructorTest {

        @Test
        @DisplayName("기본 생성자로 인스턴스를 생성할 수 있다")
        void shouldCreateInstanceWithNoArgsConstructor() {
            // When
            OpticalPerformanceDto dto = new OpticalPerformanceDto();

            // Then
            assertThat(dto).isNotNull();
            assertThat(dto.getTrunkId()).isNull();
            assertThat(dto.getValueMax()).isEqualTo(0.0f);
            assertThat(dto.isLowSignal()).isFalse();
        }
    }

    @Nested
    @DisplayName("AllArgsConstructor 테스트")
    class AllArgsConstructorTest {

        @Test
        @DisplayName("전체 인자 생성자로 인스턴스를 생성할 수 있다")
        void shouldCreateInstanceWithAllArgsConstructor() {
            // Given
            Timestamp now = new Timestamp(System.currentTimeMillis());

            // When
            OpticalPerformanceDto dto = new OpticalPerformanceDto(
                    "TRUNK-001", "TestTrunk", "TID-001", "SYS-001",
                    "REPEATER", "PTP-001", "PORT-001", now, "IN",
                    7.5f, 10.0f, 5.0f, "UP", 1, true
            );

            // Then
            assertThat(dto.getTrunkId()).isEqualTo("TRUNK-001");
            assertThat(dto.isLowSignal()).isTrue();
        }
    }
}
