package com.kt.ipms.legacy.opermgmt.tacsmgmt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kt.ipms.legacy.ipmgmt.assignmgmt.adapter.AssignMgmtAdapterService;
import com.kt.ipms.legacy.ipmgmt.assignmgmt.vo.TbIpAssignMstVo;
import com.kt.ipms.legacy.linkmgmt.socketmgmt.adapter.SocketMgmtAdapterService;
import com.kt.ipms.legacy.opermgmt.orgmgmt.adapter.OrgMgmtAdapterService;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlBasVo;
import com.kt.ipms.legacy.opermgmt.tacsmgmt.dao.TbTacsConnBasDao;
import com.kt.ipms.legacy.opermgmt.tacsmgmt.dao.TbTacsConnHistDao;
import com.kt.ipms.legacy.opermgmt.tacsmgmt.dao.TbTacsFcltCmdMstDao;
import com.kt.ipms.legacy.opermgmt.tacsmgmt.dao.TbTacsFcltMstDao;
import com.kt.ipms.legacy.opermgmt.tacsmgmt.vo.TacsRequstListVo;
import com.kt.ipms.legacy.opermgmt.tacsmgmt.vo.TacsResponseListVo;
import com.kt.ipms.legacy.opermgmt.tacsmgmt.vo.TbTacsConnBasVo;
import com.kt.ipms.legacy.opermgmt.tacsmgmt.vo.TbTacsConnHistVo;
import com.kt.ipms.legacy.opermgmt.tacsmgmt.vo.TbTacsFcltCmdMstVo;
import com.kt.ipms.legacy.opermgmt.tacsmgmt.vo.TbTacsFcltMstVo;

@Component
public class TacsMgmtTxService {
	
	@Autowired
	private TbTacsConnBasDao tbTacsConnBasDao;
	
	@Autowired
	private TbTacsFcltMstDao tbTacsFcltMstDao;
	
	@Autowired
	private TbTacsFcltCmdMstDao tbTacsFcltCmdMstDao;
	
	@Autowired
	private TbTacsConnHistDao tbTacsConnHistDao;
	
	@Autowired
	private AssignMgmtAdapterService assignMgmtAdapterService;
	
	@Autowired
	private SocketMgmtAdapterService socketMgmtAdapterService;
	
