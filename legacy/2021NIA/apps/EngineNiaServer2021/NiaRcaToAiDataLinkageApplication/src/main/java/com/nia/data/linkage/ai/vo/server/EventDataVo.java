package com.nia.data.linkage.ai.vo.server;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Component()
@Scope(value = "prototype")
public class EventDataVo implements Serializable {
    private int collectSeq;
    private String ticket_id;
    private String ticket_title;
    private String ticket_type;
    private Timestamp create_time;




//    UI에서 받는 데이터
//    ticket_id, ticket_type, ticket_title, create_time
}
