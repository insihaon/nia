package com.nia.tsdn.service.result.controller;

import com.nia.tsdn.service.result.common.UtlCommon;
import com.nia.tsdn.service.result.service.AsyncSendService;
import com.nia.tsdn.service.result.vo.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
public class TsdnServiceController {
    private static final Logger LOGGER = Logger.getLogger(TsdnServiceController.class);

    @Autowired
    @Qualifier("AsyncSendService")
    private AsyncSendService asyncSendService;

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<ReservedInputDataVo> reservedInputDataVoObjectFactory;

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<ServiceDeleteVo> serviceDeleteVoObjectFactory;

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<PceInputVo> pceInputVoObjectFactory;

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<ReservedResultVo> resultVoObjectFactory;

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<PceOutputVo> pceOutputVoObjectFactory;

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<PceOutputDataVo> pceOutputDataVoObjectFactory;

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<PceListVo> pceListVoObjectFactory;

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<PceListLinkVo> pceListLinkVoObjectFactory;

    @RequestMapping(value = "restconf/operations/koren-tsdn-nbi-service:reserve-e2e-service",method = RequestMethod.POST)
    public String createTsdnReserved(@RequestBody String reserveData) {
        LOGGER.info("=====> [TsdnServiceController] createTsdnReserved : " + reserveData + " <=====");

        Object obj;
        String resultMsg = null;
        ReservedInputDataVo reservedInputDataVo;
        ReservedResultVo reservedResultVo;

        try {
            reservedInputDataVo = reservedInputDataVoObjectFactory.getObject();
            obj = UtlCommon.jsonToObject(reservedInputDataVo, reserveData);
            reservedInputDataVo = (ReservedInputDataVo)obj;

            if(StringUtils.isNotEmpty(reservedInputDataVo.getServiceReqKey())){
                reservedResultVo = resultVoObjectFactory.getObject();
                reservedResultVo.setServiceId("1111111");
                reservedResultVo.setMessage("");
                reservedResultVo.setIsSuccess(true);
                reservedResultVo.setRequestUrl(reservedInputDataVo.getResultCallbackVo().getOkUrl());
                reservedResultVo.setReservedNum(2);
                asyncSendService.sendServiceResult(reservedResultVo);
            }else{
                reservedResultVo = resultVoObjectFactory.getObject();
                reservedResultVo.setServiceId("2222222 ");
                reservedResultVo.setMessage("");
                reservedResultVo.setIsSuccess(true);
                reservedResultVo.setRequestUrl(reservedInputDataVo.getResultCallbackVo().getOkUrl());
                asyncSendService.sendServiceResult(reservedResultVo);
            }



           // resultMsg = UtlCommon.objectToJson(reservedResultVo);

        }catch (Exception e){
            LOGGER.error("=====> [TsdnServiceController] createTsdnReserved error "+ ExceptionUtils.getStackTrace(e)+ "<=====");
            LOGGER.error("=====> [TsdnServiceController] createTsdnReserved error performaceData :  "+ reserveData+ "<=====");
        }
        return resultMsg;
    }

    @RequestMapping(value = "/restconf/operations/koren-tsdn-nbi-service:delete-e2e-service-info",method = RequestMethod.POST)
    public String deleteTsdnService(@RequestBody String serviceData) {
        LOGGER.info("=====> [TsdnServiceController] deleteTsdnService : " + serviceData + " <=====");

        Object obj;
        String resultMsg = null;
        ServiceDeleteVo serviceDeleteVo;
        ReservedResultVo reservedResultVo;

        try {
            serviceDeleteVo = serviceDeleteVoObjectFactory.getObject();
            obj = UtlCommon.jsonToObject(serviceDeleteVo, serviceData);
            serviceDeleteVo = (ServiceDeleteVo)obj;

            reservedResultVo = resultVoObjectFactory.getObject();
            reservedResultVo.setServiceId("");
            reservedResultVo.setMessage("");
            reservedResultVo.setIsSuccess(true);
            reservedResultVo.setRequestUrl(serviceDeleteVo.getResultCallbackVo().getOkUrl());
            reservedResultVo.setReservedNum(2);
            asyncSendService.sendServiceResult(reservedResultVo);

            //resultMsg = UtlCommon.objectToJson(reservedResultVo);

        }catch (Exception e){
            LOGGER.error("=====> [TsdnServiceController] deleteTsdnService error "+ ExceptionUtils.getStackTrace(e)+ "<=====");
            LOGGER.error("=====> [TsdnServiceController] deleteTsdnService error performaceData :  "+ serviceData+ "<=====");
        }
        return resultMsg;
    }

