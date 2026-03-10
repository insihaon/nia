package com.kt.ipms.legacy.ipmgmt.assignmgmt.vo;

import java.io.Serializable;
import java.util.List;

import com.kt.ipms.legacy.cmn.vo.CommonVo;
import com.kt.ipms.legacy.opermgmt.nodemgmt.vo.NodeMgmtVo;

public class TbIpAssignMstComplexVo extends CommonVo implements Serializable {

	private static final long serialVersionUID = -4839658231691540191L;
	
	private TbIpAssignMstVo srcIpAssignMstVo;
	
	private List<TbIpAssignMstVo> destIpAssignMstVos;
	
	private NodeMgmtVo nodeMgmtVo;

	public TbIpAssignMstVo getSrcIpAssignMstVo() {
		return srcIpAssignMstVo;
	}

	public void setSrcIpAssignMstVo(TbIpAssignMstVo srcIpAssignMstVo) {
		this.srcIpAssignMstVo = srcIpAssignMstVo;
	}

	public List<TbIpAssignMstVo> getDestIpAssignMstVos() {
		return destIpAssignMstVos;
	}

	public void setDestIpAssignMstVos(List<TbIpAssignMstVo> destIpAssignMstVos) {
		this.destIpAssignMstVos = destIpAssignMstVos;
	}

	public NodeMgmtVo getNodeMgmtVo() {
		return nodeMgmtVo;
	}

	public void setNodeMgmtVo(NodeMgmtVo nodeMgmtVo) {
		this.nodeMgmtVo = nodeMgmtVo;
	}
	
	

}
