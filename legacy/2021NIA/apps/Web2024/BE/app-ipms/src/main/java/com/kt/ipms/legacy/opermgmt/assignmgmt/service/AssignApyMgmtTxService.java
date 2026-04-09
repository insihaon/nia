package com.kt.ipms.legacy.opermgmt.assignmgmt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kt.ipms.legacy.opermgmt.assignmgmt.dao.TbRequestAssignMstDao;
import com.kt.ipms.legacy.opermgmt.assignmgmt.vo.TbRequestAssignMstVo;


@Component
@Transactional
public class AssignApyMgmtTxService {
	
	@Lazy @Autowired
	private TbRequestAssignMstDao tbRequestAssignMstDao;
	
	@Transactional(propagation=Propagation.REQUIRED)
	public int insertTbRequestAssignMstVo(TbRequestAssignMstVo tbRequestAssignMstVo) {
		return tbRequestAssignMstDao.insertTbRequestAssignMstVo(tbRequestAssignMstVo);
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public int updateTbRequestAssignMstVo(TbRequestAssignMstVo tbRequestAssignMstVo) {
		return tbRequestAssignMstDao.updateTbRequestAssignMstVo(tbRequestAssignMstVo);
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public int deleteTbRequestAssignMstVo(TbRequestAssignMstVo tbRequestAssignMstVo) {
		return tbRequestAssignMstDao.deleteTbRequestAssignMstVo(tbRequestAssignMstVo);
	}
	
	@Transactional(readOnly = true)
	public TbRequestAssignMstVo selectTbRequestAssignMstVo(TbRequestAssignMstVo tbRequestAssignMstVo){
		return tbRequestAssignMstDao.selectTbRequestAssignMstVo(tbRequestAssignMstVo);
	}
	
	@Transactional(readOnly = true)
	public List<TbRequestAssignMstVo> selectListTbRequestAssignMstVo(TbRequestAssignMstVo tbRequestAssignMstVo) {
		return tbRequestAssignMstDao.selectListTbRequestAssignMstVo(tbRequestAssignMstVo);
	}
	
	@Transactional(readOnly = true)
	public List<TbRequestAssignMstVo> selectListPreApyAssign(TbRequestAssignMstVo tbRequestAssignMstVo) {
		return tbRequestAssignMstDao.selectListPreApyAssign(tbRequestAssignMstVo);
	}
	
	@Transactional(readOnly = true)
	public int countListTbRequestAssignMstVo(TbRequestAssignMstVo tbRequestAssignMstVo) {
		return tbRequestAssignMstDao.countListTbRequestAssignMstVo(tbRequestAssignMstVo);
	}
	
	@Transactional(readOnly = true)
	public List<TbRequestAssignMstVo> selectListPageTbRequestAssignMstVo(TbRequestAssignMstVo tbRequestAssignMstVo) {
		return tbRequestAssignMstDao.selectListPageTbRequestAssignMstVo(tbRequestAssignMstVo);
	}
	
	
	@Transactional(readOnly = true)
	public int countListPageTbRequestAssignMstVo(TbRequestAssignMstVo tbRequestAssignMstVo) {
		return tbRequestAssignMstDao.countListPageTbRequestAssignMstVo(tbRequestAssignMstVo);
	}

}
