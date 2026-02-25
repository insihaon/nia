package kr.go.ap.core.utility.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("UtlCommon 유틸리티 테스트")
class UtlCommonTest {

    @Nested
    @DisplayName("getNowTimeToString 메서드")
    class GetNowTimeToStringTest {

        @Test
        @DisplayName("현재 시간을 yyyyMMddHHmmss 형식의 문자열로 반환한다")
        void shouldReturnCurrentTimeAsString() throws Exception {
            // When
            String result = UtlCommon.getNowTimeToString();

            // Then
            assertThat(result).isNotNull();
            assertThat(result).hasSize(14);
            assertThat(result).matches("\\d{14}");
        }
    }

    @Nested
    @DisplayName("isCheckKr 메서드")
    class IsCheckKrTest {

        @Test
        @DisplayName("한글이 포함된 문자열은 true를 반환한다")
        void shouldReturnTrueWhenContainsKorean() {
            // Given
            String koreanText = "Hello한글World";

            // When
            boolean result = UtlCommon.isCheckKr(koreanText);

            // Then
            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("한글이 포함되지 않은 문자열은 false를 반환한다")
        void shouldReturnFalseWhenNoKorean() {
            // Given
            String englishText = "HelloWorld123";

            // When
            boolean result = UtlCommon.isCheckKr(englishText);

            // Then
            assertThat(result).isFalse();
        }

        @Test
        @DisplayName("자음/모음만 있어도 true를 반환한다")
        void shouldReturnTrueForKoreanJamoOnly() {
            // Given
            String jamoText = "ㅎㅏㄴㄱㅡㄹ";

            // When
            boolean result = UtlCommon.isCheckKr(jamoText);

            // Then
            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("null 입력 시 NullPointerException을 던진다")
        void shouldThrowExceptionForNull() {
            // Given & When & Then
            assertThatThrownBy(() -> UtlCommon.isCheckKr(null))
                    .isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    @DisplayName("isCheckDigits 메서드")
    class IsCheckDigitsTest {

        @Test
        @DisplayName("숫자가 포함된 문자열은 true를 반환한다")
        void shouldReturnTrueWhenContainsDigits() {
            // Given
            String textWithDigits = "abc123def";

            // When
            boolean result = UtlCommon.isCheckDigits(textWithDigits);

            // Then
            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("숫자가 없는 문자열은 false를 반환한다")
        void shouldReturnFalseWhenNoDigits() {
            // Given
            String textWithoutDigits = "abcdef";

            // When
            boolean result = UtlCommon.isCheckDigits(textWithoutDigits);

            // Then
            assertThat(result).isFalse();
        }
    }

    @Nested
    @DisplayName("getOnlyDigits 메서드")
    class GetOnlyDigitsTest {

        @Test
        @DisplayName("문자열에서 숫자만 추출하여 정수로 반환한다")
        void shouldExtractOnlyDigits() {
            // Given
            String mixedText = "abc123def456";

            // When
            int result = UtlCommon.getOnlyDigits(mixedText);

            // Then
            assertThat(result).isEqualTo(123456);
        }

        @Test
        @DisplayName("숫자가 없는 문자열은 NumberFormatException을 던진다")
        void shouldThrowExceptionWhenNoDigits() {
            // Given
            String noDigitsText = "abcdef";

            // When & Then
            assertThatThrownBy(() -> UtlCommon.getOnlyDigits(noDigitsText))
                    .isInstanceOf(NumberFormatException.class);
        }

        @Test
        @DisplayName("null 입력 시 0을 반환한다")
        void shouldReturnZeroForNull() {
            // When
            int result = UtlCommon.getOnlyDigits(null);

            // Then
            assertThat(result).isEqualTo(0);
        }
    }

    @Nested
    @DisplayName("getOnlyStrings 메서드")
    class GetOnlyStringsTest {

        @Test
        @DisplayName("문자열에서 영문자만 추출한다")
        void shouldExtractOnlyLetters() throws Exception {
            // Given
            String mixedText = "abc123def456";

            // When
            String result = UtlCommon.getOnlyStrings(mixedText);

            // Then
            assertThat(result).isEqualTo("abcdef");
        }

        @Test
        @DisplayName("공백도 함께 추출한다")
        void shouldKeepSpaces() throws Exception {
            // Given
            String textWithSpaces = "Hello 123 World";

            // When
            String result = UtlCommon.getOnlyStrings(textWithSpaces);

            // Then
            assertThat(result).isEqualTo("Hello  World");
        }

        @Test
        @DisplayName("null 입력 시 null을 반환한다")
        void shouldReturnNullForNull() throws Exception {
            // When
            String result = UtlCommon.getOnlyStrings(null);

            // Then
            assertThat(result).isNull();
        }
    }

    @Nested
    @DisplayName("JSON 변환 메서드")
    class JsonConversionTest {

        @Test
        @DisplayName("객체를 JSON 문자열로 변환한다")
        void shouldConvertObjectToJson() throws Exception {
            // Given
            TestDto dto = new TestDto("test", 123);

            // When
            String json = UtlCommon.objectToJson(dto);

            // Then
            assertThat(json).contains("\"name\":\"test\"");
            assertThat(json).contains("\"value\":123");
        }

        @Test
        @DisplayName("JSON 문자열을 객체로 변환한다")
        void shouldConvertJsonToObject() throws JsonProcessingException {
            // Given
            String json = "{\"name\":\"test\",\"value\":123}";
            TestDto template = new TestDto();

            // When
            TestDto result = (TestDto) UtlCommon.jsonToObject(template, json);

            // Then
            assertThat(result.getName()).isEqualTo("test");
            assertThat(result.getValue()).isEqualTo(123);
        }

        @Test
        @DisplayName("알 수 없는 필드가 있어도 무시하고 변환한다")
        void shouldIgnoreUnknownProperties() throws JsonProcessingException {
            // Given
            String jsonWithExtra = "{\"name\":\"test\",\"value\":123,\"unknown\":\"ignored\"}";
            TestDto template = new TestDto();

            // When
            TestDto result = (TestDto) UtlCommon.jsonToObject(template, jsonWithExtra);

            // Then
            assertThat(result.getName()).isEqualTo("test");
            assertThat(result.getValue()).isEqualTo(123);
        }
    }

    @Nested
    @DisplayName("문자열 Trim 메서드")
    class TrimMethodsTest {

        @Test
        @DisplayName("lTrim은 왼쪽 공백만 제거한다")
        void shouldTrimLeftSpaces() {
            // Given
            String text = "   hello   ";

            // When
            String result = UtlCommon.lTrim(text);

            // Then
            assertThat(result).isEqualTo("hello   ");
        }

        @Test
        @DisplayName("rTrim은 오른쪽 공백만 제거한다")
        void shouldTrimRightSpaces() {
            // Given
            String text = "   hello   ";

            // When
            String result = UtlCommon.rTrim(text);

            // Then
            assertThat(result).isEqualTo("   hello");
        }

        @Test
        @DisplayName("aTrim은 모든 공백을 제거한다")
        void shouldTrimAllSpaces() {
            // Given
            String text = "   hel lo   ";

            // When
            String result = UtlCommon.aTrim(text);

            // Then
            assertThat(result).isEqualTo("hello");
        }

        @Test
        @DisplayName("lrTrim은 양쪽 공백만 제거한다")
        void shouldTrimBothSides() {
            // Given
            String text = "   hel lo   ";

            // When
            String result = UtlCommon.lrTrim(text);

            // Then
            assertThat(result).isEqualTo("hel lo");
        }
    }

    @Nested
    @DisplayName("stringValueOf 메서드")
    class StringValueOfTest {

        @Test
        @DisplayName("0이 아닌 정수를 문자열로 변환한다")
        void shouldConvertNonZeroIntToString() {
            // When
            String result = UtlCommon.stringValueOf(123);

            // Then
            assertThat(result).isEqualTo("123");
        }

        @Test
        @DisplayName("0인 정수는 null을 반환한다")
        void shouldReturnNullForZeroInt() {
            // When
            String result = UtlCommon.stringValueOf(0);

            // Then
            assertThat(result).isNull();
        }

        @Test
        @DisplayName("0이 아닌 long을 문자열로 변환한다")
        void shouldConvertNonZeroLongToString() {
            // When
            String result = UtlCommon.stringValueOf(123456789L);

            // Then
            assertThat(result).isEqualTo("123456789");
        }

        @Test
        @DisplayName("0인 long은 null을 반환한다")
        void shouldReturnNullForZeroLong() {
            // When
            String result = UtlCommon.stringValueOf(0L);

            // Then
            assertThat(result).isNull();
        }
    }

    // Test DTO for JSON conversion tests
    static class TestDto {
        private String name;
        private int value;

        public TestDto() {}

        public TestDto(String name, int value) {
            this.name = name;
            this.value = value;
        }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public int getValue() { return value; }
        public void setValue(int value) { this.value = value; }
    }
}
