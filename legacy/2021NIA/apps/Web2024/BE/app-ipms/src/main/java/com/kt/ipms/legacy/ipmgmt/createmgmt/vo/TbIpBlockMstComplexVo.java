package com.kt.ipms.legacy.ipmgmt.createmgmt.vo;

import java.io.Serializable;
import java.util.List;

import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class TbIpBlockMstComplexVo extends CommonVo implements Serializable {

	private static final long serialVersionUID = 243687836341648079L;
	
	private TbIpBlockMstVo srcIpBlockMstVo;
	
	private List<TbIpBlockMstVo> destIpBlockMstVos;

	public TbIpBlockMstVo getSrcIpBlockMstVo() {
		return srcIpBlockMstVo;
	}

	public void setSrcIpBlockMstVo(TbIpBlockMstVo srcIpBlockMstVo) {
		this.srcIpBlockMstVo = srcIpBlockMstVo;
	}

	public List<TbIpBlockMstVo> getDestIpBlockMstVos() {
		return destIpBlockMstVos;
	}

	public void setDestIpBlockMstVos(List<TbIpBlockMstVo> destIpBlockMstVos) {
		this.destIpBlockMstVos = destIpBlockMstVos;
	}
	

}
