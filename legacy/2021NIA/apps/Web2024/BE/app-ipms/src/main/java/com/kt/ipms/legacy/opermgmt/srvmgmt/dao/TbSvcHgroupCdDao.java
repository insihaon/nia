package com.kt.ipms.legacy.opermgmt.srvmgmt.dao;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.kt.ipms.legacy.opermgmt.srvmgmt.vo.TbSvcHgroupCdVo;


/** TB_SVC_HGROUP_CD DAO INTERFACE **/
@Mapper
public interface TbSvcHgroupCdDao {
	
	/** TB_SVC_HGROUP_CD insert **/
	public int insertTbSvcHgroupCdVo(TbSvcHgroupCdVo tbSvcHgroupCdVo) ;

	/** TB_SVC_HGROUP_CD update **/
	public int updateTbSvcHgroupCdVo(TbSvcHgroupCdVo tbSvcHgroupCdVo) ;

	/** TB_SVC_HGROUP_CD delete **/
	public int deleteTbSvcHgroupCdVo(TbSvcHgroupCdVo tbSvcHgroupCdVo) ;

	/** TB_SVC_HGROUP_CD select **/
	public TbSvcHgroupCdVo selectTbSvcHgroupCdVo(TbSvcHgroupCdVo tbSvcHgroupCdVo) ;

	/** TB_SVC_HGROUP_CD selectList **/
	public List<TbSvcHgroupCdVo> selectListTbSvcHgroupCdVo(TbSvcHgroupCdVo tbSvcHgroupCdVo) ;
	
	/** TB_SVC_HGROUP_CD selectListPage **/
	public List<TbSvcHgroupCdVo> selectListPageTbSvcHgroupCdVo(TbSvcHgroupCdVo tbSvcHgroupCdVo) ;

	/** TB_SVC_HGROUP_CD countListPage **/
	public int countListPageTbSvcHgroupCdVo(TbSvcHgroupCdVo tbSvcHgroupCdVo) ;

	
}