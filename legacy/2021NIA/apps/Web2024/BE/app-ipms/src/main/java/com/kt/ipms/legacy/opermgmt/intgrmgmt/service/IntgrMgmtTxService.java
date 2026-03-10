package com.kt.ipms.legacy.opermgmt.intgrmgmt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kt.ipms.legacy.opermgmt.intgrmgmt.dao.TbDefaultSvcMstDao;
import com.kt.ipms.legacy.opermgmt.intgrmgmt.dao.TbFcltCmdMstDao;
import com.kt.ipms.legacy.opermgmt.intgrmgmt.dao.TbFcltMstDao;
import com.kt.ipms.legacy.opermgmt.intgrmgmt.dao.TbMobileMstDao;
import com.kt.ipms.legacy.opermgmt.intgrmgmt.dao.TbMobileSummMstDao;
import com.kt.ipms.legacy.opermgmt.intgrmgmt.dao.TbRoutHistMstDao;
import com.kt.ipms.legacy.opermgmt.intgrmgmt.dao.TbWireMstDao;
import com.kt.ipms.legacy.opermgmt.intgrmgmt.vo.TbDefaultSvcMstVo;
import com.kt.ipms.legacy.opermgmt.intgrmgmt.vo.TbFcltCmdMstVo;
import com.kt.ipms.legacy.opermgmt.intgrmgmt.vo.TbFcltMstVo;
import com.kt.ipms.legacy.opermgmt.intgrmgmt.vo.TbMobileMstVo;
import com.kt.ipms.legacy.opermgmt.intgrmgmt.vo.TbMobileSummMstVo;
import com.kt.ipms.legacy.opermgmt.intgrmgmt.vo.TbRoutHistMstVo;
import com.kt.ipms.legacy.opermgmt.intgrmgmt.vo.TbWireMstVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.adapter.OrgMgmtAdapterService;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlBasVo;

@Component
public class IntgrMgmtTxService {
	
	@Lazy @Autowired
	private TbFcltMstDao tbFcltMstDao;

	@Lazy @Autowired
	private TbFcltCmdMstDao tbFcltCmdMstDao;

	@Lazy @Autowired
	private TbMobileMstDao tbMobileMstDao;

	@Lazy @Autowired
	private TbMobileSummMstDao tbMobileSummMstDao;

@Lazy @Autowired
	private OrgMgmtAdapterService orgMgmtAdapterService;

	@Lazy @Autowired
	private TbDefaultSvcMstDao tbDefaultSvcMstDao;

	@Lazy @Autowired
	private TbRoutHistMstDao tbRoutHistMstDao;

	@Lazy @Autowired
	private TbWireMstDao tbWireMstDao;

	/****************************************************************************************
	 * 조직별 장비 정보관리
	 ****************************************************************************************/
	@Transactional(readOnly = true)
	public List<TbFcltMstVo> selectListPageFcltMstVo(TbFcltMstVo tbFcltMstVo) {
		return tbFcltMstDao.selectListPageTbFcltMstVo(tbFcltMstVo);
	}
	
	@Transactional(readOnly = true)
	public int countListPageFcltMstVo(TbFcltMstVo tbFcltMstVo) {
		return tbFcltMstDao.countListPageTbFcltMstVo(tbFcltMstVo);
	}

	@Transactional(readOnly = true) 
	public TbLvlBasVo selectTbLvlBas(TbLvlBasVo tbLvlBasVo) {
		return orgMgmtAdapterService.selectTbLvlBas(tbLvlBasVo);
	}
	
