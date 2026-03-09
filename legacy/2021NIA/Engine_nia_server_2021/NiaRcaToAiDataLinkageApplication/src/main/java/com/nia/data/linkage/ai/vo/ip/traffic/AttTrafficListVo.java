package com.nia.data.linkage.ai.vo.ip.traffic;

import com.nia.data.linkage.ai.vo.ip.sflow.SflowLogVo;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;

@Data
@Component()
@Scope(value = "prototype")
public class AttTrafficListVo implements Serializable {

    private ArrayList<AttTrafficVo> data;
}
