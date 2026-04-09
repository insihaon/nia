package com.nia.alarm.ip.preprocessor.service.impl.pasing;

import com.nia.alarm.ip.preprocessor.amqp.ClusterPrdAmqp;
import com.nia.alarm.ip.preprocessor.mapper.AlarmMapper;
import com.nia.alarm.ip.preprocessor.service.pasing.PasingService;
import com.nia.alarm.ip.preprocessor.vo.alarm.BasicAlarmVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service("PasingService")
@Scope(value = "prototype")
public class PasingServiceImpl implements PasingService{
	private final static Logger LOGGER = Logger.getLogger(PasingServiceImpl.class);

    @Autowired
    private ClusterPrdAmqp clusterPrdAmqp;

    @Autowired
    @Qualifier("IpPasingService")
	private IpPasingServiceImpl ipPasingService;

	@Autowired
	private AlarmMapper alarmMapper;

    @Override
	public void alarmPasing(BasicAlarmVo basicAlarmVo){

        try {
        	switch (basicAlarmVo.getDomain()){
				case "IP" :
					basicAlarmVo = ipPasingService.alarmPasing(basicAlarmVo);
					break;
			}
			clusterPrdAmqp.sendMessageCmd(basicAlarmVo);
		}catch( Exception e ) {
			LOGGER.error(">>>>>>>>>>[PasingService] alarmPasing("+basicAlarmVo.getAlarmno()+") error : " + ExceptionUtils.getStackTrace(e) + " <<<<<<<<<<<<<<<<<");
		}
	}
}
