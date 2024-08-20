package com.kt.ipms.legacy.linkmgmt.whois.model;


import com.kt.ipms.legacy.linkmgmt.whois.util.WhoisToXml;

/**
 * Whois Response Object
 *
 */
public class WhoisResObj {

	public String reqType;			// WHOIS 신청서 종류 (NEW/ADD/MOD/RTN/CHECK/INFO)
	public String clTRID;			// Client Transaction Id
	public short resCode;			// Response Code
	public String resMessage;		// Response Message
	public WhoisToXml respObj;
	
	public String getReqType() {
		return reqType;
	}
	public void setReqType(String reqType) {
		this.reqType = reqType;
	}
	public String getClTRID() {
		return clTRID;
	}
	public void setClTRID(String clTRID) {
		this.clTRID = clTRID;
	}
	public short getResCode() {
		return resCode;
	}
	public void setResCode(short resCode) {
		this.resCode = resCode;
	}
	public String getResMessage() {
		return resMessage;
	}
	public void setResMessage(String resMessage) {
		this.resMessage = resMessage;
	}
	public WhoisToXml getRespObj() {
		return respObj;
	}
	public void setRespObj(WhoisToXml respObj) {
		this.respObj = respObj;
	} 
	
	
}
