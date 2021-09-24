package com.nia.alarm.ip.preprocessor.service.impl.pasing;

import com.nia.alarm.ip.preprocessor.common.NiaCodeInfo;
import com.nia.alarm.ip.preprocessor.service.topology.TopologyService;
import com.nia.alarm.ip.preprocessor.service.pasing.CommPasingService;
import com.nia.alarm.ip.preprocessor.vo.alarm.BasicAlarmVo;
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
    private BasicAlarmVo basicAlarmVo;

	private HashMap<String, String> parameterMap;

    @Override
	public BasicAlarmVo alarmPasing(BasicAlarmVo basicAlarmVo){
        this.basicAlarmVo = basicAlarmVo;

		try {
          //  setPortPasing(this.basicAlarmVo);

//            if(!StringUtils.isEmpty(basicAlarmVo.getPtpName())) {
//                setTopology(this.basicAlarmVo);
//            }

            StringBuffer strLog = new StringBuffer();
            strLog.append("=====> [CmVmPasingService] alarmPasing <=====\n");
            strLog.append("alarmNo : " + this.basicAlarmVo.getAlarmno()+"\n");
            strLog.append("equipType : " + this.basicAlarmVo.getEquiptype()+"\n");
            strLog.append("sysname : " + this.basicAlarmVo.getSysname()+"\n");
            strLog.append("port : " + this.basicAlarmVo.getPort()+"\n");
            strLog.append("slot : " + this.basicAlarmVo.getSlot()+"\n");
            strLog.append("ptpName : " + this.basicAlarmVo.getPtpName()+"\n");

            if(this.basicAlarmVo.getTopologyObject() != null){
                strLog.append("linkId : " + this.basicAlarmVo.getTopologyObject().getLinkId()+"\n");
                strLog.append("oppSysname : " + this.basicAlarmVo.getTopologyObject().getOppSysname()+"\n");
                strLog.append("oppPtpName : " + this.basicAlarmVo.getTopologyObject().getOppPtpName()+"\n");
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
    public void setPortPasing(BasicAlarmVo basicAlarmVo) {
        String port;
        String slot;
        String[] ptpNameArr;
        String ptpName = null;

        try {
            if(StringUtils.isNotEmpty(basicAlarmVo.getAlarmloc())){
                if(basicAlarmVo.getAlarmloc().startsWith("S")){
                    if(basicAlarmVo.getAlarmloc().contains("-")){
                        basicAlarmVo.setSlot(basicAlarmVo.getAlarmloc().split("-")[0]);
                        basicAlarmVo.setPort(basicAlarmVo.getAlarmloc().split("-")[1]);
                    }else{
                        basicAlarmVo.setSlot(basicAlarmVo.getAlarmloc());
                    }
                }
                basicAlarmVo.setPtpName(basicAlarmVo.getAlarmloc());
//                if(StringUtils.isNotEmpty(ptpName)){
//                    basicAlarmVo.setPtpName(ptpName);
//                }

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

        TopologyObject topologyObject = null;
        TopologyTmpVo topologyTmpObject = null;

        try{
            parameterMap = new HashMap<String, String>();
            parameterMap.put("ptpName", basicAlarmVo.getPtpName());
            parameterMap.put("sysname", basicAlarmVo.getSysname());

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
