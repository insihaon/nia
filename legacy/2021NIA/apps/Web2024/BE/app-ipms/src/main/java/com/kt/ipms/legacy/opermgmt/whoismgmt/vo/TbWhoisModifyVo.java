package com.kt.ipms.legacy.opermgmt.whoismgmt.vo;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import com.kt.ipms.legacy.cmn.vo.CommonVo;
import com.kt.ipms.legacy.linkmgmt.whois.model.WhoisInfoObj;

public class TbWhoisModifyVo extends CommonVo implements Serializable {

	private static final long serialVersionUID = -663055434123771614L;
	
	private WhoisInfoObj infoObj;
	
	private String ssearchIp;					// 조회 IP
	
	private String sfirstAddr;					// 시작 IP
	
	private String slastAddr;					// 끝 IP
	
	private String  pip_prefix;					// IP BLOCK_IP형
	
	private String transyn;
	
	private String snettype;
	
	private BigInteger nmodify_apply_seq; 
	
	private BigInteger nwhoisseq;
	
	private String ssaid;

	private String swhoisrequestid; 			// whois request id
	
	private String sBefOrgName;				// 변경전 한글기관명
	
	private String sBefOrgAddr;				// 변경전 한글주소
	
	private String sBefOrgAddrDetail;		// 변경전 한글상세주소
	
	private String sBefZipCode;				// 변경전 우편번호
	
	private String sBefEOrgName;			// 변경전 영문기관명
	
	private String sBefEOrgAddr;				// 변경전 영문주소
	
	private String sBefEOrgAddrDetail;		// 변경전 영문상세주소 
	
	private String sAftOrgName;				// 변경후 한글기관명
	
	private String sAftOrgAddr;				// 변경후 한글주소
					
	private String sAftOrgAddrDetail;		// 변경후 한글상세주소
					
	private String sAftZipCode;				// 변경후 우편번호
					
	private String sAftEOrgName;			// 변경후 영문기관명
					
	private String sAftEOrgAddr;			// 변경후 영문주소
					
	private String sAftEOrgAddrDetail;		// 변경후 영문상세주소 
	
	private String sStatCd;					// 상태_코드
	
	private String sStatNm;					// 상태_명

	private Date dApplyDt;					// 신청일자
	
	private String sApplyId;					// 신청자
	
	private String sApplyNm;					// 신청자명
	
	private Date dApprovalDt;				// 처리일자
	
	private String sApprovalId;				// 처리자
	
	private Date dCreateDt;					// 등록일자
	
	private String sCreateId;					// 등록자
	
	private Date dModifyDt;					// 변경일자
	
	private String sModifyId;					// 변경자
	
	private String sreject_rsn;				// 반려_사유

	public String getSsearchIp() {
		return ssearchIp;
	}

