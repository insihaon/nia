package kr.go.ap.pm.manager.service.ticket;

import kr.go.ap.pm.manager.service.pm.PmWarningDateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PmTicketService {

    private final TicketCaseOneService ticketCaseOneService;
    private final TicketCaseTwoService ticketCaseTwoService;
    private final PmWarningDateService pmWarningDateService;

    public void ticketHdlProcessor() {
        log.info("ticketHdlProcessor");

        try {
            ticketCaseOneService.ticketCaseOneHdlProcessor();
        } catch (NullPointerException | IndexOutOfBoundsException | PersistenceException e) {
            log.error("ticketHdlProcessor ticketCaseOneHdlProcessor : " + ExceptionUtils.getStackTrace(e));
        } catch (Exception e) {
            log.error("ticketHdlProcessor ticketCaseOneHdlProcessor : " + ExceptionUtils.getStackTrace(e));
        }

        try {
            pmWarningDateService.warningDateCheck();
        } catch (NullPointerException | IndexOutOfBoundsException | PersistenceException e) {
            log.error("ticketHdlProcessor warningDateCheck : " + ExceptionUtils.getStackTrace(e));
        } catch (Exception e) {
            log.error("ticketHdlProcessor warningDateCheck : " + ExceptionUtils.getStackTrace(e));
        }

        try {
            ticketCaseTwoService.ticketCaseTwoHdlProcessor();
        } catch (NullPointerException | IndexOutOfBoundsException | PersistenceException e) {
            log.error("ticketHdlProcessor ticketCaseTwoHdlProcessor : " + ExceptionUtils.getStackTrace(e));
        } catch (Exception e) {
            log.error("ticketHdlProcessor ticketCaseTwoHdlProcessor : " + ExceptionUtils.getStackTrace(e));
        }
    }
}
