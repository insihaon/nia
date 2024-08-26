package com.kt.ipms.legacy.opermgmt.intgrmgmt.vo;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class TbFcltCmdMstVo extends CommonVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1283904202293117843L;

	/**
	 * 장비 명령어 MST SEQ
	 */
	private BigInteger nfcltCmdMstSeq;
	
	/**
	 * 장비 타입
	 */
	private String sfcltType;
	
	/**
	 * 장비실행 명령어
	 */
	private String sfcltCmd;
	
	/**
	 * 장비실행 명령어 순서
	 */
	private Integer npriority;
	
	/**
	 * 사용여부
	 */
	private String suseYn;
	
	/**
	 *  삭제 Seq List
	 */
	private List<BigInteger> delList;		
	
	/**
	 * 서비스망 코드
	 */
	private String ssvcLineTypeCd;
	
	/**
	 * 본부 코드
	 */
	private String ssvcGroupCd;
	
	/**
	 * 노드 코드
	 */
	private String ssvcObjCd;
	
	/**
	 * 계위_BAS_SEQ
	 */
	private BigInteger nlvlBasSeq;
	
	/**
	 * 계위_MST_SEQ
	 */
	private BigInteger nlvlMstSeq;
	
	

	public BigInteger getNfcltCmdMstSeq() {
		return nfcltCmdMstSeq;
	}

	public void setNfcltCmdMstSeq(BigInteger nfcltCmdMstSeq) {
		this.nfcltCmdMstSeq = nfcltCmdMstSeq;
	}

	public String getSfcltType() {
		return sfcltType;
	}

	public void setSfcltType(String sfcltType) {
		this.sfcltType = sfcltType;
	}

	public String getSfcltCmd() {
		return sfcltCmd;
	}

	public void setSfcltCmd(String sfcltCmd) {
		this.sfcltCmd = sfcltCmd;
	}

	public Integer getNpriority() {
		return npriority;
	}

	public void setNpriority(Integer npriority) {
		this.npriority = npriority;
	}

	public String getSuseYn() {
		return suseYn;
	}

	public void setSuseYn(String suseYn) {
		this.suseYn = suseYn;
	}

	public List<BigInteger> getDelList() {
		return delList;
	}

	public void setDelList(List<BigInteger> delList) {
		this.delList = delList;
	}

	public String getSsvcLineTypeCd() {
		return ssvcLineTypeCd;
	}

	public void setSsvcLineTypeCd(String ssvcLineTypeCd) {
		this.ssvcLineTypeCd = ssvcLineTypeCd;
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
}
