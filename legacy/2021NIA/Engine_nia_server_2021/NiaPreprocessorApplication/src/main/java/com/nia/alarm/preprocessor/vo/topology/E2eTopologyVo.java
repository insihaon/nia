package com.nia.alarm.preprocessor.vo.topology;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@Scope(value = "prototype")
@Data
public class E2eTopologyVo implements Serializable {
    private String linkId;
    private String nodeIda;
    private String porta;
    private String nodeIdz;
    private String portz;
}
