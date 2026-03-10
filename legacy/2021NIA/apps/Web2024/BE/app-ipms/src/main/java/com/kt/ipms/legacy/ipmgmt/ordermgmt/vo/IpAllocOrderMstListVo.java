package com.kt.ipms.legacy.ipmgmt.ordermgmt.vo;
import java.io.Serializable;
import java.util.List;
import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class IpAllocOrderMstListVo extends CommonVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = -2177087810926588110L;

	private List<IpAllocOrderMstVo> ipAllocOrderMstVos;
	
	private int totalCount;
	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/
	public List<IpAllocOrderMstVo> getIpAllocOrderMstVos() {
		return this.ipAllocOrderMstVos;
	}
	
	public void setIpAllocOrderMstVos(List<IpAllocOrderMstVo> ipAllocOrderMstVos) {
		this.ipAllocOrderMstVos = ipAllocOrderMstVos;
	}
	
	public int getTotalCount() {
		return this.totalCount;
	}
	
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	/** MEMBER METHOD DECLARATION END **/
}