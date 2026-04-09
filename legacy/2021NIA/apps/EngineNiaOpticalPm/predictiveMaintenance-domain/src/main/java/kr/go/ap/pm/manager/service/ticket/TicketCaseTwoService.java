package kr.go.ap.pm.manager.service.ticket;


import kr.go.ap.code.pm.ticket.TicketReasonCode;
import kr.go.ap.core.primary.nia.dto.pm.model.PmAiTicketCheckDto;
import kr.go.ap.core.primary.nia.dto.pm.ticket.PmTicketDto;
import kr.go.ap.core.primary.nia.entity.pm.ticket.PmTicketEntity;
import kr.go.ap.core.repository.primary.jpa.pm.ticket.PmTicketRepository;
import kr.go.ap.core.repository.primary.mapper.predictive.pm.PmPreMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TicketCaseTwoService {

//    private final static String PM_TICKET_REASON = "광레벨 장기 변화 예측 경보";

    private final PmTicketRepository pmTicketRepository;
    //    private final PmTicketMapper pmTicketMapper;
    private final PmPreMapper pmPreMapper;

    public void ticketCaseTwoHdlProcessor() {
        log.info("ticketCaseTwoHdlProcessor");

        List<PmAiTicketCheckDto> pmAiTicketCheckDtoList;
        List<PmTicketEntity> matchTicketEntitiyList;

        PmTicketEntity newPmTicketEntity;

        try {
            pmAiTicketCheckDtoList = pmPreMapper.selectWarningDateCheckList("N");

            if (!CollectionUtils.isEmpty(pmAiTicketCheckDtoList)) {

                matchTicketEntitiyList = pmTicketRepository.findByReasonAndClearDateIsNull(TicketReasonCode.OPTICAL_LEVEL_LONG_TERM_VARIATION_ALERT.getName());

                if (!CollectionUtils.isEmpty(matchTicketEntitiyList)) {
                    for (PmAiTicketCheckDto pmAiTicketCheckDto : pmAiTicketCheckDtoList) {
                        newPmTicketEntity = ticketSaveCheck(matchTicketEntitiyList, pmAiTicketCheckDto);

                        if (newPmTicketEntity != null) {
                            log.info("new Ticket : " + newPmTicketEntity.toString());
                            pmTicketRepository.save(newPmTicketEntity);
                        }
                    }

                    for (PmTicketEntity pmTicketEntity : matchTicketEntitiyList) {
                        clearCheck(pmAiTicketCheckDtoList, pmTicketEntity);
                    }
                } else {
                    for (PmAiTicketCheckDto pmAiTicketCheckDto : pmAiTicketCheckDtoList) {
                        newPmTicketEntity = createTicket(pmAiTicketCheckDto);
                        log.info("new Ticket : " + newPmTicketEntity.toString());
                        pmTicketRepository.save(newPmTicketEntity);
                    }
                }
            }
        } catch (NullPointerException | IllegalArgumentException | PersistenceException e) {
            log.error(ExceptionUtils.getMessage(e));
        }
    }

    private PmTicketEntity ticketSaveCheck(List<PmTicketEntity> matchTicketList, PmAiTicketCheckDto pmAiTicketCheckDto) {
        List<PmTicketEntity> newTicketList;
        PmTicketEntity newTicket = null;

        try {
            newTicketList = matchTicketList.stream()
                    .filter(x -> StringUtils.equals(x.getTrunkName(), pmAiTicketCheckDto.getTrunkName())
                            && StringUtils.equals(x.getDirection(), pmAiTicketCheckDto.getDirection()))
                    .collect(Collectors.toList());

            if (CollectionUtils.isEmpty(newTicketList)) {
                newTicket = createTicket(pmAiTicketCheckDto);
            } else {
                log.info("stay Ticket : " + newTicketList.get(0).toString());
                return null;
            }
        } catch (Exception e) {
            log.error(ExceptionUtils.getMessage(e));
        }
        return newTicket;
    }

    private void clearCheck(List<PmAiTicketCheckDto> pmAiTicketCheckDtoList, PmTicketEntity pmTicketEntity) {
        log.info("clearCheck ticketId : " + pmTicketEntity.getTicketno());

        boolean isClear = false;

        try {
            isClear = pmAiTicketCheckDtoList.stream()
                    .anyMatch(x -> StringUtils.equals(x.getTrunkName(), pmTicketEntity.getTrunkName()) && StringUtils.equals(x.getDirection(), pmTicketEntity.getDirection())
                            && StringUtils.equals(TicketReasonCode.NODE_TOTAL_DEVIATION_CHANGE.getName(),  pmTicketEntity.getReason()));
            if (isClear) {
                log.info("clearCheck ticketId clear : " + pmTicketEntity.getTicketno());
                pmTicketEntity.setClearDate(pmAiTicketCheckDtoList.get(0).getCollectionDate());
                pmTicketRepository.save(pmTicketEntity);
//                pmTicketRepository.updateTicketClear(pmTicketEntity);
            }
        } catch (PersistenceException | NullPointerException | IllegalArgumentException e) {
            log.error(ExceptionUtils.getMessage(e));
        }
    }

    private PmTicketEntity createTicket(PmAiTicketCheckDto pmAiTicketCheckDto) {
        log.info("createTicket trunkName : " + pmAiTicketCheckDto.getTrunkName() + " direction : " + pmAiTicketCheckDto.getDirection() + " targetSysname : " + pmAiTicketCheckDto.getSysname());

        PmTicketEntity pmTicketEntity = null;

        try {
            pmTicketEntity = PmTicketEntity.builder()
                    .ticketno(pmTicketRepository.nextPmTicketKey())
                    .issueDate(pmAiTicketCheckDto.getCollectionDate())
                    .warningDate(pmAiTicketCheckDto.getWarningDate())
                    .reason(TicketReasonCode.OPTICAL_LEVEL_LONG_TERM_VARIATION_ALERT.getName())
                    .trunkName(pmAiTicketCheckDto.getTrunkName())
                    .direction(pmAiTicketCheckDto.getDirection())
                    .targetSysname(pmAiTicketCheckDto.getSysname())
                    .build();

        } catch (Exception e) {
            log.error(ExceptionUtils.getMessage(e));
        }
        return pmTicketEntity;
    }
}
