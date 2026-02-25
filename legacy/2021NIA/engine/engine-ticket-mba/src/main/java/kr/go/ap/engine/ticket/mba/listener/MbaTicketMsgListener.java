package kr.go.ap.engine.ticket.mba.listener;

import kr.go.ap.core.primary.nia.dto.mba.EngineLowPmDataListDto;
import kr.go.ap.engine.ticket.mba.service.ticket.TicketCreateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MbaTicketMsgListener  {

    private final TicketCreateService ticketCreateService;

    private final ObjectFactory<EngineLowPmDataListDto> engineLowPmDataListDtoObjectFactory;

    @RabbitListener(queues = "${spring.rabbitmq.engineMbaTicketQueue}",
            containerFactory = "rabbitListenerContainerFactory")
	public void onMessage(EngineLowPmDataListDto engineLowPmDataListDto) {

		try {
            log.info("onMessage : " + engineLowPmDataListDto.getLowPmDataDtoList().toString());

            ticketCreateService.createMbaTicket(engineLowPmDataListDto);
        } catch (Exception e) {
            log.error("onMessage error "+ ExceptionUtils.getStackTrace(e));
		}
	}
}
