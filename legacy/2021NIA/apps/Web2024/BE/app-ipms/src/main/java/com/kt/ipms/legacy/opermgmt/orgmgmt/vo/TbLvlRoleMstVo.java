package com.kt.ipms.legacy.opermgmt.orgmgmt.vo;
import java.io.Serializable;
import com.kt.ipms.legacy.cmn.vo.CommonVo;


public class TbLvlRoleMstVo extends CommonVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = 8089377979533446953L;

	private String ssvcLineTypeCd;

	private String ssvcLineTypeNm;

	private String ssvcGroupCd;

	private String ssvcGroupNm;

	private String ssvcHighCd;

	private String ssvcHighNm;

	private String ssvcObjCd;

	private String ssvcObjNm;

	private String scomment;
	
	private String bfSsvcLineTypeCd;
	
	private String bfSsvcGroupCd;
	
	private String bfSsvcHighCd;
	
	private String bfSsvcObjCd;
	
	private String slvlBasLevelCd;

	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/

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

	public void setSsvcHighCd(String ssvcHighCd) {
		this.ssvcHighCd = ssvcHighCd;
	}

	public String getSsvcHighCd() {
		return ssvcHighCd;
	}

	public void setSsvcHighNm(String ssvcHighNm) {
		this.ssvcHighNm = ssvcHighNm;
	}

	public String getSsvcHighNm() {
		return ssvcHighNm;
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

	public void setScomment(String scomment) {
		this.scomment = scomment;
	}

	public String getScomment() {
		return scomment;
	}

	public String getBfSsvcLineTypeCd() {
		return bfSsvcLineTypeCd;
	}

	public void setBfSsvcLineTypeCd(String bfSsvcLineTypeCd) {
		this.bfSsvcLineTypeCd = bfSsvcLineTypeCd;
	}

	public String getBfSsvcGroupCd() {
		return bfSsvcGroupCd;
	}

	public void setBfSsvcGroupCd(String bfSsvcGroupCd) {
		this.bfSsvcGroupCd = bfSsvcGroupCd;
	}

	public String getBfSsvcHighCd() {
		return bfSsvcHighCd;
	}

	public void setBfSsvcHighCd(String bfSsvcHighCd) {
		this.bfSsvcHighCd = bfSsvcHighCd;
	}

	public String getBfSsvcObjCd() {
		return bfSsvcObjCd;
	}

	public void setBfSsvcObjCd(String bfSsvcObjCd) {
		this.bfSsvcObjCd = bfSsvcObjCd;
	}

	public String getSlvlBasLevelCd() {
		return slvlBasLevelCd;
	}

	public void setSlvlBasLevelCd(String slvlBasLevelCd) {
		this.slvlBasLevelCd = slvlBasLevelCd;
	}



	/** MEMBER METHOD DECLARATION END **/
}