package com.kt.ipms.legacy.linkmgmt.errormgmt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kt.framework.exception.ServiceException;
import com.kt.ipms.legacy.linkmgmt.errormgmt.dao.ErrorMgmtDao;
import com.kt.ipms.legacy.linkmgmt.errormgmt.vo.ErrorMgmtListVo;
import com.kt.ipms.legacy.linkmgmt.errormgmt.vo.ErrorMgmtVo;

@Component
public class ErrorMgmtService {
	
	@Autowired
	private ErrorMgmtDao errorMgmtDao; 
	
	@Transactional(readOnly = true)
	public ErrorMgmtListVo selectListErrorMgmt(ErrorMgmtVo searchVo){
		ErrorMgmtListVo resultListVo = null;
		if(searchVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try{
			List<ErrorMgmtVo> resultList = errorMgmtDao.selectListErrorMgmtVo(searchVo);
			int totalCount = errorMgmtDao.countListPageErrorMgmtVo(searchVo);
			resultListVo = new ErrorMgmtListVo();
			resultListVo.setErrorMgmtVos(resultList);;
			resultListVo.setTotalCount(totalCount);
		}catch(ServiceException e){
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
			throw new ServiceException("CMN.HIGH.00023", new String[]{"요구사항리스트정보"});
		}
		return resultListVo;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public int updateTbNeossErrorManage(ErrorMgmtListVo errorMgmtListVo) {
		if(errorMgmtListVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		int resultCode;
		try{
			resultCode = errorMgmtDao.updateTbNeossErrorManage(errorMgmtListVo);
		} catch(ServiceException e) {
			throw e;
		} catch(Exception e) {
			System.out.println(e.toString());
			throw new ServiceException("CMN.HIGH.00000");
		}
		return resultCode;
	}
}
