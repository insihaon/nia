 package com.kt.ipms.legacy.opermgmt.whoismgmt.vo;

import java.io.Serializable;
import java.util.List;

import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class TbWhoisModifyListVo extends CommonVo implements Serializable {

	private static final long serialVersionUID = -6571068716817352004L;

	private List<TbWhoisModifyVo> tbWhoisModifyVos;
	
	private int totalCount;

	public List<TbWhoisModifyVo> getTbWhoisModifyVos() {
		return tbWhoisModifyVos;
	}

	public void setTbWhoisModifyVos(List<TbWhoisModifyVo> tbWhoisModifyVos) {
		this.tbWhoisModifyVos = tbWhoisModifyVos;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
}
