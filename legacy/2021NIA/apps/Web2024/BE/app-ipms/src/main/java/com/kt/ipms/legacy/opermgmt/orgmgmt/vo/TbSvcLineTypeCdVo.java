package com.kt.ipms.legacy.opermgmt.orgmgmt.vo;
import java.io.Serializable;
import com.kt.ipms.legacy.cmn.vo.CommonVo;


public class TbSvcLineTypeCdVo extends CommonVo implements Serializable {

	
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = -4545203868769633954L;
	
	private String ssvcLineTypeCd;
	private String ssvcLineTypeNm;
	private String suseTypeCd;
	private String suseTypeNm;	

	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/
	
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
	public String getSuseTypeCd() {
		return suseTypeCd;
	}
	public void setSuseTypeCd(String suseTypeCd) {
		this.suseTypeCd = suseTypeCd;
	}
	public String getSuseTypeNm() {
		return suseTypeNm;
	}
	public void setSuseTypeNm(String suseTypeNm) {
		this.suseTypeNm = suseTypeNm;
	}	
	

	/** MEMBER METHOD DECLARATION END **/
}