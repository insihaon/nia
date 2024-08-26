package com.kt.ipms.legacy.linkmgmt.common.vo;

import java.io.Serializable;
import java.math.BigInteger;

import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class TbLnkIpmsSvcMstvo extends CommonVo implements Serializable{

	
	private static final long serialVersionUID = -6339484646514962319L;

	/** MEMBER VARIABLE DECLARATION START **/
	

	private BigInteger nipmsSvcSeq;

	private String sipmsSvcNm;

	private String sassignTypeCd;

	private String sassignTypeNm;

	private String ssvcHgroupCd;

	private String ssvcHgroupNm;

	private String ssvcMgroupCd;

	private String ssvcMgroupNm;
	
	private String ssvcMainClsCode;
	
	private String ssvcMgroupNm1;
	
	private String ssvcSubClsCode;

	private String ssvcLgroupCd;

	private String ssvcLgroupNm;

	private String sexSvcCd;

	private String sexSvcNm;
	
	private String sexLinkUseTypeCd;
	
	private String sexLinkUseTypeNm;

	private String ssvcUseTypeCd;

	private String ssvcUseTypeNm;

	private String suseTypeCd;

	private String suseTypeNm;

	private String scomment;
	
	/** MEMBER VARIABLE DECLARATION END **/
	private String ssvcLineTypeCd;
	
	private String currentDt;
	
	/** MEMBER METHOD DECLARATION START **/
	public BigInteger getNipmsSvcSeq() {
		return nipmsSvcSeq;
	}

	public void setNipmsSvcSeq(BigInteger nipmsSvcSeq) {
		this.nipmsSvcSeq = nipmsSvcSeq;
	}

	public String getSipmsSvcNm() {
		return sipmsSvcNm;
	}

	public void setSipmsSvcNm(String sipmsSvcNm) {
		this.sipmsSvcNm = sipmsSvcNm;
	}

	public String getSassignTypeCd() {
		return sassignTypeCd;
	}

	public void setSassignTypeCd(String sassignTypeCd) {
		this.sassignTypeCd = sassignTypeCd;
	}

	public String getSassignTypeNm() {
		return sassignTypeNm;
	}

	public void setSassignTypeNm(String sassignTypeNm) {
		this.sassignTypeNm = sassignTypeNm;
	}

	public String getSsvcHgroupCd() {
		return ssvcHgroupCd;
	}

	public void setSsvcHgroupCd(String ssvcHgroupCd) {
		this.ssvcHgroupCd = ssvcHgroupCd;
	}

	public String getSsvcHgroupNm() {
		return ssvcHgroupNm;
	}

	public void setSsvcHgroupNm(String ssvcHgroupNm) {
		this.ssvcHgroupNm = ssvcHgroupNm;
	}

	public String getSsvcMgroupCd() {
		return ssvcMgroupCd;
	}

	public void setSsvcMgroupCd(String ssvcMgroupCd) {
		this.ssvcMgroupCd = ssvcMgroupCd;
	}

	public String getSsvcMgroupNm() {
		return ssvcMgroupNm;
	}

	public void setSsvcMgroupNm(String ssvcMgroupNm) {
		this.ssvcMgroupNm = ssvcMgroupNm;
	}

	public String getSsvcMainClsCode() {
		return ssvcMainClsCode;
	}

	public void setSsvcMainClsCode(String ssvcMainClsCode) {
		this.ssvcMainClsCode = ssvcMainClsCode;
	}

	public String getSsvcMgroupNm1() {
		return ssvcMgroupNm1;
	}

	public void setSsvcMgroupNm1(String ssvcMgroupNm1) {
		this.ssvcMgroupNm1 = ssvcMgroupNm1;
	}

	public String getSsvcSubClsCode() {
		return ssvcSubClsCode;
	}

	public void setSsvcSubClsCode(String ssvcSubClsCode) {
		this.ssvcSubClsCode = ssvcSubClsCode;
	}

	public String getSsvcLgroupCd() {
		return ssvcLgroupCd;
	}

	public void setSsvcLgroupCd(String ssvcLgroupCd) {
		this.ssvcLgroupCd = ssvcLgroupCd;
	}

	public String getSsvcLgroupNm() {
		return ssvcLgroupNm;
	}

	public void setSsvcLgroupNm(String ssvcLgroupNm) {
		this.ssvcLgroupNm = ssvcLgroupNm;
	}

	public String getSexSvcCd() {
		return sexSvcCd;
	}

	public void setSexSvcCd(String sexSvcCd) {
		this.sexSvcCd = sexSvcCd;
	}

	public String getSexSvcNm() {
		return sexSvcNm;
	}

	public void setSexSvcNm(String sexSvcNm) {
		this.sexSvcNm = sexSvcNm;
	}

	public String getSexLinkUseTypeCd() {
		return sexLinkUseTypeCd;
	}

	public void setSexLinkUseTypeCd(String sexLinkUseTypeCd) {
		this.sexLinkUseTypeCd = sexLinkUseTypeCd;
	}

	public String getSexLinkUseTypeNm() {
		return sexLinkUseTypeNm;
	}

	public void setSexLinkUseTypeNm(String sexLinkUseTypeNm) {
		this.sexLinkUseTypeNm = sexLinkUseTypeNm;
	}

	public String getSsvcUseTypeCd() {
		return ssvcUseTypeCd;
	}

	public void setSsvcUseTypeCd(String ssvcUseTypeCd) {
		this.ssvcUseTypeCd = ssvcUseTypeCd;
	}

	public String getSsvcUseTypeNm() {
		return ssvcUseTypeNm;
	}

	public void setSsvcUseTypeNm(String ssvcUseTypeNm) {
		this.ssvcUseTypeNm = ssvcUseTypeNm;
	}

	public String getSuseTypeCd() {
		return suseTypeCd;
	}

	public void setSuseTypeCd(String suseTypeCd) {
		this.suseTypeCd = suseTypeCd;
	}

	public String getSuseTypeNm() {
		return suseTypeNm;
	}

	public void setSuseTypeNm(String suseTypeNm) {
		this.suseTypeNm = suseTypeNm;
	}

	public String getScomment() {
		return scomment;
	}

	public void setScomment(String scomment) {
		this.scomment = scomment;
	}

	public String getCurrentDt() {
		return currentDt;
	}

	public void setCurrentDt(String currentDt) {
		this.currentDt = currentDt;
	}

	public String getSsvcLineTypeCd() {
		return ssvcLineTypeCd;
	}

	public void setSsvcLineTypeCd(String ssvcLineTypeCd) {
		this.ssvcLineTypeCd = ssvcLineTypeCd;
	}

}
