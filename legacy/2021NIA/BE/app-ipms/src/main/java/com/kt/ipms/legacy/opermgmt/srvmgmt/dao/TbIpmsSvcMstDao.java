package com.kt.ipms.legacy.opermgmt.srvmgmt.dao;
import java.util.List;
import com.kt.ipms.legacy.opermgmt.srvmgmt.vo.TbIpmsSvcMstVo;


/** TB_IPMS_SVC_MST DAO INTERFACE **/
public interface TbIpmsSvcMstDao {
	
	/** TB_IPMS_SVC_MST insert **/
	public int insertTbIpmsSvcMstVo(TbIpmsSvcMstVo tbIpmsSvcMstVo) ;

	/** TB_IPMS_SVC_MST update **/
	public int updateTbIpmsSvcMstVo(TbIpmsSvcMstVo tbIpmsSvcMstVo) ;

	/** TB_IPMS_SVC_MST delete **/
	public int deleteTbIpmsSvcMstVo(TbIpmsSvcMstVo tbIpmsSvcMstVo) ;

	/** TB_IPMS_SVC_MST select **/
	public TbIpmsSvcMstVo selectTbIpmsSvcMstVo(TbIpmsSvcMstVo tbIpmsSvcMstVo) ;

	/** TB_IPMS_SVC_MST selectList **/
	public List<TbIpmsSvcMstVo> selectListTbIpmsSvcMstVo(TbIpmsSvcMstVo tbIpmsSvcMstVo) ;
	
	/** TB_IPMS_SVC_MST_selectListSvcMgroupNm **/
	public List<TbIpmsSvcMstVo> selectListSvcMgroupNmCd(TbIpmsSvcMstVo tbIpmsSvcMstVo) ;

	/** TB_IPMS_SVC_MST_selectListSvcMgroupNm1 **/
	public List<TbIpmsSvcMstVo> selectListSvcMgroupNm1Cd(TbIpmsSvcMstVo tbIpmsSvcMstVo) ;
	
	/** TB_SVC_MGROUP_CD selectListSvcMgroup **/
	public List<TbIpmsSvcMstVo> selectListSvcMgroup(TbIpmsSvcMstVo tbIpmsSvcMstVo) ;
	
	/** TB_SVC_LGROUP_CD selectListSvcLgroup **/
	public List<TbIpmsSvcMstVo> selectListSvcLgroup(TbIpmsSvcMstVo tbIpmsSvcMstVo) ;
	
	/** TB_SVC_MGROUP_CD selectSexLinkUseTypeCd **/
	public TbIpmsSvcMstVo selectSexLinkUseTypeCd(TbIpmsSvcMstVo tbIpmsSvcMstVo) ;
	
	/** TB_IPMS_SVC_MST selectListPage **/
	public List<TbIpmsSvcMstVo> selectListPageTbIpmsSvcMstVo(TbIpmsSvcMstVo tbIpmsSvcMstVo) ;

	/** TB_IPMS_SVC_MST countListPage **/
	public int countListPageTbIpmsSvcMstVo(TbIpmsSvcMstVo tbIpmsSvcMstVo) ;

	
}