package com.kt.ipms.legacy.ticketmgmt.dhcpmgmt.dao;
import java.util.List;

import com.kt.ipms.legacy.cmn.vo.CommonCodeVo;
import com.kt.ipms.legacy.ticketmgmt.dhcpmgmt.vo.TbAuditDhcpMstVo;



public interface TbAuditDhcpMstDao {
	

	public int insertTbAuditDhcpMstVo(TbAuditDhcpMstVo tbAuditDhcpMstVo);

	
	public int updateTbAuditDhcpMstVo(TbAuditDhcpMstVo tbAuditDhcpMstVo);
	
	
	public int updateTbAuditDhcpMstBflagY(TbAuditDhcpMstVo tbAuditDhcpMstVo);

	
	public int deleteTbAuditDhcpMstVo(TbAuditDhcpMstVo tbAuditDhcpMstVo);


	public TbAuditDhcpMstVo selectTbAuditDhcpMstVo(TbAuditDhcpMstVo tbAuditDhcpMstVo);


	public List<TbAuditDhcpMstVo> selectListTbAuditDhcpMstVo(TbAuditDhcpMstVo tbAuditDhcpMstVo);
	

	public List<TbAuditDhcpMstVo> selectListPageTbAuditDhcpMstVo(TbAuditDhcpMstVo tbAuditDhcpMstVo);
	

	public int countListPageTbAuditDhcpMstVo(TbAuditDhcpMstVo tbAuditDhcpMstVo);
	

	public List<CommonCodeVo> selectOrderSofficeList(TbAuditDhcpMstVo tbAuditDhcpMstVo);
	
	
	public List<TbAuditDhcpMstVo> selectSetOrderOfficeList(TbAuditDhcpMstVo tbAuditDhcpMstVo);
	

}