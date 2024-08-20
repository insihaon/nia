package com.kt.ipms.legacy.ipmgmt.assignmgmt.dao;
import java.util.List;

import com.kt.ipms.legacy.ipmgmt.assignmgmt.vo.TbIpAssignMstListVo;
import com.kt.ipms.legacy.ipmgmt.assignmgmt.vo.TbIpAssignMstVo;


/** TB_IP_ASSIGN_MST DAO INTERFACE **/
public interface TbIpAssignMstDao {
	
	/** TB_IP_ASSIGN_MST insert **/
	public int insertTbIpAssignMstVo(TbIpAssignMstVo tbIpAssignMstVo);

	/** TB_IP_ASSIGN_MST update **/
	public int updateTbIpAssignMstVo(TbIpAssignMstVo tbIpAssignMstVo);
	
	/** TB_IP_ASSIGN_MST update(Aloc) 할당정보수정 **/
	public int updateAlocTbIpAssignMstVo(TbIpAssignMstVo tbIpAssignMstVo);

	/** TB_IP_ASSIGN_MST update(scomment) 비고수정 **/
	public int updateScommentTbIpAssignMstVo(TbIpAssignMstVo tbIpAssignMstVo);
	
	/** TB_IP_ASSIGN_MST delete **/
	public int deleteTbIpAssignMstVo(TbIpAssignMstVo tbIpAssignMstVo);

	/** TB_IP_ASSIGN_MST select **/
	public TbIpAssignMstVo selectTbIpAssignMstVo(TbIpAssignMstVo tbIpAssignMstVo);

	/** TB_IP_ASSIGN_MST selectList **/
	public List<TbIpAssignMstVo> selectListTbIpAssignMstVo(TbIpAssignMstVo tbIpAssignMstVo);
	
	/** TB_IP_ASSIGN_MST selectListPage **/
	public List<TbIpAssignMstVo> selectListPageTbIpAssignMstVo(TbIpAssignMstVo tbIpAssignMstVo);

	/** TB_IP_ASSIGN_MST countListPage **/
	public int countListPageTbIpAssignMstVo(TbIpAssignMstVo tbIpAssignMstVo);

	/** TB_IP_ASSIGN_MST countAssignBlockViaTbIpAssignMstVo **/
	public int countAssignBlockViaTbIpAssignMstVo(TbIpAssignMstVo tbIpAssignMstVo);
	
	/** TB_IP_ASSIGN_MST deleteListTbIpAssignMstVo **/
	public int deleteListTbIpAssignMstVo(TbIpAssignMstVo tbIpAssignMstVo);
	
	/** TB_IP_ASSIGN_MST selectListTbIpAssignMstVoViaInMstSeq **/
	public List<TbIpAssignMstVo> selectListTbIpAssignMstVoViaInMstSeq(TbIpAssignMstListVo tbIpAssignMstListVo);
	
	/** TB_IP_ASSIGN_MST updateSipCreateSeqCdTbIpAssignMstVo **/
	public int updateSipCreateSeqCdTbIpAssignMstVo(TbIpAssignMstVo tbIpAssignMstVo);
	
	/** TB_IP_ASSIGN_MST selectListUnAssignBlock **/
	public List<TbIpAssignMstVo> selectListUnAssignBlock(TbIpAssignMstVo tbIpAssignMstVo);
	
	/** TB_IP_ASSIGN_MST countselectListUnAssignBlock **/
	public int countSelectListUnAssignBlock(TbIpAssignMstVo tbIpAssignMstVo);
	
	/** TB_IP_ASSIGN_MST selectListPageOptimizeIpSource **/
	public List<TbIpAssignMstVo> selectListPageOptimizeIpSource(TbIpAssignMstVo tbIpAssignMstVo);
	
	/** TB_IP_ASSIGN_MST countListPageOptimizeIpSource **/
	public int countListPageOptimizeIpSource(TbIpAssignMstVo tbIpAssignMstVo);
	
	/** TB_IP_ASSIGN_MST selectListOptimizeIpTarget **/
	public List<TbIpAssignMstVo> selectListOptimizeIpTarget(TbIpAssignMstVo tbIpAssignMstVo);
	
	/** TB_IP_ASSIGN_MST countListOptimizeIpTarget **/
	public int countListOptimizeIpTarget(TbIpAssignMstVo tbIpAssignMstVo);
	
	/** TB_IP_ASSIGN_MST selectListOptimizeIpRecommend **/
	public List<TbIpAssignMstVo> selectListOptimizeIpRecommend(TbIpAssignMstVo tbIpAssignMstVo);
	
	/** TB_IP_ASSIGN_MST countListOptimizeIpRecommend **/
	public int countListOptimizeIpRecommend(TbIpAssignMstVo tbIpAssignMstVo);
	
	/** TB_IP_ASSIGN_MST checkCountAsgnIPMst **/
	public int checkCountAsgnIPMst(TbIpAssignMstVo tbIpAssignMstVo);
	
	/** TB_IP_ASSIGN_MST select **/
	public List<TbIpAssignMstVo> selectSummaryDetailKornet(TbIpAssignMstVo tbIpAssignMstVo);
	
	public int countListPageSummaryDetailKornet(TbIpAssignMstVo tbIpAssignMstVo);
	
	public List<TbIpAssignMstVo> selectSummaryDetailPremium(TbIpAssignMstVo tbIpAssignMstVo);
	
	public int countListPageSummaryDetailPremium(TbIpAssignMstVo tbIpAssignMstVo);
	
	
}