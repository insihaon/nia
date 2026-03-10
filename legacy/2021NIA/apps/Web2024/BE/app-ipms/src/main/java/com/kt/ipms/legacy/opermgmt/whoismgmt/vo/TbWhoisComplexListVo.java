package com.kt.ipms.legacy.opermgmt.whoismgmt.vo;

import java.io.Serializable;
import java.util.List;

import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class TbWhoisComplexListVo extends CommonVo implements Serializable {

	private static final long serialVersionUID = 4214444067204724368L;
	
	private int totalCount;
	
	private List<TbWhoisComplexVo> tbWhoisComplexVos;

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public List<TbWhoisComplexVo> getTbWhoisComplexVos() {
		return tbWhoisComplexVos;
	}

	public void setTbWhoisComplexVos(List<TbWhoisComplexVo> tbWhoisComplexVos) {
		this.tbWhoisComplexVos = tbWhoisComplexVos;
	}
}
