package kr.go.ap.linkage.mba.amqp;

import kr.go.ap.core.primary.nia.dto.linkage.model.ModelSendDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;


@Configuration
@Slf4j
public class MbaAiModelPrdAmqp {

	@Autowired
	@Qualifier("MbaAiModel_RabbitTemplate")
	private RabbitTemplate rabbitTemplate;
	
	public void sendMessageCmd(ModelSendDto modelSendDto) {
		try{
			log.info("=====> [sendMessageCmd] : modelSendDto " + modelSendDto.toString() + " <=====");
			rabbitTemplate.convertAndSend(modelSendDto);
			//Thread.sleep(1000);
		} catch (Exception e) {
            log.error("=====> [sendMessageCmd] : error " + ExceptionUtils.getStackTrace(e)+ " <=====");
		}
	}
}
