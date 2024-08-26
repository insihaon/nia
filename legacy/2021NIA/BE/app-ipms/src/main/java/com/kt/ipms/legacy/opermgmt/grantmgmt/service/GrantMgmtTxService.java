package com.kt.ipms.legacy.opermgmt.grantmgmt.service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kt.ipms.legacy.opermgmt.grantmgmt.dao.TbAdmrApvTxnDao;
import com.kt.ipms.legacy.opermgmt.grantmgmt.dao.TbOperTeamAuthTxnDao;
import com.kt.ipms.legacy.opermgmt.grantmgmt.dao.TbUserAuthTxnDao;
import com.kt.ipms.legacy.opermgmt.grantmgmt.vo.TbAdmrApvTxnVo;
import com.kt.ipms.legacy.opermgmt.grantmgmt.vo.TbOperTeamAuthTxnVo;
import com.kt.ipms.legacy.opermgmt.grantmgmt.vo.TbUserAuthTxnListVo;
import com.kt.ipms.legacy.opermgmt.grantmgmt.vo.TbUserAuthTxnVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.adapter.OrgMgmtAdapterService;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlBasVo;
import com.kt.ipms.legacy.opermgmt.usermgmt.adapter.UserMgmtAdapterService;
import com.kt.ipms.legacy.opermgmt.usermgmt.vo.TbUserBasVo;
@Component
@Transactional
public class GrantMgmtTxService {

	@Lazy @Autowired
	TbAdmrApvTxnDao tbAdmrApvTxnDao;
	
	@Lazy @Autowired
	TbOperTeamAuthTxnDao tbOperTeamAuthTxnDao;
	
	@Lazy @Autowired
	TbUserAuthTxnDao userAuthTxnDao;
	
	@Lazy @Autowired
	OrgMgmtAdapterService orgMgmtAdapterService;
	
	@Lazy @Autowired
	UserMgmtAdapterService  userMgmtAdapterService;
	
	
	@Transactional(propagation=Propagation.REQUIRED)
	public int insertAdmrAutTxn(TbAdmrApvTxnVo tbAdmrApvTxnVo)  {
		return tbAdmrApvTxnDao.insertTbAdmrApvTxnVo(tbAdmrApvTxnVo);
	}
	
	@Transactional(readOnly = true)
	public  List<TbAdmrApvTxnVo>  selectListTbAdmrApvTxnVo (TbAdmrApvTxnVo tbAdmrApvTxnVo)  {
		return  tbAdmrApvTxnDao.selectListTbAdmrApvTxnVo(tbAdmrApvTxnVo);
	}
	
	@Transactional(readOnly = true)
	public  List<TbAdmrApvTxnVo>  selectListPageTbAdmrApvTxnVo (TbAdmrApvTxnVo tbAdmrApvTxnVo)  {
		return  tbAdmrApvTxnDao.selectListPageTbAdmrApvTxnVo(tbAdmrApvTxnVo);
	}	
	
	@Transactional(readOnly = true)
	public int countListPageTbAdmrApvTxnVo(TbAdmrApvTxnVo tbAdmrApvTxnVo)  {
		return tbAdmrApvTxnDao.countListPageTbAdmrApvTxnVo(tbAdmrApvTxnVo);
	}
	
	@Transactional(readOnly = true)
	public String selectUserGradeAdmr(TbAdmrApvTxnVo tbAdmrApvTxnVo)  {
		return tbAdmrApvTxnDao.selectUserGradeAdmr(tbAdmrApvTxnVo);
	}	
	
