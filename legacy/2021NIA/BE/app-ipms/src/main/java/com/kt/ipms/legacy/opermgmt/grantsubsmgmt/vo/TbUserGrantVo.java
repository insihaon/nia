package com.kt.ipms.legacy.opermgmt.grantsubsmgmt.vo;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class TbUserGrantVo extends CommonVo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7159440802079340849L;

	private BigInteger grantSeq;

	private String suserId;

	private String suserNm;

	private String suserGradeCd;

	private String suserGradeNm;
	
	private String sposDeptOrgId;
	
	private String sposDeptOrgNm;
	
	private String sposDeptFullNm;
	
	private String nrequestTypeCd;
	
	private String nrequestTypeNm;
	
	private String sUserNm;
	
	private Date dCreateDt;
	
	private Date dCompleteDt;
	
	private String sCreateId;
	
	private String sModifyId;

	public BigInteger getGrantSeq() {
		return grantSeq;
	}

	public void setGrantSeq(BigInteger grantSeq) {
		this.grantSeq = grantSeq;
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

	public String getSuserGradeNm() {
		return suserGradeNm;
	}

	public void setSuserGradeNm(String suserGradeNm) {
		this.suserGradeNm = suserGradeNm;
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

	public String getNrequestTypeCd() {
		return nrequestTypeCd;
	}

	public void setNrequestTypeCd(String nrequestTypeCd) {
		this.nrequestTypeCd = nrequestTypeCd;
	}

	public String getNrequestTypeNm() {
		return nrequestTypeNm;
	}

	public void setNrequestTypeNm(String nrequestTypeNm) {
		this.nrequestTypeNm = nrequestTypeNm;
	}

	public String getsUserNm() {
		return sUserNm;
	}

	public void setsUserNm(String sUserNm) {
		this.sUserNm = sUserNm;
	}

	public Date getdCreateDt() {
		return dCreateDt;
	}

	public void setdCreateDt(Date dCreateDt) {
		this.dCreateDt = dCreateDt;
	}

	public Date getdCompleteDt() {
		return dCompleteDt;
	}

	public void setdCompleteDt(Date dCompleteDt) {
		this.dCompleteDt = dCompleteDt;
	}

	public String getsCreateId() {
		return sCreateId;
	}

	public void setsCreateId(String sCreateId) {
		this.sCreateId = sCreateId;
	}

	public String getsModifyId() {
		return sModifyId;
	}

	public void setsModifyId(String sModifyId) {
		this.sModifyId = sModifyId;
	} 
	
	
}
