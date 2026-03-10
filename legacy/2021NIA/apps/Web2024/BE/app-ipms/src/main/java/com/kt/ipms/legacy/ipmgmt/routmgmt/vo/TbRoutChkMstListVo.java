package com.kt.ipms.legacy.ipmgmt.routmgmt.vo;

import java.io.Serializable;
import java.util.List;

import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class TbRoutChkMstListVo extends CommonVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5177546759321886842L;

	private List<TbRoutChkMstVo> tbRoutChkMstVos;
	
	private int totalCount;
	
	private int totalCount2;
	
	private int totalCount3;

	public List<TbRoutChkMstVo> getTbRoutChkMstVos() {
		return tbRoutChkMstVos;
	}

	public void setTbRoutChkMstVos(List<TbRoutChkMstVo> tbRoutChkMstVos) {
		this.tbRoutChkMstVos = tbRoutChkMstVos;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getTotalCount2() {
		return totalCount2;
	}

	public void setTotalCount2(int totalCount2) {
		this.totalCount2 = totalCount2;
	}

	public int getTotalCount3() {
		return totalCount3;
	}

	public void setTotalCount3(int totalCount3) {
		this.totalCount3 = totalCount3;
	}
}
