package com.kt.ipms.legacy.opermgmt.orgmgmt.vo;
import java.io.Serializable;
import com.kt.ipms.legacy.cmn.vo.CommonVo;


public class TbLvlSubCdVo extends CommonVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = -2474280104696263144L;

	private String ssvcLineTypeCd;

	private String ssvcLineTypeNm;

	private String slvlCd;

	private String slvlNm;

	private String slofficecode;

	private String scomment;
	
	private String slvlHighNm;
	
	private String slvlGroupNm;
	
	private String slofficeNm;

	
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

	public void setSlofficecode(String slofficecode) {
		this.slofficecode = slofficecode;
	}

	public String getSlofficecode() {
		return slofficecode;
	}

	public void setScomment(String scomment) {
		this.scomment = scomment;
	}

	public String getScomment() {
		return scomment;
	}

	public String getSlvlHighNm() {
		return slvlHighNm;
	}

	public void setSlvlHighNm(String slvlHighNm) {
		this.slvlHighNm = slvlHighNm;
	}

	public String getSlvlGroupNm() {
		return slvlGroupNm;
	}

	public void setSlvlGroupNm(String slvlGroupNm) {
		this.slvlGroupNm = slvlGroupNm;
	}

	public String getSlofficeNm() {
		return slofficeNm;
	}

	public void setSlofficeNm(String slofficeNm) {
		this.slofficeNm = slofficeNm;
	}

	/** MEMBER METHOD DECLARATION END **/
}