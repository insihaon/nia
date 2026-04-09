package com.codej.nia.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.codej.base.dto.AppDto;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import lombok.extern.slf4j.Slf4j;

import java.io.File;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

@Slf4j
public class SSHUtil extends Thread {

    public static String ping(AppDto appDto, HashMap<String, Object> param) {
        String ip = (String) param.get("ip");
        String user_id = (String) param.get("user_id");

        String host = appDto.getSshHost();
        int port = appDto.getSshPort();

        String username = appDto.getSshUser();
        String password = appDto.getSshPassword();

        String fileName = String.format("%s_%s.txt", ip, user_id);
        String command = String.format("ping %s -w 5 -q > ~/web/file-access/ping/%s", ip, fileName);

        log.debug("======> Connecting to {}:{}", host, port);
        Session session = null;
        Channel channel = null;

        try {
            JSch jsch = new JSch();
            session = jsch.getSession(username, host, port);

            session.setPassword(password);

            // 세션과 관련된 정보를 설정
            java.util.Properties config = new java.util.Properties();

            // 호스트 정보를 검사하지 않는다
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();
            channel = session.openChannel("exec");

            // 채널을 SSH용 채널 객체로 캐스팅
            ChannelExec channelExec = (ChannelExec) channel;
            channelExec.setCommand(command);
            channelExec.connect();
            while (channel.isConnected()) {
                Thread.sleep(5000);
            }
            if (channel != null) {
                channel.disconnect();
            }
            if (session != null) {
                session.disconnect();
            }
        } catch (JSchException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileName;
    }
}
