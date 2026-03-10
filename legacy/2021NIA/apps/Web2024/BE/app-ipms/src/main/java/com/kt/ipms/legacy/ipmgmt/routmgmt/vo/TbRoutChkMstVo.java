package com.kt.ipms.legacy.ipmgmt.routmgmt.vo;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

import com.kt.ipms.legacy.cmn.vo.CommonVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlMstListVo;

public class TbRoutChkMstVo extends CommonVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2885934973227740283L;
	
	/**
	 * 라우팅_비교_결과_MST_SEQ
	 */
	private BigInteger nroutingChkMstSeq;
	
	/**
	 * 서비스망 코드
	 */
	private String ssvcLineTypeCd;
	
	/**
	 * 서비스망 명
	 */
	private String ssvcLineTypeNm;
	
	/**
	 * 본부 코드
	 */
	private String ssvcGroupCd;
	
	/**
	 * 본부 명
	 */
	private String ssvcGroupNm;
	
	/**
	 * 노드 코드
	 */
	private String ssvcObjCd;
	
	/**
	 * 노드 명 
	 */
	private String ssvcObjNm;
	
	/**
	 * 계위_MST_SEQ
	 */
	private BigInteger nlvlMstSeq;

	/**
	 * IP_배정_MST_SEQ
	 */
	private BigInteger nipAssignMstSeq;
	
	/**
	 * IP_할당_MST_SEQ
	 */
	private BigInteger nipAllocMstSeq;
	
	/**
	 * IPMS_IP
	 */
	private String pipmsIpPrefix;
	
	/**
	 * IPMS_IP_블록
	 */
	private String sipmsIpBlock;
	
	/**
	 * IPMS_IP_비트마스크
	 */
	private Integer nipmsIpBitmask;
	
	/**
	 * 라우팅_IP
	 */
	private String proutingIpPrefix;
	
	/**
	 * 라우팅_IP_블록
	 */
	private String sroutingIpBlock;
	
	/**
	 * 라우팅_IP_비트마스크
	 */
	private Integer nroutingIpBitmask;
	
	/**
	 * 라우팅_사용_여부
	 */
	private String sroutingUseYn;
	
	/**
	 * NextHop
	 */
	private String sipNexthop;
	
	/**
	 * Community
	 */
	private String scommunity;
	
	/**
	 * 사용장비
	 */
	private String suseRouting;
	
	/**
	 * DB현행화_결과
	 */
	private String sdbIntgrmRsltCd;
	
	/**
	 * DB현행화_결과
	 */
	private String sdbIntgrmRsltNm;
	
	/**
	 * IP_생성_유형
	 */
	private String sipCreateTypeNm;
	
	/**
	 * IPMS_서비스_유형_타입_CD
	 */
	private String sassignTypeNm;
	
	/**
	 * IPMS_서비스_유형_타입
	 */
	private String sipCreateTypeCd;
	
	/**
	 * Summary_일치_여부
	 */
	private String ssummaryMatchYn;
	
	/**
	 * 할당유형코드
	 */
	private String sassignLevelCd;
	
	/**
	 * 할당유형상태
	 */
	private String sassignLevelNm;
	
	/**
	 * 계위 List
	 */
	private TbLvlMstListVo lvlMstSeqListVo;
	
	/**
	 * bitmask
	 */
	private Integer nbitmask;
	
	/**
	 * 조회조건 > 구분
	 */
	private String skindCd;
	
	/**
	 * 체크리스트
	 */
	private List<BigInteger> chkList;
	
	/**
	 *  비교 구분 (ASSIGN/ALLOC)
	 */
	private String scompare_cd;
	
	/**
	 * 수집일자
	 */
	private String scollect_dt;
	
	/**
	 * IP 멀티조회
	 */
	private String searchWrdMulti = "";
	
	/**
	 * 분할, 병합 구분 
	 * PIPMS - 분할
	 * PROUTING - 병합
	 */
	private String sipPrefixGubun;
	
	/**
	 * 체크건수
	 */
	private List<String> chkListStr;
	
	private String pipPrefix;
	
	/**
	 * IP 버전
	 */
	private String sipVersionTypeCd;
	
	/**
	 * 분할/병합 건수
	 */
	private Integer ntargetCnt;
	
	/**
	 * Whois Seq
	 */
	private BigInteger nwhoisseq;
	
	/**
	 * 수집일자 Origin Data
	 */
	private String scollectDtOrigin;
	
	/**
	 * IP블록_MST_SEQ
	 */
	private BigInteger nipBlockMstSeq;
	
	/**
	 * IP할당유형_CD
	 */
	private String sassignTypeCd;
	
	/**
	 * 회선 CNT
	 */
	private Integer nipAllocMstCnt;
	
	/**
	 * 할당 상태 목록 문자열
	 */
	private String sassignLevelCdMultiStr;
	
	/**
	 * 할당 상태 List<String>
	 */
	private List<String> sassignLevelMulti;
	
	/**
	 * 할당 유형 목록 문자열
	 */
	private String sassignTypeCdMultiStr;
	
	/**
	 * 할당 유형 List<String>
	 */
	private List<String> sassignTypeMulti;
	
	/**
	 * 구분
	 */
	private String sipGubun;
	
	private Integer ngubunCnt;
	
	private String ssign;
	
	/**
	 * IP할당/해지 구분
	 */
	private String sallocGubun;
	
	private List<BigInteger> nList;

	/**
	 * 예외 여부
	 */
	private String sexcpt_yn;
	
	/**
	 * 예외 여부
	 */
	private String sexcptYnOrigin;
	
	/**
	 * 예외 구분 코드
	 */
	private String sexcpt_cd;
	
	private String sexcpt_nm;
	
	/**
	 * 예외 사유
	 */
	private String sexcpt_rsn;
	
	/**
	 * 변경 IP_배정_MST_SEQ
	 */
	private BigInteger nipChgAssignMstSeq;
	
	/**
	 * 변경 IP_할당_MST_SEQ
	 */
	private BigInteger nipChgAllocMstSeq;
	
	/**
	 * 변경 여부
	 */
	private String sassignChangeYn;
	
	private List<String> strIpBlock;
	
	private BigInteger nlvlMstSeq2;
	
	private String sneossDdYn;
	
	private String sllnum;
	
	private String ssubcnealias;
	
	private String sexcptNm;
	
	private String sexcptRsn;
	
	private String spageType;
	
	public BigInteger getNroutingChkMstSeq() {
		return nroutingChkMstSeq;
	}

	public void setNroutingChkMstSeq(BigInteger nroutingChkMstSeq) {
		this.nroutingChkMstSeq = nroutingChkMstSeq;
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

	public BigInteger getNlvlMstSeq() {
		return nlvlMstSeq;
	}

	public void setNlvlMstSeq(BigInteger nlvlMstSeq) {
		this.nlvlMstSeq = nlvlMstSeq;
	}

	public BigInteger getNipAssignMstSeq() {
		return nipAssignMstSeq;
	}

	public void setNipAssignMstSeq(BigInteger nipAssignMstSeq) {
		this.nipAssignMstSeq = nipAssignMstSeq;
	}

	public BigInteger getNipAllocMstSeq() {
		return nipAllocMstSeq;
	}

	public void setNipAllocMstSeq(BigInteger nipAllocMstSeq) {
		this.nipAllocMstSeq = nipAllocMstSeq;
	}

	public String getPipmsIpPrefix() {
		return pipmsIpPrefix;
	}

	public void setPipmsIpPrefix(String pipmsIpPrefix) {
		this.pipmsIpPrefix = pipmsIpPrefix;
	}

	public String getSipmsIpBlock() {
		return sipmsIpBlock;
	}

	public void setSipmsIpBlock(String sipmsIpBlock) {
		this.sipmsIpBlock = sipmsIpBlock;
	}

	public Integer getNipmsIpBitmask() {
		return nipmsIpBitmask;
	}

	public void setNipmsIpBitmask(Integer nipmsIpBitmask) {
		this.nipmsIpBitmask = nipmsIpBitmask;
	}

	public String getProutingIpPrefix() {
		return proutingIpPrefix;
	}

	public void setProutingIpPrefix(String proutingIpPrefix) {
		this.proutingIpPrefix = proutingIpPrefix;
	}

	public String getSroutingIpBlock() {
		return sroutingIpBlock;
	}

	public void setSroutingIpBlock(String sroutingIpBlock) {
		this.sroutingIpBlock = sroutingIpBlock;
	}

	public Integer getNroutingIpBitmask() {
		return nroutingIpBitmask;
	}

	public void setNroutingIpBitmask(Integer nroutingIpBitmask) {
		this.nroutingIpBitmask = nroutingIpBitmask;
	}

	public String getSroutingUseYn() {
		return sroutingUseYn;
	}

	public void setSroutingUseYn(String sroutingUseYn) {
		this.sroutingUseYn = sroutingUseYn;
	}

	public String getSipNexthop() {
		return sipNexthop;
	}

	public void setSipNexthop(String sipNexthop) {
		this.sipNexthop = sipNexthop;
	}

	public String getScommunity() {
		return scommunity;
	}

	public void setScommunity(String scommunity) {
		this.scommunity = scommunity;
	}

	public String getSuseRouting() {
		return suseRouting;
	}

	public void setSuseRouting(String suseRouting) {
		this.suseRouting = suseRouting;
	}

	public String getSsummaryMatchYn() {
		return ssummaryMatchYn;
	}

	public void setSsummaryMatchYn(String ssummaryMatchYn) {
		this.ssummaryMatchYn = ssummaryMatchYn;
	}

	public String getSipCreateTypeNm() {
		return sipCreateTypeNm;
	}

	public void setSipCreateTypeNm(String sipCreateTypeNm) {
		this.sipCreateTypeNm = sipCreateTypeNm;
	}

	public String getSassignTypeNm() {
		return sassignTypeNm;
	}

	public void setSassignTypeNm(String sassignTypeNm) {
		this.sassignTypeNm = sassignTypeNm;
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

	public TbLvlMstListVo getLvlMstSeqListVo() {
		return lvlMstSeqListVo;
	}

	public void setLvlMstSeqListVo(TbLvlMstListVo lvlMstSeqListVo) {
		this.lvlMstSeqListVo = lvlMstSeqListVo;
	}

	public Integer getNbitmask() {
		return nbitmask;
	}

	public void setNbitmask(Integer nbitmask) {
		this.nbitmask = nbitmask;
	}

	public String getSkindCd() {
		return skindCd;
	}

	public void setSkindCd(String skindCd) {
		this.skindCd = skindCd;
	}

	public List<BigInteger> getChkList() {
		return chkList;
	}

	public void setChkList(List<BigInteger> chkList) {
		this.chkList = chkList;
	}

	public String getScompare_cd() {
		return scompare_cd;
	}

	public void setScompare_cd(String scompare_cd) {
		this.scompare_cd = scompare_cd;
	}

	public String getSipCreateTypeCd() {
		return sipCreateTypeCd;
	}

	public void setSipCreateTypeCd(String sipCreateTypeCd) {
		this.sipCreateTypeCd = sipCreateTypeCd;
	}

	public String getSearchWrdMulti() {
		return searchWrdMulti;
	}

	public void setSearchWrdMulti(String searchWrdMulti) {
		this.searchWrdMulti = searchWrdMulti;
	}

	public String getSipPrefixGubun() {
		return sipPrefixGubun;
	}

	public void setSipPrefixGubun(String sipPrefixGubun) {
		this.sipPrefixGubun = sipPrefixGubun;
	}

	public List<String> getChkListStr() {
		return chkListStr;
	}

	public void setChkListStr(List<String> chkListStr) {
		this.chkListStr = chkListStr;
	}

	public String getPipPrefix() {
		return pipPrefix;
	}

	public void setPipPrefix(String pipPrefix) {
		this.pipPrefix = pipPrefix;
	}

	public String getSipVersionTypeCd() {
		return sipVersionTypeCd;
	}

	public void setSipVersionTypeCd(String sipVersionTypeCd) {
		this.sipVersionTypeCd = sipVersionTypeCd;
	}

	public Integer getNtargetCnt() {
		return ntargetCnt;
	}

	public void setNtargetCnt(Integer ntargetCnt) {
		this.ntargetCnt = ntargetCnt;
	}

	public BigInteger getNwhoisseq() {
		return nwhoisseq;
	}

	public void setNwhoisseq(BigInteger nwhoisseq) {
		this.nwhoisseq = nwhoisseq;
	}

	public String getScollect_dt() {
		return scollect_dt;
	}

	public void setScollect_dt(String scollect_dt) {
		this.scollect_dt = scollect_dt;
	}

	public String getSdbIntgrmRsltCd() {
		return sdbIntgrmRsltCd;
	}

	public void setSdbIntgrmRsltCd(String sdbIntgrmRsltCd) {
		this.sdbIntgrmRsltCd = sdbIntgrmRsltCd;
	}

	public String getScollectDtOrigin() {
		return scollectDtOrigin;
	}

	public void setScollectDtOrigin(String scollectDtOrigin) {
		this.scollectDtOrigin = scollectDtOrigin;
	}

	public BigInteger getNipBlockMstSeq() {
		return nipBlockMstSeq;
	}

	public void setNipBlockMstSeq(BigInteger nipBlockMstSeq) {
		this.nipBlockMstSeq = nipBlockMstSeq;
	}

	public String getSassignTypeCd() {
		return sassignTypeCd;
	}

	public void setSassignTypeCd(String sassignTypeCd) {
		this.sassignTypeCd = sassignTypeCd;
	}

	public String getSdbIntgrmRsltNm() {
		return sdbIntgrmRsltNm;
	}

	public void setSdbIntgrmRsltNm(String sdbIntgrmRsltNm) {
		this.sdbIntgrmRsltNm = sdbIntgrmRsltNm;
	}

	public Integer getNipAllocMstCnt() {
		return nipAllocMstCnt;
	}

	public void setNipAllocMstCnt(Integer nipAllocMstCnt) {
		this.nipAllocMstCnt = nipAllocMstCnt;
	}

	public String getSassignLevelCdMultiStr() {
		return sassignLevelCdMultiStr;
	}

	public void setSassignLevelCdMultiStr(String sassignLevelCdMultiStr) {
		this.sassignLevelCdMultiStr = sassignLevelCdMultiStr;
	}

	public List<String> getSassignLevelMulti() {
		return sassignLevelMulti;
	}

	public void setSassignLevelMulti(List<String> sassignLevelMulti) {
		this.sassignLevelMulti = sassignLevelMulti;
	}

	public String getSassignTypeCdMultiStr() {
		return sassignTypeCdMultiStr;
	}

	public void setSassignTypeCdMultiStr(String sassignTypeCdMultiStr) {
		this.sassignTypeCdMultiStr = sassignTypeCdMultiStr;
	}

	public List<String> getSassignTypeMulti() {
		return sassignTypeMulti;
	}

	public void setSassignTypeMulti(List<String> sassignTypeMulti) {
		this.sassignTypeMulti = sassignTypeMulti;
	}

	public String getSipGubun() {
		return sipGubun;
	}

	public void setSipGubun(String sipGubun) {
		this.sipGubun = sipGubun;
	}

	public Integer getNgubunCnt() {
		return ngubunCnt;
	}

	public void setNgubunCnt(Integer ngubunCnt) {
		this.ngubunCnt = ngubunCnt;
	}

	public String getSsign() {
		return ssign;
	}

	public void setSsign(String ssign) {
		this.ssign = ssign;
	}

	public String getSallocGubun() {
		return sallocGubun;
	}

	public void setSallocGubun(String sallocGubun) {
		this.sallocGubun = sallocGubun;
	}

	public List<BigInteger> getnList() {
		return nList;
	}

	public void setnList(List<BigInteger> nList) {
		this.nList = nList;
	}

	public String getSexcpt_yn() {
		return sexcpt_yn;
	}

	public void setSexcpt_yn(String sexcpt_yn) {
		this.sexcpt_yn = sexcpt_yn;
	}

	public String getSexcpt_cd() {
		return sexcpt_cd;
	}

	public void setSexcpt_cd(String sexcpt_cd) {
		this.sexcpt_cd = sexcpt_cd;
	}

	public String getSexcpt_rsn() {
		return sexcpt_rsn;
	}

	public void setSexcpt_rsn(String sexcpt_rsn) {
		this.sexcpt_rsn = sexcpt_rsn;
	}

	public BigInteger getNipChgAssignMstSeq() {
		return nipChgAssignMstSeq;
	}

	public void setNipChgAssignMstSeq(BigInteger nipChgAssignMstSeq) {
		this.nipChgAssignMstSeq = nipChgAssignMstSeq;
	}

	public BigInteger getNipChgAllocMstSeq() {
		return nipChgAllocMstSeq;
	}

	public void setNipChgAllocMstSeq(BigInteger nipChgAllocMstSeq) {
		this.nipChgAllocMstSeq = nipChgAllocMstSeq;
	}

	public String getSexcptYnOrigin() {
		return sexcptYnOrigin;
	}

	public void setSexcptYnOrigin(String sexcptYnOrigin) {
		this.sexcptYnOrigin = sexcptYnOrigin;
	}

	public String getSexcpt_nm() {
		return sexcpt_nm;
	}

	public void setSexcpt_nm(String sexcpt_nm) {
		this.sexcpt_nm = sexcpt_nm;
	}

	public String getSassignChangeYn() {
		return sassignChangeYn;
	}

	public void setSassignChangeYn(String sassignChangeYn) {
		this.sassignChangeYn = sassignChangeYn;
	}

	public List<String> getStrIpBlock() {
		return strIpBlock;
	}

	public void setStrIpBlock(List<String> strIpBlock) {
		this.strIpBlock = strIpBlock;
	}

	public BigInteger getNlvlMstSeq2() {
		return nlvlMstSeq2;
	}

	public void setNlvlMstSeq2(BigInteger nlvlMstSeq2) {
		this.nlvlMstSeq2 = nlvlMstSeq2;
	}

	public String getSneossDdYn() {
		return sneossDdYn;
	}

	public void setSneossDdYn(String sneossDdYn) {
		this.sneossDdYn = sneossDdYn;
	}

	public String getSllnum() {
		return sllnum;
	}

	public void setSllnum(String sllnum) {
		this.sllnum = sllnum;
	}

	public String getSsubcnealias() {
		return ssubcnealias;
	}

	public void setSsubcnealias(String ssubcnealias) {
		this.ssubcnealias = ssubcnealias;
	}

	public String getSexcptNm() {
		return sexcptNm;
	}

	public void setSexcptNm(String sexcptNm) {
		this.sexcptNm = sexcptNm;
	}

	public String getSexcptRsn() {
		return sexcptRsn;
	}

	public void setSexcptRsn(String sexcptRsn) {
		this.sexcptRsn = sexcptRsn;
	}

	public String getSpageType() {
		return spageType;
	}

	public void setSpageType(String spageType) {
		this.spageType = spageType;
	}
	
}