	@Autowired
	private OrgMgmtAdapterService orgMgmtAdapterService;
	
	
	@Transactional(readOnly = true)
	public TbTacsConnBasVo selectTbTacsConnBasVo() {
		return tbTacsConnBasDao.selectTbTacsConnBasVo();
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public int updateTbTacsConnBasVo(TbTacsConnBasVo tbTacsConnBasVo) {
		return tbTacsConnBasDao.updateTbTacsConnBasVo(tbTacsConnBasVo);
	}
	
	@Transactional(readOnly = true)
	public List<TbTacsFcltMstVo> selectListPageTacsFcltMstVo(TbTacsFcltMstVo tbTacsFcltMstVo) {
		return tbTacsFcltMstDao.selectListPageTbTacsFcltMstVo(tbTacsFcltMstVo);
	}
	
	@Transactional(readOnly = true)
	public int countListPageTacsFcltMstVo(TbTacsFcltMstVo tbTacsFcltMstVo) {
		return tbTacsFcltMstDao.countListPageTbTacsFcltMstVo(tbTacsFcltMstVo);
	}
	
	@Transactional(readOnly = true)
	public TbTacsFcltMstVo selectTacsFcltMstVo(TbTacsFcltMstVo tbTacsFcltMstVo) {
		return tbTacsFcltMstDao.selectTbTacsFcltMstVo(tbTacsFcltMstVo);
	}
	
	@Transactional(readOnly = true)
	public List<TbTacsFcltMstVo> selectListTacsFcltMstVo(TbTacsFcltMstVo tbTacsFcltMstVo) {
		return tbTacsFcltMstDao.selectListTbTacsFcltMstVo(tbTacsFcltMstVo);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public int insertTacsFcltMstVo(TbTacsFcltMstVo tbTacsFcltMstVo) {
		return tbTacsFcltMstDao.insertTbTacsFcltMstVo(tbTacsFcltMstVo);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public int deleteTacsFcltMstVo(TbTacsFcltMstVo tbTacsFcltMstVo) {
		return tbTacsFcltMstDao.deleteTbTacsFcltMstVo(tbTacsFcltMstVo);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public int updateTacsFcltMstVo(TbTacsFcltMstVo tbTacsFcltMstVo) {
		return tbTacsFcltMstDao.updateTbTacsFcltMstVo(tbTacsFcltMstVo);
	}
	
	@Transactional(readOnly = true)
	public List<TbTacsFcltCmdMstVo> selectListPageTacsFcltCmdMstVo(TbTacsFcltCmdMstVo tbTacsFcltCmdMstVo) {
		return tbTacsFcltCmdMstDao.selectListPageTbTacsFcltCmdMstVo(tbTacsFcltCmdMstVo);
	}
	
	@Transactional(readOnly = true)
	public int countListPageTacsFcltCmdMstVo(TbTacsFcltCmdMstVo tbTacsFcltCmdMstVo) {
		return tbTacsFcltCmdMstDao.countListPageTbTacsFcltCmdMstVo(tbTacsFcltCmdMstVo);
	}
	
	@Transactional(readOnly = true)
	public List<TbTacsFcltCmdMstVo> selectListTacsFcltCmdMstVo(TbTacsFcltCmdMstVo tbTacsFcltCmdMstVo) {
		return tbTacsFcltCmdMstDao.selectListTbTacsFcltCmdMstVo(tbTacsFcltCmdMstVo);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public int insertTacsFcltCmdMstVo(TbTacsFcltCmdMstVo tbTacsFcltCmdMstVo) {
		return tbTacsFcltCmdMstDao.insertTbTacsFcltCmdMstVo(tbTacsFcltCmdMstVo);
	}
	
	@Transactional(readOnly = true)
	public TbTacsFcltCmdMstVo selectTacsFcltCmdMstVo(TbTacsFcltCmdMstVo tbTacsFcltCmdMstVo) {
		return tbTacsFcltCmdMstDao.selectTbTacsFcltCmdMstVo(tbTacsFcltCmdMstVo);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public int updateTacsFcltCmdMstVo(TbTacsFcltCmdMstVo tbTacsFcltCmdMstVo) {
		return tbTacsFcltCmdMstDao.updateTbTacsFcltCmdMstVo(tbTacsFcltCmdMstVo);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public int insertTacsConnHistVo(TbTacsConnHistVo tbTacsConnHistVo) {
		return tbTacsConnHistDao.insertTbTacsConnHistVo(tbTacsConnHistVo);
	}
	
	
	@Transactional(readOnly = true)
	public TbIpAssignMstVo selectIpAssignMst(TbIpAssignMstVo tbIpAssignMstVo) {
		return assignMgmtAdapterService.selectIpAssignMst(tbIpAssignMstVo);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public TacsResponseListVo getCheckTacsRouteList(TacsRequstListVo tacsRequstListVo, String userId) {
		return socketMgmtAdapterService.getCheckTacsRouteList(tacsRequstListVo, userId);
	}
	
	@Transactional(readOnly = true) 
	public TbLvlBasVo selectTbLvlBas(TbLvlBasVo tbLvlBasVo) {
		return orgMgmtAdapterService.selectTbLvlBas(tbLvlBasVo);
	}
	
	@Transactional(readOnly = true)
	public List<TbTacsConnHistVo> selectListPageTbTacsConnHistVo(TbTacsConnHistVo tbTacsConnHistVo) {
		return tbTacsConnHistDao.selectListPageTbTacsConnHistVo(tbTacsConnHistVo);
	}
	
	@Transactional(readOnly = true)
	public int countListTbTacsConnHistVo(TbTacsConnHistVo tbTacsConnHistVo) {
		return tbTacsConnHistDao.countListTbTacsConnHistVo(tbTacsConnHistVo);
	}
	
	@Transactional(readOnly = true)
	public List<?> selectSresultMsg(){
		List<?> sresultMsg = tbTacsConnHistDao.selectSresultMsg();
		return sresultMsg;
	}
	
}
