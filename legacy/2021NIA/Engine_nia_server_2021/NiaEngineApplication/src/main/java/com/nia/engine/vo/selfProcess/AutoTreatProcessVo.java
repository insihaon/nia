package com.nia.engine.vo.selfProcess;

import lombok.Data;
import lombok.ToString;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;

@Component
@Data
@ToString
@Scope(value = "prototype")
public class AutoTreatProcessVo implements Serializable {
    //tb_auto_treat_process_dtl

    private String selfProcessNo;
    private String selfProcessGroup;
    private String selfProcessType;
    private String closType;
    private String feedbackMsg;
    private Timestamp occurTime;
    private Timestamp feedbackTime;
    private String ticketId;
    private String ticketType;
    private String alarmno;
    private String closStatus;
    private Timestamp closTime;
}
