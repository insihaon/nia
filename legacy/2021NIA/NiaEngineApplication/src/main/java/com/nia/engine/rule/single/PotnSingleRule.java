package com.nia.engine.rule.single;

import com.nia.engine.common.KnowledgeSessionHelper;
import com.nia.engine.vo.BasicAlarmVo;
import com.nia.engine.vo.ClusterObject;
import com.nia.engine.vo.Flag;
import com.nia.engine.vo.RcaResult;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.FactHandle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
@Scope(value = "prototype")
public class PotnSingleRule {
	private final Logger LOGGER = Logger.getLogger(PotnSingleRule.class);

	private KieSession sessionStatefull = null;
	private KieContainer kieContainer;

	@Autowired
	private RuleResultSetting ruleResultSetting;

	@Autowired
	private ClusterObject clusterObject;
	private List<FactHandle> ClusterListHandle;
	private RcaResult rcaResult = new RcaResult();

	public RcaResult potnAlarmRule(List<BasicAlarmVo> inputAlarmList){
		LOGGER.info(">>>>>>>[PotnSingleRule] potnSingleRule " + inputAlarmList +" <<<<<<<<<<<<");
		try {
			RcaResult potnRcaResult = new RcaResult();
			Flag flag = new Flag();
			kieContainer = KnowledgeSessionHelper.createRuleBase();

			// session 및 전역변수 설정부
			sessionStatefull = KnowledgeSessionHelper.getStatefulKnowledgeSession(kieContainer, "ksession-potn");

			sessionStatefull.setGlobal("rcaResult", potnRcaResult);

			ClusterListHandle = new LinkedList<FactHandle>();
			if(inputAlarmList.size() > 0){
				for(int i=0; i<inputAlarmList.size(); i++) {
					ClusterListHandle.add(sessionStatefull.insert(inputAlarmList.get(i)));
					ClusterListHandle.add(sessionStatefull.insert(flag));
				}
			}
			LOGGER.info(">>>>>>>[PotnSingleRule] potnAlarmRule inputpotnAlarmList : " + inputAlarmList +" <<<<<<<<<<<<");
			sessionStatefull.fireAllRules();

			for(int i=0; i<inputAlarmList.size(); i++) {
				sessionStatefull.delete(ClusterListHandle.get(i));
			}

			rcaResult = ruleResultSetting.setRcaResult(potnRcaResult);
		} catch (Exception e) {
			LOGGER.error(">>>>>>>[PotnSingleRule] potnAlarmRule error : " + ExceptionUtils.getStackTrace(e)+" <<<<<<<<<<<<");
		} finally {
			sessionStatefull.dispose();
		}
		return rcaResult;
	}
}
