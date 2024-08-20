package com.kt.ipms.legacy.opermgmt.whoismgmt.vo;
import java.io.Serializable;
import java.util.List;
import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class TbWhoisUserListVo extends CommonVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = 6561559333672338511L;
	
	private List<TbWhoisUserVo> tbWhoisUserVos;
	
	private int totalCount;
	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/
	public List<TbWhoisUserVo> getTbWhoisUserVos() {
		return this.tbWhoisUserVos;
	}
	
	public void setTbWhoisUserVos(List<TbWhoisUserVo> tbWhoisUserVos) {
		this.tbWhoisUserVos = tbWhoisUserVos;
	}
	
	public int getTotalCount() {
		return this.totalCount;
	}
	
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	/** MEMBER METHOD DECLARATION END **/
}