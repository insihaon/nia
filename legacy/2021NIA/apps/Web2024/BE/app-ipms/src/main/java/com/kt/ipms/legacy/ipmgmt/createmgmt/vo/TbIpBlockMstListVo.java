package com.kt.ipms.legacy.ipmgmt.createmgmt.vo;
import java.io.Serializable;
import java.util.List;

public class TbIpBlockMstListVo extends TbIpBlockMstVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = 2318001347146237357L;
	
	private List<TbIpBlockMstVo> tbIpBlockMstVos;
	
	private int totalCount;
	
	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/
	public List<TbIpBlockMstVo> getTbIpBlockMstVos() {
		return this.tbIpBlockMstVos;
	}
	
	public void setTbIpBlockMstVos(List<TbIpBlockMstVo> tbIpBlockMstVos) {
		this.tbIpBlockMstVos = tbIpBlockMstVos;
	}
	
	public int getTotalCount() {
		return this.totalCount;
	}
	
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	/** MEMBER METHOD DECLARATION END **/
}