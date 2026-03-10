package com.kt.ipms.legacy.statmgmt.ipstatmgmt.vo;

import java.io.Serializable;
import java.util.List;

import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class IpCreateStatListVo extends CommonVo implements Serializable {

	private static final long serialVersionUID = -6320053717133569411L;
	
	private List<IpCreateStatVo> ipCreateStatVos;
	
	private int totalCount;

	public List<IpCreateStatVo> getIpCreateStatVos() {
		return ipCreateStatVos;
	}

	public void setIpCreateStatVos(List<IpCreateStatVo> ipCreateStatVos) {
		this.ipCreateStatVos = ipCreateStatVos;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

}
