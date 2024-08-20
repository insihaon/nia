package com.kt.ipms.legacy.opermgmt.grantsubsmgmt.vo;
import java.io.Serializable;
import java.util.List;

import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class TbUserAuthTxnSubListVo extends CommonVo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7796982663854477478L;

	/** MEMBER VARIABLE DECLARATION START **/
	
	
	private String suserId;

	private String suserNm;
	
	private String suserGradeCd;
	
	private String nrequestTypeCd;
	
	private List<TbUserAuthTxnSubVo> tbUserAuthTxnSubVos;
	
	private int totalCount;
	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/
	
	public int getTotalCount() {
		return this.totalCount;
	}
	
	public List<TbUserAuthTxnSubVo> getTbUserAuthTxnSubVos() {
		return tbUserAuthTxnSubVos;
	}

	public void setTbUserAuthTxnSubVos(List<TbUserAuthTxnSubVo> tbUserAuthTxnSubVos) {
		this.tbUserAuthTxnSubVos = tbUserAuthTxnSubVos;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

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

	public String getNrequestTypeCd() {
		return nrequestTypeCd;
	}

	public void setNrequestTypeCd(String nrequestTypeCd) {
		this.nrequestTypeCd = nrequestTypeCd;
	}
	
	
	/** MEMBER METHOD DECLARATION END **/
}