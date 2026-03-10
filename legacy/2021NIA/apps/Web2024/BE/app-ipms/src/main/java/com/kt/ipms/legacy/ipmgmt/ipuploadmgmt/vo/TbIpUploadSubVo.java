package com.kt.ipms.legacy.ipmgmt.ipuploadmgmt.vo;

import java.io.Serializable;
import java.math.BigInteger;

import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class TbIpUploadSubVo extends CommonVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4937386171890094728L;

	private BigInteger seq;
	
	private BigInteger mstSeq;
	
	private int rnum;
	
	private String pipPrefix;
	
	private String ssuccessYn;
	
	public BigInteger getSeq() {
		return seq;
	}

	public void setSeq(BigInteger seq) {
		this.seq = seq;
	}

	public BigInteger getMstSeq() {
		return mstSeq;
	}

	public void setMstSeq(BigInteger mstSeq) {
		this.mstSeq = mstSeq;
	}

	public String getPipPrefix() {
		return pipPrefix;
	}

	public void setPipPrefix(String pipPrefix) {
		this.pipPrefix = pipPrefix;
	}

	public String getSsuccessYn() {
		return ssuccessYn;
	}

	public void setSsuccessYn(String ssuccessYn) {
		this.ssuccessYn = ssuccessYn;
	}

	public int getRnum() {
		return rnum;
	}

	public void setRnum(int rnum) {
		this.rnum = rnum;
	}
	
}
