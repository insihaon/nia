package com.kt.ipms.legacy.cmn.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.kt.ipms.legacy.cmn.dao.CommonCodeDao;
import com.kt.ipms.legacy.cmn.util.CommonCodeUtil;
import com.kt.ipms.legacy.cmn.vo.CommonCodeVo;


@Component
public class CommonCodeService {

	
	@Lazy @Autowired
	private CommonCodeDao commonCodeDao;
	
	public List<CommonCodeVo> selectListCommonCode(String codeType, Object paramObject) {
		
		if (codeType.equals(CommonCodeUtil.IP_CREATE_TYPE_CD)) {
			return commonCodeDao.selectListIpCreateTypeCd(paramObject);
		} else if (codeType.equals(CommonCodeUtil.IP_CREATE_SEQ_CD)) {
			return commonCodeDao.selectListIpCreateSeqCd();
		} else if (codeType.equals(CommonCodeUtil.IP_CREATE_SEQ_CD2)) {
			return commonCodeDao.selectListIpCreateSeqCd2();
		} else if (codeType.equals(CommonCodeUtil.IP_VERSION_TYPE_CD)) {
			return commonCodeDao.selectListIpVersionTypeCd();
		} else if (codeType.equals(CommonCodeUtil.USE_STATUS_CD)) {
			return commonCodeDao.selectListUseStatusCd();
		} else if (codeType.equals(CommonCodeUtil.USE_TYPE_CD)) {
			return commonCodeDao.selectListUseTypeCd();
		} else if (codeType.equals(CommonCodeUtil.BOARD_TYPE_CD)) {
			return commonCodeDao.selectListBoardTypeCd();
		} else if (codeType.equals(CommonCodeUtil.BOARD_TYPE_SUB_CD)) {
			return commonCodeDao.selectListBoardTypeSubCd(paramObject);
		} else if (codeType.equals(CommonCodeUtil.EXTERNAL_LINK_USE_TYPE_CD)) {
			return commonCodeDao.selectListExternalLinkUseTypeCd();
		} else if (codeType.equals(CommonCodeUtil.HNDSET_USE_STTUS_CD)) {
			return commonCodeDao.selectListHndsetUseSttusCd();
		} else if (codeType.equals(CommonCodeUtil.MENU_HIER_TYPE_CD)) {
			return commonCodeDao.selectListMenuHierTypeCd();
		} else if (codeType.equals(CommonCodeUtil.ORG_BCOM_TYPE_CD)) {
			return commonCodeDao.selectListOrgBcomTypeCd();
		} else if (codeType.equals(CommonCodeUtil.PRSNL_ORG_TYPE_CD)) {
			return commonCodeDao.selectListPrsnlOrgTypeCd();
		} else if (codeType.equals(CommonCodeUtil.SCRN_TYPE_CD)) {
			return commonCodeDao.selectListScrnTypeCd();
		} else if (codeType.equals(CommonCodeUtil.SCRN_URL_TYPE_CD)) {
			return commonCodeDao.selectListScrnUrlTypeCd();
		} else if (codeType.equals(CommonCodeUtil.SVC_HGROUP_CD)) {
			return commonCodeDao.selectListSvcHgroupCd();
		} else if (codeType.equals(CommonCodeUtil.SVC_MGROUP_CD)) {
			return commonCodeDao.selectListSvcMgroupCd();
		} else if (codeType.equals(CommonCodeUtil.SVC_LGROUP_CD)) {
			return commonCodeDao.selectListSvcLgroupCd();
		} else if (codeType.equals(CommonCodeUtil.SVC_LINE_TYPE_CD)) {
			return commonCodeDao.selectListSvcLineTypeCd(paramObject);
		} else if (codeType.equals(CommonCodeUtil.SVC_USE_TYPE_CD)) {
			return commonCodeDao.selectListSvcUseTypeCd();
		} else if (codeType.equals(CommonCodeUtil.SVCNW_TYPE_CD)) {
			return commonCodeDao.selectListSvcnwTypeCd();
		} else if (codeType.equals(CommonCodeUtil.USER_CONN_RESLT_TYPE_CD)) {
			return commonCodeDao.selectListUserConnResltTypeCd();
		} else if (codeType.equals(CommonCodeUtil.USER_GRADE_CD)) {
			return commonCodeDao.selectListUserGradeCd();
		} else if (codeType.equals(CommonCodeUtil.USER_STTUS_CD)) {
			return commonCodeDao.selectListUserSttusCd();
		} else if (codeType.equals(CommonCodeUtil.USE_TYPE_CD)) {
			return commonCodeDao.selectListUserTypeCd();
		} else if (codeType.equals(CommonCodeUtil.LVL_BAS_LEVEL_CD)) {
			return commonCodeDao.selectListLvlBasLevelCd();
		} else if (codeType.equals(CommonCodeUtil.ASSIGN_LEVEL_CD)) {
			return commonCodeDao.selectListAssignLevelCd(paramObject);
		} else if (codeType.equals(CommonCodeUtil.ASSIGN_TYPE_CD)) {
			return commonCodeDao.selectListAssignTypeCd(paramObject);
		} else if (codeType.equals(CommonCodeUtil.ASG_ASSIGN_LEVEL_CD)) {
			return commonCodeDao.selectListAsgAssignLevelCd();
		} else if (codeType.equals(CommonCodeUtil.ALC_ASSIGN_LEVEL_CD)) {
			return commonCodeDao.selectListAlcAssignLevelCd();
		} else if (codeType.equals(CommonCodeUtil.LVL_SUB_CD)) {
			return commonCodeDao.selectListLvlSubCd();
		} else if (codeType.equals(CommonCodeUtil.SEX_LINK_USE_TYPE_CD)) {
			return commonCodeDao.selectListSexLinkUseTypeCd();
		} else if (codeType.equals(CommonCodeUtil.SREQUEST_ASSIGN_TYPE_CD)) {
			return commonCodeDao.selectListRequestAssignTypeCd();
		} else if (codeType.equals(CommonCodeUtil.SREQUEST_AS_TYPE_CD)) {
			return commonCodeDao.selectListRequestAsTypeCd();
		} else if (codeType.equals(CommonCodeUtil.TACS_FLCT_TYPE_CD)) {
			return commonCodeDao.selectListTacsFcltTypeCd(paramObject);
		} else if (codeType.equals(CommonCodeUtil.FLCT_TYPE_CD)) {
			return commonCodeDao.selectListFcltTypeCd(paramObject);
		}else if (codeType.equals(CommonCodeUtil.INTGRM_RSLT_CD)) {
			return commonCodeDao.selectListIntgrmRsltTypeCd(paramObject);
		}else if (codeType.equals(CommonCodeUtil.ROUTING_EXCEPT_CD)) {
			return commonCodeDao.selectListIntgrmExcptTypeCd(paramObject);
		}else if (codeType.equals(CommonCodeUtil.IP_PRIVATE_REQ_MST_SEQ_CD)) {
			return commonCodeDao.selectListIpPrivateReqMstSeqCd();
		}else if (codeType.equals(CommonCodeUtil.HIST_TASK_CD)) {
			return commonCodeDao.selectListHistTaskCd();
		}else {
			return null;
		}
	}
}
