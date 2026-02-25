package kr.go.ap;

import kr.go.ap.pm.manager.service.model.AiPmDataSendService;
import kr.go.ap.pm.manager.service.pm.PmDailyService;
import kr.go.ap.pm.manager.service.pm.PmWarningDateService;
import kr.go.ap.pm.manager.service.ticket.PmTicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@RequiredArgsConstructor
@EnableScheduling
@SpringBootApplication
public class predictiveMaintenanceApplication implements CommandLineRunner{

	public static void main(String[] args) {
		SpringApplication.run(predictiveMaintenanceApplication.class, args);
	}

    private final AiPmDataSendService aiPmDataSendService;
    private final PmWarningDateService pmWarningDateService;
    private final PmTicketService pmTicketService;
    private final PmDailyService pmDailyService;

	@Override
	public void run(String... arg0) throws Exception {
//        aiPmDataSendService.sendPmData();
//        pmDailyService.performanceDailyHdl();
//        pmWarningDateService.warningDateCheck();
        pmTicketService.ticketHdlProcessor();


	}
}
