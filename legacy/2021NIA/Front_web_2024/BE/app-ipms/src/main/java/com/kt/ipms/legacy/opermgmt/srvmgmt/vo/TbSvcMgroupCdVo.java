package com.kt.ipms.legacy.opermgmt.srvmgmt.vo;
import java.io.Serializable;
import com.kt.ipms.legacy.cmn.vo.CommonVo;


public class TbSvcMgroupCdVo extends CommonVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = -1869917898767184994L;

	private String ssvcHgroupCd;

	private String ssvcHgroupNm;

	private String ssvcMgroupCd;

	private String ssvcMgroupNm;

	private String ssvcMgroupNm1;

	private String sexClsCd;

	private String sexClsNm;

	private String sexLinkUseTypeCd;

	private String sexLinkUseTypeNm;

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

	public void setSsvcMgroupNm1(String ssvcMgroupNm1) {
		this.ssvcMgroupNm1 = ssvcMgroupNm1;
	}

	public String getSsvcMgroupNm1() {
		return ssvcMgroupNm1;
	}

	public void setSexClsCd(String sexClsCd) {
		this.sexClsCd = sexClsCd;
	}

	public String getSexClsCd() {
		return sexClsCd;
	}

	public void setSexClsNm(String sexClsNm) {
		this.sexClsNm = sexClsNm;
	}

	public String getSexClsNm() {
		return sexClsNm;
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

	/** MEMBER METHOD DECLARATION END **/
}