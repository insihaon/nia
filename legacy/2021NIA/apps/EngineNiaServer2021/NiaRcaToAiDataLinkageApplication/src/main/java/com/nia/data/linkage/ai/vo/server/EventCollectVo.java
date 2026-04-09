package com.nia.data.linkage.ai.vo.server;


import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@Data
@Scope(value = "prototype")
public class EventCollectVo implements Serializable {
    private int collectSeq;

    public void setEventCollectVo(EventDataVo eventDataVo){
        this.setCollectSeq(eventDataVo.getCollectSeq());
    }
}


