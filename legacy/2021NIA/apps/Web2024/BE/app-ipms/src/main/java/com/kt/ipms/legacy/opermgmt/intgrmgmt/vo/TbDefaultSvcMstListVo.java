package com.kt.ipms.legacy.opermgmt.intgrmgmt.vo;

import java.io.Serializable;
import java.util.List;

import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class TbDefaultSvcMstListVo extends CommonVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7540672007555905576L;
	
	private List<TbDefaultSvcMstVo> tbDefaultSvcMstVos;
	
	private int totalCount;

	public List<TbDefaultSvcMstVo> getTbDefaultSvcMstVos() {
		return tbDefaultSvcMstVos;
	}

	public void setTbDefaultSvcMstVos(List<TbDefaultSvcMstVo> tbDefaultSvcMstVo) {
		this.tbDefaultSvcMstVos = tbDefaultSvcMstVo;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

}
