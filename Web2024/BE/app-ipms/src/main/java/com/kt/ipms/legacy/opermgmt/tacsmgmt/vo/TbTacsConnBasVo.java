package com.kt.ipms.legacy.opermgmt.tacsmgmt.vo;

import java.io.Serializable;
import java.math.BigInteger;

import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class TbTacsConnBasVo extends CommonVo implements Serializable {

	private static final long serialVersionUID = -4071851746205110354L;
	
	private BigInteger ntacsConnBasSeq;
	
	private String connId;
	
	private String connPw;
	
	private String connOgwIp;
	
	private Integer connOgwPort;
	
	private String connMacAddr;
	
	private String connServerId;
	
	private String smailAddress;					// 메일주소
	
	public BigInteger getNtacsConnBasSeq() {
		return ntacsConnBasSeq;
	}

	public void setNtacsConnBasSeq(BigInteger ntacsConnBasSeq) {
		this.ntacsConnBasSeq = ntacsConnBasSeq;
	}

	public String getConnId() {
		return connId;
	}

	public void setConnId(String connId) {
		this.connId = connId;
	}

	public String getConnPw() {
		return connPw;
	}

	public void setConnPw(String connPw) {
		this.connPw = connPw;
	}

	public String getConnOgwIp() {
		return connOgwIp;
	}

	public void setConnOgwIp(String connOgwIp) {
		this.connOgwIp = connOgwIp;
	}

	public Integer getConnOgwPort() {
		return connOgwPort;
	}

	public void setConnOgwPort(Integer connOgwPort) {
		this.connOgwPort = connOgwPort;
	}

	public String getConnMacAddr() {
		return connMacAddr;
	}

	public void setConnMacAddr(String connMacAddr) {
		this.connMacAddr = connMacAddr;
	}

	public String getConnServerId() {
		return connServerId;
	}

	public void setConnServerId(String connServerId) {
		this.connServerId = connServerId;
	}

	public String getSmailAddress() {
		return smailAddress;
	}

	public void setSmailAddress(String smailAddress) {
		this.smailAddress = smailAddress;
	}

}
