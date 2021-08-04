package com.nia.rca.cluster.preprocessor.listener;

import com.nia.rca.cluster.preprocessor.common.RcaCodeInfo;
import com.nia.rca.cluster.preprocessor.common.UtlCommon;
import com.nia.rca.cluster.preprocessor.data.DataShareBean;
import com.nia.rca.cluster.preprocessor.service.cluster.ClusterService;
import com.nia.rca.cluster.preprocessor.vo.BasicAlarmVo;
import com.rabbitmq.client.Channel;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class ClusterAlarmMsgListener implements ChannelAwareMessageListener {
	private static final Logger LOGGER = LoggerFactory.getLogger(ClusterAlarmMsgListener.class);

    @Autowired
    private BasicAlarmVo basicAlarmVo;

    @Autowired
    @Qualifier("ClusterService")
    private ClusterService clusterService;

    @Autowired
    private org.springframework.beans.factory.ObjectFactory<BasicAlarmVo> basicAlarmVoFactory;

	@Override
	public void onMessage(Message message, Channel channel) {
		try {
			LOGGER.info(">>>>>>>>>>[ClusterAlarmMsgListener] onMessage : " + message.toString() + " <<<<<<<<<<<<<<<<<");


            Object obj;
            String msg = new String(message.getBody());
            basicAlarmVo = basicAlarmVoFactory.getObject();

            obj = UtlCommon.jsonToObject(basicAlarmVo, msg);
		    basicAlarmVo = (BasicAlarmVo)obj;

            clusterService.startThreadPool(basicAlarmVo);
            //basicAlarmVo.setDomain(basicAlarmVo.getAlarmDomain());

           // ((Queue<BasicAlarmVo>)dataShareBean.getData(RcaCodeInfo.DATA_SHARE_NAME_ALARM_QUE)).offer(basicAlarmVo);

		} catch (Exception e) {
			LOGGER.error("=====> [ClusterAlarmMsgListener] onMessage error "+ ExceptionUtils.getStackTrace(e)+ "<=====");
		}
	}
}
