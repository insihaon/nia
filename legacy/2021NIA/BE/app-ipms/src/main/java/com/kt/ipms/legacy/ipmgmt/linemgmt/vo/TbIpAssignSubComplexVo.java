package com.kt.ipms.legacy.ipmgmt.linemgmt.vo;

import java.io.Serializable;
import java.util.List;

import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class TbIpAssignSubComplexVo extends CommonVo implements Serializable {

	private static final long serialVersionUID = 6890402402707563842L;
	
	private TbIpAssignSubVo srcIpAssignSubVo;
	
	private List<TbIpAssignSubVo> destIpAssignSubVos;

	public TbIpAssignSubVo getSrcIpAssignSubVo() {
		return srcIpAssignSubVo;
	}

	public void setSrcIpAssignSubVo(TbIpAssignSubVo srcIpAssignSubVo) {
		this.srcIpAssignSubVo = srcIpAssignSubVo;
	}

	public List<TbIpAssignSubVo> getDestIpAssignSubVos() {
		return destIpAssignSubVos;
	}

	public void setDestIpAssignSubVos(List<TbIpAssignSubVo> destIpAssignSubVos) {
		this.destIpAssignSubVos = destIpAssignSubVos;
	}

}
