package com.kt.ipms.legacy.opermgmt.boardmgmt.vo;

import java.io.Serializable;
import java.util.List;

import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class TbBoardReplyListVo extends CommonVo implements Serializable{
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = -4921392258363325620L;
	
	private List<TbBoardReplyVo> tbBoardReplyVos;
	
	private int totalCount;
	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/
	public List<TbBoardReplyVo> getTbBoardReplyVos() {
		return this.tbBoardReplyVos;
	}

	public void setTbBoardReplyVos(List<TbBoardReplyVo> tbBoardReplyVos) {
		this.tbBoardReplyVos = tbBoardReplyVos;
	}

	public int getTotalCount() {
		return this.totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

}
