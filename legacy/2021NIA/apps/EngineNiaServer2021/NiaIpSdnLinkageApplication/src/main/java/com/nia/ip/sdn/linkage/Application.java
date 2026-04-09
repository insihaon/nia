package com.nia.ip.sdn.linkage;

import com.nia.ip.sdn.linkage.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class Application implements CommandLineRunner{

	@Autowired
	@Qualifier("InterfaceService")
	private InterfaceService interfaceService;

	@Autowired
	@Qualifier("LinkService")
	private LinkService linkService;

	@Autowired
	@Qualifier("NodeService")
	private NodeService nodeService;

	@Autowired
	@Qualifier("LinkTrafficeService")
	private LinkTrafficeService linkTrafficeService;

	@Autowired
	@Qualifier("NodeFactorService")
	private NodeFactorService nodeFactorService;

	@Autowired
	@Qualifier("E2eNodeService")
	private E2eNodeService e2eNodeService;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... arg0) throws Exception {
	}
}
