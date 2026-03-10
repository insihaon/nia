package com.kt.ipms.legacy.ticketmgmt.optmgmt.vo;
import java.io.Serializable;
import java.util.List;
import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class TbOptimizationIpMstListVo extends CommonVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = -263976083205371641L;
	
	private List<TbOptimizationIpMstVo> tbOptimizationIpMstVos;
	
	private int totalCount;
	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/
	public List<TbOptimizationIpMstVo> getTbOptimizationIpMstVos() {
		return this.tbOptimizationIpMstVos;
	}
	
	public void setTbOptimizationIpMstVos(List<TbOptimizationIpMstVo> tbOptimizationIpMstVos) {
		this.tbOptimizationIpMstVos = tbOptimizationIpMstVos;
	}
	
	public int getTotalCount() {
		return this.totalCount;
	}
	
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	/** MEMBER METHOD DECLARATION END **/
}