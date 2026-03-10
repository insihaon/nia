package com.kt.ipms.legacy.opermgmt.menumgmt.vo;
import java.io.Serializable;
import com.kt.ipms.legacy.cmn.vo.CommonVo;


public class TbScrnBasVo extends CommonVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = 2500123119538357266L;

	private String sscrnId;

	private String sscrnNm;

	private String sscrnTypeCd;
	
	private String sscrnTypeNm;

	private String sscrnUrlTypeCd;
	
	private String sscrnUrlTypeNm;

	private String sscrnUrlAdr;

	private String scomnWrkjobScrnYn;

	private String sdgnScrnId;

	private String sscrnUseYn;
	

	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/

	public void setSscrnId(String sscrnId) {
		this.sscrnId = sscrnId;
	}

	public String getSscrnId() {
		return sscrnId;
	}

	public void setSscrnNm(String sscrnNm) {
		this.sscrnNm = sscrnNm;
	}

	public String getSscrnNm() {
		return sscrnNm;
	}

	public void setSscrnTypeCd(String sscrnTypeCd) {
		this.sscrnTypeCd = sscrnTypeCd;
	}

	public String getSscrnTypeCd() {
		return sscrnTypeCd;
	}
	
	public String getSscrnTypeNm() {
		return sscrnTypeNm;
	}

	public void setSscrnTypeNm(String sscrnTypeNm) {
		this.sscrnTypeNm = sscrnTypeNm;
	}

	public void setSscrnUrlTypeCd(String sscrnUrlTypeCd) {
		this.sscrnUrlTypeCd = sscrnUrlTypeCd;
	}

	public String getSscrnUrlTypeCd() {
		return sscrnUrlTypeCd;
	}
	
	public String getSscrnUrlTypeNm() {
		return sscrnUrlTypeNm;
	}

	public void setSscrnUrlTypeNm(String sscrnUrlTypeNm) {
		this.sscrnUrlTypeNm = sscrnUrlTypeNm;
	}


	public void setSscrnUrlAdr(String sscrnUrlAdr) {
		this.sscrnUrlAdr = sscrnUrlAdr;
	}

	public String getSscrnUrlAdr() {
		return sscrnUrlAdr;
	}

	public void setScomnWrkjobScrnYn(String scomnWrkjobScrnYn) {
		this.scomnWrkjobScrnYn = scomnWrkjobScrnYn;
	}

	public String getScomnWrkjobScrnYn() {
		return scomnWrkjobScrnYn;
	}

	public void setSdgnScrnId(String sdgnScrnId) {
		this.sdgnScrnId = sdgnScrnId;
	}

	public String getSdgnScrnId() {
		return sdgnScrnId;
	}

	public void setSscrnUseYn(String sscrnUseYn) {
		this.sscrnUseYn = sscrnUseYn;
	}

	public String getSscrnUseYn() {
		return sscrnUseYn;
	}


	/** MEMBER METHOD DECLARATION END **/
}