package com.kt.ipms.legacy.opermgmt.assignmgmt.vo;
import java.io.Serializable;
import java.util.List;
import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class TbRequestAssignMstListVo extends CommonVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = -4487738369269152542L;
	
	private List<TbRequestAssignMstVo> tbRequestAssignMstVos;
	
	private int totalCount;
	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/
	public List<TbRequestAssignMstVo> getTbRequestAssignMstVos() {
		return this.tbRequestAssignMstVos;
	}
	
	public void setTbRequestAssignMstVos(List<TbRequestAssignMstVo> tbRequestAssignMstVos) {
		this.tbRequestAssignMstVos = tbRequestAssignMstVos;
	}
	
	public int getTotalCount() {
		return this.totalCount;
	}
	
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	/** MEMBER METHOD DECLARATION END **/
}