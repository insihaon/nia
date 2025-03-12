package com.kt.ipms.legacy.ipmgmt.allocmgmt.vo;

import java.io.Serializable;

import com.kt.ipms.legacy.cmn.vo.CommonCodeVo;
import com.kt.ipms.legacy.cmn.vo.IpVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlMstListVo;

import java.math.BigInteger;
import java.util.List;

public class IpAllocOperMstVo extends IpVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = 5627637610299525126L;

	private Integer index;

	private List<Integer> indexList;

	private String ipList;

	private String groupId;

	private String ssvcLineTypeCd;

	private String ssvcLineTypeNm;

	private String ssvcGroupCd;

	private String ssvcGroupNm;

	private String ssvcObjCd;

	private String ssvcObjNm;

	private BigInteger nipBlockMstSeq;

	private BigInteger nlvlMstSeq;

	private BigInteger nipAssignMstSeq;

	private BigInteger nipAllocMstCnt;

	private String sassignLevelCd;

	private String sassignLevelNm;

	private String sipCreateSeqCd;

	private String sipCreateSeqNm;

	private String scomment;

	private BigInteger nipmsSvcSeq;

	private String sassignTypeCd;

	private String sassignTypeNm;

	private String sexSvcCd;

	private String sexSvcNm;

	private String sipCreateTypeCd;

	private String sipCreateTypeNm;

	private String sipAllocExTypeCd;

	private String sipAllocExTypeNm;

	private TbLvlMstListVo lvlMstSeqListVo;

	/* 회선정보 추가 */
	private String sllnum; // 전용회선번호
	private List<String> sllnums; // 전용회선번호 multi

	private String ssubscnealias; // 장비별칭명

	private String ssubsclgipportdescription; // 인터페이스

	private String sicisofficescode; // 수용국코드

	private String sicisofficesname; // 수용국명

	private String sofficecode; // 수용국코드 (2014.11.10)

	private String sofficename; // 수용국명 (2014.11.10)

	private String smodelname; // 모델명

	private String ssubscmstip; // 대표IP

	private String svalidCheck; // 감사여부

	private String sconnAlias; // 수용회선명
	private List<String> sconnAliass; // 수용회선명

	private BigInteger nipAllocMstSeq;

	private String ssubscnnescode; // 장비표준코드

	private String ssubscnescode; // 장비표준코드

	private String sgatewayip; // 게이트웨이IP

	/* 회선장비검색조건 */

	private String sneSrchTypeCd;// 조회 유형(시설)

	private String sicisofficescodeNe; // 장비수용국

	private String ssubscnealiasNe; // 장비별칭

	private String smodelnameNe; // 모델명

	private String ssubscmstipNe; // 대표IP

	private String sssvcMgroupNm; // 상품명

	private String ssvcLgroupNm; // 서비스 유형

	private String ssvcUseTypeNm; // 사업용여부

	private String ssaid; // 서비스 계약 번호
	private List<String> ssaids; // 서비스 계약 번호 multi

	private String scustName; // 고객명

	private String sipAssignSubNm; // 사용용도

	private String llSrchTypeCd; // 회선검색유형

	private String sordernum;// 오더번호

	/* 할당 처리 파라미터 셋팅 */
	private String sregyn;

	private String slacpsid;
	// private BigInteger nipmsSvcSeq;

	private String ssvcUseTypeCd;

	private String sexPushYn;

	private BigInteger nticketActSeq;

	private String sipmsSvcNm;

	private String sneossDdYn;

	private String sfirstAddrGwip; // 시작_IP_CIDR(GWIP용 계산)

	private String slastAddrGwip; // 종료_IP_CIDR(GWIP용 계산)

	/* 할당 메인조회와 오더할당 조회 구분을 위한 추가 구성 */
	private String sAlcSrchTypeCd;

	private BigInteger nwhoisSeq; // Whois Seq

	/**
	 * 라우팅_비교_결과_MST_SEQ
	 */
	private BigInteger nroutingChkMstSeq;

	/* 자국/대국 링크 정보 */

	/* 인터페이스 시리얼 IP */
	private String pifSerialIp;

	/* 자국 장비명 */
	private String sanealias;

	/* 자국 장비IP */
	private String samstip;

	/* 자국 IF명 */
	private String saifname;

	/* 자국 시설코드 */
	private String salocationcode;

	/* 자국 시설명 */
	private String salocationcodeNm;

	/* 자국 수용국 코드 */
	private String saofficescode;

	/* 자국 수용국 명 */
	private String saofficescodeNm;

	/* 대국 장비명 */
	private String sznealias;

	/* 대국 장비IP */
	private String szmstip;

	/* 대국 IF명 */
	private String szifname;

	/* 대국 시설코드 */
	private String szlocationcode;

	/* 대국 시설명 */
	private String szlocationcodeNm;

	/* 대국 수용국 코드 */
	private String szofficescode;

	/* 대국 수용국 명 */
	private String szofficescodeNm;

	/* 자국/대국 수용국 검색조건 */
	private String sofficescodeSrch;

	/* 자국/대국 장비명 검색조건 */
	private String snealiasSrch;

	/* 자국/대국 대표IP 검색조건 */
	private String smstipSrch;

	/* 자국/대국 IF 검색조건 */
	private String sifipSrch;

	/* 자국/대국 시설명 검색조건 */
	private String slocationcodeNmSrch;

	/* 자국/대국 전용번호 검색조건 */
	private String sllnumSrch;

	/* 자국/대국 인터페이스 시리얼 IP 검색조건 */
	private String pifSerialIpSrch;

	private BigInteger nipLinkMstSeq;

	private String sconnalias;

	private String sGubun;

	private String sNextHop;

	private int totalCount;

	/* Summary 개수 */
	private Integer summaryCnt; /* Summary 검색조건 */
	private String nsummaryCnt;
	private List<Integer> nsummaryCntMulti;

	/* null0 여부 */
	private String snull0Yn;

	/* 라우팅 비교/점검 여부 */
	private String sintgrmYn;

	private String spageType; // 링크조회, 회선조회

	private String ssubscnealiasType;

	private boolean bUploadFlag; // IP Excel Upload를 사용 플래그 *IpUpload*

	private List<CommonCodeVo> sassignTypeCds;

	private String sassignTypeCdMultiStr;

	private List<String> sassignTypeMulti;

	private String ssvcGroupCdMultiStr;

	private List<String> ssvcGroupCdMulti;

	private String ssubsclgipportip;

	private String ssubscrouterserialip;

	/** MEMBER VARIABLE DECLARATION END **/

	/** MEMBER METHOD DECLARATION START **/

	public Integer getIndex() {
		return index;
	}

	public void setIndexList(List<Integer> indexList) {
		this.indexList = indexList;
	}

	public List<Integer> getIndexList() {
		return indexList;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getGroupId() {
		return groupId;
	}

	public String getIpList() {
		return ipList;
	}

	public void setIpList(String ipList) {
		this.ipList = ipList;
	}

	public void setSsvcLineTypeCd(String ssvcLineTypeCd) {
		this.ssvcLineTypeCd = ssvcLineTypeCd;
	}

	public String getSsvcGroupCdMultiStr() {
		return ssvcGroupCdMultiStr;
	}

	public void setSsvcGroupCdMultiStr(String ssvcGroupCdMultiStr) {
		this.ssvcGroupCdMultiStr = ssvcGroupCdMultiStr;
	}

	public List<String> getSsvcGroupCdMulti() {
		return ssvcGroupCdMulti;
	}

	public void setSsvcGroupCdMulti(List<String> ssvcGroupCdMulti) {
		this.ssvcGroupCdMulti = ssvcGroupCdMulti;
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

	public void setNlvlMstSeq(BigInteger nlvlMstSeq) {
		this.nlvlMstSeq = nlvlMstSeq;
	}

	public BigInteger getNlvlMstSeq() {
		return nlvlMstSeq;
	}

	public void setNipAssignMstSeq(BigInteger nipAssignMstSeq) {
		this.nipAssignMstSeq = nipAssignMstSeq;
	}

	public BigInteger getNipAssignMstSeq() {
		return nipAssignMstSeq;
	}

	public void setNipAllocMstCnt(BigInteger nipAllocMstCnt) {
		this.nipAllocMstCnt = nipAllocMstCnt;
	}

	public BigInteger getNipAllocMstCnt() {
		return nipAllocMstCnt;
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

	public void setSipCreateSeqCd(String sipCreateSeqCd) {
		this.sipCreateSeqCd = sipCreateSeqCd;
	}

	public String getSipCreateSeqCd() {
		return sipCreateSeqCd;
	}

	public void setSipCreateSeqNm(String sipCreateSeqNm) {
		this.sipCreateSeqNm = sipCreateSeqNm;
	}

	public String getSipCreateSeqNm() {
		return sipCreateSeqNm;
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

	public void setSexSvcNm(String sexSvcNm) {
		this.sexSvcNm = sexSvcNm;
	}

	public String getSexSvcNm() {
		return sexSvcNm;
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

	public List<String> getSllnums() {
		return sllnums;
	}

	public void setSllnums(List<String> sllnums) {
		this.sllnums = sllnums;
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

	public String getSicisofficescode() {
		return sicisofficescode;
	}

	public void setSicisofficescode(String sicisofficescode) {
		this.sicisofficescode = sicisofficescode;
	}

	public String getSneSrchTypeCd() {
		return sneSrchTypeCd;
	}

	public void setSneSrchTypeCd(String sneSrchTypeCd) {
		this.sneSrchTypeCd = sneSrchTypeCd;
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

	public String getSvalidCheck() {
		return svalidCheck;
	}

	public void setSvalidCheck(String svalidCheck) {
		this.svalidCheck = svalidCheck;
	}

	public String getSconnAlias() {
		return sconnAlias;
	}

	public void setSconnAlias(String sconnAlias) {
		this.sconnAlias = sconnAlias;
	}

	public List<String> getSconnAliass() {
		return sconnAliass;
	}

	public void setSconnAliass(List<String> sconnAliass) {
		this.sconnAliass = sconnAliass;
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

	public String getSsvcUseTypeNm() {
		return ssvcUseTypeNm;
	}

	public void setSsvcUseTypeNm(String ssvcUseTypeNm) {
		this.ssvcUseTypeNm = ssvcUseTypeNm;
	}

	public String getSsaid() {
		return ssaid;
	}

	public void setSsaid(String ssaid) {
		this.ssaid = ssaid;
	}

	public List<String> getSsaids() {
		return ssaids;
	}

	public void setSsaids(List<String> ssaids) {
		this.ssaids = ssaids;
	}

	public String getScustName() {
		return scustName;
	}

	public void setScustName(String scustName) {
		this.scustName = scustName;
	}

	public String getSmodelname() {
		return smodelname;
	}

	public void setSmodelname(String smodelname) {
		this.smodelname = smodelname;
	}

	public String getSsubscmstip() {
		return ssubscmstip;
	}

	public void setSsubscmstip(String ssubscmstip) {
		this.ssubscmstip = ssubscmstip;
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

	public String getSipAssignSubNm() {
		return sipAssignSubNm;
	}

	public void setSipAssignSubNm(String sipAssignSubNm) {
		this.sipAssignSubNm = sipAssignSubNm;
	}

	public String getLlSrchTypeCd() {
		return llSrchTypeCd;
	}

	public void setLlSrchTypeCd(String llSrchTypeCd) {
		this.llSrchTypeCd = llSrchTypeCd;
	}

	public BigInteger getNipAllocMstSeq() {
		return nipAllocMstSeq;
	}

	public void setNipAllocMstSeq(BigInteger nipAllocMstSeq) {
		this.nipAllocMstSeq = nipAllocMstSeq;
	}

	public String getSsubscnnescode() {
		return ssubscnnescode;
	}

	public void setSsubscnnescode(String ssubscnnescode) {
		this.ssubscnnescode = ssubscnnescode;
	}

	public String getSgatewayip() {
		return sgatewayip;
	}

	public void setSgatewayip(String sgatewayip) {
		this.sgatewayip = sgatewayip;
	}

	public String getSregyn() {
		return sregyn;
	}

	public void setSregyn(String sregyn) {
		this.sregyn = sregyn;
	}

	public String getSlacpsid() {
		return slacpsid;
	}

	public void setSlacpsid(String slacpsid) {
		this.slacpsid = slacpsid;
	}

	public String getSsvcUseTypeCd() {
		return ssvcUseTypeCd;
	}

	public void setSsvcUseTypeCd(String ssvcUseTypeCd) {
		this.ssvcUseTypeCd = ssvcUseTypeCd;
	}

	public String getSexPushYn() {
		return sexPushYn;
	}

	public void setSexPushYn(String sexPushYn) {
		this.sexPushYn = sexPushYn;
	}

	public BigInteger getNticketActSeq() {
		return nticketActSeq;
	}

	public void setNticketActSeq(BigInteger nticketActSeq) {
		this.nticketActSeq = nticketActSeq;
	}

	public String getsAlcSrchTypeCd() {
		return sAlcSrchTypeCd;
	}

	public void setsAlcSrchTypeCd(String sAlcSrchTypeCd) {
		this.sAlcSrchTypeCd = sAlcSrchTypeCd;
	}

	public String getSneossDdYn() {
		return sneossDdYn;
	}

	public void setSneossDdYn(String sneossDdYn) {
		this.sneossDdYn = sneossDdYn;
	}

	public String getSipmsSvcNm() {
		return sipmsSvcNm;
	}

	public void setSipmsSvcNm(String sipmsSvcNm) {
		this.sipmsSvcNm = sipmsSvcNm;
	}

	public String getSordernum() {
		return sordernum;
	}

	public void setSordernum(String sordernum) {
		this.sordernum = sordernum;
	}

	public String getSfirstAddrGwip() {
		return sfirstAddrGwip;
	}

	public void setSfirstAddrGwip(String sfirstAddrGwip) {
		this.sfirstAddrGwip = sfirstAddrGwip;
	}

	public String getSlastAddrGwip() {
		return slastAddrGwip;
	}

	public void setSlastAddrGwip(String slastAddrGwip) {
		this.slastAddrGwip = slastAddrGwip;
	}

	public BigInteger getNwhoisSeq() {
		return nwhoisSeq;
	}

	public void setNwhoisSeq(BigInteger nwhoisSeq) {
		this.nwhoisSeq = nwhoisSeq;
	}

	public BigInteger getNroutingChkMstSeq() {
		return nroutingChkMstSeq;
	}

	public void setNroutingChkMstSeq(BigInteger nroutingChkMstSeq) {
		this.nroutingChkMstSeq = nroutingChkMstSeq;
	}

	public String getSanealias() {
		return sanealias;
	}

	public void setSanealias(String sanealias) {
		this.sanealias = sanealias;
	}

	public String getSamstip() {
		return samstip;
	}

	public void setSamstip(String samstip) {
		this.samstip = samstip;
	}

	public String getSaifname() {
		return saifname;
	}

	public void setSaifname(String saifname) {
		this.saifname = saifname;
	}

	public String getSalocationcode() {
		return salocationcode;
	}

	public void setSalocationcode(String salocationcode) {
		this.salocationcode = salocationcode;
	}

	public String getSalocationcodeNm() {
		return salocationcodeNm;
	}

	public void setSalocationcodeNm(String salocationcodeNm) {
		this.salocationcodeNm = salocationcodeNm;
	}

	public String getSaofficescode() {
		return saofficescode;
	}

	public void setSaofficescode(String saofficescode) {
		this.saofficescode = saofficescode;
	}

	public String getSaofficescodeNm() {
		return saofficescodeNm;
	}

	public void setSaofficescodeNm(String saofficescodeNm) {
		this.saofficescodeNm = saofficescodeNm;
	}

	public String getSznealias() {
		return sznealias;
	}

	public void setSznealias(String sznealias) {
		this.sznealias = sznealias;
	}

	public String getSzmstip() {
		return szmstip;
	}

	public void setSzmstip(String szmstip) {
		this.szmstip = szmstip;
	}

	public String getSzifname() {
		return szifname;
	}

	public void setSzifname(String szifname) {
		this.szifname = szifname;
	}

	public String getSzlocationcode() {
		return szlocationcode;
	}

	public void setSzlocationcode(String szlocationcode) {
		this.szlocationcode = szlocationcode;
	}

	public String getSzlocationcodeNm() {
		return szlocationcodeNm;
	}

	public void setSzlocationcodeNm(String szlocationcodeNm) {
		this.szlocationcodeNm = szlocationcodeNm;
	}

	public String getSzofficescode() {
		return szofficescode;
	}

	public void setSzofficescode(String szofficescode) {
		this.szofficescode = szofficescode;
	}

	public String getSzofficescodeNm() {
		return szofficescodeNm;
	}

	public void setSzofficescodeNm(String szofficescodeNm) {
		this.szofficescodeNm = szofficescodeNm;
	}

	public String getSofficescodeSrch() {
		return sofficescodeSrch;
	}

	public void setSofficescodeSrch(String sofficescodeSrch) {
		this.sofficescodeSrch = sofficescodeSrch;
	}

	public String getSnealiasSrch() {
		return snealiasSrch;
	}

	public void setSnealiasSrch(String snealiasSrch) {
		this.snealiasSrch = snealiasSrch;
	}

	public String getSmstipSrch() {
		return smstipSrch;
	}

	public void setSmstipSrch(String smstipSrch) {
		this.smstipSrch = smstipSrch;
	}

	public String getSifipSrch() {
		return sifipSrch;
	}

	public void setSifipSrch(String sifipSrch) {
		this.sifipSrch = sifipSrch;
	}

	public String getSlocationcodeNmSrch() {
		return slocationcodeNmSrch;
	}

	public void setSlocationcodeNmSrch(String slocationcodeNmSrch) {
		this.slocationcodeNmSrch = slocationcodeNmSrch;
	}

	public String getSllnumSrch() {
		return sllnumSrch;
	}

	public void setSllnumSrch(String sllnumSrch) {
		this.sllnumSrch = sllnumSrch;
	}

	public String getPifSerialIpSrch() {
		return pifSerialIpSrch;
	}

	public void setPifSerialIpSrch(String pifSerialIpSrch) {
		this.pifSerialIpSrch = pifSerialIpSrch;
	}

	public String getPifSerialIp() {
		return pifSerialIp;
	}

	public void setPifSerialIp(String pifSerialIp) {
		this.pifSerialIp = pifSerialIp;
	}

	public BigInteger getNipLinkMstSeq() {
		return nipLinkMstSeq;
	}

	public void setNipLinkMstSeq(BigInteger nipLinkMstSeq) {
		this.nipLinkMstSeq = nipLinkMstSeq;
	}

	public String getSconnalias() {
		return sconnalias;
	}

	public void setSconnalias(String sconnalias) {
		this.sconnalias = sconnalias;
	}

	public String getsGubun() {
		return sGubun;
	}

	public void setsGubun(String sGubun) {
		this.sGubun = sGubun;
	}

	public String getsNextHop() {
		return sNextHop;
	}

	public void setsNextHop(String sNextHop) {
		this.sNextHop = sNextHop;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public String getSnull0Yn() {
		return snull0Yn;
	}

	public void setSnull0Yn(String snull0Yn) {
		this.snull0Yn = snull0Yn;
	}

	public String getSintgrmYn() {
		return sintgrmYn;
	}

	public void setSintgrmYn(String sintgrmYn) {
		this.sintgrmYn = sintgrmYn;
	}

	public String getSpageType() {
		return spageType;
	}

	public void setSpageType(String spageType) {
		this.spageType = spageType;
	}

	public Integer getSummaryCnt() {
		return summaryCnt;
	}

	public void setSummaryCnt(Integer summaryCnt) {
		this.summaryCnt = summaryCnt;
	}

	public String getNsummaryCnt() {
		return nsummaryCnt;
	}

	public void setNsummaryCnt(String nsummaryCnt) {
		this.nsummaryCnt = nsummaryCnt;
	}

	public List<Integer> getNsummaryCntMulti() {
		return nsummaryCntMulti;
	}

	public void setNsummaryCntMulti(List<Integer> nsummaryCntMulti) {
		this.nsummaryCntMulti = nsummaryCntMulti;
	}

	public String getSsubscnealiasType() {
		return ssubscnealiasType;
	}

	public void setSsubscnealiasType(String ssubscnealiasType) {
		this.ssubscnealiasType = ssubscnealiasType;
	}

	public boolean isbUploadFlag() {
		return bUploadFlag;
	}

	public void setbUploadFlag(boolean bUploadFlag) {
		this.bUploadFlag = bUploadFlag;
	}

	public List<CommonCodeVo> getSassignTypeCds() {
		return sassignTypeCds;
	}

	public void setSassignTypeCds(List<CommonCodeVo> sassignTypeCds) {
		this.sassignTypeCds = sassignTypeCds;
	}

	public String getSassignTypeCdMultiStr() {
		return sassignTypeCdMultiStr;
	}

	public void setSassignTypeCdMultiStr(String sassignTypeCdMultiStr) {
		this.sassignTypeCdMultiStr = sassignTypeCdMultiStr;
	}

	public List<String> getSassignTypeMulti() {
		return sassignTypeMulti;
	}

	public void setSassignTypeMulti(List<String> sassignTypeMulti) {
		this.sassignTypeMulti = sassignTypeMulti;
	}

	public String getSsubscnescode() {
		return ssubscnescode;
	}

	public void setSsubscnescode(String ssubscnescode) {
		this.ssubscnescode = ssubscnescode;
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

	/** MEMBER METHOD DECLARATION END **/
}