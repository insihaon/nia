package com.nia.alarm.preprocessor.service.impl.pasing;

import com.nia.alarm.preprocessor.common.NiaCodeInfo;
import com.nia.alarm.preprocessor.mapper.EquipmentMapper;
import com.nia.alarm.preprocessor.service.topology.TopologyService;
import com.nia.alarm.preprocessor.service.pasing.CommPasingService;
import com.nia.alarm.preprocessor.vo.alarm.BasicAlarmVo;
import com.nia.alarm.preprocessor.vo.euqipment.EquipInfoVo;
import com.nia.alarm.preprocessor.vo.topology.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service("RoadmPasingService")
@Scope(value = "prototype")
public class RoadmPasingServiceImpl implements CommPasingService {
	private final static Logger LOGGER = Logger.getLogger(RoadmPasingServiceImpl.class);

	@Autowired
	private org.springframework.beans.factory.ObjectFactory<TopologyObject> topologyObjectFactory;

	@Autowired
    @Qualifier("TopologyService")
	private TopologyService topologyService;

	@Autowired
    private EquipmentMapper equipmentMapper;

    @Autowired
    private BasicAlarmVo basicAlarmVo;

    @Override
	public BasicAlarmVo alarmPasing(BasicAlarmVo basicAlarmVo){
        HashMap<String, String> parameterMap;
        EquipInfoVo equipInfoVo;
        this.basicAlarmVo = basicAlarmVo;

		try {

            parameterMap = new HashMap<String, String>();
            parameterMap.put("sysname", basicAlarmVo.getSysname());
		    equipInfoVo = equipmentMapper.selectEquipInfo(parameterMap);

		    if(equipInfoVo != null){
		        this.basicAlarmVo.setEquipName(equipInfoVo.getSysname());
            }

			setPortPasing(this.basicAlarmVo);

            if(!StringUtils.isEmpty(basicAlarmVo.getPtpName())) {
                setTopology(this.basicAlarmVo);
            }

            StringBuffer strLog = new StringBuffer();
            strLog.append("=====> [RoadmPasingService] alarmPasing <=====\n");
            strLog.append("alarmNo : " + this.basicAlarmVo.getAlarmno()+"\n");
            strLog.append("equipType : " + this.basicAlarmVo.getEquiptype()+"\n");
            strLog.append("sysname : " + this.basicAlarmVo.getSysname()+"\n");
//            strLog.append("port : " + this.basicAlarmVo.getPort()+"\n");
//            strLog.append("slot : " + this.basicAlarmVo.getSlot()+"\n");
            strLog.append("---------------------------------------------------------------");
            LOGGER.info(strLog.toString());
		}catch( Exception e ) {
			LOGGER.error(">>>>>>>>>>[RoadmPasingService] alarmPasing("+this.basicAlarmVo.getAlarmno()+") error : " + ExceptionUtils.getStackTrace(e) + " <<<<<<<<<<<<<<<<<");
			return this.basicAlarmVo;
		}
		return this.basicAlarmVo;
	}

	@Override
    public void setPortPasing(BasicAlarmVo basicAlarmVo) {
        String port;
        String slot;
        String[] ptpNameArr;
        String ptpName = null;

        try {
            if(StringUtils.isNotEmpty(basicAlarmVo.getAlarmloc())){
                if(basicAlarmVo.getAlarmloc().startsWith("S")){
                    if(basicAlarmVo.getAlarmloc().contains("-")){
                        ptpName = basicAlarmVo.getSysname().split("-")[1] + "-"+ basicAlarmVo.getAlarmloc().split("-")[0];

                        basicAlarmVo.setSlot(basicAlarmVo.getAlarmloc().split("-")[0]);
                        basicAlarmVo.setPort(basicAlarmVo.getAlarmloc().split("-")[1]);
                    }else{
                        ptpName = basicAlarmVo.getSysname().split("-")[1] + "-"+ basicAlarmVo.getAlarmloc();
                        basicAlarmVo.setSlot(basicAlarmVo.getAlarmloc());
                    }
                }

                if(StringUtils.isNotEmpty(ptpName)){
                    basicAlarmVo.setPtpName(ptpName);
                }

                if(StringUtils.isNotEmpty(basicAlarmVo.getPtpName())){
                    if(basicAlarmVo.getPtpName().contains("-")){
                        ptpNameArr = basicAlarmVo.getPtpName().split("-");
                  //      basicAlarmVo.setPort();
                    }
    //                basicAlarmVo.setPort(Integer.toString(Integer.parseInt(basicAlarmVo.getPportId().substring(basicAlarmVo.getPportId().length()-2))));
    //                basicAlarmVo.setSlot(Integer.toString(Integer.parseInt(basicAlarmVo.getPportId().substring(2,4))));
                }
            }

            this.basicAlarmVo = basicAlarmVo;
        } catch (Exception e) {
            LOGGER.error(">>>>>>>>>>>> RoadmPasingService setPortPasing("+basicAlarmVo.toString()+") error : "+ ExceptionUtils.getStackTrace(e)+" <<<<<<<<<<<<<<<<");
        }
    }

