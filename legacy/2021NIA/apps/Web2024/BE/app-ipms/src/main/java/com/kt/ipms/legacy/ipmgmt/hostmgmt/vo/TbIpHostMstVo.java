package com.kt.ipms.legacy.ipmgmt.hostmgmt.vo;

import java.io.Serializable;
import java.math.BigInteger;

import com.kt.ipms.legacy.cmn.vo.IpVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlMstListVo;

public class TbIpHostMstVo extends IpVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = 5432413365747270031L;

	private BigInteger nipHostMstSeq;

	private String sipHostTypeCd;

	private String sipHostTypeNm;

	private BigInteger nipAssignMstSeq;

	private BigInteger nipAllocMstSeq;

	private String sassignTypeCd;

	private String sassignTypeNm;

	private String sprorityYn;

	private String sportid;

	private Integer nlgipportseq;

	private String sipIfNm;

	private String snescode;

	private String sipHostNm;

	private String srssofficescode;

	private String srssofficesNm;

	private String ssaid;

	private String sconnAlias;

	private String sllnum;

	private String sexSvcCd;

	private String sexSvcNm;

	private String pipHostInet;

	private String snetmask;

	private String smodelname;

	private String sssvcMgroupNm;

	private String ssvcLgroupNm;

	private String ssvcUseTypeNm;

	private String sexLinkUseTypeCd;

	private String sexLinkUseTypeNm;

	private String scomment;

	private String ssvcLineTypeCd;

	private String ssvcLineTypeNm;

	private String ssvcGroupCd;

	private String ssvcGroupNm;

	private String ssvcObjCd;

	private String ssvcObjNm;

	private String sipCreateTypeCd;

	private TbLvlMstListVo lvlMstSeqListVo;

	/** MEMBER VARIABLE DECLARATION END **/

	/** MEMBER METHOD DECLARATION START **/

	public void setNipHostMstSeq(BigInteger nipHostMstSeq) {
		this.nipHostMstSeq = nipHostMstSeq;
	}

	public BigInteger getNipHostMstSeq() {
		return nipHostMstSeq;
	}

	public void setSipHostTypeCd(String sipHostTypeCd) {
		this.sipHostTypeCd = sipHostTypeCd;
	}

	public String getSipHostTypeCd() {
		return sipHostTypeCd;
	}

	public void setSipHostTypeNm(String sipHostTypeNm) {
		this.sipHostTypeNm = sipHostTypeNm;
	}

	public String getSipHostTypeNm() {
		return sipHostTypeNm;
	}

	public void setNipAssignMstSeq(BigInteger nipAssignMstSeq) {
		this.nipAssignMstSeq = nipAssignMstSeq;
	}

	public BigInteger getNipAssignMstSeq() {
		return nipAssignMstSeq;
	}

	public void setNipAllocMstSeq(BigInteger nipAllocMstSeq) {
		this.nipAllocMstSeq = nipAllocMstSeq;
	}

	public BigInteger getNipAllocMstSeq() {
		return nipAllocMstSeq;
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

	public void setSprorityYn(String sprorityYn) {
		this.sprorityYn = sprorityYn;
	}

	public String getSprorityYn() {
		return sprorityYn;
	}

	public void setSportid(String sportid) {
		this.sportid = sportid;
	}

	public String getSportid() {
		return sportid;
	}

	public void setNlgipportseq(Integer nlgipportseq) {
		this.nlgipportseq = nlgipportseq;
	}

	public Integer getNlgipportseq() {
		return nlgipportseq;
	}

	public void setSipIfNm(String sipIfNm) {
		this.sipIfNm = sipIfNm;
	}

	public String getSipIfNm() {
		return sipIfNm;
	}

	public void setSnescode(String snescode) {
		this.snescode = snescode;
	}

	public String getSnescode() {
		return snescode;
	}

	public void setSipHostNm(String sipHostNm) {
		this.sipHostNm = sipHostNm;
	}

	public String getSipHostNm() {
		return sipHostNm;
	}

	public void setSrssofficescode(String srssofficescode) {
		this.srssofficescode = srssofficescode;
	}

	public String getSrssofficescode() {
		return srssofficescode;
	}

	public void setSsaid(String ssaid) {
		this.ssaid = ssaid;
	}

	public String getSsaid() {
		return ssaid;
	}

	public void setSconnAlias(String sconnAlias) {
		this.sconnAlias = sconnAlias;
	}

	public String getSconnAlias() {
		return sconnAlias;
	}

	public void setSllnum(String sllnum) {
		this.sllnum = sllnum;
	}

	public String getSllnum() {
		return sllnum;
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

	public void setPipHostInet(String pipHostInet) {
		this.pipHostInet = pipHostInet;
	}

	public String getPipHostInet() {
		return pipHostInet;
	}

	public void setSnetmask(String snetmask) {
		this.snetmask = snetmask;
	}

	public String getSnetmask() {
		return snetmask;
	}

	public void setScomment(String scomment) {
		this.scomment = scomment;
	}

	public String getScomment() {
		return scomment;
	}

	public String getSrssofficesNm() {
		return srssofficesNm;
	}

	public void setSrssofficesNm(String srssofficesNm) {
		this.srssofficesNm = srssofficesNm;
	}

	public String getSmodelname() {
		return smodelname;
	}

	public void setSmodelname(String smodelname) {
		this.smodelname = smodelname;
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

	public String getSexLinkUseTypeCd() {
		return sexLinkUseTypeCd;
	}

	public void setSexLinkUseTypeCd(String sexLinkUseTypeCd) {
		this.sexLinkUseTypeCd = sexLinkUseTypeCd;
	}

	public String getSexLinkUseTypeNm() {
		return sexLinkUseTypeNm;
	}

	public void setSexLinkUseTypeNm(String sexLinkUseTypeNm) {
		this.sexLinkUseTypeNm = sexLinkUseTypeNm;
	}

	public String getSsvcLineTypeCd() {
		return ssvcLineTypeCd;
	}

	public void setSsvcLineTypeCd(String ssvcLineTypeCd) {
		this.ssvcLineTypeCd = ssvcLineTypeCd;
	}

	public String getSsvcLineTypeNm() {
		return ssvcLineTypeNm;
	}

	public void setSsvcLineTypeNm(String ssvcLineTypeNm) {
		this.ssvcLineTypeNm = ssvcLineTypeNm;
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

	public String getSipCreateTypeCd() {
		return sipCreateTypeCd;
	}

	public void setSipCreateTypeCd(String sipCreateTypeCd) {
		this.sipCreateTypeCd = sipCreateTypeCd;
	}

	public TbLvlMstListVo getLvlMstSeqListVo() {
		return lvlMstSeqListVo;
	}

	public void setLvlMstSeqListVo(TbLvlMstListVo lvlMstSeqListVo) {
		this.lvlMstSeqListVo = lvlMstSeqListVo;
	}

	/** MEMBER METHOD DECLARATION END **/
}