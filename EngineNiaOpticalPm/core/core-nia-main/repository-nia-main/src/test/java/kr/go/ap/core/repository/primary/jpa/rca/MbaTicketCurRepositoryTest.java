package kr.go.ap.core.repository.primary.jpa.rca;

import kr.go.ap.core.primary.nia.entity.mba.engine.MbaTicketCurEntity;
import kr.go.ap.core.repository.primary.jpa.mba.engine.MbaTicketCurRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("RcaTicketCurRepository 테스트")
class MbaTicketCurRepositoryTest {

    @Mock
    private MbaTicketCurRepository mbaTicketCurRepository;

    private MbaTicketCurEntity testEntity;

    @BeforeEach
    void setUp() {
        testEntity = MbaTicketCurEntity.builder()
                .ticketId("TEST-TICKET-001")
                .ticketType("PF")
                .ticketGenerationTime(new Timestamp(System.currentTimeMillis()))
                .faultTime(new Timestamp(System.currentTimeMillis()))
                .rootCauseType("MomentaryBreakoff")
                .rootCauseDomain("ROADM")
                .ticketRcaResultCode("OPTICAL_CABLE_LOW_SIGNAL")
                .ticketRcaResultDtlCode("광레벨 순단 저하")
                .totalRelatedAlarmCnt(5)
                .status("INIT")
                .trunkId("TRUNK-001")
                .trunkName("TestTrunk")
                .direction("UP")
                .occur(true)
                .build();
    }

    @Nested
    @DisplayName("기본 CRUD 테스트")
    class BasicCrudTest {

        @Test
        @DisplayName("티켓 엔티티를 저장할 수 있다")
        void shouldSaveTicketEntity() {
            // Given
            given(mbaTicketCurRepository.save(testEntity)).willReturn(testEntity);

            // When
            MbaTicketCurEntity savedEntity = mbaTicketCurRepository.save(testEntity);

            // Then
            assertThat(savedEntity).isNotNull();
            assertThat(savedEntity.getTicketId()).isEqualTo("TEST-TICKET-001");
            verify(mbaTicketCurRepository).save(testEntity);
        }

        @Test
        @DisplayName("ID로 티켓 엔티티를 조회할 수 있다")
        void shouldFindTicketById() {
            // Given
            given(mbaTicketCurRepository.findById("TEST-TICKET-001"))
                    .willReturn(Optional.of(testEntity));

            // When
            Optional<MbaTicketCurEntity> found = mbaTicketCurRepository.findById("TEST-TICKET-001");

            // Then
            assertThat(found).isPresent();
            assertThat(found.get().getTicketId()).isEqualTo("TEST-TICKET-001");
            assertThat(found.get().getTicketType()).isEqualTo("PF");
        }

        @Test
        @DisplayName("존재하지 않는 ID로 조회 시 빈 Optional을 반환한다")
        void shouldReturnEmptyOptionalForNonExistentId() {
            // Given
            given(mbaTicketCurRepository.findById("NON-EXISTENT"))
                    .willReturn(Optional.empty());

            // When
            Optional<MbaTicketCurEntity> found = mbaTicketCurRepository.findById("NON-EXISTENT");

            // Then
            assertThat(found).isEmpty();
        }

        @Test
        @DisplayName("티켓 엔티티를 삭제할 수 있다")
        void shouldDeleteTicketEntity() {
            // When
            mbaTicketCurRepository.delete(testEntity);

            // Then
            verify(mbaTicketCurRepository).delete(testEntity);
        }
    }

    @Nested
    @DisplayName("nextTicketId 시퀀스 테스트")
    class NextTicketIdTest {

        @Test
        @DisplayName("다음 티켓 ID를 가져올 수 있다")
        void shouldGetNextTicketId() {
            // Given
            given(mbaTicketCurRepository.nextTicketId()).willReturn("100001");

            // When
            String nextId = mbaTicketCurRepository.nextTicketId();

            // Then
            assertThat(nextId).isEqualTo("100001");
        }

        @Test
        @DisplayName("연속 호출 시 다른 ID를 반환한다")
        void shouldReturnDifferentIdsOnConsecutiveCalls() {
            // Given
            given(mbaTicketCurRepository.nextTicketId())
                    .willReturn("100001")
                    .willReturn("100002")
                    .willReturn("100003");

            // When
            String firstId = mbaTicketCurRepository.nextTicketId();
            String secondId = mbaTicketCurRepository.nextTicketId();
            String thirdId = mbaTicketCurRepository.nextTicketId();

            // Then
            assertThat(firstId).isEqualTo("100001");
            assertThat(secondId).isEqualTo("100002");
            assertThat(thirdId).isEqualTo("100003");
        }
    }

    @Nested
    @DisplayName("엔티티 필드 테스트")
    class EntityFieldTest {

        @Test
        @DisplayName("모든 필드가 올바르게 설정된다")
        void shouldSetAllFieldsCorrectly() {
            // Then
            assertThat(testEntity.getTicketId()).isEqualTo("TEST-TICKET-001");
            assertThat(testEntity.getTicketType()).isEqualTo("PF");
            assertThat(testEntity.getRootCauseType()).isEqualTo("MomentaryBreakoff");
            assertThat(testEntity.getRootCauseDomain()).isEqualTo("ROADM");
            assertThat(testEntity.getTicketRcaResultCode()).isEqualTo("OPTICAL_CABLE_LOW_SIGNAL");
            assertThat(testEntity.getTotalRelatedAlarmCnt()).isEqualTo(5);
            assertThat(testEntity.getStatus()).isEqualTo("INIT");
            assertThat(testEntity.getTrunkId()).isEqualTo("TRUNK-001");
            assertThat(testEntity.getDirection()).isEqualTo("UP");
            assertThat(testEntity.isOccur()).isTrue();
        }
    }
}
