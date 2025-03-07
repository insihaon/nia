package com.kt.ipms.legacy.cmn.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.kt.ipms.legacy.cmn.typehandler.JsonBigDecimalSerializer;
import com.kt.ipms.legacy.cmn.typehandler.JsonBigIntegerSerializer;


public class IpVo extends CommonVo implements Serializable {

	private static final long serialVersionUID = 7308563895231455582L;
	
	private String pipPrefix; // IP BLOCK_IP형_CIDR
	
	private String sipVersionTypeCd; // IP_버전_유형코드
	
	private String sipVersionTypeNm; // IP_버전_유형코드 명
	
	private String sipBlock; // IP BLOCK_문자열
	
	private Integer nbitmask; // BitMask
	
	private String snetmask; // NetMask 
	
	private String sfirstAddr; // 시작_IP_CIDR
	
	private String slastAddr; // 종료_IP_CIDR
	
	private BigInteger nfirstAddr; // 시작_IP_NUMERIC
	
	private BigInteger nlastAddr; // 종료_IP_NUMERIC
	
	private String sfirstIpPreferred; // 시작_IP_Preferred
	
	private String slastIpPreferred; // 종료_IP_Preferred
	
	private String sfirstAddrBinary; // 시작_IP_Binary
	
	private String slastAddrBinary; // 종료_IP_Binary
	
	private BigInteger ncnt; // 총IP수
	
	private BigDecimal nclassCnt; // Class_수
	
	private BigInteger nuseIpCnt; // 사용IP 수

	private BigInteger nfreeIpCnt; // 가용 IP 수	
	
	private Integer nsubnetmask; // subnet bitmask

	private Boolean isFacilities; // 시설용 화면 여부

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
	
	public String getSipVersionTypeNm() {
		return sipVersionTypeNm;
	}

	public void setSipVersionTypeNm(String sipVersionTypeNm) {
		this.sipVersionTypeNm = sipVersionTypeNm;
	}

	public String getSipBlock() {
		return sipBlock;
	}

	public void setSipBlock(String sipBlock) {
		this.sipBlock = sipBlock;
	}

	public Integer getNbitmask() {
		return nbitmask;
	}

	public void setNbitmask(Integer nbitmask) {
		this.nbitmask = nbitmask;
	}

	public String getSnetmask() {
		return snetmask;
	}

	public void setSnetmask(String snetmask) {
		this.snetmask = snetmask;
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
	
	@JsonSerialize(using=JsonBigIntegerSerializer.class)
	public BigInteger getNfirstAddr() {
		return nfirstAddr;
	}

	public void setNfirstAddr(BigInteger nfirstAddr) {
		this.nfirstAddr = nfirstAddr;
	}

	@JsonSerialize(using=JsonBigIntegerSerializer.class)
	public BigInteger getNlastAddr() {
		return nlastAddr;
	}

	public void setNlastAddr(BigInteger nlastAddr) {
		this.nlastAddr = nlastAddr;
	}

	public String getSfirstIpPreferred() {
		return sfirstIpPreferred;
	}

	public void setSfirstIpPreferred(String sfirstIpPreferred) {
		this.sfirstIpPreferred = sfirstIpPreferred;
	}

	public String getSlastIpPreferred() {
		return slastIpPreferred;
	}

	public void setSlastIpPreferred(String slastIpPreferred) {
		this.slastIpPreferred = slastIpPreferred;
	}

	public String getSfirstAddrBinary() {
		return sfirstAddrBinary;
	}

	public void setSfirstAddrBinary(String sfirstAddrBinary) {
		this.sfirstAddrBinary = sfirstAddrBinary;
	}

	public String getSlastAddrBinary() {
		return slastAddrBinary;
	}

	public void setSlastAddrBinary(String slastAddrBinary) {
		this.slastAddrBinary = slastAddrBinary;
	}
	
	@JsonSerialize(using=JsonBigIntegerSerializer.class)
	public BigInteger getNcnt() {
		return ncnt;
	}

	public void setNcnt(BigInteger ncnt) {
		this.ncnt = ncnt;
	}

	@JsonSerialize(using=JsonBigDecimalSerializer.class)
	public BigDecimal getNclassCnt() {
		return nclassCnt;
	}

	public void setNclassCnt(BigDecimal nclassCnt) {
		this.nclassCnt = nclassCnt;
	}
	
	@JsonSerialize(using=JsonBigIntegerSerializer.class)
	public BigInteger getNuseIpCnt() {
		return nuseIpCnt;
	}

	public void setNuseIpCnt(BigInteger nuseIpCnt) {
		this.nuseIpCnt = nuseIpCnt;
	}

	@JsonSerialize(using=JsonBigIntegerSerializer.class)
	public BigInteger getNfreeIpCnt() {
		return nfreeIpCnt;
	}

	public void setNfreeIpCnt(BigInteger nfreeIpCnt) {
		this.nfreeIpCnt = nfreeIpCnt;
	}

	public Integer getNsubnetmask() {
		return nsubnetmask;
	}

	public void setNsubnetmask(Integer nsubnetmask) {
		this.nsubnetmask = nsubnetmask;
	}
	public Boolean getIsFacilities() {
		return isFacilities;
	}

	public void setIsFacilities(Boolean isFacilities) {
		this.isFacilities = isFacilities;
	}
	
}
