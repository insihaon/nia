package com.nia.ai.per.ap.vo;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@Scope(value = "prototype")
@Data
public class TicketCableVo implements Serializable{
	private String ticketId;
	private String ticketCableId;
	private Boolean isRootRowSignalLine;
	private String porta;
	private String portz;
	private String sysnamea;
	private String sysnamez;
}
