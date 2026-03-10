package com.kt.ipms.legacy.linkmgmt.batchmgmt.vo;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class TbBatchLogVo extends CommonVo implements Serializable {
	private static final long serialVersionUID = 7112525055459858383L;
	private BigInteger ninputSeq;
	private BigInteger npid;
	private String sifId;
	private String ssysNm;
	private String stbNm;
	private Date dstartDt;
	private Date dendDt;
	private String dtotalTime;
	private Integer ncnt;
	private String scomment;
	private String sstepStatus;
	private String sbatchEndYn;
	public BigInteger getNinputSeq() {
		return ninputSeq;
	}
	public void setNinputSeq(BigInteger ninputSeq) {
		this.ninputSeq = ninputSeq;
	}
	public BigInteger getNpid() {
		return npid;
	}
	public void setNpid(BigInteger npid) {
		this.npid = npid;
	}
	public String getSifId() {
		return sifId;
	}
	public void setSifId(String sifId) {
		this.sifId = sifId;
	}
	public String getSsysNm() {
		return ssysNm;
	}
	public void setSsysNm(String ssysNm) {
		this.ssysNm = ssysNm;
	}
	public String getStbNm() {
		return stbNm;
	}
	public void setStbNm(String stbNm) {
		this.stbNm = stbNm;
	}
	public Date getDstartDt() {
		return dstartDt;
	}
	public void setDstartDt(Date dstartDt) {
		this.dstartDt = dstartDt;
	}
	public Date getDendDt() {
		return dendDt;
	}
	public void setDendDt(Date dendDt) {
		this.dendDt = dendDt;
	}
	public String getDtotalTime() {
		return dtotalTime;
	}
	public void setDtotalTime(String dtotalTime) {
		this.dtotalTime = dtotalTime;
	}
	public Integer getNcnt() {
		return ncnt;
	}
	public void setNcnt(Integer ncnt) {
		this.ncnt = ncnt;
	}
	public String getSstepStatus() {
		return sstepStatus;
	}
	public void setSstepStatus(String sstepStatus) {
		this.sstepStatus = sstepStatus;
	}
	public String getSbatchEndYn() {
		return sbatchEndYn;
	}
	public void setSbatchEndYn(String sbatchEndYn) {
		this.sbatchEndYn = sbatchEndYn;
	}
	public String getScomment() {
		return scomment;
	}
	public void setScomment(String scomment) {
		this.scomment = scomment;
	}
}
