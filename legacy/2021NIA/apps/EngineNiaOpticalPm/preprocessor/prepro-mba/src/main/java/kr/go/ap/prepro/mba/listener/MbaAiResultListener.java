package kr.go.ap.prepro.mba.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.go.ap.core.primary.nia.dto.linkage.model.ModelResultDto;
import kr.go.ap.prepro.mba.service.LineMonitoringHdlService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MbaAiResultListener {

    private final LineMonitoringHdlService lineMonitoringHdlService;

    @RabbitListener(queues = "${spring.rabbitmq.mbaAiModelResultQueue}",
            containerFactory = "rabbitListenerContainerFactory")
    public void onMessage(Message message) {
        ObjectMapper mapper = null;
        byte[] body = message.getBody(); // 실제 바이트 배열
        String json = new String(body, StandardCharsets.UTF_8); // 문자열로 변환
        List<ModelResultDto> list = null;

        try {
            log.info(">> [PerformanceMsgListener] onMessage : {}", json);

            mapper = new ObjectMapper();

            list = mapper.readValue(json, new TypeReference<List<ModelResultDto>>() {});

            lineMonitoringHdlService.lineMonitoringHdlProcessor(list);
        } catch (NullPointerException | JsonProcessingException e) {
            log.error("=====> onMessage error ", e);
        }
    }
}
