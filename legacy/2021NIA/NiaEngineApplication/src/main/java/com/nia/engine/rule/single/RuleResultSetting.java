package com.nia.engine.rule.single;

import com.nia.engine.common.RcaCodeInfo;
import com.nia.engine.vo.*;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;

@Component
@Scope(value = "prototype")
public class RuleResultSetting {
	private final Logger LOGGER = Logger.getLogger(RuleResultSetting.class);

	public RcaResult setRcaResult(RcaResult rcaResult) {
		try {
			if(rcaResult.getRcaResultCode() != null) {
				BasicAlarmVo $a1 = rcaResult.getRelatedAlarmList().get(0);

				rcaResult.setClusterNo($a1.getClusterno());
				rcaResult.setAlarmTime($a1.getAlarmtime());

				if($a1.getEquiptype().startsWith("POTN")) {
						rcaResult.setDomainCode(RcaCodeInfo.DOMAIN_POTN);
				}else if($a1.getEquiptype().startsWith("ROADM")){
					rcaResult.setDomainCode(RcaCodeInfo.DOMAIN_ROADM);
				}

				LOGGER.info(">>>>>>>[RcaResultSetting] rcaResultSetting rcaResult : " + rcaResult + " <<<<<<<<<<<<");
			}
		} catch (Exception e) {
			LOGGER.error(">>>>>>>[RcaResultSetting] rcaResultSetting error : " + ExceptionUtils.getStackTrace(e) + " <<<<<<<<<<<<");
		}
		return rcaResult;
	}
}
