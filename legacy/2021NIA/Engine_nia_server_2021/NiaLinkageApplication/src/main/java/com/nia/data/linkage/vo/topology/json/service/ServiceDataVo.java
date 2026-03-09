package com.nia.data.linkage.vo.topology.json.service;

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
public class ServiceDataVo implements Serializable {

    @JsonProperty("service-id")
    private String serviceId;
    @JsonProperty("service-name")
    private String serviceName;
    @JsonProperty("pce")
    private List<ServicePceDataVo> servicePceDataVoList;
    @JsonProperty("node")
    private List<ServiceNodeDataVo> serviceNodeDataVoList;
    @JsonProperty("cbs")
    private int cbs;
    @JsonProperty("topo-id")
    private String topoId;
    @JsonProperty("pir")
    private int pir;
    @JsonProperty("life-cycle")
    private String lifeCycle;
    @JsonProperty("pw-id")
    private List<String> pwIdList;
    @JsonProperty("pbs")
    private int pbs;
    @JsonProperty("service-type")
    private String serviceType;
    @JsonProperty("service-offer-num")
    private String serviceOfferNum;
    @JsonProperty("reserved-num")
    private int reservedNum;
    @JsonProperty("cir")
    private int cir;
    @JsonProperty("created-date")
    private String createdDate;

    @JsonProperty("service-id")
    public String getServiceId() {
        return serviceId;
    }

    @JsonProperty("service-id")
    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    @JsonProperty("service-name")
    public String getServiceName() {
        return serviceName;
    }

    @JsonProperty("service-name")
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    @JsonProperty("pce")
    public List<ServicePceDataVo> getServicePceDataVoList() {
        return servicePceDataVoList;
    }

    @JsonProperty("pce")
    public void setServicePceDataVoList(List<ServicePceDataVo> servicePceDataVoList) {
        this.servicePceDataVoList = servicePceDataVoList;
    }

    @JsonProperty("node")
    public List<ServiceNodeDataVo> getServiceNodeDataVoList() {
        return serviceNodeDataVoList;
    }

    @JsonProperty("node")
    public void setServiceNodeDataVoList(List<ServiceNodeDataVo> serviceNodeDataVoList) {
        this.serviceNodeDataVoList = serviceNodeDataVoList;
    }

    @JsonProperty("cbs")
    public int getCbs() {
        return cbs;
    }

    @JsonProperty("cbs")
    public void setCbs(int cbs) {
        this.cbs = cbs;
    }

    @JsonProperty("topoId")
    public String getTopoId() {
        return topoId;
    }

    @JsonProperty("topoId")
    public void setTopoId(String topoId) {
        this.topoId = topoId;
    }

    @JsonProperty("pir")
    public int getPir() {
        return pir;
    }

    @JsonProperty("pir")
    public void setPir(int pir) {
        this.pir = pir;
    }

    @JsonProperty("lifeCycle")
    public String getLifeCycle() {
        return lifeCycle;
    }

    @JsonProperty("lifeCycle")
    public void setLifeCycle(String lifeCycle) {
        this.lifeCycle = lifeCycle;
    }

    @JsonProperty("pw-id")
    public List<String> getPwIdList() {
        return pwIdList;
    }

    @JsonProperty("pw-id")
    public void setPwIdList(List<String> pwIdList) {
        this.pwIdList = pwIdList;
    }

    @JsonProperty("pbs")
    public int getPbs() {
        return pbs;
    }

    @JsonProperty("pbs")
    public void setPbs(int pbs) {
        this.pbs = pbs;
    }

    @JsonProperty("service-type")
    public String getServiceType() {
        return serviceType;
    }

    @JsonProperty("service-type")
    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    @JsonProperty("service-offer-num")
    public String getServiceOfferNum() {
        return serviceOfferNum;
    }

    @JsonProperty("service-offer-num")
    public void setServiceOfferNum(String serviceOfferNum) {
        this.serviceOfferNum = serviceOfferNum;
    }

    @JsonProperty("reserved-num")
    public int getReservedNum() {
        return reservedNum;
    }

    @JsonProperty("reserved-num")
    public void setReservedNum(int reservedNum) {
        this.reservedNum = reservedNum;
    }

    @JsonProperty("cir")
    public int getCir() {
        return cir;
    }

    @JsonProperty("cir")
    public void setCir(int cir) {
        this.cir = cir;
    }

    @JsonProperty("created-date")
    public String getCreatedDate() {
        return createdDate;
    }

    @JsonProperty("created-date")
    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }
}
