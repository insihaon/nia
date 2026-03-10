package com.kt.ipms.legacy.opermgmt.operstdmgmt.vo;
import java.io.Serializable;
import java.util.List;

import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class TbCmnMsgMstListVo extends CommonVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = 5102136819233081811L;
	
	private List<TbCmnMsgMstVo> tbCmnMsgMstVos;
	
	private int totalCount;
	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/
	public List<TbCmnMsgMstVo> getTbCmnMsgMstVos() {
		return this.tbCmnMsgMstVos;
	}
	
	public void setTbCmnMsgMstVos(List<TbCmnMsgMstVo> tbCmnMsgMstVos) {
		this.tbCmnMsgMstVos = tbCmnMsgMstVos;
	}
	
	public int getTotalCount() {
		return this.totalCount;
	}
	
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	/** MEMBER METHOD DECLARATION END **/
}