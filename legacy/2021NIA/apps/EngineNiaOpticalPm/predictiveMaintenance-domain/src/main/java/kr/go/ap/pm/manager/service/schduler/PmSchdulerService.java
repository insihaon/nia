package kr.go.ap.pm.manager.service.schduler;

import kr.go.ap.pm.manager.service.pm.PmDailyService;
import kr.go.ap.pm.manager.service.pm.PmWarningDateService;
import kr.go.ap.pm.manager.service.ticket.PmTicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PmSchdulerService {

    private final PmDailyService pmDailyService;
    private final PmTicketService pmTicketService;
    private final PmWarningDateService pmWarningDateService;

    @Scheduled(cron = "0 5 1 * * *") //초(0-59) 분(0-59) 시간(0-23) 일(1-31) 월(1-12) 요일(0-7)
    public void performanceDaily(){
        pmDailyService.performanceDailyHdl();
    }
    @Scheduled(cron = "0 5 2 * * *") //초(0-59) 분(0-59) 시간(0-23) 일(1-31) 월(1-12) 요일(0-7)
    public void performanceTicketCheck(){
        pmWarningDateService.warningDateCheck();
        pmTicketService.ticketHdlProcessor();
    }
}
