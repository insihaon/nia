package com.kt.ipms.legacy.ipmgmt.ipuploadmgmt.vo;

import java.io.Serializable;
import java.util.List;

import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class TbIpUploadSubListVo extends CommonVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1875054982625059339L;

	private List<TbIpUploadSubVo> tbIpUploadSubVos;
	
	private int totalCount;

	public List<TbIpUploadSubVo> getTbIpUploadSubVos() {
		return tbIpUploadSubVos;
	}

	public void setTbIpUploadSubVos(List<TbIpUploadSubVo> tbIpUploadSubVos) {
		this.tbIpUploadSubVos = tbIpUploadSubVos;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
}
