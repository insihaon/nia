package com.kt.ipms.legacy.opermgmt.requiremgmt.vo;

import java.io.Serializable;
import java.util.List;

import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class ReqBoardListVo extends CommonVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2700220063360935143L;
	
	private List<ReqBoardVo> reqBoardVos;
	
	private int totalCount;

	public List<ReqBoardVo> getReqBoardVos() {
		return reqBoardVos;
	}

	public void setReqBoardVos(List<ReqBoardVo> reqBoardVos) {
		this.reqBoardVos = reqBoardVos;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	
	
}
