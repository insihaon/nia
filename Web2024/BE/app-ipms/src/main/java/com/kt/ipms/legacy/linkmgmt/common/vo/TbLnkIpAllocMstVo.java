package com.kt.ipms.legacy.linkmgmt.common.vo;
import java.io.Serializable;
import com.kt.ipms.legacy.cmn.vo.IpVo;
import java.math.BigInteger;


public class TbLnkIpAllocMstVo extends IpVo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7612463195857081617L;

	/** MEMBER VARIABLE DECLARATION START **/
	

	private String ssvcLineTypeCd;

	private String ssvcLineTypeNm;

	private BigInteger nipAllocMstSeq;

	private BigInteger nipAllocNeossSeq;

	private String sordernum;

	private String sregyn;

	private String sodrCloseTypeCd;

	private String sodrCloseTypeNm;

	private String ssaid;

	private String sllnum;

	private String sicisofficescode;

	private String slacpsid;

	private String ssubscnescode;

	private String ssubscmstip;

	private String ssubsclgipportseq;

	private String ssubsclgipportdescription;

	private String ssubsclgipportip;

	private String ssubscrouterserialip;

	private String ssubscnealias;

	private String sconnalias;

	private String smodelname;

	private String scustname;

	private String sipCreateTypeCd;

	private String sipCreateTypeNm;

	private BigInteger nipAssignMstSeq;

	private Integer nbitmask;

	private BigInteger nipmsSvcSeq;

	private String sassignTypeCd;

	private String sassignTypeNm;

	private String sexSvcCd;

	private String sexSvcNm;

	private String ssvcUseTypeCd;

	private String ssvcUseTypeNm;

	private String sresultCd;

	private String sresultNm;

	private String sresultMsg;

	private String sgatewayip;

	private String sinstalladdress;

	private String sinstallroadaddress;

	private String sexPushYn;

	private BigInteger nticketActSeq;

	private String scomment;
	
	/*테이블 추가 및 관련 값*/
	private String sreportOpinion;
	
	private String sodrhopedt;
	
	private String sodrregdt;
	
	private String sipmsSvcNm;
	
	private String sicisofficesname;
	
	private String sofficecode; //수용국코드 (2014.11.10)
	
	private String sofficename; //수용국명 (2014.11.10)

	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/

	public void setSsvcLineTypeCd(String ssvcLineTypeCd) {
		this.ssvcLineTypeCd = ssvcLineTypeCd;
	}

	public String getSsvcLineTypeCd() {
		return ssvcLineTypeCd;
	}

	public void setSsvcLineTypeNm(String ssvcLineTypeNm) {
		this.ssvcLineTypeNm = ssvcLineTypeNm;
	}

	public String getSsvcLineTypeNm() {
		return ssvcLineTypeNm;
	}

	public void setNipAllocMstSeq(BigInteger nipAllocMstSeq) {
		this.nipAllocMstSeq = nipAllocMstSeq;
	}

	public BigInteger getNipAllocMstSeq() {
		return nipAllocMstSeq;
	}

	public void setNipAllocNeossSeq(BigInteger nipAllocNeossSeq) {
		this.nipAllocNeossSeq = nipAllocNeossSeq;
	}

	public BigInteger getNipAllocNeossSeq() {
		return nipAllocNeossSeq;
	}

	public void setSordernum(String sordernum) {
		this.sordernum = sordernum;
	}

	public String getSordernum() {
		return sordernum;
	}

	public void setSregyn(String sregyn) {
		this.sregyn = sregyn;
	}

	public String getSregyn() {
		return sregyn;
	}

	public void setSodrCloseTypeCd(String sodrCloseTypeCd) {
		this.sodrCloseTypeCd = sodrCloseTypeCd;
	}

	public String getSodrCloseTypeCd() {
		return sodrCloseTypeCd;
	}

	public void setSodrCloseTypeNm(String sodrCloseTypeNm) {
		this.sodrCloseTypeNm = sodrCloseTypeNm;
	}

	public String getSodrCloseTypeNm() {
		return sodrCloseTypeNm;
	}

	public void setSsaid(String ssaid) {
		this.ssaid = ssaid;
	}

	public String getSsaid() {
		return ssaid;
	}

	public void setSllnum(String sllnum) {
		this.sllnum = sllnum;
	}

	public String getSllnum() {
		return sllnum;
	}

	public void setSicisofficescode(String sicisofficescode) {
		this.sicisofficescode = sicisofficescode;
	}

	public String getSicisofficescode() {
		return sicisofficescode;
	}

	public void setSlacpsid(String slacpsid) {
		this.slacpsid = slacpsid;
	}

	public String getSlacpsid() {
		return slacpsid;
	}

	public void setSsubscnescode(String ssubscnescode) {
		this.ssubscnescode = ssubscnescode;
	}

	public String getSsubscnescode() {
		return ssubscnescode;
	}

	public void setSsubscmstip(String ssubscmstip) {
		this.ssubscmstip = ssubscmstip;
	}

	public String getSsubscmstip() {
		return ssubscmstip;
	}

	public void setSsubsclgipportseq(String ssubsclgipportseq) {
		this.ssubsclgipportseq = ssubsclgipportseq;
	}

	public String getSsubsclgipportseq() {
		return ssubsclgipportseq;
	}

	public void setSsubsclgipportdescription(String ssubsclgipportdescription) {
		this.ssubsclgipportdescription = ssubsclgipportdescription;
	}

	public String getSsubsclgipportdescription() {
		return ssubsclgipportdescription;
	}

	public void setSsubsclgipportip(String ssubsclgipportip) {
		this.ssubsclgipportip = ssubsclgipportip;
	}

	public String getSsubsclgipportip() {
		return ssubsclgipportip;
	}

	public void setSsubscrouterserialip(String ssubscrouterserialip) {
		this.ssubscrouterserialip = ssubscrouterserialip;
	}

	public String getSsubscrouterserialip() {
		return ssubscrouterserialip;
	}

	public void setSsubscnealias(String ssubscnealias) {
		this.ssubscnealias = ssubscnealias;
	}

	public String getSsubscnealias() {
		return ssubscnealias;
	}

	public void setSconnalias(String sconnalias) {
		this.sconnalias = sconnalias;
	}

	public String getSconnalias() {
		return sconnalias;
	}

	public void setSmodelname(String smodelname) {
		this.smodelname = smodelname;
	}

	public String getSmodelname() {
		return smodelname;
	}

	public void setScustname(String scustname) {
		this.scustname = scustname;
	}

	public String getScustname() {
		return scustname;
	}

	public void setSipCreateTypeCd(String sipCreateTypeCd) {
		this.sipCreateTypeCd = sipCreateTypeCd;
	}

	public String getSipCreateTypeCd() {
		return sipCreateTypeCd;
	}

	public void setSipCreateTypeNm(String sipCreateTypeNm) {
		this.sipCreateTypeNm = sipCreateTypeNm;
	}

	public String getSipCreateTypeNm() {
		return sipCreateTypeNm;
	}

	public void setNipAssignMstSeq(BigInteger nipAssignMstSeq) {
		this.nipAssignMstSeq = nipAssignMstSeq;
	}

	public BigInteger getNipAssignMstSeq() {
		return nipAssignMstSeq;
	}

	public void setNbitmask(Integer nbitmask) {
		this.nbitmask = nbitmask;
	}

	public Integer getNbitmask() {
		return nbitmask;
	}

	public void setNipmsSvcSeq(BigInteger nipmsSvcSeq) {
		this.nipmsSvcSeq = nipmsSvcSeq;
	}

	public BigInteger getNipmsSvcSeq() {
		return nipmsSvcSeq;
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

	public void setSresultCd(String sresultCd) {
		this.sresultCd = sresultCd;
	}

	public String getSresultCd() {
		return sresultCd;
	}

	public void setSresultNm(String sresultNm) {
		this.sresultNm = sresultNm;
	}

	public String getSresultNm() {
		return sresultNm;
	}

	
	public void setSresultMsg(String sresultMsg) {
		this.sresultMsg = sresultMsg;
	}

	public String getSresultMsg() {
		return sresultMsg;
	}
	

	public void setSgatewayip(String sgatewayip) {
		this.sgatewayip = sgatewayip;
	}

	public String getSgatewayip() {
		return sgatewayip;
	}

	public void setSinstalladdress(String sinstalladdress) {
		this.sinstalladdress = sinstalladdress;
	}

	public String getSinstalladdress() {
		return sinstalladdress;
	}

	public void setSinstallroadaddress(String sinstallroadaddress) {
		this.sinstallroadaddress = sinstallroadaddress;
	}

	public String getSinstallroadaddress() {
		return sinstallroadaddress;
	}

	public void setSexPushYn(String sexPushYn) {
		this.sexPushYn = sexPushYn;
	}

	public String getSexPushYn() {
		return sexPushYn;
	}

	public void setNticketActSeq(BigInteger nticketActSeq) {
		this.nticketActSeq = nticketActSeq;
	}

	public BigInteger getNticketActSeq() {
		return nticketActSeq;
	}

	public void setScomment(String scomment) {
		this.scomment = scomment;
	}

	public String getScomment() {
		return scomment;
	}

	public String getSreportOpinion() {
		return sreportOpinion;
	}

	public void setSreportOpinion(String sreportOpinion) {
		this.sreportOpinion = sreportOpinion;
	}

	public String getSodrhopedt() {
		return sodrhopedt;
	}

	public void setSodrhopedt(String sodrhopedt) {
		this.sodrhopedt = sodrhopedt;
	}

	public String getSodrregdt() {
		return sodrregdt;
	}

	public void setSodrregdt(String sodrregdt) {
		this.sodrregdt = sodrregdt;
	}

	public String getSipmsSvcNm() {
		return sipmsSvcNm;
	}

	public void setSipmsSvcNm(String sipmsSvcNm) {
		this.sipmsSvcNm = sipmsSvcNm;
	}

	public String getSicisofficesname() {
		return sicisofficesname;
	}

	public void setSicisofficesname(String sicisofficesname) {
		this.sicisofficesname = sicisofficesname;
	}

	public String getSofficecode() {
		return sofficecode;
	}

	public void setSofficecode(String sofficecode) {
		this.sofficecode = sofficecode;
	}

	public String getSofficename() {
		return sofficename;
	}

	public void setSofficename(String sofficename) {
		this.sofficename = sofficename;
	}

	/** MEMBER METHOD DECLARATION END **/
}