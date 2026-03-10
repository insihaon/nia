package com.kt.ipms.legacy.ipmgmt.createmgmt.vo;
import java.io.Serializable;
import java.math.BigInteger;

import com.kt.ipms.legacy.cmn.vo.IpVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlMstListVo;


public class TbIpBlockMstVo extends IpVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = 2682374582189640598L;

	private BigInteger nipBlockMstSeq;

	private String ssvcLineTypeCd;

	private String ssvcLineTypeNm;

	private String sipCreateTypeCd;

	private String sipCreateTypeNm;

	private String sipCreateSeqCd;

	private String sipCreateSeqNm;

	private String scomment;
	
	private String ssvcGroupCd;
	
	private String ssvcObjCd;

	private TbLvlMstListVo lvlMstSeqListVo;

	private BigInteger nlvlMstSeq;
	
	private String smenuNm;
	
	private String ssvcGroupNm;
	
	private String ssvcObjNm;
	
	private String sMenuHistCd;
	
	private String sMenuType;
	
	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/

	public void setNipBlockMstSeq(BigInteger nipBlockMstSeq) {
		this.nipBlockMstSeq = nipBlockMstSeq;
	}

	public BigInteger getNipBlockMstSeq() {
		return nipBlockMstSeq;
	}

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

	public void setSipCreateTypeCd(String sipCreateTypeCd) {
		this.sipCreateTypeCd = sipCreateTypeCd;
	}

	public String getSipCreateTypeCd() {
		return sipCreateTypeCd;
	}

	public void setSipCreateTypeNm(String sipCreateTypeNm) {
		this.sipCreateTypeNm = sipCreateTypeNm;
	}

	public String getSipCreateTypeNm() {
		return sipCreateTypeNm;
	}

	public void setSipCreateSeqCd(String sipCreateSeqCd) {
		this.sipCreateSeqCd = sipCreateSeqCd;
	}

	public String getSipCreateSeqCd() {
		return sipCreateSeqCd;
	}

	public void setSipCreateSeqNm(String sipCreateSeqNm) {
		this.sipCreateSeqNm = sipCreateSeqNm;
	}

	public String getSipCreateSeqNm() {
		return sipCreateSeqNm;
	}

	public String getScomment() {
		return scomment;
	}

	public void setScomment(String scomment) {
		this.scomment = scomment;
	}

	public String getSsvcGroupCd() {
		return ssvcGroupCd;
	}

	public void setSsvcGroupCd(String ssvcGroupCd) {
		this.ssvcGroupCd = ssvcGroupCd;
	}

	public String getSsvcObjCd() {
		return ssvcObjCd;
	}

	public void setSsvcObjCd(String ssvcObjCd) {
		this.ssvcObjCd = ssvcObjCd;
	}

	public TbLvlMstListVo getLvlMstSeqListVo() {
		return lvlMstSeqListVo;
	}

	public void setLvlMstSeqListVo(TbLvlMstListVo lvlMstSeqListVo) {
		this.lvlMstSeqListVo = lvlMstSeqListVo;
	}

	public BigInteger getNlvlMstSeq() {
		return nlvlMstSeq;
	}

	public void setNlvlMstSeq(BigInteger nlvlMstSeq) {
		this.nlvlMstSeq = nlvlMstSeq;
	}

	public String getSmenuNm() {
		return smenuNm;
	}

	public void setSmenuNm(String smenuNm) {
		this.smenuNm = smenuNm;
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

	public String getsMenuHistCd() {
		return sMenuHistCd;
	}

	public void setsMenuHistCd(String sMenuHistCd) {
		this.sMenuHistCd = sMenuHistCd;
	}

	public String getsMenuType() {
		return sMenuType;
	}

	public void setsMenuType(String sMenuType) {
		this.sMenuType = sMenuType;
	}


	/** MEMBER METHOD DECLARATION END **/
}