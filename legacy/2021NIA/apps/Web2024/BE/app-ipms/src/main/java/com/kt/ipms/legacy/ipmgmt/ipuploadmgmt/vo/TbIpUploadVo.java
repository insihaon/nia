package com.kt.ipms.legacy.ipmgmt.ipuploadmgmt.vo;

import java.io.Serializable;
import java.math.BigInteger;

import com.kt.ipms.legacy.cmn.vo.CommonVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlMstListVo;

public class TbIpUploadVo extends CommonVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6094236577118489158L;

	private BigInteger seq;
	
	private String ssvcLineTypeCd;
		
	private String ssvcGroupCd;
	
	private String ssvcObjCd;
	
	private String sFileNm;
	
	private String sSsucessYn;
	
	private String pipprefix;
	
	private String pipprefixRange;
	
	private String sllnum;
		
	private String sicisofficescodeNe;
	
	private String ssubscnealiasNe;
	
	private String smodelnameNe;
	
	private String ssubscmstipNe;
	
	private BigInteger nlvlMstSeq;
	
	private TbLvlMstListVo lvlMstSeqListVo;
	
	private String sicisofficescode;
	
	private String sipCreateTypeCd;
	
	private String sassignTypeCd;
	
	private String ssubscmstip;
	
	private String smodelname;
	
	private String sgatewayip;
	
	private String ssubscnnescode;
	
	private String ssubscnealias;
	
	private String ssubsclgipportseq;
	
	private String ssubsclgipportdescription;

	private String ssubsclgipportip;
	
	private String ssubscrouterserialip;
	
	private String sconnalias;
	
	private String scomment;
	
	public BigInteger getSeq() {
		return seq;
	}

	public void setSeq(BigInteger seq) {
		this.seq = seq;
	}

	public String getsFileNm() {
		return sFileNm;
	}

	public void setsFileNm(String sFileNm) {
		this.sFileNm = sFileNm;
	}

	public String getsSsucessYn() {
		return sSsucessYn;
	}

	public void setsSsucessYn(String sSsucessYn) {
		this.sSsucessYn = sSsucessYn;
	}

	public String getPipprefix() {
		return pipprefix;
	}

	public void setPipprefix(String pipprefix) {
		this.pipprefix = pipprefix;
	}

	public String getSsvcLineTypeCd() {
		return ssvcLineTypeCd;
	}

	public void setSsvcLineTypeCd(String ssvcLineTypeCd) {
		this.ssvcLineTypeCd = ssvcLineTypeCd;
	}

	public String getSsvcGroupCd() {
		return ssvcGroupCd;
	}

	public void setSsvcGroupCd(String ssvcGroupCd) {
		this.ssvcGroupCd = ssvcGroupCd;
	}

	public String getSsvcObjCd() {
		return ssvcObjCd;
	}

	public void setSsvcObjCd(String ssvcObjCd) {
		this.ssvcObjCd = ssvcObjCd;
	}

	public BigInteger getNlvlMstSeq() {
		return nlvlMstSeq;
	}

	public void setNlvlMstSeq(BigInteger nlvlMstSeq) {
		this.nlvlMstSeq = nlvlMstSeq;
	}

	public TbLvlMstListVo getLvlMstSeqListVo() {
		return lvlMstSeqListVo;
	}

	public void setLvlMstSeqListVo(TbLvlMstListVo lvlMstSeqListVo) {
		this.lvlMstSeqListVo = lvlMstSeqListVo;
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

	public String getSassignTypeCd() {
		return sassignTypeCd;
	}

	public void setSassignTypeCd(String sassignTypeCd) {
		this.sassignTypeCd = sassignTypeCd;
	}

	public String getSsubscmstip() {
		return ssubscmstip;
	}

	public void setSsubscmstip(String ssubscmstip) {
		this.ssubscmstip = ssubscmstip;
	}

	public String getSmodelname() {
		return smodelname;
	}

	public void setSmodelname(String smodelname) {
		this.smodelname = smodelname;
	}

	public String getSgatewayip() {
		return sgatewayip;
	}

	public void setSgatewayip(String sgatewayip) {
		this.sgatewayip = sgatewayip;
	}

	public String getSsubscnnescode() {
		return ssubscnnescode;
	}

	public void setSsubscnnescode(String ssubscnnescode) {
		this.ssubscnnescode = ssubscnnescode;
	}

	public String getSsubscnealias() {
		return ssubscnealias;
	}

	public void setSsubscnealias(String ssubscnealias) {
		this.ssubscnealias = ssubscnealias;
	}

	public String getSsubsclgipportseq() {
		return ssubsclgipportseq;
	}

	public void setSsubsclgipportseq(String ssubsclgipportseq) {
		this.ssubsclgipportseq = ssubsclgipportseq;
	}

	public String getSsubsclgipportdescription() {
		return ssubsclgipportdescription;
	}

	public void setSsubsclgipportdescription(String ssubsclgipportdescription) {
		this.ssubsclgipportdescription = ssubsclgipportdescription;
	}

	public String getSsubsclgipportip() {
		return ssubsclgipportip;
	}

	public void setSsubsclgipportip(String ssubsclgipportip) {
		this.ssubsclgipportip = ssubsclgipportip;
	}

	public String getSsubscrouterserialip() {
		return ssubscrouterserialip;
	}

	public void setSsubscrouterserialip(String ssubscrouterserialip) {
		this.ssubscrouterserialip = ssubscrouterserialip;
	}

	public String getSconnalias() {
		return sconnalias;
	}

	public void setSconnalias(String sconnalias) {
		this.sconnalias = sconnalias;
	}

	public String getPipprefixRange() {
		return pipprefixRange;
	}

	public void setPipprefixRange(String pipprefixRange) {
		this.pipprefixRange = pipprefixRange;
	}

	public String getSllnum() {
		return sllnum;
	}

	public void setSllnum(String sllnum) {
		this.sllnum = sllnum;
	}

	public String getScomment() {
		return scomment;
	}

	public void setScomment(String scomment) {
		this.scomment = scomment;
	}
	
}
