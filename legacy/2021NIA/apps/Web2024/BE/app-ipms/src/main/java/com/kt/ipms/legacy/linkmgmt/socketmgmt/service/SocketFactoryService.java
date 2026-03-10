package com.kt.ipms.legacy.linkmgmt.socketmgmt.service;

import java.io.IOException;
import java.net.Socket;

import org.springframework.stereotype.Component;

import com.kt.ipms.legacy.cmn.util.PrintLogUtil;
import com.kt.ipms.legacy.linkmgmt.socketmgmt.vo.SocketInfoVo;

@Component
public class SocketFactoryService {
	
	public Socket createSocket(SocketInfoVo socketInfoVo) throws IOException {
		PrintLogUtil.printLog("WHOIS SOCKET CREATE START");
		Socket socket = new Socket(socketInfoVo.getClientIp(), socketInfoVo.getClinetPort());
		socket.setKeepAlive(true);
		socket.setSoTimeout(socketInfoVo.getClientTimeout());
		PrintLogUtil.printLog("WHOIS SOCKET CREATE END");
		return socket;
	}
	
	public Socket createSocketNoRtn(SocketInfoVo socketInfoVo) throws IOException {
		Socket socket = null;
		
		try {
			PrintLogUtil.printLog("WHOIS SOCKET CREATE START");
			socket = new Socket(socketInfoVo.getClientIp(), socketInfoVo.getClinetPort());
			socket.setKeepAlive(true);
			socket.setSoTimeout(socketInfoVo.getClientTimeout());
			PrintLogUtil.printLog("WHOIS SOCKET CREATE END");
		} catch (Exception e) {
			// TODO: handle exception
			PrintLogUtil.printLog(e.getMessage());
		}
		
		return socket;
	}
}

