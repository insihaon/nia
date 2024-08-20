package com.kt.ipms.legacy.opermgmt.grantsubsmgmt.service;

import java.math.BigInteger;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kt.framework.exception.ServiceException;
import com.kt.ipms.legacy.cmn.util.SessionUtil;
import com.kt.ipms.legacy.opermgmt.grantmgmt.service.GrantMgmtTxService;
import com.kt.ipms.legacy.opermgmt.grantmgmt.vo.TbOperTeamAuthTxnListVo;
import com.kt.ipms.legacy.opermgmt.grantmgmt.vo.TbOperTeamAuthTxnVo;
import com.kt.ipms.legacy.opermgmt.grantmgmt.vo.TbUserAuthTxnListVo;
import com.kt.ipms.legacy.opermgmt.grantmgmt.vo.TbUserAuthTxnVo;
import com.kt.ipms.legacy.opermgmt.grantsubsmgmt.vo.TbUserAuthTxnSubListVo;
import com.kt.ipms.legacy.opermgmt.grantsubsmgmt.vo.TbUserAuthTxnSubVo;
import com.kt.ipms.legacy.opermgmt.grantsubsmgmt.vo.TbUserGrantListVo;
import com.kt.ipms.legacy.opermgmt.grantsubsmgmt.vo.TbUserGrantVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlBasVo;
import com.kt.ipms.legacy.opermgmt.usermgmt.vo.TbUserBasVo;

@Component
public class GrantSubsMgmtService {
	@Autowired
	GrantMgmtTxService grantMgmtTxService;
	
	@Autowired
	GrantSubsMgmtTxService grantSubsMgmtTxService;
	
	@Transactional(readOnly = true)
	public  TbUserGrantListVo selectTbUserGrantList(TbUserGrantVo searchVo)   {
		
		TbUserGrantListVo resultListVo = null;
		if (searchVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		
		try
		{
			List<TbUserGrantVo> resultVos = grantSubsMgmtTxService.selectTbUserGrantList(searchVo);
			int totalCount = grantSubsMgmtTxService.countListPageTbUserGrantVo(searchVo);
			resultListVo = new TbUserGrantListVo();
			resultListVo.setTbUserGrantVos(resultVos);
			resultListVo.setTotalCount(totalCount);
		}
		catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			System.out.println("e === "+e);
			throw new ServiceException("CMN.HIGH.00023", new String[]{"운용조직 권한내역"});
		}
		return resultListVo;
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public BigInteger insertTbUserAuthTxnVo(TbUserAuthTxnSubListVo tbUserAuthTxnSubListVo){
		BigInteger grant_seq = null;
		if(tbUserAuthTxnSubListVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try{
			if(tbUserAuthTxnSubListVo.getTbUserAuthTxnSubVos() != null) {
				List<TbUserAuthTxnSubVo> insertVos = tbUserAuthTxnSubListVo.getTbUserAuthTxnSubVos();
				int iCnt = insertVos.size();
				
				for(int i=0; i<iCnt; i++){
					TbUserAuthTxnSubVo insertVo = insertVos.get(i);
					if(i == 0){
						grantSubsMgmtTxService.insertTbUserGrant(insertVo);
						grant_seq = insertVo.getGrantSeq();
					}
					//insertTbUserAuthTxnVo
					insertVo.setGrantSeq(grant_seq);
					if(insertVo.getTbLvlBasVo() != null){
						TbLvlBasVo lvlBasVo = grantMgmtTxService.selectTbLvlBasVo(insertVo.getTbLvlBasVo());
						if( lvlBasVo != null && lvlBasVo.getNlvlBasSeq() != null ){
							insertVo.setNlvlBasSeq(lvlBasVo.getNlvlBasSeq());
						}
					}
					grantSubsMgmtTxService.insertTbUserAuthTxnSub(insertVo);
				}
			}
			
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			System.out.println("e === "+e);
			throw new ServiceException("CMN.HIGH.00021", new String[]{"사용자 권한등록"});
		}
		return grant_seq;
	}
	
	@Transactional(readOnly = true)
	public TbUserAuthTxnSubListVo selectDetailUserAuthTxn(TbUserAuthTxnVo searchVo){
		TbUserAuthTxnSubListVo resultListVo = null;
		if (searchVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		
		try
		{
			TbUserBasVo userBasVo = new TbUserBasVo();
			userBasVo.setSuserId(searchVo.getSuserId());
			userBasVo =  grantMgmtTxService.selectUserBasVo(userBasVo);
			
			List<TbUserAuthTxnSubVo>	resultVos	=	grantSubsMgmtTxService.selectListTbUserAuthTxnSubVo(searchVo);
			int totalCount	=	grantSubsMgmtTxService.countListTbUserAuthTxnSubVo(searchVo);
			resultListVo	=	new	TbUserAuthTxnSubListVo();
			resultListVo.setTbUserAuthTxnSubVos(resultVos);
			resultListVo.setTotalCount(totalCount);
			resultListVo.setSuserId(userBasVo.getSuserId());
			resultListVo.setSuserNm(userBasVo.getSuserNm());
			resultListVo.setTypeFlag(searchVo.getTypeFlag());
			resultListVo.setSuserGradeCd(userBasVo.getSuserGradeCd());
			
			String nrequestTypeCd = grantSubsMgmtTxService.selectNrequestTypeCd(searchVo);
			resultListVo.setNrequestTypeCd(nrequestTypeCd);
		}
		catch (ServiceException e) {
			throw e;
		}
		return resultListVo;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public int deleteGrant(TbUserGrantVo searchVo){
		if(searchVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		int resultCode =0;
		try{
			resultCode = grantSubsMgmtTxService.deleteGrant(searchVo);
			grantSubsMgmtTxService.deleteTbUserAuthTxnSubVo(searchVo);
			if (resultCode != 1){
				throw new ServiceException("CMN.HIGH.00001");
			}
		} catch(ServiceException e) {
			System.out.println(e);
			throw e;
		} catch(Exception e) {
			System.out.println(e);
			throw new ServiceException("CMN.HIGH.00000");
		}
		return resultCode;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public int updateGrant(TbUserAuthTxnListVo tbUserAuthTxnListVo){
		int resultCode = 0;
		resultCode = grantSubsMgmtTxService.updateGrant(tbUserAuthTxnListVo);
		return resultCode;
	}
	
	@Transactional(readOnly = true)
	public TbUserGrantVo selectGrantVo(TbUserGrantVo tbUserGrantVo){
		TbUserGrantVo resultVo = null;
		resultVo = grantSubsMgmtTxService.selectGrantVo(tbUserGrantVo);
		return resultVo;
	}
}
