package com.kt.ipms.legacy.ticketmgmt.optmgmt.vo;
import java.io.Serializable;
import java.math.BigInteger;

import com.kt.ipms.legacy.cmn.vo.CommonVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlMstListVo;


public class TbOptimizationIpMstVo extends CommonVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = -7688187135252610623L;

	private BigInteger noptIpMstSeq;

	private BigInteger nlvlMstSeq;

	private BigInteger nipAssignMstSeq;

	private String sassignTypeCd;

	private String sassignTypeNm;

	private String pipPrefix;

	private String sipBlock;

	private Integer nbitmask;

	private BigInteger ncnt;

	private BigInteger nipAssignMstSeqTar;

	private String pipPrefixTar;

	private String sipBlockTar;

	private Integer nbitmaskTar;

	private String scomment;
	
	private String ssvcLineTypeCd;

	private String ssvcLineTypeNm;
	
	private String ssvcGroupCd;
	
	private String ssvcGroupNm;
	
	private String ssvcObjCd;
	
	private String ssvcObjNm;
	
	private TbLvlMstListVo lvlMstSeqListVo;
	
	private String virtualPipPrefix;
	
	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/

	public void setNoptIpMstSeq(BigInteger noptIpMstSeq) {
		this.noptIpMstSeq = noptIpMstSeq;
	}

	public BigInteger getNoptIpMstSeq() {
		return noptIpMstSeq;
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

	public void setPipPrefix(String pipPrefix) {
		this.pipPrefix = pipPrefix;
	}

	public String getPipPrefix() {
		return pipPrefix;
	}

	public void setSipBlock(String sipBlock) {
		this.sipBlock = sipBlock;
	}

	public String getSipBlock() {
		return sipBlock;
	}

	public void setNbitmask(Integer nbitmask) {
		this.nbitmask = nbitmask;
	}

	public Integer getNbitmask() {
		return nbitmask;
	}

	public void setNcnt(BigInteger ncnt) {
		this.ncnt = ncnt;
	}

	public BigInteger getNcnt() {
		return ncnt;
	}

	public void setNipAssignMstSeqTar(BigInteger nipAssignMstSeqTar) {
		this.nipAssignMstSeqTar = nipAssignMstSeqTar;
	}

	public BigInteger getNipAssignMstSeqTar() {
		return nipAssignMstSeqTar;
	}

	public void setPipPrefixTar(String pipPrefixTar) {
		this.pipPrefixTar = pipPrefixTar;
	}

	public String getPipPrefixTar() {
		return pipPrefixTar;
	}

	public void setSipBlockTar(String sipBlockTar) {
		this.sipBlockTar = sipBlockTar;
	}

	public String getSipBlockTar() {
		return sipBlockTar;
	}

	public void setNbitmaskTar(Integer nbitmaskTar) {
		this.nbitmaskTar = nbitmaskTar;
	}

	public Integer getNbitmaskTar() {
		return nbitmaskTar;
	}

	public void setScomment(String scomment) {
		this.scomment = scomment;
	}

	public String getScomment() {
		return scomment;
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

	public TbLvlMstListVo getLvlMstSeqListVo() {
		return lvlMstSeqListVo;
	}

	public void setLvlMstSeqListVo(TbLvlMstListVo lvlMstSeqListVo) {
		this.lvlMstSeqListVo = lvlMstSeqListVo;
	}

	public String getVirtualPipPrefix() {
		return virtualPipPrefix;
	}

	public void setVirtualPipPrefix(String virtualPipPrefix) {
		this.virtualPipPrefix = virtualPipPrefix;
	}

	/** MEMBER METHOD DECLARATION END **/
}