package com.kt.ipms.legacy.ipmgmt.ipuploadmgmt.vo;

import java.io.Serializable;
import java.util.List;

import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class TbIpUploadListVo extends CommonVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6470056395572791410L;
	
	private List<TbIpUploadVo> tbIpUloadVos;
	
	private int totalCount;

	public List<TbIpUploadVo> getTbIpUloadVos() {
		return tbIpUloadVos;
	}

	public void setTbIpUloadVos(List<TbIpUploadVo> tbIpUloadVos) {
		this.tbIpUloadVos = tbIpUloadVos;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

}
