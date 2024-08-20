package com.kt.ipms.legacy.opermgmt.menumgmt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kt.ipms.legacy.opermgmt.menumgmt.dao.TbMenuAuthDao;
import com.kt.ipms.legacy.opermgmt.menumgmt.dao.TbMenuBasDao;
import com.kt.ipms.legacy.opermgmt.menumgmt.dao.TbScrnBasDao;
import com.kt.ipms.legacy.opermgmt.menumgmt.vo.TbMenuBasVo;
import com.kt.ipms.legacy.opermgmt.menumgmt.vo.TbMenuBasListVo;
import com.kt.ipms.legacy.opermgmt.menumgmt.vo.TbScrnBasListVo;
import com.kt.ipms.legacy.opermgmt.menumgmt.vo.TbScrnBasVo;
import com.kt.ipms.legacy.opermgmt.menumgmt.vo.TbMenuAuthVo;
import com.kt.ipms.legacy.opermgmt.menumgmt.vo.TbMenuAuthListVo;
import com.kt.framework.exception.ServiceException;


@Component
public class MenuMgmtService {
	
	private final static int MAX_MENU_LEVEL = 4;
	
	@Autowired
	TbScrnBasDao tbScrnBasDao;
	
	@Autowired
	TbMenuBasDao tbMenuBasDao;
	
	@Autowired
	TbMenuAuthDao  tbMenuAuthDao;
	
