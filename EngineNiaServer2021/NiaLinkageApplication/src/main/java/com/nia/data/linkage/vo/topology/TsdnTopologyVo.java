package com.nia.data.linkage.vo.topology;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Data
@Component()
@Scope(value = "prototype")
public class TsdnTopologyVo implements Serializable {
    private String topologyId;
    private String newSysnamea;
    private String newPtpnamea;
    private String newSysnamez;
    private String newPtpnamez;
    private String oldSysnamea;
    private String oldPtpnamea;
    private String oldSysnamez;
    private String oldPtpnamez;
}
