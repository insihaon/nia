package kr.go.ap.core.utility.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("UtlDateHelper 날짜/시간 유틸리티 테스트")
class UtlDateHelperTest {

    @Nested
    @DisplayName("getDate 메서드")
    class GetDateTest {

        @Test
        @DisplayName("기본 포맷은 Date를 Timestamp로 캐스팅 시 예외가 발생한다 (기존 코드 버그)")
        void shouldThrowClassCastExceptionDueToCodeBug() {
            // Given
            String dateString = "2024/01/15-10:30:45";

            // When & Then - 기존 코드의 버그: Date를 Timestamp로 캐스팅 불가
            assertThatThrownBy(() -> UtlDateHelper.getDate(dateString))
                    .isInstanceOf(ClassCastException.class);
        }

        @Test
        @DisplayName("사용자 지정 포맷으로 날짜 문자열을 Timestamp로 변환한다")
        void shouldConvertWithCustomFormat() throws Exception {
            // Given
            String dateString = "2024-01-15 10:30:45";
            String format = "yyyy-MM-dd HH:mm:ss";

            // When
            Timestamp result = UtlDateHelper.getDate(dateString, format);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.toLocalDateTime().getYear()).isEqualTo(2024);
            assertThat(result.toLocalDateTime().getMonthValue()).isEqualTo(1);
            assertThat(result.toLocalDateTime().getDayOfMonth()).isEqualTo(15);
        }
    }

    @Nested
    @DisplayName("stringToTimestamp 메서드")
    class StringToTimestampTest {

        @Test
        @DisplayName("표준 Timestamp 형식 문자열을 변환한다")
        void shouldConvertStandardTimestampFormat() {
            // Given
            String dateString = "2024-01-15 10:30:45";

            // When
            Timestamp result = UtlDateHelper.stringToTimestamp(dateString);

            // Then
            assertThat(result).isNotNull();
            LocalDateTime ldt = result.toLocalDateTime();
            assertThat(ldt.getYear()).isEqualTo(2024);
            assertThat(ldt.getMonthValue()).isEqualTo(1);
            assertThat(ldt.getDayOfMonth()).isEqualTo(15);
            assertThat(ldt.getHour()).isEqualTo(10);
            assertThat(ldt.getMinute()).isEqualTo(30);
            assertThat(ldt.getSecond()).isEqualTo(45);
        }

        @Test
        @DisplayName("잘못된 형식은 IllegalArgumentException을 던진다")
        void shouldThrowExceptionForInvalidFormat() {
            // Given
            String invalidDateString = "invalid-date";

            // When & Then
            assertThatThrownBy(() -> UtlDateHelper.stringToTimestamp(invalidDateString))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("stringToTimestamp2 메서드")
    class StringToTimestamp2Test {

        @Test
        @DisplayName("yyyyMMddHHmmss 형식 문자열을 Timestamp로 변환한다")
        void shouldConvertCompactDateFormat() {
            // Given
            String dateString = "20240115103045";

            // When
            Timestamp result = UtlDateHelper.stringToTimestamp2(dateString);

            // Then
            assertThat(result).isNotNull();
            LocalDateTime ldt = result.toLocalDateTime();
            assertThat(ldt.getYear()).isEqualTo(2024);
            assertThat(ldt.getMonthValue()).isEqualTo(1);
            assertThat(ldt.getDayOfMonth()).isEqualTo(15);
        }

        @Test
        @DisplayName("잘못된 형식은 null을 반환한다")
        void shouldReturnNullForInvalidFormat() {
            // Given
            String invalidDateString = "invalid";

            // When
            Timestamp result = UtlDateHelper.stringToTimestamp2(invalidDateString);

            // Then
            assertThat(result).isNull();
        }
    }

    @Nested
    @DisplayName("timestampToString 메서드")
    class TimestampToStringTest {

        @Test
        @DisplayName("Timestamp를 지정된 형식의 문자열로 변환한다")
        void shouldConvertTimestampToFormattedString() {
            // Given
            Timestamp timestamp = Timestamp.valueOf("2024-01-15 10:30:45");
            String format = "yyyy-MM-dd";

            // When
            String result = UtlDateHelper.timestampToString(format, timestamp);

            // Then
            assertThat(result).isEqualTo("2024-01-15");
        }

        @Test
        @DisplayName("시간까지 포함한 형식으로 변환한다")
        void shouldConvertWithTimeFormat() {
            // Given
            Timestamp timestamp = Timestamp.valueOf("2024-01-15 10:30:45");
            String format = "yyyy-MM-dd HH:mm:ss";

            // When
            String result = UtlDateHelper.timestampToString(format, timestamp);

            // Then
            assertThat(result).isEqualTo("2024-01-15 10:30:45");
        }
    }

    @Nested
    @DisplayName("getCurrentTime 메서드")
    class GetCurrentTimeTest {

        @Test
        @DisplayName("현재 시간을 Timestamp로 반환한다")
        void shouldReturnCurrentTimestamp() {
            // When
            Timestamp result = UtlDateHelper.getCurrentTime();

            // Then
            assertThat(result).isNotNull();
            LocalDateTime resultTime = result.toLocalDateTime();
            LocalDateTime now = LocalDateTime.now();

            assertThat(resultTime.getYear()).isEqualTo(now.getYear());
            assertThat(resultTime.getMonthValue()).isEqualTo(now.getMonthValue());
            assertThat(resultTime.getDayOfMonth()).isEqualTo(now.getDayOfMonth());
        }
    }

    @Nested
    @DisplayName("getCurrentDate 메서드")
    class GetCurrentDateTest {

        @Test
        @DisplayName("현재 날짜를 yyyy-MM-dd 형식으로 반환한다")
        void shouldReturnCurrentDateAsString() {
            // When
            String result = UtlDateHelper.getCurrentDate();

            // Then
            assertThat(result).isNotNull();
            assertThat(result).matches("\\d{4}-\\d{2}-\\d{2}");
        }

        @Test
        @DisplayName("사용자 지정 형식으로 현재 날짜를 반환한다")
        void shouldReturnCurrentDateWithCustomFormat() {
            // Given
            String format = "yyyyMMdd";

            // When
            String result = UtlDateHelper.getCurrentDate(format);

            // Then
            assertThat(result).isNotNull();
            assertThat(result).matches("\\d{8}");
        }
    }

    @Nested
    @DisplayName("getPartitionKey 메서드")
    class GetPartitionKeyTest {

        @Test
        @DisplayName("현재 연월을 yyyyMM 형식으로 반환한다")
        void shouldReturnPartitionKeyFormat() {
            // When
            String result = UtlDateHelper.getPartitionKey();

            // Then
            assertThat(result).isNotNull();
            assertThat(result).matches("\\d{6}");
        }
    }

    @Nested
    @DisplayName("selectPartitionKey 메서드")
    class SelectPartitionKeyTest {

        @Test
        @DisplayName("현재 연월보다 1 작은 값을 반환한다")
        void shouldReturnPreviousPartitionKey() {
            // When
            String result = UtlDateHelper.selectPartitionKey();

            // Then
            assertThat(result).isNotNull();
            String currentKey = UtlDateHelper.getPartitionKey();
            int expected = Integer.parseInt(currentKey) - 1;
            assertThat(result).isEqualTo(String.valueOf(expected));
        }
    }

    @Nested
    @DisplayName("subtractDaysFromCurrentDate 메서드")
    class SubtractDaysFromCurrentDateTest {

        @Test
        @DisplayName("현재 날짜에서 지정된 일수를 뺀 Timestamp를 반환한다")
        void shouldSubtractDaysFromCurrentDate() {
            // Given
            int daysToSubtract = 7;

            // When
            Timestamp result = UtlDateHelper.subtractDaysFromCurrentDate(daysToSubtract);

            // Then
            assertThat(result).isNotNull();
            LocalDateTime expected = LocalDateTime.now().minusDays(daysToSubtract);
            LocalDateTime resultTime = result.toLocalDateTime();

            assertThat(resultTime.toLocalDate()).isEqualTo(expected.toLocalDate());
        }

        @Test
        @DisplayName("0일을 빼면 현재 날짜와 동일하다")
        void shouldReturnCurrentDateWhenSubtractingZero() {
            // When
            Timestamp result = UtlDateHelper.subtractDaysFromCurrentDate(0);

            // Then
            assertThat(result).isNotNull();
            LocalDateTime resultTime = result.toLocalDateTime();
            LocalDateTime now = LocalDateTime.now();

            assertThat(resultTime.toLocalDate()).isEqualTo(now.toLocalDate());
        }
    }

    @Nested
    @DisplayName("subtractMinutes 메서드")
    class SubtractMinutesTest {

        @Test
        @DisplayName("Timestamp에서 지정된 분을 뺀다")
        void shouldSubtractMinutesFromTimestamp() {
            // Given
            Timestamp timestamp = Timestamp.valueOf("2024-01-15 10:30:00");
            int minutesToSubtract = 15;

            // When
            Timestamp result = UtlDateHelper.subtractMinutes(timestamp, minutesToSubtract);

            // Then
            assertThat(result).isNotNull();
            LocalDateTime expected = LocalDateTime.of(2024, 1, 15, 10, 15, 0);
            assertThat(result.toLocalDateTime()).isEqualTo(expected);
        }

        @Test
        @DisplayName("시간 경계를 넘어 분을 빼도 정상 동작한다")
        void shouldHandleHourBoundaryCrossing() {
            // Given
            Timestamp timestamp = Timestamp.valueOf("2024-01-15 00:15:00");
            int minutesToSubtract = 30;

            // When
            Timestamp result = UtlDateHelper.subtractMinutes(timestamp, minutesToSubtract);

            // Then
            LocalDateTime expected = LocalDateTime.of(2024, 1, 14, 23, 45, 0);
            assertThat(result.toLocalDateTime()).isEqualTo(expected);
        }

        @Test
        @DisplayName("null 입력 시 IllegalArgumentException을 던진다")
        void shouldThrowExceptionForNullTimestamp() {
            // When & Then
            assertThatThrownBy(() -> UtlDateHelper.subtractMinutes(null, 15))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Timestamp cannot be null");
        }

        @Test
        @DisplayName("경계값: 0분을 빼면 동일한 시간을 반환한다")
        void shouldReturnSameTimeWhenSubtractingZeroMinutes() {
            // Given
            Timestamp timestamp = Timestamp.valueOf("2024-01-15 10:30:00");

            // When
            Timestamp result = UtlDateHelper.subtractMinutes(timestamp, 0);

            // Then
            assertThat(result.toLocalDateTime()).isEqualTo(timestamp.toLocalDateTime());
        }
    }

    @Nested
    @DisplayName("longToTimestamp 메서드")
    class LongToTimestampTest {

        @Test
        @DisplayName("long 값을 Timestamp로 변환한다")
        void shouldConvertLongToTimestamp() {
            // Given
            Timestamp original = Timestamp.valueOf("2024-01-15 10:30:00");
            long epochMillis = original.getTime();

            // When
            Timestamp result = UtlDateHelper.longToTimestamp(epochMillis);

            // Then
            assertThat(result.getTime()).isEqualTo(epochMillis);
        }
    }
}
