package kr.go.ap.linkage.mba.scheduler;

import kr.go.ap.linkage.mba.service.EmsMmcHdlService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LinkageSchdulerService {

    private final EmsMmcHdlService emsMmcHdlService;

    @Scheduled(cron = "0 0/15 * * * *") //초(0-59) 분(0-59) 시간(0-23) 일(1-31) 월(1-12) 요일(0-7)
    public void performanceDaily() {
        emsMmcHdlService.pmCollectHdl();
    }

}
