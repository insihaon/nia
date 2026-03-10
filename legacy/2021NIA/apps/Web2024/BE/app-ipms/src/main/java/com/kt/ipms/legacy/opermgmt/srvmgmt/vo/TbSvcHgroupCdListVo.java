package com.kt.ipms.legacy.opermgmt.srvmgmt.vo;
import java.io.Serializable;
import java.util.List;
import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class TbSvcHgroupCdListVo extends CommonVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = 3633678232360207340L;
	
	private List<TbSvcHgroupCdVo> tbSvcHgroupCdVos;
	
	private int totalCount;
	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/
	public List<TbSvcHgroupCdVo> getTbSvcHgroupCdVos() {
		return this.tbSvcHgroupCdVos;
	}
	
	public void setTbSvcHgroupCdVos(List<TbSvcHgroupCdVo> tbSvcHgroupCdVos) {
		this.tbSvcHgroupCdVos = tbSvcHgroupCdVos;
	}
	
	public int getTotalCount() {
		return this.totalCount;
	}
	
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	/** MEMBER METHOD DECLARATION END **/
}