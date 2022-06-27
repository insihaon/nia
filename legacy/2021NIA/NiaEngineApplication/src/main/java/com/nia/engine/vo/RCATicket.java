package com.nia.engine.vo;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import lombok.Data;
import lombok.ToString;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Data
@ToString
@Scope(value = "prototype")
public class RCATicket implements Serializable {
	private static final long serialVersionUID = 3170781290435579732L;

	private String ticketId;
	private String clusterNo;
	private String ticketType;
	private Timestamp ticketGenerationTime;
	private Timestamp faultTime;
	private String rootCauseType;
	private String rootCauseCode;
	private String rootCauseDomain;
	private int relatedAlarmCnt;
	private String status;
	private String ticketRcaResultCode;
	private String ticketRcaResultDtlCode;
	private String ticketRcaResultOrigDtlCode;
	private String parentTicketId;
	private Timestamp ticketUpdateTime;
	private boolean occur;
	private String direction;
    private String profileTitle;
	private int childCnt;
	private boolean cur = false; // 과거 or 현재 티켓 구분 플래그

	private List<RCATicketAl> ticketAlList;

	public void addChild(){
		++childCnt;
	}
}
