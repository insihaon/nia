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
import com.kt.ipms.legacy.opermgmt.boardmgmt.dao.TbBoardReplyDao;
import com.kt.ipms.legacy.opermgmt.boardmgmt.vo.TbBoardListVo;
import com.kt.ipms.legacy.opermgmt.boardmgmt.vo.TbBoardReplyListVo;
import com.kt.ipms.legacy.opermgmt.boardmgmt.vo.TbBoardReplyVo;
import com.kt.ipms.legacy.opermgmt.boardmgmt.vo.TbBoardVo;

@Component
public class BoardService {

	@Lazy @Autowired
	private TbBoardDao tbBoardDao;
	@Lazy @Autowired
	private TbBoardReplyDao tbBoardReplyDao;

	@Transactional(readOnly = true)
	public TbBoardListVo selectListBoard(TbBoardVo searchVo) {
		TbBoardListVo resultListVo = null;
		if(searchVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try{
			searchVo.setSboardTypeCd("BH0002");
			List<TbBoardVo> resultList = tbBoardDao.selectListPageTbBoardVo(searchVo);
			int totalCount = tbBoardDao.countListPageTbBoardVo(searchVo);
			resultListVo = new TbBoardListVo();
			resultListVo.setTbBoardVos(resultList);
			resultListVo.setTotalCount(totalCount);
		}catch(ServiceException e){
			throw e;
		}catch(Exception e){
			throw new ServiceException("CMN.HIGH.00023", new String[]{"게시판리스트정보"});
		}
		
		return resultListVo;
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public TbBoardVo selectBoard(TbBoardVo tbBoardVo)  {
		
		TbBoardVo resultVo = null;
		try{
			
			if(tbBoardVo == null){
				throw new ServiceException("CMN.HIGH.00001");
			}
			
			tbBoardDao.updateNreadCnt(tbBoardVo);
			resultVo = tbBoardDao.selectTbBoardVo(tbBoardVo);
		}catch(ServiceException e){
			throw e;
		}catch(Exception e){
			throw new ServiceException("CMN.HIGH.00023", new String[]{"게시판상세정보"});
		}
		return resultVo;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public int insertBoard(TbBoardVo tbBoardVo)  {
		if (tbBoardVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		int result = 0;
		try {
			result = tbBoardDao.insertTbBoardVo(tbBoardVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00020", new String[]{"게시판"});
		}
		return result;
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public int updateBoard(TbBoardVo tbBoardVo)  {
		int result = 0;
		if (tbBoardVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {
			tbBoardVo.setDmodifyDt(new Date());
			result = tbBoardDao.updateTbBoardVo(tbBoardVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00021", new String[]{"게시판"});
		}
		if(result != 1){
			throw new ServiceException("CMN.HIGH.00001");
		}
		return result;
	}
	
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public int deleteBoard(TbBoardVo tbBoardVo)  {
		  int result = 0;
			if (tbBoardVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			try {
				
				/* 해당 Reply 삭제*/
				TbBoardReplyVo tempVo = new TbBoardReplyVo();
				tempVo.setSeq(tbBoardVo.getSeq());
				result = tbBoardReplyDao.deleteTbBoardReplyVo(tempVo);
				
				result = tbBoardDao.deleteTbBoardVo(tbBoardVo);
			} catch (ServiceException e) {
				throw e;
			} catch (Exception e) {
				throw new ServiceException("CMN.HIGH.00022", new String[]{"게시판"});
			}
			if(result != 1){
				throw new ServiceException("CMN.HIGH.00001");
			}
			return result;
	}
	
	@Transactional(readOnly = true)
	public TbBoardReplyListVo selectListBoardReply(TbBoardReplyVo tbBoardReplyVo) {
		TbBoardReplyListVo resultListVo = null;
		if(tbBoardReplyVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try{
			List<TbBoardReplyVo> resultList = tbBoardReplyDao.selectListTbBoardReplyVo(tbBoardReplyVo);
			resultListVo = new TbBoardReplyListVo();
			resultListVo.setTbBoardReplyVos(resultList);
		}catch(ServiceException e){
			throw e;
		}catch(Exception e){
			throw new ServiceException("CMN.HIGH.00023", new String[]{"댓글"});
		}
		
		return resultListVo;
	}
	
	
	@Transactional(propagation = Propagation.REQUIRED)
	public TbBoardReplyVo insertBoardReply(TbBoardReplyVo tbBoardReplyVo)  {
		if (tbBoardReplyVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {
			tbBoardReplyDao.insertTbBoardReplyVo(tbBoardReplyVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00020", new String[]{"댓글"});
		}
		return tbBoardReplyVo;
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public TbBoardReplyVo updateBoardReply(TbBoardReplyVo tbBoardReplyVo)  {
		if (tbBoardReplyVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {
			tbBoardReplyVo.setDmodifyDt(new Date());
			tbBoardReplyDao.updateTbBoardReplyVo(tbBoardReplyVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00021", new String[]{"댓글"});
		}

		return tbBoardReplyVo;
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public TbBoardReplyVo deleteBoardReply(TbBoardReplyVo tbBoardReplyVo)  {
			if (tbBoardReplyVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			try {
				tbBoardReplyDao.deleteTbBoardReplyVo(tbBoardReplyVo);
			} catch (ServiceException e) {
				throw e;
			} catch (Exception e) {
				throw new ServiceException("CMN.HIGH.00022", new String[]{"댓글"});
			}
		
			return tbBoardReplyVo;
	}
	
	
	@Transactional(readOnly = true)
	public TbBoardListVo selectListTbBoardByMain() {
		TbBoardListVo resultListVo = null;
		
		try{
			int totalCount = 0;
			List<TbBoardVo> resultList = tbBoardDao.selectListTbBoardByMain();
			if(resultList != null )
			{
				totalCount =resultList.size();
			}
			
			resultListVo = new TbBoardListVo();
			resultListVo.setTbBoardVos(resultList);
			resultListVo.setTotalCount(totalCount);
		}catch(ServiceException e){
			throw e;
		}catch(Exception e){
			throw new ServiceException("CMN.HIGH.00022", new String[]{"메인공지사항"});
		}
		
		return resultListVo;
	}

}
