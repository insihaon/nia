package com.kt.ipms.legacy.opermgmt.limitmgmt.vo;
import java.io.Serializable;
import java.util.List;
import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class TbAuditAssignBasListVo extends CommonVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = 2982107412389841446L;
	
	private List<TbAuditAssignBasVo> tbAuditAssignBasVos;
	
	private int totalCount;
	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/
	public List<TbAuditAssignBasVo> getTbAuditAssignBasVos() {
		return this.tbAuditAssignBasVos;
	}
	
	public void setTbAuditAssignBasVos(List<TbAuditAssignBasVo> tbAuditAssignBasVos) {
		this.tbAuditAssignBasVos = tbAuditAssignBasVos;
	}
	
	public int getTotalCount() {
		return this.totalCount;
	}
	
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	/** MEMBER METHOD DECLARATION END **/
}