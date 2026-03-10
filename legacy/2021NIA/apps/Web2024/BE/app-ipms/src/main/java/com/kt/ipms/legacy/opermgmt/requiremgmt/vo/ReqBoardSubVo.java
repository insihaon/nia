package com.kt.ipms.legacy.opermgmt.requiremgmt.vo;

import java.io.Serializable;

import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class ReqBoardSubVo extends CommonVo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8945172415621514154L;
	private String rboardTypeSubCd;
	
	private String rboardTypeCd;
	
	private String rboardTypeSubnm;

	public String getRboardTypeCd() {
		return rboardTypeCd;
	}

	public void setRboardTypeCd(String rboardTypeCd) {
		this.rboardTypeCd = rboardTypeCd;
	}
	
	public String getRboardTypeSubCd() {
		return rboardTypeSubCd;
	}

	public void setRboardTypeSubCd(String rboardTypeSubCd) {
		this.rboardTypeSubCd = rboardTypeSubCd;
	}

	public String getRboardTypeSubnm() {
		return rboardTypeSubnm;
	}

	public void setRboardTypeSubnm(String rboardTypeSubnm) {
		this.rboardTypeSubnm = rboardTypeSubnm;
	}
	
}