	public void setSsearchIp(String ssearchIp) {
		this.ssearchIp = ssearchIp;
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

	public String getSwhoisrequestid() {
		return swhoisrequestid;
	}

	public void setSwhoisrequestid(String swhoisrequestid) {
		this.swhoisrequestid = swhoisrequestid;
	}

	public WhoisInfoObj getInfoObj() {
		return infoObj;
	}

	public void setInfoObj(WhoisInfoObj infoObj) {
		this.infoObj = infoObj;
	}

	public String getsBefOrgName() {
		return sBefOrgName;
	}

	public void setsBefOrgName(String sBefOrgName) {
		this.sBefOrgName = sBefOrgName;
	}

	public String getsBefOrgAddr() {
		return sBefOrgAddr;
	}

	public void setsBefOrgAddr(String sBefOrgAddr) {
		this.sBefOrgAddr = sBefOrgAddr;
	}

	public String getsBefOrgAddrDetail() {
		return sBefOrgAddrDetail;
	}

	public void setsBefOrgAddrDetail(String sBefOrgAddrDetail) {
		this.sBefOrgAddrDetail = sBefOrgAddrDetail;
	}

	public String getsBefZipCode() {
		return sBefZipCode;
	}

	public void setsBefZipCode(String sBefZipCode) {
		this.sBefZipCode = sBefZipCode;
	}

	public String getsBefEOrgName() {
		return sBefEOrgName;
	}

	public void setsBefEOrgName(String sBefEOrgName) {
		this.sBefEOrgName = sBefEOrgName;
	}

	public String getsBefEOrgAddr() {
		return sBefEOrgAddr;
	}

	public void setsBefEOrgAddr(String sBefEOrgAddr) {
		this.sBefEOrgAddr = sBefEOrgAddr;
	}

	public String getsBefEOrgAddrDetail() {
		return sBefEOrgAddrDetail;
	}

	public void setsBefEOrgAddrDetail(String sBefEOrgAddrDetail) {
		this.sBefEOrgAddrDetail = sBefEOrgAddrDetail;
	}

	public String getsAftOrgName() {
		return sAftOrgName;
	}

	public void setsAftOrgName(String sAftOrgName) {
		this.sAftOrgName = sAftOrgName;
	}

	public String getsAftOrgAddr() {
		return sAftOrgAddr;
	}

	public void setsAftOrgAddr(String sAftOrgAddr) {
		this.sAftOrgAddr = sAftOrgAddr;
	}

	public String getsAftOrgAddrDetail() {
		return sAftOrgAddrDetail;
	}

	public void setsAftOrgAddrDetail(String sAftOrgAddrDetail) {
		this.sAftOrgAddrDetail = sAftOrgAddrDetail;
	}

	public String getsAftZipCode() {
		return sAftZipCode;
	}

	public void setsAftZipCode(String sAftZipCode) {
		this.sAftZipCode = sAftZipCode;
	}

	public String getsAftEOrgName() {
		return sAftEOrgName;
	}

	public void setsAftEOrgName(String sAftEOrgName) {
		this.sAftEOrgName = sAftEOrgName;
	}

	public String getsAftEOrgAddr() {
		return sAftEOrgAddr;
	}

	public void setsAftEOrgAddr(String sAftEOrgAddr) {
		this.sAftEOrgAddr = sAftEOrgAddr;
	}

	public String getsAftEOrgAddrDetail() {
		return sAftEOrgAddrDetail;
	}

	public void setsAftEOrgAddrDetail(String sAftEOrgAddrDetail) {
		this.sAftEOrgAddrDetail = sAftEOrgAddrDetail;
	}

	public String getsStatCd() {
		return sStatCd;
	}

	public void setsStatCd(String sStatCd) {
		this.sStatCd = sStatCd;
	}

	public String getsStatNm() {
		return sStatNm;
	}

	public void setsStatNm(String sStatNm) {
		this.sStatNm = sStatNm;
	}

	public String getsApplyId() {
		return sApplyId;
	}

	public void setsApplyId(String sApplyId) {
		this.sApplyId = sApplyId;
	}

	public String getsApprovalId() {
		return sApprovalId;
	}

	public void setsApprovalId(String sApprovalId) {
		this.sApprovalId = sApprovalId;
	}

	public String getsCreateId() {
		return sCreateId;
	}

	public void setsCreateId(String sCreateId) {
		this.sCreateId = sCreateId;
	}

	public String getsModifyId() {
		return sModifyId;
	}

	public void setsModifyId(String sModifyId) {
		this.sModifyId = sModifyId;
	}

	public Date getdApplyDt() {
		return dApplyDt;
	}

	public void setdApplyDt(Date dApplyDt) {
		this.dApplyDt = dApplyDt;
	}

	public Date getdApprovalDt() {
		return dApprovalDt;
	}

	public void setdApprovalDt(Date dApprovalDt) {
		this.dApprovalDt = dApprovalDt;
	}

	public Date getdCreateDt() {
		return dCreateDt;
	}

	public void setdCreateDt(Date dCreateDt) {
		this.dCreateDt = dCreateDt;
	}

	public Date getdModifyDt() {
		return dModifyDt;
	}

	public void setdModifyDt(Date dModifyDt) {
		this.dModifyDt = dModifyDt;
	}

	public String getsApplyNm() {
		return sApplyNm;
	}

	public void setsApplyNm(String sApplyNm) {
		this.sApplyNm = sApplyNm;
	}

	public BigInteger getNmodify_apply_seq() {
		return nmodify_apply_seq;
	}

	public void setNmodify_apply_seq(BigInteger nmodify_apply_seq) {
		this.nmodify_apply_seq = nmodify_apply_seq;
	}

	public String getPip_prefix() {
		return pip_prefix;
	}

	public void setPip_prefix(String pip_prefix) {
		this.pip_prefix = pip_prefix;
	}

	public BigInteger getNwhoisseq() {
		return nwhoisseq;
	}

	public void setNwhoisseq(BigInteger nwhoisseq) {
		this.nwhoisseq = nwhoisseq;
	}

	public String getTransyn() {
		return transyn;
	}

	public void setTransyn(String transyn) {
		this.transyn = transyn;
	}

	public String getSnettype() {
		return snettype;
	}

	public void setSnettype(String snettype) {
		this.snettype = snettype;
	}

	public String getSsaid() {
		return ssaid;
	}

	public void setSsaid(String ssaid) {
		this.ssaid = ssaid;
	}

	public String getSreject_rsn() {
		return sreject_rsn;
	}

	public void setSreject_rsn(String sreject_rsn) {
		this.sreject_rsn = sreject_rsn;
	}

}
