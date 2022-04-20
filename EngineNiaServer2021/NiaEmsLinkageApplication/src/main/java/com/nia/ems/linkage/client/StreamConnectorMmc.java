package com.nia.ems.linkage.client;

import com.nia.ems.linkage.common.LinkageCodeInfo;
import com.nia.ems.linkage.data.DataShareBean;
import com.nia.ems.linkage.thread.NiaEmsLinkageThread;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.util.Queue;

@Component
@Scope(value = "prototype")
public class StreamConnectorMmc implements NiaEmsLinkageThread {
    private static final Logger LOGGER = LoggerFactory.getLogger(StreamConnectorMmc.class);

    @Autowired
    private DataShareBean dataShareBean;

    private InputStream src = null;
    private OutputStream dist = null;
    private String host;
    private int port;
    private Boolean isStart = true;
    private Boolean isConnection = true;
    private TelnetMmc telnetMmc;

    public StreamConnectorMmc(){
        super();
    }

    public void setStream(TelnetMmc telnetMmc, InputStream in, OutputStream out, String host, int port){
        this.src = in;
        this.dist = out;
        this.host = host;
        this.port = port;
        this.telnetMmc = telnetMmc;
    }

    //  처리 부분
    //  스트림 읽고 쓰기를 무한히 반복한다.
    @Override
    public void run(){
        LOGGER.info("=====> [StreamConnectorMmc] thread run host("+this.host+") <=====");

        byte[] data;
        int bufferSize = 8192;
        int cnt = 0;
        String line = null;
        StringBuffer sbTemp = new StringBuffer();
        Boolean isSend = false;
        Boolean isMmcResult = false;

        while (this.isStart) {
            try {
                data = new byte[bufferSize];
                cnt = this.src.read(data, 0, bufferSize);

                if(cnt > 0) {
                    line = new String(data, 0, cnt, "EUC-KR");
                    sbTemp.append(line);
                }

                if(line.contains("COMPLD")){
                    isMmcResult = true;
                    if(line.contains(";")){
                        isSend = true;
                    }else{
                        isSend = false;
                    }
                }else{
                    if(!isMmcResult){
                        isSend = true;
                    }else{
                        if(line.contains(";")){
                            isSend = true;
                        }
                    }
                }
                LOGGER.info("=====> [StreamConnectorMmc] run() mmcMsg isSend : "+ isSend+ " isMmcResult : "+isMmcResult+"<=====");

                if(isSend){
                    if(sbTemp != null && sbTemp.length() > 0){
                        ((Queue<String>)dataShareBean.getData(LinkageCodeInfo.DATA_SHARE_NAME_EMS_MMC_MSG_QUE)).offer(sbTemp.toString());
                        ((Queue<String>)dataShareBean.getData(LinkageCodeInfo.DATA_SHARE_NAME_EMS_MMC_MSG_PASING_QUE)).offer(sbTemp.toString());

                        LOGGER.info("=====> [StreamConnectorMmc] run() mmcMsg : "+ sbTemp.toString()+ "<=====");

                        if(sbTemp.toString().contains("canc-user")){
                            this.isStart = false;
                        }

                        sbTemp.delete(0,sbTemp.length());
                        isSend = false;
                        isMmcResult = false;
                    }
                }
                try {
                    Thread.sleep(50);
                }catch (InterruptedException e){
                    LOGGER.error("=====> [StreamConnectorMmc] sendCommand() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
                }
            }catch(SocketException e){
                this.isStart = false;
                this.isConnection = false;
                this.telnetMmc.closeConnection();
                try {
                    this.src.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                LOGGER.error("=====> [StreamConnectorMmc] run error("+this.host+") "+ ExceptionUtils.getStackTrace(e)+ "<=====");
            }catch (Exception e){
                this.isStart = false;
                this.telnetMmc.closeConnection();
                LOGGER.error("=====> [StreamConnectorMmc] run error("+this.host+") "+ ExceptionUtils.getStackTrace(e)+ "<=====");
            }
        }
    }

    public void setStart(Boolean start) {
        this.isStart = start;
    }

    public Boolean isConnected(){
        return this.isConnection;
    }
}
