package com.nia.ai.traffic.preprocessor.vo.anomalous;

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
public class AnomalousTrafficListVo implements Serializable {
    private ArrayList<AnomalousTrafficVo> data;
}
