package kr.go.ap.prepro.mba.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import kr.go.ap.PreproMbaApplication;
import kr.go.ap.core.config.primary.db.PrimaryDatabaseConfiguration;
import kr.go.ap.core.primary.nia.dto.linkage.model.ModelResultDto;
import kr.go.ap.core.primary.nia.dto.pm.optical.OpticalPerformanceDto;
import kr.go.ap.core.primary.nia.entity.mba.performance.RoadmCheckOpticalPerformanceEntity;
import kr.go.ap.core.repository.primary.jpa.mba.performance.RoadmCheckOpticalPerformanceRepository;
import kr.go.ap.core.repository.primary.mapper.prepro.pm.PmPreproMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.repository.core.support.RepositoryMethodInvocationListener;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@Import({PreproMbaApplication.class, PrimaryDatabaseConfiguration.class})
@Slf4j
@ActiveProfiles("local")
@SpringBootTest()
class LineMonitoringHdlServiceTest {

    @Autowired
    private LineMonitoringHdlService lineMonitoringHdlService;

    @Autowired
    private RoadmCheckOpticalPerformanceRepository checkOptPerfRepo;

    @Autowired
    private PmPreproMapper pmPreproMapper;

    @Autowired
    private ModelMapper modelMapper;
    private RepositoryMethodInvocationListener repositoryMethodInvocationListener;

    @Transactional
    @Test
    void lineMonitoringHdlProcessor() {

        var params = new HashMap<String, String>();
        params.put("ocrTime", "2024-08-14 02:30:00");

        List<OpticalPerformanceDto> realData =
                pmPreproMapper.selectOpticalPerformanceList(params);


        List<ModelResultDto> modelResultDtos = realData.stream().map(perfDto -> {
            return modelMapper.map(perfDto, ModelResultDto.class);
        }).collect(Collectors.toList());
        log.info("realData: {}", realData);

        lineMonitoringHdlService.lineMonitoringHdlProcessor(modelResultDtos);

    }


    @PersistenceContext EntityManager em;



}