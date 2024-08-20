package com.kt.ipms.legacy.linkmgmt.socketmgmt.vo;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonProperty;

public class LanguageSectionVo implements Serializable {

	private static final long serialVersionUID = 4679670806267398831L;
	
	@JsonProperty("ISP")
	private UserSectionVo isp;
	
	@JsonProperty("PI")
	private UserSectionVo pi;
	
	@JsonProperty("user")
	private UserSectionVo user;

	public UserSectionVo getIsp() {
		return isp;
	}

	public void setIsp(UserSectionVo isp) {
		this.isp = isp;
	}

	public UserSectionVo getPi() {
		return pi;
	}

	public void setPi(UserSectionVo pi) {
		this.pi = pi;
	}

	public UserSectionVo getUser() {
		return user;
	}

	public void setUser(UserSectionVo user) {
		this.user = user;
	}

}
