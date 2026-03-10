package com.kt.ipms.legacy.opermgmt.limitmgmt.vo;
import java.io.Serializable;

import com.kt.ipms.legacy.cmn.vo.CommonVo;


public class TbAuditAssignBasVo extends CommonVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = 412241374497033853L;

	private Double nmainNodeAssignValue;

	private Double nmainNodeAssignNoValue;

	private Double nmainNodeDhcpValue;

	private Double nmainNodeTicketCntValue;

	private Double nmainNodeNeossIpblockChkValue;

	private String scomment;

	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/

	public void setScomment(String scomment) {
		this.scomment = scomment;
	}

	public Double getNmainNodeAssignValue() {
		return nmainNodeAssignValue;
	}

	public void setNmainNodeAssignValue(Double nmainNodeAssignValue) {
		this.nmainNodeAssignValue = nmainNodeAssignValue;
	}

	public Double getNmainNodeAssignNoValue() {
		return nmainNodeAssignNoValue;
	}

	public void setNmainNodeAssignNoValue(Double nmainNodeAssignNoValue) {
		this.nmainNodeAssignNoValue = nmainNodeAssignNoValue;
	}

	public Double getNmainNodeDhcpValue() {
		return nmainNodeDhcpValue;
	}

	public void setNmainNodeDhcpValue(Double nmainNodeDhcpValue) {
		this.nmainNodeDhcpValue = nmainNodeDhcpValue;
	}

	public Double getNmainNodeTicketCntValue() {
		return nmainNodeTicketCntValue;
	}

	public void setNmainNodeTicketCntValue(Double nmainNodeTicketCntValue) {
		this.nmainNodeTicketCntValue = nmainNodeTicketCntValue;
	}

	public Double getNmainNodeNeossIpblockChkValue() {
		return nmainNodeNeossIpblockChkValue;
	}

	public void setNmainNodeNeossIpblockChkValue(
			Double nmainNodeNeossIpblockChkValue) {
		this.nmainNodeNeossIpblockChkValue = nmainNodeNeossIpblockChkValue;
	}

	public String getScomment() {
		return scomment;
	}

	/** MEMBER METHOD DECLARATION END **/
}