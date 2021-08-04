package com.nia.data.linkage.vo.topology;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@Data
@Component()
@Scope(value = "prototype")
public class ServiceVo implements Serializable {
    private String serviceId;
    private String serviceName;
    private String dstNodeId;
    private String dstTpId;
    private String srcNodeId;
    private String srcTpId;
    private List<ServicePceVo> servicePceVoList;
    private List<ServiceNodeVo> serviceNodeVoList;
    private int cbs;
    private String topoId;
    private int pir;
    private int pbs;
    private String serviceType;
    private String serviceOfferNum;
    private int reservedNum;
    private int cir;
    private Timestamp createdDate;
}
