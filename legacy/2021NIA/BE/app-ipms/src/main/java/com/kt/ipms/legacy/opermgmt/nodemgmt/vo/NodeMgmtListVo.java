package com.kt.ipms.legacy.opermgmt.nodemgmt.vo;

import java.io.Serializable;
import java.util.List;

import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class NodeMgmtListVo extends CommonVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3854189260907258521L;
	
	private int totalCount;
	
	private List<NodeMgmtVo> nodeMgmtVos;

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public List<NodeMgmtVo> getNodeMgmtVos() {
		return nodeMgmtVos;
	}

	public void setNodeMgmtVos(List<NodeMgmtVo> nodeMgmtVos) {
		this.nodeMgmtVos = nodeMgmtVos;
	}
	
	
}
