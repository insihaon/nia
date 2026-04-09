package kr.go.ap.engine.ticket.mba.amqp;

import kr.go.ap.core.primary.nia.dto.rca.engine.ui.RcaEngineResultDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;


@Configuration
@Slf4j
public class EngineToUiTicketPrdAmqp {

	@Autowired
	@Qualifier("EngineToUi_RabbitTemplate")
	private RabbitTemplate rabbitTemplate;
	
	public void sendMessageCmd(RcaEngineResultDto rcaEngineResult) {
		try{
			log.info("=====> [sendMessageCmd] : rcaEngineResult " +rcaEngineResult.toString() + " <=====");
			rabbitTemplate.convertAndSend(rcaEngineResult);
			//Thread.sleep(1000);
		} catch (Exception e) {
            log.error("=====> [sendMessageCmd] : error " + ExceptionUtils.getStackTrace(e)+ " <=====");
		}
	}
}
