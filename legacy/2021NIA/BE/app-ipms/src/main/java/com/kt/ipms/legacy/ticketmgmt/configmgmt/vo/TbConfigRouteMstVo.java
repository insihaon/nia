package com.kt.ipms.legacy.ticketmgmt.configmgmt.vo;
import java.io.Serializable;

import com.kt.ipms.legacy.cmn.vo.CommonVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlMstListVo;

import java.math.BigInteger;


public class TbConfigRouteMstVo extends CommonVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = -2624027527672934873L;

	private BigInteger nconfigRouteSeq;

	private String sofficecode;

	private String sfileNm;

	private String shostIp;

	private String shostNm;

	private String srouteIpBlock;

	private String prouteCidr;

	private String srouteGwIp;

	private String srouteGwIfIp;

	private String srouteGwIfNm;

	private String pifCidr;

	private BigInteger nconfigInterfaceSeq;

	private Integer nflagLevel;

	private String ssaid;

	private String sllum;

	private BigInteger nipAllocMstSeq;

	private String sauditFlag;

	private String scomment;
	
	private String sipVersionTypeCd; // IP_버전_유형코드(2014.11.17 추가)
	
	private TbLvlMstListVo lvlMstSeqListVo;

	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/

	public void setNconfigRouteSeq(BigInteger nconfigRouteSeq) {
		this.nconfigRouteSeq = nconfigRouteSeq;
	}

	public BigInteger getNconfigRouteSeq() {
		return nconfigRouteSeq;
	}

	public void setSofficecode(String sofficecode) {
		this.sofficecode = sofficecode;
	}

	public String getSofficecode() {
		return sofficecode;
	}

	public void setSfileNm(String sfileNm) {
		this.sfileNm = sfileNm;
	}

	public String getSfileNm() {
		return sfileNm;
	}

	public void setShostIp(String shostIp) {
		this.shostIp = shostIp;
	}

	public String getShostIp() {
		return shostIp;
	}

	public void setShostNm(String shostNm) {
		this.shostNm = shostNm;
	}

	public String getShostNm() {
		return shostNm;
	}

	public void setSrouteIpBlock(String srouteIpBlock) {
		this.srouteIpBlock = srouteIpBlock;
	}

	public String getSrouteIpBlock() {
		return srouteIpBlock;
	}

	public void setProuteCidr(String prouteCidr) {
		this.prouteCidr = prouteCidr;
	}

	public String getProuteCidr() {
		return prouteCidr;
	}

	public void setSrouteGwIp(String srouteGwIp) {
		this.srouteGwIp = srouteGwIp;
	}

	public String getSrouteGwIp() {
		return srouteGwIp;
	}

	public void setSrouteGwIfIp(String srouteGwIfIp) {
		this.srouteGwIfIp = srouteGwIfIp;
	}

	public String getSrouteGwIfIp() {
		return srouteGwIfIp;
	}

	public void setSrouteGwIfNm(String srouteGwIfNm) {
		this.srouteGwIfNm = srouteGwIfNm;
	}

	public String getSrouteGwIfNm() {
		return srouteGwIfNm;
	}

	public void setPifCidr(String pifCidr) {
		this.pifCidr = pifCidr;
	}

	public String getPifCidr() {
		return pifCidr;
	}

	public void setNconfigInterfaceSeq(BigInteger nconfigInterfaceSeq) {
		this.nconfigInterfaceSeq = nconfigInterfaceSeq;
	}

	public BigInteger getNconfigInterfaceSeq() {
		return nconfigInterfaceSeq;
	}

	public void setNflagLevel(Integer nflagLevel) {
		this.nflagLevel = nflagLevel;
	}

	public Integer getNflagLevel() {
		return nflagLevel;
	}

	public void setSsaid(String ssaid) {
		this.ssaid = ssaid;
	}

	public String getSsaid() {
		return ssaid;
	}

	public void setSllum(String sllum) {
		this.sllum = sllum;
	}

	public String getSllum() {
		return sllum;
	}

	public void setNipAllocMstSeq(BigInteger nipAllocMstSeq) {
		this.nipAllocMstSeq = nipAllocMstSeq;
	}

	public BigInteger getNipAllocMstSeq() {
		return nipAllocMstSeq;
	}

	public void setSauditFlag(String sauditFlag) {
		this.sauditFlag = sauditFlag;
	}

	public String getSauditFlag() {
		return sauditFlag;
	}

	public void setScomment(String scomment) {
		this.scomment = scomment;
	}

	public String getScomment() {
		return scomment;
	}

	public String getSipVersionTypeCd() {
		return sipVersionTypeCd;
	}

	public void setSipVersionTypeCd(String sipVersionTypeCd) {
		this.sipVersionTypeCd = sipVersionTypeCd;
	}

	public TbLvlMstListVo getLvlMstSeqListVo() {
		return lvlMstSeqListVo;
	}

	public void setLvlMstSeqListVo(TbLvlMstListVo lvlMstSeqListVo) {
		this.lvlMstSeqListVo = lvlMstSeqListVo;
	}

	/** MEMBER METHOD DECLARATION END **/
}