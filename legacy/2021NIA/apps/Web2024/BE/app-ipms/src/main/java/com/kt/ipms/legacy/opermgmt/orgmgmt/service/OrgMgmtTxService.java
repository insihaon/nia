package com.kt.ipms.legacy.opermgmt.orgmgmt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kt.ipms.legacy.opermgmt.grantmgmt.adapter.GrantMgmtAdapterService;
import com.kt.ipms.legacy.opermgmt.grantmgmt.vo.TbUserAuthTxnVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.dao.TbLvlBasDao;
import com.kt.ipms.legacy.opermgmt.orgmgmt.dao.TbLvlCdDao;
import com.kt.ipms.legacy.opermgmt.orgmgmt.dao.TbLvlMstDao;
import com.kt.ipms.legacy.opermgmt.orgmgmt.dao.TbLvlRoleMstDao;
import com.kt.ipms.legacy.opermgmt.orgmgmt.dao.TbLvlRoleSubDao;
import com.kt.ipms.legacy.opermgmt.orgmgmt.dao.TbLvlSubCdDao;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlBasVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlCdVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlMstVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlRoleMstVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlRoleSubVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlSubCdVo;


@Component
@Transactional
public class OrgMgmtTxService {

	@Lazy @Autowired
	private TbLvlRoleSubDao tbLvlRoleSubDao;

	@Lazy @Autowired
	private TbLvlCdDao tbLvlCdDao;

	@Lazy @Autowired
	private TbLvlSubCdDao tbLvlSubCdDao;

	@Lazy @Autowired
	private TbLvlBasDao tbLvlBasDao;

	@Lazy @Autowired
	private TbLvlMstDao tbLvlMstDao;

	@Lazy @Autowired
	private TbLvlRoleMstDao tbLvlRoleMstDao;

