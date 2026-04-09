package com.kt.ipms.legacy.opermgmt.srvmgmt.vo;
import java.io.Serializable;
import java.util.List;
import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class TbSvcMgroupCdListVo extends CommonVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = -581478698011504643L;
	
	private List<TbSvcMgroupCdVo> tbSvcMgroupCdVos;
	
	private int totalCount;
	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/
	public List<TbSvcMgroupCdVo> getTbSvcMgroupCdVos() {
		return this.tbSvcMgroupCdVos;
	}
	
	public void setTbSvcMgroupCdVos(List<TbSvcMgroupCdVo> tbSvcMgroupCdVos) {
		this.tbSvcMgroupCdVos = tbSvcMgroupCdVos;
	}
	
	public int getTotalCount() {
		return this.totalCount;
	}
	
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	/** MEMBER METHOD DECLARATION END **/
}