package com.kt.ipms.legacy.linkmgmt.socketmgmt.vo;

import java.io.Serializable;
import java.net.InetAddress;

public class SocketInfoVo implements Serializable {

	private static final long serialVersionUID = -1006158425551043395L;
	
	private InetAddress clientIp;
	
	private int clinetPort;
	
	private int clientTimeout = 10000;
	
	private String requestUrl;
	
	public InetAddress getClientIp() {
		return clientIp;
	}

	public void setClientIp(InetAddress clientIp) {
		this.clientIp = clientIp;
	}

	public int getClinetPort() {
		return clinetPort;
	}

	public void setClinetPort(int clinetPort) {
		this.clinetPort = clinetPort;
	}

	public int getClientTimeout() {
		return clientTimeout;
	}

	public void setClientTimeout(int clientTimeout) {
		this.clientTimeout = clientTimeout;
	}

	public String getRequestUrl() {
		return requestUrl;
	}

	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}

}
