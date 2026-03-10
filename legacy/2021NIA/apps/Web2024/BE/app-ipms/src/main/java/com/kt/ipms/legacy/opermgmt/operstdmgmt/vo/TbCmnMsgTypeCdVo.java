package com.kt.ipms.legacy.opermgmt.operstdmgmt.vo;

import java.io.Serializable;

import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class TbCmnMsgTypeCdVo extends CommonVo implements Serializable{
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = 3818190315860929721L;
	private String cmnMsgTypeSubCd;
	private String cmnMsgTypeNm;
	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/
	public String getCmnMsgTypeSubCd() {
		return cmnMsgTypeSubCd;
	}
	public void setCmnMsgTypeSubCd(String cmnMsgTypeSubCd) {
		this.cmnMsgTypeSubCd = cmnMsgTypeSubCd;
	}
	public String getCmnMsgTypeNm() {
		return cmnMsgTypeNm;
	}
	public void setCmnMsgTypeNm(String cmnMsgTypeNm) {
		this.cmnMsgTypeNm = cmnMsgTypeNm;
	}
	/** MEMBER METHOD DECLARATION END **/
}
