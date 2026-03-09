package com.nia.engine.service.impl;

import com.nia.engine.mapper.TicketMapper;
import com.nia.engine.vo.SopTimeoutVo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service("SopTimeoutAiService")
public class SopTimeoutAiService {
    private final Logger LOGGER = Logger.getLogger(SopTimeoutAiService.class);

    @Autowired
    private TicketMapper ticketMapper;


    public void processTimeoutSop(){
        HashMap<String,String> map = new HashMap<>();

    //    0. inner join에 걸리지 않는 티켓 삭제
        ticketMapper.deleteNotTimeoutData();

    //    1. sendtime sop 조회
        List<SopTimeoutVo> sopTimeoutVoList = ticketMapper.selectAiTimeoutTicket();
        for (SopTimeoutVo list:sopTimeoutVoList){
            //    2. 30초 이상 티켓이 model 값이 null 이면 0 insert
                map.put("ticketId",list.getTicketId());
                ticketMapper.updateTimeoutTicket(map);
                LOGGER.info("[processTimeoutSop] >>>>> Occur Timeout !!! ");
                LOGGER.info("[processTimeoutSop] >>>>> update Sop ticket_id : " + list.getTicketId());
        }

        //    3. 전체 조회 후 model 값이 null이 아니면 sendtime sop에서 삭제
        List<SopTimeoutVo> sopAllTimeoutVoList = ticketMapper.selectAllTimeoutTicket();
        for (SopTimeoutVo list:sopAllTimeoutVoList){
                ticketMapper.deleteAiModelIsNotNullData(list.getTicketId());
            LOGGER.info("[processTimeoutSop] >>>>> delete Sop ticket_id : " + list.getTicketId());
        }


    }



}
