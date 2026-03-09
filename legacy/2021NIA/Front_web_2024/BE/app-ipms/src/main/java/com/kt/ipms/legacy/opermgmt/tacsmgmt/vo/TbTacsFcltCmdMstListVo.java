package com.kt.ipms.legacy.opermgmt.tacsmgmt.vo;
import java.io.Serializable;
import java.util.List;
import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class TbTacsFcltCmdMstListVo extends CommonVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = 8370582867806013002L;
	
	private List<TbTacsFcltCmdMstVo> tbTacsFcltCmdMstVos;
	
	private int totalCount;
	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/
	public List<TbTacsFcltCmdMstVo> getTbTacsFcltCmdMstVos() {
		return this.tbTacsFcltCmdMstVos;
	}
	
	public void setTbTacsFcltCmdMstVos(List<TbTacsFcltCmdMstVo> tbTacsFcltCmdMstVos) {
		this.tbTacsFcltCmdMstVos = tbTacsFcltCmdMstVos;
	}
	
	public int getTotalCount() {
		return this.totalCount;
	}
	
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	/** MEMBER METHOD DECLARATION END **/
}