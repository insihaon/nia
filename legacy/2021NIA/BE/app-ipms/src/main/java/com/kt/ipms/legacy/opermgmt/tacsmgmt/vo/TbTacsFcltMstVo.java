package com.kt.ipms.legacy.opermgmt.tacsmgmt.vo;
import java.io.Serializable;

import com.kt.ipms.legacy.cmn.vo.CommonVo;

import java.math.BigInteger;


public class TbTacsFcltMstVo extends CommonVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = 8937945140671497944L;

	private BigInteger ntacsFcltMstSeq;

	private BigInteger nlvlBasSeq;

	private String stacsServerId;

	private String sfcltPromptNm;

	private String sfcltModelNm;

	private String sfcltType;

	private String pipFcltInet;
	
	private Integer nportFclt;

	private String suseYn;
	
	private String ssvcLineTypeCd;
	
	private String ssvcLineTypeNm;
	
	private String ssvcGroupCd;
	
	private String ssvcGroupNm;
	
	private String ssvcObjCd;
	
	private String ssvcObjNm;

	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/

	public void setNtacsFcltMstSeq(BigInteger ntacsFcltMstSeq) {
		this.ntacsFcltMstSeq = ntacsFcltMstSeq;
	}

	public BigInteger getNtacsFcltMstSeq() {
		return ntacsFcltMstSeq;
	}

	public void setNlvlBasSeq(BigInteger nlvlBasSeq) {
		this.nlvlBasSeq = nlvlBasSeq;
	}

	public BigInteger getNlvlBasSeq() {
		return nlvlBasSeq;
	}

	public void setStacsServerId(String stacsServerId) {
		this.stacsServerId = stacsServerId;
	}

	public String getStacsServerId() {
		return stacsServerId;
	}

	public void setSfcltPromptNm(String sfcltPromptNm) {
		this.sfcltPromptNm = sfcltPromptNm;
	}

	public String getSfcltPromptNm() {
		return sfcltPromptNm;
	}

	public void setSfcltModelNm(String sfcltModelNm) {
		this.sfcltModelNm = sfcltModelNm;
	}

	public String getSfcltModelNm() {
		return sfcltModelNm;
	}

	public void setSfcltType(String sfcltType) {
		this.sfcltType = sfcltType;
	}

	public String getSfcltType() {
		return sfcltType;
	}

	public void setPipFcltInet(String pipFcltInet) {
		this.pipFcltInet = pipFcltInet;
	}

	public String getPipFcltInet() {
		return pipFcltInet;
	}

	public Integer getNportFclt() {
		return nportFclt;
	}

	public void setNportFclt(Integer nportFclt) {
		this.nportFclt = nportFclt;
	}

	public void setSuseYn(String suseYn) {
		this.suseYn = suseYn;
	}

	public String getSuseYn() {
		return suseYn;
	}

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

	public String getSsvcObjCd() {
		return ssvcObjCd;
	}

	public void setSsvcObjCd(String ssvcObjCd) {
		this.ssvcObjCd = ssvcObjCd;
	}

	public String getSsvcObjNm() {
		return ssvcObjNm;
	}

	public void setSsvcObjNm(String ssvcObjNm) {
		this.ssvcObjNm = ssvcObjNm;
	}
	
	/** MEMBER METHOD DECLARATION END **/
}