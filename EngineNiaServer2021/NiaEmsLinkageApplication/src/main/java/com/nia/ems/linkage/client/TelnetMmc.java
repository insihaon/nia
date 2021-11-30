package com.nia.ems.linkage.client;

import com.nia.ems.linkage.common.LinkageCodeInfo;
import com.nia.ems.linkage.data.DataShareBean;
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
public class TelnetMmc {
    private static final Logger LOGGER = LoggerFactory.getLogger(TelnetMmc.class);

    private Socket serverSocket;//       접속용 소켓
    private InputStream serverInput;//   네트워크 입력용 스트림
    private OutputStream serverOutput;//   네트워크 출력용 스트림
    private String host;//       접속 서버의  주소
    private int port;   //        접속 서버의 포트 번호
    private Boolean isStart = false;
    private StreamConnectorMmc socket_to_stdout = null;
    private Thread output_thread = null;

    @Autowired
    private DataShareBean dataShareBean;

    @Autowired
	private org.springframework.beans.factory.ObjectFactory<StreamConnectorMmc> streamConnectorMmcObjectFactory;

    public TelnetMmc() {
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
        serverInput = serverSocket.getInputStream();
        serverOutput = serverSocket.getOutputStream();
    }

    public Boolean isConnected(){
        if(serverSocket != null){
            if(socket_to_stdout.isConnected() && serverSocket.isConnected()){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }

    public void main_proc() {
        output_thread = null;

        try {
            socket_to_stdout = streamConnectorMmcObjectFactory.getObject();
            socket_to_stdout.setStream(this, serverInput, System.out, host, port);
            // 슬롯을 생성한다.
            output_thread = new Thread(socket_to_stdout);
            // 슬롯을 시작한다.
            output_thread.start();

        } catch(Exception e) {
            LOGGER.error("=====> [TelnetMmc] main_proc error : "+ExceptionUtils.getStackTrace(e)+" <=====");
            System.exit(1);
        }
    }

    public String sendCommand(String command, Boolean isFlush) {
        LOGGER.info("=====> [TelnetMmc] sendCommand("+command+") <=====");
        String msg = null;
        OutputStream output;

		try {
			if(serverSocket.isConnected()) {
                output = new PrintStream(serverOutput, isFlush, "ASCII");
                output.write(command.getBytes());
                output.flush();

                LOGGER.info("=====> [TelnetMmc] sendCommand :  "+ command+ " OK <=====");

                try {
                    Thread.sleep(1000);
                }catch (InterruptedException e){
                    LOGGER.error("=====> [TelnetMmc] sendCommand() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
                }

                isStart = true;
                msg = readStream();
                isStart = false;
            }
		} catch (Exception e) {
            LOGGER.error("=====> [TelnetMmc] sendCommand() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
		}
		return msg;
	}

	public String readStream(){
        String msg = null;

        try {
            while (serverSocket.isConnected()){
                if(!((Queue<String>)dataShareBean.getData(LinkageCodeInfo.DATA_SHARE_NAME_EMS_MMC_QUE)).isEmpty()){
                    msg = ((Queue<String>)dataShareBean.getData(LinkageCodeInfo.DATA_SHARE_NAME_EMS_MMC_QUE)).poll();

                    if(msg.contains("LOGIN:") || msg.contains("PASSWORD:") || msg.contains("TL1>") || msg.contains("canc-user")){
                        return msg;
                    }
                }
                try {
                    Thread.sleep(50);
                }catch (InterruptedException e){
                    LOGGER.error("=====> [TelnetMmc] sendCommand() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
                }
            }
        }catch (Exception e){
            LOGGER.error("=====> [TelnetMmc] readStream error : "+ExceptionUtils.getStackTrace(e)+" <=====");
        }
        return msg;
    }

    public void closeConnection(){
        try {
            if(serverSocket != null && serverSocket.isConnected()){
                socket_to_stdout.setStart(false);
                socket_to_stdout = null;
                serverSocket.close();
                serverSocket = null;
                output_thread.interrupt();
            }else{
                socket_to_stdout.setStart(false);
                socket_to_stdout = null;
                serverSocket = null;
                output_thread.interrupt();
            }
        }catch (Exception e){
            LOGGER.error("=====> [TelnetMmc] closeConnection error : "+ExceptionUtils.getStackTrace(e)+" <=====");
        }
    }
}