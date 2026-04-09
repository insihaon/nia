package kr.go.ap.prepro.mba.service;

import kr.go.ap.core.primary.nia.dto.linkage.model.ModelResultDto;
import kr.go.ap.core.primary.nia.dto.pm.optical.OpticalPerformanceDto;
import kr.go.ap.core.primary.nia.entity.mba.performance.RoadmCheckOpticalPerformanceEntity;
import kr.go.ap.core.repository.primary.jpa.mba.performance.RoadmCheckOpticalPerformanceRepository;
import kr.go.ap.core.utility.map.OptPerfStructMapper;
import kr.go.ap.prepro.mba.service.ticket.TicketDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.ibatis.exceptions.PersistenceException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class LineMonitoringHdlService {

    //    private final PmPreproMapper pmPreproMapper;
    private final TicketDataService ticketDataService;
    private final RoadmCheckOpticalPerformanceRepository checkOptPerfRepo;
    private final ModelMapper modelMapper;
    private final OptPerfStructMapper optPerfStructMapper;

    public void lineMonitoringHdlProcessor(List<ModelResultDto> modelResultDtoList) {
        if (CollectionUtils.isEmpty(modelResultDtoList)) {
            return;
        }

        try {
            List<OpticalPerformanceDto> lowOptPerfDtoList = new ArrayList<>();
            for (ModelResultDto modelResultDto : modelResultDtoList) {
                OpticalPerformanceDto optPerfDto = modelMapper.map(modelResultDto, OpticalPerformanceDto.class);
               if ((optPerfDto.getValueMax() - optPerfDto.getValueMin() >= 3)) {
                    lowOptPerfDtoList.add(optPerfDto);
                }
            }

            log.info("lowOptPerfDtoList Cnt : {}", lowOptPerfDtoList.size());
            if (!lowOptPerfDtoList.isEmpty()) {
                List<RoadmCheckOpticalPerformanceEntity> checkOptPerfEntityList = lowOptPerfDtoList.stream().map(optPerfStructMapper::toCheckOptPerfEntity).toList();
                log.info("checkOptPerfEntityList Cnt : {}", checkOptPerfEntityList.size());
                checkOptPerfRepo.saveAll(checkOptPerfEntityList);
                ticketDataService.setTicketData(lowOptPerfDtoList);
            }
        } catch (NullPointerException | PersistenceException e) {
            log.error("lineMonitoringHdlProcessor : ",e);
        }
    }
}
