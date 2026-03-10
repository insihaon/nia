package com.kt.ipms.legacy.opermgmt.menumgmt.vo;
import java.io.Serializable;
import com.kt.ipms.legacy.cmn.vo.CommonVo;


public class TbMenuAuthVo extends CommonVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = -2365487991404345449L;

	private String suserGradeCd;
	
	private String suserGradeNm;

	private String smenuId;
	
	private String smenuNm;
	
	private String sComment;

	private String smenuAutUseYn;
	
	private String sscrnId;
	
	private String sscrnUrlAdr;

	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/

	public void setSuserGradeCd(String suserGradeCd) {
		this.suserGradeCd = suserGradeCd;
	}

	public String getSuserGradeCd() {
		return suserGradeCd;
	}

	public void setSmenuId(String smenuId) {
		this.smenuId = smenuId;
	}

	public String getSmenuId() {
		return smenuId;
	}

	public void setSmenuAutUseYn(String smenuAutUseYn) {
		this.smenuAutUseYn = smenuAutUseYn;
	}

	public String getSmenuAutUseYn() {
		return smenuAutUseYn;
	}

	public String getSuserGradeNm() {
		return suserGradeNm;
	}

	public void setSuserGradeNm(String suserGradeNm) {
		this.suserGradeNm = suserGradeNm;
	}

	public String getSmenuNm() {
		return smenuNm;
	}

	public void setSmenuNm(String smenuNm) {
		this.smenuNm = smenuNm;
	}

	public String getsComment() {
		return sComment;
	}

	public void setsComment(String sComment) {
		this.sComment = sComment;
	}

	public String getSscrnId() {
		return sscrnId;
	}

	public void setSscrnId(String sscrnId) {
		this.sscrnId = sscrnId;
	}

	public String getSscrnUrlAdr() {
		return sscrnUrlAdr;
	}

	public void setSscrnUrlAdr(String sscrnUrlAdr) {
		this.sscrnUrlAdr = sscrnUrlAdr;
	}
	
	

	/** MEMBER METHOD DECLARATION END **/
}