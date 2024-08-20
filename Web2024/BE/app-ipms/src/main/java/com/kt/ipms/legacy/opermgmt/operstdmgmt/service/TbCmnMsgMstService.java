package com.kt.ipms.legacy.opermgmt.operstdmgmt.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kt.framework.exception.ServiceException;
import com.kt.ipms.legacy.opermgmt.operstdmgmt.dao.TbCmnMsgMstDao;
import com.kt.ipms.legacy.opermgmt.operstdmgmt.vo.TbCmnMsgLvlCdListVo;
import com.kt.ipms.legacy.opermgmt.operstdmgmt.vo.TbCmnMsgLvlCdVo;
import com.kt.ipms.legacy.opermgmt.operstdmgmt.vo.TbCmnMsgMstListVo;
import com.kt.ipms.legacy.opermgmt.operstdmgmt.vo.TbCmnMsgMstVo;
import com.kt.ipms.legacy.opermgmt.operstdmgmt.vo.TbCmnMsgTypeCdListVo;
import com.kt.ipms.legacy.opermgmt.operstdmgmt.vo.TbCmnMsgTypeCdVo;


@Component
public class TbCmnMsgMstService {
	
	@Autowired
	private TbCmnMsgMstDao tbCmnMsgCdDao;
	
	public boolean validateTbCmnMsgCd(TbCmnMsgMstVo tbCmnMsgCdVo) throws ServiceException {
		String msgRegex = "((CMN)|(DMN)|()|())(()|()|()|())([0-9][0-9][0-9])";
		if(tbCmnMsgCdVo.getMsgCd().matches(msgRegex)) {
			return true;
		}
		return false;
	}
	public TbCmnMsgMstListVo selectListTbCmnMsgCdVo(TbCmnMsgMstVo tbCmnMsgCdVo) throws ServiceException {
		
		TbCmnMsgMstListVo resultListVo = null;
		if (tbCmnMsgCdVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		if(tbCmnMsgCdVo.getSearchCnd() != null && !tbCmnMsgCdVo.getSearchCnd().equals(""))
		{
			if(tbCmnMsgCdVo.getSearchCnd().equals("msg_cd")){
				tbCmnMsgCdVo.setMsgCd(tbCmnMsgCdVo.getSearchWrd());
			}
			else
				if(tbCmnMsgCdVo.getSearchCnd().equals("msg_desc")){
					tbCmnMsgCdVo.setMsgDesc(tbCmnMsgCdVo.getSearchWrd());
			}else
				if(tbCmnMsgCdVo.getSearchCnd().equals("screate_id")){
					tbCmnMsgCdVo.setScreateId(tbCmnMsgCdVo.getSearchWrd());
			}else
				if(tbCmnMsgCdVo.getSearchCnd().equals("smodify_id")){
					tbCmnMsgCdVo.setSmodifyId(tbCmnMsgCdVo.getSearchWrd());
			}else{
				throw new ServiceException("CMN.HIGH.00000");
			}
		}
		try {
			List<TbCmnMsgMstVo> resultList = tbCmnMsgCdDao.selectListPageTbCmnMsgMstVo(tbCmnMsgCdVo);
			int totalCount = tbCmnMsgCdDao.countListPageTbCmnMsgMstVo(tbCmnMsgCdVo);
			resultListVo = new TbCmnMsgMstListVo();
			resultListVo.setTbCmnMsgMstVos(resultList);
			resultListVo.setTotalCount(totalCount);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00000");
		}
		return resultListVo;
	}
	public TbCmnMsgMstVo selectTbCmnMsgMstVo(TbCmnMsgMstVo tbCmnMsgCdVo) throws ServiceException {
		TbCmnMsgMstVo resultVo = null;
		
		try {
			
			if (tbCmnMsgCdVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			
			
			resultVo = tbCmnMsgCdDao.selectTbCmnMsgMstVo(tbCmnMsgCdVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00000");
		}
		return resultVo;
	}
	@Transactional(propagation = Propagation.REQUIRED)
	public TbCmnMsgMstVo insertTbCmnMsgCdVo(TbCmnMsgMstVo tbCmnMsgCdVo) throws ServiceException {
		if (tbCmnMsgCdVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {
			tbCmnMsgCdDao.insertTbCmnMsgMstVo(tbCmnMsgCdVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00000");
		}
		return tbCmnMsgCdVo;
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public int updateTbCmnMsgCdVo(TbCmnMsgMstVo tbCmnMsgCdVo) throws ServiceException {
		int result = 0;
		if (tbCmnMsgCdVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		tbCmnMsgCdVo.setMsgTypeCd(tbCmnMsgCdVo.getMsgTypeCd().replaceAll("\n", "").replaceAll("\t", ""));
		tbCmnMsgCdVo.setMsgLvlCd(tbCmnMsgCdVo.getMsgLvlCd().replaceAll("\n", "").replaceAll("\t", ""));
		try {
			tbCmnMsgCdVo.setDmodifyDt(new Date());
			result = tbCmnMsgCdDao.updateTbCmnMsgMstVo(tbCmnMsgCdVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00000");
		}
		if(result != 1){
			throw new ServiceException("CMN.HIGH.00000");
		}
		return result;
	}

	
	public String selectMsgDesc(ServiceException serviceException) throws ServiceException{
		String regex = "([\\{][\\}])";
		String msgCd = serviceException.getMessageKey();
		String msgDesc;
		Object[] args = serviceException.getArgs();
		TbCmnMsgMstVo tbCmnMsgCdVo = new TbCmnMsgMstVo();
		if(msgCd == null || msgCd.equals("")) {
			return "메시지 코드를 입력하지 않았습니다.";//오류 메시지 코드를 입력하지 않았을 때
		}else {
			tbCmnMsgCdVo.setMsgCd(msgCd);
			try {
				tbCmnMsgCdVo = tbCmnMsgCdDao.selectTbCmnMsgMstVo(tbCmnMsgCdVo);
				if(tbCmnMsgCdVo == null) {
					return "메시지가 등록되지 않았습니다.";//오류 메시지 코드가 DB에 등록되지 않았을 때
				}
			} catch(Exception e){
				return "메시지 조회 파라미터가 잘못되었습니다.";//오류 메시지 코드가 DB에 등록되지 않았을 때
			}
			msgDesc = tbCmnMsgCdVo.getMsgDesc();
			if(msgDesc == null) {
				return "메시지코드가 등록되지 않았습니다.";//오류 메시지 코드가 DB에 등록되지 않았을 때
			}
			if(serviceException.getArgs() != null && msgDesc.contains("{}")) {
				for(int i = 0; i < args.length; i++){
					msgDesc = msgDesc.replaceFirst(regex, args[i].toString());
				}
			}
		}
		return msgDesc;
	}
	public TbCmnMsgLvlCdListVo selectListTbCmnMsgLvlCdVo() throws ServiceException{
		TbCmnMsgLvlCdListVo resultListVo = null;
		try {
			List<TbCmnMsgLvlCdVo> resultList = tbCmnMsgCdDao.selectListTbCmnMsgLvlCdVo();
			resultListVo = new TbCmnMsgLvlCdListVo();
			resultListVo.setTbCmnMsgLvlVos(resultList);
			resultListVo.setTotalCount(resultList.size());
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00000");
		}
		return resultListVo;
	}
	
	public TbCmnMsgTypeCdListVo selectListTbCmnMsgTypeCdVo() throws ServiceException{
		TbCmnMsgTypeCdListVo resultListVo = null;
		try {
			List<TbCmnMsgTypeCdVo> resultList = tbCmnMsgCdDao.selectListTbCmnMsgTypeCdVo();
			resultListVo = new TbCmnMsgTypeCdListVo();
			resultListVo.setTbCmnMsgTypeCdVos(resultList);
			resultListVo.setTotalCount(resultList.size());
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00000");
		}
		return resultListVo;
	}
}
