package com.kt.ipms.legacy.opermgmt.whoismgmt.vo;

import java.util.List;

import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class TbNewZipcodeListVo extends CommonVo {
	private int totalCount;
	private List<TbNewZipcodeVo> tbZipcodeVos;
	
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	public List<TbNewZipcodeVo> getTbZipcodeVos() {
		return tbZipcodeVos;
	}
	public void setTbZipcodeVos(List<TbNewZipcodeVo> tbZipcodeVos) {
		this.tbZipcodeVos = tbZipcodeVos;
	}
	
	
}
