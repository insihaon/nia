package com.nia.data.linkage.ai.vo.sop;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;

@Component
@Data
@Scope(value = "prototype")
public class SopVo implements Serializable {
    private String sop_id;
    private String ticket_id;
    private String ticket_type;
    private String ticket_result;
    private String status;
    private Timestamp request_time;
    private Timestamp receive_time;
    private String detail;
    private String fault_classify;
    private String fault_type;
    private String fault_detail_content;
    private String etc_content;
    private String ai_accuracy;
    private String fault_type_content;
    private Timestamp start_time;
    private Timestamp end_time;
    private String handling_ack_user;
    private Timestamp handling_ack_time;
    private String handling_fin_user;
    private Timestamp handling_fin_time;
    private String autoproc_type;
    private String zero1_model;
    private String zero1_entropy;
}
