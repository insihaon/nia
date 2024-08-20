package com.kt.ipms.legacy.opermgmt.intgrmgmt.vo;

import java.io.Serializable;
import java.util.List;

import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class TbRoutHistMstListVo extends CommonVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -858593478940749525L;
	
	private List<TbRoutHistMstVo> tbRoutHistMstVos;
	
	private int totalCount;

	public List<TbRoutHistMstVo> getTbRoutHistMstVos() {
		return tbRoutHistMstVos;
	}

	public void setTbRoutHistMstVos(List<TbRoutHistMstVo> tbRoutHistMstVos) {
		this.tbRoutHistMstVos = tbRoutHistMstVos;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}


}
