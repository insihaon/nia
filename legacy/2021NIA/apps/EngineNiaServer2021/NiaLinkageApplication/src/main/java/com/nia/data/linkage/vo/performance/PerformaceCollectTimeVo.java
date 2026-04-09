package com.nia.data.linkage.vo.performance;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Time;
import java.sql.Timestamp;

@Data
@Component()
@Scope(value = "prototype")
public class PerformaceCollectTimeVo implements Serializable {
    private String collectTime;
    private Timestamp collectDateTime;
}
