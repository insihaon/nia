package com.nia.ems.linkage.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.net.telnet.TelnetClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;

@Slf4j
@Component
@Scope(value = "prototype")
public class RoadmEmsTNClient {

    @Value("${spring.ems.roadm_s_ip}")
    private String roadmSIp;
    @Value("${spring.ems.roadm_d_ip}")
    private String roadmDIp;
    @Value("${spring.ems.port_mmc}")
    private int port;


    private Socket serverSocket;
    private TelnetClient telnet = new TelnetClient();
    private InputStream in = telnet.getInputStream();
    private PrintStream out;
    private String prompt = "TL1>";

    public void initD () {
        try {
            // Connect to the specified server
            log.info("ip :" + roadmDIp + " , port :" + port);
            telnet.connect(roadmDIp, port);
            in = telnet.getInputStream();
            out = new PrintStream(telnet.getOutputStream(), false, "ASCII");
        } catch (IOException e) {
            log.error(ExceptionUtils.getStackTrace(e));
            disconnect();
        }
    }

    public String sendCommand (String command) {
        try {
            write(command);
            return readUntil(prompt);
        } catch (IOException e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }
        return null;
    }

    public void write (String value) throws IOException {
        if (StringUtils.isNotEmpty(value)) {
//            out.write(value.getBytes("ASCII"));
//            out.flush();
            byte[] asciiBytes = value.getBytes("ASCII");
            out.write(asciiBytes);
            out.flush();
//            log.info("=====> [RoadmEmsTNClient] write : " + value.replace("\r", "\\r").replace("\n", "\\n") + " OK <=====");
//            log.info("ASCII bytes sent: " + Arrays.toString(asciiBytes)); // ASCII 바이트 배열 출력
        }
    }

//    public String readUntil (String pattern) {
//        char lastChar = pattern.charAt(pattern.length() - 1);
//        char ch;
//        StringBuffer sb = null;
//        try {
//            sb = new StringBuffer();
//            ch = (char) in.read();
//            log.info("[readUntil] ch >>>> " + ch);
//            while (true) {
//                sb.append(ch);
//                if (ch == lastChar) {
//                    if (sb.toString().endsWith(pattern)) {
//                        log.info("[readUntil] return success >>>>> " + sb.toString());
//                        return sb.toString();
//                    }
//                }
//                ch = (char) in.read();
//            }
//        } catch (NullPointerException e) {
//            log.error(ExceptionUtils.getStackTrace(e));
//        } catch (IOException e) {
//            log.error(ExceptionUtils.getStackTrace(e));
//        }
//        return null;
//    }

    public String readUntil (String pattern) {
        char lastChar = pattern.charAt(pattern.length() - 1);
        char ch;
        StringBuffer sb = null;

        try {
            sb = new StringBuffer();
            if (in == null) {
                log.info("[readUntil] in null <<<<<");
            } else {
                ch = (char) in.read();
                log.info("[readUntil] ch >>>> " + ch);
                while (true) {
//                    log.info("[readUntil] ch : " + ch + "\n");
                    sb.append(ch);
                    if (ch == lastChar) {
                        log.info("[readUntil] sb1 : " + sb.toString());
                        if (sb.toString().endsWith(pattern)) {
                            log.info("[readUntil] sb2 : " + sb.toString());
                            return sb.toString();
                        }
                    }
                    ch = (char) in.read();
                }
            }

        } catch (NullPointerException e) {
            log.error(ExceptionUtils.getStackTrace(e));
        } catch (IOException e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }
        return null;
    }


    public void disconnect () {
        try {
            telnet.disconnect();
        } catch (IOException e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }
    }

}
