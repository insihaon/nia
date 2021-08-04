package com.nia.engine.service;

import com.nia.engine.vo.RCATicket;
import com.nia.engine.vo.RcaTicketResult;

public interface RcaTicketMergeService {
    /*
     * Ticket 중복 체크
     */
    RcaTicketResult rcaTicketMerge(RCATicket rcaTicket);
}
