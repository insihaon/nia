package com.nia.engine.vo;

import lombok.Data;
import lombok.ToString;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@Data
@ToString
@Scope(value = "prototype")
public class RCATicketCntVo implements Serializable {
	private static final long serialVersionUID = 3170781290435579732L;

	private String ticketId;
	private int totalAlarmCount;
	private int relatedAlarmCount;
	private int childCount;
}
