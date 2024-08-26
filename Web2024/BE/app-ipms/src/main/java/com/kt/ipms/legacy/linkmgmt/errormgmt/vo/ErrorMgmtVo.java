package com.kt.ipms.legacy.linkmgmt.errormgmt.vo;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class ErrorMgmtVo extends CommonVo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3420715404718865034L;
	
	private BigInteger seq;

	private Integer ipbitmask;
	
	private BigInteger nipAssignMstSeq;
	
	private String slacpBlockYN;
	
	private String ipBlock;
	
	private String svcUseTypeCd;
	
	private String svcUseTypeNm;
	
	private BigInteger ipmsSvcSeq;
	
	private String assignTypeCd;

	private String assignTypeNm;
	
	private String exSvcCd;

	private String exSvcNm;
	
	private String ipVersionTypeCd;

	private String ipVersionTypeNm;
	
	private String ipCreateTypeCd;

	private String ipCreateTypeNm;
	
	private String sipaddr;

	private String eipaddr;
	
	private String gatewayip;
	
	private String ssaid;
	
	private String sllnum;
	
	private String errorResultCd;
	
	private String errorResultNm;
	
	private Date dCreateDt;
	
	private Date dCompleteDt;
	
	private String sCreateId;
	
	private String sModifyId;
	
	private ErrorMgmtListVo errorMgmtListVo;
	
	
	
	public ErrorMgmtListVo getErrorMgmtListVo() {
		return errorMgmtListVo;
	}

	public void setErrorMgmtListVo(ErrorMgmtListVo errorMgmtListVo) {
		this.errorMgmtListVo = errorMgmtListVo;
	}

	public BigInteger getSeq() {
		return seq;
	}

	public void setSeq(BigInteger seq) {
		this.seq = seq;
	}
	
	public Integer getIpbitmask() {
		return ipbitmask;
	}

	public void setIpbitmask(Integer ipbitmask) {
		this.ipbitmask = ipbitmask;
	}

	public BigInteger getNipAssignMstSeq() {
		return nipAssignMstSeq;
	}

	public void setNipAssignMstSeq(BigInteger nipAssignMstSeq) {
		this.nipAssignMstSeq = nipAssignMstSeq;
	}

	public String getSlacpBlockYN() {
		return slacpBlockYN;
	}

	public void setSlacpBlockYN(String slacpBlockYN) {
		this.slacpBlockYN = slacpBlockYN;
	}

	public String getIpBlock() {
		return ipBlock;
	}

	public void setIpBlock(String ipBlock) {
		this.ipBlock = ipBlock;
	}

	public String getSvcUseTypeCd() {
		return svcUseTypeCd;
	}

	public void setSvcUseTypeCd(String svcUseTypeCd) {
		this.svcUseTypeCd = svcUseTypeCd;
	}

	public String getSvcUseTypeNm() {
		return svcUseTypeNm;
	}

	public void setSvcUseTypeNm(String svcUseTypeNm) {
		this.svcUseTypeNm = svcUseTypeNm;
	}

	public BigInteger getIpmsSvcSeq() {
		return ipmsSvcSeq;
	}

	public void setIpmsSvcSeq(BigInteger ipmsSvcSeq) {
		this.ipmsSvcSeq = ipmsSvcSeq;
	}

	public String getAssignTypeCd() {
		return assignTypeCd;
	}

	public void setAssignTypeCd(String assignTypeCd) {
		this.assignTypeCd = assignTypeCd;
	}

	public String getAssignTypeNm() {
		return assignTypeNm;
	}

	public void setAssignTypeNm(String assignTypeNm) {
		this.assignTypeNm = assignTypeNm;
	}

	public String getExSvcCd() {
		return exSvcCd;
	}

	public void setExSvcCd(String exSvcCd) {
		this.exSvcCd = exSvcCd;
	}

	public String getExSvcNm() {
		return exSvcNm;
	}

	public void setExSvcNm(String exSvcNm) {
		this.exSvcNm = exSvcNm;
	}

	public String getIpVersionTypeCd() {
		return ipVersionTypeCd;
	}

	public void setIpVersionTypeCd(String ipVersionTypeCd) {
		this.ipVersionTypeCd = ipVersionTypeCd;
	}

	public String getIpVersionTypeNm() {
		return ipVersionTypeNm;
	}

	public void setIpVersionTypeNm(String ipVersionTypeNm) {
		this.ipVersionTypeNm = ipVersionTypeNm;
	}

	public String getIpCreateTypeCd() {
		return ipCreateTypeCd;
	}

	public void setIpCreateTypeCd(String ipCreateTypeCd) {
		this.ipCreateTypeCd = ipCreateTypeCd;
	}

	public String getIpCreateTypeNm() {
		return ipCreateTypeNm;
	}

	public void setIpCreateTypeNm(String ipCreateTypeNm) {
		this.ipCreateTypeNm = ipCreateTypeNm;
	}

	public String getSipaddr() {
		return sipaddr;
	}

	public void setSipaddr(String sipaddr) {
		this.sipaddr = sipaddr;
	}

	public String getEipaddr() {
		return eipaddr;
	}

	public void setEipaddr(String eipaddr) {
		this.eipaddr = eipaddr;
	}

	public String getGatewayip() {
		return gatewayip;
	}

	public void setGatewayip(String gatewayip) {
		this.gatewayip = gatewayip;
	}

	public String getErrorResultCd() {
		return errorResultCd;
	}

	public void setErrorResultCd(String errorResultCd) {
		this.errorResultCd = errorResultCd;
	}

	public String getErrorResultNm() {
		return errorResultNm;
	}

	public void setErrorResultNm(String errorResultNm) {
		this.errorResultNm = errorResultNm;
	}

	public Date getdCreateDt() {
		return dCreateDt;
	}

	public void setdCreateDt(Date dCreateDt) {
		this.dCreateDt = dCreateDt;
	}

	public Date getdCompleteDt() {
		return dCompleteDt;
	}

	public void setdCompleteDt(Date dCompleteDt) {
		this.dCompleteDt = dCompleteDt;
	}

	public String getsCreateId() {
		return sCreateId;
	}

	public void setsCreateId(String sCreateId) {
		this.sCreateId = sCreateId;
	}

	public String getsModifyId() {
		return sModifyId;
	}

	public void setsModifyId(String sModifyId) {
		this.sModifyId = sModifyId;
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
	
	
}
