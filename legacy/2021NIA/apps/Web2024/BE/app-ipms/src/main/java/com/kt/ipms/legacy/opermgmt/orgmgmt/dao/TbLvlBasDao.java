package com.kt.ipms.legacy.opermgmt.orgmgmt.dao;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlBasVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlRoleMstVo;


/** TB_LVL_BAS DAO INTERFACE **/
@Mapper
public interface TbLvlBasDao {
	
	/** TB_LVL_BAS insert **/
	public int insertTbLvlBasVo(TbLvlRoleMstVo tbLvlBasVo) ;

	/** TB_LVL_BAS update **/
	public int updateTbLvlBasVo(TbLvlBasVo tbLvlBasVo) ;

	/** TB_LVL_BAS delete **/
	public int deleteTbLvlBasVo(TbLvlBasVo tbLvlBasVo) ;

	/** TB_LVL_BAS select **/
	public TbLvlBasVo selectTbLvlBasVo(TbLvlBasVo tbLvlBasVo) ;

	/** TB_LVL_BAS selectList **/
	public List<TbLvlBasVo> selectListTbLvlBasVo(TbLvlBasVo tbLvlBasVo) ;
	
	/** TB_LVL_BAS selectListSvcLine **/
	public List<TbLvlBasVo> selectListSvcLine(TbLvlBasVo tbLvlBasVo) ;
	
	/** TB_LVL_BAS selectlistCenter **/
	public List<TbLvlBasVo> selectlistCenter(TbLvlBasVo tbLvlBasVo) ;
	
	/** TB_LVL_BAS selectlistNode **/
	public List<TbLvlBasVo> selectlistNode(TbLvlBasVo tbLvlBasVo) ;
	
	/** TB_LVL_BAS selectListPage **/
	public List<TbLvlBasVo> selectListPageTbLvlBasVo(TbLvlBasVo tbLvlBasVo) ;

	/** TB_LVL_BAS countListPage **/
	public int countListPageTbLvlBasVo(TbLvlBasVo tbLvlBasVo) ;
	
	/** TB_LVL_BAS selectListSvcLineALL **/
	public List<TbLvlBasVo> selectListSvcLineAll() ;
	
	/** TB_LVL_BAS selectlistCenterALL **/
	public List<TbLvlBasVo> selectlistCenterAll() ;
	
	/** TB_LVL_BAS selectlistNodeALL **/
	public List<TbLvlBasVo> selectlistNodeAll() ;
	
	/** TB_LVL_BAS countSsvcGroupCd **/
	public int countSsvcGroupCd(TbLvlBasVo tbLvlBasVo) ;
	
	/** TB_LVL_BAS countNodeTbLvlBasVo **/
	public int countNodeTbLvlBasVo(TbLvlBasVo tbLvlBasVo) ;


}