    @Override
    public void setTopology(BasicAlarmVo basicAlarmVo) {
        boolean isTopology = false;
        HashMap<String, String> parameterMap;

        TopologyObject topologyObject = null;
        TopologyTmpVo topologyTmpObject = null;

        UniTopologyObject uniTopologyObject = null;
        RoadmRepeaterRouteVo roadmRepeaterRouteVo = null;
        E2eTopologyVo e2eTopologyVo;

        try{
            switch (basicAlarmVo.getUnit()){
                case "O401SLU" :
                case "OMX24U" :
                case "OLPA-3" :
                case "MRPA_A" :
                case "MRPA_AR" :
                case "OTUC1A-L" :
                case "OM2C2A-L" :
                case "OM24A-L" :
                case "OM24A" :
                    parameterMap = new HashMap<String, String>();
                    parameterMap.put("ptpName", basicAlarmVo.getAlarmloc());
                    parameterMap.put("sysname", basicAlarmVo.getSysname());

                    uniTopologyObject = topologyService.selectUniTopologyList(parameterMap);

                    if(StringUtils.isNotEmpty(uniTopologyObject.getLinkId())){
                        topologyObject = topologyObjectFactory.getObject();
                        topologyObject.setNwType(NiaCodeInfo.TOPOLOGY_GB_UNI);
                        topologyObject.setLinkId(uniTopologyObject.getLinkId());
                        topologyObject.setOppSysname(uniTopologyObject.getNeZ().getSysname());
                        topologyObject.setOppPtpName(uniTopologyObject.getNeZ().getPtpname());

                        if(topologyObject.getOppPtpName().startsWith("S")){
                            if(topologyObject.getOppPtpName().contains("-")){
                                topologyObject.setOppSlot(topologyObject.getOppPtpName().split("-")[0]);
                                topologyObject.setOppPort(topologyObject.getOppPtpName().split("-")[1]);
                            }else{
                                topologyObject.setOppSlot(topologyObject.getOppPtpName());
                            }
                        }

                        isTopology = true;
                    }else{
                        parameterMap = new HashMap<String, String>();
                        parameterMap.put("nodeId", basicAlarmVo.getSysname());
                        parameterMap.put("port", basicAlarmVo.getPtpName());
                        e2eTopologyVo = topologyService.selectE2eTopologyList(parameterMap);

                        if(e2eTopologyVo != null){
                            topologyObject = topologyObjectFactory.getObject();
                            topologyObject.setLinkId(e2eTopologyVo.getLinkId());
                            topologyObject.setOppSysname(e2eTopologyVo.getNodeIdz());
                            topologyObject.setOppPort(e2eTopologyVo.getPortz());

                            isTopology = true;
                        }
                    }

                    break;
                case "MRSA-2B" :
                case "MRSA-2A" :
                    parameterMap = new HashMap<String, String>();
                    parameterMap.put("ptpName", basicAlarmVo.getPtpName());
                    parameterMap.put("sysname", basicAlarmVo.getSysname().split("-")[0]);

                    topologyTmpObject = topologyService.selectTopologyList(parameterMap);

                    if(StringUtils.isNotEmpty(topologyTmpObject.getLinkId())){
                        topologyObject = topologyObjectFactory.getObject();
                        topologyObject.setNwType(NiaCodeInfo.TOPOLOGY_GB_NNI);
                        topologyObject.setLinkId(topologyTmpObject.getLinkId());
                        topologyObject.setOppSysname(topologyTmpObject.getNeZ().getSysname());

                        if(basicAlarmVo.getPtpName().equals(topologyTmpObject.getNeA().getPtpNameBau())){
                            topologyObject.setOppPtpName(topologyTmpObject.getNeZ().getPtpNamePau());
                        }else if(basicAlarmVo.getPtpName().equals(topologyTmpObject.getNeA().getPtpNamePau())){
                            topologyObject.setOppPtpName(topologyTmpObject.getNeZ().getPtpNameBau());
                        }

                        if(topologyObject.getOppPtpName().startsWith("S")){
                            if(topologyObject.getOppPtpName().contains("-")){
                                topologyObject.setOppSlot(topologyObject.getOppPtpName().split("-")[0]);
                                topologyObject.setOppPort(topologyObject.getOppPtpName().split("-")[1]);
                            }else{
                                topologyObject.setOppSlot(topologyObject.getOppPtpName());
                            }
                        }

                        isTopology = true;

                        if(isTopology){
                            parameterMap = new HashMap<String, String>();
                            parameterMap.put("ptpName", basicAlarmVo.getPtpName());
                            parameterMap.put("sysname", basicAlarmVo.getSysname().split("-")[0]);
                            roadmRepeaterRouteVo = topologyService.selectRoadmTrunkId(parameterMap);

                            if(roadmRepeaterRouteVo != null){
                                topologyObject.setTrunkId(roadmRepeaterRouteVo.getTrunkId());
                            }
                        }
                    }
                    break;
            }

            if(isTopology){
                topologyObject.setAlarmno(basicAlarmVo.getAlarmno());
                basicAlarmVo.setTopologyObject(topologyObject);
                this.basicAlarmVo = basicAlarmVo;
            }else{
                topologyObject = topologyObjectFactory.getObject();
                topologyObject.setNwType(NiaCodeInfo.TOPOLOGY_GB_UNKNOWN);
                topologyObject.setAlarmno(basicAlarmVo.getAlarmno());
                basicAlarmVo.setTopologyObject(topologyObject);
                this.basicAlarmVo = basicAlarmVo;
            }
        }catch (Exception e) {
            LOGGER.error(">>>>>>>>>>>> RoadmPasingService setTopology("+basicAlarmVo.getAlarmno()+") error : "+ ExceptionUtils.getStackTrace(e)+" <<<<<<<<<<<<<<<<");
        }
    }
}
