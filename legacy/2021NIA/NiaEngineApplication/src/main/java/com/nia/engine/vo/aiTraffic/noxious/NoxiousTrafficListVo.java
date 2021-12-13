package com.nia.engine.vo.aiTraffic.noxious;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Data
@Component()
@Scope(value = "prototype")
@JsonIgnoreProperties(ignoreUnknown = true)
public class NoxiousTrafficListVo {
    private ArrayList<NoxiousTrfficVo> data;
}
