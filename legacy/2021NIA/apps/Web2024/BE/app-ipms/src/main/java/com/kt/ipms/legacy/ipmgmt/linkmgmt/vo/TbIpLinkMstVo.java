package com.kt.ipms.legacy.ipmgmt.linkmgmt.vo;

import java.io.Serializable;
import java.math.BigInteger;

import com.kt.ipms.legacy.cmn.vo.IpVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlMstListVo;

public class TbIpLinkMstVo extends IpVo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7671753486943596702L;

	private BigInteger nipLinkMstSeq;
	
	private String pifSerialIp;
	
	/* 자국 장비명 */
	private String sanealias;
	
	/* 자국 장비IP */
	private String samstip;
	
	/* 자국 IF명 */
	private String saifname;
	
	/* 자국 시설코드 */
	private String salocationcode;
	
	/* 자국 시설명 */
	private String salocationcodeNm;
	
	/* 자국 수용국 코드 */
	private String saofficescode;
	
	/* 자국 수용국 명 */
	private String saofficescodeNm;
	
	/* 대국 장비명 */
	private String sznealias;
	
	/* 대국 장비IP */
	private String szmstip;
	
	/* 대국 IF명 */
	private String szifname;
	
	/* 대국 시설코드 */
	private String szlocationcode;
	
	/* 대국 시설명 */
	private String szlocationcodeNm;
	
	/* 대국 수용국 코드 */
	private String szofficescode;
	
	/* 대국 수용국 명 */
	private String szofficescodeNm;
	
	private String ssvcLineTypeCd;

	private String ssvcLineTypeNm;
	
	private String ssvcGroupCd;
	
	private String ssvcGroupNm;
	
	private String ssvcObjCd;
	
	private String ssvcObjNm;
	
	private String sipCreateTypeCd;
	
	private TbLvlMstListVo lvlMstSeqListVo;
	
	private String srssofficescode;
	
	private String srssofficesNm;
	
	private String sconnalias;
	
	private String ssaid;
	
	private String sllnum;
	
	private String smodelNm;
	
	private String sifNm;
	
	private String llSrchTypeCd;
	
	private BigInteger allocCnt;
	
	public String getSanealias() {
		return sanealias;
	}

	public void setSanealias(String sanealias) {
		this.sanealias = sanealias;
	}

	public String getSamstip() {
		return samstip;
	}

	public void setSamstip(String samstip) {
		this.samstip = samstip;
	}

	public String getSaifname() {
		return saifname;
	}

	public void setSaifname(String saifname) {
		this.saifname = saifname;
	}

	public String getSalocationcode() {
		return salocationcode;
	}

	public void setSalocationcode(String salocationcode) {
		this.salocationcode = salocationcode;
	}

	public String getSalocationcodeNm() {
		return salocationcodeNm;
	}

	public void setSalocationcodeNm(String salocationcodeNm) {
		this.salocationcodeNm = salocationcodeNm;
	}

	public String getSaofficescode() {
		return saofficescode;
	}

	public void setSaofficescode(String saofficescode) {
		this.saofficescode = saofficescode;
	}

	public String getSaofficescodeNm() {
		return saofficescodeNm;
	}

	public void setSaofficescodeNm(String saofficescodeNm) {
		this.saofficescodeNm = saofficescodeNm;
	}

	public String getSznealias() {
		return sznealias;
	}

	public void setSznealias(String sznealias) {
		this.sznealias = sznealias;
	}

	public String getSzmstip() {
		return szmstip;
	}

	public void setSzmstip(String szmstip) {
		this.szmstip = szmstip;
	}

	public String getSzifname() {
		return szifname;
	}

	public void setSzifname(String szifname) {
		this.szifname = szifname;
	}

	public String getSzlocationcode() {
		return szlocationcode;
	}

	public void setSzlocationcode(String szlocationcode) {
		this.szlocationcode = szlocationcode;
	}

	public String getSzlocationcodeNm() {
		return szlocationcodeNm;
	}

	public void setSzlocationcodeNm(String szlocationcodeNm) {
		this.szlocationcodeNm = szlocationcodeNm;
	}

	public String getSzofficescode() {
		return szofficescode;
	}

	public void setSzofficescode(String szofficescode) {
		this.szofficescode = szofficescode;
	}

	public String getSzofficescodeNm() {
		return szofficescodeNm;
	}

	public void setSzofficescodeNm(String szofficescodeNm) {
		this.szofficescodeNm = szofficescodeNm;
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

	public String getSipCreateTypeCd() {
		return sipCreateTypeCd;
	}

	public void setSipCreateTypeCd(String sipCreateTypeCd) {
		this.sipCreateTypeCd = sipCreateTypeCd;
	}

	public TbLvlMstListVo getLvlMstSeqListVo() {
		return lvlMstSeqListVo;
	}

	public void setLvlMstSeqListVo(TbLvlMstListVo lvlMstSeqListVo) {
		this.lvlMstSeqListVo = lvlMstSeqListVo;
	}

	public BigInteger getNipLinkMstSeq() {
		return nipLinkMstSeq;
	}

	public void setNipLinkMstSeq(BigInteger nipLinkMstSeq) {
		this.nipLinkMstSeq = nipLinkMstSeq;
	}

	public String getPifSerialIp() {
		return pifSerialIp;
	}

	public void setPifSerialIp(String pifSerialIp) {
		this.pifSerialIp = pifSerialIp;
	}

	public String getSrssofficescode() {
		return srssofficescode;
	}

	public void setSrssofficescode(String srssofficescode) {
		this.srssofficescode = srssofficescode;
	}

	public String getSrssofficesNm() {
		return srssofficesNm;
	}

	public void setSrssofficesNm(String srssofficesNm) {
		this.srssofficesNm = srssofficesNm;
	}

	public String getSconnalias() {
		return sconnalias;
	}

	public void setSconnalias(String sconnalias) {
		this.sconnalias = sconnalias;
	}

	public String getSsaid() {
		return ssaid;
	}

	public void setSsaid(String ssaid) {
		this.ssaid = ssaid;
	}

	public String getSllnum() {
		return sllnum;
	}

	public void setSllnum(String sllnum) {
		this.sllnum = sllnum;
	}

	public String getSmodelNm() {
		return smodelNm;
	}

	public void setSmodelNm(String smodelNm) {
		this.smodelNm = smodelNm;
	}

	public String getSifNm() {
		return sifNm;
	}

	public void setSifNm(String sifNm) {
		this.sifNm = sifNm;
	}

	public String getLlSrchTypeCd() {
		return llSrchTypeCd;
	}

	public void setLlSrchTypeCd(String llSrchTypeCd) {
		this.llSrchTypeCd = llSrchTypeCd;
	}

	public BigInteger getAllocCnt() {
		return allocCnt;
	}

	public void setAllocCnt(BigInteger allocCnt) {
		this.allocCnt = allocCnt;
	}
}
