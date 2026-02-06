package com.nia.data.linkage.ip.sflow.vo.ai;

import com.nia.data.linkage.ip.sflow.vo.sflow.SflowLogVo;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;

@Data
@Component()
@Scope(value = "prototype")
public class AiSflowLogListVo implements Serializable {
    private ArrayList<SflowLogVo> data;
}
