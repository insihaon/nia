package com.kt.ipms.legacy.linkmgmt.batchmgmt.vo;

import java.io.Serializable;
import java.util.List;

import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class TbBatchLogListVo extends CommonVo implements Serializable{
	private static final long serialVersionUID = 2582332281291758576L;
	private List<TbBatchLogVo> tbBatchLogVos;
	private int totalCount;
	public List<TbBatchLogVo> getTbBatchLogVos() {
		return tbBatchLogVos;
	}

	public void setTbBatchLogVos(List<TbBatchLogVo> tbBatchLogVos) {
		this.tbBatchLogVos = tbBatchLogVos;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
}
