package com.nia.engine.vo.selfProcess;


import lombok.Data;
import lombok.ToString;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@Data
@ToString
@Scope(value = "prototype")
public class SopVo implements Serializable {
    private String sop_id;
    private String ticket_id;
    private String ticket_type;
    private String ticket_result;
    private String status;
    private String request_time;
    private String receive_time;
    private String detail;
    private String fault_classify;
    private String fault_type;
    private String fault_detail_content;
    private String etc_content;
    private String ai_accuracy;
    private String fault_type_content;
    private String start_time;
    private String end_time;
    private String handling_ack_user;
    private String handling_ack_time;
    private String handling_fin_user;
    private String handling_fin_time;
    private String autoproc_type;
    private int zero1_model;
    private double zero1_entropy;
}
