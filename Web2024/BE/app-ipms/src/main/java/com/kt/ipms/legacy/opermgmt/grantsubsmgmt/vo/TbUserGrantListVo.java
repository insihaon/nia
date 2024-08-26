package com.kt.ipms.legacy.opermgmt.grantsubsmgmt.vo;

import java.io.Serializable;
import java.util.List;

import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class TbUserGrantListVo extends CommonVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2746134894005852500L;
	
	private String suserId;

	private String suserNm;
	
	private String suserGradeCd;
	
	private List<TbUserGrantVo> tbUserGrantVos;
	
	private int totalCount;

	public String getSuserId() {
		return suserId;
	}

	public void setSuserId(String suserId) {
		this.suserId = suserId;
	}

	public String getSuserNm() {
		return suserNm;
	}

	public void setSuserNm(String suserNm) {
		this.suserNm = suserNm;
	}

	public String getSuserGradeCd() {
		return suserGradeCd;
	}

	public void setSuserGradeCd(String suserGradeCd) {
		this.suserGradeCd = suserGradeCd;
	}

	
	public List<TbUserGrantVo> getTbUserGrantVos() {
		return tbUserGrantVos;
	}

	public void setTbUserGrantVos(List<TbUserGrantVo> tbUserGrantVos) {
		this.tbUserGrantVos = tbUserGrantVos;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	
	
	
}
