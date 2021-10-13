package com.nia.data.linkage.ai.vo.ip.perf;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;

@Data
@Component()
@Scope(value = "prototype")
public class AiPerfIfListVo implements Serializable {
    private ArrayList<PerfVo> data;
}
