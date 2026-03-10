package com.kt.ipms.legacy.opermgmt.intgrmgmt.vo;

import java.io.Serializable;
import java.util.List;

import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class TbMobileMstListVo extends CommonVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1047561224306746047L;

	private List<TbMobileMstVo> tbMobileMstVos;
	
	private int totalCount;

	public List<TbMobileMstVo> getTbMobileMstVos() {
		return tbMobileMstVos;
	}

	public void setTbMobileMstVos(List<TbMobileMstVo> tbMobileMstVos) {
		this.tbMobileMstVos = tbMobileMstVos;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

}
