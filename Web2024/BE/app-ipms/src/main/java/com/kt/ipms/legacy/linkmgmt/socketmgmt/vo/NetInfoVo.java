package com.kt.ipms.legacy.linkmgmt.socketmgmt.vo;

public class NetInfoVo extends WhoisCommonVo {

	private static final long serialVersionUID = -6265918813086694932L;
	
	private String range;
	
	private String prefix;
	
	private String netName;
	
	private String servName;
	
	private String orgID;
	
	private String regDate;
	
	private String publishes;

	public String getRange() {
		return range;
	}

	public void setRange(String range) {
		this.range = range;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getNetName() {
		return netName;
	}

	public void setNetName(String netName) {
		this.netName = netName;
	}

	public String getServName() {
		return servName;
	}

	public void setServName(String servName) {
		this.servName = servName;
	}

	public String getOrgID() {
		return orgID;
	}

	public void setOrgID(String orgID) {
		this.orgID = orgID;
	}

	public String getRegDate() {
		return regDate;
	}

	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}

	public String getPublishes() {
		return publishes;
	}

	public void setPublishes(String publishes) {
		this.publishes = publishes;
	}

}
