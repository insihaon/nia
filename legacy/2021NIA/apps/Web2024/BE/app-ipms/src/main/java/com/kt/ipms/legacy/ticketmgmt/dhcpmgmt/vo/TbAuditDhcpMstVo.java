package com.kt.ipms.legacy.ticketmgmt.dhcpmgmt.vo;
import java.io.Serializable;

import com.kt.ipms.legacy.cmn.vo.CommonVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlMstListVo;

import java.util.Date;
import java.math.BigInteger;


public class TbAuditDhcpMstVo extends CommonVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = -8368094858308337624L;

	private BigInteger nauditDhcpMstSeq;

	private String ssvcLineTypeCd;

	private String ssvcLineTypeNm;
	
	private String ssvcGroupCd;
	
	private String ssvcGroupNm;
	
	private String ssvcObjCd;
	
	private String ssvcObjNm;

	private BigInteger nlvlMstSeq;

	private String stopNodeCd;

	private String stopNodeNm;

	private String sofficeCd;

	private String sofficeNm;

	private String sl3Nescode;

	private String sl3EquipAlias;

	private String sl3Mstip;

	private String ssaid;

	private String sllnum;

	private BigInteger nipBlockCnt;

	private BigInteger nipCnt;

	private Integer nmaxPeak;

	private Integer nminPeak;

	private Integer nauditValue;

	private Integer nopimizeValue;

	private Integer ndhcpMinIpBlockCntOver;

	private Integer ndhcpMinIpCntOver;

	private Integer ndhcpRangeDays;

	private String bauditYn;
	
	private String bauditNm;
	
	private String boptimizeYn;
	
	private String boptimizeNm;
	
	private Date dauditCheckDt;

	private Date dauditProcessDt;

	private Date noptimizeCheckDt;

	private Date doptimizeProcessDt;

	private Date dmodifyDt;
	
	private String smodifyId;
	
	private Date dcreateDt;
	
	private String suseStatusCd;

	private String suseStatusNm;

	private String bfalg;
	
	private String bfalgNm;

	private String scomment;
	
	private String screateId;
	
	private TbLvlMstListVo lvlMstSeqListVo;
	
	private String llSrchTypeCd;
	
	private String llsrchVal;
	
	private String scDateTypeCd;
	
	private String sortType;
	
	private String sicisofficescodeNe;
	
	private String sicisofficescode;
	
	private String sortOrdr;
	
	

	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/

	public void setNauditDhcpMstSeq(BigInteger nauditDhcpMstSeq) {
		this.nauditDhcpMstSeq = nauditDhcpMstSeq;
	}

	public BigInteger getNauditDhcpMstSeq() {
		return nauditDhcpMstSeq;
	}

	public void setSsvcLineTypeCd(String ssvcLineTypeCd) {
		this.ssvcLineTypeCd = ssvcLineTypeCd;
	}

	public String getSsvcLineTypeCd() {
		return ssvcLineTypeCd;
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

	public void setStopNodeCd(String stopNodeCd) {
		this.stopNodeCd = stopNodeCd;
	}

	public String getStopNodeCd() {
		return stopNodeCd;
	}

	public void setStopNodeNm(String stopNodeNm) {
		this.stopNodeNm = stopNodeNm;
	}

	public String getStopNodeNm() {
		return stopNodeNm;
	}

	public void setSofficeCd(String sofficeCd) {
		this.sofficeCd = sofficeCd;
	}

	public String getSofficeCd() {
		return sofficeCd;
	}

	public void setSofficeNm(String sofficeNm) {
		this.sofficeNm = sofficeNm;
	}

	public String getSofficeNm() {
		return sofficeNm;
	}

	public String getSsvcObjCd() {
		return ssvcObjCd;
	}

	public void setSsvcObjCd(String ssvcObjCd) {
		this.ssvcObjCd = ssvcObjCd;
	}

	public void setSl3Nescode(String sl3Nescode) {
		this.sl3Nescode = sl3Nescode;
	}

	public String getSl3Nescode() {
		return sl3Nescode;
	}

	public void setSl3EquipAlias(String sl3EquipAlias) {
		this.sl3EquipAlias = sl3EquipAlias;
	}

	public String getSl3EquipAlias() {
		return sl3EquipAlias;
	}

	public void setSl3Mstip(String sl3Mstip) {
		this.sl3Mstip = sl3Mstip;
	}

	public String getSl3Mstip() {
		return sl3Mstip;
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

	public void setNipBlockCnt(BigInteger nipBlockCnt) {
		this.nipBlockCnt = nipBlockCnt;
	}

	public BigInteger getNipBlockCnt() {
		return nipBlockCnt;
	}

	public void setNipCnt(BigInteger nipCnt) {
		this.nipCnt = nipCnt;
	}

	public BigInteger getNipCnt() {
		return nipCnt;
	}

	public void setNmaxPeak(Integer nmaxPeak) {
		this.nmaxPeak = nmaxPeak;
	}

	public Integer getNmaxPeak() {
		return nmaxPeak;
	}

	public void setNminPeak(Integer nminPeak) {
		this.nminPeak = nminPeak;
	}

	public Integer getNminPeak() {
		return nminPeak;
	}

	public void setNauditValue(Integer nauditValue) {
		this.nauditValue = nauditValue;
	}

	public Integer getNauditValue() {
		return nauditValue;
	}

	public void setNopimizeValue(Integer nopimizeValue) {
		this.nopimizeValue = nopimizeValue;
	}

	public Integer getNopimizeValue() {
		return nopimizeValue;
	}

	public void setNdhcpMinIpBlockCntOver(Integer ndhcpMinIpBlockCntOver) {
		this.ndhcpMinIpBlockCntOver = ndhcpMinIpBlockCntOver;
	}

	public Integer getNdhcpMinIpBlockCntOver() {
		return ndhcpMinIpBlockCntOver;
	}

	public void setNdhcpMinIpCntOver(Integer ndhcpMinIpCntOver) {
		this.ndhcpMinIpCntOver = ndhcpMinIpCntOver;
	}

	public Integer getNdhcpMinIpCntOver() {
		return ndhcpMinIpCntOver;
	}

	public void setNdhcpRangeDays(Integer ndhcpRangeDays) {
		this.ndhcpRangeDays = ndhcpRangeDays;
	}

	public Integer getNdhcpRangeDays() {
		return ndhcpRangeDays;
	}

	public void setBauditYn(String bauditYn) {
		this.bauditYn = bauditYn;
	}

	public String getBauditYn() {
		return bauditYn;
	}
    
	public String getBauditNm() {
		return bauditNm;
	}

	public void setBauditNm(String bauditNm) {
		this.bauditNm = bauditNm;
	}

	public String getBoptimizeNm() {
		return boptimizeNm;
	}

	public void setBoptimizeNm(String boptimizeNm) {
		this.boptimizeNm = boptimizeNm;
	}

	public void setBoptimizeYn(String boptimizeYn) {
		this.boptimizeYn = boptimizeYn;
	}

	public String getBoptimizeYn() {
		return boptimizeYn;
	}

	public void setDauditCheckDt(Date dauditCheckDt) {
		this.dauditCheckDt = dauditCheckDt;
	}

	public Date getDauditCheckDt() {
		return dauditCheckDt;
	}

	public void setDauditProcessDt(Date dauditProcessDt) {
		this.dauditProcessDt = dauditProcessDt;
	}

	public Date getDauditProcessDt() {
		return dauditProcessDt;
	}

	public void setNoptimizeCheckDt(Date noptimizeCheckDt) {
		this.noptimizeCheckDt = noptimizeCheckDt;
	}

	public Date getNoptimizeCheckDt() {
		return noptimizeCheckDt;
	}

	public void setDoptimizeProcessDt(Date doptimizeProcessDt) {
		this.doptimizeProcessDt = doptimizeProcessDt;
	}

	public Date getDoptimizeProcessDt() {
		return doptimizeProcessDt;
	}

	public Date getDmodifyDt() {
		return dmodifyDt;
	}

	public void setDmodifyDt(Date dmodifyDt) {
		this.dmodifyDt = dmodifyDt;
	}

	public void setSuseStatusCd(String suseStatusCd) {
		this.suseStatusCd = suseStatusCd;
	}

	public String getSuseStatusCd() {
		return suseStatusCd;
	}

	public void setSuseStatusNm(String suseStatusNm) {
		this.suseStatusNm = suseStatusNm;
	}

	public String getSuseStatusNm() {
		return suseStatusNm;
	}

	public void setBfalg(String bfalg) {
		this.bfalg = bfalg;
	}

	public String getBfalg() {
		return bfalg;
	}

	public String getBfalgNm() {
		return bfalgNm;
	}

	public void setBfalgNm(String bfalgNm) {
		this.bfalgNm = bfalgNm;
	}

	public void setScomment(String scomment) {
		this.scomment = scomment;
	}

	public String getScomment() {
		return scomment;
	}

	public TbLvlMstListVo getLvlMstSeqListVo() {
		return lvlMstSeqListVo;
	}

	public void setLvlMstSeqListVo(TbLvlMstListVo lvlMstSeqListVo) {
		this.lvlMstSeqListVo = lvlMstSeqListVo;
	}

	public String getSmodifyId() {
		return smodifyId;
	}

	public void setSmodifyId(String smodifyId) {
		this.smodifyId = smodifyId;
	}

	public Date getDcreateDt() {
		return dcreateDt;
	}

	public void setDcreateDt(Date dcreateDt) {
		this.dcreateDt = dcreateDt;
	}

	public String getScreateId() {
		return screateId;
	}

	public void setScreateId(String screateId) {
		this.screateId = screateId;
	}

	public String getSicisofficescodeNe() {
		return sicisofficescodeNe;
	}

	public void setSicisofficescodeNe(String sicisofficescodeNe) {
		this.sicisofficescodeNe = sicisofficescodeNe;
	}

	public String getLlSrchTypeCd() {
		return llSrchTypeCd;
	}

	public void setLlSrchTypeCd(String llSrchTypeCd) {
		this.llSrchTypeCd = llSrchTypeCd;
	}

	public String getLlsrchVal() {
		return llsrchVal;
	}

	public void setLlsrchVal(String llsrchVal) {
		this.llsrchVal = llsrchVal;
	}

	public String getScDateTypeCd() {
		return scDateTypeCd;
	}

	public void setScDateTypeCd(String scDateTypeCd) {
		this.scDateTypeCd = scDateTypeCd;
	}

	public String getSortType() {
		return sortType;
	}

	public void setSortType(String sortType) {
		this.sortType = sortType;
	}

	public String getSsvcObjNm() {
		return ssvcObjNm;
	}

	public void setSsvcObjNm(String ssvcObjNm) {
		this.ssvcObjNm = ssvcObjNm;
	}

	
	
	public String getSicisofficescode() {
		return sicisofficescode;
	}

	public void setSicisofficescode(String sicisofficescode) {
		this.sicisofficescode = sicisofficescode;
	}

	public String getSortOrdr() {
		return sortOrdr;
	}

	public void setSortOrdr(String sortOrdr) {
		this.sortOrdr = sortOrdr;
	}
	
	
	/** MEMBER METHOD DECLARATION END **/
}