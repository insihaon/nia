package com.kt.ipms.legacy.opermgmt.intgrmgmt.vo;

import java.io.Serializable;

import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class TbDefaultSvcMstVo extends CommonVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -776623242955094862L;
	
	/**
	 * 서비스망_유형_CD
	 */
	private String ssvcLineTypeCd;
	
	/**
	 * 서비스망_유형_명
	 */
	private String ssvcLineTypeNm;
	
	/**
	 * 센터/본부_CD
	 */
	private String ssvcGroupCd;
	
	/**
	 * 센터/본부_명
	 */
	private String ssvcGroupNm;
	
	/**
	 * 노드_CD
	 */
	private String ssvcObjCd;
	
	/**
	 * 노드_명
	 */
	private String ssvcObjNm;
	
	/**
	 * IP할당유형_CD
	 */
	private String sassignTypeCd;
	
	/**
	 * 시설표준코드
	 */
	private String ssubsnescode;
	
	/**
	 * 시설장비명
	 */
	private String snealias;

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

	public String getSsvcObjNm() {
		return ssvcObjNm;
	}

	public void setSsvcObjNm(String ssvcObjNm) {
		this.ssvcObjNm = ssvcObjNm;
	}

	public String getSassignTypeCd() {
		return sassignTypeCd;
	}

	public void setSassignTypeCd(String sassignTypeCd) {
		this.sassignTypeCd = sassignTypeCd;
	}

	public String getSsubsnescode() {
		return ssubsnescode;
	}

	public void setSsubsnescode(String ssubsnescode) {
		this.ssubsnescode = ssubsnescode;
	}

	public String getSsvcObjCd() {
		return ssvcObjCd;
	}

	public void setSsvcObjCd(String ssvcObjCd) {
		this.ssvcObjCd = ssvcObjCd;
	}

	public String getSnealias() {
		return snealias;
	}

	public void setSnealias(String snealias) {
		this.snealias = snealias;
	}
	 

}
