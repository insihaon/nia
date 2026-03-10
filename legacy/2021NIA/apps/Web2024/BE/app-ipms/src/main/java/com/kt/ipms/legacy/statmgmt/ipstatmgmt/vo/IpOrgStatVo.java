package com.kt.ipms.legacy.statmgmt.ipstatmgmt.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

import com.kt.ipms.legacy.cmn.vo.IpVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlMstListVo;

public class IpOrgStatVo extends IpVo implements Serializable{

	/**
	 * 조직별 IP 현황  VO
	 */
	private static final long serialVersionUID = 7466315991882964452L;
	
	
	private String sipCreateSeqCd;
	
	private String sipCreateTypeCd;

	private String sipCreateTypeNm;
	
	/** 계위정보 VO START**/

	private String ssvcLineTypeCd;
	
	private String ssvcLineTypeNm;
	
	private String ssvcGroupCd;
	
	private String ssvcGroupNm;
	
	private String ssvcObjCd;
	
	private String ssvcObjNm;
	
	private TbLvlMstListVo lvlMstSeqListVo;
	
	/** 계위정보 END **/
	
	/** 조직별 현황 VO START **/

	private BigInteger ncntTotal; //총 IP 수
	
	private BigInteger nallocCnt; // 할당 IP 수
	
	private BigInteger nasssignNcnt24; //미할당IP수(24/51)
	
	private BigInteger nasssignNcnt25; //미할당IP수(25/52~)

	private BigInteger nasssignNcnt26; //미할당IP수(26/56~)

	private BigInteger nasssignNcnt27; //미할당IP수(27/60~)

	private BigInteger nasssignNcnt28; //미할당IP수(28/64)
	
	private BigInteger nasssignNcnt29; //미할당IP수(29/65 이하)
	
	private BigInteger nasssignNcntSum; //미할당 IP수(계)
	
	private BigDecimal nassignPercent; //할당율(%)
	
	/** 조직별 현황 VO END **/

	public String getSipCreateSeqCd() {
		return sipCreateSeqCd;
	}

	public void setSipCreateSeqCd(String sipCreateSeqCd) {
		this.sipCreateSeqCd = sipCreateSeqCd;
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

	public BigInteger getNcntTotal() {
		return ncntTotal;
	}

	public void setNcntTotal(BigInteger ncntTotal) {
		this.ncntTotal = ncntTotal;
	}

	public BigInteger getNallocCnt() {
		return nallocCnt;
	}

	public void setNallocCnt(BigInteger nallocCnt) {
		this.nallocCnt = nallocCnt;
	}

	public BigInteger getNasssignNcnt24() {
		return nasssignNcnt24;
	}

	public void setNasssignNcnt24(BigInteger nasssignNcnt24) {
		this.nasssignNcnt24 = nasssignNcnt24;
	}

	public BigInteger getNasssignNcnt25() {
		return nasssignNcnt25;
	}

	public void setNasssignNcnt25(BigInteger nasssignNcnt25) {
		this.nasssignNcnt25 = nasssignNcnt25;
	}

	public BigInteger getNasssignNcnt26() {
		return nasssignNcnt26;
	}

	public void setNasssignNcnt26(BigInteger nasssignNcnt26) {
		this.nasssignNcnt26 = nasssignNcnt26;
	}

	public BigInteger getNasssignNcnt27() {
		return nasssignNcnt27;
	}

	public void setNasssignNcnt27(BigInteger nasssignNcnt27) {
		this.nasssignNcnt27 = nasssignNcnt27;
	}

	public BigInteger getNasssignNcnt28() {
		return nasssignNcnt28;
	}

	public void setNasssignNcnt28(BigInteger nasssignNcnt28) {
		this.nasssignNcnt28 = nasssignNcnt28;
	}

	public BigInteger getNasssignNcnt29() {
		return nasssignNcnt29;
	}

	public void setNasssignNcnt29(BigInteger nasssignNcnt29) {
		this.nasssignNcnt29 = nasssignNcnt29;
	}

	public BigInteger getNasssignNcntSum() {
		return nasssignNcntSum;
	}

	public void setNasssignNcntSum(BigInteger nasssignNcntSum) {
		this.nasssignNcntSum = nasssignNcntSum;
	}

	public BigDecimal getNassignPercent() {
		return nassignPercent;
	}

	public void setNassignPercent(BigDecimal nassignPercent) {
		this.nassignPercent = nassignPercent;
	}

	
	
	
}
