package com.kt.ipms.legacy.opermgmt.srvmgmt.vo;

import java.io.Serializable;
import java.util.List;

import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class TbIpmsSvcSubListVo extends CommonVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = 555259241088194454L;
	
	private List<TbIpmsSvcSubVo> tbIpmsSvcSubVos;
	
	private int totalCount;
	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/
	public List<TbIpmsSvcSubVo> getTbIpmsSvcSubVos() {
		return tbIpmsSvcSubVos;
	}

	public void setTbIpmsSvcSubVos(List<TbIpmsSvcSubVo> tbIpmsSvcSubVos) {
		this.tbIpmsSvcSubVos = tbIpmsSvcSubVos;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	/** MEMBER METHOD DECLARATION END **/
}
