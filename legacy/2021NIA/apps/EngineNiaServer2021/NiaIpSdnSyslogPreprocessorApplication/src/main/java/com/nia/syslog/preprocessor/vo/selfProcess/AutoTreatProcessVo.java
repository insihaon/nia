package com.nia.syslog.preprocessor.vo.selfProcess;

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

    private String self_process_no;
    private String self_process_group;
    private String self_process_type;
    private String clos_type;
    private String feedback_msg;
    private String occur_time;
    private Timestamp feedback_time;
    private String ticket_id;
    private String ticket_type;
    private String alarmno;
    private String clos_status;
    private Timestamp clos_time;

}
