package com.codej.nia.scheduler.task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;

import com.codej.base.dto.Channel.EmChannel;
import com.codej.base.dto.SocketMessage;
import com.codej.base.dto.model.ResultMap;
import com.codej.base.utils.JsonUtil;
import com.codej.nia.service.NiaService;
import com.codej.ws.service.WebsocketService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@ConditionalOnExpression("'${myconf.websocket.enabled}' == 'true'")
public class NiaAlarmTask {

    @Autowired
    private NiaService niaService;

    @Autowired
    @Lazy
    private WebsocketService websocketService;

    @Scheduled(cron = "0 */5 * * * *") /* 5분주기 */
    // @Scheduled(cron = "*/30 * * * * *") /* 30초 주기 */
    private void sendSystemMonitoring() throws Exception {
        try {
            List<ResultMap> list = (List<ResultMap>) niaService.selectSystemMonitoringCur();
            if (list == null || list.size() == 0)
                return;

            String message = JsonUtil.convertClassToJsonString(list);

            SocketMessage socketMessage = SocketMessage.builder()
                    .type(SocketMessage.EmMessage.BROADCAST)
                    .channelName(EmChannel.SYSTEM_MONITORING)
                    .sender("SYSTEM")
                    .message(message)
                    .build();

            websocketService.sendMessage(socketMessage);
            log.info("websocket sendSystemMonitoring Message: {}", socketMessage);
        } catch (Exception e) {
            log.error(e.toString());
        }
    }

    @Scheduled(cron = "0 */5 * * * *") /* 5분주기 */
    // @Scheduled(cron = "*/30 * * * * *") /* 30초 주기 */
    private void sendIpsdnAlarm() throws Exception {
        try {
            HashMap<String, Object> param = new HashMap<String, Object>();
            param.put("IS_POLLING", true);
            List<ResultMap> list = (List<ResultMap>) niaService.selectIpsdnAlarmList(param);
            if (list == null || list.size() == 0)
                return;

            String message = JsonUtil.convertClassToJsonString(list);

            SocketMessage socketMessage = SocketMessage.builder()
                    .type(SocketMessage.EmMessage.BROADCAST)
                    .channelName(EmChannel.IPSDN_ALARM)
                    .sender("SYSTEM")
                    .message(message)
                    .build();

            websocketService.sendMessage(socketMessage);
            log.info("websocket sendIpsdnAlarm Message: {}", socketMessage);
        } catch (Exception e) {
            log.error(e.toString());
        }
    }

    @Scheduled(cron = "0 */5 * * * *")
    private void sendTransAlarm() throws Exception {
        try {
            HashMap<String, Object> param = new HashMap<String, Object>();
            param.put("IS_POLLING", true);
            List<ResultMap> list = (List<ResultMap>) niaService.selectTransmissionAlarmList(param);
            if (list == null || list.size() == 0)
                return;

            String message = JsonUtil.convertClassToJsonString(list);

            SocketMessage socketMessage = SocketMessage.builder()
                    .type(SocketMessage.EmMessage.BROADCAST)
                    .channelName(EmChannel.TRANS_ALARM)
                    .sender("SYSTEM")
                    .message(message)
                    .build();

            websocketService.sendMessage(socketMessage);
            log.info("websocket sendTransAlarm Message: {}", socketMessage);
        } catch (Exception e) {
            log.error(e.toString());
        }
    }
}
