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
public class LineVo implements Serializable {

    @JsonProperty("direction")
    private String direction;
    @JsonProperty("source")
    private SourceVo source;
    @JsonProperty("destination")
    private DestinationVo destination;
    @JsonProperty("cir")
    private String cir;
    @JsonProperty("pir")
    private String pir;
    @JsonProperty("cbs")
    private String cbs;
    @JsonProperty("pbs")
    private String pbs;

    @JsonProperty("direction")
    public String getDirection() {
        return direction;
    }

    @JsonProperty("direction")
    public void setDirection(String direction) {
        this.direction = direction;
    }

    @JsonProperty("source")
    public SourceVo getSource() {
        return source;
    }

    @JsonProperty("source")
    public void setSource(SourceVo source) {
        this.source = source;
    }

    @JsonProperty("destination")
    public DestinationVo getDestination() {
        return destination;
    }

    @JsonProperty("destination")
    public void setDestination(DestinationVo destination) {
        this.destination = destination;
    }

    @JsonProperty("cir")
    public String getCir() {
        return cir;
    }

    @JsonProperty("cir")
    public void setCir(String cir) {
        this.cir = cir;
    }

    @JsonProperty("pir")
    public String getPir() {
        return pir;
    }

    @JsonProperty("pir")
    public void setPir(String pir) {
        this.pir = pir;
    }

    @JsonProperty("cbs")
    public String getCbs() {
        return cbs;
    }

    @JsonProperty("cbs")
    public void setCbs(String cbs) {
        this.cbs = cbs;
    }

    @JsonProperty("pbs")
    public String getPbs() {
        return pbs;
    }

    @JsonProperty("pbs")
    public void setPbs(String pbs) {
        this.pbs = pbs;
    }
}
