package com.kt.ipms.legacy.opermgmt.asmgmt.vo;

import java.io.Serializable;
import java.util.List;

import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class TbRequestAsApyTxnListVo extends CommonVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = -7939925484005948835L;
	
	private List<TbRequestAsApyTxnVo> tbRequestAsApyTxnVos;
	
	private int totalCount;
	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/
	public List<TbRequestAsApyTxnVo> getTbRequestAsApyTxnVos() {
		return tbRequestAsApyTxnVos;
	}

	public void setTbRequestAsApyTxnVos(
			List<TbRequestAsApyTxnVo> tbRequestAsApyTxnVos) {
		this.tbRequestAsApyTxnVos = tbRequestAsApyTxnVos;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	
	/** MEMBER METHOD DECLARATION END **/
	
}
