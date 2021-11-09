package com.nia.engine.service.impl;

import com.nia.engine.common.*;
//import com.nia.engine.service.MultiDomainRcaService;
import com.nia.engine.rule.single.*;
import com.nia.engine.service.RcaTicketManagerService;
import com.nia.engine.service.SingleDomainRcaService;
import com.nia.engine.vo.*;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.StatelessKieSession;
import org.kie.api.runtime.rule.FactHandle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.*;

@Component
@Scope(value = "prototype", proxyMode= ScopedProxyMode.TARGET_CLASS)
public class SingleDomainRcaServiceImpl {
	private final Logger LOGGER = Logger.getLogger(SingleDomainRcaServiceImpl.class);
	
	@Autowired
    @Qualifier("RcaTicketManagerService")
	private RcaTicketManagerService rcaTicketManager;

	@Autowired
	private PotnSingleRule potnSingleRule;

	@Autowired
	private RoadmSingleRule roadmAlarmRule;

	@Autowired
	private SwitchSingleRule switchAlarmRule;

	private List<BasicAlarmVo> inputPotnAlarmList;
	private List<BasicAlarmVo> inputRoadmAlarmList;
	private List<BasicAlarmVo> inputSwitchAlarmList;
	
	private List<RcaResult> rcaResultList;
	
	//@Override
	public void singleDomainHdlProcessor(ClusterObject clusterObject){
		try {
			inputPotnAlarmList = new ArrayList<BasicAlarmVo>();
			inputRoadmAlarmList = new ArrayList<BasicAlarmVo>();
			inputSwitchAlarmList = new ArrayList<BasicAlarmVo>();

			for(BasicAlarmVo basicAlarmVo :  clusterObject.getBasicAlarmtVoList()){
				if(basicAlarmVo.getEquiptype().startsWith("POTN")) {
					inputPotnAlarmList.add(basicAlarmVo);
				}else if(basicAlarmVo.getEquiptype().startsWith("ROADM")){
					basicAlarmVo.setSysname(basicAlarmVo.getSysname().split("-")[0]);
					inputRoadmAlarmList.add(basicAlarmVo);
				}else if(basicAlarmVo.getAlarmno().startsWith("I")){
					inputSwitchAlarmList.add(basicAlarmVo);
				}
			}

			rcaResultList = new ArrayList<RcaResult>();

			if(inputPotnAlarmList.size() > 0){
				Collections.sort(inputPotnAlarmList, new CompareAlarmAsc());
				RcaResult potnRcaResult = potnSingleRule.potnAlarmRule(inputPotnAlarmList);
				if(potnRcaResult.getRcaResultCode() != null) {
					rcaResultList.add(potnRcaResult);
				}
			}

			if(inputRoadmAlarmList.size() > 0){
				Collections.sort(inputRoadmAlarmList, new CompareAlarmAsc());
				RcaResult roadm8thwRcaResult = roadmAlarmRule.roadmAlarmRule(inputRoadmAlarmList);
				if(roadm8thwRcaResult.getRcaResultCode() != null) {
					rcaResultList.add(roadm8thwRcaResult);
				}
			}

			if(inputSwitchAlarmList.size() > 0){
				Collections.sort(inputSwitchAlarmList, new CompareAlarmAsc());
				RcaResult switchRcaResult = switchAlarmRule.switchAlarmRule(inputSwitchAlarmList);
				if(switchRcaResult.getRcaResultCode() != null) {
					rcaResultList.add(switchRcaResult);
				}
			}


			if(rcaResultList.size() > 0){
                rcaTicketManager.createRcaTicket(rcaResultList);
				LOGGER.info(">>>>>>>[SingleDomainRcaService] rcaResultList : " + rcaResultList.size() +" <<<<<<<<<<<<");
			}
		} catch (Exception e) {
			LOGGER.error(">>>>>>>[SingleDomainRcaService] singleDomainHdlProcessor error : " +ExceptionUtils.getStackTrace(e)+" <<<<<<<<<<<<");
		} 	
	}

	class CompareAlarmAsc implements Comparator<Object>{
	    @Override
	    public int compare(Object o1, Object o2) {
	    	if(o1 instanceof BasicAlarmVo){
	    		return ((BasicAlarmVo)o1).getReceivetime().compareTo(((BasicAlarmVo)o2).getReceivetime());
	    	}else{
	    		return 0;
	    	}
	    }        
	}
}
