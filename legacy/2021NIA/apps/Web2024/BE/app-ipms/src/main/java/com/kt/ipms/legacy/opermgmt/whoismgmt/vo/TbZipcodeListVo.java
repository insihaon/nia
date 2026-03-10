package com.kt.ipms.legacy.opermgmt.whoismgmt.vo;

import java.io.Serializable;
import java.util.List;

import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class TbZipcodeListVo extends CommonVo implements Serializable {
	private static final long serialVersionUID = 6102989244741896994L;
	private int totalCount;
	private List<TbZipcodeVo> tbZipcodeVos;
	
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	public List<TbZipcodeVo> getTbZipcodeVos() {
		return tbZipcodeVos;
	}
	public void setTbZipcodeVos(List<TbZipcodeVo> tbZipcodeVos) {
		this.tbZipcodeVos = tbZipcodeVos;
	}
}
