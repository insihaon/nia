package com.nia.ai.traffic.preprocessor.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nia.ai.traffic.preprocessor.vo.anomalous.AnomalousTrafficListVo;
import com.nia.ai.traffic.preprocessor.vo.noxious.NoxiousTrafficListVo;
import com.nia.ai.traffic.preprocessor.vo.sdn.factor.NodeFactorListVo;
import com.nia.ai.traffic.preprocessor.vo.sdn.traffic.SdnTrafficListVo;
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
    private SdnTrafficListVo trafficListVo;
    private NodeFactorListVo nodeFactorListVo;
}
