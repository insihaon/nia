package com.kt.ipms.legacy.opermgmt.asmgmt.vo;
import java.io.Serializable;
import java.util.List;
import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class TbRequestAsHistListVo extends CommonVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = -4923367565548698210L;
	
	private List<TbRequestAsHistVo> tbRequestAsHistVos;
	
	private int totalCount;
	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/
	public List<TbRequestAsHistVo> getTbRequestAsHistVos() {
		return this.tbRequestAsHistVos;
	}
	
	public void setTbRequestAsHistVos(List<TbRequestAsHistVo> tbRequestAsHistVos) {
		this.tbRequestAsHistVos = tbRequestAsHistVos;
	}
	
	public int getTotalCount() {
		return this.totalCount;
	}
	
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	/** MEMBER METHOD DECLARATION END **/
}