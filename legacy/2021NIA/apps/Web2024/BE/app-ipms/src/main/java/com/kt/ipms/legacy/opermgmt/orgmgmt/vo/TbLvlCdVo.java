package com.kt.ipms.legacy.opermgmt.orgmgmt.vo;
import java.io.Serializable;
import com.kt.ipms.legacy.cmn.vo.CommonVo;


public class TbLvlCdVo extends CommonVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = -5762554856494840950L;

	private String slvlCd;

	private String slvlNm;

	private String sorgOfficeFlagYn;

	private String sexLinkUseTypeCd;

	private String sexLinkUseTypeNm;

	private String scomment;

	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/

	public void setSlvlCd(String slvlCd) {
		this.slvlCd = slvlCd;
	}

	public String getSlvlCd() {
		return slvlCd;
	}

	public void setSlvlNm(String slvlNm) {
		this.slvlNm = slvlNm;
	}

	public String getSlvlNm() {
		return slvlNm;
	}

	public void setSorgOfficeFlagYn(String sorgOfficeFlagYn) {
		this.sorgOfficeFlagYn = sorgOfficeFlagYn;
	}

	public String getSorgOfficeFlagYn() {
		return sorgOfficeFlagYn;
	}

	public void setSexLinkUseTypeCd(String sexLinkUseTypeCd) {
		this.sexLinkUseTypeCd = sexLinkUseTypeCd;
	}

	public String getSexLinkUseTypeCd() {
		return sexLinkUseTypeCd;
	}

	public void setSexLinkUseTypeNm(String sexLinkUseTypeNm) {
		this.sexLinkUseTypeNm = sexLinkUseTypeNm;
	}

	public String getSexLinkUseTypeNm() {
		return sexLinkUseTypeNm;
	}

	public void setScomment(String scomment) {
		this.scomment = scomment;
	}

	public String getScomment() {
		return scomment;
	}

	/** MEMBER METHOD DECLARATION END **/
}