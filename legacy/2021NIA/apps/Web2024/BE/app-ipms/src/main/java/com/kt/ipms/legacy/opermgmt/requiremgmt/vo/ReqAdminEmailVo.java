package com.kt.ipms.legacy.opermgmt.requiremgmt.vo;

import java.io.Serializable;

import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class ReqAdminEmailVo extends CommonVo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 760241247076985802L;
	private String sUserId;
	private String sUserNm;
	private String sUserEmail;
	
	public String getsUserId() {
		return sUserId;
	}
	public void setsUserId(String sUserId) {
		this.sUserId = sUserId;
	}
	public String getsUserNm() {
		return sUserNm;
	}
	public void setsUserNm(String sUserNm) {
		this.sUserNm = sUserNm;
	}
	public String getsUserEmail() {
		return sUserEmail;
	}
	public void setsUserEmail(String sUserEmail) {
		this.sUserEmail = sUserEmail;
	}
	
	
}
