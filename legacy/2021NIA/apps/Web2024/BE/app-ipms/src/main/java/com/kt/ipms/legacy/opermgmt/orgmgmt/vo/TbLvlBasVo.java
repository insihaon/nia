package com.kt.ipms.legacy.opermgmt.orgmgmt.vo;
import java.io.Serializable;

import com.kt.ipms.legacy.cmn.vo.CommonVo;

import java.math.BigInteger;


public class TbLvlBasVo extends CommonVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = -5845491809555428183L;

	private BigInteger nlvlBasSeq;

	private String ssvcLineTypeCd;
	
	private String ssvcLineTypeNm;

	private String ssvcGroupCd;
	
	private String ssvcGroupNm;

	private String ssvcObjCd;
	
	private String ssvcObjNm;

	private BigInteger nmaxSeq;

	private String slvlBasLevelCd;

	private String scomment;
	
	private String ssvchighObjNm;
	
	private String ssvchighObjCd;
	
	private String orderCnt;
	
	private String fmCnt;
	
	private TbLvlMstListVo lvlMstSeqListVo;
	


	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/

	public void setNlvlBasSeq(BigInteger nlvlBasSeq) {
		this.nlvlBasSeq = nlvlBasSeq;
	}

	public BigInteger getNlvlBasSeq() {
		return nlvlBasSeq;
	}

	public void setSsvcLineTypeCd(String ssvcLineTypeCd) {
		this.ssvcLineTypeCd = ssvcLineTypeCd;
	}

	public String getSsvcLineTypeCd() {
		return ssvcLineTypeCd;
	}	
	

	public String getSsvcLineTypeNm() {
		return ssvcLineTypeNm;
	}

	public void setSsvcLineTypeNm(String ssvcLineTypeNm) {
		this.ssvcLineTypeNm = ssvcLineTypeNm;
	}

	public void setSsvcGroupCd(String ssvcGroupCd) {
		this.ssvcGroupCd = ssvcGroupCd;
	}

	public String getSsvcGroupCd() {
		return ssvcGroupCd;
	}
	
	public String getSsvcGroupNm() {
		return ssvcGroupNm;
	}

	public void setSsvcGroupNm(String ssvcGroupNm) {
		this.ssvcGroupNm = ssvcGroupNm;
	}

	public void setSsvcObjCd(String ssvcObjCd) {
		this.ssvcObjCd = ssvcObjCd;
	}

	public String getSsvcObjCd() {
		return ssvcObjCd;
	}

	public String getSsvcObjNm() {
		return ssvcObjNm;
	}

	public void setSsvcObjNm(String ssvcObjNm) {
		this.ssvcObjNm = ssvcObjNm;
	}

	public void setNmaxSeq(BigInteger nmaxSeq) {
		this.nmaxSeq = nmaxSeq;
	}

	public BigInteger getNmaxSeq() {
		return nmaxSeq;
	}

	public void setSlvlBasLevelCd(String slvlBasLevelCd) {
		this.slvlBasLevelCd = slvlBasLevelCd;
	}

	public String getSlvlBasLevelCd() {
		return slvlBasLevelCd;
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

	public String getSsvchighObjCd() {
		return ssvchighObjCd;
	}

	public void setSsvchighObjCd(String ssvchighObjCd) {
		this.ssvchighObjCd = ssvchighObjCd;
	}

	public String getOrderCnt() {
		return orderCnt;
	}

	public void setOrderCnt(String orderCnt) {
		this.orderCnt = orderCnt;
	}

	public String getFmCnt() {
		return fmCnt;
	}

	public void setFmCnt(String fmCnt) {
		this.fmCnt = fmCnt;
	}

	public TbLvlMstListVo getLvlMstSeqListVo() {
		return lvlMstSeqListVo;
	}

	public void setLvlMstSeqListVo(TbLvlMstListVo lvlMstSeqListVo) {
		this.lvlMstSeqListVo = lvlMstSeqListVo;
	}

	/** MEMBER METHOD DECLARATION END **/
}