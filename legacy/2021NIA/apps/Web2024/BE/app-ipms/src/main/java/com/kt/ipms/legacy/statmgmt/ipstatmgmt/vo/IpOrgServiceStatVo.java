package com.kt.ipms.legacy.statmgmt.ipstatmgmt.vo;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.kt.ipms.legacy.cmn.vo.IpVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlMstListVo;

/**
 * 조직서비스별 통계 VO
 * @author KimMinSeok
 *
 */
public class IpOrgServiceStatVo extends IpVo {

	private static final long serialVersionUID = 8270677967713264245L;
	
	private String sipCreateTypeCd;

	private String sipCreateTypeNm;
	
	private String ssvcLineTypeCd;
	
	private String ssvcLineTypeNm;
	
	private String ssvcGroupCd;
	
	private String ssvcGroupNm;
	
	private String ssvcObjCd;
	
	private String ssvcObjNm;
	
	private String sassignTypeCd;

	private String sassignTypeNm;
	
	private String sumTypeCd; //집계유형
	
	private BigInteger ncntTotal; // 전체 IP 수
	
	private BigInteger ncntNotAssign; // 미배정 IP 수
	
	private BigInteger ncntRersAssign; // 예비배정 IP 수
	
	private BigInteger ncntAssign; // 배정 IP 수
	
	private BigInteger ncntServAssign; // 서비스배정 IP 수 
	
	private BigInteger ncntRersAlloc; // 할당예약 IP 수
	
	private BigInteger ncntAlloc; // 할당 IP 수
	
	private BigDecimal ncntPercent; // 할당율
	
	private TbLvlMstListVo lvlMstSeqListVo; // 계위 Seq 파라미터

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

	public String getSumTypeCd() {
		return sumTypeCd;
	}

	public void setSumTypeCd(String sumTypeCd) {
		this.sumTypeCd = sumTypeCd;
	}

	public BigInteger getNcntTotal() {
		return ncntTotal;
	}

	public void setNcntTotal(BigInteger ncntTotal) {
		this.ncntTotal = ncntTotal;
	}

	public BigInteger getNcntNotAssign() {
		return ncntNotAssign;
	}

	public void setNcntNotAssign(BigInteger ncntNotAssign) {
		this.ncntNotAssign = ncntNotAssign;
	}

	public BigInteger getNcntRersAssign() {
		return ncntRersAssign;
	}

	public void setNcntRersAssign(BigInteger ncntRersAssign) {
		this.ncntRersAssign = ncntRersAssign;
	}

	public BigInteger getNcntAssign() {
		return ncntAssign;
	}

	public void setNcntAssign(BigInteger ncntAssign) {
		this.ncntAssign = ncntAssign;
	}

	public BigInteger getNcntServAssign() {
		return ncntServAssign;
	}

	public void setNcntServAssign(BigInteger ncntServAssign) {
		this.ncntServAssign = ncntServAssign;
	}

	public BigInteger getNcntRersAlloc() {
		return ncntRersAlloc;
	}

	public void setNcntRersAlloc(BigInteger ncntRersAlloc) {
		this.ncntRersAlloc = ncntRersAlloc;
	}

	public BigInteger getNcntAlloc() {
		return ncntAlloc;
	}

	public void setNcntAlloc(BigInteger ncntAlloc) {
		this.ncntAlloc = ncntAlloc;
	}

	public BigDecimal getNcntPercent() {
		return ncntPercent;
	}

	public void setNcntPercent(BigDecimal ncntPercent) {
		this.ncntPercent = ncntPercent;
	}

	public TbLvlMstListVo getLvlMstSeqListVo() {
		return lvlMstSeqListVo;
	}

	public void setLvlMstSeqListVo(TbLvlMstListVo lvlMstSeqListVo) {
		this.lvlMstSeqListVo = lvlMstSeqListVo;
	}

}
