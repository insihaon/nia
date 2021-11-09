package com.nia.ems.linkage.service.impl;

import com.nia.ems.linkage.config.TelnetMmc;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "prototype")
public class RoadmEmsTL1Client {
    private static final Logger LOGGER = LoggerFactory.getLogger(RoadmEmsTL1Client.class);

    @Value("${spring.ems.roadm_s_ip}")
    private String roadmSIp;

    @Value("${spring.ems.roadm_d_ip}")
    private String roadmDIp;

    @Value("${spring.ems.port_mmc}")
    private int port;

    @Value("${spring.ems.id}")
    private String id;

    @Value("${spring.ems.pw}")
    private String pw;

    private TelnetMmc telnet;

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<TelnetMmc> telnetObjectFactory;

    public Boolean login(String emsGb) {
        LOGGER.info("=====> [RoadmEmsTL1Client] login <=====");
        Boolean isLoginOk = false;
        String mmcResult;
        int loginCnt = 0;

        try {
            if(telnet != null && telnet.isConnected()){
                telnet.sendCommand("canc-user:::1:"+id+";", false);
                telnet.closeConnection();
                telnet = null;
            }else if(telnet != null){
                telnet = null;
            }

            telnet = telnetObjectFactory.getObject();

            if("D".equals(emsGb)){
                telnet.setHostPort(roadmDIp, port);
            }else if("S".equals(emsGb)){
                telnet.setHostPort(roadmSIp, port);
            }

            telnet.openConnection();
            telnet.main_proc();

            if(telnet.isConnected()){
                mmcResult = telnet.sendCommand("\r", false);
                Thread.sleep(500);

                if(mmcResult.contains("LOGIN:")){

                    mmcResult = telnet.sendCommand( id+"\r", false);
                    Thread.sleep(500);

                    if(mmcResult.contains("PASSWORD:")) {
                        mmcResult = telnet.sendCommand( pw+"\r", false);

                        if(mmcResult.contains("TL1>")) {
                            isLoginOk = true;
                        }else if(mmcResult.contains("LOGIN:")){
                            telnet.closeConnection();
                            telnet = null;

                            if(loginCnt == 3){
                                return false;
                            }
                            Thread.sleep(3000);
                            loginCnt++;
                            login(emsGb);
                        }
                    }
                }else{
                    telnet.closeConnection();
                    telnet = null;

                    if(loginCnt == 3){
                        return false;
                    }
                    Thread.sleep(3000);
                    loginCnt++;
                    login(emsGb);
                }
            }
        }catch (Exception e){
            LOGGER.error("=====> [RoadmEmsTL1Client] login error() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
        }
        return isLoginOk;
    }

    public Boolean sendCommand(String command, Boolean isFlush){
        try {
            if(telnet != null && telnet.isConnected()){
                telnet.sendCommand(command, isFlush);
                return true;
            }else{
                return false;
            }
        }catch (Exception e){
            return false;
        }
    }

    public Boolean isConnected(){
        if(telnet == null){
            return false;
        }else{
            return telnet.isConnected();
        }
    }

    public void logout(){
        if(telnet != null && telnet.isConnected()){
            telnet.sendCommand("canc-user:::1:"+id+";", false);
            telnet.closeConnection();
            telnet = null;
        }else{
            if(telnet != null){
                telnet.closeConnection();
                telnet = null;
            }
        }
    }

}
