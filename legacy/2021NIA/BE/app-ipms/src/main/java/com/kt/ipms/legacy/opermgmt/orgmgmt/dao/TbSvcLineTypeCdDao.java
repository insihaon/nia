package com.kt.ipms.legacy.opermgmt.orgmgmt.dao;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbSvcLineTypeCdVo;


/** TB_SVC_LINE_TYPE_CD DAO INTERFACE **/
@Mapper
public interface TbSvcLineTypeCdDao {
	
	/** TB_SVC_LINE_TYPE_CD insert **/
	public int insertTbSvcLineTypeCdVo(TbSvcLineTypeCdVo  tbSvcLineTypeCdVo) ;

	/** TB_SVC_LINE_TYPE_CD update **/
	public int updateTbSvcLineTypeCdVo(TbSvcLineTypeCdVo tbSvcLineTypeCdVo) ;

	/** TB_SVC_LINE_TYPE_CD delete **/
	public int deleteTbSvcLineTypeCdVo(TbSvcLineTypeCdVo tbSvcLineTypeCdVo) ;

	/** TB_SVC_LINE_TYPE_CD select **/
	public TbSvcLineTypeCdVo selectTbSvcLineTypeCdVo(TbSvcLineTypeCdVo tbSvcLineTypeCdVo) ;

	/** TB_SVC_LINE_TYPE_CD selectList **/
	public List<TbSvcLineTypeCdVo> selectListTbSvcLineTypeCdVo(TbSvcLineTypeCdVo tbSvcLineTypeCdVo) ;
	
	/** TB_SVC_LINE_TYPE_CD selectListPage **/
	public List<TbSvcLineTypeCdVo> selectListPageTbSvcLineTypeCdVo(TbSvcLineTypeCdVo tbSvcLineTypeCdVo) ;

	/** TB_SVC_LINE_TYPE_CD countListPage **/
	public int countListPageTbSvcLineTypeCdVo(TbSvcLineTypeCdVo tbSvcLineTypeCdVo) ;
	
	/** TB_SVC_LINE_TYPE_CD newLinetypeCd **/
	public String selectNewLinetypeCd() ;
	
}