package com.kt.ipms.legacy.ipmgmt.tracemgmt.vo;

import java.io.Serializable;
import java.math.BigInteger;
import com.kt.ipms.legacy.cmn.vo.CommonVo;


public class TbTraceIPVo extends CommonVo implements Serializable{

	private static final long serialVersionUID = 2209951420490194643L;
	
	// Trace Req 전용
	private String sprotocolTypeCd;	// 요청 프로토콜 ICMP(I) or TCP(T)
	private String sportNum;		// 요청 포트NUM
	private String sipVersionTypeCd; 
	
	// Trace Req/Res 공용 
	private Integer ntraceIpSeq;		// Trace Seq
	private String sipAddress;	// IP

	
	//Trace Res 전용
	private BigInteger nipHostMstSeq; // ip_homst_mst_seq
	private String sasOrgCd;	// AS기관번호
	private String sasOrgNm;	// AS기관명
	private String sofficecode; // 수용국코드
	private String sofficename; // 수용구명	
	private String sipHostNm;	// 장비명
	private String sipIfNm;		// 인터페이스명
	
	//Trace 조회용 
	private String sipaddresslist;	// IP 조회용
	private String sasOrgCdList;	// AS기관번호 조회용
	
	
	
	public String getSipAddress() {
		return sipAddress;
	}

	public void setSipAddress(String sipAddress) {
		this.sipAddress = sipAddress;
	}

	public String getSprotocolTypeCd() {
		return sprotocolTypeCd;
	}
	
	public void setSprotocolTypeCd(String sprotocolTypeCd) {
		this.sprotocolTypeCd = sprotocolTypeCd;
	}
	
	public String getSportNum() {
		return sportNum;
	}
	
	public void setSportNum(String sportNum) {
		this.sportNum = sportNum;
	}

	public Integer getNtraceIpSeq() {
		return ntraceIpSeq;
	}

	public void setNtraceIpSeq(Integer ntraceIpSeq) {
		this.ntraceIpSeq = ntraceIpSeq;
	}

	public String getSasOrgCd() {
		return sasOrgCd;
	}

	public void setSasOrgCd(String sasOrgCd) {
		this.sasOrgCd = sasOrgCd;
	}

	public String getSasOrgNm() {
		return sasOrgNm;
	}

	public void setSasOrgNm(String sasOrgNm) {
		this.sasOrgNm = sasOrgNm;
	}

	public String getSofficecode() {
		return sofficecode;
	}

	public void setSofficecode(String sofficecode) {
		this.sofficecode = sofficecode;
	}

	public String getSofficename() {
		return sofficename;
	}

	public void setSofficename(String sofficename) {
		this.sofficename = sofficename;
	}

	public String getSipHostNm() {
		return sipHostNm;
	}

	public void setSipHostNm(String sipHostNm) {
		this.sipHostNm = sipHostNm;
	}

	public String getSipIfNm() {
		return sipIfNm;
	}

	public void setSipIfNm(String sipIfNm) {
		this.sipIfNm = sipIfNm;
	}

	public String getSipaddresslist() {
		return sipaddresslist;
	}

	public void setSipaddresslist(String sipaddresslist) {
		this.sipaddresslist = sipaddresslist;
	}

	public String getSasOrgCdList() {
		return sasOrgCdList;
	}

	public void setSasOrgCdList(String sasOrgCdList) {
		this.sasOrgCdList = sasOrgCdList;
	}

	public BigInteger getNipHostMstSeq() {
		return nipHostMstSeq;
	}

	public void setNipHostMstSeq(BigInteger nipHostMstSeq) {
		this.nipHostMstSeq = nipHostMstSeq;
	}

	public String getSipVersionTypeCd() {
		return sipVersionTypeCd;
	}

	public void setSipVersionTypeCd(String sipVersionTypeCd) {
		this.sipVersionTypeCd = sipVersionTypeCd;
	}
	

}
