package com.kt.ipms.legacy.ipmgmt.allocmgmt.vo;

import java.io.Serializable;
import java.math.BigInteger;

public class IpmsSvcVo implements Serializable {

	private static final long serialVersionUID = 3020057717847858807L;
	
	private BigInteger nipmsSvcSeq;
	
	private String sassignTypeCd;
	
	private String sexSvcCd;
	
	private String ssvcUseTypeCd;
	
	private String suseTypeCd;
	
	private String ssvcLineTypeCd;

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

	public String getSexSvcCd() {
		return sexSvcCd;
	}

	public void setSexSvcCd(String sexSvcCd) {
		this.sexSvcCd = sexSvcCd;
	}

	public String getSsvcUseTypeCd() {
		return ssvcUseTypeCd;
	}

	public void setSsvcUseTypeCd(String ssvcUseTypeCd) {
		this.ssvcUseTypeCd = ssvcUseTypeCd;
	}

	public String getSuseTypeCd() {
		return suseTypeCd;
	}

	public void setSuseTypeCd(String suseTypeCd) {
		this.suseTypeCd = suseTypeCd;
	}

	public String getSsvcLineTypeCd() {
		return ssvcLineTypeCd;
	}

	public void setSsvcLineTypeCd(String ssvcLineTypeCd) {
		this.ssvcLineTypeCd = ssvcLineTypeCd;
	}
	

}
