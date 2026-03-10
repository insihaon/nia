package com.kt.ipms.legacy.ipmgmt.linkmgmt.vo;

import java.io.Serializable;
import java.util.List;

import com.kt.ipms.legacy.cmn.vo.IpVo;

public class TbIpLinkMstListVo extends IpVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7637586379856004662L;

	private List<TbIpLinkMstVo> tbIpLinkMstVos;
	
	private int totalCount;

	public List<TbIpLinkMstVo> getTbIpLinkMstVos() {
		return tbIpLinkMstVos;
	}

	public void setTbIpLinkMstVos(List<TbIpLinkMstVo> tbIpLinkMstVos) {
		this.tbIpLinkMstVos = tbIpLinkMstVos;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
}
