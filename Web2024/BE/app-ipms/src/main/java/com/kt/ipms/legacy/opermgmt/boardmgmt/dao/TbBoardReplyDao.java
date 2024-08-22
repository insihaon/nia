package com.kt.ipms.legacy.opermgmt.boardmgmt.dao;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.kt.ipms.legacy.opermgmt.boardmgmt.vo.TbBoardReplyVo;

/** TB_BOARD_REPLY DAO INTERFACE **/
@Mapper
public interface TbBoardReplyDao {
	
	/** TB_BOARD_REPLY insert **/
	public int insertTbBoardReplyVo(TbBoardReplyVo tbBoardReplyVo) ;
	
	/** TB_BOARD_REPLY update **/
	public int updateTbBoardReplyVo(TbBoardReplyVo tbBoardReplyVo) ;
	
	/** TB_BOARD_REPLY delete **/
	public int deleteTbBoardReplyVo(TbBoardReplyVo tbBoardReplyVo) ;
	
	/** TB_BOARD_REPLY select **/
	public TbBoardReplyVo selectTbBoardReplyVo(TbBoardReplyVo tbBoardReplyVo) ;
	
	/** TB_BOARD_REPLY selectList **/
	public List<TbBoardReplyVo> selectListTbBoardReplyVo(TbBoardReplyVo tbBoardReplyVo) ;
}
