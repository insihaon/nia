package com.kt.ipms.legacy.opermgmt.srvmgmt.vo;
import java.io.Serializable;
import java.util.List;
import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class TbSvcLgroupCdListVo extends CommonVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = -1342934662751761695L;
	
	private List<TbSvcLgroupCdVo> tbSvcLgroupCdVos;
	
	private int totalCount;
	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/
	public List<TbSvcLgroupCdVo> getTbSvcLgroupCdVos() {
		return this.tbSvcLgroupCdVos;
	}
	
	public void setTbSvcLgroupCdVos(List<TbSvcLgroupCdVo> tbSvcLgroupCdVos) {
		this.tbSvcLgroupCdVos = tbSvcLgroupCdVos;
	}
	
	public int getTotalCount() {
		return this.totalCount;
	}
	
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	/** MEMBER METHOD DECLARATION END **/
}