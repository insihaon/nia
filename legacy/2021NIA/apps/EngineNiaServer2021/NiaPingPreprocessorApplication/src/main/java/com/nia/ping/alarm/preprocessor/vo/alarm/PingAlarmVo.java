package com.nia.ping.alarm.preprocessor.vo.alarm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;

@Component
@Data
@Scope(value = "prototype")
public class PingAlarmVo implements Serializable {
    private String alarmno;
    private Timestamp alarmtime;
    private Timestamp cleartime;
    private String alarmlvl;
    private String host;
    private String url;
    private String packetsReceived;
    private int percentPacketLoss;
    private String resultCode;
    private String packetsTransmitted;

    public void setAlarmtime(Timestamp alarmtime) {
        this.alarmtime = alarmtime;
    }

}
