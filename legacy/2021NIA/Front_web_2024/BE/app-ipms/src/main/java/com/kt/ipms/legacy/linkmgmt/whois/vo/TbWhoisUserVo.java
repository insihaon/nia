package com.kt.ipms.legacy.linkmgmt.whois.vo;

import java.io.Serializable;

import com.kt.ipms.legacy.cmn.vo.CommonVo;

import java.math.BigInteger;


public class TbWhoisUserVo extends CommonVo implements Serializable {
	
	private static final long serialVersionUID = 6934187158093546756L;

	private String ssaid;
	
	private BigInteger nwhoisSeq;

	private String snetname;

	private String sorgname;

	private String saddr;

	private String saddrDetail;

	private String szipcode;

	private String seorgname;

	private String seaddr;

	private String seaddrDetail;

	private String sadmName;

	private String sadmOrgname;

	private String sadmAddr;

	private String sadmAddrDetail;

	private String sadmZipcode;

	private String sadmEname;

	private String sadmEorgname;

	private String sadmEaddr;

	private String sadmEaddrDetail;

	private String sadmPhone;

	private String sadmDpphone;

	private String sadmFax;

	private String sadmEmail;

	private String sflag;

	private BigInteger saddrseq;

	private String sserialno;

	private String sadmSerialno;

	private String scity;

	private String secity;

	private String sorgtype;

	private String scustname;
	
	private String stransKind;			// Transaction 구분 (시스템입력/사용자입력/담당자입력)

	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/

	public void setSsaid(String ssaid) {
		this.ssaid = ssaid;
	}

	public String getSsaid() {
		return ssaid;
	}

	public void setSnetname(String snetname) {
		this.snetname = snetname;
	}

	public String getSnetname() {
		return snetname;
	}

	public void setSorgname(String sorgname) {
		this.sorgname = sorgname;
	}

	public String getSorgname() {
		return sorgname;
	}

	public void setSaddr(String saddr) {
		this.saddr = saddr;
	}

	public String getSaddr() {
		return saddr;
	}

	public void setSaddrDetail(String saddrDetail) {
		this.saddrDetail = saddrDetail;
	}

	public String getSaddrDetail() {
		return saddrDetail;
	}

	public void setSzipcode(String szipcode) {
		this.szipcode = szipcode;
	}

	public String getSzipcode() {
		return szipcode;
	}

	public void setSeorgname(String seorgname) {
		this.seorgname = seorgname;
	}

	public String getSeorgname() {
		return seorgname;
	}

	public void setSeaddr(String seaddr) {
		this.seaddr = seaddr;
	}

	public String getSeaddr() {
		return seaddr;
	}

	public void setSeaddrDetail(String seaddrDetail) {
		this.seaddrDetail = seaddrDetail;
	}

	public String getSeaddrDetail() {
		return seaddrDetail;
	}

	public void setSadmName(String sadmName) {
		this.sadmName = sadmName;
	}

	public String getSadmName() {
		return sadmName;
	}

	public void setSadmOrgname(String sadmOrgname) {
		this.sadmOrgname = sadmOrgname;
	}

	public String getSadmOrgname() {
		return sadmOrgname;
	}

	public void setSadmAddr(String sadmAddr) {
		this.sadmAddr = sadmAddr;
	}

	public String getSadmAddr() {
		return sadmAddr;
	}

	public void setSadmAddrDetail(String sadmAddrDetail) {
		this.sadmAddrDetail = sadmAddrDetail;
	}

	public String getSadmAddrDetail() {
		return sadmAddrDetail;
	}

	public void setSadmZipcode(String sadmZipcode) {
		this.sadmZipcode = sadmZipcode;
	}

	public String getSadmZipcode() {
		return sadmZipcode;
	}

	public void setSadmEname(String sadmEname) {
		this.sadmEname = sadmEname;
	}

	public String getSadmEname() {
		return sadmEname;
	}

	public void setSadmEorgname(String sadmEorgname) {
		this.sadmEorgname = sadmEorgname;
	}

	public String getSadmEorgname() {
		return sadmEorgname;
	}

	public void setSadmEaddr(String sadmEaddr) {
		this.sadmEaddr = sadmEaddr;
	}

	public String getSadmEaddr() {
		return sadmEaddr;
	}

	public void setSadmEaddrDetail(String sadmEaddrDetail) {
		this.sadmEaddrDetail = sadmEaddrDetail;
	}

	public String getSadmEaddrDetail() {
		return sadmEaddrDetail;
	}

	public void setSadmPhone(String sadmPhone) {
		this.sadmPhone = sadmPhone;
	}

	public String getSadmPhone() {
		return sadmPhone;
	}

	public void setSadmDpphone(String sadmDpphone) {
		this.sadmDpphone = sadmDpphone;
	}

	public String getSadmDpphone() {
		return sadmDpphone;
	}

	public void setSadmFax(String sadmFax) {
		this.sadmFax = sadmFax;
	}

	public String getSadmFax() {
		return sadmFax;
	}

	public void setSadmEmail(String sadmEmail) {
		this.sadmEmail = sadmEmail;
	}

	public String getSadmEmail() {
		return sadmEmail;
	}

	public void setSflag(String sflag) {
		this.sflag = sflag;
	}

	public String getSflag() {
		return sflag;
	}

	public void setSaddrseq(BigInteger saddrseq) {
		this.saddrseq = saddrseq;
	}

	public BigInteger getSaddrseq() {
		return saddrseq;
	}

	public void setSserialno(String sserialno) {
		this.sserialno = sserialno;
	}

	public String getSserialno() {
		return sserialno;
	}

	public void setSadmSerialno(String sadmSerialno) {
		this.sadmSerialno = sadmSerialno;
	}

	public String getSadmSerialno() {
		return sadmSerialno;
	}

	public void setScity(String scity) {
		this.scity = scity;
	}

	public String getScity() {
		return scity;
	}

	public void setSecity(String secity) {
		this.secity = secity;
	}

	public String getSecity() {
		return secity;
	}

	public void setSorgtype(String sorgtype) {
		this.sorgtype = sorgtype;
	}

	public String getSorgtype() {
		return sorgtype;
	}

	public void setScustname(String scustname) {
		this.scustname = scustname;
	}

	public String getScustname() {
		return scustname;
	}

	public BigInteger getNwhoisSeq() {
		return nwhoisSeq;
	}

	public void setNwhoisSeq(BigInteger nwhoisSeq) {
		this.nwhoisSeq = nwhoisSeq;
	}

	public String getStransKind() {
		return stransKind;
	}

	public void setStransKind(String stransKind) {
		this.stransKind = stransKind;
	}

	/** MEMBER METHOD DECLARATION END **/
}