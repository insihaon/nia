package com.kt.ipms.legacy.ipmgmt.tracemgmt.vo;

import java.io.Serializable;
import java.util.List;

import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class TbTraceIPListVo extends CommonVo implements Serializable{
	
/**
	 * 
	 */
	private static final long serialVersionUID = -717674107586449561L;

	private List<TbTraceIPVo> tbTraceIPVos;
	
	private int totalCount;		
	
	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/
	
	
	public int getTotalCount() {
		return this.totalCount;
	}
	
	public List<TbTraceIPVo> getTbTraceIPVos() {
		return tbTraceIPVos;
	}

	public void setTbTraceIPVos(List<TbTraceIPVo> tbTraceIPVos) {
		this.tbTraceIPVos = tbTraceIPVos;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	/** MEMBER METHOD DECLARATION END **/
}
