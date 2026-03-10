package com.kt.ipms.legacy.opermgmt.orgmgmt.vo;
import java.io.Serializable;
import com.kt.ipms.legacy.cmn.vo.CommonVo;


public class TbLvlRoleVo extends CommonVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = -1142453555104232394L;

	private String slvlCd;

	private String svcnetcode;

	private String headorgscode;

	private String pofficecode;

	private String pofficeNm;

	private String scomment;

	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/

	public void setSlvlCd(String slvlCd) {
		this.slvlCd = slvlCd;
	}

	public String getSlvlCd() {
		return slvlCd;
	}

	public void setSvcnetcode(String svcnetcode) {
		this.svcnetcode = svcnetcode;
	}

	public String getSvcnetcode() {
		return svcnetcode;
	}

	public void setHeadorgscode(String headorgscode) {
		this.headorgscode = headorgscode;
	}

	public String getHeadorgscode() {
		return headorgscode;
	}

	public void setPofficecode(String pofficecode) {
		this.pofficecode = pofficecode;
	}

	public String getPofficecode() {
		return pofficecode;
	}

	public void setPofficeNm(String pofficeNm) {
		this.pofficeNm = pofficeNm;
	}

	public String getPofficeNm() {
		return pofficeNm;
	}

	public void setScomment(String scomment) {
		this.scomment = scomment;
	}

	public String getScomment() {
		return scomment;
	}

	/** MEMBER METHOD DECLARATION END **/
}