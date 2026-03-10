package com.kt.ipms.legacy.statmgmt.ipstatmgmt.vo;

import java.math.BigInteger;

import com.kt.ipms.legacy.cmn.vo.IpVo;

/**
 * 생성차수별 통계 VO
 * @author KimMinSeok
 *
 */
public class IpCreateStatVo extends IpVo {

	private static final long serialVersionUID = -9113263565127423966L;
	
	private String sipCreateSeqCd;
	
	private String sipCreateTypeCd;

	private String sipCreateTypeNm;
	
	private BigInteger ncntTotal; // 전체 IP 수
	
	private BigInteger ncntNotAssign; // 미배정 IP 수
	
	private BigInteger ncntRersAssign; // 예비배정 IP 수
	
	private BigInteger ncntAssign; // 배정 IP 수
	
	private BigInteger ncntServAssign; // 서비스배정 IP 수 
	
	private BigInteger ncntRersAlloc; // 할당예약 IP 수
	
	private BigInteger ncntAlloc; // 할당 IP 수

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

}
