package com.kt.ipms.legacy.opermgmt.assignmgmt.vo;
import java.io.Serializable;

import com.kt.ipms.legacy.cmn.vo.CommonVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlMstListVo;

import java.util.Date;
import java.math.BigInteger;


public class TbRequestAssignMstVo extends CommonVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = 1049072958602166476L;

	private String ssvcLineTypeCd;

	private String ssvcLineTypeNm;
	
	private String ssvcGroupCd;
	
	private String ssvcGroupNm;
	
	private String ssvcHighCd;
	
	private String ssvcHighNm;
	
	private String ssvcObjCd;
	
	private String ssvcObjNm;
	
	private BigInteger nrequestAssignSeq;

	private String stitle;

	private String scontents;

	private BigInteger nlvlMstSeq;

	private String sapyUserId;
	
	private String sapyUserNm;
	
	private String sapyUserDeptOrgCd;
	
	private String sapyUserDeptOrgNm;
	
	private String sapyUserFullDeptOrgNm;

	private Date dapyDt;

	private BigInteger napyIpCnt;

	private String strtContents;

	private Date dtrtDt;

	private String strtUserId;
	
	private String strtUserNm;

	private String srequestAssignTypeCd;

	private String srequestAssignTypeNm;
	
	private TbLvlMstListVo lvlMstSeqListVo;
	
	private BigInteger nassignCnt;
	
	private BigInteger nallocCnt;
	
	private BigInteger nassignCeCnt;
	
	private BigInteger nassignClCnt;
	
	private String sFalg;
	
	private BigInteger nL3AllCnt;
	
	private BigInteger nL3OptCnt;
	
	private BigInteger nIpOptCnt;
	
	private String sdhcpFlag;
	
	private BigInteger nCntSum;
	
	private BigInteger nCntAll;
	
	private String sAnalFalg;
	
	private BigInteger nCntNeOSS;
	
	private String sFactFlag;
	
	private BigInteger nAlloc24Cnt;
	
	private BigInteger nAlloc24OverCnt;
	
	private BigInteger nAllocOut24Cnt;
	
	private BigInteger nAllocOut24OverCnt;
	
	private BigInteger nIpCnt;
	
	private String sFinalFalg;
	
	private String sassigncontents;
	
	private BigInteger nassignIpCnt;
	
	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/	
	
	public void setNrequestAssignSeq(BigInteger nrequestAssignSeq) {
		this.nrequestAssignSeq = nrequestAssignSeq;
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

	public BigInteger getNrequestAssignSeq() {
		return nrequestAssignSeq;
	}

	public void setStitle(String stitle) {
		this.stitle = stitle;
	}

	public String getStitle() {
		return stitle;
	}

	public void setScontents(String scontents) {
		this.scontents = scontents;
	}

	public String getScontents() {
		return scontents;
	}

	public void setNlvlMstSeq(BigInteger nlvlMstSeq) {
		this.nlvlMstSeq = nlvlMstSeq;
	}

	public BigInteger getNlvlMstSeq() {
		return nlvlMstSeq;
	}

	public void setSapyUserId(String sapyUserId) {
		this.sapyUserId = sapyUserId;
	}

	public String getSapyUserId() {
		return sapyUserId;
	}

	public void setDapyDt(Date dapyDt) {
		this.dapyDt = dapyDt;
	}

	public Date getDapyDt() {
		return dapyDt;
	}

	public void setNapyIpCnt(BigInteger napyIpCnt) {
		this.napyIpCnt = napyIpCnt;
	}

	public BigInteger getNapyIpCnt() {
		return napyIpCnt;
	}

	public void setStrtContents(String strtContents) {
		this.strtContents = strtContents;
	}

	public String getStrtContents() {
		return strtContents;
	}

	public void setDtrtDt(Date dtrtDt) {
		this.dtrtDt = dtrtDt;
	}

	public Date getDtrtDt() {
		return dtrtDt;
	}

	public void setStrtUserId(String strtUserId) {
		this.strtUserId = strtUserId;
	}

	public String getStrtUserId() {
		return strtUserId;
	}

	
	public TbLvlMstListVo getLvlMstSeqListVo() {
		return lvlMstSeqListVo;
	}

	public void setLvlMstSeqListVo(TbLvlMstListVo lvlMstSeqListVo) {
		this.lvlMstSeqListVo = lvlMstSeqListVo;
	}

	public String getSapyUserNm() {
		return sapyUserNm;
	}

	public void setSapyUserNm(String sapyUserNm) {
		this.sapyUserNm = sapyUserNm;
	}

	public String getSapyUserDeptOrgCd() {
		return sapyUserDeptOrgCd;
	}

	public void setSapyUserDeptOrgCd(String sapyUserDeptOrgCd) {
		this.sapyUserDeptOrgCd = sapyUserDeptOrgCd;
	}

	public String getSapyUserDeptOrgNm() {
		return sapyUserDeptOrgNm;
	}

	public void setSapyUserDeptOrgNm(String sapyUserDeptOrgNm) {
		this.sapyUserDeptOrgNm = sapyUserDeptOrgNm;
	}

	public String getSapyUserFullDeptOrgNm() {
		return sapyUserFullDeptOrgNm;
	}

	public void setSapyUserFullDeptOrgNm(String sapyUserFullDeptOrgNm) {
		this.sapyUserFullDeptOrgNm = sapyUserFullDeptOrgNm;
	}

	public String getStrtUserNm() {
		return strtUserNm;
	}

	public void setStrtUserNm(String strtUserNm) {
		this.strtUserNm = strtUserNm;
	}

	public String getSrequestAssignTypeCd() {
		return srequestAssignTypeCd;
	}

	public void setSrequestAssignTypeCd(String srequestAssignTypeCd) {
		this.srequestAssignTypeCd = srequestAssignTypeCd;
	}

	public String getSrequestAssignTypeNm() {
		return srequestAssignTypeNm;
	}

	public void setSrequestAssignTypeNm(String srequestAssignTypeNm) {
		this.srequestAssignTypeNm = srequestAssignTypeNm;
	}

	public String getSsvcHighCd() {
		return ssvcHighCd;
	}

	public void setSsvcHighCd(String ssvcHighCd) {
		this.ssvcHighCd = ssvcHighCd;
	}

	public String getSsvcHighNm() {
		return ssvcHighNm;
	}

	public void setSsvcHighNm(String ssvcHighNm) {
		this.ssvcHighNm = ssvcHighNm;
	}

	public BigInteger getNassignCnt() {
		return nassignCnt;
	}

	public void setNassignCnt(BigInteger nassignCnt) {
		this.nassignCnt = nassignCnt;
	}

	public BigInteger getNallocCnt() {
		return nallocCnt;
	}

	public void setNallocCnt(BigInteger nallocCnt) {
		this.nallocCnt = nallocCnt;
	}

	public BigInteger getNassignCeCnt() {
		return nassignCeCnt;
	}

	public void setNassignCeCnt(BigInteger nassignCeCnt) {
		this.nassignCeCnt = nassignCeCnt;
	}

	public BigInteger getNassignClCnt() {
		return nassignClCnt;
	}

	public void setNassignClCnt(BigInteger nassignClCnt) {
		this.nassignClCnt = nassignClCnt;
	}

	public String getsFalg() {
		return sFalg;
	}

	public void setsFalg(String sFalg) {
		this.sFalg = sFalg;
	}

	public BigInteger getnL3AllCnt() {
		return nL3AllCnt;
	}

	public void setnL3AllCnt(BigInteger nL3AllCnt) {
		this.nL3AllCnt = nL3AllCnt;
	}

	public BigInteger getnL3OptCnt() {
		return nL3OptCnt;
	}

	public void setnL3OptCnt(BigInteger nL3OptCnt) {
		this.nL3OptCnt = nL3OptCnt;
	}

	public BigInteger getnIpOptCnt() {
		return nIpOptCnt;
	}

	public void setnIpOptCnt(BigInteger nIpOptCnt) {
		this.nIpOptCnt = nIpOptCnt;
	}

	public String getSdhcpFlag() {
		return sdhcpFlag;
	}

	public void setSdhcpFlag(String sdhcpFlag) {
		this.sdhcpFlag = sdhcpFlag;
	}

	public BigInteger getnCntSum() {
		return nCntSum;
	}

	public void setnCntSum(BigInteger nCntSum) {
		this.nCntSum = nCntSum;
	}

	public BigInteger getnCntAll() {
		return nCntAll;
	}

	public void setnCntAll(BigInteger nCntAll) {
		this.nCntAll = nCntAll;
	}

	public String getsAnalFalg() {
		return sAnalFalg;
	}

	public void setsAnalFalg(String sAnalFalg) {
		this.sAnalFalg = sAnalFalg;
	}

	public BigInteger getnCntNeOSS() {
		return nCntNeOSS;
	}

	public void setnCntNeOSS(BigInteger nCntNeOSS) {
		this.nCntNeOSS = nCntNeOSS;
	}

	public String getsFactFlag() {
		return sFactFlag;
	}

	public void setsFactFlag(String sFactFlag) {
		this.sFactFlag = sFactFlag;
	}

	public BigInteger getnAlloc24Cnt() {
		return nAlloc24Cnt;
	}

	public void setnAlloc24Cnt(BigInteger nAlloc24Cnt) {
		this.nAlloc24Cnt = nAlloc24Cnt;
	}

	public BigInteger getnAlloc24OverCnt() {
		return nAlloc24OverCnt;
	}

	public void setnAlloc24OverCnt(BigInteger nAlloc24OverCnt) {
		this.nAlloc24OverCnt = nAlloc24OverCnt;
	}

	public BigInteger getnAllocOut24Cnt() {
		return nAllocOut24Cnt;
	}

	public void setnAllocOut24Cnt(BigInteger nAllocOut24Cnt) {
		this.nAllocOut24Cnt = nAllocOut24Cnt;
	}

	public BigInteger getnAllocOut24OverCnt() {
		return nAllocOut24OverCnt;
	}

	public void setnAllocOut24OverCnt(BigInteger nAllocOut24OverCnt) {
		this.nAllocOut24OverCnt = nAllocOut24OverCnt;
	}

	public BigInteger getnIpCnt() {
		return nIpCnt;
	}

	public void setnIpCnt(BigInteger nIpCnt) {
		this.nIpCnt = nIpCnt;
	}

	public String getsFinalFalg() {
		return sFinalFalg;
	}

	public void setsFinalFalg(String sFinalFalg) {
		this.sFinalFalg = sFinalFalg;
	}

	public String getSassigncontents() {
		return sassigncontents;
	}

	public void setSassigncontents(String sassigncontents) {
		this.sassigncontents = sassigncontents;
	}

	public BigInteger getNassignIpCnt() {
		return nassignIpCnt;
	}

	public void setNassignIpCnt(BigInteger nassignIpCnt) {
		this.nassignIpCnt = nassignIpCnt;
	}
	
	

	/** MEMBER METHOD DECLARATION END **/
}