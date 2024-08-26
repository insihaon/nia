package com.kt.ipms.legacy.opermgmt.srvmgmt.vo;
import java.io.Serializable;
import java.util.List;
import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class TbAssignTypeCdListVo extends CommonVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = -5247981773126416190L;
	
	private List<TbAssignTypeCdVo> tbAssignTypeCdVos;
	
	private int totalCount;
	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/
	public List<TbAssignTypeCdVo> getTbAssignTypeCdVos() {
		return this.tbAssignTypeCdVos;
	}
	
	public void setTbAssignTypeCdVos(List<TbAssignTypeCdVo> tbAssignTypeCdVos) {
		this.tbAssignTypeCdVos = tbAssignTypeCdVos;
	}
	
	public int getTotalCount() {
		return this.totalCount;
	}
	
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	/** MEMBER METHOD DECLARATION END **/
}