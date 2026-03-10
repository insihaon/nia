package com.kt.ipms.legacy.opermgmt.orgmgmt.vo;
import java.io.Serializable;
import java.util.List;
import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class TbSvcLineTypeCdListVo extends CommonVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = 882950984881162888L;
	
	private List<TbSvcLineTypeCdVo> tbSvcLineTypeCdVos;
	
	private int totalCount;
	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/
	
	public List<TbSvcLineTypeCdVo> getTbSvcLineTypeCdVos() {
		return tbSvcLineTypeCdVos;
	}

	public void setTbSvcLineTypeCdVos(List<TbSvcLineTypeCdVo> tbSvcLineTypeCdVos) {
		this.tbSvcLineTypeCdVos = tbSvcLineTypeCdVos;
	}

	public int getTotalCount() {
		return this.totalCount;
	}
	
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	/** MEMBER METHOD DECLARATION END **/
}