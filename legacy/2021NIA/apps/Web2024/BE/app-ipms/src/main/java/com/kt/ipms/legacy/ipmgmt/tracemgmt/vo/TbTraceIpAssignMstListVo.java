package com.kt.ipms.legacy.ipmgmt.tracemgmt.vo;

import java.io.Serializable;
import java.util.List;

import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class TbTraceIpAssignMstListVo  extends CommonVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2430213624010109885L;
	
	private List<TbTraceIpAssignMstVo> tbTraceIpAssignMstVos;
	
	private int totalCount;

	public List<TbTraceIpAssignMstVo> getTbTraceIpAssignMstVos() {
		return tbTraceIpAssignMstVos;
	}

	public void setTbTraceIpAssignMstVos(List<TbTraceIpAssignMstVo> tbTraceIpAssignMstVos) {
		this.tbTraceIpAssignMstVos = tbTraceIpAssignMstVos;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

}
