package com.nia.engine.listener;

import com.nia.engine.common.UtlCommon;
import com.nia.engine.mapper.AutoProcessMapper;
import com.nia.engine.service.RcaMailingService;
import com.nia.engine.service.TicketService;
import com.nia.engine.vo.RCATicket;
import com.nia.engine.vo.aiToEngine.AiToEngineAnoVo;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class NiaAiAnoToEngineMsgListener implements ChannelAwareMessageListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(NiaAiAnoToEngineMsgListener.class);
    @Autowired
    private ObjectFactory<AiToEngineAnoVo> anomalousTrafficVoObjectFactory;

    @Autowired
    private AutoProcessMapper autoProcessMapper;

    @Autowired
    @Qualifier("RcaMailingService")
    private RcaMailingService rcaMailingService;
    @Autowired
    @Qualifier("TicketService")
    private TicketService ticketService;



    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        LOGGER.info("==========>[NiaAiAnoToEngineMsgListener] onMessage : "+new String(message.getBody())+"<==============");

        try {
            Object obj;
            String msg = new String(message.getBody());
            HashMap<String,String> map;
            String sopId = "";
            AiToEngineAnoVo aiToEngineAno = anomalousTrafficVoObjectFactory.getObject();
            obj = UtlCommon.jsonToObject(aiToEngineAno, msg);
            aiToEngineAno = (AiToEngineAnoVo)obj;
        LOGGER.info("AI 판단 결과 >>>>>>>> "+aiToEngineAno.getZero1Model()+","+aiToEngineAno.getZero1Entropy());

            if (0 ==aiToEngineAno.getZero1Model()){
                //장애 (AI 데이터 업데이트)

                if (autoProcessMapper.selectParentTicket(aiToEngineAno.getTicketId()) == null){
                    rcaMailingService.attSendMail(aiToEngineAno);
                    LOGGER.info(">>>>>>> [NiaAiAnoToEngineMsgListener] ATT2 장애 메일링");
                }else{
                    LOGGER.info(">>>>>>> [NiaAiAnoToEngineMsgListener] ATT2 장애 메일링 하지않음( ParentTicket exists. )");
                }
                /*
                 * 티켓 발생 하자마자 메일링 하는 로직으로 변경 : myk 240404
                 * */

                map = new HashMap<>();
                map.put("model", String.valueOf(aiToEngineAno.getZero1Model()));
                map.put("entropy", String.valueOf(aiToEngineAno.getZero1Entropy()));
                map.put("ticketId", aiToEngineAno.getTicketId());
                autoProcessMapper.updateAiResult(map);
                autoProcessMapper.updateSop(map);
                LOGGER.info(">>>>>>> [NiaAiAnoToEngineMsgListener] updateAiResult :" + map);

                RCATicket rcaTicket = ticketService.selectRcaTicket(aiToEngineAno.getTicketId());
                map = new HashMap<>();
//                        autoProcessInserMap.put("selfProcessNo", String.valueOf(self_process_no+1));
                map.put("selfProcessGroup","SO");
                map.put("selfProcessType","A");
                map.put("occur_time", String.valueOf(rcaTicket.getTicketGenerationTime()));
                map.put("ticketId",rcaTicket.getTicketId());
                map.put("ticketType",rcaTicket.getTicketType());
                map.put("alarmno",null);

//                autoProcessMapper.insertAutoProcess(map);
//              ★ RcaTrafficTicketServiceImpl에서 insert 함으로 주석처리

                //sop 장애구분 장애유형 조치내용 업데이트

                map = new HashMap<>();
                map.put("model", String.valueOf(aiToEngineAno.getZero1Model()));
                map.put("entropy", String.valueOf(aiToEngineAno.getZero1Entropy()));
                map.put("ticketId",aiToEngineAno.getTicketId());
                autoProcessMapper.updateSop(map);


            }else if(1 == aiToEngineAno.getZero1Model()){
//                //비장애 (티켓타입 FTT , AI 데이터 , 상태 업데이트 , sop 업데이트)
//
                map = new HashMap<>();
                map.put("model", String.valueOf(aiToEngineAno.getZero1Model()));
                map.put("entropy", String.valueOf(aiToEngineAno.getZero1Entropy()));
                map.put("ticketId", aiToEngineAno.getTicketId());

                autoProcessMapper.updateAiFTTResult(map);
//                autoProcessMapper.updateAutoProcessFTT(aiToEngineAno.getTicketId());
////                비장애 sop insert
//

//                RCATicket rcaTicket = ticketService.selectRcaTicket(aiToEngineAno.getTicketId());
//                map = new HashMap<>();ㅔ
//                sopId = ticketService.selectSopKey();
//                map.put("sopId",sopId);
//                map.put("ticketId", aiToEngineAno.getTicketId());
//                map.put("ticketType", "FTT");
//                map.put("ticketResult", rcaTicket.getTicketRcaResultDtlCode());
//                map.put("status", "AUTO_FIN");
//                map.put("detail", "");
//                map.put("faultClassify", "비장애");
//                map.put("faultType", "장애무효");
//                map.put("faultDetailContent", "장애무효");
//                map.put("etcContent", "");
//                map.put("aiAccuracy", "0");
//                map.put("faultTypeContent", null);
//                map.put("startTime", null);
//                map.put("endTime", null);
//                if (rcaTicket.getStatus().equals("ACK")){
//                    map.put("handlingAckUser", "NIA ADMIN");
//                }else{
//                    map.put("handlingAckUser", null);
//                }
//                map.put("autoprocType","STF");
//                autoProcessMapper.insertAutoProcessSop(map);
//                LOGGER.info(">>>>>>> [NiaAiAnoToEngineMsgListener] 자가최적화 테이블 업데이트 : "+aiToEngineAno.getTicketId());
//                LOGGER.info(">>>>>>> [NiaAiAnoToEngineMsgListener] ATT2 비장애 메일링");
//
//
                map = new HashMap<>();
                map.put("model", String.valueOf(aiToEngineAno.getZero1Model()));
                map.put("entropy", String.valueOf(aiToEngineAno.getZero1Entropy()));
                map.put("ticketId",aiToEngineAno.getTicketId());
//
//                //sop 장애구분 장애유형 조치내용 업데이트
//                autoProcessMapper.updateFTTSop(map);


//                rcaMailingService.AttFttsendMail(aiToEngineAno);
            }



        }catch (Exception e){

        }

    }
}
