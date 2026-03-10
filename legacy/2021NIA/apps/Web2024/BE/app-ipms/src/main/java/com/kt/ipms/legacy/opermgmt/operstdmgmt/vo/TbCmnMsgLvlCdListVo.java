package com.kt.ipms.legacy.opermgmt.operstdmgmt.vo;

import java.io.Serializable;
import java.util.List;

import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class TbCmnMsgLvlCdListVo extends CommonVo implements Serializable{
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = 5325004772877689879L;
	private List<TbCmnMsgLvlCdVo> tbCmnMsgLvlCdVos;
	private int totalCount;
	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/	
	public List<TbCmnMsgLvlCdVo> getTbCmnMsgLvlCdVos() {
		return tbCmnMsgLvlCdVos;
	}
	public void setTbCmnMsgLvlVos(List<TbCmnMsgLvlCdVo> tbCmnMsgLvlCdVos) {
		this.tbCmnMsgLvlCdVos = tbCmnMsgLvlCdVos;
	}
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	/** MEMBER METHOD DECLARATION END **/
}
