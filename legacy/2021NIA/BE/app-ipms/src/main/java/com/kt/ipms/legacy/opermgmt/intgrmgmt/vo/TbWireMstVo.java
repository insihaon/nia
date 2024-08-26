package com.kt.ipms.legacy.opermgmt.intgrmgmt.vo;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class TbWireMstVo extends CommonVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1132578849182914017L;

	private BigInteger nwireIpCommuSeq;
	
	private String skindCd;
	
	private String scommunity;
	
	private String snexthop;
	
	private BigInteger nlvlBasSeq;
	
	private String ssvcLineTypeCd;
	
	private String ssvcLineTypeNm;
	
	private String ssvcGroupCd;
	
	private String ssvcGroupNm;
	
	private String ssvcObjCd;
	
	private String ssvcObjNm;
	
	private List<BigInteger> delList;

	public BigInteger getNwireIpCommuSeq() {
		return nwireIpCommuSeq;
	}

	public void setNwireIpCommuSeq(BigInteger nwireIpCommuSeq) {
		this.nwireIpCommuSeq = nwireIpCommuSeq;
	}

	public String getSkindCd() {
		return skindCd;
	}

	public void setSkindCd(String skindCd) {
		this.skindCd = skindCd;
	}

	public String getScommunity() {
		return scommunity;
	}

	public void setScommunity(String scommunity) {
		this.scommunity = scommunity;
	}

	public String getSnexthop() {
		return snexthop;
	}

	public void setSnexthop(String snexthop) {
		this.snexthop = snexthop;
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

	public BigInteger getNlvlBasSeq() {
		return nlvlBasSeq;
	}

	public void setNlvlBasSeq(BigInteger nlvlBasSeq) {
		this.nlvlBasSeq = nlvlBasSeq;
	}

	public List<BigInteger> getDelList() {
		return delList;
	}

	public void setDelList(List<BigInteger> delList) {
		this.delList = delList;
	}
	
}
