package com.kt.ipms.legacy.opermgmt.intgrmgmt.vo;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class TbFcltMstVo extends CommonVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2143742298473145722L;

	
	/**
	 * 장비_MST_SEQ
	 */
	private BigInteger nfcltMstSeq;
	
	/**
	 * 계위_BAS_SEQ
	 */
	private BigInteger nlvlBasSeq;
	
	/**
	 * 호스트명
	 */
	private String shostNm;
	
	/**
	 * 장비_타입
	 */
	private String sfcltType;
	
	/**
	 * 모델명
	 */
	private String sfcltModelNm;
	
	/**
	 * TELNET_IP
	 */
	private String ptelnetIp;
	
	/**
	 * 장비PORT
	 */
	private Integer nportFclt;
	
	/**
	 * PEER_장비_IP
	 */
	private String pipPeerFclt;
	
	/**
	 * 상위_장비_IP
	 */
	private String pipUpperFclt;
	
	/**
	 * 사용_여부
	 */
	private String suseYn;
	
	/**
	 * 
	 */
	private String ssvcLineTypeCd;
	
	/**
	 * 
	 */
	private String ssvcLineTypeNm;
	
	/**
	 * 
	 */
	private String ssvcGroupCd;
	
	/**
	 * 
	 */
	private String ssvcGroupNm;
	
	/**
	 * 
	 */
	private String ssvcObjCd;
	
	/**
	 * 
	 */
	private String ssvcObjNm;
	
	/**
	 *  삭제 Seq List
	 */
	private List<BigInteger> delList;

	public BigInteger getNfcltMstSeq() {
		return nfcltMstSeq;
	}

	public void setNfcltMstSeq(BigInteger nfcltMstSeq) {
		this.nfcltMstSeq = nfcltMstSeq;
	}

	public BigInteger getNlvlBasSeq() {
		return nlvlBasSeq;
	}

	public void setNlvlBasSeq(BigInteger nlvlBasSeq) {
		this.nlvlBasSeq = nlvlBasSeq;
	}

	public String getShostNm() {
		return shostNm;
	}

	public void setShostNm(String shostNm) {
		this.shostNm = shostNm;
	}

	public String getSfcltType() {
		return sfcltType;
	}

	public void setSfcltType(String sfcltType) {
		this.sfcltType = sfcltType;
	}

	public String getPtelnetIp() {
		return ptelnetIp;
	}

	public void setPtelnetIp(String ptelnetIp) {
		this.ptelnetIp = ptelnetIp;
	}

	public String getPipPeerFclt() {
		return pipPeerFclt;
	}

	public void setPipPeerFclt(String pipPeerFclt) {
		this.pipPeerFclt = pipPeerFclt;
	}

	public String getPipUpperFclt() {
		return pipUpperFclt;
	}

	public void setPipUpperFclt(String pipUpperFclt) {
		this.pipUpperFclt = pipUpperFclt;
	}

	public String getSuseYn() {
		return suseYn;
	}

	public void setSuseYn(String suseYn) {
		this.suseYn = suseYn;
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

	public String getSfcltModelNm() {
		return sfcltModelNm;
	}

	public void setSfcltModelNm(String sfcltModelNm) {
		this.sfcltModelNm = sfcltModelNm;
	}

	public Integer getNportFclt() {
		return nportFclt;
	}

	public void setNportFclt(Integer nportFclt) {
		this.nportFclt = nportFclt;
	}

	public List<BigInteger> getDelList() {
		return delList;
	}

	public void setDelList(List<BigInteger> delList) {
		this.delList = delList;
	}
	
	
}
