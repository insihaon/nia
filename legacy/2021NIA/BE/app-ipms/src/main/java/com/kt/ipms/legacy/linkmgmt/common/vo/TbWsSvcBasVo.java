package com.kt.ipms.legacy.linkmgmt.common.vo;
import java.io.Serializable;
import com.kt.ipms.legacy.cmn.vo.CommonVo;
import java.math.BigInteger;


public class TbWsSvcBasVo extends CommonVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = 6756483607648553477L;

	private BigInteger wsMstSeq;

	private BigInteger lnkSvcMstSeq;

	private String sifId;

	private String ssystemNm;

	private String suriMethod;

	private String sproviderSys;

	private String linktype;

	private String sopstate;

	private String scomment;

	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/

	public void setWsMstSeq(BigInteger wsMstSeq) {
		this.wsMstSeq = wsMstSeq;
	}

	public BigInteger getWsMstSeq() {
		return wsMstSeq;
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

	public void setSuriMethod(String suriMethod) {
		this.suriMethod = suriMethod;
	}

	public String getSuriMethod() {
		return suriMethod;
	}

	public void setSproviderSys(String sproviderSys) {
		this.sproviderSys = sproviderSys;
	}

	public String getSproviderSys() {
		return sproviderSys;
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