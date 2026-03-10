package com.nia.engine.service;

import com.nia.engine.vo.aiToEngine.AiToEngineAnoVo;
import com.nia.engine.vo.aiToEngine.AiToEngineNoxVo;

public interface RcaMailingService {

    //NTT 장애 비장애
    boolean NttsendMail(AiToEngineNoxVo aiToEngineNoxVo);
    boolean NttFttsendMail(AiToEngineNoxVo aiToEngineNoxVo);

    //ATT2 장애 비장애
    boolean attSendMail(AiToEngineAnoVo aiToEngineAno);
    boolean AttFttsendMail(AiToEngineAnoVo aiToEngineAno);

    

    //ATT2 비장애,장애
}
