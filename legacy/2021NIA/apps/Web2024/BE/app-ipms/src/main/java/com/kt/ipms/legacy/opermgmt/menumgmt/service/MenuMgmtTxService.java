package com.kt.ipms.legacy.opermgmt.menumgmt.service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kt.ipms.legacy.opermgmt.menumgmt.dao.TbMenuAuthDao;
import com.kt.ipms.legacy.opermgmt.menumgmt.dao.TbMenuBasDao;
import com.kt.ipms.legacy.opermgmt.menumgmt.dao.TbScrnBasDao;
import com.kt.ipms.legacy.opermgmt.menumgmt.vo.TbMenuAuthVo;
import com.kt.ipms.legacy.opermgmt.menumgmt.vo.TbMenuBasVo;
import com.kt.ipms.legacy.opermgmt.menumgmt.vo.TbScrnBasListVo;
import com.kt.ipms.legacy.opermgmt.menumgmt.vo.TbScrnBasVo;


@Component
public class MenuMgmtTxService {
	
	
	@Lazy @Autowired
	private TbMenuAuthDao tbMenuAuthDao;
	
	@Lazy @Autowired
	private TbScrnBasDao tbScrnBasDao;
	
	@Lazy @Autowired
	private TbMenuBasDao tbMenuBasDao;
	
	
	
	@Transactional(readOnly = true)
	public List<TbScrnBasVo> selectListPageTbScrnBasVo(TbScrnBasVo tbScrnBasVo)  {
		return  tbScrnBasDao.selectListPageTbScrnBasVo(tbScrnBasVo);
	}
	
	@Transactional(readOnly = true)
	public List<TbScrnBasVo> selectListTbScrnBasVo(TbScrnBasVo tbScrnBasVo)  {
		return  tbScrnBasDao.selectListTbScrnBasVo(tbScrnBasVo);
	}
	
	@Transactional(readOnly = true)
	public  int countListPageTbScrnBasVo(TbScrnBasVo tbScrnBasVo) {
		return tbScrnBasDao.countListPageTbScrnBasVo(tbScrnBasVo);
	}
	
	@Transactional(readOnly = true)
	public TbScrnBasVo selectScrnBas(TbScrnBasVo tbScrnBasVo)  {
		return tbScrnBasDao.selectTbScrnBasVo(tbScrnBasVo);
	}	
	
	@Transactional(readOnly = true)
	public String selectNewsscrnId()  {
		return tbScrnBasDao.selectNewsscrnId();
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public int insertScrnBas(TbScrnBasVo tbScrnBasVo)  {
		return tbScrnBasDao.insertTbScrnBasVo(tbScrnBasVo);
	}
		
	@Transactional(propagation=Propagation.REQUIRED)
	public int updateScrnBas(TbScrnBasVo tbScrnBasVo)  {
		return tbScrnBasDao.updateTbScrnBasVo(tbScrnBasVo);
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public int updateSsrnUseYn(TbScrnBasListVo tbScrnBasListVo)  {
		return tbScrnBasDao.updateSscrnUseYn(tbScrnBasListVo);
	}
		
	@Transactional(propagation=Propagation.REQUIRED)
	public void insertMenuAuthUseYn(TbMenuAuthVo menuAuth)  {
		tbMenuAuthDao.insertMenuAuthUseYn(menuAuth);
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public void deleteAuthUseYn(TbMenuAuthVo menuAuth)  {
		tbMenuAuthDao.deleteAuthUseYn(menuAuth);
	}
	
	@Transactional(readOnly = true)
	public List<TbMenuBasVo> selectListTbMenuBasVo(TbMenuBasVo tbMenuBasVo) {
		return tbMenuBasDao.selectListTbMenuBasVo(tbMenuBasVo);
	}
	
	@Transactional(readOnly = true)
	public int countListPageTbMenuBasVo(TbMenuBasVo tbMenuBasVo) {
		return tbMenuBasDao.countListPageTbMenuBasVo(tbMenuBasVo);
	}
	
	@Transactional(readOnly = true)
	public List<TbMenuAuthVo> selectListPageTbMenuAuthVo(TbMenuAuthVo tbMenuAuthVo)  {
		return tbMenuAuthDao.selectListPageTbMenuAuthVo(tbMenuAuthVo);
	}
	@Transactional(readOnly = true)
	public int countListPageTbMenuAuthVo(TbMenuAuthVo tbMenuAuthVo) {
		return  tbMenuAuthDao.countListPageTbMenuAuthVo(tbMenuAuthVo);
	}
	
	@Transactional(readOnly = true)
	public TbMenuBasVo selectTbMenuBasVo(TbMenuBasVo tbMenuBasVo) {
		return  tbMenuBasDao.selectTbMenuBasVo(tbMenuBasVo);
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public int updateTbMenuBasVo(TbMenuBasVo tbMenuBasVo) {
		return  tbMenuBasDao.updateTbMenuBasVo(tbMenuBasVo);
	}
	
}
