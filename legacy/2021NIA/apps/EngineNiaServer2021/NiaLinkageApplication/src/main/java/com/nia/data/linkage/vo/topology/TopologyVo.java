package com.nia.data.linkage.vo.topology;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Data
@Component()
@Scope(value = "prototype")
public class TopologyVo implements Serializable {
    private String linkId;
    private String sysnamea;
    private String ptpnamea;
    private String sysnamez;
    private String ptpnamez;
    private String topologyId;
}
