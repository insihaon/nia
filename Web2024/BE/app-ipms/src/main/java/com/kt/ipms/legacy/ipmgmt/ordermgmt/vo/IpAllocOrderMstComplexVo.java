package com.kt.ipms.legacy.ipmgmt.ordermgmt.vo;

import java.io.Serializable;
import java.util.List;

import com.kt.ipms.legacy.cmn.vo.CommonVo;
import com.kt.ipms.legacy.ipmgmt.allocmgmt.vo.IpAllocOperMstVo;

public class IpAllocOrderMstComplexVo extends CommonVo implements Serializable {

	private static final long serialVersionUID = -5164735189161619178L;

	private IpAllocOrderMstVo srcIpAllocOrderMstVo;
	
	private List<IpAllocOperMstVo> destIpAllocOrderMstVos;

	public IpAllocOrderMstVo getSrcIpAllocOrderMstVo() {
		return srcIpAllocOrderMstVo;
	}

	public void setSrcIpAllocOrderMstVo(IpAllocOrderMstVo srcIpAllocOrderMstVo) {
		this.srcIpAllocOrderMstVo = srcIpAllocOrderMstVo;
	}

	public List<IpAllocOperMstVo> getDestIpAllocOrderMstVos() {
		return destIpAllocOrderMstVos;
	}

	public void setDestIpAllocOrderMstVos(
			List<IpAllocOperMstVo> destIpAllocOrderMstVos) {
		this.destIpAllocOrderMstVos = destIpAllocOrderMstVos;
	}

	

}
