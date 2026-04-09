package com.nia.data.linkage.config;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.io.*;
import java.nio.charset.Charset;

import org.apache.commons.net.telnet.TelnetClient;

@Service("Tl1")
public class TelnetSample {
    private static final Logger LOGGER = LoggerFactory.getLogger(TelnetSample.class);

    private TelnetClient telnet = new TelnetClient();
    private InputStream in = null;

    public TelnetSample() {
        super();
    }

    public void sendCommand(TelnetClient client, String command, Boolean isFlush) throws RuntimeException {
        OutputStream out;
        String msg;

		try {
			if(client.isAvailable() && client.isConnected()) {

			    if(StringUtils.isNotEmpty(command)){
                    char[] chars = (command).toCharArray();
                    StringBuffer hex = new StringBuffer();
                    for(int i = 0; i < chars.length; i++){
                        hex.append(Integer.toHexString((int) chars[i]));
                    }
                    System.out.println("String -> hex : "+hex.toString());
                }
//                String hexString = hex.toString();
//
//                hexString = hexString.replaceAll("a", "");
//                System.out.println("substring : " + hexString);
//                byte[] bytes = Hex.decodeHex(hexString.toCharArray());
//                msg = new String(bytes, "ASCII");
//                System.out.println("hex -> String : " + msg);

                out = new PrintStream(client.getOutputStream(), isFlush, "UTF-8");
                out.write(command.getBytes());
                out.flush();
			}
		} catch (Exception e) {
            LOGGER.error("=====> [TelnetSample] sendCommand() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
		}
	}

    public void telnetConnect(String server, int port, String user, String password){
        byte[] data;
        int bufferSize = 2048;
        int cnt = 0;
        String line = null;
        StringBuffer sbTemp;
		try {
            // telnet 서버로의 접속
            telnet.connect(server, port);
            telnet.setCharset(Charset.forName("EUC-KR"));
            // 참조할 input, output stream 객체 획득
            in = telnet.getInputStream();
            sendCommand(telnet, "\r", false);

            sbTemp = new StringBuffer();

            Thread.sleep(500);

            data = new byte[bufferSize];
            cnt = in.read(data, 0, bufferSize);
            if(cnt > 0) {
                line = new String(data, 0, cnt, "EUC-KR");
                sbTemp.append(line);
            }

            System.out.println(sbTemp.toString());

            sendCommand(telnet, user, false);

          //  sbTemp = new StringBuffer();

            data = new byte[bufferSize];
            cnt = in.read(data, 0, bufferSize);
            if(cnt > 0) {
                line = new String(data, 0, cnt, "EUC-KR");
                sbTemp.append(line);
            }

            System.out.println(sbTemp.toString());

            sendCommand(telnet, "\r", false);

          //  sbTemp = new StringBuffer();

            Thread.sleep(500);

            data = new byte[bufferSize];
            cnt = in.read(data, 0, bufferSize);
            if(cnt > 0) {
                line = new String(data, 0, cnt, "EUC-KR");
                sbTemp.append(line);
            }

            System.out.println(sbTemp.toString());

            sendCommand(telnet, password, false);

         //   sbTemp = new StringBuffer();

            Thread.sleep(500);

            data = new byte[bufferSize];
            cnt = in.read(data, 0, bufferSize);
            if(cnt > 0) {
                line = new String(data, 0, cnt, "EUC-KR");
                sbTemp.append(line);
            }

            System.out.println(sbTemp.toString());

            sendCommand(telnet, "\r", false);

            //sbTemp = new StringBuffer();

            Thread.sleep(500);

            data = new byte[bufferSize];
            cnt = in.read(data, 0, bufferSize);
            if (cnt > 0) {
                line = new String(data, 0, cnt, "EUC-KR");
                sbTemp.append(line);
            }

            System.out.println(sbTemp.toString());
        } catch (Exception e) {
            LOGGER.error("=====> [TelnetSample] telnetConnect() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
        }
    }

    /**
     * Telnet 접속을 해제한다.
     */
    public void disconnect() {
        try {
            telnet.disconnect();
        } catch (Exception e) {
            LOGGER.error("=====> [TelnetSample] disconnect() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
        }
    }
}