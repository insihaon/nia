package kr.go.ap.engine.ticket.mba.service.schduler;

import kr.go.ap.engine.ticket.mba.service.ticket.TicketPerfService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MbaSchdulerService {

    private final TicketPerfService ticketPerfService;

    @Scheduled(cron = "0 0/5 * * * *") //초(0-59) 분(0-59) 시간(0-23) 일(1-31) 월(1-12) 요일(0-7)
    public void ticketPerformanceDataCheck(){
        ticketPerfService.addPerfData();
    }
}
