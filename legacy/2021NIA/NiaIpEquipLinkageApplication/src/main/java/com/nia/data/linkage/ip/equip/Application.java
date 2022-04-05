package com.nia.data.linkage.ip.equip;

import com.nia.data.linkage.ip.equip.service.CvnmsResourceIfService;
import com.nia.data.linkage.ip.equip.service.CvnmsResourceService;
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
	@Qualifier("CvnmsResourceIfService")
	private CvnmsResourceIfService cvnmsResourceIfService;

	@Autowired
	@Qualifier("CvnmsResourceService")
	private CvnmsResourceService cvnmsResourceService;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... arg0) throws Exception {
//		cvnmsResourceService.getCvnmsResourceData();
//		cvnmsResourceIfService.getCvnmsResourceIfData();

	}
}
