package kr.go.ap.prepro.mba.service.ticket;

import kr.go.ap.core.primary.nia.dto.mba.EngineLowPmDataListDto;
import kr.go.ap.core.primary.nia.dto.pm.optical.OpticalPerformanceDto;
import kr.go.ap.core.primary.nia.entity.mba.performance.RoadmPerformanceHistEntity;
import kr.go.ap.core.repository.primary.jpa.mba.performance.RoadmPerformanceHistRepository;
import kr.go.ap.core.repository.primary.mapper.prepro.pm.PmPreproMapper;
import kr.go.ap.prepro.mba.amqp.MbaEnginePrdAmqp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.ibatis.exceptions.PersistenceException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TicketDataService {

    private final PmPreproMapper pmPreproMapper;
    private final ModelMapper modelMapper;
    private final MbaEnginePrdAmqp mbaEnginePrdAmqp;
    private final RoadmPerformanceHistRepository perfHistRepository;

    public void setTicketData(List<OpticalPerformanceDto> optPerfDtoList) {

        Map<Object, List<OpticalPerformanceDto>> groups = null;
        HashMap<String, String> hashMap = null;
        List<List<OpticalPerformanceDto>> listByCollectors = null;
        List<OpticalPerformanceDto> upLowOptPmList = null;
        List<OpticalPerformanceDto> downLowOptPmList = null;
        List<OpticalPerformanceDto> lowPmHistDataDtoList = null;
        List<OpticalPerformanceDto> nextLowPmHistDataDtoList = null;

        Comparator<OpticalPerformanceDto> comparatorByRouteNum = null;

        OpticalPerformanceDto lowOpticalPerformanceDto = null;  // --- 해당데이터는 초기화된 적이 없는지?
        OpticalPerformanceDto lowOpticalPmSTMaxRouteNum = null;
        OpticalPerformanceDto lowOpticalPmSTMinRouteNum = null;
        OpticalPerformanceDto lowOpticalPmMaxRouteNum = null;
        OpticalPerformanceDto lowOpticalPmMinRouteNum = null;
        EngineLowPmDataListDto engineLowPmDataListDto = null;
        try {
            groups = optPerfDtoList.stream()
                    .collect(Collectors.groupingBy(optPerfEntity -> optPerfEntity.getTrunkId()));

            listByCollectors = new ArrayList<>(groups.values());

            if (!listByCollectors.isEmpty()) {

                for (List<OpticalPerformanceDto> optList : listByCollectors) {
                    upLowOptPmList = new ArrayList<>();
                    downLowOptPmList = new ArrayList<>();

                    for (OpticalPerformanceDto opticalPerformanceDto : optList) {

                        opticalPerformanceDto.setLowSignal(true);

                        if (StringUtils.equals("UP", opticalPerformanceDto.getDirection())) {
                            upLowOptPmList.add(opticalPerformanceDto);
                        } else if (StringUtils.equals("DOWN", opticalPerformanceDto.getDirection())) {
                            downLowOptPmList.add(opticalPerformanceDto);
                        }
                    }

                    if (!CollectionUtils.isEmpty(upLowOptPmList)) {
                        comparatorByRouteNum = Comparator.comparingInt(OpticalPerformanceDto::getRouteNum);

                        try {
                            lowOpticalPmSTMaxRouteNum = upLowOptPmList.stream()
                                    .filter(x -> StringUtils.equals("IN", x.getInOut()))
                                    .max(comparatorByRouteNum)
                                    .orElseThrow(NoSuchElementException::new);

                            lowOpticalPmMaxRouteNum = upLowOptPmList.stream()
                                    .max(comparatorByRouteNum)
                                    .orElseThrow(NoSuchElementException::new);
                        } catch (NoSuchElementException e) {
                            log.warn("No such element found in upOptPwrVectorEntityList: " + ExceptionUtils.getMessage(e));

                            lowPmHistDataDtoList = setLowSignalDataHist(upLowOptPmList.get(0));
                            lowPmHistDataDtoList = histLowSignalCheck(upLowOptPmList, lowPmHistDataDtoList);

                            sendLowPmDataToEngine(upLowOptPmList, lowPmHistDataDtoList);
                            continue;
                        }

                        lowPmHistDataDtoList = setLowSignalDataHist(upLowOptPmList.get(0));
                        lowPmHistDataDtoList = histLowSignalCheck(upLowOptPmList, lowPmHistDataDtoList);
//                        lowPmHistDataDtoList.addAll(upLowOptPmList);

                        List<OpticalPerformanceDto> lowPmToAddList = new ArrayList<>();

                        for (OpticalPerformanceDto upLowPmDto : upLowOptPmList) {
                            if (!StringUtils.equals("REPEATER", upLowPmDto.getRoadmCode())) continue;

                            if (StringUtils.equals(upLowPmDto.getTid(), lowOpticalPmSTMaxRouteNum.getTid())
                                    && StringUtils.equals(upLowPmDto.getTrunkId(), lowOpticalPmSTMaxRouteNum.getTrunkId())
                                    && StringUtils.equals(upLowPmDto.getInOut(), lowOpticalPmSTMaxRouteNum.getInOut())
                                    && upLowPmDto.getRouteNum() == lowOpticalPmMaxRouteNum.getRouteNum()) {

                                int routeNum = Math.min(lowOpticalPmSTMaxRouteNum.getRouteNum() + 1, 10);

                                List<RoadmPerformanceHistEntity> nextList =
                                        perfHistRepository.findHistByTrunkIdAndOcrTmeAndRouteNum(
                                                lowOpticalPmSTMaxRouteNum.getTrunkId(),
                                                lowOpticalPmSTMaxRouteNum.getOcrtime(),
                                                routeNum
                                        ).orElse(Collections.emptyList());

                                if (!nextList.isEmpty()) {
                                    upLowPmDto.setLowSignal(true);
                                    lowPmToAddList.add(lowOpticalPerformanceDto);
                                }
                            }
                        }

                        if (!lowPmToAddList.isEmpty()) {
                            upLowOptPmList.addAll(lowPmToAddList);
                        }

//                        engineLowPmDataListDto = EngineLowPmDataListDto.builder()
//                                .lowPmDataDtoList(upLowOptPmList)
//                                .lowPmHistDataDtoList(lowPmHistDataDtoList)
//                                .build();
//
//                        mbaEnginePrdAmqp.sendMessageCmd(engineLowPmDataListDto);
                        sendLowPmDataToEngine(upLowOptPmList, lowPmHistDataDtoList);

                    }

                    if (!CollectionUtils.isEmpty(downLowOptPmList)) {
                        comparatorByRouteNum = Comparator.comparingInt(OpticalPerformanceDto::getRouteNum);

                        try {
                            lowOpticalPmSTMinRouteNum = downLowOptPmList.stream()
                                    .filter(x -> StringUtils.equals("IN", x.getInOut()))
                                    .min(comparatorByRouteNum)
                                    .orElseThrow(NoSuchElementException::new);

                            lowOpticalPmMinRouteNum = downLowOptPmList.stream()
                                    .min(comparatorByRouteNum)
                                    .orElseThrow(NoSuchElementException::new);

                        } catch (NoSuchElementException e) {
                            log.warn("No such element found in upOptPwrVectorEntityList: " + ExceptionUtils.getMessage(e));

                            lowPmHistDataDtoList = setLowSignalDataHist(downLowOptPmList.get(0));
                            lowPmHistDataDtoList = histLowSignalCheck(downLowOptPmList, lowPmHistDataDtoList);

//                            e
                            sendLowPmDataToEngine(downLowOptPmList, lowPmHistDataDtoList);
                            continue;
                        }

                        lowPmHistDataDtoList = setLowSignalDataHist(downLowOptPmList.get(0));
                        lowPmHistDataDtoList = histLowSignalCheck(downLowOptPmList, lowPmHistDataDtoList);
//                        lowPmHistDataDtoList.addAll(downLowOptPmList);

                        List<OpticalPerformanceDto> lowPmToAddList = new ArrayList<>();

                        for (OpticalPerformanceDto upLowPmDto : downLowOptPmList) {
                            if (StringUtils.equals("REPEATER", upLowPmDto.getRoadmCode())) {
                                if (StringUtils.equals(upLowPmDto.getTid(), lowOpticalPmSTMinRouteNum.getTid())
                                        && StringUtils.equals(upLowPmDto.getTrunkId(), lowOpticalPmSTMinRouteNum.getTrunkId())
                                        && StringUtils.equals(upLowPmDto.getInOut(), lowOpticalPmSTMinRouteNum.getInOut())
                                        && upLowPmDto.getRouteNum() == lowOpticalPmMinRouteNum.getRouteNum()) {

                                    int routeNum = (Math.min(lowOpticalPmSTMinRouteNum.getRouteNum() + 1, 10));
                                    List<RoadmPerformanceHistEntity> nextLowPmHistDataEntityList = perfHistRepository.findHistByTrunkIdAndOcrTmeAndRouteNum(lowOpticalPmSTMinRouteNum.getTrunkId(), lowOpticalPmSTMinRouteNum.getOcrtime(), routeNum).orElse(Collections.emptyList());
                                    if (!CollectionUtils.isEmpty(nextLowPmHistDataEntityList)) {
                                        lowOpticalPerformanceDto.setLowSignal(true);
                                        lowPmToAddList.add(lowOpticalPerformanceDto);
                                    }
                                }
                            }
                        }

                        if (!lowPmToAddList.isEmpty()) {
                            downLowOptPmList.addAll(lowPmToAddList);
                        }
                        sendLowPmDataToEngine(downLowOptPmList, lowPmHistDataDtoList);
                    }
                }
            }
        } catch (NullPointerException | PersistenceException | IllegalArgumentException e) {
            log.error("setTicketData : ",e);
        }
    }

    private void sendLowPmDataToEngine(List<OpticalPerformanceDto> lowPmDataDtoList, List<OpticalPerformanceDto> lowPmHistDataDtoList) {

        EngineLowPmDataListDto engineLowPmDataListDto;
        engineLowPmDataListDto = EngineLowPmDataListDto.builder()
                .lowPmDataDtoList(lowPmDataDtoList)
                .lowPmHistDataDtoList(lowPmHistDataDtoList)
                .build();
        mbaEnginePrdAmqp.sendMessageCmd(engineLowPmDataListDto);
    }

    private List<OpticalPerformanceDto> setLowSignalDataHist(OpticalPerformanceDto lowOptPerfDto) {
        List<OpticalPerformanceDto> lowOpticalPerformanceHistList = null;
        Calendar cal = Calendar.getInstance();
        Timestamp preTime = null;

        try {
            cal.setTimeInMillis(lowOptPerfDto.getOcrtime().getTime());

            cal.add(Calendar.MINUTE, -15);
            preTime = new Timestamp(cal.getTime().getTime());
            List<RoadmPerformanceHistEntity> lowOptPerfHistEntityList = perfHistRepository.findHistByTrunkIdAndDirectionAndTimeRange(lowOptPerfDto.getTrunkId(), lowOptPerfDto.getDirection(), preTime, lowOptPerfDto.getOcrtime()).orElseThrow();


            //@TODO : model Mapper 수정 하기
            lowOpticalPerformanceHistList = lowOptPerfHistEntityList.stream()
                    .map(perfHistEntity -> {
                        OpticalPerformanceDto dto = modelMapper.map(perfHistEntity, OpticalPerformanceDto.class);
                        dto.setInOut(perfHistEntity.getInOut());
                        dto.setPort(perfHistEntity.getPort());
                        dto.setTid(perfHistEntity.getTid());
                        dto.setTrunkId(perfHistEntity.getTrunkId());
                        dto.setOcrtime(perfHistEntity.getOcrtime());
                        dto.setPtpname(perfHistEntity.getPtpname());
                        return dto;
                    }).collect(Collectors.toList());


        } catch (NullPointerException | IllegalArgumentException e) {
            log.error("setLowSignalDataHist : ",e);
        }
        return lowOpticalPerformanceHistList;
    }

    private List<OpticalPerformanceDto> histLowSignalCheck(List<OpticalPerformanceDto> lowOptList, List<OpticalPerformanceDto> lowOptHistList) {
        try {
            Set<String> lowOptKeys = lowOptList.stream()
                    .map(dto -> dto.getTid() + "|" + dto.getPtpname() + "|" + dto.getPort() + "|" + dto.getInOut() + "|" + String.valueOf(dto.getOcrtime()))
                    .collect(Collectors.toSet());

            lowOptHistList.stream()
                    .filter(hist -> lowOptKeys.contains(hist.getTid() + "|" + hist.getPtpname() + "|" + hist.getPort() + "|" + hist.getInOut() + "|" + String.valueOf(hist.getOcrtime())))
                    .forEach(hist -> hist.setLowSignal(true));

        } catch (IndexOutOfBoundsException | NullPointerException e) {
            log.error("histLowSignalCheck : ",e);
        }
        return lowOptHistList;
    }
}
