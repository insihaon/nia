package com.nia.data.linkage.ai.vo.ipsdn.resource;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;

@Data
@Component
@Scope(value = "prototype")
public class NodeListVo implements Serializable {
    private ArrayList<NodeVo> data;

}


