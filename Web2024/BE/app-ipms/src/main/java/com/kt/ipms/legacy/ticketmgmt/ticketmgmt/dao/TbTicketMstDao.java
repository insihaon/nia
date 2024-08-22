package com.kt.ipms.legacy.ticketmgmt.ticketmgmt.dao;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.kt.ipms.legacy.ticketmgmt.ticketmgmt.vo.TbTicketMstVo;


/** TB_TICKET_MST DAO INTERFACE **/
@Mapper
public interface TbTicketMstDao {
	
	/** TB_TICKET_MST insertTbTicketMstVo **/
	public int insertTbTicketMstVo(TbTicketMstVo tbTicketMstVo);

	/** TB_TICKET_MST updateTbTicketMstVo **/
	public int updateTbTicketMstVo(TbTicketMstVo tbTicketMstVo);

	/** TB_TICKET_MST deleteTbTicketMstVo **/
	public int deleteTbTicketMstVo(TbTicketMstVo tbTicketMstVo);

	/** TB_TICKET_MST selectTbTicketMstVo **/
	public TbTicketMstVo selectTbTicketMstVo(TbTicketMstVo tbTicketMstVo);

	/** TB_TICKET_MST selectListTbTicketMstVo **/
	public List<TbTicketMstVo> selectListTbTicketMstVo(TbTicketMstVo tbTicketMstVo);
	
	/** TB_TICKET_MST countListTbTicketMstVo **/
	public int countListTbTicketMstVo(TbTicketMstVo tbTicketMstVo);
	
	/** TB_TICKET_MST selectListPageTbTicketMstVo **/
	public List<TbTicketMstVo> selectListPageTbTicketMstVo(TbTicketMstVo tbTicketMstVo);

	/** TB_TICKET_MST countListPageTbTicketMstVo **/
	public int countListPageTbTicketMstVo(TbTicketMstVo tbTicketMstVo);

	
}