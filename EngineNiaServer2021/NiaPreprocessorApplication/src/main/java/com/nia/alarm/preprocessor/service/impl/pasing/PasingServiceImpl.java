package com.nia.alarm.preprocessor.service.impl.pasing;

import com.nia.alarm.preprocessor.amqp.ClusterPrdAmqp;
import com.nia.alarm.preprocessor.mapper.AlarmMapper;
import com.nia.alarm.preprocessor.service.pasing.PasingService;
import com.nia.alarm.preprocessor.vo.alarm.BasicAlarmVo;
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
    @Qualifier("PotnPasingService")
	private PotnPasingServiceImpl potnPasingService;

    @Autowired
    @Qualifier("RoadmPasingService")
	private RoadmPasingServiceImpl roadmPasingService;

	@Autowired
	private AlarmMapper alarmMapper;

    @Override
	public void alarmPasing(BasicAlarmVo basicAlarmVo){

        try {
        	String equiptype = null;

        	equiptype = alarmMapper.selectEquiptype(basicAlarmVo.getSysname());

			basicAlarmVo.setAlarmno("T"+basicAlarmVo.getAlarmno());

        	if(StringUtils.isNotEmpty(equiptype)){
        		basicAlarmVo.setEquiptype(equiptype);
			}

        	switch (basicAlarmVo.getEquiptype()){
				case "POTN" :
					basicAlarmVo.setEquiptype("POTN");
					basicAlarmVo = potnPasingService.alarmPasing(basicAlarmVo);
					break;
				case "Utrans-6300p-H3" :
				case "Utrans-6300p-V9" :
					basicAlarmVo.setEquiptype("ROADM");
					basicAlarmVo = roadmPasingService.alarmPasing(basicAlarmVo);
					break;
			}
			clusterPrdAmqp.sendMessageCmd(basicAlarmVo);
		}catch( Exception e ) {
			LOGGER.error(">>>>>>>>>>[PasingService] alarmPasing("+basicAlarmVo.getAlarmno()+") error : " + ExceptionUtils.getStackTrace(e) + " <<<<<<<<<<<<<<<<<");
		}
	}
}
