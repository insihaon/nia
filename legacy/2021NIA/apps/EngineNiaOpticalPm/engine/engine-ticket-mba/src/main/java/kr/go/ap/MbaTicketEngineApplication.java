package kr.go.ap;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@RequiredArgsConstructor
@EnableScheduling
@SpringBootApplication
public class MbaTicketEngineApplication{

	public static void main(String[] args) {
		SpringApplication.run(MbaTicketEngineApplication.class, args);
	}

}
