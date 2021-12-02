package com.nia.engine.service.impl;

import com.nia.engine.common.KnowledgeSessionHelper;
import com.nia.engine.common.RcaCodeInfo;
import com.nia.engine.common.UtlDateHelper;
import com.nia.engine.data.DataShareBean;
import com.nia.engine.rule.single.RuleResultSetting;
import com.nia.engine.vo.*;
import lombok.Synchronized;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.FactHandle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Component
@Scope(value = "prototype", proxyMode= ScopedProxyMode.TARGET_CLASS)
public class MultiDomainRcaServiceImpl {
	private final Logger LOGGER = Logger.getLogger(MultiDomainRcaServiceImpl.class);

	@Autowired
	private org.springframework.beans.factory.ObjectFactory<RcaResult> rcaResultObjectFactory;

	@Autowired
	private org.springframework.beans.factory.ObjectFactory<MultiResult> multiResultObjectFactory;

	@Autowired
	private org.springframework.beans.factory.ObjectFactory<Flag> flagObjectFactory;

	@Autowired
	private DataShareBean dataShareBean;

	@Autowired
	private RuleResultSetting ruleResultSetting;

	private KieSession sessionStatefull = null;
	private List<FactHandle> ClusterListHandle;
	private KieContainer kieContainer;
	private List<RcaResult> tmpRcaResultList;

	@Synchronized
	public List<RcaResult> multiDomainHdlProcessor(List<RcaResult> rcaResultList){
		MultiResult multiResult;
		RcaResult rcaResult;
		Flag flag;
		try {
			multiResult = multiResultObjectFactory.getObject();
			flag = flagObjectFactory.getObject();

			tmpRcaResultList = new ArrayList<>();

			kieContainer = KnowledgeSessionHelper.createRuleBase();

			// session 및 전역변수 설정부
			sessionStatefull = KnowledgeSessionHelper.getStatefulKnowledgeSession(kieContainer, "ksession-multi");

			sessionStatefull.setGlobal("multiResult", multiResult);

			ClusterListHandle = new LinkedList<FactHandle>();

			for(RcaResult rcaResult1 : rcaResultList) {
				ClusterListHandle.add(sessionStatefull.insert(rcaResult1));
			}

			ClusterListHandle.add(sessionStatefull.insert(flag));

			sessionStatefull.fireAllRules();

			if(multiResult != null && multiResult.getMultiRcaResultCodeList().size() > 0){
				rcaResult = ruleResultSetting.setRcaResult(multiResult.getRcaResult());

				for(String multiRcaResultCode : rcaResult.getMultiRcaResultCodeList()){
					for(RcaResult result : rcaResultList){
						if(!result.getRcaResultCode().equals(multiRcaResultCode)){
							tmpRcaResultList.add(result);
						}
					}
				}

				tmpRcaResultList.add(rcaResult);
			} else {
				tmpRcaResultList = rcaResultList;
			}
		} catch (Exception e) {
			LOGGER.error(">>>>>>>[MultiDomainRcaService] multiDomainHdlProcessor error : " +ExceptionUtils.getStackTrace(e)+" <<<<<<<<<<<<");
		} finally {
			for (FactHandle factHandle : ClusterListHandle) {
				sessionStatefull.delete(factHandle);
			}

			ClusterListHandle.clear();
			sessionStatefull.dispose();
			sessionStatefull.destroy();
		}

		return tmpRcaResultList;
	}
}
