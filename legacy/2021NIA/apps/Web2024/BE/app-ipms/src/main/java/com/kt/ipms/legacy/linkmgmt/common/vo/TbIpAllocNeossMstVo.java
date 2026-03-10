package com.kt.ipms.legacy.linkmgmt.common.vo;
import java.io.Serializable;
import com.kt.ipms.legacy.cmn.vo.CommonVo;
import java.math.BigInteger;


public class TbIpAllocNeossMstVo extends CommonVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = -8328160088184438228L;

	private BigInteger nipAllocNeossSeq;

	private String sordernum;

	private String regyn;

	private String odrCloseTypeCd;

	private String odrCloseTypeNm;

	private String said;

	private String llnum;

	private String icisofficescode;

	private String lacpsid;

	private String subscnescode;

	private String subscmstip;

	private String subsclgipportseq;

	private String subsclgipportdescription;

	private String subsclgipportip;

	private String subscrouterserialip;

	private String subscnealias;

	private String connalias;

	private String modelname;

	private String ipVersionTypeCd;

	private String ipVersionTypeNm;

	private BigInteger nipAssignMstSeq;

	private String ipCreateTypeCd;

	private String ipCreateTypeNm;

	private String ipBlock;

	private Integer ipbitmask;

	private String sipaddr;

	private String eipaddr;

	private BigInteger ipmsSvcSeq;

	private String assignTypeCd;

	private String assignTypeNm;

	private String exSvcCd;

	private String exSvcNm;

	private String svcUseTypeCd;

	private String svcUseTypeNm;

	private String gatewayip;

	private String installaddress;

	private String installroadaddress;

	private String workerid;

	private String workername;

	private String odrregdt;

	private String odrhopedt;

	private String custname;

	private String sexPushYn;

	private BigInteger nticketNoSeq;

	private String pipPrefix;
	
	private String reportopinion;
	
	private String resultcd;
	
	private String resultmsg;

	private String scomment;
	
	/* User Define Start*/
	private String ssvcLineTypeCd;
	
	private Integer nResult;
	
	private String 	contactNum;
	
	private String  email;
	
	private String sspeeddesc;
	
	private String sofficecode;
	
	private String sipAddressSearchKey;
	
	private String slacpBlockYN;
	
	private Integer nlimit;
	
	
	/* User Define END*/

	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/

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

	public void setRegyn(String regyn) {
		this.regyn = regyn;
	}

	public String getRegyn() {
		return regyn;
	}

	public void setOdrCloseTypeCd(String odrCloseTypeCd) {
		this.odrCloseTypeCd = odrCloseTypeCd;
	}

	public String getOdrCloseTypeCd() {
		return odrCloseTypeCd;
	}

	public void setOdrCloseTypeNm(String odrCloseTypeNm) {
		this.odrCloseTypeNm = odrCloseTypeNm;
	}

	public String getOdrCloseTypeNm() {
		return odrCloseTypeNm;
	}

	public void setSaid(String said) {
		this.said = said;
	}

	public String getSaid() {
		return said;
	}

	public void setLlnum(String llnum) {
		this.llnum = llnum;
	}

	public String getLlnum() {
		return llnum;
	}

	public void setIcisofficescode(String icisofficescode) {
		this.icisofficescode = icisofficescode;
	}

	public String getIcisofficescode() {
		return icisofficescode;
	}

	public void setLacpsid(String lacpsid) {
		this.lacpsid = lacpsid;
	}

	public String getLacpsid() {
		return lacpsid;
	}

	public void setSubscnescode(String subscnescode) {
		this.subscnescode = subscnescode;
	}

	public String getSubscnescode() {
		return subscnescode;
	}

	public void setSubscmstip(String subscmstip) {
		this.subscmstip = subscmstip;
	}

	public String getSubscmstip() {
		return subscmstip;
	}

	public void setSubsclgipportseq(String subsclgipportseq) {
		this.subsclgipportseq = subsclgipportseq;
	}

	public String getSubsclgipportseq() {
		return subsclgipportseq;
	}

	public void setSubsclgipportdescription(String subsclgipportdescription) {
		this.subsclgipportdescription = subsclgipportdescription;
	}

	public String getSubsclgipportdescription() {
		return subsclgipportdescription;
	}

	public void setSubsclgipportip(String subsclgipportip) {
		this.subsclgipportip = subsclgipportip;
	}

	public String getSubsclgipportip() {
		return subsclgipportip;
	}

	public void setSubscrouterserialip(String subscrouterserialip) {
		this.subscrouterserialip = subscrouterserialip;
	}

	public String getSubscrouterserialip() {
		return subscrouterserialip;
	}

	public void setSubscnealias(String subscnealias) {
		this.subscnealias = subscnealias;
	}

	public String getSubscnealias() {
		return subscnealias;
	}

	public void setConnalias(String connalias) {
		this.connalias = connalias;
	}

	public String getConnalias() {
		return connalias;
	}

	public void setModelname(String modelname) {
		this.modelname = modelname;
	}

	public String getModelname() {
		return modelname;
	}

	public void setIpVersionTypeCd(String ipVersionTypeCd) {
		this.ipVersionTypeCd = ipVersionTypeCd;
	}

	public String getIpVersionTypeCd() {
		return ipVersionTypeCd;
	}

	public void setIpVersionTypeNm(String ipVersionTypeNm) {
		this.ipVersionTypeNm = ipVersionTypeNm;
	}

	public String getIpVersionTypeNm() {
		return ipVersionTypeNm;
	}

	public void setNipAssignMstSeq(BigInteger nipAssignMstSeq) {
		this.nipAssignMstSeq = nipAssignMstSeq;
	}

	public BigInteger getNipAssignMstSeq() {
		return nipAssignMstSeq;
	}

	public void setIpCreateTypeCd(String ipCreateTypeCd) {
		this.ipCreateTypeCd = ipCreateTypeCd;
	}

	public String getIpCreateTypeCd() {
		return ipCreateTypeCd;
	}

	public void setIpCreateTypeNm(String ipCreateTypeNm) {
		this.ipCreateTypeNm = ipCreateTypeNm;
	}

	public String getIpCreateTypeNm() {
		return ipCreateTypeNm;
	}

	public void setIpBlock(String ipBlock) {
		this.ipBlock = ipBlock;
	}

	public String getIpBlock() {
		return ipBlock;
	}

	public void setIpbitmask(Integer ipbitmask) {
		this.ipbitmask = ipbitmask;
	}

	public Integer getIpbitmask() {
		return ipbitmask;
	}

	public void setSipaddr(String sipaddr) {
		this.sipaddr = sipaddr;
	}

	public String getSipaddr() {
		return sipaddr;
	}

	public void setEipaddr(String eipaddr) {
		this.eipaddr = eipaddr;
	}

	public String getEipaddr() {
		return eipaddr;
	}

	public void setIpmsSvcSeq(BigInteger ipmsSvcSeq) {
		this.ipmsSvcSeq = ipmsSvcSeq;
	}

	public BigInteger getIpmsSvcSeq() {
		return ipmsSvcSeq;
	}

	public void setAssignTypeCd(String assignTypeCd) {
		this.assignTypeCd = assignTypeCd;
	}

	public String getAssignTypeCd() {
		return assignTypeCd;
	}

	public void setAssignTypeNm(String assignTypeNm) {
		this.assignTypeNm = assignTypeNm;
	}

	public String getAssignTypeNm() {
		return assignTypeNm;
	}

	public void setExSvcCd(String exSvcCd) {
		this.exSvcCd = exSvcCd;
	}

	public String getExSvcCd() {
		return exSvcCd;
	}

	public void setExSvcNm(String exSvcNm) {
		this.exSvcNm = exSvcNm;
	}

	public String getExSvcNm() {
		return exSvcNm;
	}

	public void setSvcUseTypeCd(String svcUseTypeCd) {
		this.svcUseTypeCd = svcUseTypeCd;
	}

	public String getSvcUseTypeCd() {
		return svcUseTypeCd;
	}

	public void setSvcUseTypeNm(String svcUseTypeNm) {
		this.svcUseTypeNm = svcUseTypeNm;
	}

	public String getSvcUseTypeNm() {
		return svcUseTypeNm;
	}

	public void setGatewayip(String gatewayip) {
		this.gatewayip = gatewayip;
	}

	public String getGatewayip() {
		return gatewayip;
	}

	public void setInstalladdress(String installaddress) {
		this.installaddress = installaddress;
	}

	public String getInstalladdress() {
		return installaddress;
	}

	public void setInstallroadaddress(String installroadaddress) {
		this.installroadaddress = installroadaddress;
	}

	public String getInstallroadaddress() {
		return installroadaddress;
	}

	public void setWorkerid(String workerid) {
		this.workerid = workerid;
	}

	public String getWorkerid() {
		return workerid;
	}

	public void setWorkername(String workername) {
		this.workername = workername;
	}

	public String getWorkername() {
		return workername;
	}

	public void setOdrregdt(String odrregdt) {
		this.odrregdt = odrregdt;
	}

	public String getOdrregdt() {
		return odrregdt;
	}

	public void setOdrhopedt(String odrhopedt) {
		this.odrhopedt = odrhopedt;
	}

	public String getOdrhopedt() {
		return odrhopedt;
	}

	public void setCustname(String custname) {
		this.custname = custname;
	}

	public String getCustname() {
		return custname;
	}

	public void setSexPushYn(String sexPushYn) {
		this.sexPushYn = sexPushYn;
	}

	public String getSexPushYn() {
		return sexPushYn;
	}

	public void setNticketNoSeq(BigInteger nticketNoSeq) {
		this.nticketNoSeq = nticketNoSeq;
	}

	public BigInteger getNticketNoSeq() {
		return nticketNoSeq;
	}

	public void setPipPrefix(String pipPrefix) {
		this.pipPrefix = pipPrefix;
	}	

	public String getPipPrefix() {
		return pipPrefix;
	}
	
	public void setreportopinion(String reportopinion) {
		this.reportopinion = reportopinion;
	}
	
	public String getreportopinion() {
		return reportopinion;
	}
	
	public void setresultcd(String resultcd) {
		this.resultcd = resultcd;
	}
	
	public String getresultcd() {
		return resultcd;
	}
		
	public void setresultmsg(String resultmsg) {
		this.resultmsg = resultmsg;
	}
	
	public String getresultmsg() {
		return resultmsg;
	}

	public void setScomment(String scomment) {
		this.scomment = scomment;
	}

	public String getScomment() {
		return scomment;
	}

	/* User Define Start*/
	public void setssvcLineTypeCd(String ssvcLineTypeCd) {
		this.ssvcLineTypeCd = ssvcLineTypeCd;
	}
	
	public String getssvcLineTypeCd() {
		return this.ssvcLineTypeCd;
	}
	
	public void setnResult(Integer nResult) {
		this.nResult = nResult;
	}
	
	public Integer getnResult() {
		return this.nResult;
	}

	public String getContactNum() {
		return contactNum;
	}

	public void setContactNum(String contactNum) {
		this.contactNum = contactNum;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSspeeddesc() {
		return sspeeddesc;
	}

	public void setSspeeddesc(String sspeeddesc) {
		this.sspeeddesc = sspeeddesc;
	}

	public String getSofficecode() {
		return sofficecode;
	}

	public void setSofficecode(String sofficecode) {
		this.sofficecode = sofficecode;
	}

	public String getSipAddressSearchKey() {
		return sipAddressSearchKey;
	}

	public void setSipAddressSearchKey(String sipAddressSearchKey) {
		this.sipAddressSearchKey = sipAddressSearchKey;
	}

	public String getSlacpBlockYN() {
		return slacpBlockYN;
	}

	public void setSlacpBlockYN(String slacpBlockYN) {
		this.slacpBlockYN = slacpBlockYN;
	}

	public Integer getNlimit() {
		return nlimit;
	}

	public void setNlimit(Integer nlimit) {
		this.nlimit = nlimit;
	}

	/* User Define END*/
	/** MEMBER METHOD DECLARATION END **/
}