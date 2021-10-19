package com.nia.alarm.ip.preprocessor.service.impl.pasing;

import com.nia.alarm.ip.preprocessor.common.NiaCodeInfo;
import com.nia.alarm.ip.preprocessor.mapper.EquipmentMapper;
import com.nia.alarm.ip.preprocessor.service.topology.TopologyService;
import com.nia.alarm.ip.preprocessor.service.pasing.CommPasingService;
import com.nia.alarm.ip.preprocessor.vo.alarm.BasicAlarmVo;
import com.nia.alarm.ip.preprocessor.vo.euqipment.NodeInfoVo;
import com.nia.alarm.ip.preprocessor.vo.euqipment.PortMstVo;
import com.nia.alarm.ip.preprocessor.vo.topology.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service("IpPasingService")
@Scope(value = "prototype")
public class IpPasingServiceImpl implements CommPasingService {
	private final static Logger LOGGER = Logger.getLogger(IpPasingServiceImpl.class);

	@Autowired
	private org.springframework.beans.factory.ObjectFactory<TopologyObject> topologyObjectFactory;

	@Autowired
    @Qualifier("TopologyService")
	private TopologyService topologyService;

    @Autowired
    private EquipmentMapper equipmentMapper;

    @Autowired
    private BasicAlarmVo basicAlarmVo;

	private HashMap<String, String> parameterMap;

    @Override
	public BasicAlarmVo alarmPasing(BasicAlarmVo basicAlarmVo){
        this.basicAlarmVo = basicAlarmVo;

        NodeInfoVo nodeInfoVo;
        PortMstVo portMstVo;

		try {
            parameterMap = new HashMap<String, String>();
            parameterMap.put("nodeNum", basicAlarmVo.getEquipCode());
            nodeInfoVo = equipmentMapper.selectNodeMst(parameterMap);

            if(nodeInfoVo != null){
                this.basicAlarmVo.setSysname(nodeInfoVo.getNodeId());
                this.basicAlarmVo.setEquiptype(nodeInfoVo.getModelNm());
            }

            parameterMap.put("nodeNum", basicAlarmVo.getEquipCode());
            parameterMap.put("ifNum", basicAlarmVo.getIfNum());
            portMstVo = equipmentMapper.selectPortMst(parameterMap);

            if(portMstVo != null){
                this.basicAlarmVo.setIfId(portMstVo.getIfId());
                this.basicAlarmVo.setAlarmloc(portMstVo.getIfId());
            }

            setTopology(this.basicAlarmVo);

            StringBuffer strLog = new StringBuffer();
            strLog.append("=====> [CmVmPasingService] alarmPasing <=====\n");
            strLog.append("alarmNo : " + this.basicAlarmVo.getAlarmno()+"\n");
            strLog.append("equipType : " + this.basicAlarmVo.getEquiptype()+"\n");
            strLog.append("sysname : " + this.basicAlarmVo.getSysname()+"\n");
            strLog.append("ifId : " + this.basicAlarmVo.getIfId()+"\n");
            strLog.append("ptpName : " + this.basicAlarmVo.getPtpName()+"\n");

            if(this.basicAlarmVo.getTopologyObject() != null){
                strLog.append("linkId : " + this.basicAlarmVo.getTopologyObject().getLinkId()+"\n");
                strLog.append("oppSysname : " + this.basicAlarmVo.getTopologyObject().getOppSysname()+"\n");
                strLog.append("oppIfId : " + this.basicAlarmVo.getTopologyObject().getOppIfId()+"\n");
            }
            strLog.append("---------------------------------------------------------------");
            LOGGER.info(strLog.toString());
		}catch( Exception e ) {
			LOGGER.error(">>>>>>>>>>[RoadmPasingService] alarmPasing("+this.basicAlarmVo.getAlarmno()+") error : " + ExceptionUtils.getStackTrace(e) + " <<<<<<<<<<<<<<<<<");
			return this.basicAlarmVo;
		}
		return this.basicAlarmVo;
	}

    @Override
    public void setTopology(BasicAlarmVo basicAlarmVo) {
        boolean isTopology = false;

        TopologyObject topologyObject = null;
        TopologyTmpVo topologyTmpObject = null;

        try{
            parameterMap = new HashMap<String, String>();
            parameterMap.put("nodeNum", basicAlarmVo.getEquipCode());
            parameterMap.put("ifNum", basicAlarmVo.getIfNum());

            topologyTmpObject = topologyService.selectTopologyList(parameterMap);

            if(StringUtils.isNotEmpty(topologyTmpObject.getLinkId())){
                topologyObject = topologyObjectFactory.getObject();
                topologyObject.setLinkId(topologyTmpObject.getLinkId());

                topologyObject.setOppSysname(topologyTmpObject.getNeZ().getNodeId());
                topologyObject.setOppNescode(topologyTmpObject.getNeZ().getNodeNum());
                topologyObject.setOppPort(topologyTmpObject.getNeZ().getIfNum());
                topologyObject.setOppIfId(topologyTmpObject.getNeZ().getIfId());
                topologyObject.setOppIfNum(topologyTmpObject.getNeZ().getIfNum());

                isTopology = true;
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
