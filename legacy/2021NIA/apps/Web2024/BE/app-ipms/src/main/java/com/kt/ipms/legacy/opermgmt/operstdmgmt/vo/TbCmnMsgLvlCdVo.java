package com.kt.ipms.legacy.opermgmt.operstdmgmt.vo;

import java.io.Serializable;

import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class TbCmnMsgLvlCdVo extends CommonVo implements Serializable{
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = 5548383908857886132L;
	private String cmnMsgLvlSubCd;
	private String cmnMsgLvlNm;
	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/
	public String getCmnMsgLvlSubCd() {
		return cmnMsgLvlSubCd;
	}
	public void setCmnMsgLvlSubCd(String cmnMsgLvlSubCd) {
		this.cmnMsgLvlSubCd = cmnMsgLvlSubCd;
	}
	public String getCmnMsgLvlNm() {
		return cmnMsgLvlNm;
	}
	public void setCmnMsgLvlNm(String cmnMsgLvlNm) {
		this.cmnMsgLvlNm = cmnMsgLvlNm;
	}
	/** MEMBER METHOD DECLARATION END **/
}
