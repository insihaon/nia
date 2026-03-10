package com.kt.ipms.legacy.opermgmt.intgrmgmt.vo;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class TbMobileMstVo extends CommonVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1629441574883128285L;
	
	/**
	 *  무선_IP_COMMUNITY_SEQ
	 */
	private BigInteger nmobileIpCommuSeq;
	
	/**
	 *  구분 코드
	 */
	private String skindCd;
	
	/**
	 *  구분 명
	 */
	private String skindNm;
	
	/**
	 *  Community
	 */
	private String scommu;
	
	/**
	 *  IP 블록
	 */
	private String pipPrefix;
	
	/**
	 *  서비스 명
	 */
	private String sserviceNm;

	/**
	 *  삭제 Seq List
	 */
	private List<BigInteger> delList;
	
	public BigInteger getNmobileIpCommuSeq() {
		return nmobileIpCommuSeq;
	}

	public void setNmobileIpCommuSeq(BigInteger nmobileIpCommuSeq) {
		this.nmobileIpCommuSeq = nmobileIpCommuSeq;
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

	public String getScommu() {
		return scommu;
	}

	public void setScommu(String scommu) {
		this.scommu = scommu;
	}

	public String getPipPrefix() {
		return pipPrefix;
	}

	public void setPipPrefix(String pipPrefix) {
		this.pipPrefix = pipPrefix;
	}

	public String getSserviceNm() {
		return sserviceNm;
	}

	public void setSserviceNm(String sserviceNm) {
		this.sserviceNm = sserviceNm;
	}

	public List<BigInteger> getDelList() {
		return delList;
	}

	public void setDelList(List<BigInteger> delList) {
		this.delList = delList;
	}
	
	

}
