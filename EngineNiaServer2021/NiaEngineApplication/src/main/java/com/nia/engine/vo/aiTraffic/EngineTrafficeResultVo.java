package com.nia.engine.vo.aiTraffic;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nia.engine.vo.aiTraffic.anomalous.AnomalousTrafficListVo;
import com.nia.engine.vo.aiTraffic.noxious.NoxiousTrafficListVo;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Data
@Component()
@Scope(value = "prototype")
@JsonIgnoreProperties(ignoreUnknown = true)
public class EngineTrafficeResultVo {
    private String gb;
    private AnomalousTrafficListVo perfListVo;
    private NoxiousTrafficListVo noxiousListVo;
}
