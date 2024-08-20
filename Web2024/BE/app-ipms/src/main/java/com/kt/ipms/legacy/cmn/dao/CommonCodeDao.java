package com.kt.ipms.legacy.cmn.dao;

import java.util.List;

import com.kt.ipms.legacy.cmn.vo.CommonCodeVo;

public interface CommonCodeDao {
	
	public List<CommonCodeVo> selectListIpVersionTypeCd();
	
	public List<CommonCodeVo> selectListIpCreateTypeCd(Object paramObject);
	
	public List<CommonCodeVo> selectListIpCreateSeqCd();
	
	public List<CommonCodeVo> selectListIpCreateSeqCd2();
		
	public List<CommonCodeVo> selectListUseStatusCd();
	
	public List<CommonCodeVo> selectListUseTypeCd();
	
	public List<CommonCodeVo> selectListBoardTypeCd();
	
	public List<CommonCodeVo> selectListBoardTypeSubCd(Object paramObject);
	
	public List<CommonCodeVo> selectListExternalLinkUseTypeCd();
	
	public List<CommonCodeVo> selectListHndsetUseSttusCd();
	
	public List<CommonCodeVo> selectListMenuHierTypeCd();
	
	public List<CommonCodeVo> selectListOrgBcomTypeCd();
	
	public List<CommonCodeVo> selectListPrsnlOrgTypeCd();
	
	public List<CommonCodeVo> selectListScrnTypeCd();
	
	public List<CommonCodeVo> selectListScrnUrlTypeCd();
	
	public List<CommonCodeVo> selectListSvcHgroupCd();
	
	public List<CommonCodeVo> selectListSvcMgroupCd();
	
	public List<CommonCodeVo> selectListSvcLgroupCd();
	
	public List<CommonCodeVo> selectListSvcLineTypeCd(Object paramObject);
	
	public List<CommonCodeVo> selectListSvcUseTypeCd();
	
	public List<CommonCodeVo> selectListSvcnwTypeCd();
	
	public List<CommonCodeVo> selectListUserConnResltTypeCd();
	
	public List<CommonCodeVo> selectListUserGradeCd();
	
	public List<CommonCodeVo> selectListUserSttusCd();
	
	public List<CommonCodeVo> selectListUserTypeCd();
	
	public List<CommonCodeVo> selectListLvlBasLevelCd();
	
	public List<CommonCodeVo> selectListAssignLevelCd(Object paramObject);
	
	public List<CommonCodeVo> selectListAssignTypeCd(Object paramObject);
	
	public List<CommonCodeVo> selectListAsgAssignLevelCd();
	
	public List<CommonCodeVo> selectListAlcAssignLevelCd();
	
	public List<CommonCodeVo> selectListLvlSubCd();
	
	public List<CommonCodeVo> selectListSexLinkUseTypeCd();
	
	public List<CommonCodeVo> selectListRequestAssignTypeCd();
	
	public List<CommonCodeVo> selectListRequestAsTypeCd();
	
	public List<CommonCodeVo> selectListTacsFcltTypeCd(Object paramObject);
	
	public List<CommonCodeVo> selectListFcltTypeCd(Object paramObject);
	
	public List<CommonCodeVo> selectListIntgrmRsltTypeCd(Object paramObject);
	
	public List<CommonCodeVo> selectListIntgrmExcptTypeCd(Object paramObject);

	public List<CommonCodeVo> selectListIpPrivateReqMstSeqCd();
	
	public List<CommonCodeVo> selectListHistTaskCd();
	
	
}
