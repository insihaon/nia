package com.nia.ems.linkage.config;

import com.nia.ems.linkage.common.LinkageCodeInfo;
import com.nia.ems.linkage.data.DataShareBean;
import com.nia.ems.linkage.thread.NiaEmsLinkageThread;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

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

    public StreamConnectorMmc(){
        super();
    }

    public void setStream(InputStream in, OutputStream out, String host, int port){
        src = in;
        dist = out;
        this.host = host;
        this.port = port;
    }

    //  처리 부분
    //  스트림 읽고 쓰기를 무한히 반복한다.
    @Override
    public void run(){
        LOGGER.info("=====> [StreamConnectorMmc] thread run host("+host+") <=====");

        byte[] data;
        int bufferSize = 8192;
        int cnt = 0;
        String line = null;
        StringBuffer sbTemp = new StringBuffer();
        Boolean isSend = false;
        Boolean isMmcResult = false;

        while (isStart) {
            try {
                data = new byte[bufferSize];
                cnt = src.read(data, 0, bufferSize);

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
                            isStart = false;
                        }

                        sbTemp = new StringBuffer();
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
                isStart = false;
                isConnection = false;
                LOGGER.error("=====> [StreamConnectorMmc] run error("+host+") "+ ExceptionUtils.getStackTrace(e)+ "<=====");
            }catch (Exception e){
                isStart = false;
                LOGGER.error("=====> [StreamConnectorMmc] run error("+host+") "+ ExceptionUtils.getStackTrace(e)+ "<=====");
            }
        }
    }

    public void setStart(Boolean start) {
        isStart = start;
    }

    public Boolean isConnected(){
        return isConnection;
    }
}
