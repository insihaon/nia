package com.nia.engine.common;

import com.jcraft.jsch.*;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@Component
public class SSHSession {
    private final Logger LOGGER = Logger.getLogger(SSHSession.class);

    private Session session = null;
    private Channel channel = null;

//    @Value("${spring.ssh.host}")
//    private String host = null;


    private ChannelExec channelExec;

    public void init(int port, String user, String pw,String host){

        try{
            JSch jsch = new JSch();

            session = jsch.getSession(user, host, port);
            // 3. 패스워드를 설정한다.
            session.setPassword(pw);

            // 4. 세션과 관련된 정보를 설정한다.
            java.util.Properties config = new java.util.Properties();

            // 4-1. 호스트 정보를 검사하지 않는다.
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);

            // 5. 접속한다.
            session.connect();

            // 6. sftp 채널을 연다.
            channel = session.openChannel("exec");

            // 8. 채널을 SSH용 채널 객체로 캐스팅한다
            channelExec = (ChannelExec) channel;
        } catch (NullPointerException e) {
            LOGGER.error(">>>>>>>>>>>> SshSession init() NullPointerException error : "+ ExceptionUtils.getStackTrace(e)+" <<<<<<<<<<<<<<<<");
        }catch (Exception e){
            LOGGER.error(">>>>>>>>>>>> SshSession init()  error : "+ ExceptionUtils.getStackTrace(e)+" <<<<<<<<<<<<<<<<");
        }
    }

    /** * 명령어를 실행 시킨다. * * @param command * 실행시킬 명령어 */
    public String exec(String command) {
        String output = "";

        try {
            //실행할 명령어를 설정한다.
            channelExec.setCommand(command);
            OutputStream out = channelExec.getOutputStream();
            InputStream in = channelExec.getInputStream();
            InputStream err = channelExec.getErrStream();
            // 명령어를 실행한다.
            channelExec.connect(15000);
//            LOGGER.info("sendCommand : " + command);
            byte[] buf = new byte[1024];
            int length;

            while ((length = in.read(buf)) != -1){
                output += new String(buf,0,length);
               //  LOGGER.info("=== command result : " + new String(buf,0,length));
            } if(output.trim().matches(".*invalid.*")) {//invalid 형태이면
                LOGGER.info("=== invalid command.. : " + output.trim());
            } else if(output.trim().matches(".*command not found.*")) {
                LOGGER.info("=== invalid command.. : " + output.trim()); //command not found 형태이면
            }
        } catch (JSchException e) {
            LOGGER.error(">>>>>>>>>>>> SshSession exec() JSchException error : "+ ExceptionUtils.getStackTrace(e)+" <<<<<<<<<<<<<<<<");
        } catch (IOException e) {
            LOGGER.error(">>>>>>>>>>>> SshSession exec() IOException error : "+ ExceptionUtils.getStackTrace(e)+" <<<<<<<<<<<<<<<<");
        } return output;
    }

    public void disconnection() {
        try {
            if (channelExec != null) {
                channelExec.disconnect();
            }
            if (session != null) {
                session.disconnect();
            }
        } catch (NullPointerException e) {
            LOGGER.error(">>>>>>>>>>>> SshSession disconnection() NullPointerException error : "+ ExceptionUtils.getStackTrace(e)+" <<<<<<<<<<<<<<<<");
        }catch (Exception e){
            LOGGER.error(">>>>>>>>>>>> SshSession disconnection()  error : "+ ExceptionUtils.getStackTrace(e)+" <<<<<<<<<<<<<<<<");
        }
    }

}
