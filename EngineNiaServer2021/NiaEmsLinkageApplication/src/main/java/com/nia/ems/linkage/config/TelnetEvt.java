package com.nia.ems.linkage.config;

import com.nia.ems.linkage.common.LinkageCodeInfo;
import com.nia.ems.linkage.data.DataShareBean;
import com.nia.ems.linkage.thread.impl.RoadmEmsThreadImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Queue;

@Component
@Scope(value = "prototype")
public class TelnetEvt {
    private final static org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(TelnetEvt.class);

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
    @Value("${spring.ems.port_evt}")
    private int evtPort;
    @Value("${spring.ems.port_mmc}")
    private int mmcPort;


    public TelnetEvt() {
        super();
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
            socket_to_stdout = streamConnectorObjectFactory.getObject();
            socket_to_stdout.setStream(serverInput, System.out, host, port);
            // 슬롯을 생성한다.
            output_thread = new Thread(socket_to_stdout);
            // 슬롯을 시작한다.
       //     input_thread.start();
            output_thread.start();
        } catch(Exception e) {
            LOGGER.error("=====> [Telnet] main_proc error : "+ExceptionUtils.getStackTrace(e)+" <=====");
            System.exit(1);
        }
    }

    public void closeConnection(){
        try {
            if(serverOutput != null){
                serverOutput.close();
            }
            if(serverInput != null){
                serverInput.close();
            }
            if(serverSocket.isConnected()){
                serverSocket.close();
            }
        }catch (Exception e){
            LOGGER.error("=====> [Telnet] closeConnection error : "+ExceptionUtils.getStackTrace(e)+" <=====");
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

    @Autowired
	private DataShareBean dataShareBean;

    private InputStream src = null;
    private OutputStream dist = null;
    private String host;
    private int port;

    public StreamConnector(){
        super();
    }

    //  Constructor 입출력 스트림을 받아서 처리한다.
    public StreamConnector(InputStream in, OutputStream out){
        src = in;
        dist = out;
    }

    public void setStream(InputStream in, OutputStream out, String host, int port){
        src = in;
        dist = out;
        this.host = host;
        this.port = port;
    }

    //  처리 부분
    //  스트림 읽고 쓰기를 무한히 반복한다.
    public void run(){
        LOGGER.info("=====> [StreamConnector] thread run host("+host+":"+port+") <=====");

        StringBuffer sbEmsMsg = null;
        BufferedReader br;
        String str;
        Boolean isStart = true;
        int bufferSize = 1024;

        try {
            while (isStart) {

                try {
                    br = new BufferedReader(new InputStreamReader(src, "euc-kr"), bufferSize);

                    if(sbEmsMsg == null){
                        sbEmsMsg = new StringBuffer();
                    }

                    while ((str = br.readLine()) != null) {
                        if(StringUtils.isEmpty(str) && sbEmsMsg.length() == 0){
                            continue;
                        }else{
//                            LOGGER.info("=====> [StreamConnector] Evt msg : "+str+"  <=====");
                            if(str.contains(";")){
                                if((sbEmsMsg.toString()).toUpperCase().replaceAll("\\s","").contains("REPTALM")){
                                    ((Queue<StringBuffer>)dataShareBean.getData(LinkageCodeInfo.DATA_SHARE_NAME_EMS_ROADM_ALARM_MSG_QUE)).offer(sbEmsMsg);
                                    LOGGER.info("=====> [StreamConnector] run host("+host+":"+port+") msg : "+sbEmsMsg+" <=====");
                                }
                                sbEmsMsg = new StringBuffer();
                                continue;
                            }
                            sbEmsMsg.append(str+"\n");
                        }
                    }
                }catch(Exception e){
                    isStart = false;
                    ((Queue<String>)dataShareBean.getData(LinkageCodeInfo.DATA_SHARE_NAME_EMS_SOCKET_EVT_RECONNECTION_QUE)).offer(host);
                    LOGGER.error("=====> [StreamConnector] run error(host("+host+":"+port+")) "+ ExceptionUtils.getStackTrace(e)+ "<=====");
                }
            }

            try {
                Thread.sleep(50);
            }catch (InterruptedException e){
                LOGGER.error("=====> [StreamConnector] sendCommand() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
            }
        }catch (Exception e){
            isStart = false;
            ((Queue<String>)dataShareBean.getData(LinkageCodeInfo.DATA_SHARE_NAME_EMS_SOCKET_EVT_RECONNECTION_QUE)).offer(host);
            LOGGER.error("=====> [StreamConnector] run error(host("+host+":"+port+")) "+ ExceptionUtils.getStackTrace(e)+ "<=====");
        }
//        while (isStart) {
//            LOGGER.info("=====> 111111111 <=====");
//            try {
//
//                if(sbEmsMsg == null){
//                    sbEmsMsg = new StringBuffer();
//                }
//
//                while ((str = br.readLine()) != null) {
//                    if(StringUtils.isEmpty(str) && sbEmsMsg.length() == 0){
//                        continue;
//                    }else{
//                        LOGGER.info("=====> [StreamConnector] Evt msg : "+str+"  <=====");
//                        if(str.contains(";")){
//                            if((sbEmsMsg.toString()).toUpperCase().replaceAll("\\s","").contains("REPTALM")){
//                                ((Queue<StringBuffer>)dataShareBean.getData(LinkageCodeInfo.DATA_SHARE_NAME_EMS_ROADM_ALARM_MSG_QUE)).offer(sbEmsMsg);
//                                LOGGER.info("=====> [StreamConnector] run host("+host+":"+port+") msg : "+sbEmsMsg+" <=====");
//                            }
////                                else if((sbEmsMsg.toString()).contains("REPT PM AMPPWR")){
////                                    ((Queue<StringBuffer>)dataShareBean.getData(LinkageCodeInfo.DATA_SHARE_NAME_EMS_ROADM_PERFORMANCE_MSG_QUE)).offer(sbEmsMsg);
////                                    LOGGER.info("=====> [StreamConnector] run host("+host+":"+port+") msg : "+sbEmsMsg+" <=====");
////                                }
//                            sbEmsMsg = new StringBuffer();
//                        }
//                        sbEmsMsg.append(str+"\n");
//                    }
//                  //  LOGGER.info(str);
//                }
//
//                LOGGER.info("=====> isStart 2 : "+isStart+" <=====");
//            }catch(Exception e){
//                isStart = false;
//                ((Queue<String>)dataShareBean.getData(LinkageCodeInfo.DATA_SHARE_NAME_EMS_SOCKET_EVT_RECONNECTION_QUE)).offer(host);
//                LOGGER.error("=====> [StreamConnector] run error(host("+host+":"+port+")) "+ ExceptionUtils.getStackTrace(e)+ "<=====");
//            }
//        }
    }
}