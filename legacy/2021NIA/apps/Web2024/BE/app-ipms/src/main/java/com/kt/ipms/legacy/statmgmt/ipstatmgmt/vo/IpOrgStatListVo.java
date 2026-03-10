package com.kt.ipms.legacy.statmgmt.ipstatmgmt.vo;

import java.io.Serializable;
import java.util.List;

import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class IpOrgStatListVo extends CommonVo implements Serializable {

	/**
	 * 조직별 IP 현황 LIST VO
	 */
	private static final long serialVersionUID = 7073222540173706080L;
	
	private List<IpOrgStatVo> ipOrgStatVos;
	
	private int totalCount;
	/** MEMBER VARIABLE DECLARATION END **/

	public List<IpOrgStatVo> getIpOrgStatVos() {
		return ipOrgStatVos;
	}

	public void setIpOrgStatVos(List<IpOrgStatVo> ipOrgStatVos) {
		this.ipOrgStatVos = ipOrgStatVos;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	
	/** MEMBER METHOD DECLARATION START **/
	
	

}
