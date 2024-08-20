package com.kt.ipms.legacy.ipmgmt.tracemgmt.vo;

import java.io.Serializable;
import java.math.BigInteger;

import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class TbTraceIpHostMstVo extends CommonVo implements Serializable{
	
	
	private static final long serialVersionUID = -439366969362341830L;

	private BigInteger nipHostMstSeq;
	
	private String sipBlock;
		
	private String sipHostTypeCd;

	private String sipHostTypeNm;
	
	private String srssofficescode;
	
	private String srssofficesNm;
	
	private String sipHostNm;
	
	private String sipIfNm;
	
	private String sssvcMgroupNm;

	private String sexSvcNm;
	
	private String sassignTypeNm;
	
	private String sllnum;
	
	private String ssaid;
	
	private String ssvcUseTypeNm;
	
	private BigInteger nipAssignMstSeq;
	
	private String sipVersionTypeCd;

	public BigInteger getNipHostMstSeq() {
		return nipHostMstSeq;
	}

	public void setNipHostMstSeq(BigInteger nipHostMstSeq) {
		this.nipHostMstSeq = nipHostMstSeq;
	}

	public String getSipBlock() {
		return sipBlock;
	}

	public void setSipBlock(String sipBlock) {
		this.sipBlock = sipBlock;
	}

	public String getSipHostTypeCd() {
		return sipHostTypeCd;
	}

	public void setSipHostTypeCd(String sipHostTypeCd) {
		this.sipHostTypeCd = sipHostTypeCd;
	}

	public String getSipHostTypeNm() {
		return sipHostTypeNm;
	}

	public void setSipHostTypeNm(String sipHostTypeNm) {
		this.sipHostTypeNm = sipHostTypeNm;
	}

	public String getSrssofficescode() {
		return srssofficescode;
	}

	public void setSrssofficescode(String srssofficescode) {
		this.srssofficescode = srssofficescode;
	}

	public String getSrssofficesNm() {
		return srssofficesNm;
	}

	public void setSrssofficesNm(String srssofficesNm) {
		this.srssofficesNm = srssofficesNm;
	}

	public String getSipHostNm() {
		return sipHostNm;
	}

	public void setSipHostNm(String sipHostNm) {
		this.sipHostNm = sipHostNm;
	}

	public String getSipIfNm() {
		return sipIfNm;
	}

	public void setSipIfNm(String sipIfNm) {
		this.sipIfNm = sipIfNm;
	}

	public String getSssvcMgroupNm() {
		return sssvcMgroupNm;
	}

	public void setSssvcMgroupNm(String sssvcMgroupNm) {
		this.sssvcMgroupNm = sssvcMgroupNm;
	}

	public String getSexSvcNm() {
		return sexSvcNm;
	}

	public void setSexSvcNm(String sexSvcNm) {
		this.sexSvcNm = sexSvcNm;
	}

	public String getSassignTypeNm() {
		return sassignTypeNm;
	}

	public void setSassignTypeNm(String sassignTypeNm) {
		this.sassignTypeNm = sassignTypeNm;
	}

	public String getSllnum() {
		return sllnum;
	}

	public void setSllnum(String sllnum) {
		this.sllnum = sllnum;
	}

	public String getSsaid() {
		return ssaid;
	}

	public void setSsaid(String ssaid) {
		this.ssaid = ssaid;
	}

	public String getSsvcUseTypeNm() {
		return ssvcUseTypeNm;
	}

	public void setSsvcUseTypeNm(String ssvcUseTypeNm) {
		this.ssvcUseTypeNm = ssvcUseTypeNm;
	}

	public BigInteger getNipAssignMstSeq() {
		return nipAssignMstSeq;
	}

	public void setNipAssignMstSeq(BigInteger nipAssignMstSeq) {
		this.nipAssignMstSeq = nipAssignMstSeq;
	}

	public String getSipVersionTypeCd() {
		return sipVersionTypeCd;
	}

	public void setSipVersionTypeCd(String sipVersionTypeCd) {
		this.sipVersionTypeCd = sipVersionTypeCd;
	}	

}
