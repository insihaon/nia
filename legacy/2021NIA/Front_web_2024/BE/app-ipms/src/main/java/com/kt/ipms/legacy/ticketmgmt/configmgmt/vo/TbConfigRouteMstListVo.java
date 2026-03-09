package com.kt.ipms.legacy.ticketmgmt.configmgmt.vo;
import java.io.Serializable;
import java.util.List;
import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class TbConfigRouteMstListVo extends CommonVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = -4002464032413036562L;
	
	private List<TbConfigRouteMstVo> tbConfigRouteMstVos;
	
	private int totalCount;
	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/
	public List<TbConfigRouteMstVo> getTbConfigRouteMstVos() {
		return this.tbConfigRouteMstVos;
	}
	
	public void setTbConfigRouteMstVos(List<TbConfigRouteMstVo> tbConfigRouteMstVos) {
		this.tbConfigRouteMstVos = tbConfigRouteMstVos;
	}
	
	public int getTotalCount() {
		return this.totalCount;
	}
	
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	/** MEMBER METHOD DECLARATION END **/
}