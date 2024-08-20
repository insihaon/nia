package com.kt.ipms.legacy.opermgmt.usermgmt.vo;
import java.io.Serializable;
import com.kt.ipms.legacy.cmn.vo.CommonVo;
import java.math.BigInteger;
import java.util.Date;


public class TbUserBasVo extends CommonVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = -3120592476274800085L;

	private String suserId;

	private String suserNm;

	private String suserTypeCd;
	
	private String suserTypeNm;

	private Date duserRegDate;

	private String suserTelNo;

	private String suserFaxNo;

	private String suserMphonNo;

	private String suserEmailAdr;

	private BigInteger nloginFailTmscnt;

	private String schrMsgRcvYn;

	private String smphonOttpYn;

	private String suserSttusCd;
	
	private String suserSttusNm;

	private String sposDeptOrgId;
	
	private String sposDeptOrgNm;
	
	private String sposDeptFullNm;

	private String sdibelUclsUserId;
	
	private String sdibelUclsUserNm; 

	private String sktTkcgUserId;
	
	private String sktTkcgUserNm;

	private String suserRmark;

	private String soldUserId;

	private String sipmsConnYn;

	private String sipmsUserYn;

	private String suserGradeCd;
	
	private String suserGradeNm;
	
	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/
	public String getSuserId() {
		return suserId;
	}

	public void setSuserId(String suserId) {
		this.suserId = suserId;
	}

	public String getSuserNm() {
		return suserNm;
	}

	public void setSuserNm(String suserNm) {
		this.suserNm = suserNm;
	}

	public String getSuserTypeCd() {
		return suserTypeCd;
	}

	public void setSuserTypeCd(String suserTypeCd) {
		this.suserTypeCd = suserTypeCd;
	}

	public String getSuserTypeNm() {
		return suserTypeNm;
	}

	public void setSuserTypeNm(String suserTypeNm) {
		this.suserTypeNm = suserTypeNm;
	}

	public Date getDuserRegDate() {
		return duserRegDate;
	}

	public void setDuserRegDate(Date duserRegDate) {
		this.duserRegDate = duserRegDate;
	}

	public String getSuserTelNo() {
		return suserTelNo;
	}

	public void setSuserTelNo(String suserTelNo) {
		this.suserTelNo = suserTelNo;
	}

	public String getSuserFaxNo() {
		return suserFaxNo;
	}

	public void setSuserFaxNo(String suserFaxNo) {
		this.suserFaxNo = suserFaxNo;
	}

	public String getSuserMphonNo() {
		return suserMphonNo;
	}

	public void setSuserMphonNo(String suserMphonNo) {
		this.suserMphonNo = suserMphonNo;
	}

	public String getSuserEmailAdr() {
		return suserEmailAdr;
	}

	public void setSuserEmailAdr(String suserEmailAdr) {
		this.suserEmailAdr = suserEmailAdr;
	}

	public BigInteger getNloginFailTmscnt() {
		return nloginFailTmscnt;
	}

	public void setNloginFailTmscnt(BigInteger nloginFailTmscnt) {
		this.nloginFailTmscnt = nloginFailTmscnt;
	}

	public String getSchrMsgRcvYn() {
		return schrMsgRcvYn;
	}

	public void setSchrMsgRcvYn(String schrMsgRcvYn) {
		this.schrMsgRcvYn = schrMsgRcvYn;
	}

	public String getSmphonOttpYn() {
		return smphonOttpYn;
	}

	public void setSmphonOttpYn(String smphonOttpYn) {
		this.smphonOttpYn = smphonOttpYn;
	}

	public String getSuserSttusCd() {
		return suserSttusCd;
	}

	public void setSuserSttusCd(String suserSttusCd) {
		this.suserSttusCd = suserSttusCd;
	}

	public String getSuserSttusNm() {
		return suserSttusNm;
	}

	public void setSuserSttusNm(String suserSttusNm) {
		this.suserSttusNm = suserSttusNm;
	}

	public String getSposDeptOrgId() {
		return sposDeptOrgId;
	}

	public void setSposDeptOrgId(String sposDeptOrgId) {
		this.sposDeptOrgId = sposDeptOrgId;
	}
	
	public String getSposDeptOrgNm() {
		return sposDeptOrgNm;
	}

	public void setSposDeptOrgNm(String sposDeptOrgNm) {
		this.sposDeptOrgNm = sposDeptOrgNm;
	}

	
	public String getSposDeptFullNm() {
		return sposDeptFullNm;
	}

	public void setSposDeptFullNm(String sposDeptFullNm) {
		this.sposDeptFullNm = sposDeptFullNm;
	}

	public String getSdibelUclsUserId() {
		return sdibelUclsUserId;
	}

	public void setSdibelUclsUserId(String sdibelUclsUserId) {
		this.sdibelUclsUserId = sdibelUclsUserId;
	}

	public String getSktTkcgUserId() {
		return sktTkcgUserId;
	}

	public void setSktTkcgUserId(String sktTkcgUserId) {
		this.sktTkcgUserId = sktTkcgUserId;
	}

	public String getSuserRmark() {
		return suserRmark;
	}

	public void setSuserRmark(String suserRmark) {
		this.suserRmark = suserRmark;
	}

	public String getSoldUserId() {
		return soldUserId;
	}

	public void setSoldUserId(String soldUserId) {
		this.soldUserId = soldUserId;
	}

	public String getSipmsConnYn() {
		return sipmsConnYn;
	}

	public void setSipmsConnYn(String sipmsConnYn) {
		this.sipmsConnYn = sipmsConnYn;
	}

	public String getSipmsUserYn() {
		return sipmsUserYn;
	}

	public void setSipmsUserYn(String sipmsUserYn) {
		this.sipmsUserYn = sipmsUserYn;
	}

	public String getSuserGradeCd() {
		return suserGradeCd;
	}

	public void setSuserGradeCd(String suserGradeCd) {
		this.suserGradeCd = suserGradeCd;
	}

	public String getSuserGradeNm() {
		return suserGradeNm;
	}

	public void setSuserGradeNm(String suserGradeNm) {
		this.suserGradeNm = suserGradeNm;
	}

	public String getSdibelUclsUserNm() {
		return sdibelUclsUserNm;
	}

	public void setSdibelUclsUserNm(String sdibelUclsUserNm) {
		this.sdibelUclsUserNm = sdibelUclsUserNm;
	}

	public String getSktTkcgUserNm() {
		return sktTkcgUserNm;
	}

	public void setSktTkcgUserNm(String sktTkcgUserNm) {
		this.sktTkcgUserNm = sktTkcgUserNm;
	}
	
	
	/** MEMBER METHOD DECLARATION END **/
}