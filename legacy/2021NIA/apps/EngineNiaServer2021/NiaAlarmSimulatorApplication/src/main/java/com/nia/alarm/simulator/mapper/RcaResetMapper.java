package com.nia.alarm.simulator.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RcaResetMapper {
    void deleteTicket();
    void deleteTicketAl();
    void deleteTicketCable();
}