	@Autowired
	GrantMgmtAdapterService grantMgmtAdapterService;
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void insertTbLvlRoleSubVo(TbLvlRoleSubVo searchVo )  {
		tbLvlRoleSubDao.insertTbLvlRoleSubVo(searchVo);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteTbLvlRoleSubVo(TbLvlRoleSubVo searchVo )  {
		tbLvlRoleSubDao.deleteTbLvlRoleSubVo(searchVo);
	}
	
	@Transactional(readOnly = true)
	public List<TbLvlCdVo>  selectListPageTbLvlCdVo(TbLvlCdVo searchVo){
		return tbLvlCdDao.selectListPageTbLvlCdVo(searchVo);
	}
	
	@Transactional(readOnly = true)
	public List<TbLvlCdVo>  selectListTbLvlCdVo(TbLvlCdVo searchVo){
		return tbLvlCdDao.selectListTbLvlCdVo(searchVo);
	}
	
	@Transactional(readOnly = true)
	public int  countListPageTbLvlCdVo(TbLvlCdVo searchVo){
		return tbLvlCdDao.countListPageTbLvlCdVo(searchVo);
	}
	
	@Transactional(readOnly = true)
	public TbLvlCdVo  selectTbLvlCdVo(TbLvlCdVo searchVo){
		return tbLvlCdDao.selectTbLvlCdVo(searchVo);
	}
	
	@Transactional(readOnly = true)
	public String  selectNewLvlCd(){
		return tbLvlCdDao.selectNewLvlCd();
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public int  insertTbLvlCdVo(TbLvlCdVo tbLvlCdVo){
		return tbLvlCdDao.insertTbLvlCdVo(tbLvlCdVo);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public int  updateTbLvlCdVo(TbLvlCdVo tbLvlCdVo){
		return tbLvlCdDao.updateTbLvlCdVo(tbLvlCdVo);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void insertTbLvlSubCd(TbLvlSubCdVo searchVo )  {
		tbLvlSubCdDao.insertTbLvlSubCdVo(searchVo);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteTbLvlSubCd(TbLvlSubCdVo searchVo )  {
		tbLvlSubCdDao.deleteTbLvlSubCdVo(searchVo);
	}
	
/*	@Transactional(propagation = Propagation.REQUIRED)
	public void insertTbLvlBas(TbLvlRoleMstVo searchVo )  {
		tbLvlBasDao.insertTbLvlBasVo(searchVo);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void insertTbLvlMst(TbLvlRoleMstVo searchVo )  {
		tbLvlMstDao.insertTbLvlMstVo(searchVo);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void insertTbLvlRoleMst(TbLvlRoleMstVo searchVo )  {
		tbLvlRoleMstDao.insertTbLvlRoleMstVo(searchVo);
	}
*/
	@Transactional(propagation = Propagation.REQUIRED)
	public void insertTbLvlMgmt(TbLvlRoleMstVo searchVo )  {	
		tbLvlMstDao.insertTbLvlMstVo(searchVo);
		tbLvlBasDao.insertTbLvlBasVo(searchVo);
		//if(!searchVo.getSsvcObjCd().equals("000000") && !searchVo.getSsvcHighCd().equals("000000")){
		tbLvlRoleMstDao.insertTbLvlRoleMstVo(searchVo);
		//}
	}
	
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void updateTbLvlMove(TbLvlMstVo searchVo )  {	

		TbLvlRoleSubVo tbLvlRoleSubVo = new TbLvlRoleSubVo();
		tbLvlRoleSubVo.setSsvcLineTypeCd(searchVo.getBfSsvcLineTypeCd());
		tbLvlRoleSubVo.setSsvcObjCd(searchVo.getBfSsvcObjCd());

		TbLvlSubCdVo tbLvlSubCdVo = new TbLvlSubCdVo();
		tbLvlSubCdVo.setSsvcLineTypeCd(searchVo.getBfSsvcLineTypeCd());
		tbLvlSubCdVo.setSlvlCd(searchVo.getBfSsvcObjCd());
		
		TbLvlRoleMstVo tbLvlRoleMstVo = new TbLvlRoleMstVo();
		tbLvlRoleMstVo.setSsvcLineTypeCd(searchVo.getBfSsvcLineTypeCd());
		tbLvlRoleMstVo.setSsvcGroupCd(searchVo.getBfSsvcGroupCd());
		tbLvlRoleMstVo.setSsvcObjCd(searchVo.getBfSsvcObjCd());
		
		TbLvlBasVo tbLvlBasVo = new TbLvlBasVo();
		tbLvlBasVo.setSsvcLineTypeCd(searchVo.getBfSsvcLineTypeCd());
		tbLvlBasVo.setSsvcGroupCd(searchVo.getBfSsvcGroupCd());
		tbLvlBasVo.setSsvcObjCd(searchVo.getBfSsvcObjCd());
		
		
			
		//delete
		//아답타호출 권한seq삭제
		TbUserAuthTxnVo tbUserAuthTxnVo = new TbUserAuthTxnVo();
		tbUserAuthTxnVo.setNlvlBasSeq(searchVo.getNlvlSeq());
		grantMgmtAdapterService.deleteTbUserAuthTxnByLvlBasSeq(tbUserAuthTxnVo);
	
		tbLvlRoleSubDao.deleteTbLvlRoleSubVo(tbLvlRoleSubVo);
		tbLvlSubCdDao.deleteTbLvlSubCdVo(tbLvlSubCdVo);
		tbLvlRoleMstDao.deleteTbLvlRoleMstVo(tbLvlRoleMstVo);
		tbLvlBasDao.deleteTbLvlBasVo(tbLvlBasVo);
		
		//update hist성 데이터
		tbLvlMstDao.updateTbLvlMstVo(searchVo);

		tbLvlBasVo.setSsvcLineTypeCd(searchVo.getSsvcLineTypeCd());
		tbLvlBasVo.setSsvcGroupCd(searchVo.getSsvcGroupCd());
		tbLvlBasVo.setSsvcObjCd(searchVo.getSsvcObjCd());
		tbLvlBasVo.setSmodifyId(searchVo.getSmodifyId());
		tbLvlBasDao.updateTbLvlBasVo(tbLvlBasVo);
		
		tbLvlRoleMstVo.setSsvcLineTypeCd(searchVo.getSsvcLineTypeCd());
		tbLvlRoleMstVo.setSsvcGroupCd(searchVo.getSsvcGroupCd());
		tbLvlRoleMstVo.setSsvcObjCd(searchVo.getSsvcObjCd());
		tbLvlRoleMstVo.setSsvcHighCd(searchVo.getSsvchighcd());
		tbLvlRoleMstVo.setSmodifyId(searchVo.getSmodifyId());
		tbLvlRoleMstDao.updateTbLvlRoleMstVo(tbLvlRoleMstVo);

	}
	
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void updateTbLvlRoleMstVo(TbLvlMstVo searchVo )  {	

		TbLvlRoleMstVo tbLvlRoleMstVo = new TbLvlRoleMstVo();
		tbLvlRoleMstVo.setSsvcLineTypeCd(searchVo.getSsvcLineTypeCd());
		tbLvlRoleMstVo.setSsvcGroupCd(searchVo.getSsvcGroupCd());
		tbLvlRoleMstVo.setSsvcObjCd(searchVo.getSsvcObjCd());
		tbLvlRoleMstVo.setSsvcHighCd(searchVo.getSsvchighcd());
		
		tbLvlRoleMstVo.setSmodifyId(searchVo.getSmodifyId());
		tbLvlRoleMstDao.updateTbLvlRoleMstVo(tbLvlRoleMstVo);

		
	}
	
	

	
}
