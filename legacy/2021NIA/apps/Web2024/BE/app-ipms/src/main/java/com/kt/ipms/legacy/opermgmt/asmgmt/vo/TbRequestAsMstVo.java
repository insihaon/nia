package com.kt.ipms.legacy.opermgmt.asmgmt.vo;
import java.io.Serializable;

import com.kt.ipms.legacy.cmn.vo.CommonVo;

import java.math.BigInteger;
import java.util.Date;


public class TbRequestAsMstVo extends CommonVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = -2807854438158546005L;

	private BigInteger nrequestAsSeq;

	private String srequestAsTypeCd;

	private String srequestAsTypeNm;

	private String scomment;

	private String srequestAsCtm; //고객명

	private Date apyDt; //신청일

	private String apyUserId; //신청자ID
	
	private String apyUserNm; //신청자명

	private String srequestAsObjNm1;
		
	private String srequestAsObjLlnum1;

	private String srequestAsObjNm2;
		
	private String srequestAsObjLlnum2;
	
	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/

	public void setNrequestAsSeq(BigInteger nrequestAsSeq) {
		this.nrequestAsSeq = nrequestAsSeq;
	}

	public BigInteger getNrequestAsSeq() {
		return nrequestAsSeq;
	}

	public void setSrequestAsTypeCd(String srequestAsTypeCd) {
		this.srequestAsTypeCd = srequestAsTypeCd;
	}

	public String getSrequestAsTypeCd() {
		return srequestAsTypeCd;
	}

	public void setSrequestAsTypeNm(String srequestAsTypeNm) {
		this.srequestAsTypeNm = srequestAsTypeNm;
	}

	public String getSrequestAsTypeNm() {
		return srequestAsTypeNm;
	}

	public void setScomment(String scomment) {
		this.scomment = scomment;
	}

	public String getScomment() {
		return scomment;
	}

	public String getSrequestAsCtm() {
		return srequestAsCtm;
	}

	public void setSrequestAsCtm(String srequestAsCtm) {
		this.srequestAsCtm = srequestAsCtm;
	}

	public Date getApyDt() {
		return apyDt;
	}

	public void setApyDt(Date apyDt) {
		this.apyDt = apyDt;
	}

	public String getApyUserId() {
		return apyUserId;
	}

	public void setApyUserId(String apyUserId) {
		this.apyUserId = apyUserId;
	}

	public String getApyUserNm() {
		return apyUserNm;
	}

	public void setApyUserNm(String apyUserNm) {
		this.apyUserNm = apyUserNm;
	}

	public String getSrequestAsObjNm1() {
		return srequestAsObjNm1;
	}

	public void setSrequestAsObjNm1(String srequestAsObjNm1) {
		this.srequestAsObjNm1 = srequestAsObjNm1;
	}

	public String getSrequestAsObjLlnum1() {
		return srequestAsObjLlnum1;
	}

	public void setSrequestAsObjLlnum1(String srequestAsObjLlnum1) {
		this.srequestAsObjLlnum1 = srequestAsObjLlnum1;
	}

	public String getSrequestAsObjNm2() {
		return srequestAsObjNm2;
	}

	public void setSrequestAsObjNm2(String srequestAsObjNm2) {
		this.srequestAsObjNm2 = srequestAsObjNm2;
	}

	public String getSrequestAsObjLlnum2() {
		return srequestAsObjLlnum2;
	}

	public void setSrequestAsObjLlnum2(String srequestAsObjLlnum2) {
		this.srequestAsObjLlnum2 = srequestAsObjLlnum2;
	}
	
	

	/** MEMBER METHOD DECLARATION END **/
}