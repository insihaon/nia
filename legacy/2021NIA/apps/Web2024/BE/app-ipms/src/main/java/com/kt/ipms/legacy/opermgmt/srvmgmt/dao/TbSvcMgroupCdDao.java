package com.kt.ipms.legacy.opermgmt.srvmgmt.dao;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.kt.ipms.legacy.opermgmt.srvmgmt.vo.TbSvcMgroupCdVo;


/** TB_SVC_MGROUP_CD DAO INTERFACE **/
@Mapper
public interface TbSvcMgroupCdDao {
	
	/** TB_SVC_MGROUP_CD insert **/
	public int insertTbSvcMgroupCdVo(TbSvcMgroupCdVo tbSvcMgroupCdVo) ;

	/** TB_SVC_MGROUP_CD update **/
	public int updateTbSvcMgroupCdVo(TbSvcMgroupCdVo tbSvcMgroupCdVo) ;

	/** TB_SVC_MGROUP_CD delete **/
	public int deleteTbSvcMgroupCdVo(TbSvcMgroupCdVo tbSvcMgroupCdVo) ;

	/** TB_SVC_MGROUP_CD select **/
	public TbSvcMgroupCdVo selectTbSvcMgroupCdVo(TbSvcMgroupCdVo tbSvcMgroupCdVo) ;

	/** TB_SVC_MGROUP_CD selectList **/
	public List<TbSvcMgroupCdVo> selectListTbSvcMgroupCdVo(TbSvcMgroupCdVo tbSvcMgroupCdVo) ;
	
	/** TB_SVC_MGROUP_CD selectListPage **/
	public List<TbSvcMgroupCdVo> selectListPageTbSvcMgroupCdVo(TbSvcMgroupCdVo tbSvcMgroupCdVo) ;

	/** TB_SVC_MGROUP_CD countListPage **/
	public int countListPageTbSvcMgroupCdVo(TbSvcMgroupCdVo tbSvcMgroupCdVo) ;

	
}