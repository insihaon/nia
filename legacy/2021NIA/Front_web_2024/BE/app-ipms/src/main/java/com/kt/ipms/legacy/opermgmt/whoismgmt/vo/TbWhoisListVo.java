package com.kt.ipms.legacy.opermgmt.whoismgmt.vo;
import java.io.Serializable;
import java.util.List;

import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class TbWhoisListVo extends CommonVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = 1680993427037439827L;
	
	private List<TbWhoisVo> tbWhoisVos;
	
	private int totalCount;
	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/
	public List<TbWhoisVo> getTbWhoisVos() {
		return this.tbWhoisVos;
	}
	
	public void setTbWhoisVos(List<TbWhoisVo> tbWhoisVos) {
		this.tbWhoisVos = tbWhoisVos;
	}
	
	public int getTotalCount() {
		return this.totalCount;
	}
	
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	/** MEMBER METHOD DECLARATION END **/
}