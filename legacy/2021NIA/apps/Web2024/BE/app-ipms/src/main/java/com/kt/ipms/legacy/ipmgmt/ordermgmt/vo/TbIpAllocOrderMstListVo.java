package com.kt.ipms.legacy.ipmgmt.ordermgmt.vo;
import java.io.Serializable;
import java.util.List;
import com.kt.ipms.legacy.cmn.vo.IpVo;

public class TbIpAllocOrderMstListVo extends IpVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = -6606494075071212572L;

	private List<TbIpAllocOrderMstVo> tbIpAllocOrderMstVos;
	
	private int totalCount;
	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/
	public List<TbIpAllocOrderMstVo> getTbIpAllocOrderMstVos() {
		return this.tbIpAllocOrderMstVos;
	}
	
	public void setTbIpAllocOrderMstVos(List<TbIpAllocOrderMstVo> tbIpAllocOrderMstVos) {
		this.tbIpAllocOrderMstVos = tbIpAllocOrderMstVos;
	}
	
	public int getTotalCount() {
		return this.totalCount;
	}
	
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	/** MEMBER METHOD DECLARATION END **/
}