package com.nia.data.linkage.service;

import com.nia.data.linkage.vo.topology.ServiceVo;
import com.nia.data.linkage.vo.topology.json.TopologyDataVo;

import java.util.List;

public interface TopologyService {

    void getTopologyData();

    void getServicePceData(List<ServiceVo> serviceVoList);

    void setTopologyAndEquipPortData(TopologyDataVo topologyDataVo);

    void setTsdnServiceData(TopologyDataVo topologyDataVo);

}
