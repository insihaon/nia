package com.kt.ipms.legacy.linkmgmt.socketmgmt.vo;

import java.io.Serializable;

public class WhoisCommonVo implements Serializable {

	private static final long serialVersionUID = 7783675881019930520L;
	
	private String orgName;
	
	private String addr;
	
	private String zipCode;
	
	private String email;

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	
	

}
