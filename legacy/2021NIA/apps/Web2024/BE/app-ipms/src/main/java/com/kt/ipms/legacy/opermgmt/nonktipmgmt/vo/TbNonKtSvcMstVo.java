package com.kt.ipms.legacy.opermgmt.nonktipmgmt.vo;
import java.io.Serializable;
import java.math.BigInteger;

import com.kt.ipms.legacy.cmn.vo.CommonVo;


public class TbNonKtSvcMstVo extends CommonVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = -7353924434406522880L;

	private BigInteger nnonKtSvcMstSeq;

	private String sodrnum;

	private String sregyn;
	
	private String sregynTypeNm;

	private String sworkerid;

	private String sworkername;

	private String ssaid;

	private String sllnum;

	private String scustname;

	private BigInteger nipmsSvcSeq;

	private String ssvcUseTypeCd;

	private String ssvcUseTypeNm;

	private String sexSvcCd;

	private String sexSvcNm;

	private String sinstalladdress;

	private String sinstallroadaddress;

	private String sofficecode;
	
	private String sofficename;

	private String sicisofficescode;
	
	private String sicisofficesname;

	private String senCustname;

	private String sasNum;
	
	private String sipBlock;
	
	private String sflag;
	
	private String sflagTypeNm;
	
	private BigInteger ipBlockCnt;

	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/

	public void setNnonKtSvcMstSeq(BigInteger nnonKtSvcMstSeq) {
		this.nnonKtSvcMstSeq = nnonKtSvcMstSeq;
	}

	public BigInteger getNnonKtSvcMstSeq() {
		return nnonKtSvcMstSeq;
	}

	public void setSodrnum(String sodrnum) {
		this.sodrnum = sodrnum;
	}

	public String getSodrnum() {
		return sodrnum;
	}

	public void setSregyn(String sregyn) {
		this.sregyn = sregyn;
	}

	public String getSregyn() {
		return sregyn;
	}

	public void setSworkerid(String sworkerid) {
		this.sworkerid = sworkerid;
	}

	public String getSworkerid() {
		return sworkerid;
	}

	public void setSworkername(String sworkername) {
		this.sworkername = sworkername;
	}

	public String getSworkername() {
		return sworkername;
	}

	public void setSsaid(String ssaid) {
		this.ssaid = ssaid;
	}

	public String getSsaid() {
		return ssaid;
	}

	public void setSllnum(String sllnum) {
		this.sllnum = sllnum;
	}

	public String getSllnum() {
		return sllnum;
	}

	public void setScustname(String scustname) {
		this.scustname = scustname;
	}

	public String getScustname() {
		return scustname;
	}

	public void setNipmsSvcSeq(BigInteger nipmsSvcSeq) {
		this.nipmsSvcSeq = nipmsSvcSeq;
	}

	public BigInteger getNipmsSvcSeq() {
		return nipmsSvcSeq;
	}

	public void setSsvcUseTypeCd(String ssvcUseTypeCd) {
		this.ssvcUseTypeCd = ssvcUseTypeCd;
	}

	public String getSsvcUseTypeCd() {
		return ssvcUseTypeCd;
	}

	public void setSsvcUseTypeNm(String ssvcUseTypeNm) {
		this.ssvcUseTypeNm = ssvcUseTypeNm;
	}

	public String getSsvcUseTypeNm() {
		return ssvcUseTypeNm;
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

	public void setSinstalladdress(String sinstalladdress) {
		this.sinstalladdress = sinstalladdress;
	}

	public String getSinstalladdress() {
		return sinstalladdress;
	}

	public void setSinstallroadaddress(String sinstallroadaddress) {
		this.sinstallroadaddress = sinstallroadaddress;
	}

	public String getSinstallroadaddress() {
		return sinstallroadaddress;
	}

	public void setSicisofficescode(String sicisofficescode) {
		this.sicisofficescode = sicisofficescode;
	}

	public String getSicisofficescode() {
		return sicisofficescode;
	}

	public void setSenCustname(String senCustname) {
		this.senCustname = senCustname;
	}

	public String getSenCustname() {
		return senCustname;
	}

	public String getSasNum() {
		return sasNum;
	}

	public void setSasNum(String sasNum) {
		this.sasNum = sasNum;
	}

	public String getSofficecode() {
		return sofficecode;
	}

	public void setSofficecode(String sofficecode) {
		this.sofficecode = sofficecode;
	}

	public String getSipBlock() {
		return sipBlock;
	}

	public void setSipBlock(String sipBlock) {
		this.sipBlock = sipBlock;
	}

	public String getSofficename() {
		return sofficename;
	}

	public void setSofficename(String sofficename) {
		this.sofficename = sofficename;
	}

	public String getSicisofficesname() {
		return sicisofficesname;
	}

	public void setSicisofficesname(String sicisofficesname) {
		this.sicisofficesname = sicisofficesname;
	}

	public BigInteger getIpBlockCnt() {
		return ipBlockCnt;
	}

	public void setIpBlockCnt(BigInteger ipBlockCnt) {
		this.ipBlockCnt = ipBlockCnt;
	}

	public String getSregynTypeNm() {
		return sregynTypeNm;
	}

	public void setSregynTypeNm(String sregynTypeNm) {
		this.sregynTypeNm = sregynTypeNm;
	}

	public String getSflag() {
		return sflag;
	}

	public void setSflag(String sflag) {
		this.sflag = sflag;
	}

	public String getSflagTypeNm() {
		return sflagTypeNm;
	}

	public void setSflagTypeNm(String sflagTypeNm) {
		this.sflagTypeNm = sflagTypeNm;
	}



	/** MEMBER METHOD DECLARATION END **/
}