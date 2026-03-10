package com.kt.ipms.legacy.opermgmt.intgrmgmt.vo;

import java.io.Serializable;
import java.util.List;

import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class TbWireMstListVo extends CommonVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5890486955566137668L;
	
	private List<TbWireMstVo> tbWireMstVos;
	
	private int totalCount;

	public List<TbWireMstVo> getTbWireMstVos() {
		return tbWireMstVos;
	}

	public void setTbWireMstVos(List<TbWireMstVo> tbWireMstVos) {
		this.tbWireMstVos = tbWireMstVos;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

}
