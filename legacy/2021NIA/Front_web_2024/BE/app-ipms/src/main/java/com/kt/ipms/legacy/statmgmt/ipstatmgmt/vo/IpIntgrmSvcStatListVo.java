package com.kt.ipms.legacy.statmgmt.ipstatmgmt.vo;

import java.io.Serializable;
import java.util.List;

import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class IpIntgrmSvcStatListVo extends CommonVo implements Serializable {

	private static final long serialVersionUID = 838688481495406282L;
	
	private List<IpIntgrmSvcStatVo> ipIntgrmSvcStatVos;
	
	private int totalCount;

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public List<IpIntgrmSvcStatVo> getIpIntgrmSvcStatVos() {
		return ipIntgrmSvcStatVos;
	}

	public void setIpIntgrmSvcStatVos(List<IpIntgrmSvcStatVo> ipIntgrmSvcStatVos) {
		this.ipIntgrmSvcStatVos = ipIntgrmSvcStatVos;
	}
	
}
