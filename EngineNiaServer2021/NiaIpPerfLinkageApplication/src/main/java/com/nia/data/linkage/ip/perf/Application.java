package com.nia.data.linkage.ip.perf;

import com.nia.data.linkage.ip.perf.service.PerfDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 
 * @author 
 *
 */
@EnableScheduling
@SpringBootApplication
public class Application implements CommandLineRunner{

	@Autowired
	@Qualifier("PerfDataService")
	private PerfDataService perfDataService;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... arg0) throws Exception {
		perfDataService.getPerfData();
	}
}
