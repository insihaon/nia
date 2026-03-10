package com.kt.ipms.legacy.opermgmt.srvmgmt.vo;
import java.io.Serializable;

import com.kt.ipms.legacy.cmn.vo.CommonVo;


public class TbSvcLgroupCdVo extends CommonVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = -2129780932185339716L;

	private String ssvcHgroupCd;

	private String ssvcHgroupNm;

	private String ssvcMgroupCd;

	private String ssvcMgroupNm;

	private String ssvcLgroupCd;

	private String ssvcLgroupNm;

	private String sexSvcCd;

	private String sexSvcNm;

	private String sexLinkUseTypeCd;

	private String sexLinkUseTypeNm;
	
	private String scomment;

	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/

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

	public void setSsvcLgroupCd(String ssvcLgroupCd) {
		this.ssvcLgroupCd = ssvcLgroupCd;
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

	public String getSexSvcNm() {
		return sexSvcNm;
	}

	public void setSexLinkUseTypeCd(String sexLinkUseTypeCd) {
		this.sexLinkUseTypeCd = sexLinkUseTypeCd;
	}

	public String getSexLinkUseTypeCd() {
		return sexLinkUseTypeCd;
	}

	public void setSexLinkUseTypeNm(String sexLinkUseTypeNm) {
		this.sexLinkUseTypeNm = sexLinkUseTypeNm;
	}

	public String getSexLinkUseTypeNm() {
		return sexLinkUseTypeNm;
	}

	public String getScomment() {
		return scomment;
	}

	public void setScomment(String scomment) {
		this.scomment = scomment;
	}

	/** MEMBER METHOD DECLARATION END **/
}