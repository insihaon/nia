package com.kt.ipms.legacy.ipmgmt.linemgmt.vo;
import java.io.Serializable;

import com.kt.ipms.legacy.cmn.vo.IpVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlMstListVo;

import java.math.BigInteger;
import java.util.List;


public class TbIpAssignSubVo extends IpVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = -6568430773284550539L;

	private String ssvcLineTypeCd;

	private String ssvcLineTypeNm;
	
	private String ssvcGroupCd;
	
	private String ssvcGroupNm;
	
	private String ssvcObjCd;
	
	private String ssvcObjNm;
	
	private String sipCreateTypeCd;

	private String sipCreateTypeNm;

	private BigInteger nlvlMstSeq;

	private BigInteger nipAssignMstSeq;

	private BigInteger nipAllocMstSeq;

	private BigInteger nipAssignSubSeq;

	private String nipAssignSubNm;

	private String sassignTypeCd;

	private String sassignTypeNm;

	private BigInteger nipmsSvcSeq;
	
	private String sipmsSvcNm;

	private String snetmask;

	private String scomment;
	
	private TbLvlMstListVo lvlMstSeqListVo;
	
	/* User Define Start*/
	private String sicisofficescode; //수용국코드
	
	private String sicisofficesname; //수용국명
	
	private String srssofficescode; //수용국코드
	
	private String srssofficesNm; //수용국명
	
	private String sneSrchTypeCd;//조회 유형(시설)
	
	private String ssubscnnescode; // 장비표준코드
	
	private String sicisofficescodeNe;	//장비수용국
	
	private String ssubscnealiasNe;	//장비별칭
	
	private String smodelnameNe;	//모델명
	
	private String ssubscmstipNe;	//대표IP
	
	private String llSrchTypeCd; //조회 유형(회선)
	
	private String sllnum;	//전용회선번호
	
	private String ssaid;  // 서비스 계약 번호
	
	private String sofficecode; //수용국코드(2014.11.06 -수용국 변경 추가)
	
	private String sofficename; //수용국명(2014.11.06 -수용국 변경 추가)
	
	private String ssvcGroupCdMultiStr;
	
	private List<String> ssvcGroupCdMulti;
	
	/* User Define End*/
	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/
	

	public void setSsvcLineTypeCd(String ssvcLineTypeCd) {
		this.ssvcLineTypeCd = ssvcLineTypeCd;
	}

	public String getSsvcGroupCdMultiStr() {
		return ssvcGroupCdMultiStr;
	}

	public void setSsvcGroupCdMultiStr(String ssvcGroupCdMultiStr) {
		this.ssvcGroupCdMultiStr = ssvcGroupCdMultiStr;
	}

	public List<String> getSsvcGroupCdMulti() {
		return ssvcGroupCdMulti;
	}

	public void setSsvcGroupCdMulti(List<String> ssvcGroupCdMulti) {
		this.ssvcGroupCdMulti = ssvcGroupCdMulti;
	}

	public String getSsvcLineTypeCd() {
		return ssvcLineTypeCd;
	}

	public void setSsvcLineTypeNm(String ssvcLineTypeNm) {
		this.ssvcLineTypeNm = ssvcLineTypeNm;
	}

	public String getSsvcLineTypeNm() {
		return ssvcLineTypeNm;
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

	public String getSipCreateTypeNm() {
		return sipCreateTypeNm;
	}

	public void setSipCreateTypeNm(String sipCreateTypeNm) {
		this.sipCreateTypeNm = sipCreateTypeNm;
	}

	public void setNlvlMstSeq(BigInteger nlvlMstSeq) {
		this.nlvlMstSeq = nlvlMstSeq;
	}

	public BigInteger getNlvlMstSeq() {
		return nlvlMstSeq;
	}

	public void setNipAssignMstSeq(BigInteger nipAssignMstSeq) {
		this.nipAssignMstSeq = nipAssignMstSeq;
	}

	public BigInteger getNipAssignMstSeq() {
		return nipAssignMstSeq;
	}

	public void setNipAllocMstSeq(BigInteger nipAllocMstSeq) {
		this.nipAllocMstSeq = nipAllocMstSeq;
	}

	public BigInteger getNipAllocMstSeq() {
		return nipAllocMstSeq;
	}

	public void setNipAssignSubSeq(BigInteger nipAssignSubSeq) {
		this.nipAssignSubSeq = nipAssignSubSeq;
	}

	public BigInteger getNipAssignSubSeq() {
		return nipAssignSubSeq;
	}

	public void setNipAssignSubNm(String nipAssignSubNm) {
		this.nipAssignSubNm = nipAssignSubNm;
	}

	public String getNipAssignSubNm() {
		return nipAssignSubNm;
	}

	public void setSassignTypeCd(String sassignTypeCd) {
		this.sassignTypeCd = sassignTypeCd;
	}

	public String getSassignTypeCd() {
		return sassignTypeCd;
	}

	public void setSassignTypeNm(String sassignTypeNm) {
		this.sassignTypeNm = sassignTypeNm;
	}

	public String getSassignTypeNm() {
		return sassignTypeNm;
	}

	public void setNipmsSvcSeq(BigInteger nipmsSvcSeq) {
		this.nipmsSvcSeq = nipmsSvcSeq;
	}

	public BigInteger getNipmsSvcSeq() {
		return nipmsSvcSeq;
	}

	public String getSipmsSvcNm() {
		return sipmsSvcNm;
	}

	public void setSipmsSvcNm(String sipmsSvcNm) {
		this.sipmsSvcNm = sipmsSvcNm;
	}

	public void setSnetmask(String snetmask) {
		this.snetmask = snetmask;
	}

	public String getSnetmask() {
		return snetmask;
	}

	public void setScomment(String scomment) {
		this.scomment = scomment;
	}

	public String getScomment() {
		return scomment;
	}

	public TbLvlMstListVo getLvlMstSeqListVo() {
		return lvlMstSeqListVo;
	}

	public void setLvlMstSeqListVo(TbLvlMstListVo lvlMstSeqListVo) {
		this.lvlMstSeqListVo = lvlMstSeqListVo;
	}

	public String getSicisofficescode() {
		return sicisofficescode;
	}

	public void setSicisofficescode(String sicisofficescode) {
		this.sicisofficescode = sicisofficescode;
	}

	public String getSicisofficesname() {
		return sicisofficesname;
	}

	public void setSicisofficesname(String sicisofficesname) {
		this.sicisofficesname = sicisofficesname;
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

	public String getSneSrchTypeCd() {
		return sneSrchTypeCd;
	}

	public void setSneSrchTypeCd(String sneSrchTypeCd) {
		this.sneSrchTypeCd = sneSrchTypeCd;
	}
	
	public String getSsubscnnescode() {
		return ssubscnnescode;
	}

	public void setSsubscnnescode(String ssubscnnescode) {
		this.ssubscnnescode = ssubscnnescode;
	}

	public String getSicisofficescodeNe() {
		return sicisofficescodeNe;
	}

	public void setSicisofficescodeNe(String sicisofficescodeNe) {
		this.sicisofficescodeNe = sicisofficescodeNe;
	}

	public String getSsubscnealiasNe() {
		return ssubscnealiasNe;
	}

	public void setSsubscnealiasNe(String ssubscnealiasNe) {
		this.ssubscnealiasNe = ssubscnealiasNe;
	}

	public String getSmodelnameNe() {
		return smodelnameNe;
	}

	public void setSmodelnameNe(String smodelnameNe) {
		this.smodelnameNe = smodelnameNe;
	}

	public String getSsubscmstipNe() {
		return ssubscmstipNe;
	}

	public void setSsubscmstipNe(String ssubscmstipNe) {
		this.ssubscmstipNe = ssubscmstipNe;
	}

	public String getLlSrchTypeCd() {
		return llSrchTypeCd;
	}

	public void setLlSrchTypeCd(String llSrchTypeCd) {
		this.llSrchTypeCd = llSrchTypeCd;
	}

	public String getSllnum() {
		return sllnum;
	}

	public void setSllnum(String sllnum) {
		this.sllnum = sllnum;
	}

	public String getSsaid() {
		return ssaid;
	}

	public void setSsaid(String ssaid) {
		this.ssaid = ssaid;
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
	
	/** MEMBER METHOD DECLARATION END **/
}