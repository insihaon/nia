package com.kt.ipms.legacy.opermgmt.uploadmgmt.vo;

import java.util.List;

public class TbUploadListVo {
	private List<TbUploadVo> tbUploadVo;
	
	private int totalCount;

	public List<TbUploadVo> getTbUploadVo() {
		return tbUploadVo;
	}

	public void setTbUploadVo(List<TbUploadVo> tbUploadVo) {
		this.tbUploadVo = tbUploadVo;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}


	
	
}
