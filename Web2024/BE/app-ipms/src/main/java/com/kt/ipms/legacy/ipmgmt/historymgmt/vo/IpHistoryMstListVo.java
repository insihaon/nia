package com.kt.ipms.legacy.ipmgmt.historymgmt.vo;

import java.io.Serializable;
import java.util.List;

import com.kt.ipms.legacy.cmn.vo.IpVo;

public class IpHistoryMstListVo extends IpVo implements Serializable {

	private static final long serialVersionUID = 7032445460705259509L;
	
	private List<IpHistoryMstVo> ipHistoryMstVos;
	
	private int totalCount;

	private String srchTypeFlag;

	public List<IpHistoryMstVo> getIpHistoryMstVos() {
		return ipHistoryMstVos;
	}

	public void setIpHistoryMstVos(List<IpHistoryMstVo> ipHistoryMstVos) {
		this.ipHistoryMstVos = ipHistoryMstVos;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public String getSrchTypeFlag() {
		return srchTypeFlag;
	}

	public void setSrchTypeFlag(String srchTypeFlag) {
		this.srchTypeFlag = srchTypeFlag;
	}
}
