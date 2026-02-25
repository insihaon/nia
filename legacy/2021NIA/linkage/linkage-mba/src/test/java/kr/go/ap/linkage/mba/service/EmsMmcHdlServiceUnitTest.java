package kr.go.ap.linkage.mba.service;

import kr.go.ap.core.primary.nia.dto.linkage.model.ModelSendDto;
import kr.go.ap.core.primary.nia.entity.mba.resource.eqmnt.EqmntEntity;
import kr.go.ap.core.primary.nia.entity.mba.resource.eqmnt.EqmntSipcEntity;
import kr.go.ap.core.repository.primary.jpa.resource.eqmnt.EqmntRepository;
import kr.go.ap.core.repository.primary.jpa.resource.eqmnt.EqmntSipcRepository;
import kr.go.ap.core.repository.primary.mapper.common.CommonMapper;
import kr.go.ap.core.repository.primary.mapper.linkage.pm.PmLinkageMapper;
import kr.go.ap.linkage.mba.amqp.MbaAiModelPrdAmqp;
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
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("EmsMmcHdlService 단위 테스트")
class EmsMmcHdlServiceUnitTest {

    @Mock
    private PmLinkageMapper pmMapper;

    @Mock
    private EqmntRepository eqmntRepository;

    @Mock
    private EqmntSipcRepository eqmntSipcRepository;

    @Mock
    private CommonMapper commonMapper;

    @Mock
    private ParsingHdlService parsingHdlService;

    @Mock
    private MbaAiModelPrdAmqp mbaAiModelPrdAmqp;

    @InjectMocks
    private EmsMmcHdlService emsMmcHdlService;

    @Captor
    private ArgumentCaptor<ModelSendDto> modelSendDtoCaptor;

    @Captor
    private ArgumentCaptor<HashMap<String, String>> hashMapCaptor;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(emsMmcHdlService, "roadmIp", "192.168.1.1");
        ReflectionTestUtils.setField(emsMmcHdlService, "port", 3082);
        ReflectionTestUtils.setField(emsMmcHdlService, "id", "testUser");
        ReflectionTestUtils.setField(emsMmcHdlService, "pw", "testPass");
    }

    private EqmntEntity createEqmntEntity(String tid, String model) {
        EqmntEntity entity = new EqmntEntity();
        ReflectionTestUtils.setField(entity, "tid", tid);
        ReflectionTestUtils.setField(entity, "model", model);
        return entity;
    }

    private EqmntSipcEntity createEqmntSipcEntity(String tid, String systemName) {
        EqmntSipcEntity entity = new EqmntSipcEntity();
        ReflectionTestUtils.setField(entity, "tid", tid);
        ReflectionTestUtils.setField(entity, "systemName", systemName);
        return entity;
    }

    @Nested
    @DisplayName("pmCollectHdl 메서드")
    class PmCollectHdlTest {

        @Test
        @DisplayName("장비 목록이 비어있으면 SIPC 수집을 수행하지 않는다")
        void shouldNotCollectSipcWhenEquipmentListEmpty() {
            // Given
            given(eqmntRepository.findByModelContaining("6300"))
                    .willReturn(Collections.emptyList());

            // When
            emsMmcHdlService.pmCollectHdl();

            // Then
            verify(parsingHdlService, never()).parsingHdl(eq("sipc"), anyString());
        }

        @Test
        @DisplayName("예외 발생 시에도 안전하게 처리된다")
        void shouldHandleExceptionSafely() {
            // Given
            given(eqmntRepository.findByModelContaining("6300"))
                    .willThrow(new RuntimeException("DB Connection Error"));

            // When & Then
            assertThatCode(() -> emsMmcHdlService.pmCollectHdl())
                    .doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("shutdown 메서드")
    class ShutdownTest {

        @Test
        @DisplayName("클라이언트가 null이면 예외 없이 종료한다")
        void shouldShutdownSafelyWhenClientIsNull() {
            // When & Then
            assertThatCode(() -> emsMmcHdlService.shutdown())
                    .doesNotThrowAnyException();
        }
    }
}
