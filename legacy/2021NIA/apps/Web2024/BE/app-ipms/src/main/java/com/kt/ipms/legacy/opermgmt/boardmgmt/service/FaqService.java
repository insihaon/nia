package com.kt.ipms.legacy.opermgmt.boardmgmt.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kt.framework.exception.ServiceException;
import com.kt.ipms.legacy.opermgmt.boardmgmt.dao.TbBoardDao;
import com.kt.ipms.legacy.opermgmt.boardmgmt.vo.TbBoardListVo;
import com.kt.ipms.legacy.opermgmt.boardmgmt.vo.TbBoardVo;

@Component
public class FaqService {
	
	@Lazy @Autowired 
	private TbBoardDao tbBoardDao;

	/**
	 * FAQ Table 목록 조회
	 * @param searchVo
	 * @return
	 * @
	 */
	public TbBoardListVo selectListFaq(TbBoardVo searchVo) {
		TbBoardListVo resultListVo = null;
		if(searchVo == null){
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {
			searchVo.setSboardTypeCd("BH0003"); // FAQcd : BH0003
			List<TbBoardVo> resultList = tbBoardDao.selectListPageTbBoardVo(searchVo);
			int totalCount = tbBoardDao.countListPageTbBoardVo(searchVo);
			resultListVo = new TbBoardListVo();
			resultListVo.setTbBoardVos(resultList);
			resultListVo.setTotalCount(totalCount);
			
		} catch (ServiceException e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"FAQ리스트정보"});
		}
		return resultListVo;
			
	}
	/**
	 * FAQ Table 상세 조회
	 * @param tbBoardVo
	 * @return
	 * @
	 */
	public TbBoardVo selectFaq(TbBoardVo tbBoardVo) {
		TbBoardVo resultVo = null;
		if(tbBoardVo == null)
		{
			throw new ServiceException("CMN.HIGH.00001");
		}
		try{
			resultVo = tbBoardDao.selectTbBoardVo(tbBoardVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"FAQ상세정보"});
		}
		return resultVo;
	}
	
	/**
	 * FAQ 조회수
	 * @param tbBoardVo
	 * @return
	 * @
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void updateNreadCnt(TbBoardVo tbBoardVo)  {
		try {
			tbBoardDao.updateNreadCnt(tbBoardVo);
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00021", new String[]{"FAQ조회수"});
		}
	}
	
	/**
	 * FAQ Table 등록
	 * @param tbBoardVo
	 * @return
	 * @
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public TbBoardVo insertFaq(TbBoardVo tbBoardVo) {
		if(tbBoardVo == null)
		{
			throw new ServiceException("CMN.HIGH.00001");
		}
		try{
			tbBoardVo.setSboardTypeCd("BH0003"); // FAQcd : BH0003
			tbBoardDao.insertTbBoardVo(tbBoardVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00020", new String[]{"FAQ"});
		}
		return tbBoardVo;
	}
	
	
	/**
	 * FAQ Table 수정
	 * @param tbBoardVo
	 * @return
	 * @
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public TbBoardVo updateFaq(TbBoardVo tbBoardVo)  {
		if(tbBoardVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try{
			tbBoardVo.setDmodifyDt(new Date());
			tbBoardDao.updateTbBoardVo(tbBoardVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00021", new String[]{"FAQ"});
		}
		return tbBoardVo;
	}
	
	
	
	/**
	 * FAQ Table 삭제
	 * @param tbBoardVo
	 * @return
	 * @
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public TbBoardVo deleteFaq(TbBoardVo tbBoardVo)  {
		if (tbBoardVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {
			tbBoardDao.deleteTbBoardVo(tbBoardVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00022", new String[]{"FAQ"});
		}
		return tbBoardVo;
	}
	
	

}
