package com.nia.korenrca.service.util;

import com.nia.korenrca.shared.ConfigProperties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.jcraft.jsch.*;
import com.nia.korenrca.server.http.route.HttpRouteMatcher;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SSHUtil extends Thread {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");

    public static String ping(String ip, String user_id) {
        Date date = new Date();
        String host = ConfigProperties.get().getPingHost();
        int port = Integer.parseInt(ConfigProperties.get().getPingPort());
        String username = ConfigProperties.get().getPingUsername();
        String password = ConfigProperties.get().getPingPassword();

        StringBuilder sb = new StringBuilder();

        String fileName = ip + "_" + user_id;
        String command = "ping " + ip + " -w 5 -q";

        LOGGER.info("======> Connecting to " + host + ":" + port);
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
            InputStream in = channelExec.getInputStream(); // 입력 스트림 가져오기
            channelExec.connect();

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n"); // 결과를 StringBuilder에 추가
            }
            
            while (channel.isConnected()) {
                Thread.sleep(5000);
            }
        } catch (JSchException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            if (channel != null) {
                channel.disconnect();
            }
            if (session != null) {
                session.disconnect();
            }

            return sb.toString();
        }
    }

    public static void main(String[] args)  {
        ping("116.89.161.24", "test");
    }
}