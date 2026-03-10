package com.kt.ipms.legacy.linkmgmt.common.vo;
import java.io.Serializable;
import com.kt.ipms.legacy.cmn.vo.CommonVo;
import java.math.BigInteger;


public class TbLnkSvcBasVo extends CommonVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = 1413123861623882761L;

	private BigInteger lnkSvcMstSeq;

	private String sifId;

	private String sifNm;

	private String sifProtocol;

	private String ssystemNm;

	private String smoduleNm;

	private String snetNm;

	private String saddress;

	private String sport;

	private String suri;

	private String sopstate;

	private String scomment;

	/** MEMBER VARIABLE DECLARATION END **/
	
	private String suriMethod;
	
	/** MEMBER METHOD DECLARATION START **/

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

	public void setSifNm(String sifNm) {
		this.sifNm = sifNm;
	}

	public String getSifNm() {
		return sifNm;
	}

	public void setSifProtocol(String sifProtocol) {
		this.sifProtocol = sifProtocol;
	}

	public String getSifProtocol() {
		return sifProtocol;
	}

	public void setSsystemNm(String ssystemNm) {
		this.ssystemNm = ssystemNm;
	}

	public String getSsystemNm() {
		return ssystemNm;
	}

	public void setSmoduleNm(String smoduleNm) {
		this.smoduleNm = smoduleNm;
	}

	public String getSmoduleNm() {
		return smoduleNm;
	}

	public void setSnetNm(String snetNm) {
		this.snetNm = snetNm;
	}

	public String getSnetNm() {
		return snetNm;
	}

	public void setSaddress(String saddress) {
		this.saddress = saddress;
	}

	public String getSaddress() {
		return saddress;
	}

	public void setSport(String sport) {
		this.sport = sport;
	}

	public String getSport() {
		return sport;
	}

	public void setSuri(String suri) {
		this.suri = suri;
	}

	public String getSuri() {
		return suri;
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

	public String getSuriMethod() {
		return suriMethod;
	}

	public void setSuriMethod(String suriMethod) {
		this.suriMethod = suriMethod;
	}

	/** MEMBER METHOD DECLARATION END **/
}