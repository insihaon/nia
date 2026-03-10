package com.kt.ipms.legacy.linkmgmt.common.vo;
import java.io.Serializable;

import com.kt.ipms.legacy.cmn.vo.CommonVo;

import java.math.BigInteger;


public class TbBatchSvcBasVo extends CommonVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = -3847374313509600429L;

	private BigInteger batMstSeq;

	private BigInteger lnkSvcMstSeq;

	private String sifId;

	private String ssystemNm;

	private String stableNm;

	private String sscriptPath;

	private String sscriptNm;

	private String sinterlockPath;

	private String sinterlockNm;

	private String sproviderSys;

	private String speriod;

	private String batchSdt;

	private String linktype;

	private String sopstate;

	private String scomment;

	/** MEMBER VARIABLE DECLARATION END **/
	/** MEMBER METHOD DECLARATION START **/

	public void setBatMstSeq(BigInteger batMstSeq) {
		this.batMstSeq = batMstSeq;
	}

	public BigInteger getBatMstSeq() {
		return batMstSeq;
	}

	public void setLnkSvcMstSeq(BigInteger lnkSvcMstSeq) {
		this.lnkSvcMstSeq = lnkSvcMstSeq;
	}

	public BigInteger getLnkSvcMstSeq() {
		return lnkSvcMstSeq;
	}

	public void setSifId(String sifId) {
		this.sifId = sifId;
	}

	public String getSifId() {
		return sifId;
	}

	public void setSsystemNm(String ssystemNm) {
		this.ssystemNm = ssystemNm;
	}

	public String getSsystemNm() {
		return ssystemNm;
	}

	public void setStableNm(String stableNm) {
		this.stableNm = stableNm;
	}

	public String getStableNm() {
		return stableNm;
	}

	public void setSscriptPath(String sscriptPath) {
		this.sscriptPath = sscriptPath;
	}

	public String getSscriptPath() {
		return sscriptPath;
	}

	public void setSscriptNm(String sscriptNm) {
		this.sscriptNm = sscriptNm;
	}

	public String getSscriptNm() {
		return sscriptNm;
	}

	public void setSinterlockPath(String sinterlockPath) {
		this.sinterlockPath = sinterlockPath;
	}

	public String getSinterlockPath() {
		return sinterlockPath;
	}

	public void setSinterlockNm(String sinterlockNm) {
		this.sinterlockNm = sinterlockNm;
	}

	public String getSinterlockNm() {
		return sinterlockNm;
	}

	public void setSproviderSys(String sproviderSys) {
		this.sproviderSys = sproviderSys;
	}

	public String getSproviderSys() {
		return sproviderSys;
	}

	public void setSperiod(String speriod) {
		this.speriod = speriod;
	}

	public String getSperiod() {
		return speriod;
	}

	public void setBatchSdt(String batchSdt) {
		this.batchSdt = batchSdt;
	}

	public String getBatchSdt() {
		return batchSdt;
	}

	public void setLinktype(String linktype) {
		this.linktype = linktype;
	}

	public String getLinktype() {
		return linktype;
	}

	public void setSopstate(String sopstate) {
		this.sopstate = sopstate;
	}

	public String getSopstate() {
		return sopstate;
	}

	public void setScomment(String scomment) {
		this.scomment = scomment;
	}

	public String getScomment() {
		return scomment;
	}

	/** MEMBER METHOD DECLARATION END **/
}