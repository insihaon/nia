package com.nia.data.linkage.ipsdn;

import com.nia.data.linkage.ipsdn.service.ipsdn.alarm.IpSdnAlarmToAiLinkageService;
import com.nia.data.linkage.ipsdn.service.ipsdn.factor.IpSdnFactorLinkageService;
import com.nia.data.linkage.ipsdn.service.ipsdn.resource.IpSdnResourceLinkageService;
import com.nia.data.linkage.ipsdn.service.ipsdn.sflow.IpSdnSflowLinkageService;
import com.nia.data.linkage.ipsdn.service.ipsdn.syslog.IpSdnSyslogLinkageService;
import com.nia.data.linkage.ipsdn.service.ipsdn.traffic.IpSdnTrafficLinkageService;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableEncryptableProperties
@SpringBootApplication
public class Application implements CommandLineRunner {


    @Autowired
    @Qualifier("IpSdnResourceLinkageService")
    private IpSdnResourceLinkageService ipSdnResourceLinkageService;

    @Autowired
    @Qualifier("IpSdnFactorLinkageService")
    private IpSdnFactorLinkageService ipSdnFactorLinkageService;

    @Autowired
    @Qualifier("IpSdnTrafficLinkageService")
    private IpSdnTrafficLinkageService ipSdnTrafficLinkageService;

    @Autowired
    @Qualifier("IpSdnSyslogDataAiLinkageService")
    private IpSdnSyslogLinkageService ipSdnSyslogLinkageService;

    @Autowired
    @Qualifier("IpSdnSflowDataAiLinkageService")
    private IpSdnSflowLinkageService ipSdnSflowLinkageService;

    @Autowired
    @Qualifier("IpSdnAlarmToAiLinkageService")
    private IpSdnAlarmToAiLinkageService ipSdnAlarmToAiLinkageService;


    @Autowired
    @Qualifier("EntityService")


    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... arg0) throws Exception {

//        ipSdnResourceLinkageService.sendNodeData();
//        ipSdnResourceLinkageService.sendLinkData();
//        ipSdnResourceLinkageService.sendInterfaceData();

//        ipSdnFactorLinkageService.sendFactorData();
//        ipSdnTrafficLinkageService.sendTrafficData();

//        ipSdnSflowLinkageService.sendSflowData();
//         ipSdnSyslogLinkageService.sendSyslogData();

//        ipSdnAlarmToAiLinkageService.sendAlarmData();
    }
}