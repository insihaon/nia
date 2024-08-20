package com.kt.ipms.legacy.linkmgmt.common.vo;

import java.io.Serializable;
import java.util.List;
import com.kt.ipms.legacy.cmn.vo.IpVo;

public class TbLnkIpAllocMstListVo extends IpVo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3714274839080010599L;

	/** MEMBER VARIABLE DECLARATION START **/
	
	
	private List<TbLnkIpAllocMstVo> tbLnkIpAllocMstVos;
	
	private int totalCount;
	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/
	public List<TbLnkIpAllocMstVo> getTbLnkIpAllocMstVos() {
		return this.tbLnkIpAllocMstVos;
	}
	
	public void setLnkTbIpAllocMstVos(List<TbLnkIpAllocMstVo> tbLnkIpAllocMstVos) {
		this.tbLnkIpAllocMstVos = tbLnkIpAllocMstVos;
	}
	
	public int getTotalCount() {
		return this.totalCount;
	}
	
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	/** MEMBER METHOD DECLARATION END **/
}

