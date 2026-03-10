package com.kt.ipms.legacy.linkmgmt.errormgmt.vo;

import java.io.Serializable;
import java.util.List;

import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class ErrorMgmtListVo extends CommonVo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1589724773974990414L;

	private int totalCount;
	
	private List<ErrorMgmtVo> errorMgmtVos;
	
	private String sModifyId;
	
	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public List<ErrorMgmtVo> getErrorMgmtVos() {
		return errorMgmtVos;
	}

	public void setErrorMgmtVos(List<ErrorMgmtVo> errorMgmtVos) {
		this.errorMgmtVos = errorMgmtVos;
	}

	public String getsModifyId() {
		return sModifyId;
	}

	public void setsModifyId(String sModifyId) {
		this.sModifyId = sModifyId;
	}
	
	

}
