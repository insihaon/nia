package com.kt.ipms.legacy.ipmgmt.allocmgmt.vo;
import java.io.Serializable;
import java.util.List;

import com.kt.ipms.legacy.cmn.vo.IpVo;

public class TbIpAllocMstListVo extends IpVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = 7094205411304819257L;
	
	private List<TbIpAllocMstVo> tbIpAllocMstVos;
	
	private int totalCount;
	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/
	public List<TbIpAllocMstVo> getTbIpAllocMstVos() {
		return this.tbIpAllocMstVos;
	}
	
	public void setTbIpAllocMstVos(List<TbIpAllocMstVo> tbIpAllocMstVos) {
		this.tbIpAllocMstVos = tbIpAllocMstVos;
	}
	
	public int getTotalCount() {
		return this.totalCount;
	}
	
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	/** MEMBER METHOD DECLARATION END **/
}