	@Autowired
	private MenuMgmtTxService menuMgmtTxService;
	/* ----------------------------- 화면 관리 start ------------------------------- */
	@Transactional(readOnly = true)
	public TbScrnBasListVo selectListPageScrnBas(TbScrnBasVo searchVo)  {
		TbScrnBasListVo resultListVo = null;
		if(searchVo == null){
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {
			List<TbScrnBasVo> resultList = menuMgmtTxService.selectListPageTbScrnBasVo(searchVo);
			int totalCount = menuMgmtTxService.countListPageTbScrnBasVo(searchVo);
			resultListVo = new TbScrnBasListVo();
			resultListVo.setTbScrnBasVos(resultList);
			
			resultListVo.setTotalCount(totalCount);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"화면기본정보 내역"});
		}
		return resultListVo;
	}
	
	@Transactional(readOnly = true)
	public TbScrnBasListVo selectListScrnBas(TbScrnBasVo searchVo)  {
		TbScrnBasListVo resultListVo = null;
		if(searchVo == null){
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {
			List<TbScrnBasVo> resultList = menuMgmtTxService.selectListTbScrnBasVo(searchVo);
			int totalCount = menuMgmtTxService.countListPageTbScrnBasVo(searchVo);
			resultListVo = new TbScrnBasListVo();
			resultListVo.setTbScrnBasVos(resultList);
			
			resultListVo.setTotalCount(totalCount);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"화면기본정보 내역"});
		}
		return resultListVo;
	}
	@Transactional(readOnly = true)
	public TbScrnBasVo selectScrnBas(TbScrnBasVo tbScrnBasVo)  {
		TbScrnBasVo resultVo = null;
		if(tbScrnBasVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {
			resultVo = menuMgmtTxService.selectScrnBas(tbScrnBasVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"화면상세정보"});
		}
		return resultVo; 
	}
	
	@Transactional(readOnly = true)
	public String selectNewsscrnId()  {
		String newSscrnId = null;
		try {
			newSscrnId = menuMgmtTxService.selectNewsscrnId();
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"화면정보"});
		}
		
		return newSscrnId;
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public TbScrnBasVo insertScrnBas(TbScrnBasVo tbScrnBasVo)  {
		if(tbScrnBasVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try{
			menuMgmtTxService.insertScrnBas(tbScrnBasVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00020", new String[]{"화면정보"});
		}
		
		return tbScrnBasVo;
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public TbScrnBasVo updateScrnBas(TbScrnBasVo tbScrnBasVo)  {
		if(tbScrnBasVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try{
			menuMgmtTxService.updateScrnBas(tbScrnBasVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00021", new String[]{"화면정보"});
		}
		
		return tbScrnBasVo;
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public TbScrnBasListVo updateSsrnUseYn(TbScrnBasListVo tbScrnBasListVo)  {
		if(tbScrnBasListVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try{
			menuMgmtTxService.updateSsrnUseYn(tbScrnBasListVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00021", new String[]{"사용여부"});
		}
		return tbScrnBasListVo;
	}
	
	

	/* ----------------------------- 화면 관리 end ------------------------------- */

	/* ----------------------------- 메뉴 관리 Start ------------------------------- */
	@Transactional(readOnly = true)
	public TbMenuBasListVo selectListTbMenuBas(TbMenuBasVo searchVo)  {
		
		TbMenuBasListVo resultListVo = null;
		
		try {
			
			List<TbMenuBasVo> resultList = menuMgmtTxService.selectListTbMenuBasVo(searchVo);
			int totalCount = menuMgmtTxService.countListPageTbMenuBasVo(searchVo);
			resultListVo = new TbMenuBasListVo();
			resultListVo.setTbMenuBasVos(resultList);
			resultListVo.setTotalCount(totalCount);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			//logger.error(e.toString());
			throw new ServiceException("CMN.HIGH.00023", new String[]{"메뉴기본정보내역 "});
		}
		return resultListVo;
	}
	
	@Transactional (readOnly = true)
	public TbMenuBasListVo selectMenuBas(TbMenuBasVo tbMenuBasVo)  {
		TbMenuBasListVo resultListVo = new TbMenuBasListVo();
		try {
			
			for (int i=1; i < MAX_MENU_LEVEL; i++) {
				TbMenuBasVo searchVo = new TbMenuBasVo();
				searchVo.setNmenuLvlSeq(i);
				
				List<TbMenuBasVo> tempListVos = menuMgmtTxService.selectListTbMenuBasVo(searchVo);
				if (tempListVos != null && tempListVos.size() > 0) {
					if (i == 1) {
						resultListVo.setFirMenuBasVos(tempListVos);
						resultListVo.setfTotalCount(tempListVos.size());
					} else if (i == 2) {
						resultListVo.setSecMenuBasVos(tempListVos);
						resultListVo.setsTotalConut(tempListVos.size());
					} else if (i == 3) {
						resultListVo.setThrmenuBasVos(tempListVos);
						resultListVo.settTotalCount(tempListVos.size());
					}
				}
			}
			//loginInfoVo.setMenuBasListVo(resultListVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			// 사용자 메뉴정보 설정 실패
			throw new ServiceException("CMN.HIGH.00000");
		}
		
		return resultListVo;
	}
	
	@Transactional(readOnly = true)
	public TbMenuBasVo selectTbMenuBasVo(TbMenuBasVo tbMenuBasVo) {
		TbMenuBasVo resultVo = null;
		try {
			resultVo =  menuMgmtTxService.selectTbMenuBasVo(tbMenuBasVo);
		}catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			
			throw new ServiceException("CMN.HIGH.00000");
		}
		
		return resultVo;
	}	
	
	@Transactional(propagation=Propagation.REQUIRED)
	public TbMenuBasVo updateTbMenuBasVo(TbMenuBasVo tbMenuBasVo)  {
		if(tbMenuBasVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try{
			menuMgmtTxService.updateTbMenuBasVo(tbMenuBasVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00021", new String[]{"메뉴정보"});
		}
		
		return tbMenuBasVo;
	}
	/* ----------------------------- 메뉴 관리 End ------------------------------- */
	
	/* ----------------------------- 메뉴 권한 관리 Start ------------------------------- */
	@Transactional(readOnly = true)
	public TbMenuAuthListVo selectListTbMenuAuth(TbMenuAuthVo searchVo)  {
		
		TbMenuAuthListVo resultListVo = null;
		
		try {
			
			List<TbMenuAuthVo> resultList = menuMgmtTxService.selectListPageTbMenuAuthVo(searchVo);
			int totalCount = menuMgmtTxService.countListPageTbMenuAuthVo(searchVo);
			resultListVo = new TbMenuAuthListVo();
			resultListVo.setTbMenuAuthVos(resultList);
			resultListVo.setTotalCount(totalCount);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			//logger.error(e.toString());
			throw new ServiceException("CMN.HIGH.00023", new String[]{"메뉴기본정보내역 "});
		}
		return resultListVo;
	}
	
	
	
	@Transactional(propagation=Propagation.REQUIRED)
	public void updateMenuAuthUseYn(TbMenuAuthListVo menuAuthList)  {

		try {

			String apyType = menuAuthList.getMenuAutUseYn();

			if(apyType.equals("N")){
				for(int i=0; i < menuAuthList.getTbMenuAuthVos().size(); i++){
					TbMenuAuthVo menuAuth = menuAuthList.getTbMenuAuthVos().get(i);
					menuMgmtTxService.insertMenuAuthUseYn(menuAuth);
				}	
			}
			else{
				for(int i=0; i < menuAuthList.getTbMenuAuthVos().size(); i++){
					TbMenuAuthVo menuAuth = menuAuthList.getTbMenuAuthVos().get(i);
					menuMgmtTxService.deleteAuthUseYn(menuAuth);
				}
			
			}	
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"메뉴기본정보변경 "});
		}
	}
	
	/* ----------------------------- 메뉴 권한 관리 End ------------------------------- */
}
