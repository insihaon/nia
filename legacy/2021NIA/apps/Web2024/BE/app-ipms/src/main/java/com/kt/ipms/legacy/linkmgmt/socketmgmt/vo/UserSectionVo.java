package com.kt.ipms.legacy.linkmgmt.socketmgmt.vo;

import java.io.Serializable;

public class UserSectionVo implements Serializable {

	private static final long serialVersionUID = 7635218121629137702L;
	
	private NetInfoVo netinfo;
	
	private ContactVo adminContact;
	
	private ContactVo techContact;
	
	private ContactVo abuseContact;

	public NetInfoVo getNetinfo() {
		return netinfo;
	}

	public void setNetinfo(NetInfoVo netinfo) {
		this.netinfo = netinfo;
	}

	public ContactVo getAdminContact() {
		return adminContact;
	}

	public void setAdminContact(ContactVo adminContact) {
		this.adminContact = adminContact;
	}

	public ContactVo getTechContact() {
		return techContact;
	}

	public void setTechContact(ContactVo techContact) {
		this.techContact = techContact;
	}

	public ContactVo getAbuseContact() {
		return abuseContact;
	}

	public void setAbuseContact(ContactVo abuseContact) {
		this.abuseContact = abuseContact;
	}

}
