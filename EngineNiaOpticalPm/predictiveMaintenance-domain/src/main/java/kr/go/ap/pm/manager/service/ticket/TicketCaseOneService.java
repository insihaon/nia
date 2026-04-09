package kr.go.ap.pm.manager.service.ticket;


import kr.go.ap.code.pm.ticket.TicketReasonCode;
import kr.go.ap.core.primary.nia.dto.pm.optical.PerformanceDailyDto;
import kr.go.ap.core.primary.nia.entity.pm.ticket.PmTicketEntity;
import kr.go.ap.core.repository.primary.jpa.pm.ticket.PmTicketRepository;
import kr.go.ap.core.repository.primary.mapper.predictive.pm.PmPreMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TicketCaseOneService {

    private final PmTicketRepository pmTicketRepository;

//    private final static String PM_TICKET_REASON = "Node Total Deviation 변화";


    //    private final PmTicketMapper pmTicketMapper;
    private final PmPreMapper pmPreMapper;


    public void ticketCaseOneHdlProcessor() {
        List<PerformanceDailyDto> nodeNtdVarianceList;
        List<PmTicketEntity> matchTicketList;
        PmTicketEntity newPmTicketEntity;

        try {
                nodeNtdVarianceList = pmPreMapper.selectPmAlarmCheckList();

            if (!CollectionUtils.isEmpty(nodeNtdVarianceList)){
                matchTicketList = pmTicketRepository.findByReasonAndClearDateIsNull(TicketReasonCode.NODE_TOTAL_DEVIATION_CHANGE.getName());

                if (!CollectionUtils.isEmpty(matchTicketList)) {
                    for (PerformanceDailyDto nodeNtdVariance : nodeNtdVarianceList) {
                        newPmTicketEntity = ticketSaveCheck(matchTicketList, nodeNtdVariance);

                        if (newPmTicketEntity != null) {
                            log.info("new Ticket : " + newPmTicketEntity.toString());
                            pmTicketRepository.save(newPmTicketEntity);
                        }
                    }

                    for (PmTicketEntity pmTicketDto : matchTicketList) {
                        clearCheck(nodeNtdVarianceList, pmTicketDto);
                    }
                } else {
                    for (PerformanceDailyDto nodeNtdVariance : nodeNtdVarianceList) {
                        newPmTicketEntity = createTicket(nodeNtdVariance);
                        log.info("new Ticket : " + newPmTicketEntity.toString());
                        pmTicketRepository.save(newPmTicketEntity);

                    }
                }
            }
        } catch (NullPointerException | IndexOutOfBoundsException | PersistenceException e) {
            log.error("ticketCaseOneHdlProcessor : " , e);
        } catch (Exception e) {
            log.error("ticketCaseOneHdlProcessor : " , e);
        }
    }

    private PmTicketEntity ticketSaveCheck(List<PmTicketEntity> matchTicketList, PerformanceDailyDto nodeNtdVariance) {
        List<PmTicketEntity> newTicketList;
        PmTicketEntity newTicket = null;

        try {
            newTicketList = matchTicketList.stream().filter(x -> x.getTrunkName().equals(nodeNtdVariance.getTrunkName())
                            && x.getDirection().equals(nodeNtdVariance.getDirection()))
                    .collect(Collectors.toList());

            if (CollectionUtils.isEmpty(newTicketList)) {
                newTicket = createTicket(nodeNtdVariance);
            } else {
                if (!newTicketList.get(0).getTargetSysname().equals(nodeNtdVariance.getSysname())) {
                    log.info("clear Ticket : " + newTicketList.get(0).toString());
                    newTicketList.get(0).setClearDate(nodeNtdVariance.getCollectionDate());
                    pmTicketRepository.save(newTicketList.get(0));
                    newTicket = createTicket(nodeNtdVariance);
                } else {
                    log.info("stay Ticket : " + newTicketList.get(0).toString());
                    return null;
                }
            }
        } catch (NullPointerException | IndexOutOfBoundsException | PersistenceException e) {
            log.error("ticketSaveCheck : " , e);
        } catch (Exception e) {
            log.error("ticketSaveCheck : " , e);
        }
        return newTicket;
    }

    private void clearCheck(List<PerformanceDailyDto> nodeNtdVarianceList, PmTicketEntity pmTicketEntity) {
        log.info("clearCheck ticketId : " + pmTicketEntity.getTicketno());

        boolean isClear = false;

        try {
            isClear = nodeNtdVarianceList.stream()
                    .anyMatch(x -> x.getTrunkName().equals(pmTicketEntity.getTrunkName()) && x.getDirection().equals(pmTicketEntity.getDirection()));

            if (!isClear) {
                log.info("clearCheck ticketId clear : " + pmTicketEntity.getTicketno());
                pmTicketEntity.setClearDate(nodeNtdVarianceList.get(0).getCollectionDate());
                pmTicketRepository.save(pmTicketEntity);
            }
        } catch (NullPointerException | IndexOutOfBoundsException | PersistenceException e) {
            log.error("clearCheck : " , e);
        } catch (Exception e) {
            log.error("clearCheck : " , e);
        }
    }

    private PmTicketEntity createTicket(PerformanceDailyDto performanceDailyDto) {
        log.info("createTicket trunkName : " + performanceDailyDto.getTrunkName() + " direction : " + performanceDailyDto.getDirection() + " targetSysname : " + performanceDailyDto.getSysname());

        PmTicketEntity pmTicketEntity = null;

        try {
            pmTicketEntity = PmTicketEntity.builder()
                    .ticketno(pmTicketRepository.nextPmTicketKey())
                    .issueDate(performanceDailyDto.getCollectionDate())
                    .reason(TicketReasonCode.NODE_TOTAL_DEVIATION_CHANGE.getName())
                    .trunkName(performanceDailyDto.getTrunkName())
                    .direction(performanceDailyDto.getDirection())
                    .targetSysname(performanceDailyDto.getSysname())
                    .build();
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            log.error("createTicket : " , e);
        } catch (Exception e) {
            log.error("createTicket : " , e);
        }
        return pmTicketEntity;
    }
}
