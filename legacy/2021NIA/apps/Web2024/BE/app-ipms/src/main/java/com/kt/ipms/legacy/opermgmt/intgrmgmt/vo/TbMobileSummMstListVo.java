package com.kt.ipms.legacy.opermgmt.intgrmgmt.vo;

import java.io.Serializable;
import java.util.List;

import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class TbMobileSummMstListVo extends CommonVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7550728637909790584L;

	private List<TbMobileSummMstVo> tbMobileSummMstVos;
	
	private int totalCount;

	public List<TbMobileSummMstVo> getTbMobileSummMstVos() {
		return tbMobileSummMstVos;
	}

	public void setTbMobileSummMstVos(List<TbMobileSummMstVo> tbMobileSummMstVos) {
		this.tbMobileSummMstVos = tbMobileSummMstVos;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	

}
