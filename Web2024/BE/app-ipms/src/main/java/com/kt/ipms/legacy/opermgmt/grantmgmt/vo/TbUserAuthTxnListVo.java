package com.kt.ipms.legacy.opermgmt.grantmgmt.vo;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class TbUserAuthTxnListVo extends CommonVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = 7964210440491057523L;
	
	
	private String suserId;

	private String suserNm;
	
	private String suserGradeCd;
	
	private String nrequestTypeCd;
	
	private BigInteger grantSeq;
	
	private List<TbUserAuthTxnVo> tbUserAuthTxnVos;
	
	private int totalCount;
	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/
	public List<TbUserAuthTxnVo> getTbUserAuthTxnVos() {
		return this.tbUserAuthTxnVos;
	}
	
	public void setTbUserAuthTxnVos(List<TbUserAuthTxnVo> tbUserAuthTxnVos) {
		this.tbUserAuthTxnVos = tbUserAuthTxnVos;
	}
	
	public int getTotalCount() {
		return this.totalCount;
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

	public BigInteger getGrantSeq() {
		return grantSeq;
	}

	public void setGrantSeq(BigInteger grantSeq) {
		this.grantSeq = grantSeq;
	}
	
	
	/** MEMBER METHOD DECLARATION END **/
}