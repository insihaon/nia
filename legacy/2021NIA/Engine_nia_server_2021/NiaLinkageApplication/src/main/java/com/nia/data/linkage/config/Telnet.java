package com.nia.data.linkage.config;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Queue;

@Component
@Scope(value = "prototype")
public class Telnet {
    private final static org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(Telnet.class);

    @Autowired
	private org.springframework.beans.factory.ObjectFactory<StreamConnector> streamConnectorObjectFactory;

    private Socket serverSocket;//       접속용 소켓
    private OutputStream serverOutput;//   네트워크 출력용 스트림
    private BufferedInputStream serverInput;//   네트워크 입력용 스트림
    private String host;//       접속 서버의  주소
    private int port;   //        접속 서버의 포트 번호
    private Thread input_thread;
    private Thread output_thread;
    private StreamConnector stdin_to_socket;
    private StreamConnector socket_to_stdout;

    public Telnet() {
        super();
    }

    // Constructor(1) 주소와 포트 번호가 지정된 경우
    public Telnet(String host, int port){
        this.host = host;
        this.port = port;
    }

    public void setHostPort(String host, int port){
        this.host = host;
        this.port = port;
    }

    // openConnection  메소드
    // 주소와 포트 번호로부터 소켓을 만들고 스트림을 작성한다.
    public void openConnection() throws IOException, UnknownHostException{
        serverSocket = new Socket(host, port);
        serverOutput = serverSocket.getOutputStream();
        serverInput = new BufferedInputStream(serverSocket.getInputStream());
        // 접속 상대방이 telnet 포트번호이므로 협상을 행한다.
    }

    // main_proc 메소드
    // 네트워크와의 통신을 행하는 슬롯을 시작시킨다.
    public void main_proc() throws IOException {
        try {
            // 슬롯용 클래스 StreamConnector의 객체를 생성한다.
           // StreamConnector stdin_to_socket = new StreamConnector(System.in, serverOutput);
            stdin_to_socket = streamConnectorObjectFactory.getObject();
            stdin_to_socket.setStream(System.in, serverOutput, host);
            //StreamConnector socket_to_stdout = new StreamConnector(serverInput, System.out);
            socket_to_stdout = streamConnectorObjectFactory.getObject();
            socket_to_stdout.setStream(serverInput, System.out, host);
            // 슬롯을 생성한다.
            input_thread = new Thread(stdin_to_socket);
            output_thread = new Thread(socket_to_stdout);
            // 슬롯을 시작한다.
            input_thread.start();
            output_thread.start();

        } catch(Exception e) {
            LOGGER.error("=====> [Telnet] main_proc error : "+ExceptionUtils.getStackTrace(e)+" <=====");
            System.exit(1);
        }
    }
}

// StreamConnector 클래스
// 스트림을 받아서 처리한 후 양자를 통합하여 데이터를 받아서 넘겨준다.
// StreamConnector 클래스는 슬롯을 구성하기 위한 클래스이다.
@Component
@Scope(value = "prototype")
class StreamConnector implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(StreamConnector.class);

    private InputStream src = null;
    private OutputStream dist = null;
    private String host;

    public StreamConnector(){
        super();
    }

    //  Constructor 입출력 스트림을 받아서 처리한다.
    public StreamConnector(InputStream in, OutputStream out){
        src = in;
        dist = out;
    }

    public void setStream(InputStream in, OutputStream out, String host){
        src = in;
        dist = out;
        this.host = host;
    }

    //  처리 부분
    //  스트림 읽고 쓰기를 무한히 반복한다.
    public void run(){
        LOGGER.info("=====> [StreamConnector] thread run host("+host+") <=====");

        byte[] buff = new byte[1024];
        StringBuffer sbEmsMsg = null;
        BufferedReader br;
        String str;
        Boolean isStart = true;

        while (isStart) {

            try {
                br = new BufferedReader(new InputStreamReader(src, "euc-kr"), 8192);

                if(sbEmsMsg == null){
                    sbEmsMsg = new StringBuffer();
                }

                while ((str = br.readLine()) != null) {
                    if(StringUtils.isEmpty(str) && sbEmsMsg.length() == 0){
                        continue;
                    }else{
                        if(";".equals(str)){
                            LOGGER.info("=====> [StreamConnector] run host : "+host+" msg : "+sbEmsMsg+" <=====");
                            sbEmsMsg = new StringBuffer();
                            continue;
                        }
                        sbEmsMsg.append(str+"\n");
                    }
                  //  LOGGER.info(str);
                }

                Thread.sleep(50);
            }catch(Exception e){
                isStart = false;
                LOGGER.error("=====> [StreamConnector] run error("+host+") "+ ExceptionUtils.getStackTrace(e)+ "<=====");
            }
        }
    }
}