package com.kt.ipms.legacy.opermgmt.intgrmgmt.vo;

import java.io.Serializable;
import java.math.BigInteger;

import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class TbRoutHistMstVo extends CommonVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -635037940001165904L;

	/**
	 * 라우팅 이력관리 MST SEQ
	 */
	private BigInteger nroutHistMstSeq;
	
	/**
	 * TELNET_IP
	 */
	private String ptelnetIp;
	
	/**
	 * 호스트명
	 */
	private String shostNm;
	
	/**
	 * 결과메세지
	 */
	private String sresultMsg;
	
	/**
	 * 계위_BAS_SEQ
	 */
	private BigInteger nlvlBasSeq;
	
	/**
	 * 계위_MST_SEQ
	 */
	private BigInteger nlvlMstSeq;
	
	/**
	 * 진행 여부
	 */
	private String sprocessYn;

	public BigInteger getNroutHistMstSeq() {
		return nroutHistMstSeq;
	}

	public void setNroutHistMstSeq(BigInteger nroutHistMstSeq) {
		this.nroutHistMstSeq = nroutHistMstSeq;
	}

	public String getPtelnetIp() {
		return ptelnetIp;
	}

	public void setPtelnetIp(String ptelnetIp) {
		this.ptelnetIp = ptelnetIp;
	}

	public String getShostNm() {
		return shostNm;
	}

	public void setShostNm(String shostNm) {
		this.shostNm = shostNm;
	}

	public String getSresultMsg() {
		return sresultMsg;
	}

	public void setSresultMsg(String sresultMsg) {
		this.sresultMsg = sresultMsg;
	}

	public BigInteger getNlvlBasSeq() {
		return nlvlBasSeq;
	}

	public void setNlvlBasSeq(BigInteger nlvlBasSeq) {
		this.nlvlBasSeq = nlvlBasSeq;
	}

	public BigInteger getNlvlMstSeq() {
		return nlvlMstSeq;
	}

	public void setNlvlMstSeq(BigInteger nlvlMstSeq) {
		this.nlvlMstSeq = nlvlMstSeq;
	}

	public String getSprocessYn() {
		return sprocessYn;
	}

	public void setSprocessYn(String sprocessYn) {
		this.sprocessYn = sprocessYn;
	}
	
	
	
}
