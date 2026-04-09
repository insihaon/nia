package com.nia.engine.vo;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Component
@Scope(value = "prototype")
public class SopTimeoutVo implements Serializable {

    private String ticketId;
    private Timestamp sendTime;
    private int zero1Model;
    private int zero1Entropy;

}
