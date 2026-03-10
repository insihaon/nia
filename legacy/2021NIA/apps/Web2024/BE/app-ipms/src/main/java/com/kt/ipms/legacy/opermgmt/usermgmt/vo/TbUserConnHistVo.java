package com.kt.ipms.legacy.opermgmt.usermgmt.vo;
import java.io.Serializable;

import com.kt.ipms.legacy.cmn.vo.CommonVo;

import java.util.Date;


public class TbUserConnHistVo extends CommonVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = 910332672423775863L;

	private String suserId;
	
	private String suserNm;

	private Date dloginDt;

	private Date dlogoutDt;

	private Date dconnConfDt;

	private String suserHndsetId;

	private Integer nhndsetApySeq;

	private String suserConnResltTypeCd;

	private String suserConnResltTypeNm;

	private String suserConnErrMsgSbst;

	private String sapplSrvrId;
	
	private String sposDeptOrgId;
	
	private String sposDeptOrgNm;
	
	private String sposDeptFullNm;

	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/

	public void setSuserId(String suserId) {
		this.suserId = suserId;
	}

	public String getSuserId() {
		return suserId;
	}

	
	public String getSuserNm() {
		return suserNm;
	}

	public void setSuserNm(String suserNm) {
		this.suserNm = suserNm;
	}

	public void setDloginDt(Date dloginDt) {
		this.dloginDt = dloginDt;
	}

	public Date getDloginDt() {
		return dloginDt;
	}

	public void setDlogoutDt(Date dlogoutDt) {
		this.dlogoutDt = dlogoutDt;
	}

	public Date getDlogoutDt() {
		return dlogoutDt;
	}

	public void setDconnConfDt(Date dconnConfDt) {
		this.dconnConfDt = dconnConfDt;
	}

	public Date getDconnConfDt() {
		return dconnConfDt;
	}

	public void setSuserHndsetId(String suserHndsetId) {
		this.suserHndsetId = suserHndsetId;
	}

	public String getSuserHndsetId() {
		return suserHndsetId;
	}

	public void setNhndsetApySeq(Integer nhndsetApySeq) {
		this.nhndsetApySeq = nhndsetApySeq;
	}

	public Integer getNhndsetApySeq() {
		return nhndsetApySeq;
	}

	public void setSuserConnResltTypeCd(String suserConnResltTypeCd) {
		this.suserConnResltTypeCd = suserConnResltTypeCd;
	}

	public String getSuserConnResltTypeCd() {
		return suserConnResltTypeCd;
	}

	public void setSuserConnResltTypeNm(String suserConnResltTypeNm) {
		this.suserConnResltTypeNm = suserConnResltTypeNm;
	}

	public String getSuserConnResltTypeNm() {
		return suserConnResltTypeNm;
	}

	public void setSuserConnErrMsgSbst(String suserConnErrMsgSbst) {
		this.suserConnErrMsgSbst = suserConnErrMsgSbst;
	}

	public String getSuserConnErrMsgSbst() {
		return suserConnErrMsgSbst;
	}

	public void setSapplSrvrId(String sapplSrvrId) {
		this.sapplSrvrId = sapplSrvrId;
	}

	public String getSapplSrvrId() {
		return sapplSrvrId;
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
	
	

	/** MEMBER METHOD DECLARATION END **/
}