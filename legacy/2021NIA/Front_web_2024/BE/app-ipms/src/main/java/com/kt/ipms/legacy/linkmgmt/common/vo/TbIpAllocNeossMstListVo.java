package com.kt.ipms.legacy.linkmgmt.common.vo;
import java.io.Serializable;
import java.util.List;
import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class TbIpAllocNeossMstListVo extends CommonVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = -5589019845853558125L;
	
	private List<TbIpAllocNeossMstVo> tbIpAllocNeossMstVos;
	
	private int totalCount;
	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/
	public List<TbIpAllocNeossMstVo> getTbIpAllocNeossMstVos() {
		return this.tbIpAllocNeossMstVos;
	}
	
	public void setTbIpAllocNeossMstVos(List<TbIpAllocNeossMstVo> tbIpAllocNeossMstVos) {
		this.tbIpAllocNeossMstVos = tbIpAllocNeossMstVos;
	}
	
	public int getTotalCount() {
		return this.totalCount;
	}
	
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	/** MEMBER METHOD DECLARATION END **/
}