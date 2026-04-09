package com.nia.engine.service;

import com.nia.engine.vo.*;

import java.util.List;

public interface RcaTicketManagerService {

    void createPerformanceTicket(PerformanceClusterVo performanceClusterVo);

    void createRcaTicket(List<RcaResult> ptnRcaResultList);

    void uiSendTicketResult(RcaEngineResult rcaEngineResult);

    void ticketClearCheck(RCATicket rcaTicket);

    RCATicket getParentTicket(String ticketId);

}
