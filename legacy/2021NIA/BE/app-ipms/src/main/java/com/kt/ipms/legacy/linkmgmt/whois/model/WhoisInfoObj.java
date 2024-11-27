package com.kt.ipms.legacy.linkmgmt.whois.model;

import com.kt.ipms.legacy.cmn.vo.BaseVo;

public class WhoisInfoObj extends BaseVo{

	public String clTRID;			
	
	public String startIP;					// 시작IP
	public String endIP;					// 끝IP
	
	public String koOrgName;			// 한글 기관명
	public String koOrgAddr;				// 한글 주소
	public String koOrgAddrDtl;			// 한글 상세 주소
	public String enOrgName;			// 영문 기관명
	public String enOrgAddr;				// 영문 주소
	public String enOrgAddrDtl;			// 영문 상세 주소
	public String orgPhone;				// 기관 전화번호	
	public String orgEmail;				// 기관 이메일
	public String orgPost;					// 기관 우편번호
	
	public String rtnMsg;			// 반송 메세지
	
	public String netName;				// 네트워크 이름
	public String netType;				// 네트워크  분류
	public String svcType;				// 서비스 분류
	public String ipType;					// IP 분류
	public String orgType;				// 이용기관 분류
	public String svcLoc;					// 서비스 지역
	
	public String comment;				// 추가 사항

	public String rtnReason;			// 반납사유


	public String getClTRID() {
		return clTRID;
	}
	public void setClTRID(String clTRID) {
		this.clTRID = clTRID;
	}
	public String getStartIP() {
		return startIP;
	}
	public void setStartIP(String startIP) {
		this.startIP = startIP;
	}
	public String getEndIP() {
		return endIP;
	}
	public void setEndIP(String endIP) {
		this.endIP = endIP;
	}
	public String getKoOrgName() {
		return koOrgName;
	}
	public void setKoOrgName(String koOrgName) {
		this.koOrgName = koOrgName;
	}
	public String getKoOrgAddr() {
		return koOrgAddr;
	}
	public void setKoOrgAddr(String koOrgAddr) {
		this.koOrgAddr = koOrgAddr;
	}
	public String getKoOrgAddrDtl() {
		return koOrgAddrDtl;
	}
	public void setKoOrgAddrDtl(String koOrgAddrDtl) {
		this.koOrgAddrDtl = koOrgAddrDtl;
	}
	public String getEnOrgName() {
		return enOrgName;
	}
	public void setEnOrgName(String enOrgName) {
		this.enOrgName = enOrgName;
	}
	public String getEnOrgAddr() {
		return enOrgAddr;
	}
	public void setEnOrgAddr(String enOrgAddr) {
		this.enOrgAddr = enOrgAddr;
	}
	public String getEnOrgAddrDtl() {
		return enOrgAddrDtl;
	}
	public void setEnOrgAddrDtl(String enOrgAddrDtl) {
		this.enOrgAddrDtl = enOrgAddrDtl;
	}
	public String getOrgPhone() {
		return orgPhone;
	}
	public void setOrgPhone(String orgPhone) {
		this.orgPhone = orgPhone;
	}
	public String getOrgEmail() {
		return orgEmail;
	}
	public void setOrgEmail(String orgEmail) {
		this.orgEmail = orgEmail;
	}
	public String getOrgPost() {
		return orgPost;
	}
	public void setOrgPost(String orgPost) {
		this.orgPost = orgPost;
	}
	public String getRtnMsg() {
		return rtnMsg;
	}
	public void setRtnMsg(String rtnMsg) {
		this.rtnMsg = rtnMsg;
	}
	public String getNetName() {
		return netName;
	}
	public void setNetName(String netName) {
		this.netName = netName;
	}
	public String getNetType() {
		return netType;
	}
	public void setNetType(String netType) {
		this.netType = netType;
	}
	public String getSvcType() {
		return svcType;
	}
	public void setSvcType(String svcType) {
		this.svcType = svcType;
	}
	public String getIpType() {
		return ipType;
	}
	public void setIpType(String ipType) {
		this.ipType = ipType;
	}
	public String getOrgType() {
		return orgType;
	}
	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}
	public String getSvcLoc() {
		return svcLoc;
	}
	public void setSvcLoc(String svcLoc) {
		this.svcLoc = svcLoc;
	}
	
}
