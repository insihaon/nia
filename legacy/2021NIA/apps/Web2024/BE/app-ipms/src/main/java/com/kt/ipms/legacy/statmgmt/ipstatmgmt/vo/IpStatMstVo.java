package com.kt.ipms.legacy.statmgmt.ipstatmgmt.vo;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

import com.kt.ipms.legacy.cmn.vo.IpVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlMstListVo;

public class IpStatMstVo extends IpVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = -3175684322370674569L;
	
	/** 1. 생성차수 현황 VO **/
	
	private String sipCreateSeqCd;
	
	private String sipCreateTypeCd;

	private String sipCreateTypeNm;
	
	private BigDecimal nclassCntTotal; // Class_총수
	
	private BigDecimal nclassCntAssignTotal; // Class_배정수
	
	private BigDecimal nclassCntReservedTotal; // Class_예비배정수
	
	/** 1. 생성차수 현황 VO END**/
	
	/** 2,3,4. 계위정보 VO **/
	
	private String ssvcLineTypeCd;
	
	private String ssvcLineTypeNm;
	
	private String ssvcGroupCd;
	
	private String ssvcGroupNm;
	
	private String ssvcObjCd;
	
	private String ssvcObjNm;
	
	private TbLvlMstListVo lvlMstSeqListVo;
	
	/** 2,3,4. 계위정보 END **/
	
	/** 2. 조직별 현황 VO START **/
	
	private BigDecimal nasssignNcnt; //배정 IP 수
	
	private BigDecimal nasssignNcnt24; //미할당IP수(24/51)
	
	private BigDecimal nasssignNcnt25; //미할당IP수(25/52~)

	private BigDecimal nasssignNcnt26; //미할당IP수(26/56~)

	private BigDecimal nasssignNcnt27; //미할당IP수(27/60~)

	private BigDecimal nasssignNcnt28; //미할당IP수(28/64)
	
	private BigDecimal nasssignNcnt29; //미할당IP수(29/65 이하)
	
	private BigDecimal nasssignNcntSum; //미할당 IP수(계)
	
	private BigDecimal nassignPercent; //할당율(%)
	
	/** 2. 조직별 현황 VO END **/
	
	/** 3. 조직서비스별 현황 VO START **/
	
	private BigDecimal ncntSum; //배정 IP 수
	
	private BigDecimal ncntAllocSum; //할당 IP 수
	
	private BigDecimal ncntNonAllocSum; //미할당 IP수(계)
	
	private BigDecimal ncntPercent; //할당율(%)
	
	private String sumTypeCd; //집계유형
	
	/** 3. 조직서비스별 현황 VO END **/
	
	/** 4. 단일블록별 현황 VO START **/
	
	private String sassignTypeCd;

	private String sassignTypeNm;
	
	private BigDecimal nallocCnt; // 할당 IP 수
	
	private BigDecimal nuseFreeCnt; // 가용 IP 수
	
	private BigDecimal npercent; // 할당율
	
	private BigInteger nsubnetCnt; //SubNet 수
	
	/** 4. 단일블록별 현황 VO END **/
	 
	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/
	
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

	public BigDecimal getNclassCntTotal() {
		return nclassCntTotal;
	}

	public void setNclassCntTotal(BigDecimal nclassCntTotal) {
		this.nclassCntTotal = nclassCntTotal;
	}

	public BigDecimal getNclassCntAssignTotal() {
		return nclassCntAssignTotal;
	}

	public void setNclassCntAssignTotal(BigDecimal nclassCntAssignTotal) {
		this.nclassCntAssignTotal = nclassCntAssignTotal;
	}

	public BigDecimal getNclassCntReservedTotal() {
		return nclassCntReservedTotal;
	}

	public void setNclassCntReservedTotal(BigDecimal nclassCntReservedTotal) {
		this.nclassCntReservedTotal = nclassCntReservedTotal;
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
	
	public BigDecimal getNasssignNcnt() {
		return nasssignNcnt;
	}

	public void setNasssignNcnt(BigDecimal nasssignNcnt) {
		this.nasssignNcnt = nasssignNcnt;
	}

	public BigDecimal getNasssignNcnt24() {
		return nasssignNcnt24;
	}

	public void setNasssignNcnt24(BigDecimal nasssignNcnt24) {
		this.nasssignNcnt24 = nasssignNcnt24;
	}

	public BigDecimal getNasssignNcnt25() {
		return nasssignNcnt25;
	}

	public void setNasssignNcnt25(BigDecimal nasssignNcnt25) {
		this.nasssignNcnt25 = nasssignNcnt25;
	}

	public BigDecimal getNasssignNcnt26() {
		return nasssignNcnt26;
	}

	public void setNasssignNcnt26(BigDecimal nasssignNcnt26) {
		this.nasssignNcnt26 = nasssignNcnt26;
	}

	public BigDecimal getNasssignNcnt27() {
		return nasssignNcnt27;
	}

	public void setNasssignNcnt27(BigDecimal nasssignNcnt27) {
		this.nasssignNcnt27 = nasssignNcnt27;
	}

	public BigDecimal getNasssignNcnt28() {
		return nasssignNcnt28;
	}

	public void setNasssignNcnt28(BigDecimal nasssignNcnt28) {
		this.nasssignNcnt28 = nasssignNcnt28;
	}

	public BigDecimal getNasssignNcnt29() {
		return nasssignNcnt29;
	}

	public void setNasssignNcnt29(BigDecimal nasssignNcnt29) {
		this.nasssignNcnt29 = nasssignNcnt29;
	}

	public BigDecimal getNasssignNcntSum() {
		return nasssignNcntSum;
	}

	public void setNasssignNcntSum(BigDecimal nasssignNcntSum) {
		this.nasssignNcntSum = nasssignNcntSum;
	}

	public BigDecimal getNassignPercent() {
		return nassignPercent;
	}

	public void setNassignPercent(BigDecimal nassignPercent) {
		this.nassignPercent = nassignPercent;
	}

	public BigDecimal getNcntSum() {
		return ncntSum;
	}

	public void setNcntSum(BigDecimal ncntSum) {
		this.ncntSum = ncntSum;
	}

	public BigDecimal getNcntAllocSum() {
		return ncntAllocSum;
	}

	public void setNcntAllocSum(BigDecimal ncntAllocSum) {
		this.ncntAllocSum = ncntAllocSum;
	}

	public BigDecimal getNcntNonAllocSum() {
		return ncntNonAllocSum;
	}

	public void setNcntNonAllocSum(BigDecimal ncntNonAllocSum) {
		this.ncntNonAllocSum = ncntNonAllocSum;
	}

	public BigDecimal getNcntPercent() {
		return ncntPercent;
	}

	public void setNcntPercent(BigDecimal ncntPercent) {
		this.ncntPercent = ncntPercent;
	}

	public String getSumTypeCd() {
		return sumTypeCd;
	}

	public void setSumTypeCd(String sumTypeCd) {
		this.sumTypeCd = sumTypeCd;
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
	
	/** MEMBER METHOD DECLARATION END **/
}
