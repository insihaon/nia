package com.kt.ipms.legacy.opermgmt.intgrmgmt.vo;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class TbMobileSummMstVo extends CommonVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8271071471180463620L;
	
	/**
	 * 무선_IP_SUMMARY_SEQ
	 */
	private BigInteger nmobileIpSummSeq;
	
	/**
	 *  구분 코드
	 */
	private String skindCd;
	
	/**
	 *  구분 명
	 */
	private String skindNm;
	
	/**
	 *  IP 블록
	 */
	private String pipPrefix;

	/**
	 *  삭제 Seq List
	 */
	private List<BigInteger> delList;

	public BigInteger getNmobileIpSummSeq() {
		return nmobileIpSummSeq;
	}

	public void setNmobileIpSummSeq(BigInteger nmobileIpSummSeq) {
		this.nmobileIpSummSeq = nmobileIpSummSeq;
	}

	public String getSkindCd() {
		return skindCd;
	}

	public void setSkindCd(String skindCd) {
		this.skindCd = skindCd;
	}

	public String getSkindNm() {
		return skindNm;
	}

	public void setSkindNm(String skindNm) {
		this.skindNm = skindNm;
	}

	public String getPipPrefix() {
		return pipPrefix;
	}

	public void setPipPrefix(String pipPrefix) {
		this.pipPrefix = pipPrefix;
	}

	public List<BigInteger> getDelList() {
		return delList;
	}

	public void setDelList(List<BigInteger> delList) {
		this.delList = delList;
	}

}
