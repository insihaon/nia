package com.nia.data.linkage.ai.vo.trans.perf;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

@Data
@Component()
@Scope(value = "prototype")
public class PerfDataAiLinkageVo implements Serializable {
    List<PerformaceVo> data;
}
