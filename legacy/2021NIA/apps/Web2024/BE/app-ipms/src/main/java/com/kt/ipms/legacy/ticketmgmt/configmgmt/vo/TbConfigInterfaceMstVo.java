package com.kt.ipms.legacy.ticketmgmt.configmgmt.vo;
import java.io.Serializable;

import com.kt.ipms.legacy.cmn.vo.CommonVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlMstListVo;

import java.math.BigInteger;


public class TbConfigInterfaceMstVo extends CommonVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = -8082102608838311648L;

	private BigInteger nconfigInterfaceSeq;

	private String sofficecode;
	
	private String sofficename;

	private String sfileNm;

	private String shostIp;

	private String shostNm;

	private String sifIp;
	
	private Integer nifIpBitmask;

	private String pifInet;

	private String pifCidr;

	private String sifNm;

	private String sifDescription;

	private Integer nflagLevel;

	private String ssaid;

	private String sllum;

	private String scomment;
	
	private String ssvcLineTypeCd;

	private String ssvcLineTypeNm;
	
	private String ssvcGroupCd;
	
	private String ssvcGroupNm;
	
	private String ssvcObjCd;
	
	private String ssvcObjNm;
	
	private TbLvlMstListVo lvlMstSeqListVo;

	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/

	public void setNconfigInterfaceSeq(BigInteger nconfigInterfaceSeq) {
		this.nconfigInterfaceSeq = nconfigInterfaceSeq;
	}

	public BigInteger getNconfigInterfaceSeq() {
		return nconfigInterfaceSeq;
	}

	public void setSofficecode(String sofficecode) {
		this.sofficecode = sofficecode;
	}

	public String getSofficecode() {
		return sofficecode;
	}

	public String getSofficename() {
		return sofficename;
	}

	public void setSofficename(String sofficename) {
		this.sofficename = sofficename;
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

	public void setSifIp(String sifIp) {
		this.sifIp = sifIp;
	}

	public String getSifIp() {
		return sifIp;
	}

	public Integer getNifIpBitmask() {
		return nifIpBitmask;
	}

	public void setNifIpBitmask(Integer nifIpBitmask) {
		this.nifIpBitmask = nifIpBitmask;
	}

	public void setPifInet(String pifInet) {
		this.pifInet = pifInet;
	}

	public String getPifInet() {
		return pifInet;
	}

	public void setPifCidr(String pifCidr) {
		this.pifCidr = pifCidr;
	}

	public String getPifCidr() {
		return pifCidr;
	}

	public void setSifNm(String sifNm) {
		this.sifNm = sifNm;
	}

	public String getSifNm() {
		return sifNm;
	}

	public void setSifDescription(String sifDescription) {
		this.sifDescription = sifDescription;
	}

	public String getSifDescription() {
		return sifDescription;
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

	public void setScomment(String scomment) {
		this.scomment = scomment;
	}

	public String getScomment() {
		return scomment;
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

	public TbLvlMstListVo getLvlMstSeqListVo() {
		return lvlMstSeqListVo;
	}

	public void setLvlMstSeqListVo(TbLvlMstListVo lvlMstSeqListVo) {
		this.lvlMstSeqListVo = lvlMstSeqListVo;
	}

	
	/** MEMBER METHOD DECLARATION END **/
}