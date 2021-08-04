package com.nia.engine.service;

import com.nia.engine.vo.*;

import java.util.List;

public interface RcaTicketHandlingService {

    void ticketStatusModify(RCATicketHandlingStatus rcaTicketHandlingStatus);

    void removeRcaTicket(String ticketId);

}
