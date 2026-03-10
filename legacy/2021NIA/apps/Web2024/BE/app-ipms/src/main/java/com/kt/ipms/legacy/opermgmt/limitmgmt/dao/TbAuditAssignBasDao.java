package com.kt.ipms.legacy.opermgmt.limitmgmt.dao;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.kt.ipms.legacy.opermgmt.limitmgmt.vo.TbAuditAssignBasVo;
import com.kt.ipms.legacy.opermgmt.limitmgmt.vo.TbAuditDhcpBasVo;


/** TB_AUDIT_ASSIGN_BAS DAO INTERFACE **/
@Mapper
public interface TbAuditAssignBasDao {
	
	/** TB_AUDIT_ASSIGN_BAS insertTbAuditAssignBasVo **/
	public int insertTbAuditAssignBasVo(TbAuditAssignBasVo tbAuditAssignBasVo);

	/** TB_AUDIT_ASSIGN_BAS updateTbAuditAssignBasVo **/
	public int updateTbAuditAssignBasVo(TbAuditAssignBasVo tbAuditAssignBasVo);

	/** TB_AUDIT_ASSIGN_BAS deleteTbAuditAssignBasVo **/
	public int deleteTbAuditAssignBasVo(TbAuditAssignBasVo tbAuditAssignBasVo);

	/** TB_AUDIT_ASSIGN_BAS selectTbAuditAssignBasVo **/
	public TbAuditAssignBasVo selectTbAuditAssignBasVo(TbAuditDhcpBasVo tbAuditDhcpBasVo);

	/** TB_AUDIT_ASSIGN_BAS selectListTbAuditAssignBasVo **/
	public List<TbAuditAssignBasVo> selectListTbAuditAssignBasVo(TbAuditAssignBasVo tbAuditAssignBasVo);
	
	/** TB_AUDIT_ASSIGN_BAS countListTbAuditAssignBasVo **/
	public int countListTbAuditAssignBasVo(TbAuditAssignBasVo tbAuditAssignBasVo);
	
	/** TB_AUDIT_ASSIGN_BAS selectListPageTbAuditAssignBasVo **/
	public List<TbAuditAssignBasVo> selectListPageTbAuditAssignBasVo(TbAuditAssignBasVo tbAuditAssignBasVo);

	/** TB_AUDIT_ASSIGN_BAS countListPageTbAuditAssignBasVo **/
	public int countListPageTbAuditAssignBasVo(TbAuditAssignBasVo tbAuditAssignBasVo);

	
}