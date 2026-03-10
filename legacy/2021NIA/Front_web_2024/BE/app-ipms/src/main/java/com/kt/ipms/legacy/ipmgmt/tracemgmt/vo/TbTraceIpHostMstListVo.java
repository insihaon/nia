package com.kt.ipms.legacy.ipmgmt.tracemgmt.vo;

import java.io.Serializable;
import java.util.List;

import com.kt.ipms.legacy.cmn.vo.CommonVo;


public class TbTraceIpHostMstListVo extends CommonVo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2677062098739683226L;

	private List<TbTraceIpHostMstVo> tbTraceIpHostMstVos;
	
	private int totalCount;

	public List<TbTraceIpHostMstVo> getTbTraceIpHostMstVos() {
		return tbTraceIpHostMstVos;
	}

	public void setTbTraceIpHostMstVos(List<TbTraceIpHostMstVo> tbTraceIpHostMstVos) {
		this.tbTraceIpHostMstVos = tbTraceIpHostMstVos;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

}
