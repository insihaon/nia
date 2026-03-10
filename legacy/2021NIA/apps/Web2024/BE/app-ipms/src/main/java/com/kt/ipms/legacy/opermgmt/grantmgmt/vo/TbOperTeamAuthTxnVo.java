package com.kt.ipms.legacy.opermgmt.grantmgmt.vo;
import java.io.Serializable;

import com.kt.ipms.legacy.cmn.vo.CommonVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.*;

import java.math.BigInteger;


public class TbOperTeamAuthTxnVo extends CommonVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = -2735456695295065149L;

	private String sposDeptOrgId;
	
	private String sposDeptOrgNm;
	
	private String sposDeptOrgFullNm;

	private BigInteger nlvlBasSeq;

	private String scomment;

	private TbLvlBasVo tbLvlBasVo;
	
	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/

	public void setSposDeptOrgId(String sposDeptOrgId) {
		this.sposDeptOrgId = sposDeptOrgId;
	}

	public String getSposDeptOrgId() {
		return sposDeptOrgId;
	}	

	public String getSposDeptOrgNm() {
		return sposDeptOrgNm;
	}

	public void setSposDeptOrgNm(String sposDeptOrgNm) {
		this.sposDeptOrgNm = sposDeptOrgNm;
	}

	public String getSposDeptOrgFullNm() {
		return sposDeptOrgFullNm;
	}

	public void setSposDeptOrgFullNm(String sposDeptOrgFullNm) {
		this.sposDeptOrgFullNm = sposDeptOrgFullNm;
	}

	public void setNlvlBasSeq(BigInteger nlvlBasSeq) {
		this.nlvlBasSeq = nlvlBasSeq;
	}

	public BigInteger getNlvlBasSeq() {
		return nlvlBasSeq;
	}

	public void setScomment(String scomment) {
		this.scomment = scomment;
	}

	public String getScomment() {
		return scomment;
	}

	public TbLvlBasVo getTbLvlBasVo() {
		return tbLvlBasVo;
	}

	public void setTbLvlBasVo(TbLvlBasVo tbLvlBasVo) {
		this.tbLvlBasVo = tbLvlBasVo;
	}

	/** MEMBER METHOD DECLARATION END **/
}