	@Transactional(readOnly = true)
	public String selectUserGrade(TbUserAuthTxnVo userAuthTxnVo)  {
		return userAuthTxnDao.selectUserGrade(userAuthTxnVo);
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public int deleteAdmrAutTxn(TbAdmrApvTxnVo tbAdmrApvTxnVo)  {
		return tbAdmrApvTxnDao.deleteTbAdmrApvTxnVo(tbAdmrApvTxnVo);
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public int updateUserBas(TbUserBasVo tbUserBasVo){
		return userMgmtAdapterService.updateTbuserBas(tbUserBasVo);
	}
	
	@Transactional(readOnly = true)
	public TbUserBasVo selectUserBasVo(TbUserBasVo tbUserBasVo){
		return userMgmtAdapterService.selectTbuserBas(tbUserBasVo);
	}
	
	@Transactional(readOnly = true)
	public List<TbOperTeamAuthTxnVo>  selectListPageTbOperTeamAuthTxnVo(TbOperTeamAuthTxnVo searchVo) {
		return tbOperTeamAuthTxnDao.selectListPageTbOperTeamAuthTxnVo(searchVo);
	}
	
	@Transactional(readOnly = true)
	public  int   countListPageTbOperTeamAuthTxnVo(TbOperTeamAuthTxnVo searchVo) {
		return tbOperTeamAuthTxnDao.countListPageTbOperTeamAuthTxnVo(searchVo);
	}
	
	@Transactional(readOnly = true)
	public List<TbLvlBasVo>  selectListTeamSvcLineList(TbOperTeamAuthTxnVo searchVo) {
		return tbOperTeamAuthTxnDao.selectListTeamSvcLineList(searchVo);
	}
	
	@Transactional(readOnly = true)
	public List<TbLvlBasVo>  selectListTeamCenterList(TbOperTeamAuthTxnVo searchVo) {
		return tbOperTeamAuthTxnDao.selectListTeamCenterList(searchVo);
	}
	
	@Transactional(readOnly = true)
	public List<TbLvlBasVo>  selectListTeamNodeList(TbOperTeamAuthTxnVo searchVo) {
		return tbOperTeamAuthTxnDao.selectListTeamNodeList(searchVo);
	}
	
	@Transactional(readOnly = true)
	public List<TbLvlBasVo> selectListSvcLine(TbAdmrApvTxnVo tbAdmrApvTxnVo)  {
		return tbAdmrApvTxnDao.selectListSvcLine(tbAdmrApvTxnVo);
	}
	
	
	@Transactional(readOnly = true)
	public List<TbLvlBasVo>  selectListUserSvcLineList(TbUserAuthTxnVo searchVo) {
		return userAuthTxnDao.selectListUserSvcLineList(searchVo);
	}
	
	@Transactional(readOnly = true)
	public List<TbLvlBasVo>  selectListUserCenterList(TbUserAuthTxnVo searchVo) {
		return userAuthTxnDao.selectListUserCenterList(searchVo);
	}
	
	@Transactional(readOnly = true)
	public List<TbLvlBasVo>  selectListUserNodeList(TbUserAuthTxnVo searchVo) {
		return userAuthTxnDao.selectListUserNodeList(searchVo);
	}
	
	/*사용자 권한관리 START*/
	
	@Transactional(propagation=Propagation.REQUIRED)
	public int insertTbUserAuthTxnVo(TbUserAuthTxnVo tbUserAuthTxnVo) {
		return userAuthTxnDao.insertTbUserAuthTxnVo(tbUserAuthTxnVo);
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public int updateTbUserAuthTxnVo(TbUserAuthTxnVo tbUserAuthTxnVo){
		return userAuthTxnDao.updateTbUserAuthTxnVo(tbUserAuthTxnVo);
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public int deleteTbUserAuthTxnVo(TbUserAuthTxnVo tbUserAuthTxnVo){
		return userAuthTxnDao.deleteTbUserAuthTxnVo(tbUserAuthTxnVo);
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public int deleteTbUserAuthByLvlBasSeq(TbUserAuthTxnVo tbUserAuthTxnVo){
		return userAuthTxnDao.deleteTbUserAuthByLvlBasSeq(tbUserAuthTxnVo);
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public int deleteTbUserAuthListVo(TbUserAuthTxnListVo tbUserAuthTxnListVo){
		return userAuthTxnDao.deleteTbUserAuthListVo(tbUserAuthTxnListVo);
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public int deleteTbUserAuthByUserId(TbUserAuthTxnListVo tbUserAuthTxnListVo){
		return userAuthTxnDao.deleteTbUserAuthByUserId(tbUserAuthTxnListVo);
	}
	
	@Transactional(readOnly = true)
	public TbUserAuthTxnVo selectTbUserAuthTxnVo(TbUserAuthTxnVo tbUserAuthTxnVo) { 
		return userAuthTxnDao.selectTbUserAuthTxnVo(tbUserAuthTxnVo);
	}
	
	@Transactional(readOnly = true)
	public List<TbUserAuthTxnVo> selectListTbUserAuthTxnVo(TbUserAuthTxnVo tbUserAuthTxnVo) { 
		return userAuthTxnDao.selectListTbUserAuthTxnVo(tbUserAuthTxnVo);
	}
	
	@Transactional(readOnly = true)
	public int countListTbUserAuthTxnVo(TbUserAuthTxnVo tbUserAuthTxnVo) { 
		return userAuthTxnDao.countListTbUserAuthTxnVo(tbUserAuthTxnVo);
	}
	
	@Transactional(readOnly = true)
	public List<TbUserAuthTxnVo> selectListPageTbUserAuthTxnVo(TbUserAuthTxnVo tbUserAuthTxnVo) { 
		return userAuthTxnDao.selectListPageTbUserAuthTxnVo(tbUserAuthTxnVo);
	}
	
	@Transactional(readOnly = true)
	public int countListPageTbUserAuthTxnVo(TbUserAuthTxnVo tbUserAuthTxnVo) { 
		return userAuthTxnDao.countListPageTbUserAuthTxnVo(tbUserAuthTxnVo);
	}
	
	public TbLvlBasVo selectTbLvlBasVo(TbLvlBasVo tbLvlBasVo){
		return orgMgmtAdapterService.selectTbLvlBas(tbLvlBasVo);
	}
	/*사용자 권한관리  END*/
}
