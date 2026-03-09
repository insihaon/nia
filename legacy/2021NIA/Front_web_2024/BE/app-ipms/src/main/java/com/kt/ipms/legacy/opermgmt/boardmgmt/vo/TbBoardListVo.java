package com.kt.ipms.legacy.opermgmt.boardmgmt.vo;
import java.io.Serializable;
import java.util.List;
import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class TbBoardListVo extends CommonVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = -6952489951453229396L;
	
	private List<TbBoardVo> tbBoardVos;
	
	private int totalCount;
	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/
	public List<TbBoardVo> getTbBoardVos() {
		return this.tbBoardVos;
	}
	
	public void setTbBoardVos(List<TbBoardVo> tbBoardVos) {
		this.tbBoardVos = tbBoardVos;
	}
	
	public int getTotalCount() {
		return this.totalCount;
	}
	
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	/** MEMBER METHOD DECLARATION END **/
}