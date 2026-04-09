package com.kt.ipms.legacy.opermgmt.tacsmgmt.vo;

import java.io.Serializable;
import java.util.List;

public class TacsRequestVo implements Serializable {

	private static final long serialVersionUID = -2793274358632554555L;
	
	private String targetIp;
	
	private Integer targetPort;
	
	private String ogwIp;
	
	private Integer ogwPort;
	
	private String userId;
	
	private String userPwd;
	
	private String macaddr;
	
	private String serverId;
	
	private String promptNm;
	
	private Boolean isSubConn;
	
	private Integer subConnCnt;
	
	private String pipPrefix;
	
	private List<TacsRequestCmdVo> cmdList;

	public String getTargetIp() {
		return targetIp;
	}

	public void setTargetIp(String targetIp) {
		this.targetIp = targetIp;
	}

	public String getOgwIp() {
		return ogwIp;
	}

	public Integer getTargetPort() {
		return targetPort;
	}

	public void setTargetPort(Integer targetPort) {
		this.targetPort = targetPort;
	}

	public void setOgwIp(String ogwIp) {
		this.ogwIp = ogwIp;
	}

	public Integer getOgwPort() {
		return ogwPort;
	}

	public void setOgwPort(Integer ogwPort) {
		this.ogwPort = ogwPort;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserPwd() {
		return userPwd;
	}

	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}

	public String getMacaddr() {
		return macaddr;
	}

	public void setMacaddr(String macaddr) {
		this.macaddr = macaddr;
	}

	public String getServerId() {
		return serverId;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	public String getPromptNm() {
		return promptNm;
	}

	public void setPromptNm(String promptNm) {
		this.promptNm = promptNm;
	}

	public Boolean getIsSubConn() {
		return isSubConn;
	}

	public void setIsSubConn(Boolean isSubConn) {
		this.isSubConn = isSubConn;
	}

	public Integer getSubConnCnt() {
		return subConnCnt;
	}

	public void setSubConnCnt(Integer subConnCnt) {
		this.subConnCnt = subConnCnt;
	}

	public List<TacsRequestCmdVo> getCmdList() {
		return cmdList;
	}

	public void setCmdList(List<TacsRequestCmdVo> cmdList) {
		this.cmdList = cmdList;
	}

	public String getPipPrefix() {
		return pipPrefix;
	}

	public void setPipPrefix(String pipPrefix) {
		this.pipPrefix = pipPrefix;
	}
	
}
