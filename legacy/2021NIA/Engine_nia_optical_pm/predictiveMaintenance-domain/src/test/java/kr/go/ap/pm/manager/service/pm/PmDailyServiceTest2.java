package kr.go.ap.pm.manager.service.pm;

import kr.go.ap.core.repository.primary.mapper.predictive.pm.PmPreMapper;
import kr.go.ap.pm.manager.service.model.AiPmDataSendService;
import org.apache.ibatis.exceptions.PersistenceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("PmDailyService 단위 테스트")
class PmDailyServiceTest2 {

    @Mock
    private AiPmDataSendService aiPmDataSendService;

    @Mock
    private PmPreMapper pmPreMapper;

    @InjectMocks
    private PmDailyService pmDailyService;

    @Nested
    @DisplayName("performanceDailyHdl 메서드")
    class PerformanceDailyHdlTest {

        @Test
        @DisplayName("모든 일일 데이터 생성 프로시저를 순차적으로 호출한다")
        void shouldCallAllDailyDataCreationProceduresInOrder() {
            // When
            pmDailyService.performanceDailyHdl();

            // Then
            InOrder inOrder = inOrder(pmPreMapper, aiPmDataSendService);
            inOrder.verify(pmPreMapper).callSetPerformanceDaily();
            inOrder.verify(pmPreMapper).callSetPerformanceDailyNtd();
            inOrder.verify(pmPreMapper).callSetPerformanceDailyReference();
            inOrder.verify(aiPmDataSendService).sendPmData();
        }

        @Test
        @DisplayName("일일 데이터 생성 중 예외 발생해도 다음 단계를 실행한다")
        void shouldContinueWhenDailyDataCreationFails() {
            // Given
            willThrow(new PersistenceException("DB Error"))
                    .given(pmPreMapper).callSetPerformanceDaily();

            // When
            pmDailyService.performanceDailyHdl();

            // Then
            verify(pmPreMapper).callSetPerformanceDaily();
            verify(pmPreMapper).callSetPerformanceDailyNtd();
            verify(pmPreMapper).callSetPerformanceDailyReference();
            verify(aiPmDataSendService).sendPmData();
        }

        @Test
        @DisplayName("NTD 데이터 생성 중 예외 발생해도 다음 단계를 실행한다")
        void shouldContinueWhenNtdDataCreationFails() {
            // Given
            willThrow(new PersistenceException("DB Error"))
                    .given(pmPreMapper).callSetPerformanceDailyNtd();

            // When
            pmDailyService.performanceDailyHdl();

            // Then
            verify(pmPreMapper).callSetPerformanceDaily();
            verify(pmPreMapper).callSetPerformanceDailyNtd();
            verify(pmPreMapper).callSetPerformanceDailyReference();
            verify(aiPmDataSendService).sendPmData();
        }

        @Test
        @DisplayName("Reference 데이터 생성 중 예외 발생해도 AI 데이터 전송을 실행한다")
        void shouldSendPmDataWhenReferenceDataCreationFails() {
            // Given
            willThrow(new PersistenceException("DB Error"))
                    .given(pmPreMapper).callSetPerformanceDailyReference();

            // When
            pmDailyService.performanceDailyHdl();

            // Then
            verify(pmPreMapper).callSetPerformanceDaily();
            verify(pmPreMapper).callSetPerformanceDailyNtd();
            verify(pmPreMapper).callSetPerformanceDailyReference();
            verify(aiPmDataSendService).sendPmData();
        }

        @Test
        @DisplayName("모든 데이터 생성이 실패해도 예외를 던지지 않는다")
        void shouldNotThrowExceptionWhenAllDataCreationFails() {
            // Given
            willThrow(new PersistenceException("Error 1"))
                    .given(pmPreMapper).callSetPerformanceDaily();
            willThrow(new PersistenceException("Error 2"))
                    .given(pmPreMapper).callSetPerformanceDailyNtd();
            willThrow(new PersistenceException("Error 3"))
                    .given(pmPreMapper).callSetPerformanceDailyReference();

            // When & Then
            assertThatCode(() -> pmDailyService.performanceDailyHdl())
                    .doesNotThrowAnyException();

            verify(aiPmDataSendService).sendPmData();
        }

        @Test
        @DisplayName("정상적인 경우 AI 데이터 전송이 마지막에 호출된다")
        void shouldCallSendPmDataAtTheEnd() {
            // When
            pmDailyService.performanceDailyHdl();

            // Then
            verify(aiPmDataSendService, times(1)).sendPmData();
        }
    }
}
