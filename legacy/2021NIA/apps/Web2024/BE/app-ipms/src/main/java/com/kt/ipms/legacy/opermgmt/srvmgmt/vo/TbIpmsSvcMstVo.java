package com.kt.ipms.legacy.opermgmt.srvmgmt.vo;
import java.io.Serializable;
import com.kt.ipms.legacy.cmn.vo.CommonVo;
import java.math.BigInteger;


public class TbIpmsSvcMstVo extends CommonVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = -7409736533795779233L;

	private BigInteger nipmsSvcSeq;

	private String sipmsSvcNm;

	private String sassignTypeCd;

	private String sassignTypeNm;

	private String ssvcHgroupCd;

	private String ssvcHgroupNm;

	private String ssvcMgroupCd;

	private String ssvcMgroupNm;
	
	private String ssvcMainClsCode;
	
	private String ssvcMgroupNm1;
	
	private String ssvcSubClsCode;

	private String ssvcLgroupCd;

	private String ssvcLgroupNm;

	private String sexSvcCd;

	private String sexSvcNm;
	
	private String sexLinkUseTypeCd;
	
	private String sexLinkUseTypeNm;

	private String ssvcUseTypeCd;

	private String ssvcUseTypeNm;

	private String suseTypeCd;

	private String suseTypeNm;

	private String scomment;
	
	private int checkCount;
	

	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/

	public void setNipmsSvcSeq(BigInteger nipmsSvcSeq) {
		this.nipmsSvcSeq = nipmsSvcSeq;
	}

	public BigInteger getNipmsSvcSeq() {
		return nipmsSvcSeq;
	}

	public void setSipmsSvcNm(String sipmsSvcNm) {
		this.sipmsSvcNm = sipmsSvcNm;
	}

	public String getSipmsSvcNm() {
		return sipmsSvcNm;
	}

	public void setSassignTypeCd(String sassignTypeCd) {
		this.sassignTypeCd = sassignTypeCd;
	}

	public String getSassignTypeCd() {
		return sassignTypeCd;
	}

	public void setSassignTypeNm(String sassignTypeNm) {
		this.sassignTypeNm = sassignTypeNm;
	}

	public String getSassignTypeNm() {
		return sassignTypeNm;
	}

	public void setSsvcHgroupCd(String ssvcHgroupCd) {
		this.ssvcHgroupCd = ssvcHgroupCd;
	}

	public String getSsvcHgroupCd() {
		return ssvcHgroupCd;
	}

	public void setSsvcHgroupNm(String ssvcHgroupNm) {
		this.ssvcHgroupNm = ssvcHgroupNm;
	}

	public String getSsvcHgroupNm() {
		return ssvcHgroupNm;
	}

	public void setSsvcMgroupCd(String ssvcMgroupCd) {
		this.ssvcMgroupCd = ssvcMgroupCd;
	}

	public String getSsvcMgroupCd() {
		return ssvcMgroupCd;
	}

	public void setSsvcMgroupNm(String ssvcMgroupNm) {
		this.ssvcMgroupNm = ssvcMgroupNm;
	}

	public String getSsvcMgroupNm() {
		return ssvcMgroupNm;
	}
	
	public String getSsvcMainClsCode() {
		return ssvcMainClsCode;
	}

	public void setSsvcMainClsCode(String ssvcMainClsCode) {
		this.ssvcMainClsCode = ssvcMainClsCode;
	}
	
	public String getSsvcMgroupNm1() {
		return ssvcMgroupNm1;
	}

	public void setSsvcMgroupNm1(String ssvcMgroupNm1) {
		this.ssvcMgroupNm1 = ssvcMgroupNm1;
	}

	public void setSsvcLgroupCd(String ssvcLgroupCd) {
		this.ssvcLgroupCd = ssvcLgroupCd;
	}
	
	public String getSsvcSubClsCode() {
		return ssvcSubClsCode;
	}

	public void setSsvcSubClsCode(String ssvcSubClsCode) {
		this.ssvcSubClsCode = ssvcSubClsCode;
	}

	public String getSsvcLgroupCd() {
		return ssvcLgroupCd;
	}

	public void setSsvcLgroupNm(String ssvcLgroupNm) {
		this.ssvcLgroupNm = ssvcLgroupNm;
	}

	public String getSsvcLgroupNm() {
		return ssvcLgroupNm;
	}

	public void setSexSvcCd(String sexSvcCd) {
		this.sexSvcCd = sexSvcCd;
	}

	public String getSexSvcCd() {
		return sexSvcCd;
	}

	public void setSexSvcNm(String sexSvcNm) {
		this.sexSvcNm = sexSvcNm;
	}
	
	public String getSexLinkUseTypeCd() {
		return sexLinkUseTypeCd;
	}

	public void setSexLinkUseTypeCd(String sexLinkUseTypeCd) {
		this.sexLinkUseTypeCd = sexLinkUseTypeCd;
	}
	
	public String getSexLinkUseTypeNm() {
		return sexLinkUseTypeNm;
	}

	public void setSexLinkUseTypeNm(String sexLinkUseTypeNm) {
		this.sexLinkUseTypeNm = sexLinkUseTypeNm;
	}
	
	public String getSexSvcNm() {
		return sexSvcNm;
	}

	public void setSsvcUseTypeCd(String ssvcUseTypeCd) {
		this.ssvcUseTypeCd = ssvcUseTypeCd;
	}

	public String getSsvcUseTypeCd() {
		return ssvcUseTypeCd;
	}

	public void setSsvcUseTypeNm(String ssvcUseTypeNm) {
		this.ssvcUseTypeNm = ssvcUseTypeNm;
	}

	public String getSsvcUseTypeNm() {
		return ssvcUseTypeNm;
	}

	public void setSuseTypeCd(String suseTypeCd) {
		this.suseTypeCd = suseTypeCd;
	}

	public String getSuseTypeCd() {
		return suseTypeCd;
	}

	public void setSuseTypeNm(String suseTypeNm) {
		this.suseTypeNm = suseTypeNm;
	}

	public String getSuseTypeNm() {
		return suseTypeNm;
	}

	public void setScomment(String scomment) {
		this.scomment = scomment;
	}

	public String getScomment() {
		return scomment;
	}

	public int getCheckCount() {
		return checkCount;
	}

	public void setCheckCount(int checkCount) {
		this.checkCount = checkCount;
	}

	/** MEMBER METHOD DECLARATION END **/
}