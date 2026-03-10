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
public class NoticeService {

	@Lazy @Autowired
	private TbBoardDao tbBoardDao;

	/**
	 * Board Table 목록 조회
	 * @param searchVo
	 * @return
	 * @
	 */
	public TbBoardListVo selectListNotice(TbBoardVo searchVo) {
		TbBoardListVo resultListVo = null;
		if (searchVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {
			List<TbBoardVo> resultList = tbBoardDao.selectListPageTbBoardVo(searchVo);
			int totalCount = tbBoardDao.countListPageTbBoardVo(searchVo);
			resultListVo = new TbBoardListVo();
			resultListVo.setTbBoardVos(resultList);
			resultListVo.setTotalCount(totalCount);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[] { "공지리스트정보" });
		}
		return resultListVo;
	}

	public TbBoardVo selectNotice(TbBoardVo tbBoardVo) {
		TbBoardVo resultVo = null;
		if (tbBoardVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {
			//updateNreadCnt(tbBoardVo);
			resultVo = tbBoardDao.selectTbBoardVo(tbBoardVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[] { "공지상세정보" });
		}
		return resultVo;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public TbBoardVo insertNotice(TbBoardVo tbBoardVo) {
		if (tbBoardVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {
			tbBoardDao.insertTbBoardVo(tbBoardVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00020", new String[] { "공지" });
		}
		return tbBoardVo;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void updateNreadCnt(TbBoardVo tbBoardVo) {
		try {
			tbBoardDao.updateNreadCnt(tbBoardVo);
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00021", new String[] { "공지조회수" });
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public TbBoardVo updateNotice(TbBoardVo tbBoardVo) {
		if (tbBoardVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {
			tbBoardVo.setDmodifyDt(new Date());
			tbBoardDao.updateTbBoardVo(tbBoardVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00021", new String[] { "공지" });
		}
		return tbBoardVo;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public TbBoardVo deleteNotice(TbBoardVo tbBoardVo) {
		if (tbBoardVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {
			tbBoardDao.deleteTbBoardVo(tbBoardVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00022", new String[] { "공지" });
		}
		return tbBoardVo;
	}

}