	@Transactional(readOnly = true)
	public List<TbFcltMstVo> selectListFcltMstVo(TbFcltMstVo tbFcltMstVo) {
		return tbFcltMstDao.selectListTbFcltMstVo(tbFcltMstVo);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public int insertFcltMstVo(TbFcltMstVo tbFcltMstVo) {
		return tbFcltMstDao.insertTbFcltMstVo(tbFcltMstVo);
	}
	
	@Transactional(readOnly = true)
	public TbFcltMstVo selectFcltMstVo(TbFcltMstVo tbFcltMstVo) {
		return tbFcltMstDao.selectTbFcltMstVo(tbFcltMstVo);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public int updateFcltMstVo(TbFcltMstVo tbFcltMstVo) {
		return tbFcltMstDao.updateTbFcltMstVo(tbFcltMstVo);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public int deleteFcltMstVo(TbFcltMstVo tbFcltMstVo) {
		return tbFcltMstDao.deleteTbFcltMstVo(tbFcltMstVo);
	}
	
	/****************************************************************************************
	 * 장비별 명령어 정보관리
	 ****************************************************************************************/
	
	@Transactional(readOnly = true)
	public List<TbFcltCmdMstVo> selectListPageFcltCmdMstVo(TbFcltCmdMstVo tbFcltCmdMstVo) {
		return tbFcltCmdMstDao.selectListPageTbFcltCmdMstVo(tbFcltCmdMstVo);
	}
	
	@Transactional(readOnly = true)
	public int countListPageFcltCmdMstVo(TbFcltCmdMstVo tbFcltCmdMstVo) {
		return tbFcltCmdMstDao.countListPageTbFcltCmdMstVo(tbFcltCmdMstVo);
	}
	
	@Transactional(readOnly = true)
	public List<TbFcltCmdMstVo> selectListFcltCmdMstVo(TbFcltCmdMstVo tbFcltCmdMstVo) {
		return tbFcltCmdMstDao.selectListTbFcltCmdMstVo(tbFcltCmdMstVo);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public int insertFcltCmdMstVo(TbFcltCmdMstVo tbFcltCmdMstVo) {
		return tbFcltCmdMstDao.insertTbFcltCmdMstVo(tbFcltCmdMstVo);
	}
	
	@Transactional(readOnly = true)
	public TbFcltCmdMstVo selectFcltCmdMstVo(TbFcltCmdMstVo tbFcltCmdMstVo) {
		return tbFcltCmdMstDao.selectTbFcltCmdMstVo(tbFcltCmdMstVo);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public int updateFcltCmdMstVo(TbFcltCmdMstVo tbFcltCmdMstVo) {
		return tbFcltCmdMstDao.updateTbFcltCmdMstVo(tbFcltCmdMstVo);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public int deleteFcltCmdMstVo(TbFcltCmdMstVo tbFcltMstVo) {
		return tbFcltCmdMstDao.deleteTbFcltCmdMstVo(tbFcltMstVo);
	}
	
	/****************************************************************************************
	 * 무선IP 사전 정보관리
	 ****************************************************************************************/
	
	@Transactional(readOnly = true)
	public List<TbMobileMstVo> selectListPageMobileMstVo(TbMobileMstVo tbMobileMstVo) {
		return tbMobileMstDao.selectListPageTbMobileMstVo(tbMobileMstVo);
	}
	
	@Transactional(readOnly = true)
	public int countListPageMobileMstVo(TbMobileMstVo tbMobileMstVo) {
		return tbMobileMstDao.countListPageTbMobileMstVo(tbMobileMstVo);
	}

	@Transactional(readOnly = true)
	public List<TbMobileMstVo> selectListMobileMstVo(TbMobileMstVo tbMobileMstVo) {
		return tbMobileMstDao.selectListTbMobileMstVo(tbMobileMstVo);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public int insertMobileMstVo(TbMobileMstVo tbMobileMstVo) {
		return tbMobileMstDao.insertTbMobileMstVo(tbMobileMstVo);
	}
	
	@Transactional(readOnly = true)
	public TbMobileMstVo selectMobileMstVo(TbMobileMstVo tbMobileMstVo) {
		return tbMobileMstDao.selectTbMobileMstVo(tbMobileMstVo);
	}
	
	
	@Transactional(propagation = Propagation.REQUIRED)
	public int updateMobileMstVo(TbMobileMstVo tbMobileMstVo) {
		return tbMobileMstDao.updateTbMobileMstVo(tbMobileMstVo);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public int deleteMobileMstVo(TbMobileMstVo tbMobileMstVo) {
		return tbMobileMstDao.deleteTbMobileMstVo(tbMobileMstVo);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public int deleteMobileMstAllVo(TbMobileMstVo tbMobileMstVo) {
		return tbMobileMstDao.deleteTbMobileMstAllVo(tbMobileMstVo);
	}
	
	/****************************************************************************************
	 * 무선IP 사전 정보관리 > Summary 등록
	 ****************************************************************************************/
	
	@Transactional(readOnly = true)
	public List<TbMobileSummMstVo> selectListPageMobileSummMstVo(TbMobileSummMstVo tbMobileSummMstVo) {
		return tbMobileSummMstDao.selectListPageTbMobileSummMstVo(tbMobileSummMstVo);
	}
	
	@Transactional(readOnly = true)
	public int countListPageMobileSummMstVo(TbMobileSummMstVo tbMobileSummMstVo) {
		return tbMobileSummMstDao.countListPageTbMobileSummMstVo(tbMobileSummMstVo);
	}

	@Transactional(readOnly = true)
	public List<TbMobileSummMstVo> selectListMobileSummMstVo(TbMobileSummMstVo tbMobileSummMstVo) {
		return tbMobileSummMstDao.selectListTbMobileSummMstVo(tbMobileSummMstVo);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public int insertMobileSummMstVo(TbMobileSummMstVo tbMobileSummMstVo) {
		return tbMobileSummMstDao.insertTbMobileSummMstVo(tbMobileSummMstVo);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public int deleteMobileSummMstVo(TbMobileSummMstVo tbMobileSummMstVo) {
		return tbMobileSummMstDao.deleteTbMobileSummMstVo(tbMobileSummMstVo);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public int deleteMobileSummMstAllVo(TbMobileSummMstVo tbMobileSummMstVo) {
		return tbMobileSummMstDao.deleteTbMobileSummMstAllVo(tbMobileSummMstVo);
	}
	
	/****************************************************************************************
	 * 서비스망별 서비스 정보관리
	 ****************************************************************************************/
	@Transactional(readOnly = true)
	public List<TbDefaultSvcMstVo> selectListPageDefaultSvcMstVo(TbDefaultSvcMstVo tbDefaultSvcMstVo) {
		return tbDefaultSvcMstDao.selectListPageTbDefaultSvcMstVo(tbDefaultSvcMstVo);
	}
	
	@Transactional(readOnly = true)
	public int countListPageDefaultSvcMstVo(TbDefaultSvcMstVo tbDefaultSvcMstVo) {
		return tbDefaultSvcMstDao.countListPageTbDefaultSvcMstVo(tbDefaultSvcMstVo);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public int insertDefaultSvcMstVo(TbDefaultSvcMstVo tbDefaultSvcMstVo) {
		return tbDefaultSvcMstDao.insertTbDefaultSvcMstVo(tbDefaultSvcMstVo);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public int updateDefaultSvcMstVo(TbDefaultSvcMstVo tbDefaultSvcMstVo) {
		return tbDefaultSvcMstDao.updateTbDefaultSvcMstVo(tbDefaultSvcMstVo);
	}
	
	@Transactional(readOnly = true)
	public List<TbDefaultSvcMstVo> selectListDefaultSvcMstVo(TbDefaultSvcMstVo tbDefaultSvcMstVo) {
		return tbDefaultSvcMstDao.selectListTbDefaultSvcMstVo(tbDefaultSvcMstVo);
	}
	
	/****************************************************************************************
	 * 라우팅 연동 이력관리
	 ****************************************************************************************/
	@Transactional(readOnly = true)
	public List<TbRoutHistMstVo> selectListPageTbRoutHistVo(TbRoutHistMstVo tbRoutHistMstVo) {
		return tbRoutHistMstDao.selectListPageTbRoutHistVo(tbRoutHistMstVo);
	}
	
	@Transactional(readOnly = true)
	public int countListTbRoutHistVo(TbRoutHistMstVo tbRoutHistMstVo) {
		return tbRoutHistMstDao.countListTbRoutHistVo(tbRoutHistMstVo);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public int updateRoutHistMstVo(TbRoutHistMstVo tbRoutHistMstVo) {
		return tbRoutHistMstDao.updateRoutHistMstVo(tbRoutHistMstVo);
	}

	/****************************************************************************************
	 * 유선 커뮤니티 관리
	 ****************************************************************************************/
	@Transactional(readOnly = true)
	public List<TbWireMstVo> selectListPageWireMstVo(TbWireMstVo tbWireMstVo) {
		return tbWireMstDao.selectListPageWireMstVo(tbWireMstVo);
	}

	@Transactional(readOnly = true)
	public int countListPageWireMstVo(TbWireMstVo tbWireMstVo) {
		return tbWireMstDao.countListPageWireMstVo(tbWireMstVo);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void insertWireMst(TbWireMstVo tbWireMstVo) {
		tbWireMstDao.insertWireMst(tbWireMstVo);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void updateWireMst(TbWireMstVo tbWireMstVo) {
		tbWireMstDao.updateWireMst(tbWireMstVo);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteWireMst(TbWireMstVo tbWireMstVo) {
		tbWireMstDao.deleteWireMst(tbWireMstVo);
	}

	@Transactional(readOnly = true)
	public List<TbWireMstVo> selectWireMst(TbWireMstVo tbWireMstVo) {
		return tbWireMstDao.selectWireMst(tbWireMstVo);
	}

	@Transactional(readOnly = true)
	public List<TbWireMstVo> selectListWireMstExcel(TbWireMstVo tbWireMstVo) {
		return tbWireMstDao.selectListWireMstExcel(tbWireMstVo);
	}
	
	@Transactional(readOnly = true)
	public List<?> selectSresultMsg(){
		List<?> sresultMsg = tbRoutHistMstDao.selectSresultMsg();
		return sresultMsg;
	}
	
}
