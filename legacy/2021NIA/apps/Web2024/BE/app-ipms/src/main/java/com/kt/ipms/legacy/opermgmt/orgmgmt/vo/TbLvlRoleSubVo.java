package com.kt.ipms.legacy.opermgmt.orgmgmt.vo;
import java.io.Serializable;
import com.kt.ipms.legacy.cmn.vo.CommonVo;


public class TbLvlRoleSubVo extends CommonVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = -735862710565122240L;

	private String ssvcLineTypeCd;

	private String ssvcLineTypeNm;

	private String ssvcObjCd;

	private String ssvcObjNm;    

	private String ssvcOfficeCd;

	private String ssvcOfficeNm;

	private String scomment;
	
	private String ssvchighObjNm;
	
	private String ssvchighObjCd;
	
	private String ssvcGroupNm;

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

	public void setSsvcOfficeCd(String ssvcOfficeCd) {
		this.ssvcOfficeCd = ssvcOfficeCd;
	}

	public String getSsvcOfficeCd() {
		return ssvcOfficeCd;
	}

	public void setSsvcOfficeNm(String ssvcOfficeNm) {
		this.ssvcOfficeNm = ssvcOfficeNm;
	}

	public String getSsvcOfficeNm() {
		return ssvcOfficeNm;
	}

	public void setScomment(String scomment) {
		this.scomment = scomment;
	}

	public String getScomment() {
		return scomment;
	}

	public String getSsvchighObjNm() {
		return ssvchighObjNm;
	}

	public void setSsvchighObjNm(String ssvchighObjNm) {
		this.ssvchighObjNm = ssvchighObjNm;
	}

	public String getSsvcGroupNm() {
		return ssvcGroupNm;
	}
	

	public String getSsvchighObjCd() {
		return ssvchighObjCd;
	}

	public void setSsvchighObjCd(String ssvchighObjCd) {
		this.ssvchighObjCd = ssvchighObjCd;
	}

	public void setSsvcGroupNm(String ssvcGroupNm) {
		this.ssvcGroupNm = ssvcGroupNm;
	}

	/** MEMBER METHOD DECLARATION END **/
}