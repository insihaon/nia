package com.kt.ipms.legacy.opermgmt.intgrmgmt.vo;

import java.io.Serializable;
import java.util.List;

import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class TbFcltMstListVo extends CommonVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5582668240197889862L;
	
	private List<TbFcltMstVo> tbFcltMstVos;
	
	private int totalCount;

	public List<TbFcltMstVo> getTbFcltMstVos() {
		return tbFcltMstVos;
	}

	public void setTbFcltMstVos(List<TbFcltMstVo> tbFcltMstVos) {
		this.tbFcltMstVos = tbFcltMstVos;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	
	

}
