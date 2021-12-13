package com.nia.engine.service;

import com.nia.engine.vo.aiTraffic.anomalous.AnomalousTrafficListVo;
import com.nia.engine.vo.aiTraffic.noxious.NoxiousTrafficListVo;

public interface RcaTrafficTicketService {

    void createAnomalousTrafficTicket(AnomalousTrafficListVo anomalousTrafficListVo);

    void createNoxiousTrfficTicket(NoxiousTrafficListVo noxiousTrafficListVo);
}
