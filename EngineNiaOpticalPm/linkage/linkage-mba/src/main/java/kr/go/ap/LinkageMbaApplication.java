package kr.go.ap;

import kr.go.ap.linkage.mba.amqp.MbaAiModelPrdAmqp;
import kr.go.ap.linkage.mba.service.EmsMmcHdlService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@RequiredArgsConstructor
@EnableScheduling
@SpringBootApplication
@Slf4j
public class LinkageMbaApplication {

	public static void main(String[] args) {
		SpringApplication.run(LinkageMbaApplication.class, args);
	}

}
