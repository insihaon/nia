package com.nia.data.linkage.ipsdn.service.impl;

import com.nia.data.linkage.ipsdn.service.ipsdn.alarm.IpSdnAlarmToAiLinkageService;
import com.nia.data.linkage.ipsdn.service.ipsdn.factor.IpSdnFactorLinkageService;
import com.nia.data.linkage.ipsdn.service.ipsdn.resource.IpSdnResourceLinkageService;
import com.nia.data.linkage.ipsdn.service.ipsdn.sflow.IpSdnSflowLinkageService;
import com.nia.data.linkage.ipsdn.service.ipsdn.syslog.IpSdnSyslogLinkageService;
import com.nia.data.linkage.ipsdn.service.ipsdn.traffic.IpSdnTrafficLinkageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class NiaAiLinkageSchdulerServiceImpl {


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


    // Resource Node정보 - 1
    @Scheduled(cron = "0 0 3 * * *") //초(0-59) 분(0-59) 시간(0-23) 일(1-31) 월(1-12) 요일(0-7)
    public void conJobIpsdnNodeData() {ipSdnResourceLinkageService.sendNodeData();}

    // Resource Interface정보 - 2
    @Scheduled(cron = "0 10 3 * * *") //초(0-59) 분(0-59) 시간(0-23) 일(1-31) 월(1-12) 요일(0-7)
    public void conJobIpsdnInterfaceData() {ipSdnResourceLinkageService.sendInterfaceData();}

    // Resource Link정보 - 3
    @Scheduled(cron = "0 50 2 * * *") //초(0-59) 분(0-59) 시간(0-23) 일(1-31) 월(1-12) 요일(0-7)
    public void conJobIpsdnLinkData() {ipSdnResourceLinkageService.sendLinkData();}

    // Factor
    @Scheduled(cron = "0 0/1 * * * *") //초(0-59) 분(0-59) 시간(0-23) 일(1-31) 월(1-12) 요일(0-7)
    public void conJobIpsdnFactorData() {ipSdnFactorLinkageService.sendFactorData();}

    // Traffic
    @Scheduled(cron = "0 0/1 * * * *") //초(0-59) 분(0-59) 시간(0-23) 일(1-31) 월(1-12) 요일(0-7)
    public void conJobIpsdnTrafficData() {ipSdnTrafficLinkageService.sendTrafficData();}

    // Sflow
    @Scheduled(cron = "0 0/1 * * * *") //초(0-59) 분(0-59) 시간(0-23) 일(1-31) 월(1-12) 요일(0-7)
    public void conJobIpsdnSflowData() {ipSdnSflowLinkageService.sendSflowData();}

    // Syslog 수집 정보
    @Scheduled(cron = "0 0/1 * * * *") //초(0-59) 분(0-59) 시간(0-23) 일(1-31) 월(1-12) 요일(0-7)
    public void conJobIpsdnSyslogData() {ipSdnSyslogLinkageService.sendSyslogData();}

    // Syslog 티켓 발행 정보 전송
    @Scheduled(cron = "0 0/1 * * * *") //초(0-59) 분(0-59) 시간(0-23) 일(1-31) 월(1-12) 요일(0-7)
    public void conJobIpsdnSyslogAlarm() {ipSdnAlarmToAiLinkageService.sendAlarmData();}







}
