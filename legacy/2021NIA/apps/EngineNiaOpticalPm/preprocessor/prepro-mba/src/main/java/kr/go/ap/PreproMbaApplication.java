package kr.go.ap;

import kr.go.ap.core.primary.nia.dto.linkage.model.ModelResultDto;
import kr.go.ap.core.primary.nia.dto.pm.optical.OpticalPerformanceDto;
import kr.go.ap.core.repository.primary.jpa.mba.performance.RoadmPerformanceHistRepository;
import kr.go.ap.core.repository.primary.mapper.prepro.pm.PmPreproMapper;
import kr.go.ap.prepro.mba.service.LineMonitoringHdlService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@EnableScheduling
@Slf4j
@SpringBootApplication
public class PreproMbaApplication implements CommandLineRunner{

	public static void main(String[] args) {
		SpringApplication.run(PreproMbaApplication.class, args);
	}

//    private final LineMonitoringHdlService lineMonitoringHdlService;
//    private final PmPreproMapper pmPreproMapper;
//    private final ModelMapper modelMapper;

	@Override
	public void run(String... arg0) throws Exception {
////        lineMonitoringHdlService.lineMonitoringHdlProcessor("2024-08-14 02:30:00");
//        var params = new HashMap<String, String>();
//        params.put("ocrTime", "2024-08-14 02:30:00");
//
//        List<OpticalPerformanceDto> realData =
//                pmPreproMapper.selectOpticalPerformanceList(params);
//
//
//
//        List<ModelResultDto> modelResultDtos = realData.stream().map(perfDto -> {
//            return modelMapper.map(perfDto, ModelResultDto.class);
//        }).collect(Collectors.toList());
//        log.info("realData: {}", realData);
//
//        lineMonitoringHdlService.lineMonitoringHdlProcessor(modelResultDtos);
	}
}
