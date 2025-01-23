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
public class TicketCurrentVo implements Serializable {

    private String rootCauseSysnamea;
    private String rootCausePorta;
}
