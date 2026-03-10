package com.kt.ipms.legacy.ipmgmt.assignmgmt.vo;
import java.io.Serializable;
import java.util.List;
import com.kt.ipms.legacy.cmn.vo.IpVo;

public class TbIpAssignMstListVo extends IpVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = -3986203317109043778L;
	
	private List<TbIpAssignMstVo> tbIpAssignMstVos;
	
	private int totalCount;
	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/
	public List<TbIpAssignMstVo> getTbIpAssignMstVos() {
		return this.tbIpAssignMstVos;
	}
	
	public void setTbIpAssignMstVos(List<TbIpAssignMstVo> tbIpAssignMstVos) {
		this.tbIpAssignMstVos = tbIpAssignMstVos;
	}
	
	public int getTotalCount() {
		return this.totalCount;
	}
	
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	/** MEMBER METHOD DECLARATION END **/
}