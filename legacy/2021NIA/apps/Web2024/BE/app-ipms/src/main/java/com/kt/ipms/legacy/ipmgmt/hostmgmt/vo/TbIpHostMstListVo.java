package com.kt.ipms.legacy.ipmgmt.hostmgmt.vo;
import java.io.Serializable;
import java.util.List;
import com.kt.ipms.legacy.cmn.vo.IpVo;

public class TbIpHostMstListVo extends IpVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = -6035685666273146033L;
	
	private List<TbIpHostMstVo> tbIpHostMstVos;
	
	private int totalCount;
	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/
	public List<TbIpHostMstVo> getTbIpHostMstVos() {
		return this.tbIpHostMstVos;
	}
	
	public void setTbIpHostMstVos(List<TbIpHostMstVo> tbIpHostMstVos) {
		this.tbIpHostMstVos = tbIpHostMstVos;
	}
	
	public int getTotalCount() {
		return this.totalCount;
	}
	
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	/** MEMBER METHOD DECLARATION END **/
}