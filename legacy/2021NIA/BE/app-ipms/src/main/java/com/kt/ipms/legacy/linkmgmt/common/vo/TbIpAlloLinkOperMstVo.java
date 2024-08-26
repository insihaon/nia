package com.kt.ipms.legacy.linkmgmt.common.vo;

import java.io.Serializable;
import java.math.BigInteger;

import com.kt.ipms.legacy.cmn.vo.IpVo;

public class TbIpAlloLinkOperMstVo extends IpVo  implements Serializable{
	
	private static final long serialVersionUID = 6435910605431923092L;
		
	private BigInteger priority;
	
	private String ssvcLineTypeCd;

	private String sicisofficescode;		// 국사코드

	private String sipCreateTypeCd;

	private BigInteger nipmsSvcSeq;

	private String sassignTypeCd;

	private BigInteger nipAssignMstSeq;
	
	private BigInteger nipAllocMstSeq;
	
	private String sexSvcCd;
	
	private String gatewayip;
	
	private String lacpsid;
	
	private String ssaid;
	
	private String sipCreateSeqCd;
	
	private String slacpblockyn;	
	
	private String ssubscnealias;		// 시설명
	
	private String sipBlock;
	
	private Integer nbitmask;
	
	private String sregYn;
	
	private String ssvcUseTypeCd;
	
	private BigInteger nbitmask2;
	
	private String detailRslt;		//	라우팅 정보 비교 결과
	
	private BigInteger nipAllocMstCnt;
	
	private String sordernum;//오더번호
	
 	public void setpriority(BigInteger priority) {
		this.priority = priority;
	}

	public BigInteger getpriority() {
		return priority;
	}

	public String getSsvcLineTypeCd() {
		return ssvcLineTypeCd;
	}

	public void setSsvcLineTypeCd(String ssvcLineTypeCd) {
		this.ssvcLineTypeCd = ssvcLineTypeCd;
	}

	public String getSicisofficescode() {
		return sicisofficescode;
	}

	public void setSicisofficescode(String sicisofficescode) {
		this.sicisofficescode = sicisofficescode;
	}

	public String getSipCreateTypeCd() {
		return sipCreateTypeCd;
	}

	public void setSipCreateTypeCd(String sipCreateTypeCd) {
		this.sipCreateTypeCd = sipCreateTypeCd;
	}

	public BigInteger getNipmsSvcSeq() {
		return nipmsSvcSeq;
	}

	public void setNipmsSvcSeq(BigInteger nipmsSvcSeq) {
		this.nipmsSvcSeq = nipmsSvcSeq;
	}

	public String getSassignTypeCd() {
		return sassignTypeCd;
	}

	public void setSassignTypeCd(String sassignTypeCd) {
		this.sassignTypeCd = sassignTypeCd;
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

	public String getSexSvcCd() {
		return sexSvcCd;
	}

	public void setSexSvcCd(String sexSvcCd) {
		this.sexSvcCd = sexSvcCd;
	}

	public String getGatewayip() {
		return gatewayip;
	}

	public void setGatewayip(String gatewayip) {
		this.gatewayip = gatewayip;
	}

	public String getLacpsid() {
		return lacpsid;
	}

	public void setLacpsid(String lacpsid) {
		this.lacpsid = lacpsid;
	}

	public String getSsaid() {
		return ssaid;
	}

	public void setSsaid(String ssaid) {
		this.ssaid = ssaid;
	}

	public String getSipCreateSeqCd() {
		return sipCreateSeqCd;
	}

	public void setSipCreateSeqCd(String sipCreateSeqCd) {
		this.sipCreateSeqCd = sipCreateSeqCd;
	}

	public String getSlacpblockyn() {
		return slacpblockyn;
	}

	public void setSlacpblockyn(String slacpblockyn) {
		this.slacpblockyn = slacpblockyn;
	}

	public String getSsubscnealias() {
		return ssubscnealias;
	}

	public void setSsubscnealias(String ssubscnealias) {
		this.ssubscnealias = ssubscnealias;
	}

	public void setNbitmask(Integer nbitmask) {
		this.nbitmask = nbitmask;
	}

	public Integer getNbitmask() {
		return nbitmask;
	}
	
	public String getSipBlock() {
		return sipBlock;
	}

	public void setSipBlock(String sipBlock) {
		this.sipBlock = sipBlock;
	}

	public String getSregYn() {
		return sregYn;
	}

	public void setSregYn(String sregYn) {
		this.sregYn = sregYn;
	}

	public String getSsvcUseTypeCd() {
		return ssvcUseTypeCd;
	}

	public void setSsvcUseTypeCd(String ssvcUseTypeCd) {
		this.ssvcUseTypeCd = ssvcUseTypeCd;
	}

	public BigInteger getNbitmask2() {
		return nbitmask2;
	}

	public void setNbitmask2(BigInteger nbitmask2) {
		this.nbitmask2 = nbitmask2;
	}

	public String getDetailRslt() {
		return detailRslt;
	}

	public void setDetailRslt(String detailRslt) {
		this.detailRslt = detailRslt;
	}

	public BigInteger getNipAllocMstCnt() {
		return nipAllocMstCnt;
	}

	public void setNipAllocMstCnt(BigInteger nipAllocMstCnt) {
		this.nipAllocMstCnt = nipAllocMstCnt;
	}

	public String getSordernum() {
		return sordernum;
	}

	public void setSordernum(String sordernum) {
		this.sordernum = sordernum;
	}


}
