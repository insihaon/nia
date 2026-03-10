package com.kt.ipms.legacy.opermgmt.privatemgmt.vo;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.kt.ipms.legacy.cmn.vo.IpVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlMstListVo;

public class TbIpPrivateReqMstVo extends IpVo implements Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private BigInteger nipPrivateReqMstSeq;
	
	private String sipPrivateReqMstSeqCd;
	
	private String sipCreateSeqCd;
	
	private String srequestTypeCd;
	
	private String srequestTypeNm;
	
	private String srequestStatCd;
	
	private String srequestStatNm;
	
	private String ssvcLineTypeCd;  // 서비스망 유형 코드

	private String ssvcLineTypeNm;  // 서비스망 유형 명
	
	private String ssvcGroupCd;      // 본부 코드
	
	private String ssvcGroupNm;		// 본부 명
	
	private String ssvcObjCd;			// 국사 코드
	
	private String ssvcObjNm;			// 국사 명
	
	private BigInteger nlvlMstSeq;
	
	private String sipVersionTypeCd;
	
	private String sipCreateTypeCd;			// FN 생성유형 : 공인/사설/Bogon

	private String sipCreateTypeNm;
	
	private String scomment;
	
	private BigInteger nipBlockMstSeq;		// IP_블럭MST_Seq
	
	private Date drequestDt;
	
	private String srequestId;
	
	private Date dapproveDt;
	
	private String sapproveId;
	
	private TbLvlMstListVo lvlMstSeqListVo;

	private String sipCreateSeqNm;
	
	private String sreject_rsn;
	
	private String srequestNm;
	
	private List<BigInteger> nChkList;		// 일괄승인, 반려 건들 체크 seq
	
	private List<String> sChkList;
	
	public BigInteger getNipPrivateReqMstSeq() {
		return nipPrivateReqMstSeq;
	}

	public void setNipPrivateReqMstSeq(BigInteger nipPrivateReqMstSeq) {
		this.nipPrivateReqMstSeq = nipPrivateReqMstSeq;
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

	public BigInteger getNipBlockMstSeq() {
		return nipBlockMstSeq;
	}

	public void setNipBlockMstSeq(BigInteger nipBlockMstSeq) {
		this.nipBlockMstSeq = nipBlockMstSeq;
	}

	public String getScomment() {
		return scomment;
	}

	public void setScomment(String scomment) {
		this.scomment = scomment;
	}

	public Date getDrequestDt() {
		return drequestDt;
	}

	public void setDrequestDt(Date drequestDt) {
		this.drequestDt = drequestDt;
	}

	public String getSrequestId() {
		return srequestId;
	}

	public void setSrequestId(String srequestId) {
		this.srequestId = srequestId;
	}

	public Date getDapproveDt() {
		return dapproveDt;
	}

	public void setDapproveDt(Date dapproveDt) {
		this.dapproveDt = dapproveDt;
	}

	public String getSapproveId() {
		return sapproveId;
	}

	public void setSapproveId(String sapproveId) {
		this.sapproveId = sapproveId;
	}

	public TbLvlMstListVo getLvlMstSeqListVo() {
		return lvlMstSeqListVo;
	}

	public void setLvlMstSeqListVo(TbLvlMstListVo lvlMstSeqListVo) {
		this.lvlMstSeqListVo = lvlMstSeqListVo;
	}

	public String getSrequestTypeCd() {
		return srequestTypeCd;
	}

	public void setSrequestTypeCd(String srequestTypeCd) {
		this.srequestTypeCd = srequestTypeCd;
	}

	public String getSrequestTypeNm() {
		return srequestTypeNm;
	}

	public void setSrequestTypeNm(String srequestTypeNm) {
		this.srequestTypeNm = srequestTypeNm;
	}

	public String getSipCreateSeqCd() {
		return sipCreateSeqCd;
	}

	public void setSipCreateSeqCd(String sipCreateSeqCd) {
		this.sipCreateSeqCd = sipCreateSeqCd;
	}

	public String getSrequestStatCd() {
		return srequestStatCd;
	}

	public void setSrequestStatCd(String srequestStatCd) {
		this.srequestStatCd = srequestStatCd;
	}

	public String getSrequestStatNm() {
		return srequestStatNm;
	}

	public void setSrequestStatNm(String srequestStatNm) {
		this.srequestStatNm = srequestStatNm;
	}

	public String getSipCreateSeqNm() {
		return sipCreateSeqNm;
	}

	public void setSipCreateSeqNm(String sipCreateSeqNm) {
		this.sipCreateSeqNm = sipCreateSeqNm;
	}

	public String getSipPrivateReqMstSeqCd() {
		return sipPrivateReqMstSeqCd;
	}

	public void setSipPrivateReqMstSeqCd(String sipPrivateReqMstSeqCd) {
		this.sipPrivateReqMstSeqCd = sipPrivateReqMstSeqCd;
	}

	public String getSreject_rsn() {
		return sreject_rsn;
	}

	public void setSreject_rsn(String sreject_rsn) {
		this.sreject_rsn = sreject_rsn;
	}

	public String getSrequestNm() {
		return srequestNm;
	}

	public void setSrequestNm(String srequestNm) {
		this.srequestNm = srequestNm;
	}

	public List<BigInteger> getnChkList() {
		return nChkList;
	}

	public void setnChkList(List<BigInteger> nChkList) {
		this.nChkList = nChkList;
	}

	public List<String> getsChkList() {
		return sChkList;
	}

	public void setsChkList(List<String> sChkList) {
		this.sChkList = sChkList;
	}

}
