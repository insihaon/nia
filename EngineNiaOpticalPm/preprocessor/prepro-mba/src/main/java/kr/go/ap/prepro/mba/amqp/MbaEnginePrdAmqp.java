package kr.go.ap.prepro.mba.amqp;

import kr.go.ap.core.primary.nia.dto.mba.EngineLowPmDataListDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;


@Configuration
@Slf4j
public class MbaEnginePrdAmqp {

	@Autowired
	@Qualifier("EngineMbaTicket_RabbitTemplate")
	private RabbitTemplate rabbitTemplate;

	public void sendMessageCmd(EngineLowPmDataListDto engineLowPmDataListDto) {
		try {
            log.info("sendMessageCmd : " + engineLowPmDataListDto.getLowPmDataDtoList().toString());
		    rabbitTemplate.convertAndSend(engineLowPmDataListDto);
		} catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
		}
	}
}

