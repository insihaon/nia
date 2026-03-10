package com.kt.ipms.legacy.ticketmgmt.ticketmgmt.vo;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

import com.kt.ipms.legacy.cmn.vo.CommonVo;


public class TbTicketMstVo extends CommonVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = 3047921262558920839L;

	private BigInteger nticketMstSeq;

	private BigInteger nticketTypeSeq;

	private BigInteger nipAssignMstSeq;

	private String ssvcLineTypeCd;

	private String ssvcLineTypeNm;

	private String ssvcGroupCd;

	private String ssvcGroupNm;

	private String ssvcObjCd;

	private String ssvcObjNm;

	private String sassignLevelCd;

	private String sassignLevelNm;

	private String sassignTypeCd;

	private String sassignTypeNm;

	private BigInteger nipmsSvcSeq;

	private String sipmsSvcNm;

	private String sexSvcCd;

	private String sexSvcNm;

	private String ssvcUseTypeCd;

	private String ssvcUseTypeNm;

	private String sipCreateTypeCd;

	private String sipCreateTypeNm;

	private String sipVersionTypeCd;

	private String sipVersionTypeNm;

	private String pipPrefix;

	private String sipBlock;

	private Integer nbitmask;

	private BigInteger ncnt;

	private BigDecimal nclassCnt;

	private String sactionResultTxt;

	private String scomment;
	
	private BigInteger nlvlMstSeq;

	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/

	public void setNticketMstSeq(BigInteger nticketMstSeq) {
		this.nticketMstSeq = nticketMstSeq;
	}

	public BigInteger getNticketMstSeq() {
		return nticketMstSeq;
	}

	public void setNticketTypeSeq(BigInteger nticketTypeSeq) {
		this.nticketTypeSeq = nticketTypeSeq;
	}

	public BigInteger getNticketTypeSeq() {
		return nticketTypeSeq;
	}

	public void setNipAssignMstSeq(BigInteger nipAssignMstSeq) {
		this.nipAssignMstSeq = nipAssignMstSeq;
	}

	public BigInteger getNipAssignMstSeq() {
		return nipAssignMstSeq;
	}

	public void setSsvcLineTypeCd(String ssvcLineTypeCd) {
		this.ssvcLineTypeCd = ssvcLineTypeCd;
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

	public void setSsvcGroupCd(String ssvcGroupCd) {
		this.ssvcGroupCd = ssvcGroupCd;
	}

	public String getSsvcGroupCd() {
		return ssvcGroupCd;
	}

	public void setSsvcGroupNm(String ssvcGroupNm) {
		this.ssvcGroupNm = ssvcGroupNm;
	}

	public String getSsvcGroupNm() {
		return ssvcGroupNm;
	}

	public void setSsvcObjCd(String ssvcObjCd) {
		this.ssvcObjCd = ssvcObjCd;
	}

	public String getSsvcObjCd() {
		return ssvcObjCd;
	}

	public void setSsvcObjNm(String ssvcObjNm) {
		this.ssvcObjNm = ssvcObjNm;
	}

	public String getSsvcObjNm() {
		return ssvcObjNm;
	}

	public void setSassignLevelCd(String sassignLevelCd) {
		this.sassignLevelCd = sassignLevelCd;
	}

	public String getSassignLevelCd() {
		return sassignLevelCd;
	}

	public void setSassignLevelNm(String sassignLevelNm) {
		this.sassignLevelNm = sassignLevelNm;
	}

	public String getSassignLevelNm() {
		return sassignLevelNm;
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

	public void setSipmsSvcNm(String sipmsSvcNm) {
		this.sipmsSvcNm = sipmsSvcNm;
	}

	public String getSipmsSvcNm() {
		return sipmsSvcNm;
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

	public void setSipCreateTypeCd(String sipCreateTypeCd) {
		this.sipCreateTypeCd = sipCreateTypeCd;
	}

	public String getSipCreateTypeCd() {
		return sipCreateTypeCd;
	}

	public void setSipCreateTypeNm(String sipCreateTypeNm) {
		this.sipCreateTypeNm = sipCreateTypeNm;
	}

	public String getSipCreateTypeNm() {
		return sipCreateTypeNm;
	}

	public void setSipVersionTypeCd(String sipVersionTypeCd) {
		this.sipVersionTypeCd = sipVersionTypeCd;
	}

	public String getSipVersionTypeCd() {
		return sipVersionTypeCd;
	}

	public void setSipVersionTypeNm(String sipVersionTypeNm) {
		this.sipVersionTypeNm = sipVersionTypeNm;
	}

	public String getSipVersionTypeNm() {
		return sipVersionTypeNm;
	}

	public void setPipPrefix(String pipPrefix) {
		this.pipPrefix = pipPrefix;
	}

	public String getPipPrefix() {
		return pipPrefix;
	}

	public void setSipBlock(String sipBlock) {
		this.sipBlock = sipBlock;
	}

	public String getSipBlock() {
		return sipBlock;
	}

	public void setNbitmask(Integer nbitmask) {
		this.nbitmask = nbitmask;
	}

	public Integer getNbitmask() {
		return nbitmask;
	}

	public void setNcnt(BigInteger ncnt) {
		this.ncnt = ncnt;
	}

	public BigInteger getNcnt() {
		return ncnt;
	}

	public void setNclassCnt(BigDecimal nclassCnt) {
		this.nclassCnt = nclassCnt;
	}

	public BigDecimal getNclassCnt() {
		return nclassCnt;
	}

	public void setSactionResultTxt(String sactionResultTxt) {
		this.sactionResultTxt = sactionResultTxt;
	}

	public String getSactionResultTxt() {
		return sactionResultTxt;
	}

	public void setScomment(String scomment) {
		this.scomment = scomment;
	}

	public String getScomment() {
		return scomment;
	}

	public BigInteger getNlvlMstSeq() {
		return nlvlMstSeq;
	}

	public void setNlvlMstSeq(BigInteger nlvlMstSeq) {
		this.nlvlMstSeq = nlvlMstSeq;
	}

	/** MEMBER METHOD DECLARATION END **/
}