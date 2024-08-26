package com.kt.ipms.legacy.statmgmt.ipstatmgmt.vo;

import java.io.Serializable;
import java.util.List;

import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class IpOrgServiceStatListVo extends CommonVo implements Serializable {

	private static final long serialVersionUID = 838688481495406282L;
	
	private List<IpOrgServiceStatVo> ipOrgServiceStatVos;
	
	private int totalCount;

	public List<IpOrgServiceStatVo> getIpOrgServiceStatVos() {
		return ipOrgServiceStatVos;
	}

	public void setIpOrgServiceStatVos(List<IpOrgServiceStatVo> ipOrgServiceStatVos) {
		this.ipOrgServiceStatVos = ipOrgServiceStatVos;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	
}
