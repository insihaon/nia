package com.kt.ipms.legacy.opermgmt.boardmgmt.dao;
import java.util.List;
import com.kt.ipms.legacy.opermgmt.boardmgmt.vo.TbBoardVo;


/** TB_BOARD DAO INTERFACE **/
public interface TbBoardDao {
	
	/** TB_BOARD insert **/
	public int insertTbBoardVo(TbBoardVo tbBoardVo) ;

	/** TB_BOARD update **/
	public int updateTbBoardVo(TbBoardVo tbBoardVo) ;

	/** TB_BOARD delete **/
	public int deleteTbBoardVo(TbBoardVo tbBoardVo) ;

	/** TB_BOARD select **/
	public TbBoardVo selectTbBoardVo(TbBoardVo tbBoardVo) ;

	/** TB_BOARD selectList **/
	public List<TbBoardVo> selectListTbBoardVo(TbBoardVo tbBoardVo) ;
	
	/** TB_BOARD selectListPage **/
	public List<TbBoardVo> selectListPageTbBoardVo(TbBoardVo tbBoardVo) ;

	/** TB_BOARD countListPage **/
	public int countListPageTbBoardVo(TbBoardVo tbBoardVo) ;
	
	/** TB_BOARD updateNreadCnt **/
	public int updateNreadCnt(TbBoardVo tbBoardVo) ;
	
	
	/** TB_BOARD selectList **/
	public List<TbBoardVo> selectListTbBoardByMain() ;
	
}