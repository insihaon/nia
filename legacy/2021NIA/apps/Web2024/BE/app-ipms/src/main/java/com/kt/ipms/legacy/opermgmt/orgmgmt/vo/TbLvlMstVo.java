package com.kt.ipms.legacy.opermgmt.orgmgmt.vo;
import java.io.Serializable;

import com.kt.ipms.legacy.cmn.vo.CommonVo;

import java.math.BigInteger;
import java.util.List;


public class TbLvlMstVo extends CommonVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = 1983878405864244488L;

	private BigInteger nlvlSeq;

	private String ssvcLineTypeCd;

	private String ssvcGroupCd;

	private String ssvcObjCd;

	private String scomment;
	
	private String suserGradeCd;
	
	private String ssvchighcd;
	
	private String bfSsvcLineTypeCd;

	private String bfSsvcGroupCd;

	private String bfSsvcObjCd;
	
	private String ssvcGroupCdMultiStr;
	
	private List<String> ssvcGroupCdMulti;
	

	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/

	
	public void setNlvlSeq(BigInteger nlvlSeq) {
		this.nlvlSeq = nlvlSeq;
	}

	public String getSsvcGroupCdMultiStr() {
		return ssvcGroupCdMultiStr;
	}

	public void setSsvcGroupCdMultiStr(String ssvcGroupCdMultiStr) {
		this.ssvcGroupCdMultiStr = ssvcGroupCdMultiStr;
	}

	public List<String> getSsvcGroupCdMulti() {
		return ssvcGroupCdMulti;
	}

	public void setSsvcGroupCdMulti(List<String> ssvcGroupCdMulti) {
		this.ssvcGroupCdMulti = ssvcGroupCdMulti;
	}

	public BigInteger getNlvlSeq() {
		return nlvlSeq;
	}

	public void setSsvcLineTypeCd(String ssvcLineTypeCd) {
		this.ssvcLineTypeCd = ssvcLineTypeCd;
	}

	public String getSsvcLineTypeCd() {
		return ssvcLineTypeCd;
	}

	public void setSsvcGroupCd(String ssvcGroupCd) {
		this.ssvcGroupCd = ssvcGroupCd;
	}

	public String getSsvcGroupCd() {
		return ssvcGroupCd;
	}

	public void setSsvcObjCd(String ssvcObjCd) {
		this.ssvcObjCd = ssvcObjCd;
	}

	public String getSsvcObjCd() {
		return ssvcObjCd;
	}

	public void setScomment(String scomment) {
		this.scomment = scomment;
	}

	public String getScomment() {
		return scomment;
	}

	public String getSuserGradeCd() {
		return suserGradeCd;
	}

	public void setSuserGradeCd(String suserGradeCd) {
		this.suserGradeCd = suserGradeCd;
	}

	public String getSsvchighcd() {
		return ssvchighcd;
	}

	public void setSsvchighcd(String ssvchighcd) {
		this.ssvchighcd = ssvchighcd;
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

	public String getBfSsvcObjCd() {
		return bfSsvcObjCd;
	}

	public void setBfSsvcObjCd(String bfSsvcObjCd) {
		this.bfSsvcObjCd = bfSsvcObjCd;
	}
	
	

	/** MEMBER METHOD DECLARATION END **/
}