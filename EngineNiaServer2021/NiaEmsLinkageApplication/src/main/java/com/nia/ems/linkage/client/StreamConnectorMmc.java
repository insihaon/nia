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
public class StreamConnectorMmc implements NiaEmsLinkageThread{
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
        StringBuffer fullMsg = new StringBuffer();

        while (this.isStart) {
            try {
                data = new byte[bufferSize];
                cnt = this.src.read(data, 0, bufferSize);

                if(cnt > 0) {
                    line = new String(data, 0, cnt, "EUC-KR");
                    sbTemp.append(line);
                    fullMsg.append(line);
                }

                if(sbTemp != null && sbTemp.length() > 0){
//                    LOGGER.info("=====> [StreamConnectorMmc] run() mmcMsg : "+ sbTemp.toString()+ "<=====");
//                    if(fullMsg.toString().contains("canc-user")){
//                        LOGGER.info("=====> [StreamConnectorMmc] canc-user");
//                        ((Queue<String>)dataShareBean.getData(LinkageCodeInfo.DATA_SHARE_NAME_EMS_MMC_MSG_QUE)).offer(sbTemp.toString()+"canc-user");
//                        this.setStart(false);
//                    }else{
//                        ((Queue<String>)dataShareBean.getData(LinkageCodeInfo.DATA_SHARE_NAME_EMS_MMC_MSG_QUE)).offer(sbTemp.toString());
//                    }
//
//                    ((Queue<String>)dataShareBean.getData(LinkageCodeInfo.DATA_SHARE_NAME_EMS_MMC_MSG_PASING_QUE)).offer(sbTemp.toString());
//                    sbTemp.delete(0,sbTemp.length());

                    ((Queue<String>)dataShareBean.getData(LinkageCodeInfo.DATA_SHARE_NAME_EMS_MMC_MSG_QUE)).offer(sbTemp.toString());
                    ((Queue<String>)dataShareBean.getData(LinkageCodeInfo.DATA_SHARE_NAME_EMS_MMC_MSG_PASING_QUE)).offer(sbTemp.toString());

                    LOGGER.info("=====> [StreamConnectorMmc] run() mmcMsg : "+ sbTemp.toString()+ "<=====");

//                    if(sbTemp.toString().contains("canc-user")){
                    if(sbTemp.toString().contains("canc-user") || sbTemp.toString().contains("nc-user")){
                        LOGGER.info("=====> [StreamConnectorMmc] canc-user");
                        this.endStream();
                    }

                    sbTemp.delete(0,sbTemp.length());
                }

                try {
                    Thread.sleep(50);
                }catch (InterruptedException e){
                    LOGGER.error("=====> [StreamConnectorMmc] sendCommand() "+ ExceptionUtils.getStackTrace(e)+ "<=====");
                }
            }catch(SocketException e){
                LOGGER.error("=====> [StreamConnectorMmc] SocketException run error SocketException("+this.host+") "+ ExceptionUtils.getStackTrace(e)+ "<=====");
                LOGGER.error("=====> [StreamConnectorMmc] SocketException src : " + this.src);

                this.endStream();
            }catch (Exception e){
                LOGGER.error("=====> [StreamConnectorMmc] Exception run error Exception("+this.host+") "+ ExceptionUtils.getStackTrace(e)+ "<=====");
                LOGGER.error("=====> [StreamConnectorMmc] Exception src : " + this.src);
                this.endStream();
            }
        }

        LOGGER.info("=====> [StreamConnectorMmc] StreamConnectorMmc END <=====");
    }

    public void endStream() {
        LOGGER.info("=====> [StreamConnectorMmc] endStream <=====");
        this.isConnection = false;
        this.isStart = false;
        try{
            if(this.src != null){
                this.src.close();
                this.src = null;
            }
        }catch(IOException ex) {
            LOGGER.error("=====> [StreamConnectorMmc] this.src.close() Error : " + this.src);
        }
    }

    public Boolean isConnected(){
        return this.isConnection;
    }
}
