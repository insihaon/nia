package com.kt.ipms.legacy.statmgmt.ipstatmgmt.vo;

import java.io.Serializable;
import java.util.List;

import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class IpSingleBlockStatListVo extends CommonVo implements Serializable{
	
	/**
	 *  단일블록별 현황 ListVO
	 */
	private static final long serialVersionUID = -8101211772746473172L;

	private List<IpSingleBlockStatVo> ipSingleBlockStatVos;
	
	private int totalCount;

	public List<IpSingleBlockStatVo> getIpSingleBlockStatVos() {
		return ipSingleBlockStatVos;
	}

	public void setIpSingleBlockStatVos(List<IpSingleBlockStatVo> ipSingleBlockStatVos) {
		this.ipSingleBlockStatVos = ipSingleBlockStatVos;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	
}
