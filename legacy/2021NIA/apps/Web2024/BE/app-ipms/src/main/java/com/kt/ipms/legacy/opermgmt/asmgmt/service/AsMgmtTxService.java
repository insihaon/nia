package com.kt.ipms.legacy.opermgmt.asmgmt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kt.ipms.legacy.opermgmt.asmgmt.dao.TbRequestAsApyTxnDao;
import com.kt.ipms.legacy.opermgmt.asmgmt.dao.TbRequestAsHistDao;
import com.kt.ipms.legacy.opermgmt.asmgmt.dao.TbRequestAsMstDao;
import com.kt.ipms.legacy.opermgmt.asmgmt.vo.TbRequestAsApyTxnVo;
import com.kt.ipms.legacy.opermgmt.asmgmt.vo.TbRequestAsHistVo;
import com.kt.ipms.legacy.opermgmt.asmgmt.vo.TbRequestAsMstVo;

@Component
public class AsMgmtTxService {
	
	@Lazy @Autowired
	private TbRequestAsApyTxnDao tbRequestAsApyTxnDao;
	@Lazy @Autowired
	private TbRequestAsHistDao tbRequestAsHistDao;
	@Lazy @Autowired
	private TbRequestAsMstDao tbRequestAsMstDao;
	
	/*-------------------------------------- 사설AS 신청 start--------------------------------------------*/
	@Transactional(readOnly = true)
	public TbRequestAsApyTxnVo selectTbRequestAsApyTxnVo(TbRequestAsApyTxnVo tbRequestAsApyTxnVo) {
		return tbRequestAsApyTxnDao.selectTbRequestAsApyTxnVo(tbRequestAsApyTxnVo);
	}
	
	@Transactional(readOnly = true)
	public List<TbRequestAsApyTxnVo> selectListTbRequestAsApyTxnVo(TbRequestAsApyTxnVo tbRequestAsApyTxnVo) {
		return tbRequestAsApyTxnDao.selectListTbRequestAsApyTxnVo(tbRequestAsApyTxnVo);
	}
	
	@Transactional(readOnly = true)
	public List<TbRequestAsApyTxnVo> selectListPageTbRequestAsApyTxnVo(TbRequestAsApyTxnVo tbRequestAsApyTxnVo) {
		return tbRequestAsApyTxnDao.selectListPageTbRequestAsApyTxnVo(tbRequestAsApyTxnVo);
	}
	
	@Transactional(readOnly = true)
	public int countListTbRequestAsApyTxnVo(TbRequestAsApyTxnVo tbRequestAsApyTxnVo) {
		return tbRequestAsApyTxnDao.countListTbRequestAsApyTxnVo(tbRequestAsApyTxnVo);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public int insertTbRequestAsApyTxnVo(TbRequestAsApyTxnVo tbRequestAsApyTxnVo) {
		return tbRequestAsApyTxnDao.insertTbRequestAsApyTxnVo(tbRequestAsApyTxnVo);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public int updateTbRequestAsApyTxnVo(TbRequestAsApyTxnVo tbRequestAsApyTxnVo) {
		return tbRequestAsApyTxnDao.updateTbRequestAsApyTxnVo(tbRequestAsApyTxnVo);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public int deleteTbRequestAsApyTxnVo(TbRequestAsApyTxnVo tbRequestAsApyTxnVo) {
		return tbRequestAsApyTxnDao.deleteTbRequestAsApyTxnVo(tbRequestAsApyTxnVo);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public int updateNrequestAsSeqYn(TbRequestAsApyTxnVo tbRequestAsApyTxnVo) {
		return tbRequestAsApyTxnDao.updateNrequestAsSeqYn(tbRequestAsApyTxnVo);
	}
	//TbRequestAsApyTxn에서 조회후 이력테이블에 저장
	@Transactional(propagation = Propagation.REQUIRED)
	public int insertAsHist(TbRequestAsApyTxnVo tbRequestAsApyTxnVo) {
		return tbRequestAsApyTxnDao.insertAsHistVo(tbRequestAsApyTxnVo);
	}
	/*-------------------------------------- 사설AS 신청 End--------------------------------------------*/

	/*-------------------------------------- 사설AS 이력 Start -----------------------------------------*/
	@Transactional(readOnly = true)
	public TbRequestAsHistVo selectTbRequestAsHistVo(TbRequestAsHistVo tbRequestAsHistVo) {
		return tbRequestAsHistDao.selectTbRequestAsHistVo(tbRequestAsHistVo);
	}
	
	@Transactional(readOnly = true)
	public List<TbRequestAsHistVo> selectListPageTbRequestAsHistVo(TbRequestAsHistVo tbRequestAsHistVo) {
		return tbRequestAsHistDao.selectListPageTbRequestAsHistVo(tbRequestAsHistVo);
	}
	
	@Transactional(readOnly = true)
	public int countListTbRequestAsHistVo(TbRequestAsHistVo tbRequestAsHistVo) {
		return tbRequestAsHistDao.countListTbRequestAsHistVo(tbRequestAsHistVo);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public int insertTbRequestAsHistVo(TbRequestAsHistVo tbRequestAsHistVo) {
		return tbRequestAsHistDao.insertTbRequestAsHistVo(tbRequestAsHistVo);
	}
	/*-------------------------------------- 사설AS 이력 Start --------------------------------------------*/

	/*-------------------------------------- 사설AS MST Table Start -------------------------------------*/
	@Transactional(readOnly = true)
	public List<TbRequestAsMstVo> selectListPageTbRequestAsMstVo(TbRequestAsMstVo tbRequestAsMstVo) {
		return tbRequestAsMstDao.selectListPageTbRequestAsMstVo(tbRequestAsMstVo);
	}
	
	@Transactional(readOnly = true)
	public int countListTbRequestAsMstVo(TbRequestAsMstVo tbRequestAsMstVo) {
		return tbRequestAsMstDao.countListTbRequestAsMstVo(tbRequestAsMstVo);
	}
	
	@Transactional(readOnly = true)
	public int useCountListTbRequestAsMstVo(TbRequestAsMstVo tbRequestAsMstVo) {
		return tbRequestAsMstDao.useCountListTbRequestAsMstVo(tbRequestAsMstVo);
	}
	
	// 미사용중인 nrequest_as_seq 조회
	@Transactional(readOnly = true)
	public TbRequestAsMstVo selectMinNrequestAsSeq(TbRequestAsMstVo tbRequestAsMstVo) {
		return tbRequestAsMstDao.selectMinNrequestAsSeq(tbRequestAsMstVo);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public int updateTbRequestAsMstVo(TbRequestAsMstVo tbRequestAsMstVo) {
		return tbRequestAsMstDao.updateTbRequestAsMstVo(tbRequestAsMstVo);
	}
	
	/*-------------------------------------- 사설AS MST Table End ---------------------------------------*/
}
