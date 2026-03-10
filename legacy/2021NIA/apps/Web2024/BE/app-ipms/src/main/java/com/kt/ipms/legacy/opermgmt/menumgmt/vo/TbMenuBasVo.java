package com.kt.ipms.legacy.opermgmt.menumgmt.vo;
import java.io.Serializable;

import com.kt.ipms.legacy.cmn.vo.CommonVo;


public class TbMenuBasVo extends CommonVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = 368138644429881907L;

	private String smenuId;
	
	private String sUpMenuId;
	
	private String sUpMenuNm;

	private String smenuNm;

	private String smenuHierTypeCd;

	private Integer nmenuLvlSeq;

	private Integer nmenuIndcOdrg;

	private String smenuDescSbst;

	private String sscrnId;
	
	private String sscrnNm;
	
	private String sscrnUrlAdr;
	
	private String sscrnUseYn;

	private String smenuGroupId;

	private String smenuUseYn;
	
	private String suserGradeCd;
	
	private String scomment;

	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/

	public void setSmenuId(String smenuId) {
		this.smenuId = smenuId;
	}

	public String getSmenuId() {
		return smenuId;
	}	

	public String getsUpMenuId() {
		return sUpMenuId;
	}

	public void setsUpMenuId(String sUpMenuId) {
		this.sUpMenuId = sUpMenuId;
	}
	
	public String getsUpMenuNm() {
		return sUpMenuNm;
	}

	public void setsUpMenuNm(String sUpMenuNm) {
		this.sUpMenuNm = sUpMenuNm;
	}

	public void setSmenuNm(String smenuNm) {
		this.smenuNm = smenuNm;
	}

	public String getSmenuNm() {
		return smenuNm;
	}

	public void setSmenuHierTypeCd(String smenuHierTypeCd) {
		this.smenuHierTypeCd = smenuHierTypeCd;
	}

	public String getSmenuHierTypeCd() {
		return smenuHierTypeCd;
	}

	public void setNmenuLvlSeq(Integer nmenuLvlSeq) {
		this.nmenuLvlSeq = nmenuLvlSeq;
	}

	public Integer getNmenuLvlSeq() {
		return nmenuLvlSeq;
	}

	public void setNmenuIndcOdrg(Integer nmenuIndcOdrg) {
		this.nmenuIndcOdrg = nmenuIndcOdrg;
	}

	public Integer getNmenuIndcOdrg() {
		return nmenuIndcOdrg;
	}

	public void setSmenuDescSbst(String smenuDescSbst) {
		this.smenuDescSbst = smenuDescSbst;
	}

	public String getSmenuDescSbst() {
		return smenuDescSbst;
	}

	public void setSscrnId(String sscrnId) {
		this.sscrnId = sscrnId;
	}

	public String getSscrnId() {
		return sscrnId;
	}
		
	public String getSscrnNm() {
		return sscrnNm;
	}

	public void setSscrnNm(String sscrnNm) {
		this.sscrnNm = sscrnNm;
	}

	public String getSscrnUrlAdr() {
		return sscrnUrlAdr;
	}

	public void setSscrnUrlAdr(String sscrnUrlAdr) {
		this.sscrnUrlAdr = sscrnUrlAdr;
	}

	public String getSscrnUseYn() {
		return sscrnUseYn;
	}

	public void setSscrnUseYn(String sscrnUseYn) {
		this.sscrnUseYn = sscrnUseYn;
	}

	public void setSmenuGroupId(String smenuGroupId) {
		this.smenuGroupId = smenuGroupId;
	}

	public String getSmenuGroupId() {
		return smenuGroupId;
	}

	public void setSmenuUseYn(String smenuUseYn) {
		this.smenuUseYn = smenuUseYn;
	}

	public String getSmenuUseYn() {
		return smenuUseYn;
	}

	public String getSuserGradeCd() {
		return suserGradeCd;
	}

	public void setSuserGradeCd(String suserGradeCd) {
		this.suserGradeCd = suserGradeCd;
	}

	public String getScomment() {
		return scomment;
	}

	public void setScomment(String scomment) {
		this.scomment = scomment;
	}

	
	/** MEMBER METHOD DECLARATION END **/
}