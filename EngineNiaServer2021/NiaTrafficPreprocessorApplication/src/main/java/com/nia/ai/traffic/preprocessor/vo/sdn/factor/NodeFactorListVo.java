package com.nia.ai.traffic.preprocessor.vo.sdn.factor;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;

@Data
@Component()
@Scope(value = "prototype")
@JsonIgnoreProperties(ignoreUnknown = true)
public class NodeFactorListVo implements Serializable {
    private ArrayList<NodeFactorVo> data;

}
