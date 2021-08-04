package com.nia.data.linkage.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nia.data.linkage.common.UtlCommon;
import com.nia.data.linkage.common.UtlDateHelper;
import com.nia.data.linkage.mapper.TopologyMapper;
import com.nia.data.linkage.service.TopologyService;
import com.nia.data.linkage.vo.pce.*;
import com.nia.data.linkage.vo.topology.*;
import com.nia.data.linkage.vo.topology.json.TopologyDataVo;
import com.nia.data.linkage.vo.topology.json.TopologyListVo;
import com.nia.data.linkage.vo.topology.json.link.LinkVo;
import com.nia.data.linkage.vo.topology.json.node.NodeVo;
import com.nia.data.linkage.vo.topology.json.node.TerminationPointListVo;
import com.nia.data.linkage.vo.topology.json.service.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service("TopologyService")
public class TopologyServiceImpl implements TopologyService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TopologyServiceImpl.class);

    @Autowired
    private TopologyMapper topologyMapper;

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<TopologyDataVo> topologyDataVoFactory;

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<EquipPortVo> equipPortVoObjectFactory;

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<TopologyVo> topologyVoFactory;

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<ServiceVo> serviceVoObjectFactory;

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<ServicePceVo> servicePceVoObjectFactory;

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<ServiceNodeVo> serviceNodeVoObjectFactory;

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<ServicePceInputJsonVo> servicePceInputJsonVoObjectFactory;

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<ServicePceInputDataJsonVo> servicePceJsonVoObjectFactory;

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<ServicePceOutputJsonVo> servicePceOutputJsonVoObjectFactory;

    @Autowired
    @Qualifier("NIA_RestTemplate")
    private RestTemplate restTemplate;

    @Value("${spring.nia.api.topology.url}")
    private String topologyUrl;

    @Value("${spring.nia.api.service-pce-url}")
    private String servicePceUrl;

    @Value("${spring.nia.api.topology.hearderName}")
    private String hearderName;

    @Value("${spring.nia.api.topology.hearderValue}")
    private String hearderValue;


    @Override
    public void getTopologyData() {
        LOGGER.info("==========>[TopologyService] getTopologyData <==============");
        ResponseEntity<String> responseEntity;
        String requestData;
        Object obj;
        String msg;
        HttpHeaders httpHeaders;
        TopologyDataVo topologyDataVo;

        try {

            httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            httpHeaders.add("Authorization","Basic YWRtaW46YWRtaW4=");

            MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
            HttpEntity<String> request = new HttpEntity<String>(httpHeaders);


            restTemplate.getMessageConverters()
                        .add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));

            try {
                responseEntity = restTemplate.exchange(topologyUrl, HttpMethod.GET, request, String.class);

                LOGGER.info("==========>[TopologyService] getTopologyData result: "+ responseEntity.getBody() +"<==============");
            }catch (ResourceAccessException rae){
                Thread.sleep(3000);
                restTemplate.getMessageConverters()
                            .add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));

                responseEntity = restTemplate.exchange(topologyUrl, HttpMethod.GET, request, String.class);
            }

            msg = new String(responseEntity.getBody());
            topologyDataVo = topologyDataVoFactory.getObject();

            obj = UtlCommon.jsonToObject(topologyDataVo, msg);

            topologyDataVo = (TopologyDataVo)obj;

            LOGGER.info(topologyDataVo.toString());
            setTopologyAndEquipPortData(topologyDataVo);
            //setTsdnServiceData(topologyDataVo);

        }catch (Exception e){
            LOGGER.error("=====> [TopologyService] getTopologyData error "+ ExceptionUtils.getStackTrace(e)+ "<=====");
        }
    }

    @Override
    public void getServicePceData(List<ServiceVo> serviceVoList) {
        LOGGER.info("==========>[TopologyService] getServicePceData <==============");
        ResponseEntity<String> responseEntity;
        Object obj;
        String msg;
        HttpHeaders httpHeaders;
        ServicePceOutputJsonVo servicePceOutputJsonVo;
        ServicePceInputJsonVo servicePceInputJsonVo;
        ServicePceInputDataJsonVo servicePceJsonVo;
        ObjectMapper objectMapper;
        String pceInputJson = null;
        ServicePceVo servicePceVo;
        List<ServicePceVo> servicePceVoList = null;
        HashMap<String, Object> paramMap;
        HttpEntity<String> request;
        int pceIndex;
        List<TsdnTopologyVo> tsdnTopology;

        try {
            if(serviceVoList != null && serviceVoList.size() > 0){
                for(ServiceVo serviceVo : serviceVoList){
                    servicePceInputJsonVo = servicePceInputJsonVoObjectFactory.getObject();
                    servicePceJsonVo = servicePceJsonVoObjectFactory.getObject();
                    servicePceVoList = new ArrayList<ServicePceVo>();

                    servicePceJsonVo.setDstNodeId(serviceVo.getDstNodeId());
                    servicePceJsonVo.setDstTpId(serviceVo.getDstTpId());
                    servicePceJsonVo.setSrcNodeId(serviceVo.getSrcNodeId());
                    servicePceJsonVo.setSrcTpId(serviceVo.getSrcTpId());
                    servicePceJsonVo.setTopologyId(serviceVo.getTopoId());
                    servicePceJsonVo.setCount("10");
                    servicePceJsonVo.setPir("0");
                    servicePceJsonVo.setCir("0");

                    servicePceInputJsonVo.setServicePceJsonVo(servicePceJsonVo);

                    objectMapper = new ObjectMapper();
                    pceInputJson = objectMapper.writeValueAsString(servicePceInputJsonVo);

                    httpHeaders = new HttpHeaders();
                    httpHeaders.setContentType(MediaType.APPLICATION_JSON);
                    httpHeaders.add("Authorization","Basic YWRtaW46YWRtaW4=");
                    httpHeaders.add("Content-Type","application/json");

                    request = new HttpEntity<String>(pceInputJson, httpHeaders);

                    restTemplate.getMessageConverters()
                                .add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));

                    try {
                        responseEntity = restTemplate.exchange(servicePceUrl, HttpMethod.POST, request, String.class);

                        LOGGER.info("==========>[TopologyService] getServicePceData result: "+ responseEntity.getBody() +"<==============");
                    }catch (ResourceAccessException rae){
                        Thread.sleep(3000);
                        restTemplate.getMessageConverters()
                                    .add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));

                        responseEntity = restTemplate.exchange(servicePceUrl, HttpMethod.POST, request, String.class);
                    }

                    msg = new String(responseEntity.getBody());
                    servicePceOutputJsonVo = servicePceOutputJsonVoObjectFactory.getObject();

                    obj = UtlCommon.jsonToObject(servicePceOutputJsonVo, msg);

                    servicePceOutputJsonVo = (ServicePceOutputJsonVo)obj;

                    if(servicePceOutputJsonVo != null){
                        if(servicePceOutputJsonVo.getServicePceOutputJsonDataVo() != null){
                            if(servicePceOutputJsonVo.getServicePceOutputJsonDataVo().getServicePceOutputPceJsonVoList() != null
                                &&  servicePceOutputJsonVo.getServicePceOutputJsonDataVo().getServicePceOutputPceJsonVoList().size() > 0){
                                pceIndex = 1;
                                for(ServicePceOutputPceJsonVo servicePceOutputPceJsonVo : servicePceOutputJsonVo.getServicePceOutputJsonDataVo().getServicePceOutputPceJsonVoList()){
                                    if(servicePceOutputPceJsonVo.getServicePceOutputPceLinkDataJsonVoList() != null && servicePceOutputPceJsonVo.getServicePceOutputPceLinkDataJsonVoList().size() > 0){
                                        for(ServicePceOutputPceLinkDataJsonVo servicePceOutputPceLinkDataJsonVo : servicePceOutputPceJsonVo.getServicePceOutputPceLinkDataJsonVoList() ){
                                            servicePceVo = servicePceVoObjectFactory.getObject();
                                            servicePceVo.setServiceId(serviceVo.getServiceId());
                                            servicePceVo.setCost(servicePceOutputPceJsonVo.getCost());
                                            servicePceVo.setPceIndex(pceIndex++);
                                            servicePceVo.setDstNodeId(servicePceOutputPceLinkDataJsonVo.getDstNodeId());
                                            servicePceVo.setSrcTpId(servicePceOutputPceLinkDataJsonVo.getSrcTpId());
                                            servicePceVo.setDstTpId(servicePceOutputPceLinkDataJsonVo.getDstTpId());
                                            servicePceVo.setSrcNodeId(servicePceOutputPceLinkDataJsonVo.getSrcNodeId());
                                            servicePceVoList.add(servicePceVo);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if(servicePceVoList != null && servicePceVoList.size() > 0 ){
                        paramMap = new HashMap<String, Object>();
                        paramMap.put("servicePceVoList", servicePceVoList);
                        topologyMapper.insertTsdnServicePce(paramMap);
                    }
                    Thread.sleep(500);
                }
            }
        }catch (Exception e){
            LOGGER.error("=====> [TopologyService] getServicePceData error "+ ExceptionUtils.getStackTrace(e)+ "<=====");
        }
    }

    @Override
    public void setTopologyAndEquipPortData(TopologyDataVo topologyDataVo) {
        TopologyVo topologyVo;
        List<TopologyVo> topologyVoList = null;
        List<TopologyVo> topologyTmpVoList = null;

        EquipPortVo equipPortVo;
        List<EquipPortVo> equipPortList = null;

        HashMap<String, Object> paramMap;

        try {
            if(topologyDataVo != null){
                if(topologyDataVo.getNetWorkTopologyVo().getTopologyListVo() != null && topologyDataVo.getNetWorkTopologyVo().getTopologyListVo().size() > 0){
                    topologyVoList = new ArrayList<TopologyVo>();
                    equipPortList = new ArrayList<EquipPortVo>();

                    for(TopologyListVo topologyListVo : topologyDataVo.getNetWorkTopologyVo().getTopologyListVo()){
                        if(topologyListVo.getLinkVo() != null && topologyListVo.getLinkVo().size() > 0){
                            for(LinkVo linkVo : topologyListVo.getLinkVo()){
                                topologyVo = topologyVoFactory.getObject();
                                topologyVo.setLinkId(linkVo.getLinkId());
                                topologyVo.setTopologyId(topologyListVo.getTopologyId());
                                topologyVo.setSysnamea(linkVo.getLinkDestinationVo().getDestNode());
                                topologyVo.setPtpnamea(linkVo.getLinkDestinationVo().getDestTp());
                                topologyVo.setSysnamez(linkVo.getLinkSourceVo().getSourceNode());
                                topologyVo.setPtpnamez(linkVo.getLinkSourceVo().getSourceTp());
                                topologyVoList.add(topologyVo);
                            }
                        }

//                        if(topologyListVo.getNodeVo() != null && topologyListVo.getNodeVo().size() > 0){
//                            for(NodeVo nodeVo : topologyListVo.getNodeVo()){
//                                if(nodeVo.getTerminationPointListVo() != null && nodeVo.getTerminationPointListVo().size() > 0){
//                                    for(TerminationPointListVo terminationPointListVo : nodeVo.getTerminationPointListVo()){
//                                        equipPortVo = equipPortVoObjectFactory.getObject();
//                                        equipPortVo.setSysname(nodeVo.getNodeId());
//                                        equipPortVo.setPtpname(terminationPointListVo.getTpId());
//                                        equipPortVo.setPortType(terminationPointListVo.getPortType());
//                                        equipPortList.add(equipPortVo);
//                                    }
//                                }
//                            }
//                        }
                    }
                }
            }

            if(topologyVoList != null && topologyVoList.size() > 0){
                topologyTmpVoList = new ArrayList<TopologyVo>();

                for(TopologyVo topology : topologyVoList){
                    if(!topologyTmpVoList.stream().anyMatch(link -> link.getLinkId().contains(topology.getSysnamea()+"/"+topology.getPtpnamea())
                                                                    && link.getLinkId().contains(topology.getSysnamez()+"/"+topology.getPtpnamez()))) {
                        topologyTmpVoList.add(topology);
                    }
                }

                paramMap = new HashMap<String, Object>();
                paramMap.put("topologyVoList", topologyTmpVoList);
                topologyMapper.deleteTopology();
                topologyMapper.insertTopology(paramMap);
            }

//            if(equipPortList != null && equipPortList.size() > 0){
//                paramMap = new HashMap<String, Object>();
//                paramMap.put("equipPortList", equipPortList);
//                topologyMapper.deleteEquipPort();
//                topologyMapper.insertEquipPort(paramMap);
//            }

        }catch (Exception e){
            LOGGER.error("=====> [TopologyService] setTopologyAndEquipTypePortData error "+ ExceptionUtils.getStackTrace(e)+ "<=====");
        }
    }

    @Override
    public void setTsdnServiceData(TopologyDataVo topologyDataVo) {
        ServiceVo serviceVo;
        ServicePceVo servicePceVo;
        ServiceNodeVo serviceNodeVo;
        int pceIndex;

        List<ServiceVo> serviceVoList = null;
        List<ServicePceVo> servicePceVoList = null;
        List<ServiceNodeVo> serviceNodeVoList = null;

        HashMap<String, Object> paramMap;
        String[] serviceNameArr;
        String[] serviceNamePotnArr;

        try {
            if(topologyDataVo != null){
                if(topologyDataVo.getNetWorkTopologyVo().getTopologyListVo() != null && topologyDataVo.getNetWorkTopologyVo().getTopologyListVo().size() > 0){
                    serviceVoList = new ArrayList<ServiceVo>();

                    for(ServiceDataVo serviceDataVo : topologyDataVo.getNetWorkTopologyVo().getServiceDataVoList()){
                         if(serviceDataVo.getServiceNodeDataVoList().size() <= 1){
                            continue;
                        }
//                        if(serviceDataVo.getServiceName().contains("Src-Pw")){
//                            continue;
//                        }
                        serviceVo = serviceVoObjectFactory.getObject();
                        serviceVo.setServiceId(serviceDataVo.getServiceId());
                        serviceVo.setServiceName(serviceDataVo.getServiceName());
                        serviceVo.setCbs(serviceDataVo.getCbs());
                        serviceVo.setTopoId(serviceDataVo.getTopoId());
                        serviceVo.setPir(serviceDataVo.getPir());
                        serviceVo.setPbs(serviceDataVo.getPbs());
                        serviceVo.setServiceType(serviceDataVo.getServiceType());
                        serviceVo.setServiceOfferNum(serviceDataVo.getServiceOfferNum());
                        serviceVo.setReservedNum(serviceDataVo.getReservedNum());
                        serviceVo.setCreatedDate(UtlDateHelper.stringToTimestamp("20"+serviceDataVo.getCreatedDate()));

                        if(serviceDataVo.getTopoId().startsWith("roadm")){
                            serviceVo.setDstNodeId(serviceDataVo.getServiceNodeDataVoList().get(0).getNodeId());
                            serviceVo.setDstTpId(serviceDataVo.getServiceNodeDataVoList().get(0).getTpId());
                            serviceVo.setSrcNodeId(serviceDataVo.getServiceNodeDataVoList().get(1).getNodeId());
                            serviceVo.setSrcTpId(serviceDataVo.getServiceNodeDataVoList().get(1).getTpId());

//                            serviceNameArr = serviceDataVo.getServiceName().split("--");
//                            for(int i=0; i<serviceNameArr.length; i++){
//                                if(serviceNameArr[i].contains("/")){
//                                    if(i == 0){
//                                        serviceVo.setDstNodeId(serviceNameArr[i].split("/")[0]);
//                                        serviceVo.setDstTpId(serviceNameArr[i].split("/")[1]);
//                                    }else if(i == 1){
//                                        serviceVo.setSrcNodeId(serviceNameArr[i].split("/")[0]);
//                                        serviceVo.setSrcTpId(serviceNameArr[i].split("/")[1]);
//                                    }
//                                }
//                            }
                        }else if(serviceDataVo.getTopoId().startsWith("potn")){
                            serviceVo.setDstNodeId(serviceDataVo.getServiceNodeDataVoList().get(1).getNodeId());
                            serviceVo.setDstTpId(serviceDataVo.getServiceNodeDataVoList().get(1).getTpId());
                            serviceVo.setSrcNodeId(serviceDataVo.getServiceNodeDataVoList().get(0).getNodeId());
                            serviceVo.setSrcTpId(serviceDataVo.getServiceNodeDataVoList().get(0).getTpId());

//                            serviceNameArr = serviceDataVo.getServiceName().split("_");
//                            serviceNamePotnArr = serviceNameArr[2].split("-");
//
//                            serviceVo.setDstNodeId(serviceNamePotnArr[0]);
//                            serviceVo.setDstTpId(serviceNamePotnArr[1]+"-"+serviceNamePotnArr[2]);
//                            serviceVo.setSrcNodeId(serviceNamePotnArr[3]);
//                            serviceVo.setSrcTpId(serviceNamePotnArr[4]+"-"+serviceNamePotnArr[5]);
                        }

                        if(serviceDataVo.getServicePceDataVoList() != null && serviceDataVo.getServicePceDataVoList().size() > 0){
                            servicePceVoList = new ArrayList<ServicePceVo>();
                            for(ServicePceDataVo servicePceDataVo : serviceDataVo.getServicePceDataVoList()){
                                if(servicePceDataVo.getServicePceLinkDataVoList() != null && servicePceDataVo.getServicePceLinkDataVoList().size() > 0){
                                    pceIndex = 1;
                                     for(ServicePceLinkDataVo servicePceLinkDataVo : servicePceDataVo.getServicePceLinkDataVoList()){
                                         if(StringUtils.isNotEmpty(servicePceLinkDataVo.getTunnelId())){
                                            servicePceVo = servicePceVoObjectFactory.getObject();
                                            servicePceVo.setServiceId(serviceDataVo.getServiceId());
                                            servicePceVo.setCost(servicePceDataVo.getCost());
                                            servicePceVo.setPceIndex(pceIndex++);
                                            servicePceVo.setDstNodeId(servicePceLinkDataVo.getDstNodeId());
                                            servicePceVo.setSrcTpId(servicePceLinkDataVo.getSrcTpId());
                                            servicePceVo.setDstTpId(servicePceLinkDataVo.getDstTpId());
                                            servicePceVo.setSrcNodeId(servicePceLinkDataVo.getSrcNodeId());
                                            servicePceVoList.add(servicePceVo);
                                         }
                                    }
                                }
                            }

                            if(servicePceVoList != null && servicePceVoList.size() > 0){
                                serviceVo.setServicePceVoList(servicePceVoList);
                            }
                        }

                        if(serviceDataVo.getServicePceDataVoList() != null && serviceDataVo.getServiceNodeDataVoList().size() > 0){
                            serviceNodeVoList = new ArrayList<ServiceNodeVo>();
                            for(ServiceNodeDataVo serviceNodeDataVo : serviceDataVo.getServiceNodeDataVoList()){
                                if(serviceNodeDataVo.getServiceNodeTagDataVoList() != null && serviceNodeDataVo.getServiceNodeTagDataVoList().size() > 0){

                                    if(serviceNodeDataVo.getServiceNodeTagDataVoList() != null && serviceNodeDataVo.getServiceNodeTagDataVoList().size() > 0){
                                        for(ServiceNodeTagDataVo serviceNodeTagDataVo : serviceNodeDataVo.getServiceNodeTagDataVoList()){
                                            serviceNodeVo = serviceNodeVoObjectFactory.getObject();
                                            serviceNodeVo.setServiceId(serviceDataVo.getServiceId());
                                            serviceNodeVo.setNodeId(serviceNodeDataVo.getNodeId());
                                            serviceNodeVo.setNodeIndex(serviceNodeDataVo.getNodeIndex());
                                            serviceNodeVo.setIsRootNode(serviceNodeDataVo.getRootNode());
                                            serviceNodeVo.setTagIndex(serviceNodeTagDataVo.getTagIndex());
                                            serviceNodeVo.setTagType(serviceNodeTagDataVo.getTagType());
                                            serviceNodeVo.setTag(serviceNodeTagDataVo.getTag());
                                            serviceNodeVo.setTpId(serviceNodeDataVo.getTpId());

                                            serviceNodeVoList.add(serviceNodeVo);
                                        }
                                    }
                                }
                            }

                            if(servicePceVoList != null && servicePceVoList.size() > 0){
                                serviceVo.setServicePceVoList(servicePceVoList);
                            }

                            if(serviceNodeVoList != null && serviceNodeVoList.size() > 0){
                                serviceVo.setServiceNodeVoList(serviceNodeVoList);
                            }
                        }

                        serviceVoList.add(serviceVo);
                    }

                    if(serviceVoList != null && serviceVoList.size() > 0) {

                        topologyMapper.deleteTsdnService();
                        topologyMapper.deleteTsdnServicePce();
                        paramMap = new HashMap<String, Object>();
                        paramMap.put("serviceVoList", serviceVoList);
                        topologyMapper.insertTsdnService(paramMap);

                        for(ServiceVo serviceData : serviceVoList){
                            if(serviceData.getServicePceVoList() != null && serviceData.getServicePceVoList().size() > 0){
                                paramMap = new HashMap<String, Object>();
                                paramMap.put("servicePceVoList", serviceData.getServicePceVoList());
                                topologyMapper.insertTsdnServicePce(paramMap);
                            }

//                            if(serviceData.getServiceNodeVoList() != null && serviceData.getServiceNodeVoList().size() > 0){
//                                paramMap = new HashMap<String, Object>();
//                                paramMap.put("serviceNodeVoList", serviceData.getServiceNodeVoList());
//                                topologyMapper.insertTsdnServiceNode(paramMap);
//                            }
                        }

                     //   getServicePceData(serviceVoList);
                    }
                }
            }
        }catch (Exception e){
            LOGGER.error("=====> [TopologyService] setTopologyAndEquipTypePortData error "+ ExceptionUtils.getStackTrace(e)+ "<=====");
        }
    }
}
