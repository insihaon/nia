package com.nia.engine.vo.aiTraffic;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nia.engine.vo.aiTraffic.noxious.NoxiousAccuracyVo;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Data
@Component()
@Scope(value = "prototype")
@JsonIgnoreProperties(ignoreUnknown = true)
public class EngineNttTrafficResultVo {
    private String gb;
    private String ticketId;
    private String jsonKey;
    private String error_type;
    private NoxiousAccuracyVo noxiousAccuracyVo;
}
