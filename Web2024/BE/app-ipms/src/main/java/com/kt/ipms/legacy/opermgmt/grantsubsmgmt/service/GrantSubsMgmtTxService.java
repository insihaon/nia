package com.kt.ipms.legacy.opermgmt.grantsubsmgmt.service;

import java.math.BigInteger;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.kt.ipms.legacy.opermgmt.grantmgmt.vo.TbUserAuthTxnListVo;
import com.kt.ipms.legacy.opermgmt.grantmgmt.vo.TbUserAuthTxnVo;
import com.kt.ipms.legacy.opermgmt.grantsubsmgmt.dao.GrantSubsMgmtDao;
import com.kt.ipms.legacy.opermgmt.grantsubsmgmt.vo.TbUserAuthTxnSubListVo;
import com.kt.ipms.legacy.opermgmt.grantsubsmgmt.vo.TbUserAuthTxnSubVo;
import com.kt.ipms.legacy.opermgmt.grantsubsmgmt.vo.TbUserGrantVo;

@Component
@Transactional
public class GrantSubsMgmtTxService {
	@Autowired
	GrantSubsMgmtDao grantSubsMgmtDao;
	
	@Transactional(readOnly = true)
	public  List<TbUserGrantVo>  selectTbUserGrantList (TbUserGrantVo searchVo)  {
		return  grantSubsMgmtDao.selectTbUserGrantList(searchVo);
	}
	
	@Transactional(readOnly = true)
	public int countListPageTbUserGrantVo (TbUserGrantVo searchVo)  {
		return  grantSubsMgmtDao.countListPageTbUserGrantVo(searchVo);
	}
	
	public int insertTbUserGrant(TbUserAuthTxnSubVo insertVo){
		return grantSubsMgmtDao.insertTbUserGrant(insertVo);
	}
	
	public int insertTbUserAuthTxnSub(TbUserAuthTxnSubVo insertVo){
		return grantSubsMgmtDao.insertTbUserAuthTxnSub(insertVo);
	}
	
	@Transactional(readOnly = true)
	public List<TbUserAuthTxnSubVo> selectListTbUserAuthTxnSubVo(TbUserAuthTxnVo searchVo){
		return grantSubsMgmtDao.selectListTbUserAuthTxnSubVo(searchVo);
	}
	
	@Transactional(readOnly = true)
	public int countListTbUserAuthTxnSubVo(TbUserAuthTxnVo searchVo) { 
		return grantSubsMgmtDao.countListTbUserAuthTxnSubVo(searchVo);
	}
	
	public String selectNrequestTypeCd(TbUserAuthTxnVo searchVo){
		return grantSubsMgmtDao.selectNrequestTypeCd(searchVo);
		
	}
	
	public int deleteGrant(TbUserGrantVo searchVo){
		return grantSubsMgmtDao.deleteGrant(searchVo);
	}
	
	public int deleteTbUserAuthTxnSubVo(TbUserGrantVo searchVo){
		return grantSubsMgmtDao.deleteTbUserAuthTxnSubVo(searchVo);
	}
	
	public int updateGrant(TbUserAuthTxnListVo tbUserAuthTxnListVo){
		return grantSubsMgmtDao.updateGrant(tbUserAuthTxnListVo);
	}
	
	public TbUserGrantVo selectGrantVo(TbUserGrantVo tbUserGrantVo){
		return grantSubsMgmtDao.selectGrantVo(tbUserGrantVo);
	}
}
