package com.nia.ai.performance.send.vo.performance;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

@Data
@Component()
@Scope(value = "prototype")
public class PerformaceListVo implements Serializable {
    private List<PerformaceVo> data;
}
