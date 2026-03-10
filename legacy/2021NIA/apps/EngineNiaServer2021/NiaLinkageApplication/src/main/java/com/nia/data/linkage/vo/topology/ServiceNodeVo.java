package com.nia.data.linkage.vo.topology;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Data
@Component()
@Scope(value = "prototype")
public class ServiceNodeVo implements Serializable {
    private String serviceId;
    private int nodeIndex;
    private Boolean isRootNode;
    private String nodeId;
    private int tagIndex;
    private String tagType;
    private String tag;
    private String tpId;
}
