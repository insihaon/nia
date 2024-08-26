package com.kt.ipms.legacy.ipmgmt.ordermgmt.vo;
import java.io.Serializable;
import java.math.BigInteger;

import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlMstListVo;

public class IpAllocOrderMstVo extends TbIpAllocOrderMstVo implements Serializable {
	
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = 6905868261251392236L;

	private String ssvcLineTypeNm;
	
	private String ssvcGroupCd;
	
	private String ssvcGroupNm;
	
	private String ssvcObjCd;
	
	private String ssvcObjNm;
	
	private BigInteger nlvlMstSeq;
	
	private TbLvlMstListVo lvlMstSeqListVo;
	
	/* User Define Start*/
	
	private String llSrchTypeCd; //회선검색유형
	
	/** 검색시작일 */
	private String searchRecpBgnDe = "";

	/** 검색종료일 */
	private String searchRecpEndDe = "";
	
	/** 검색시작일 */
	private String searchInstBgnDe = "";

	/** 검색종료일 */
	private String searchInstEndDe = "";
	
	/* User Define End*/
	
	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/

	public BigInteger getNlvlMstSeq() {
		return nlvlMstSeq;
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

	public void setNlvlMstSeq(BigInteger nlvlMstSeq) {
		this.nlvlMstSeq = nlvlMstSeq;
	}

	public TbLvlMstListVo getLvlMstSeqListVo() {
		return lvlMstSeqListVo;
	}

	public void setLvlMstSeqListVo(TbLvlMstListVo lvlMstSeqListVo) {
		this.lvlMstSeqListVo = lvlMstSeqListVo;
	}

	/* User Define Start*/
	
	public String getLlSrchTypeCd() {
		return llSrchTypeCd;
	}
	
	public void setLlSrchTypeCd(String llSrchTypeCd) {
		this.llSrchTypeCd = llSrchTypeCd;
	}

	public String getSearchRecpBgnDe() {
		return searchRecpBgnDe;
	}

	public void setSearchRecpBgnDe(String searchRecpBgnDe) {
		this.searchRecpBgnDe = searchRecpBgnDe;
	}

	public String getSearchRecpEndDe() {
		return searchRecpEndDe;
	}

	public void setSearchRecpEndDe(String searchRecpEndDe) {
		this.searchRecpEndDe = searchRecpEndDe;
	}

	public String getSearchInstBgnDe() {
		return searchInstBgnDe;
	}

	public void setSearchInstBgnDe(String searchInstBgnDe) {
		this.searchInstBgnDe = searchInstBgnDe;
	}

	public String getSearchInstEndDe() {
		return searchInstEndDe;
	}

	public void setSearchInstEndDe(String searchInstEndDe) {
		this.searchInstEndDe = searchInstEndDe;
	}
	
	
	
	/* User Define END*/
	
	/** MEMBER METHOD DECLARATION END **/
}