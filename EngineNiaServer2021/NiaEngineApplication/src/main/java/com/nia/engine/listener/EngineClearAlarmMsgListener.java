package com.nia.engine.listener;

import com.nia.engine.common.RcaCodeInfo;
import com.nia.engine.data.DataShareBean;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.rabbitmq.client.Channel;

import java.util.concurrent.ConcurrentLinkedQueue;

@Service("EngineClearAlarmMsgListener")
public class EngineClearAlarmMsgListener implements ChannelAwareMessageListener {
	private static final Logger LOGGER = LoggerFactory.getLogger(EngineClearAlarmMsgListener.class);

    @Autowired
    private DataShareBean dataShareBean;

	@Override
	public void onMessage(Message message, Channel channel) {
		LOGGER.info("==========>[EngineClearAlarmMsgListener] onMessage : "+new String(message.getBody())+"<==============");
		try {
//            while (true) {
//                if (dataShareBean.isContainsKey(RcaCodeInfo.DATA_SHARE_NAME_IS_START)) {
//                    if ((Boolean) dataShareBean.getData(RcaCodeInfo.DATA_SHARE_NAME_IS_START)) {
//            Object obj;
//            String msg = new String(message.getBody());
//            basicAlarmVo = basicAlarmVoFactory.getObject();
//
//            obj = UtlCommon.jsonToObject(basicAlarmVo, msg);
//            basicAlarmVo = (BasicAlarmVo)obj;

            Object obj;
            String msg = new String(message.getBody()).replaceAll("\"", "");

            //  ticketClear(msg);

            ((ConcurrentLinkedQueue<String>) dataShareBean.getData(RcaCodeInfo.DATA_SHARE_NAME_CLEAR_AL_LIST)).add(msg);

                        //      ticketClear(msg);
//                        break;
//                    }
//                }
//            }
		} catch (Exception e) {
			LOGGER.error("==========>[EngineClearAlarmMsgListener] onMessage error : "+ExceptionUtils.getStackTrace(e)+" <==============");
		}
	}

//    @Async("clearAlarmThreadPoolTaskExecutor")
    public void ticketClear(String alarmNo){
        try{
         //   ticketClearService.clearAlarm(alarmNo);
        }catch (Exception e){
            LOGGER.error("=====> [TopasAlarmHdlService] ticketClear error "+ ExceptionUtils.getStackTrace(e)+ "<=====");
        }
    }
}
