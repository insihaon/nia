package com.kt.ipms.legacy.opermgmt.asmgmt.vo;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class TbRequestAsApyTxnVo extends CommonVo implements Serializable{
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = 8209884190396207282L;
	
	private BigInteger nrequestAsApyTxnSeq;
	
	private BigInteger nrequestAsSeq;
	
	private String srequestAsCtm;
	
	private String srequestAsObjNm1;
	
	private String srequestAsObjLlnum1;
	
	private Date drequestAsObjOpenDt1;
	
	private String srequestAsObjIpBlock1;
	
	private String srequestAsObjNm2;
	
	private String srequestAsObjLlnum2;
	
	private Date drequestAsObjOpenDt2;
	
	private String srequestAsObjIpBlock2;
	
	private String srequestAsTypeCd;
	
	private String srequestAsTypeNm;
	
	private String sasTpicNm;
	
	private String sasTpicOrg;
	
	private String sexistSpLine;
	
	private BigInteger nexistSales;
	
	private String saltSpLine;
	
	private BigInteger naltSales;
	
	private Date dapvDt;
	
	private String sapvuserId;
	
	private String sapvuserNm;
	
	private String scomment;
	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/
	public BigInteger getNrequestAsApyTxnSeq() {
		return nrequestAsApyTxnSeq;
	}

	public void setNrequestAsApyTxnSeq(BigInteger nrequestAsApyTxnSeq) {
		this.nrequestAsApyTxnSeq = nrequestAsApyTxnSeq;
	}

	public BigInteger getNrequestAsSeq() {
		return nrequestAsSeq;
	}

	public void setNrequestAsSeq(BigInteger nrequestAsSeq) {
		this.nrequestAsSeq = nrequestAsSeq;
	}

	public String getSrequestAsCtm() {
		return srequestAsCtm;
	}

	public void setSrequestAsCtm(String srequestAsCtm) {
		this.srequestAsCtm = srequestAsCtm;
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

	public Date getDrequestAsObjOpenDt1() {
		return drequestAsObjOpenDt1;
	}

	public void setDrequestAsObjOpenDt1(Date drequestAsObjOpenDt1) {
		this.drequestAsObjOpenDt1 = drequestAsObjOpenDt1;
	}

	public String getSrequestAsObjIpBlock1() {
		return srequestAsObjIpBlock1;
	}

	public void setSrequestAsObjIpBlock1(String srequestAsObjIpBlock1) {
		this.srequestAsObjIpBlock1 = srequestAsObjIpBlock1;
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

	public Date getDrequestAsObjOpenDt2() {
		return drequestAsObjOpenDt2;
	}

	public void setDrequestAsObjOpenDt2(Date drequestAsObjOpenDt2) {
		this.drequestAsObjOpenDt2 = drequestAsObjOpenDt2;
	}

	public String getSrequestAsObjIpBlock2() {
		return srequestAsObjIpBlock2;
	}

	public void setSrequestAsObjIpBlock2(String srequestAsObjIpBlock2) {
		this.srequestAsObjIpBlock2 = srequestAsObjIpBlock2;
	}

	public String getSrequestAsTypeCd() {
		return srequestAsTypeCd;
	}

	public void setSrequestAsTypeCd(String srequestAsTypeCd) {
		this.srequestAsTypeCd = srequestAsTypeCd;
	}

	public String getSasTpicNm() {
		return sasTpicNm;
	}

	public void setSasTpicNm(String sasTpicNm) {
		this.sasTpicNm = sasTpicNm;
	}

	public String getSasTpicOrg() {
		return sasTpicOrg;
	}

	public void setSasTpicOrg(String sasTpicOrg) {
		this.sasTpicOrg = sasTpicOrg;
	}

	public String getSexistSpLine() {
		return sexistSpLine;
	}

	public void setSexistSpLine(String sexistSpLine) {
		this.sexistSpLine = sexistSpLine;
	}

	public BigInteger getNexistSales() {
		return nexistSales;
	}

	public void setNexistSales(BigInteger nexistSales) {
		this.nexistSales = nexistSales;
	}

	public String getSaltSpLine() {
		return saltSpLine;
	}

	public void setSaltSpLine(String saltSpLine) {
		this.saltSpLine = saltSpLine;
	}

	public BigInteger getNaltSales() {
		return naltSales;
	}

	public void setNaltSales(BigInteger naltSales) {
		this.naltSales = naltSales;
	}

	public Date getDapvDt() {
		return dapvDt;
	}

	public void setDapvDt(Date dapvDt) {
		this.dapvDt = dapvDt;
	}

	public String getSapvuserId() {
		return sapvuserId;
	}

	public void setSapvuserId(String sapvuserId) {
		this.sapvuserId = sapvuserId;
	}

	public String getScomment() {
		return scomment;
	}

	public void setScomment(String scomment) {
		this.scomment = scomment;
	}

	public String getSrequestAsTypeNm() {
		return srequestAsTypeNm;
	}

	public void setSrequestAsTypeNm(String srequestAsTypeNm) {
		this.srequestAsTypeNm = srequestAsTypeNm;
	}

	public String getSapvuserNm() {
		return sapvuserNm;
	}

	public void setSapvuserNm(String sapvuserNm) {
		this.sapvuserNm = sapvuserNm;
	}
	
	/** MEMBER VARIABLE DECLARATION START **/
	
	
}
