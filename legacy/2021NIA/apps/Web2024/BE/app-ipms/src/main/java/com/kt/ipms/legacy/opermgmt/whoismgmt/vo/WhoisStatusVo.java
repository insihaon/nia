package com.kt.ipms.legacy.opermgmt.whoismgmt.vo;

import java.io.Serializable;
import java.math.BigInteger;

import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class WhoisStatusVo extends CommonVo implements Serializable {
	private static final long serialVersionUID = 7757469910050513939L;
	private BigInteger transWait;
	private BigInteger trans;
	private BigInteger fail;
	private BigInteger registered;
	private BigInteger transOk;
	private BigInteger custWait;
	private BigInteger excepted;
	public BigInteger getTransWait() {
		return transWait;
	}
	public void setTransWait(BigInteger transWait) {
		this.transWait = transWait;
	}
	public BigInteger getTrans() {
		return trans;
	}
	public void setTrans(BigInteger trans) {
		this.trans = trans;
	}
	public BigInteger getFail() {
		return fail;
	}
	public void setFail(BigInteger fail) {
		this.fail = fail;
	}
	public BigInteger getRegistered() {
		return registered;
	}
	public void setRegistered(BigInteger registered) {
		this.registered = registered;
	}
	public BigInteger getTransOk() {
		return transOk;
	}
	public void setTransOk(BigInteger transOk) {
		this.transOk = transOk;
	}
	public BigInteger getCustWait() {
		return custWait;
	}
	public void setCustWait(BigInteger custWait) {
		this.custWait = custWait;
	}
	public BigInteger getExcepted() {
		return excepted;
	}
	public void setExcepted(BigInteger excepted) {
		this.excepted = excepted;
	}
}
