package com.nia.tsdn.service.result.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Component()
@Scope(value = "prototype")
public class PceDataVo implements Serializable {

    @JsonProperty("pce")
    private List<PceListVo> pceListVo;

    @JsonProperty("topology-id")
    private String topologyId;

    @JsonProperty("src-node-id")
    private String srcNodeId;

    @JsonProperty("src-tp-id")
    private String srcTpId;

    @JsonProperty("dst-node-id")
    private String dstNodeId;

    @JsonProperty("dst-tp-id")
    private String dstTpId;

    @JsonProperty("cir")
    private String cir;

    @JsonProperty("pir")
    private String pir;

    @JsonProperty("count")
    private String count;


}
