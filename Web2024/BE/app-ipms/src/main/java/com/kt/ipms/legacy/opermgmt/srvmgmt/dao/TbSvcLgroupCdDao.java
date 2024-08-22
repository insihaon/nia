package com.kt.ipms.legacy.opermgmt.srvmgmt.dao;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.kt.ipms.legacy.opermgmt.srvmgmt.vo.TbSvcLgroupCdVo;


/** TB_SVC_LGROUP_CD DAO INTERFACE **/
@Mapper
public interface TbSvcLgroupCdDao {
	
	/** TB_SVC_LGROUP_CD insert **/
	public int insertTbSvcLgroupCdVo(TbSvcLgroupCdVo tbSvcLgroupCdVo) ;

	/** TB_SVC_LGROUP_CD update **/
	public int updateTbSvcLgroupCdVo(TbSvcLgroupCdVo tbSvcLgroupCdVo) ;

	/** TB_SVC_LGROUP_CD delete **/
	public int deleteTbSvcLgroupCdVo(TbSvcLgroupCdVo tbSvcLgroupCdVo) ;

	/** TB_SVC_LGROUP_CD select **/
	public TbSvcLgroupCdVo selectTbSvcLgroupCdVo(TbSvcLgroupCdVo tbSvcLgroupCdVo) ;

	/** TB_SVC_LGROUP_CD selectList **/
	public List<TbSvcLgroupCdVo> selectListTbSvcLgroupCdVo(TbSvcLgroupCdVo tbSvcLgroupCdVo) ;
	
	/** TB_SVC_LGROUP_CD selectListPage **/
	public List<TbSvcLgroupCdVo> selectListPageTbSvcLgroupCdVo(TbSvcLgroupCdVo tbSvcLgroupCdVo) ;

	/** TB_SVC_LGROUP_CD countListPage **/
	public int countListPageTbSvcLgroupCdVo(TbSvcLgroupCdVo tbSvcLgroupCdVo) ;
	
	/** TB_SRCN_BAS selectNewLgroupCd **/
	public String selectNewLgroupCd() ;

}