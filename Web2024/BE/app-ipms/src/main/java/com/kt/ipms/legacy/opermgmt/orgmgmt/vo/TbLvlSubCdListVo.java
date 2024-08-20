package com.kt.ipms.legacy.opermgmt.orgmgmt.vo;
import java.io.Serializable;
import java.util.List;

import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class TbLvlSubCdListVo extends CommonVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = -6472626008059002008L;
	
	private String bfssvcLineTypeCd;

	private String bfslvlCd;

	private String bfssvcLineTypeNm;

	private String bfslvlNm;

	private String bfslvlGroupNm;
	
	private String bfslvlHighNm;
	
	private List<TbLvlSubCdVo> tbLvlSubCdVos;
	
	private int totalCount;
	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/
	
	
	public List<TbLvlSubCdVo> getTbLvlSubCdVos() {
		return this.tbLvlSubCdVos;
	}
	
	public String getBfssvcLineTypeCd() {
		return bfssvcLineTypeCd;
	}

	public void setBfssvcLineTypeCd(String bfssvcLineTypeCd) {
		this.bfssvcLineTypeCd = bfssvcLineTypeCd;
	}

	public String getBfslvlCd() {
		return bfslvlCd;
	}

	public void setBfslvlCd(String bfslvlCd) {
		this.bfslvlCd = bfslvlCd;
	}

	public String getBfssvcLineTypeNm() {
		return bfssvcLineTypeNm;
	}

	public void setBfssvcLineTypeNm(String bfssvcLineTypeNm) {
		this.bfssvcLineTypeNm = bfssvcLineTypeNm;
	}

	public String getBfslvlNm() {
		return bfslvlNm;
	}

	public void setBfslvlNm(String bfslvlNm) {
		this.bfslvlNm = bfslvlNm;
	}

	public String getBfslvlGroupNm() {
		return bfslvlGroupNm;
	}

	public void setBfslvlGroupNm(String bfslvlGroupNm) {
		this.bfslvlGroupNm = bfslvlGroupNm;
	}

	public String getBfslvlHighNm() {
		return bfslvlHighNm;
	}

	public void setBfslvlHighNm(String bfslvlHighNm) {
		this.bfslvlHighNm = bfslvlHighNm;
	}

	public void setTbLvlSubCdVos(List<TbLvlSubCdVo> tbLvlSubCdVos) {
		this.tbLvlSubCdVos = tbLvlSubCdVos;
	}
	
	public int getTotalCount() {
		return this.totalCount;
	}
	
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	/** MEMBER METHOD DECLARATION END **/
}