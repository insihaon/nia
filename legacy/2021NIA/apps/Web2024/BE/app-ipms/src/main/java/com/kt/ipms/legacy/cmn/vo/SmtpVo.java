package com.kt.ipms.legacy.cmn.vo;

import java.io.Serializable;
import java.util.Date;

public class SmtpVo extends CommonVo implements Serializable{

	private static final long serialVersionUID = 3485362919441037987L;
	
	private String mailType;		// 메일 유형
	
	private String fromEmail;		// 발신자 메일주소
	
	private String toEmail;			// 수신자 메일주소
	
	private String subject;		// 메일 제목
	
	private String message;		// 메일내용
	
	private Date sendDate;		// 메일 전송일자
	
	private String result;			// 전송결과
	
	private String userID;		// 사용자
	
	private String searchIp;		// 사용자 검색 IP
	
	private String searchFirstIp;		// 사용자 검색 시작IP
	
	private String searchLastIp;		// 사용자 검색 끝IP
	
	private String whoisRtnMsg;		// Whois 정보 조회 후 반송 메세지

	public String getFromEmail() {
		return fromEmail;
	}

	public void setFromEmail(String fromEmail) {
		this.fromEmail = fromEmail;
	}

	public String getToEmail() {
		return toEmail;
	}

	public void setToEmail(String toEmail) {
		this.toEmail = toEmail;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getSendDate() {
		return sendDate;
	}

	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMailType() {
		return mailType;
	}

	public void setMailType(String mailType) {
		this.mailType = mailType;
	}

	public String getSearchIp() {
		return searchIp;
	}

	public void setSearchIp(String searchIp) {
		this.searchIp = searchIp;
	}

	public String getSearchLastIp() {
		return searchLastIp;
	}

	public void setSearchLastIp(String searchLastIp) {
		this.searchLastIp = searchLastIp;
	}

	public String getSearchFirstIp() {
		return searchFirstIp;
	}

	public void setSearchFirstIp(String searchFirstIp) {
		this.searchFirstIp = searchFirstIp;
	}

	public String getWhoisRtnMsg() {
		return whoisRtnMsg;
	}

	public void setWhoisRtnMsg(String whoisRtnMsg) {
		this.whoisRtnMsg = whoisRtnMsg;
	}
	
	
	
	

}
