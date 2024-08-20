package com.kt.ipms.legacy.opermgmt.tacsmgmt.vo;
import java.io.Serializable;
import com.kt.ipms.legacy.cmn.vo.CommonVo;
import java.math.BigInteger;


public class TbTacsConnHistVo extends CommonVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = 8247149499355252568L;

	private BigInteger ntacsConnHistSeq;

	private String pipFcltInet;

	private String sfcltPromptNm;
	
	private String pipPrefix;
	
	private String savailYn;

	private Integer nresultCd;

	private String sresultMsg;

	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/

	public void setNtacsConnHistSeq(BigInteger ntacsConnHistSeq) {
		this.ntacsConnHistSeq = ntacsConnHistSeq;
	}

	public BigInteger getNtacsConnHistSeq() {
		return ntacsConnHistSeq;
	}

	public void setPipFcltInet(String pipFcltInet) {
		this.pipFcltInet = pipFcltInet;
	}

	public String getPipFcltInet() {
		return pipFcltInet;
	}

	public void setSfcltPromptNm(String sfcltPromptNm) {
		this.sfcltPromptNm = sfcltPromptNm;
	}

	public String getSfcltPromptNm() {
		return sfcltPromptNm;
	}

	public String getPipPrefix() {
		return pipPrefix;
	}

	public void setPipPrefix(String pipPrefix) {
		this.pipPrefix = pipPrefix;
	}

	public String getSavailYn() {
		return savailYn;
	}

	public void setSavailYn(String savailYn) {
		this.savailYn = savailYn;
	}

	public void setNresultCd(Integer nresultCd) {
		this.nresultCd = nresultCd;
	}

	public Integer getNresultCd() {
		return nresultCd;
	}

	public void setSresultMsg(String sresultMsg) {
		this.sresultMsg = sresultMsg;
	}

	public String getSresultMsg() {
		return sresultMsg;
	}

	/** MEMBER METHOD DECLARATION END **/
}