package com.kt.ipms.legacy.ipmgmt.tracemgmt.vo;

import java.io.Serializable;
import java.math.BigInteger;

import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class TbTraceIpAssignMstVo extends CommonVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5947888632880827978L;
	
	private BigInteger nipHostMstSeq;	
	private BigInteger nipAssignMstSeq;
	private String sfirstAddr;
	private String slastAddr;
	private int nbitmask;
	private String sipCreateTypeCd;
	private String sipCreateTypeNm;
	private String ssvcLineTypeCd;
	private String ssvcLineTypeNm;
	private String sassignLevelCd;
	private String sassignLevelNm;
	private String validationCheckNm;
	private String ssvcGroupCd;
	private String ssvcGroupNm;
	private String ssvcObjCd;
	private String ssvcObjNm;
	private String srssofficescode;
	private String srssofficesNm;
	private String ssubscnealias;
	private String smodelname;
	private String ssubsclgipportdescription;
	private String sconnalias;
	private String sssvcMgroupNm;
	private String sipmsSvcNm;
	private String sassignTypeNm;
	private String ssvcUseTypeNm;
	private String sllnum;
	private String ssaid;
	private String scustname;
	private String sipVersionTypeCd;
	
	public BigInteger getNipHostMstSeq() {
		return nipHostMstSeq;
	}
	public void setNipHostMstSeq(BigInteger nipHostMstSeq) {
		this.nipHostMstSeq = nipHostMstSeq;
	}
	public BigInteger getNipAssignMstSeq() {
		return nipAssignMstSeq;
	}
	public void setNipAssignMstSeq(BigInteger nipAssignMstSeq) {
		this.nipAssignMstSeq = nipAssignMstSeq;
	}
	public String getSfirstAddr() {
		return sfirstAddr;
	}
	public void setSfirstAddr(String sfirstAddr) {
		this.sfirstAddr = sfirstAddr;
	}
	public String getSlastAddr() {
		return slastAddr;
	}
	public void setSlastAddr(String slastAddr) {
		this.slastAddr = slastAddr;
	}
	public int getNbitmask() {
		return nbitmask;
	}
	public void setNbitmask(int nbitmask) {
		this.nbitmask = nbitmask;
	}
	public String getSipCreateTypeCd() {
		return sipCreateTypeCd;
	}
	public void setSipCreateTypeCd(String sipCreateTypeCd) {
		this.sipCreateTypeCd = sipCreateTypeCd;
	}
	public String getSipCreateTypeNm() {
		return sipCreateTypeNm;
	}
	public void setSipCreateTypeNm(String sipCreateTypeNm) {
		this.sipCreateTypeNm = sipCreateTypeNm;
	}
	public String getSsvcLineTypeCd() {
		return ssvcLineTypeCd;
	}
	public void setSsvcLineTypeCd(String ssvcLineTypeCd) {
		this.ssvcLineTypeCd = ssvcLineTypeCd;
	}
	public String getSsvcLineTypeNm() {
		return ssvcLineTypeNm;
	}
	public void setSsvcLineTypeNm(String ssvcLineTypeNm) {
		this.ssvcLineTypeNm = ssvcLineTypeNm;
	}
	public String getSassignLevelCd() {
		return sassignLevelCd;
	}
	public void setSassignLevelCd(String sassignLevelCd) {
		this.sassignLevelCd = sassignLevelCd;
	}
	public String getSassignLevelNm() {
		return sassignLevelNm;
	}
	public void setSassignLevelNm(String sassignLevelNm) {
		this.sassignLevelNm = sassignLevelNm;
	}
	public String getValidationCheckNm() {
		return validationCheckNm;
	}
	public void setValidationCheckNm(String validationCheckNm) {
		this.validationCheckNm = validationCheckNm;
	}
	public String getSsvcGroupCd() {
		return ssvcGroupCd;
	}
	public void setSsvcGroupCd(String ssvcGroupCd) {
		this.ssvcGroupCd = ssvcGroupCd;
	}
	public String getSsvcGroupNm() {
		return ssvcGroupNm;
	}
	public void setSsvcGroupNm(String ssvcGroupNm) {
		this.ssvcGroupNm = ssvcGroupNm;
	}
	public String getSsvcObjCd() {
		return ssvcObjCd;
	}
	public void setSsvcObjCd(String ssvcObjCd) {
		this.ssvcObjCd = ssvcObjCd;
	}
	public String getSsvcObjNm() {
		return ssvcObjNm;
	}
	public void setSsvcObjNm(String ssvcObjNm) {
		this.ssvcObjNm = ssvcObjNm;
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
	public String getSsubscnealias() {
		return ssubscnealias;
	}
	public void setSsubscnealias(String ssubscnealias) {
		this.ssubscnealias = ssubscnealias;
	}
	public String getSmodelname() {
		return smodelname;
	}
	public void setSmodelname(String smodelname) {
		this.smodelname = smodelname;
	}
	public String getSsubsclgipportdescription() {
		return ssubsclgipportdescription;
	}
	public void setSsubsclgipportdescription(String ssubsclgipportdescription) {
		this.ssubsclgipportdescription = ssubsclgipportdescription;
	}
	public String getSconnalias() {
		return sconnalias;
	}
	public void setSconnalias(String sconnalias) {
		this.sconnalias = sconnalias;
	}
	public String getSssvcMgroupNm() {
		return sssvcMgroupNm;
	}
	public void setSssvcMgroupNm(String sssvcMgroupNm) {
		this.sssvcMgroupNm = sssvcMgroupNm;
	}
	public String getSipmsSvcNm() {
		return sipmsSvcNm;
	}
	public void setSipmsSvcNm(String sipmsSvcNm) {
		this.sipmsSvcNm = sipmsSvcNm;
	}
	public String getSassignTypeNm() {
		return sassignTypeNm;
	}
	public void setSassignTypeNm(String sassignTypeNm) {
		this.sassignTypeNm = sassignTypeNm;
	}
	public String getSsvcUseTypeNm() {
		return ssvcUseTypeNm;
	}
	public void setSsvcUseTypeNm(String ssvcUseTypeNm) {
		this.ssvcUseTypeNm = ssvcUseTypeNm;
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
	public String getScustname() {
		return scustname;
	}
	public void setScustname(String scustname) {
		this.scustname = scustname;
	}
	public String getSipVersionTypeCd() {
		return sipVersionTypeCd;
	}
	public void setSipVersionTypeCd(String sipVersionTypeCd) {
		this.sipVersionTypeCd = sipVersionTypeCd;
	}
	

}
