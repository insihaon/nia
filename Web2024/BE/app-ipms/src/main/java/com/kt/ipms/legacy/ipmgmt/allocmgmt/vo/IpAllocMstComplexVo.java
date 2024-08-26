package com.kt.ipms.legacy.ipmgmt.allocmgmt.vo;

import java.io.Serializable;
import java.util.List;

import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class IpAllocMstComplexVo extends CommonVo implements Serializable {

	private static final long serialVersionUID = 3691543951636222396L; 

	private IpAllocOperMstVo srcIpAllocMstVo;
	
	private List<IpAllocOperMstVo> destIpAllocMstVos;

	public IpAllocOperMstVo getSrcIpAllocMstVo() {
		return srcIpAllocMstVo;
	}

	public void setSrcIpAllocMstVo(IpAllocOperMstVo srcIpAllocMstVo) {
		this.srcIpAllocMstVo = srcIpAllocMstVo;
	}

	public List<IpAllocOperMstVo> getDestIpAllocMstVos() {
		return destIpAllocMstVos;
	}

	public void setDestIpAllocMstVos(List<IpAllocOperMstVo> destIpAllocMstVos) {
		this.destIpAllocMstVos = destIpAllocMstVos;
	}

}
