package kr.go.ap.pm.manager.service.schduler;

import kr.go.ap.pm.manager.service.pm.PmDailyService;
import kr.go.ap.pm.manager.service.pm.PmWarningDateService;
import kr.go.ap.pm.manager.service.ticket.PmTicketService;
import lombok.RequiredArgsConstructor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest
@RequiredArgsConstructor
@Transactional
@ActiveProfiles("local")
@RunWith(SpringRunner.class)
public class PmSchdulerServiceTest {

    private final PmDailyService pmDailyService;
    private final PmTicketService pmTicketService;
    private final PmWarningDateService pmWarningDateService;

    @Test
    public void performanceDaily() {
        pmDailyService.performanceDailyHdl();
    }

    @Test
    public void performanceTicketCheck() {
        pmWarningDateService.warningDateCheck();
        pmTicketService.ticketHdlProcessor();

    }
}