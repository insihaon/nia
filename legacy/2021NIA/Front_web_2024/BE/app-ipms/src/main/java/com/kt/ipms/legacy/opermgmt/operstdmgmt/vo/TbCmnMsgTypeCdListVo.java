package com.kt.ipms.legacy.opermgmt.operstdmgmt.vo;

import java.io.Serializable;
import java.util.List;

import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class TbCmnMsgTypeCdListVo extends CommonVo implements Serializable{
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = 5208100550587605199L;
	private List<TbCmnMsgTypeCdVo> tbCmnMsgTypeCdVos;
	private int totalCount;
	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/
	public List<TbCmnMsgTypeCdVo> getTbCmnMsgTypeCdVos() {
		return tbCmnMsgTypeCdVos;
	}
	public void setTbCmnMsgTypeCdVos(List<TbCmnMsgTypeCdVo> tbCmnMsgTypeVos) {
		this.tbCmnMsgTypeCdVos = tbCmnMsgTypeVos;
	}
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	/** MEMBER METHOD DECLARATION END **/
}
