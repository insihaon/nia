package com.kt.ipms.legacy.ipmgmt.historymgmt.vo;

import java.io.Serializable;
import java.math.BigInteger;

import com.kt.ipms.legacy.cmn.vo.IpVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlMstListVo;

public class IpHistoryMstVo extends IpVo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String tableNm;
	
	private int yyyy;
	
	private BigInteger nipHistMstSeq;
	
	private String sworkSystem;
	
	private String smenuId;
	
	private String smenuNm;
	
	private String sMenuHistCd;
	
	private BigInteger nlvlMstSeq;
	
	private BigInteger nipHistTaskCd;
	
	private String sipHistTaskNm;
	
	private BigInteger nipBlockMstSeq;		// IP_블럭MST_Seq
	
	private BigInteger nipAssignMstSeq;	// IP_배정MST_Seq
	
	private BigInteger nipAllocMstSeq;
	
	private String ssvcLineTypeCd;  // 서비스망 유형 코드

	private String ssvcLineTypeNm;  // 서비스망 유형 명
	
	private String ssvcGroupCd;      // 본부 코드
	
	private String ssvcGroupNm;		// 본부 명
	
	private String ssvcObjCd;			// 국사 코드
	
	private String ssvcObjNm;			// 국사 명	
	
	private String sassignLevelCd;			// IP_관리_단계_레벨_CD

	private String sassignLevelNm;			// IP_관리_단계_레벨_명
	
	private String sassignTypeCd;			// FN 참조 IPMS 서비스 유형 타입

	private String sassignTypeNm;
	
	private String sipAllocExTypeCd;		// FN 할당 연동유형

	private String sipAllocExTypeNm;
	
	private BigInteger nipmsSvcSeq;		// FN 상품MST --> 대상상품 Search
	
	private String sipmsSvcNm;
	
	private String sexSvcCd;					// FN NeOSS 상품코드 세부 OPTION

	private String ssvcUseTypeCd;
	
	private String ssvcUseTypeNm; // 사업용여부 
	
	private String sipCreateTypeCd;			// FN 생성유형 : 공인/사설/Bogon

	private String sipCreateTypeNm;
	
	private String sipVersionTypeCd;
	
	private BigInteger nipAllocNeossSeq;  // NeoSS_오더_확정_Seq
	
	private String sordernum;			// NeOSS 오더번호

	private String sregyn;		// 확정/회수 구분 Y:할당확정, O:할당예약
	
	private String sodrCloseTypeCd;	// 시설작업구분
	
	private String ssaid;		// 서비스계약번호
	
	private String sllnum;	//전용회선번호

	private String sicisofficescode;		// 수용국코드

	private String slacpsid;		// LACP 연관 SCN

	private String ssubscnescode;		// 시설표준코드

	private String ssubscmstip;		// 장비대표IP

	private String ssubsclgipportseq;	// 인터페이스SEQ

	private String ssubsclgipportdescription;	//인터페이스

	private String ssubsclgipportip;		// 국측스위치시리얼IP

	private String ssubscrouterserialip;		// 가입자측스위치시리얼IP

	private String ssubscnealias;	//장비별칭명

	private String sconnalias;		// 수용회선명

	private String smodelname;

	private String scustname;
	
	private String scomment;					// 비고
	
	private TbLvlMstListVo lvlMstSeqListVo;
	
	private int totalCount;
	
	private String LlSrchTypeCd;
	
	private String sofficecode;
	
	private String sicisofficescodeNe;
	
	private String ssubscnealiasNe;
	
	private String smodelnameNe;
	
	private String ssubscmstipNe;
	
	private String ssubscnnescode;
	
	private String sgatewayip;

	private String sinstalladdress;

	private String sinstallroadaddress;

	private String sexPushYn;

	private String sodrhopedt;
	
	private String sodrregdt;
	
	private String sofficename;
	
	private BigInteger nipLinkMstSeq;		// 링크마스터SEQ
	
	private String sicisofficesname;
	
	private String sssvcMgroupNm;
	
	private String ssvcLgroupNm;
	
	private Integer npriority;
	
	private String svalidCheck;

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

	public void setNipBlockMstSeq(BigInteger nipBlockMstSeq) {
		this.nipBlockMstSeq = nipBlockMstSeq;
	}

	public BigInteger getNipBlockMstSeq() {
		return nipBlockMstSeq;
	}

	public void setNipAssignMstSeq(BigInteger nipAssignMstSeq) {
		this.nipAssignMstSeq = nipAssignMstSeq;
	}

	public BigInteger getNipAssignMstSeq() {
		return nipAssignMstSeq;
	}

	public void setSassignLevelCd(String sassignLevelCd) {
		this.sassignLevelCd = sassignLevelCd;
	}

	public String getSassignLevelCd() {
		return sassignLevelCd;
	}

	public void setSassignLevelNm(String sassignLevelNm) {
		this.sassignLevelNm = sassignLevelNm;
	}

	public String getSassignLevelNm() {
		return sassignLevelNm;
	}

	public void setScomment(String scomment) {
		this.scomment = scomment;
	}

	public String getScomment() {
		return scomment;
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

	public void setSipAllocExTypeCd(String sipAllocExTypeCd) {
		this.sipAllocExTypeCd = sipAllocExTypeCd;
	}

	public String getSipAllocExTypeCd() {
		return sipAllocExTypeCd;
	}

	public void setSipAllocExTypeNm(String sipAllocExTypeNm) {
		this.sipAllocExTypeNm = sipAllocExTypeNm;
	}

	public String getSipAllocExTypeNm() {
		return sipAllocExTypeNm;
	}

	public TbLvlMstListVo getLvlMstSeqListVo() {
		return lvlMstSeqListVo;
	}

	public void setLvlMstSeqListVo(TbLvlMstListVo lvlMstSeqListVo) {
		this.lvlMstSeqListVo = lvlMstSeqListVo;
	}
	
	public String getSllnum() {
		return sllnum;
	}

	public void setSllnum(String sllnum) {
		this.sllnum = sllnum;
	}

	public String getSsubscnealias() {
		return ssubscnealias;
	}

	public void setSsubscnealias(String ssubscnealias) {
		this.ssubscnealias = ssubscnealias;
	}

	public String getSsubsclgipportdescription() {
		return ssubsclgipportdescription;
	}

	public void setSsubsclgipportdescription(String ssubsclgipportdescription) {
		this.ssubsclgipportdescription = ssubsclgipportdescription;
	}

	public String getSsvcUseTypeNm() {
		return ssvcUseTypeNm;
	}

	public void setSsvcUseTypeNm(String ssvcUseTypeNm) {
		this.ssvcUseTypeNm = ssvcUseTypeNm;
	}

	public BigInteger getNipAllocMstSeq() {
		return nipAllocMstSeq;
	}

	public void setNipAllocMstSeq(BigInteger nipAllocMstSeq) {
		this.nipAllocMstSeq = nipAllocMstSeq;
	}

	public String getSsvcUseTypeCd() {
		return ssvcUseTypeCd;
	}

	public void setSsvcUseTypeCd(String ssvcUseTypeCd) {
		this.ssvcUseTypeCd = ssvcUseTypeCd;
	}

	public String getSipmsSvcNm() {
		return sipmsSvcNm;
	}

	public void setSipmsSvcNm(String sipmsSvcNm) {
		this.sipmsSvcNm = sipmsSvcNm;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getYyyy() {
		return yyyy;
	}

	public void setYyyy(int yyyy) {
		this.yyyy = yyyy;
	}

	public BigInteger getNipHistMstSeq() {
		return nipHistMstSeq;
	}

	public void setNipHistMstSeq(BigInteger nipHistMstSeq) {
		this.nipHistMstSeq = nipHistMstSeq;
	}

	public String getSipVersionTypeCd() {
		return sipVersionTypeCd;
	}
	
	public void setSipVersionTypeCd(String sipVersionTypeCd) {
		this.sipVersionTypeCd = sipVersionTypeCd;
	}

	public String getTableNm() {
		return tableNm;
	}

	public void setTableNm(String tableNm) {
		this.tableNm = tableNm;
	}

	public String getSipHistTaskNm() {
		return sipHistTaskNm;
	}

	public void setSipHistTaskNm(String sipHistTaskNm) {
		this.sipHistTaskNm = sipHistTaskNm;
	}

	public String getLlSrchTypeCd() {
		return LlSrchTypeCd;
	}

	public void setLlSrchTypeCd(String llSrchTypeCd) {
		LlSrchTypeCd = llSrchTypeCd;
	}

	public String getSofficecode() {
		return sofficecode;
	}

	public void setSofficecode(String sofficecode) {
		this.sofficecode = sofficecode;
	}

	public String getSicisofficescode() {
		return sicisofficescode;
	}

	public void setSicisofficescode(String sicisofficescode) {
		this.sicisofficescode = sicisofficescode;
	}

	public String getSicisofficescodeNe() {
		return sicisofficescodeNe;
	}

	public void setSicisofficescodeNe(String sicisofficescodeNe) {
		this.sicisofficescodeNe = sicisofficescodeNe;
	}

	public String getSsubscnealiasNe() {
		return ssubscnealiasNe;
	}

	public void setSsubscnealiasNe(String ssubscnealiasNe) {
		this.ssubscnealiasNe = ssubscnealiasNe;
	}

	public String getSmodelnameNe() {
		return smodelnameNe;
	}

	public void setSmodelnameNe(String smodelnameNe) {
		this.smodelnameNe = smodelnameNe;
	}

	public String getSsubscmstipNe() {
		return ssubscmstipNe;
	}

	public void setSsubscmstipNe(String ssubscmstipNe) {
		this.ssubscmstipNe = ssubscmstipNe;
	}

	public String getSsaid() {
		return ssaid;
	}

	public void setSsaid(String ssaid) {
		this.ssaid = ssaid;
	}

	public String getSordernum() {
		return sordernum;
	}

	public void setSordernum(String sordernum) {
		this.sordernum = sordernum;
	}

	public String getSsubscnnescode() {
		return ssubscnnescode;
	}

	public void setSsubscnnescode(String ssubscnnescode) {
		this.ssubscnnescode = ssubscnnescode;
	}

	public BigInteger getNipHistTaskCd() {
		return nipHistTaskCd;
	}

	public void setNipHistTaskCd(BigInteger nipHistTaskCd) {
		this.nipHistTaskCd = nipHistTaskCd;
	}

	public String getSmenuId() {
		return smenuId;
	}

	public void setSmenuId(String smenuId) {
		this.smenuId = smenuId;
	}

	public String getSmenuNm() {
		return smenuNm;
	}

	public void setSmenuNm(String smenuNm) {
		this.smenuNm = smenuNm;
	}

	public BigInteger getNipAllocNeossSeq() {
		return nipAllocNeossSeq;
	}

	public void setNipAllocNeossSeq(BigInteger nipAllocNeossSeq) {
		this.nipAllocNeossSeq = nipAllocNeossSeq;
	}

	public String getSregyn() {
		return sregyn;
	}

	public void setSregyn(String sregyn) {
		this.sregyn = sregyn;
	}

	public String getSodrCloseTypeCd() {
		return sodrCloseTypeCd;
	}

	public void setSodrCloseTypeCd(String sodrCloseTypeCd) {
		this.sodrCloseTypeCd = sodrCloseTypeCd;
	}

	public String getSlacpsid() {
		return slacpsid;
	}

	public void setSlacpsid(String slacpsid) {
		this.slacpsid = slacpsid;
	}

	public String getSsubscnescode() {
		return ssubscnescode;
	}

	public void setSsubscnescode(String ssubscnescode) {
		this.ssubscnescode = ssubscnescode;
	}

	public String getSsubscmstip() {
		return ssubscmstip;
	}

	public void setSsubscmstip(String ssubscmstip) {
		this.ssubscmstip = ssubscmstip;
	}

	public String getSsubsclgipportseq() {
		return ssubsclgipportseq;
	}

	public void setSsubsclgipportseq(String ssubsclgipportseq) {
		this.ssubsclgipportseq = ssubsclgipportseq;
	}

	public String getSsubsclgipportip() {
		return ssubsclgipportip;
	}

	public void setSsubsclgipportip(String ssubsclgipportip) {
		this.ssubsclgipportip = ssubsclgipportip;
	}

	public String getSsubscrouterserialip() {
		return ssubscrouterserialip;
	}

	public void setSsubscrouterserialip(String ssubscrouterserialip) {
		this.ssubscrouterserialip = ssubscrouterserialip;
	}

	public String getSconnalias() {
		return sconnalias;
	}

	public void setSconnalias(String sconnalias) {
		this.sconnalias = sconnalias;
	}

	public String getSmodelname() {
		return smodelname;
	}

	public void setSmodelname(String smodelname) {
		this.smodelname = smodelname;
	}

	public String getScustname() {
		return scustname;
	}

	public void setScustname(String scustname) {
		this.scustname = scustname;
	}

	public BigInteger getNlvlMstSeq() {
		return nlvlMstSeq;
	}

	public void setNlvlMstSeq(BigInteger nlvlMstSeq) {
		this.nlvlMstSeq = nlvlMstSeq;
	}

	public String getsMenuHistCd() {
		return sMenuHistCd;
	}

	public void setsMenuHistCd(String sMenuHistCd) {
		this.sMenuHistCd = sMenuHistCd;
	}

	public String getSworkSystem() {
		return sworkSystem;
	}

	public void setSworkSystem(String sworkSystem) {
		this.sworkSystem = sworkSystem;
	}

	public String getSgatewayip() {
		return sgatewayip;
	}

	public void setSgatewayip(String sgatewayip) {
		this.sgatewayip = sgatewayip;
	}

	public String getSinstalladdress() {
		return sinstalladdress;
	}

	public void setSinstalladdress(String sinstalladdress) {
		this.sinstalladdress = sinstalladdress;
	}

	public String getSinstallroadaddress() {
		return sinstallroadaddress;
	}

	public void setSinstallroadaddress(String sinstallroadaddress) {
		this.sinstallroadaddress = sinstallroadaddress;
	}

	public String getSexPushYn() {
		return sexPushYn;
	}

	public void setSexPushYn(String sexPushYn) {
		this.sexPushYn = sexPushYn;
	}

	public String getSodrhopedt() {
		return sodrhopedt;
	}

	public void setSodrhopedt(String sodrhopedt) {
		this.sodrhopedt = sodrhopedt;
	}

	public String getSofficename() {
		return sofficename;
	}

	public void setSofficename(String sofficename) {
		this.sofficename = sofficename;
	}

	public String getSodrregdt() {
		return sodrregdt;
	}

	public void setSodrregdt(String sodrregdt) {
		this.sodrregdt = sodrregdt;
	}

	public BigInteger getNipLinkMstSeq() {
		return nipLinkMstSeq;
	}

	public void setNipLinkMstSeq(BigInteger nipLinkMstSeq) {
		this.nipLinkMstSeq = nipLinkMstSeq;
	}

	public String getSicisofficesname() {
		return sicisofficesname;
	}

	public void setSicisofficesname(String sicisofficesname) {
		this.sicisofficesname = sicisofficesname;
	}

	public String getSssvcMgroupNm() {
		return sssvcMgroupNm;
	}

	public void setSssvcMgroupNm(String sssvcMgroupNm) {
		this.sssvcMgroupNm = sssvcMgroupNm;
	}

	public String getSsvcLgroupNm() {
		return ssvcLgroupNm;
	}

	public void setSsvcLgroupNm(String ssvcLgroupNm) {
		this.ssvcLgroupNm = ssvcLgroupNm;
	}

	public Integer getNpriority() {
		return npriority;
	}

	public void setNpriority(Integer npriority) {
		this.npriority = npriority;
	}

	public String getSvalidCheck() {
		return svalidCheck;
	}

	public void setSvalidCheck(String svalidCheck) {
		this.svalidCheck = svalidCheck;
	}


}
