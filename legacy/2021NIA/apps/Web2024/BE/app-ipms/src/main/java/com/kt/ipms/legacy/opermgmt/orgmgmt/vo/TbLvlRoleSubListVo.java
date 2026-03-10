package com.kt.ipms.legacy.opermgmt.orgmgmt.vo;
import java.io.Serializable;
import java.util.List;

import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class TbLvlRoleSubListVo extends CommonVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = 7465671817047920002L;
	
	private String bfssvcLineTypeCd;

	private String bfssvcObjCd;

	private String bfssvcLineTypeNm;

	private String bfssvcObjNm;

	private String bfssvcGroupNm;
	
	private String bfssvchighObjNm;
	
	private List<TbLvlRoleSubVo> tbLvlRoleSubVos;
	
	private int totalCount;
	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/
	
	
	public List<TbLvlRoleSubVo> getTbLvlRoleSubVos() {
		return this.tbLvlRoleSubVos;
	}
	
	public String getBfssvcLineTypeCd() {
		return bfssvcLineTypeCd;
	}

	public void setBfssvcLineTypeCd(String bfssvcLineTypeCd) {
		this.bfssvcLineTypeCd = bfssvcLineTypeCd;
	}

	public String getBfssvcObjCd() {
		return bfssvcObjCd;
	}

	public void setBfssvcObjCd(String bfssvcObjCd) {
		this.bfssvcObjCd = bfssvcObjCd;
	}

	public void setTbLvlRoleSubVos(List<TbLvlRoleSubVo> tbLvlRoleSubVos) {
		this.tbLvlRoleSubVos = tbLvlRoleSubVos;
	}
	
	public int getTotalCount() {
		return this.totalCount;
	}
	
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public String getBfssvcLineTypeNm() {
		return bfssvcLineTypeNm;
	}

	public void setBfssvcLineTypeNm(String bfssvcLineTypeNm) {
		this.bfssvcLineTypeNm = bfssvcLineTypeNm;
	}

	public String getBfssvcObjNm() {
		return bfssvcObjNm;
	}

	public void setBfssvcObjNm(String bfssvcObjNm) {
		this.bfssvcObjNm = bfssvcObjNm;
	}

	public String getBfssvchighObjNm() {
		return bfssvchighObjNm;
	}

	public void setBfssvchighObjNm(String bfssvchighObjNm) {
		this.bfssvchighObjNm = bfssvchighObjNm;
	}

	public String getBfssvcGroupNm() {
		return bfssvcGroupNm;
	}

	public void setBfssvcGroupNm(String bfssvcGroupNm) {
		this.bfssvcGroupNm = bfssvcGroupNm;
	}
	
	/** MEMBER METHOD DECLARATION END **/
}