package com.kt.ipms.legacy.opermgmt.intgrmgmt.vo;

import java.io.Serializable;
import java.util.List;

import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class TbFcltCmdMstListVo extends CommonVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4411947191880708260L;

	private List<TbFcltCmdMstVo> tbFcltCmdMstVos;
	
	private int totalCount;

	public List<TbFcltCmdMstVo> getTbFcltCmdMstVos() {
		return tbFcltCmdMstVos;
	}

	public void setTbFcltCmdMstVos(List<TbFcltCmdMstVo> tbFcltCmdMstVos) {
		this.tbFcltCmdMstVos = tbFcltCmdMstVos;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
}
