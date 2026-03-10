package com.kt.ipms.legacy.opermgmt.whoismgmt.vo;
import java.io.Serializable;

import com.kt.ipms.legacy.cmn.vo.CommonVo;
import com.kt.ipms.legacy.linkmgmt.whois.model.WhoisInfoObj;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlMstListVo;

import java.util.Date;
import java.util.List;
import java.math.BigInteger;


public class TbWhoisVo extends CommonVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = -1721157618368689729L;
	
	private List<TbWhoisVo> tbWhoisVos;
	
	private int totalCount;
	
	private BigInteger nwhoisSeq;

	private String ssvcLineTypeCd;

	private String ssvcLineTypeNm;

	private BigInteger nlvlMstSeq;

	private String sassignTypeCd;

	private String sassignTypeNm;

	private String swhoisrequestid;

	private String sapplicationname;

	private String pipPrefix;

	private String sfirstAddr;

	private String slastAddr;

	private BigInteger nfirstAddr;

	private BigInteger nlastAddr;

	private BigInteger ncnt;

	private String srequestCd;

	private String srequestNm;

	private String swhoisresultCd;

	private String swhoisresultNm;

	private String swhoisresultMsg;

	private String skrnicmemberId;

	private String sispNm;

	private String ssaid;

	private String sllnm;

	private String snetNm;

	private String sscreteYn;

	private String suserorggb;

	private String snettype;

	private String sservicegb;

	private String ssvcloc;

	private String shostnow;

	private String susedtlnow;

	private String shost6month;

	private String susedtl6month;

	private String shost12month;

	private String susedtl12month;

	private String ssvccommnet;

	private String sremark;

	private Date dsendDt;

	private Date dreceiptDt;

	private Date dreceiptokDt;

	private Date dreplyDt;

	private Date dregokDt;

	private String skrnicno;

	private String skrnicorgcode;

	private String soldNet;

	private String soldTotSubnet;

	private String soldTotHost;

	private String soldComment;

	private String snetPlan;

	private String stotSubnet;

	private String stotHost;

	private String ssvcnetwork;

	private String scity;

	private String scclassip;
	
	private String siptype;
	
	/* WHOIS USER DATA */
	private String sorgname;
	
	/* NODE DATAS*/
	private String ssvcGroupCd;
	
	private String ssvcGroupNm;
	
	private String ssvcObjCd;
	
	private String ssvcObjNm;
	
	/* Transfer Code Desc */
	private String swhoisTranStatusNm;
	
	/* Request Code Desc */
	private String swhoisRequestTypeNm;
	
	private TbLvlMstListVo lvlMstSeqListVo;
	
	private String delyn;		//삭제여부

	private List<BigInteger> delList;		// 삭제 seq
	
	private List<BigInteger> matchList;		// match seq
	
	private String stransKind;			// Transaction 구분 (시스템입력/사용자입력/담당자입력)
	
	private String nodeNm;

	private String smodifyDt;
	
	private String ssearchIp;
	
	private String type;
	
	private String dbMatchYn;
	
	private String loadSearchYn;
	

	private String sTargetYn; // 
	
	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/

	public void setNwhoisSeq(BigInteger nwhoisSeq) {
		this.nwhoisSeq = nwhoisSeq;
	}

	public BigInteger getNwhoisSeq() {
		return nwhoisSeq;
	}

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

	public void setNlvlMstSeq(BigInteger nlvlMstSeq) {
		this.nlvlMstSeq = nlvlMstSeq;
	}

	public BigInteger getNlvlMstSeq() {
		return nlvlMstSeq;
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

	public void setSwhoisrequestid(String swhoisrequestid) {
		this.swhoisrequestid = swhoisrequestid;
	}

	public String getSwhoisrequestid() {
		return swhoisrequestid;
	}

	public void setSapplicationname(String sapplicationname) {
		this.sapplicationname = sapplicationname;
	}

	public String getSapplicationname() {
		return sapplicationname;
	}

	public void setPipPrefix(String pipPrefix) {
		this.pipPrefix = pipPrefix;
	}

	public String getPipPrefix() {
		return pipPrefix;
	}

	public void setSfirstAddr(String sfirstAddr) {
		this.sfirstAddr = sfirstAddr;
	}

	public String getSfirstAddr() {
		return sfirstAddr;
	}

	public void setSlastAddr(String slastAddr) {
		this.slastAddr = slastAddr;
	}

	public String getSlastAddr() {
		return slastAddr;
	}

	public void setNfirstAddr(BigInteger nfirstAddr) {
		this.nfirstAddr = nfirstAddr;
	}

	public BigInteger getNfirstAddr() {
		return nfirstAddr;
	}

	public void setNlastAddr(BigInteger nlastAddr) {
		this.nlastAddr = nlastAddr;
	}

	public BigInteger getNlastAddr() {
		return nlastAddr;
	}

	public void setNcnt(BigInteger ncnt) {
		this.ncnt = ncnt;
	}

	public BigInteger getNcnt() {
		return ncnt;
	}

	public void setSrequestCd(String srequestCd) {
		this.srequestCd = srequestCd;
	}

	public String getSrequestCd() {
		return srequestCd;
	}

	public void setSrequestNm(String srequestNm) {
		this.srequestNm = srequestNm;
	}

	public String getSrequestNm() {
		return srequestNm;
	}

	public void setSwhoisresultCd(String swhoisresultCd) {
		this.swhoisresultCd = swhoisresultCd;
	}

	public String getSwhoisresultCd() {
		return swhoisresultCd;
	}

	public void setSwhoisresultNm(String swhoisresultNm) {
		this.swhoisresultNm = swhoisresultNm;
	}

	public String getSwhoisresultNm() {
		return swhoisresultNm;
	}

	public void setSwhoisresultMsg(String swhoisresultMsg) {
		this.swhoisresultMsg = swhoisresultMsg;
	}

	public String getSwhoisresultMsg() {
		return swhoisresultMsg;
	}

	public void setSkrnicmemberId(String skrnicmemberId) {
		this.skrnicmemberId = skrnicmemberId;
	}

	public String getSkrnicmemberId() {
		return skrnicmemberId;
	}

	public void setSispNm(String sispNm) {
		this.sispNm = sispNm;
	}

	public String getSispNm() {
		return sispNm;
	}

	public void setSsaid(String ssaid) {
		this.ssaid = ssaid;
	}

	public String getSsaid() {
		return ssaid;
	}

	public void setSllnm(String sllnm) {
		this.sllnm = sllnm;
	}

	public String getSllnm() {
		return sllnm;
	}

	public void setSnetNm(String snetNm) {
		this.snetNm = snetNm;
	}

	public String getSnetNm() {
		return snetNm;
	}

	public void setSscreteYn(String sscreteYn) {
		this.sscreteYn = sscreteYn;
	}

	public String getSscreteYn() {
		return sscreteYn;
	}

	public void setSuserorggb(String suserorggb) {
		this.suserorggb = suserorggb;
	}

	public String getSuserorggb() {
		return suserorggb;
	}

	public void setSnettype(String snettype) {
		this.snettype = snettype;
	}

	public String getSnettype() {
		return snettype;
	}

	public void setSservicegb(String sservicegb) {
		this.sservicegb = sservicegb;
	}

	public String getSservicegb() {
		return sservicegb;
	}

	public void setSsvcloc(String ssvcloc) {
		this.ssvcloc = ssvcloc;
	}

	public String getSsvcloc() {
		return ssvcloc;
	}

	public void setShostnow(String shostnow) {
		this.shostnow = shostnow;
	}

	public String getShostnow() {
		return shostnow;
	}

	public void setSusedtlnow(String susedtlnow) {
		this.susedtlnow = susedtlnow;
	}

	public String getSusedtlnow() {
		return susedtlnow;
	}

	public void setShost6month(String shost6month) {
		this.shost6month = shost6month;
	}

	public String getShost6month() {
		return shost6month;
	}

	public void setSusedtl6month(String susedtl6month) {
		this.susedtl6month = susedtl6month;
	}

	public String getSusedtl6month() {
		return susedtl6month;
	}

	public void setShost12month(String shost12month) {
		this.shost12month = shost12month;
	}

	public String getShost12month() {
		return shost12month;
	}

	public void setSusedtl12month(String susedtl12month) {
		this.susedtl12month = susedtl12month;
	}

	public String getSusedtl12month() {
		return susedtl12month;
	}

	public void setSsvccommnet(String ssvccommnet) {
		this.ssvccommnet = ssvccommnet;
	}

	public String getSsvccommnet() {
		return ssvccommnet;
	}

	public void setSremark(String sremark) {
		this.sremark = sremark;
	}

	public String getSremark() {
		return sremark;
	}

	public void setDsendDt(Date dsendDt) {
		this.dsendDt = dsendDt;
	}

	public Date getDsendDt() {
		return dsendDt;
	}

	public void setDreceiptDt(Date dreceiptDt) {
		this.dreceiptDt = dreceiptDt;
	}

	public Date getDreceiptDt() {
		return dreceiptDt;
	}

	public void setDreceiptokDt(Date dreceiptokDt) {
		this.dreceiptokDt = dreceiptokDt;
	}

	public Date getDreceiptokDt() {
		return dreceiptokDt;
	}

	public void setDreplyDt(Date dreplyDt) {
		this.dreplyDt = dreplyDt;
	}

	public Date getDreplyDt() {
		return dreplyDt;
	}

	public void setDregokDt(Date dregokDt) {
		this.dregokDt = dregokDt;
	}

	public Date getDregokDt() {
		return dregokDt;
	}

	public void setSkrnicno(String skrnicno) {
		this.skrnicno = skrnicno;
	}

	public String getSkrnicno() {
		return skrnicno;
	}

	public void setSkrnicorgcode(String skrnicorgcode) {
		this.skrnicorgcode = skrnicorgcode;
	}

	public String getSkrnicorgcode() {
		return skrnicorgcode;
	}

	public void setSoldNet(String soldNet) {
		this.soldNet = soldNet;
	}

	public String getSoldNet() {
		return soldNet;
	}

	public void setSoldTotSubnet(String soldTotSubnet) {
		this.soldTotSubnet = soldTotSubnet;
	}

	public String getSoldTotSubnet() {
		return soldTotSubnet;
	}

	public void setSoldTotHost(String soldTotHost) {
		this.soldTotHost = soldTotHost;
	}

	public String getSoldTotHost() {
		return soldTotHost;
	}

	public void setSoldComment(String soldComment) {
		this.soldComment = soldComment;
	}

	public String getSoldComment() {
		return soldComment;
	}

	public void setSnetPlan(String snetPlan) {
		this.snetPlan = snetPlan;
	}

	public String getSnetPlan() {
		return snetPlan;
	}

	public void setStotSubnet(String stotSubnet) {
		this.stotSubnet = stotSubnet;
	}

	public String getStotSubnet() {
		return stotSubnet;
	}

	public void setStotHost(String stotHost) {
		this.stotHost = stotHost;
	}

	public String getStotHost() {
		return stotHost;
	}

	public void setSsvcnetwork(String ssvcnetwork) {
		this.ssvcnetwork = ssvcnetwork;
	}

	public String getSsvcnetwork() {
		return ssvcnetwork;
	}

	public void setScity(String scity) {
		this.scity = scity;
	}

	public String getScity() {
		return scity;
	}

	public void setScclassip(String scclassip) {
		this.scclassip = scclassip;
	}

	public String getScclassip() {
		return scclassip;
	}

	public String getSorgname() {
		return sorgname;
	}

	public void setSorgname(String sorgname) {
		this.sorgname = sorgname;
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

	public String getSwhoisTranStatusNm() {
		return swhoisTranStatusNm;
	}

	public void setSwhoisTranStatusNm(String swhoisTranStatusNm) {
		this.swhoisTranStatusNm = swhoisTranStatusNm;
	}

	public String getSwhoisRequestTypeNm() {
		return swhoisRequestTypeNm;
	}

	public void setSwhoisRequestTypeNm(String swhoisRequestTypeNm) {
		this.swhoisRequestTypeNm = swhoisRequestTypeNm;
	}

	public TbLvlMstListVo getLvlMstSeqListVo() {
		return lvlMstSeqListVo;
	}

	public void setLvlMstSeqListVo(TbLvlMstListVo lvlMstSeqListVo) {
		this.lvlMstSeqListVo = lvlMstSeqListVo;
	}


	public String getSiptype() {
		return siptype;
	}

	public void setSiptype(String siptype) {
		this.siptype = siptype;
	}

	public String getDelyn() {
		return delyn;
	}

	public void setDelyn(String delyn) {
		this.delyn = delyn;
	}

	public List<BigInteger> getDelList() {
		return delList;
	}

	public void setDelList(List<BigInteger> delList) {
		this.delList = delList;
	}
	/** MEMBER METHOD DECLARATION END **/

	public String getStransKind() {
		return stransKind;
	}

	public void setStransKind(String stransKind) {
		this.stransKind = stransKind;
	}

	public List<TbWhoisVo> getTbWhoisVos() {
		return tbWhoisVos;
	}

	public void setTbWhoisVos(List<TbWhoisVo> tbWhoisVos) {
		this.tbWhoisVos = tbWhoisVos;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public String getNodeNm() {
		return nodeNm;
	}

	public void setNodeNm(String nodeNm) {
		this.nodeNm = nodeNm;
	}

	public String getSmodifyDt() {
		return smodifyDt;
	}

	public void setSmodifyDt(String smodifyDt) {
		this.smodifyDt = smodifyDt;
	}

	public String getSsearchIp() {
		return ssearchIp;
	}

	public void setSsearchIp(String ssearchIp) {
		this.ssearchIp = ssearchIp;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDbMatchYn() {
		return dbMatchYn;
	}

	public void setDbMatchYn(String dbMatchYn) {
		this.dbMatchYn = dbMatchYn;
	}

	public List<BigInteger> getMatchList() {
		return matchList;
	}

	public void setMatchList(List<BigInteger> matchList) {
		this.matchList = matchList;
	}

	public String getsTargetYn() {
		return sTargetYn;
	}

	public void setsTargetYn(String sTargetYn) {
		this.sTargetYn = sTargetYn;
	}

	public String getLoadSearchYn() {
		return loadSearchYn;
	}

	public void setLoadSearchYn(String loadSearchYn) {
		this.loadSearchYn = loadSearchYn;
	}

}