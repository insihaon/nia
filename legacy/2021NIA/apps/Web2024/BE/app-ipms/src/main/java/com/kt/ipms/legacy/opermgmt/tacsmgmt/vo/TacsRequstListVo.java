package com.kt.ipms.legacy.opermgmt.tacsmgmt.vo;

import java.io.Serializable;
import java.util.List;

import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class TacsRequstListVo extends CommonVo implements Serializable {

	private static final long serialVersionUID = 7431945285342102080L;
	
	private List<TacsRequestVo> tacsRequestVos;
	
	private int totalCount = 0;
	
	public List<TacsRequestVo> getTacsRequestVos() {
		return tacsRequestVos;
	}

	public void setTacsRequestVos(List<TacsRequestVo> tacsRequestVos) {
		this.tacsRequestVos = tacsRequestVos;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

}
