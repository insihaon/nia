package com.nia.engine.vo;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Data
@Scope(value = "prototype")
public class ClearTicketResultVo {
    public String clearAlarmTicket;
    public boolean isAllClearTicket;

    public String getClearAlarmTicket() {
        return clearAlarmTicket;
    }

    public boolean getisAllClearTicket() {
        return isAllClearTicket;
    }
}