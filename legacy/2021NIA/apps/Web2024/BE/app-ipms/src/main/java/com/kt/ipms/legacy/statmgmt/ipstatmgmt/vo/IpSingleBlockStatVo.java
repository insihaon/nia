package com.kt.ipms.legacy.statmgmt.ipstatmgmt.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

import com.kt.ipms.legacy.cmn.vo.IpVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlMstListVo;

public class IpSingleBlockStatVo extends IpVo implements Serializable{

	/**
	 * 단일블록별 현황 VO
	 */
	private static final long serialVersionUID = 3907425257046676588L;
	
	
	private String sipCreateTypeCd;

	private String sipCreateTypeNm;
	
	private String sipVersionTypeCd;
	
	private String sipVersionTypeNm;
	
	private String pipPrefix;
	
   /** 계위정보 VO **/
	
	private String ssvcLineTypeCd;
	
	private String ssvcLineTypeNm;
	
	private String ssvcGroupCd;
	
	private String ssvcGroupNm;
	
	private String ssvcObjCd;
	
	private String ssvcObjNm;
	
	private TbLvlMstListVo lvlMstSeqListVo;
	
	/** 단일블록별 현황 VO  **/
	
	private String sassignTypeCd;

	private String sassignTypeNm;
	
	private BigDecimal nallocCnt; // 할당 IP 수
	
	private BigDecimal nuseFreeCnt; // 가용 IP 수
	
	private BigDecimal npercent; // 할당율
	
	private BigInteger nsubnetCnt; //SubNet 수

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

	public String getSipVersionTypeCd() {
		return sipVersionTypeCd;
	}

	public void setSipVersionTypeCd(String sipVersionTypeCd) {
		this.sipVersionTypeCd = sipVersionTypeCd;
	}

	public String getSipVersionTypeNm() {
		return sipVersionTypeNm;
	}

	public void setSipVersionTypeNm(String sipVersionTypeNm) {
		this.sipVersionTypeNm = sipVersionTypeNm;
	}

	public String getPipPrefix() {
		return pipPrefix;
	}

	public void setPipPrefix(String pipPrefix) {
		this.pipPrefix = pipPrefix;
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

	public TbLvlMstListVo getLvlMstSeqListVo() {
		return lvlMstSeqListVo;
	}

	public void setLvlMstSeqListVo(TbLvlMstListVo lvlMstSeqListVo) {
		this.lvlMstSeqListVo = lvlMstSeqListVo;
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

	public BigDecimal getNallocCnt() {
		return nallocCnt;
	}

	public void setNallocCnt(BigDecimal nallocCnt) {
		this.nallocCnt = nallocCnt;
	}

	public BigDecimal getNuseFreeCnt() {
		return nuseFreeCnt;
	}

	public void setNuseFreeCnt(BigDecimal nuseFreeCnt) {
		this.nuseFreeCnt = nuseFreeCnt;
	}

	public BigDecimal getNpercent() {
		return npercent;
	}

	public void setNpercent(BigDecimal npercent) {
		this.npercent = npercent;
	}

	public BigInteger getNsubnetCnt() {
		return nsubnetCnt;
	}

	public void setNsubnetCnt(BigInteger nsubnetCnt) {
		this.nsubnetCnt = nsubnetCnt;
	}



}
