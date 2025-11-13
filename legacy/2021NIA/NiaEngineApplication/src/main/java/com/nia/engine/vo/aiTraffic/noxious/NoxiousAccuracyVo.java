package com.nia.engine.vo.aiTraffic.noxious;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Data
@Component()
@Scope(value = "prototype")
@JsonIgnoreProperties(ignoreUnknown = true)
public class NoxiousAccuracyVo implements Serializable {
     private String normalTraffic;
     private String tcpSynFlooding;
     private String landAttack;
     private String pingOfDeath;
     private String udpFlooding;
}
