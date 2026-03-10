package com.kt.ipms.legacy.ticketmgmt.configmgmt.vo;
import java.io.Serializable;
import java.util.List;
import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class TbConfigLinkMstListVo extends CommonVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = -2487148134704199596L;
	
	private List<TbConfigLinkMstVo> tbConfigLinkMstVos;
	
	private int totalCount;
	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/
	public List<TbConfigLinkMstVo> getTbConfigLinkMstVos() {
		return this.tbConfigLinkMstVos;
	}
	
	public void setTbConfigLinkMstVos(List<TbConfigLinkMstVo> tbConfigLinkMstVos) {
		this.tbConfigLinkMstVos = tbConfigLinkMstVos;
	}
	
	public int getTotalCount() {
		return this.totalCount;
	}
	
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	/** MEMBER METHOD DECLARATION END **/
}