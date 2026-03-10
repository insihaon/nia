package com.kt.ipms.legacy.ticketmgmt.configmgmt.vo;
import java.io.Serializable;
import com.kt.ipms.legacy.cmn.vo.CommonVo;
import java.math.BigInteger;


public class TbConfigLinkMstVo extends CommonVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = -499494131788002710L;

	private BigInteger nconfigLinkMstSeq;

	private String saOfficecode;

	private String saHostIp;

	private String saHostNm;

	private String saIfIp;

	private String paIfInet;

	private String saIfNm;

	private String saIfDescription;

	private Integer naFlagLevel;

	private String sgOfficecode;

	private String sgHostIp;

	private String sgHostNm;

	private String sgIfIp;

	private String pgIfInet;

	private String sgIfNm;

	private String sgIfDescription;

	private Integer ngFlagLevel;

	private String scomment;

	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/

	public void setNconfigLinkMstSeq(BigInteger nconfigLinkMstSeq) {
		this.nconfigLinkMstSeq = nconfigLinkMstSeq;
	}

	public BigInteger getNconfigLinkMstSeq() {
		return nconfigLinkMstSeq;
	}

	public void setSaOfficecode(String saOfficecode) {
		this.saOfficecode = saOfficecode;
	}

	public String getSaOfficecode() {
		return saOfficecode;
	}

	public void setSaHostIp(String saHostIp) {
		this.saHostIp = saHostIp;
	}

	public String getSaHostIp() {
		return saHostIp;
	}

	public void setSaHostNm(String saHostNm) {
		this.saHostNm = saHostNm;
	}

	public String getSaHostNm() {
		return saHostNm;
	}

	public void setSaIfIp(String saIfIp) {
		this.saIfIp = saIfIp;
	}

	public String getSaIfIp() {
		return saIfIp;
	}

	public void setPaIfInet(String paIfInet) {
		this.paIfInet = paIfInet;
	}

	public String getPaIfInet() {
		return paIfInet;
	}

	public void setSaIfNm(String saIfNm) {
		this.saIfNm = saIfNm;
	}

	public String getSaIfNm() {
		return saIfNm;
	}

	public void setSaIfDescription(String saIfDescription) {
		this.saIfDescription = saIfDescription;
	}

	public String getSaIfDescription() {
		return saIfDescription;
	}

	public void setNaFlagLevel(Integer naFlagLevel) {
		this.naFlagLevel = naFlagLevel;
	}

	public Integer getNaFlagLevel() {
		return naFlagLevel;
	}

	public void setSgOfficecode(String sgOfficecode) {
		this.sgOfficecode = sgOfficecode;
	}

	public String getSgOfficecode() {
		return sgOfficecode;
	}

	public void setSgHostIp(String sgHostIp) {
		this.sgHostIp = sgHostIp;
	}

	public String getSgHostIp() {
		return sgHostIp;
	}

	public void setSgHostNm(String sgHostNm) {
		this.sgHostNm = sgHostNm;
	}

	public String getSgHostNm() {
		return sgHostNm;
	}

	public void setSgIfIp(String sgIfIp) {
		this.sgIfIp = sgIfIp;
	}

	public String getSgIfIp() {
		return sgIfIp;
	}

	public void setPgIfInet(String pgIfInet) {
		this.pgIfInet = pgIfInet;
	}

	public String getPgIfInet() {
		return pgIfInet;
	}

	public void setSgIfNm(String sgIfNm) {
		this.sgIfNm = sgIfNm;
	}

	public String getSgIfNm() {
		return sgIfNm;
	}

	public void setSgIfDescription(String sgIfDescription) {
		this.sgIfDescription = sgIfDescription;
	}

	public String getSgIfDescription() {
		return sgIfDescription;
	}

	public void setNgFlagLevel(Integer ngFlagLevel) {
		this.ngFlagLevel = ngFlagLevel;
	}

	public Integer getNgFlagLevel() {
		return ngFlagLevel;
	}

	public void setScomment(String scomment) {
		this.scomment = scomment;
	}

	public String getScomment() {
		return scomment;
	}

	/** MEMBER METHOD DECLARATION END **/
}