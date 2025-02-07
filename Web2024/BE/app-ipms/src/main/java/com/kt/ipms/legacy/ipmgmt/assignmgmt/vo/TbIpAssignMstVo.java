package com.kt.ipms.legacy.ipmgmt.assignmgmt.vo;
import java.io.Serializable;

import com.kt.ipms.legacy.cmn.vo.CommonCodeVo;
import com.kt.ipms.legacy.cmn.vo.IpVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlMstListVo;

import java.math.BigInteger;
import java.util.List;


public class TbIpAssignMstVo extends IpVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = 3585868905953709272L;

	private int groupId;
	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	private String ssvcLineTypeCd;

	private String ssvcLineTypeNm;
	
	private String ssvcGroupCd;
	
	private String ssvcGroupNm;
	
	private String ssvcObjCd;
	
	private String ssvcObjNm;

	private BigInteger nipBlockMstSeq;

	private BigInteger nlvlMstSeq;

	private BigInteger nipAssignMstSeq;

	private BigInteger nipAllocMstCnt;

	private String sassignLevelCd;

	private String sassignLevelNm;

	private String sipCreateSeqCd;

	private String sipCreateSeqNm;

	private String scomment;

	private BigInteger nipmsSvcSeq;

	private String sassignTypeCd;
	
	private List<CommonCodeVo> sassignTypeCds;
	
	public List<CommonCodeVo> getSassignTypeCds() {
		return sassignTypeCds;
	}

	public void setSassignTypeCds(List<CommonCodeVo> sassignTypeCds) {
		this.sassignTypeCds = sassignTypeCds;
	}

	/**
	 * 할당 유형 목록 문자열
	 */
	private String sassignTypeCdMultiStr;

	/**
	 * 할당 유형 List<String>
	 */
	private List<String> sassignTypeMulti;
	
	/**
	 * 서비스망 목록 문자열
	 */
	
	private String ssvcGroupCdMultiStr;
	
	private List<String> ssvcGroupCdMulti;

	
	public String getSsvcGroupCdMultiStr() {
		return ssvcGroupCdMultiStr;
	}

	public void setSsvcGroupCdMultiStr(String ssvcGroupCdMultiStr) {
		this.ssvcGroupCdMultiStr = ssvcGroupCdMultiStr;
	}

	public List<String> getSsvcGroupCdMulti() {
		return ssvcGroupCdMulti;
	}

	public void setSsvcGroupCdMulti(List<String> ssvcGroupCdMulti) {
		this.ssvcGroupCdMulti = ssvcGroupCdMulti;
	}

	private String sassignTypeNm;

	private String sexSvcCd;

	private String sexSvcNm;

	private String sipCreateTypeCd;

	private String sipCreateTypeNm;

	private String sipAllocExTypeCd;

	private String sipAllocExTypeNm;

	private Integer npriority;

	private TbLvlMstListVo lvlMstSeqListVo;
	
	private BigInteger nUnAssignBlockCnt;
	
	private BigInteger nReserveAssignBlockCnt;
	
	private String sGubun;
	
	/* Summary 개수 */
	private Integer summaryCnt; /* Summary 검색조건 */
	private String nsummaryCnt;
	private List<Integer> nsummaryCntMulti;
	
	/* null0 여부 */
	private String snull0Yn;
	
	/* 라우팅 비교/점검 여부 */
	private String sintgrmYn;
	
	private String asPath;
	
	private String scommunity;
	
	private String ssvcHighNm; // 주노드
	
	private BigInteger nipAllocMstSeq;

	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/
	
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

	public void setSsvcLineTypeCd(String ssvcLineTypeCd) {
		this.ssvcLineTypeCd = ssvcLineTypeCd;
	}

	public String getSsvcLineTypeCd() {
		return ssvcLineTypeCd;
	}

	public void setSsvcLineTypeNm(String ssvcLineTypeNm) {
		this.ssvcLineTypeNm = ssvcLineTypeNm;
	}

	public String getSsvcLineTypeNm() {
		return ssvcLineTypeNm;
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

	public void setNipBlockMstSeq(BigInteger nipBlockMstSeq) {
		this.nipBlockMstSeq = nipBlockMstSeq;
	}

	public BigInteger getNipBlockMstSeq() {
		return nipBlockMstSeq;
	}

	public void setNlvlMstSeq(BigInteger nlvlMstSeq) {
		this.nlvlMstSeq = nlvlMstSeq;
	}

	public BigInteger getNlvlMstSeq() {
		return nlvlMstSeq;
	}

	public void setNipAssignMstSeq(BigInteger nipAssignMstSeq) {
		this.nipAssignMstSeq = nipAssignMstSeq;
	}

	public BigInteger getNipAssignMstSeq() {
		return nipAssignMstSeq;
	}

	public void setNipAllocMstCnt(BigInteger nipAllocMstCnt) {
		this.nipAllocMstCnt = nipAllocMstCnt;
	}

	public BigInteger getNipAllocMstCnt() {
		return nipAllocMstCnt;
	}

	public void setSassignLevelCd(String sassignLevelCd) {
		this.sassignLevelCd = sassignLevelCd;
	}

	public String getSassignLevelCd() {
		return sassignLevelCd;
	}

	public void setSassignLevelNm(String sassignLevelNm) {
		this.sassignLevelNm = sassignLevelNm;
	}

	public String getSassignLevelNm() {
		return sassignLevelNm;
	}

	public void setSipCreateSeqCd(String sipCreateSeqCd) {
		this.sipCreateSeqCd = sipCreateSeqCd;
	}

	public String getSipCreateSeqCd() {
		return sipCreateSeqCd;
	}

	public void setSipCreateSeqNm(String sipCreateSeqNm) {
		this.sipCreateSeqNm = sipCreateSeqNm;
	}

	public String getSipCreateSeqNm() {
		return sipCreateSeqNm;
	}

	public void setScomment(String scomment) {
		this.scomment = scomment;
	}

	public String getScomment() {
		return scomment;
	}

	public void setNipmsSvcSeq(BigInteger nipmsSvcSeq) {
		this.nipmsSvcSeq = nipmsSvcSeq;
	}

	public BigInteger getNipmsSvcSeq() {
		return nipmsSvcSeq;
	}

	public void setSassignTypeCd(String sassignTypeCd) {
		this.sassignTypeCd = sassignTypeCd;
	}

	public String getSassignTypeCd() {
		return sassignTypeCd;
	}

	public void setSassignTypeNm(String sassignTypeNm) {
		this.sassignTypeNm = sassignTypeNm;
	}

	public String getSassignTypeNm() {
		return sassignTypeNm;
	}

	public void setSexSvcCd(String sexSvcCd) {
		this.sexSvcCd = sexSvcCd;
	}

	public String getSexSvcCd() {
		return sexSvcCd;
	}

	public void setSexSvcNm(String sexSvcNm) {
		this.sexSvcNm = sexSvcNm;
	}

	public String getSexSvcNm() {
		return sexSvcNm;
	}

	public void setSipCreateTypeCd(String sipCreateTypeCd) {
		this.sipCreateTypeCd = sipCreateTypeCd;
	}

	public String getSipCreateTypeCd() {
		return sipCreateTypeCd;
	}

	public void setSipCreateTypeNm(String sipCreateTypeNm) {
		this.sipCreateTypeNm = sipCreateTypeNm;
	}

	public String getSipCreateTypeNm() {
		return sipCreateTypeNm;
	}

	public void setSipAllocExTypeCd(String sipAllocExTypeCd) {
		this.sipAllocExTypeCd = sipAllocExTypeCd;
	}

	public String getSipAllocExTypeCd() {
		return sipAllocExTypeCd;
	}

	public void setSipAllocExTypeNm(String sipAllocExTypeNm) {
		this.sipAllocExTypeNm = sipAllocExTypeNm;
	}

	public String getSipAllocExTypeNm() {
		return sipAllocExTypeNm;
	}

	public Integer getNpriority() {
		return npriority;
	}

	public void setNpriority(Integer npriority) {
		this.npriority = npriority;
	}

	public TbLvlMstListVo getLvlMstSeqListVo() {
		return lvlMstSeqListVo;
	}

	public void setLvlMstSeqListVo(TbLvlMstListVo lvlMstSeqListVo) {
		this.lvlMstSeqListVo = lvlMstSeqListVo;
	}

	public BigInteger getnUnAssignBlockCnt() {
		return nUnAssignBlockCnt;
	}

	public void setnUnAssignBlockCnt(BigInteger nUnAssignBlockCnt) {
		this.nUnAssignBlockCnt = nUnAssignBlockCnt;
	}

	public BigInteger getnReserveAssignBlockCnt() {
		return nReserveAssignBlockCnt;
	}

	public void setnReserveAssignBlockCnt(BigInteger nReserveAssignBlockCnt) {
		this.nReserveAssignBlockCnt = nReserveAssignBlockCnt;
	}

	public String getsGubun() {
		return sGubun;
	}

	public void setsGubun(String sGubun) {
		this.sGubun = sGubun;
	}

	public String getSnull0Yn() {
		return snull0Yn;
	}

	public void setSnull0Yn(String snull0Yn) {
		this.snull0Yn = snull0Yn;
	}

	public String getSintgrmYn() {
		return sintgrmYn;
	}

	public void setSintgrmYn(String sintgrmYn) {
		this.sintgrmYn = sintgrmYn;
	}

	public Integer getSummaryCnt() {
		return summaryCnt;
	}

	public void setSummaryCnt(Integer summaryCnt) {
		this.summaryCnt = summaryCnt;
	}
	public String getNsummaryCnt() {
		return nsummaryCnt;
	}

	public void setNsummaryCnt(String nsummaryCnt) {
		this.nsummaryCnt = nsummaryCnt;
	}

	public List<Integer> getNsummaryCntMulti() {
		return nsummaryCntMulti;
	}

	public void setNsummaryCntMulti(List<Integer> nsummaryCntMulti) {
		this.nsummaryCntMulti = nsummaryCntMulti;
	}

	public String getAsPath() {
		return asPath;
	}

	public void setAsPath(String asPath) {
		this.asPath = asPath;
	}

	public String getScommunity() {
		return scommunity;
	}

	public void setScommunity(String scommunity) {
		this.scommunity = scommunity;
	}

	public String getSsvcHighNm() {
		return ssvcHighNm;
	}

	public void setSsvcHighNm(String ssvcHighNm) {
		this.ssvcHighNm = ssvcHighNm;
	}

	public BigInteger getNipAllocMstSeq() {
		return nipAllocMstSeq;
	}

	public void setNipAllocMstSeq(BigInteger nipAllocMstSeq) {
		this.nipAllocMstSeq = nipAllocMstSeq;
	}
	
	/** MEMBER METHOD DECLARATION END **/
}