    @RequestMapping(value = "/restconf/operations/koren-tsdn-nbi-service:discovery-pce",method = RequestMethod.POST)
    public String servicePceSearch(@RequestBody String serviceData) {
        LOGGER.info("=====> [TsdnServiceController] servicePceSearch : " + serviceData + " <=====");

        Object obj;
        String resultMsg = null;
        PceInputVo pceInputVo;
        PceOutputVo pceOutputVo;
        PceOutputDataVo pceOutputDataVo;
        List<PceListVo> pceListVoList;
        PceListVo pceListVo;
        List<PceListLinkVo> pceListLink;
        PceListLinkVo pceListLinkVo;

        try {
            pceInputVo = pceInputVoObjectFactory.getObject();
            obj = UtlCommon.jsonToObject(pceInputVo, serviceData);
            pceInputVo = (PceInputVo)obj;

            pceOutputVo = pceOutputVoObjectFactory.getObject();
            pceOutputDataVo = pceOutputDataVoObjectFactory.getObject();

            pceOutputDataVo.setTopologyId("roadm-cwv-nodes-01");
            pceOutputDataVo.setSrcNodeId(pceInputVo.getPceInputDataVo().getSrcNodeId());
            pceOutputDataVo.setSrcTpId(pceInputVo.getPceInputDataVo().getSrcTpId());
            pceOutputDataVo.setDstNodeId(pceInputVo.getPceInputDataVo().getDstNodeId());
            pceOutputDataVo.setDstTpId(pceInputVo.getPceInputDataVo().getDstTpId());
            pceOutputDataVo.setCir(0);
            pceOutputDataVo.setCount(1);
            pceOutputDataVo.setPir(0);

            pceListVoList = new ArrayList<PceListVo>();

            pceListVo = pceListVoObjectFactory.getObject();
            pceListVo.setCost(7);

            pceListLink = new ArrayList<PceListLinkVo>();

            pceListLinkVo = pceListLinkVoObjectFactory.getObject();
            pceListLinkVo.setSrcNodeId("192.168.200.214-SH1");
            pceListLinkVo.setSrcTpId("SH1-S.1");
            pceListLinkVo.setDstNodeId("192.168.200.210-SH5");
            pceListLinkVo.setDstTpId("SH5-S.1");
            pceListLink.add(pceListLinkVo);

            pceListLinkVo = pceListLinkVoObjectFactory.getObject();
            pceListLinkVo.setSrcNodeId("192.168.200.210-SH1");
            pceListLinkVo.setSrcTpId("SH1-S.1");
            pceListLinkVo.setDstNodeId("192.168.200.213-SH1");
            pceListLinkVo.setDstTpId("SH1-S.2");
            pceListLink.add(pceListLinkVo);

            pceListLinkVo = pceListLinkVoObjectFactory.getObject();
            pceListLinkVo.setSrcNodeId("192.168.200.213-SH1");
            pceListLinkVo.setSrcTpId("SH1-S.1");
            pceListLinkVo.setDstNodeId("10.82.49.182-SH1");
            pceListLinkVo.setDstTpId("SH1-S.2");
            pceListLink.add(pceListLinkVo);

            pceListLinkVo = pceListLinkVoObjectFactory.getObject();
            pceListLinkVo.setSrcNodeId("10.82.49.182-SH1");
            pceListLinkVo.setSrcTpId("SH1-S.2");
            pceListLinkVo.setDstNodeId("192.168.200.220-SH5");
            pceListLinkVo.setDstTpId("SH5-S.1");
            pceListLink.add(pceListLinkVo);

            pceListLinkVo = pceListLinkVoObjectFactory.getObject();
            pceListLinkVo.setSrcNodeId("192.168.200.220-SH1");
            pceListLinkVo.setSrcTpId("SH1-S.1");
            pceListLinkVo.setDstNodeId("10.85.112.199-SH1");
            pceListLinkVo.setDstTpId("SH1-S.2");
            pceListLink.add(pceListLinkVo);

            pceListLinkVo = pceListLinkVoObjectFactory.getObject();
            pceListLinkVo.setSrcNodeId("10.85.112.199-SH1");
            pceListLinkVo.setSrcTpId("SH1-S.2");
            pceListLinkVo.setDstNodeId("10.85.80.183-SH1");
            pceListLinkVo.setDstTpId("SH1-S.2");
            pceListLink.add(pceListLinkVo);

            pceListLinkVo = pceListLinkVoObjectFactory.getObject();
            pceListLinkVo.setSrcNodeId("10.85.80.183-SH1");
            pceListLinkVo.setSrcTpId("SH1-S.2");
            pceListLinkVo.setDstNodeId("10.38.96.110-SH1");
            pceListLinkVo.setDstTpId("SH1-S.2");
            pceListLink.add(pceListLinkVo);

            pceListLinkVo = pceListLinkVoObjectFactory.getObject();
            pceListLinkVo.setSrcNodeId("10.38.96.110-SH1");
            pceListLinkVo.setSrcTpId("SH1-S.2");
            pceListLinkVo.setDstNodeId("192.168.200.214-SH1");
            pceListLinkVo.setDstTpId("SH1-S.2");
            pceListLink.add(pceListLinkVo);

            pceListVo.setPceListLink(pceListLink);

            pceListVoList.add(pceListVo);


            pceListVo = pceListVoObjectFactory.getObject();
            pceListVo.setCost(4);

            pceListLink = new ArrayList<PceListLinkVo>();

            pceListLinkVo = pceListLinkVoObjectFactory.getObject();
            pceListLinkVo.setSrcNodeId("1192.168.200.214-SH1");
            pceListLinkVo.setSrcTpId("SH1-S.2");
            pceListLinkVo.setDstNodeId("10.38.96.110-SH1");
            pceListLinkVo.setDstTpId("SH1-S.1");
            pceListLink.add(pceListLinkVo);

            pceListLinkVo = pceListLinkVoObjectFactory.getObject();
            pceListLinkVo.setSrcNodeId("10.38.96.110-SH1");
            pceListLinkVo.setSrcTpId("SH1-S.1");
            pceListLinkVo.setDstNodeId("10.85.80.183-SH1");
            pceListLinkVo.setDstTpId("SH1-S.1");
            pceListLink.add(pceListLinkVo);

            pceListLinkVo = pceListLinkVoObjectFactory.getObject();
            pceListLinkVo.setSrcNodeId("10.85.80.183-SH1");
            pceListLinkVo.setSrcTpId("SH1-S.1");
            pceListLinkVo.setDstNodeId("10.85.112.199-SH1");
            pceListLinkVo.setDstTpId("SH1-S.1");
            pceListLink.add(pceListLinkVo);

            pceListLinkVo = pceListLinkVoObjectFactory.getObject();
            pceListLinkVo.setSrcNodeId("10.85.112.199-SH1");
            pceListLinkVo.setSrcTpId("SH1-S.1");
            pceListLinkVo.setDstNodeId("192.168.200.220-SH1");
            pceListLinkVo.setDstTpId("SH1-S.1");
            pceListLink.add(pceListLinkVo);


            pceListVo.setPceListLink(pceListLink);

            pceListVoList.add(pceListVo);

            pceOutputDataVo.setPceListVo(pceListVoList);
            pceOutputVo.setPceOutputDataVo(pceOutputDataVo);
            resultMsg = UtlCommon.objectToJson(pceOutputVo);

        }catch (Exception e){
            LOGGER.error("=====> [TsdnServiceController] servicePceSearch error "+ ExceptionUtils.getStackTrace(e)+ "<=====");
            LOGGER.error("=====> [TsdnServiceController] servicePceSearch error pceInputVo :  "+ serviceData+ "<=====");
        }
        return resultMsg;
    }
}

