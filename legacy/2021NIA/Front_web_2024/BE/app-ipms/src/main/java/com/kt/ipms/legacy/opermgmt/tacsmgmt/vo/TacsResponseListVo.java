package com.kt.ipms.legacy.opermgmt.tacsmgmt.vo;

import java.io.Serializable;
import java.util.List;

import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class TacsResponseListVo extends CommonVo implements Serializable {

	private static final long serialVersionUID = 223689975230953523L;
	
	private List<TacsResponseVo> tacsResponseVos;
	
	private int totalCount = 0;

	public List<TacsResponseVo> getTacsResponseVos() {
		return tacsResponseVos;
	}

	public void setTacsResponseVos(List<TacsResponseVo> tacsResponseVos) {
		this.tacsResponseVos = tacsResponseVos;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

}
