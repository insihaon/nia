package com.kt.ipms.legacy.opermgmt.limitmgmt.dao;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.kt.ipms.legacy.opermgmt.limitmgmt.vo.TbAuditDhcpBasVo;


/** TB_AUDIT_DHCP_BAS DAO INTERFACE **/
@Mapper
public interface TbAuditDhcpBasDao {
	
	/** TB_AUDIT_DHCP_BAS insertTbAuditDhcpBasVo **/
	public int insertTbAuditDhcpBasVo(TbAuditDhcpBasVo tbAuditDhcpBasVo);

	/** TB_AUDIT_DHCP_BAS updateTbAuditDhcpBasVo **/
	public int updateTbAuditDhcpBasVo(TbAuditDhcpBasVo tbAuditDhcpBasVo);

	/** TB_AUDIT_DHCP_BAS deleteTbAuditDhcpBasVo **/
	public int deleteTbAuditDhcpBasVo(TbAuditDhcpBasVo tbAuditDhcpBasVo);

	/** TB_AUDIT_DHCP_BAS selectTbAuditDhcpBasVo **/
	public TbAuditDhcpBasVo selectTbAuditDhcpBasVo(TbAuditDhcpBasVo tbAuditDhcpBasVo);

	/** TB_AUDIT_DHCP_BAS selectListTbAuditDhcpBasVo **/
	public List<TbAuditDhcpBasVo> selectListTbAuditDhcpBasVo(TbAuditDhcpBasVo tbAuditDhcpBasVo);
	
	/** TB_AUDIT_DHCP_BAS countListTbAuditDhcpBasVo **/
	public int countListTbAuditDhcpBasVo(TbAuditDhcpBasVo tbAuditDhcpBasVo);
	
	/** TB_AUDIT_DHCP_BAS selectListPageTbAuditDhcpBasVo **/
	public List<TbAuditDhcpBasVo> selectListPageTbAuditDhcpBasVo(TbAuditDhcpBasVo tbAuditDhcpBasVo);

	/** TB_AUDIT_DHCP_BAS countListPageTbAuditDhcpBasVo **/
	public int countListPageTbAuditDhcpBasVo(TbAuditDhcpBasVo tbAuditDhcpBasVo);

	
}