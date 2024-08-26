package com.kt.ipms.legacy.ipmgmt.allocmgmt.vo;
import java.io.Serializable;
import java.util.List;

import com.kt.ipms.legacy.cmn.vo.IpVo;
import com.kt.ipms.legacy.ipmgmt.routmgmt.vo.TbRoutChkMstVo;

public class IpAllocOperMstListVo extends IpVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = -7751922513122235482L;
	
	private List<IpAllocOperMstVo> ipAllocOperMstVos;
	
	private TbRoutChkMstVo tbRoutChkMstVo;
	
	private int totalCount;
	
	private String srchTypeFlag;
	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/
	public List<IpAllocOperMstVo> getIpAllocOperMstVos() {
		return this.ipAllocOperMstVos;
	}
	
	public void setIpAllocOperMstVos(List<IpAllocOperMstVo> ipAllocOperMstVos) {
		this.ipAllocOperMstVos = ipAllocOperMstVos;
	}
	
	public int getTotalCount() {
		return this.totalCount;
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

	public TbRoutChkMstVo getTbRoutChkMstVo() {
		return tbRoutChkMstVo;
	}

	public void setTbRoutChkMstVo(TbRoutChkMstVo tbRoutChkMstVo) {
		this.tbRoutChkMstVo = tbRoutChkMstVo;
	}
	
	
	/** MEMBER METHOD DECLARATION END **/
}