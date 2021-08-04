package com.nia.tsdn.service.result.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

@Data
@Component()
@Scope(value = "prototype")
public class ReservedInputDataVo implements Serializable {
    @JsonProperty("service-req-key")
    private String serviceReqKey;
    @JsonProperty("node")
    private List<NodeVo> nodeList;
    @JsonProperty("line")
    private List<LineVo> lineList;
    @JsonProperty("result-callback")
    private ResultCallbackVo resultCallbackVo;
    @JsonProperty("use-reserve-pce")
    private String useReservePce;
    @JsonProperty("reserved-num")
    private String reservedNum;
    @JsonProperty("pce")
    private PceDataVo pceDataVo;

    @JsonProperty("service-req-key")
    public String getServiceReqKey() {
        return serviceReqKey;
    }

    @JsonProperty("service-req-key")
    public void setServiceReqKey(String serviceReqKey) {
        this.serviceReqKey = serviceReqKey;
    }

    @JsonProperty("node")
    public List<NodeVo> getNodeList() {
        return nodeList;
    }

    @JsonProperty("node")
    public void setNodeList(List<NodeVo> nodeList) {
        this.nodeList = nodeList;
    }

    @JsonProperty("line")
    public List<LineVo> getLineList() {
        return lineList;
    }

    @JsonProperty("line")
    public void setLineList(List<LineVo> lineList) {
        this.lineList = lineList;
    }

    @JsonProperty("result-callback")
    public ResultCallbackVo getResultCallbackVo() {
        return resultCallbackVo;
    }

    @JsonProperty("result-callback")
    public void setResultCallbackVo(ResultCallbackVo resultCallbackVo) {
        this.resultCallbackVo = resultCallbackVo;
    }

    @JsonProperty("use-reserve-pce")
    public String getUseReservePce() {
        return useReservePce;
    }

    @JsonProperty("use-reserve-pce")
    public void setUseReservePce(String useReservePce) {
        this.useReservePce = useReservePce;
    }

    @JsonProperty("reserved-num")
    public String getReservedNum() {
        return reservedNum;
    }

    @JsonProperty("reserved-num")
    public void setReservedNum(String reservedNum) {
        this.reservedNum = reservedNum;
    }

    @JsonProperty("pce")
    public PceDataVo getPceDataVo() {
        return pceDataVo;
    }

    @JsonProperty("pce")
    public void setPceDataVo(PceDataVo pceDataVo) {
        this.pceDataVo = pceDataVo;
    }
}
