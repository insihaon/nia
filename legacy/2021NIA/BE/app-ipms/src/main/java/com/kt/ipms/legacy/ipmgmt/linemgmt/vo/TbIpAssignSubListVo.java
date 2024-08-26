package com.kt.ipms.legacy.ipmgmt.linemgmt.vo;
import java.io.Serializable;
import java.util.List;
import com.kt.ipms.legacy.cmn.vo.IpVo;

public class TbIpAssignSubListVo extends IpVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = -7764648211664320683L;
	
	private List<TbIpAssignSubVo> tbIpAssignSubVos;
	
	private int totalCount;
	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/
	public List<TbIpAssignSubVo> getTbIpAssignSubVos() {
		return this.tbIpAssignSubVos;
	}
	
	public void setTbIpAssignSubVos(List<TbIpAssignSubVo> tbIpAssignSubVos) {
		this.tbIpAssignSubVos = tbIpAssignSubVos;
	}
	
	public int getTotalCount() {
		return this.totalCount;
	}
	
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	/** MEMBER METHOD DECLARATION END **/
}