package com.kt.ipms.legacy.opermgmt.grantsubsmgmt.vo;
import java.io.Serializable;

import com.kt.ipms.legacy.cmn.vo.CommonVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlBasVo;

import java.math.BigInteger;


public class TbUserAuthTxnSubVo extends CommonVo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7002182201077208984L;

	/** MEMBER VARIABLE DECLARATION START **/

	private BigInteger nuserAutSeq;

	private String suserId;

	private String suserNm;

	private String suserGradeCd;

	private String suserGradeNm;
	
	private String sposDeptOrgId;
	
	private String sposDeptOrgNm;
	
	private String sposDeptFullNm;
	
	private BigInteger grantSeq;

	private BigInteger nlvlBasSeq;
	
	private TbLvlBasVo tbLvlBasVo;

	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/

	public void setNuserAutSeq(BigInteger nuserAutSeq) {
		this.nuserAutSeq = nuserAutSeq;
	}

	public BigInteger getNuserAutSeq() {
		return nuserAutSeq;
	}

	public void setSuserId(String suserId) {
		this.suserId = suserId;
	}

	public String getSuserId() {
		return suserId;
	}

	public void setSuserGradeCd(String suserGradeCd) {
		this.suserGradeCd = suserGradeCd;
	}

	public String getSuserGradeCd() {
		return suserGradeCd;
	}

	public void setSuserGradeNm(String suserGradeNm) {
		this.suserGradeNm = suserGradeNm;
	}

	public String getSuserGradeNm() {
		return suserGradeNm;
	}

	public void setNlvlBasSeq(BigInteger nlvlBasSeq) {
		this.nlvlBasSeq = nlvlBasSeq;
	}

	public BigInteger getNlvlBasSeq() {
		return nlvlBasSeq;
	}

	public TbLvlBasVo getTbLvlBasVo() {
		return tbLvlBasVo;
	}

	public void setTbLvlBasVo(TbLvlBasVo tbLvlBasVo) {
		this.tbLvlBasVo = tbLvlBasVo;
	}

	public String getSuserNm() {
		return suserNm;
	}

	public void setSuserNm(String suserNm) {
		this.suserNm = suserNm;
	}

	public String getSposDeptOrgId() {
		return sposDeptOrgId;
	}

	public void setSposDeptOrgId(String sposDeptOrgId) {
		this.sposDeptOrgId = sposDeptOrgId;
	}

	public String getSposDeptOrgNm() {
		return sposDeptOrgNm;
	}

	public void setSposDeptOrgNm(String sposDeptOrgNm) {
		this.sposDeptOrgNm = sposDeptOrgNm;
	}

	public String getSposDeptFullNm() {
		return sposDeptFullNm;
	}

	public void setSposDeptFullNm(String sposDeptFullNm) {
		this.sposDeptFullNm = sposDeptFullNm;
	}

	public BigInteger getGrantSeq() {
		return grantSeq;
	}

	public void setGrantSeq(BigInteger grantSeq) {
		this.grantSeq = grantSeq;
	}
	
	

	/** MEMBER METHOD DECLARATION END **/
}