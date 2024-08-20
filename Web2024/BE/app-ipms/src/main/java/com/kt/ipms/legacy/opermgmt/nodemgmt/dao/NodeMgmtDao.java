package com.kt.ipms.legacy.opermgmt.nodemgmt.dao;

import java.util.List;

import com.kt.ipms.legacy.opermgmt.nodemgmt.vo.NodeMgmtVo;

public interface NodeMgmtDao {

	public List<NodeMgmtVo> selectListNodeMgmtVo(NodeMgmtVo nodeMgmtVo);
	
	public int countListPageNodeMgmtVo(NodeMgmtVo nodeMgmtVo);
	
	public int insertNode(NodeMgmtVo nodeMgmtVo);
	
	public NodeMgmtVo selectNode(NodeMgmtVo nodeMgmtVo);
	
	public int deleteNode(NodeMgmtVo nodeMgmtVo);
	
	public int cancelNode(NodeMgmtVo nodeMgmtVo);
	
	public int comfirmDcompleteDate(NodeMgmtVo nodeMgmtVo);
}