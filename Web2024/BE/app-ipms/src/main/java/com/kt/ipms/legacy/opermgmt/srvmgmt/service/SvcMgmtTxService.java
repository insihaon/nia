package com.kt.ipms.legacy.opermgmt.srvmgmt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kt.ipms.legacy.opermgmt.srvmgmt.dao.TbAssignTypeCdDao;
import com.kt.ipms.legacy.opermgmt.srvmgmt.dao.TbIpmsSvcMstDao;
import com.kt.ipms.legacy.opermgmt.srvmgmt.dao.TbIpmsSvcSubDao;
import com.kt.ipms.legacy.opermgmt.srvmgmt.dao.TbSvcLgroupCdDao;
import com.kt.ipms.legacy.opermgmt.srvmgmt.vo.TbAssignTypeCdVo;
import com.kt.ipms.legacy.opermgmt.srvmgmt.vo.TbIpmsSvcMstVo;
import com.kt.ipms.legacy.opermgmt.srvmgmt.vo.TbIpmsSvcSubVo;
import com.kt.ipms.legacy.opermgmt.srvmgmt.vo.TbSvcLgroupCdVo;

@Component
public class SvcMgmtTxService {
	@Lazy @Autowired
	private TbIpmsSvcMstDao tbIpmsSvcMstDao;
	@Lazy @Autowired
	private TbAssignTypeCdDao tbAssignTypeCdDao;
	@Lazy @Autowired
	private TbIpmsSvcSubDao tbIpmsSvcSubDao;
	@Lazy @Autowired
	private TbSvcLgroupCdDao tbSvcLgroupCdDao;
	
	/* 상품 목록 리스트 조회 */
	@Transactional(readOnly = true)
	public List<TbIpmsSvcMstVo> selectListPageTbIpmsSvcMstVo(TbIpmsSvcMstVo tbIpmsSvcMstVo) {
		return tbIpmsSvcMstDao.selectListPageTbIpmsSvcMstVo(tbIpmsSvcMstVo);
	}
	/* 상품 목록 리스트 개수 */
	@Transactional(readOnly = true)
	public int countListPageTbIpmsSvcMstVo(TbIpmsSvcMstVo tbIpmsSvcMstVo) {
		return tbIpmsSvcMstDao.countListPageTbIpmsSvcMstVo(tbIpmsSvcMstVo);
	}
	/* 상품 대분류 조회 */
	@Transactional(readOnly = true)
	public List<TbIpmsSvcMstVo> selectListSvcMgroupNmCd(TbIpmsSvcMstVo tbIpmsSvcMstVo) {
		return tbIpmsSvcMstDao.selectListSvcMgroupNmCd(tbIpmsSvcMstVo);
	}
	/* 상품 소분류 조회 */
	@Transactional(readOnly = true)
	public List<TbIpmsSvcMstVo> selectListSvcMgroupNm1Cd(TbIpmsSvcMstVo tbIpmsSvcMstVo) {
		return tbIpmsSvcMstDao.selectListSvcMgroupNm1Cd(tbIpmsSvcMstVo);
	}
	/* 상품 상세 정보 조회 */
	@Transactional(readOnly = true)
	public TbIpmsSvcMstVo selectTbIpmsSvcMstVo(TbIpmsSvcMstVo tbIpmsSvcMstVo) {
		return tbIpmsSvcMstDao.selectTbIpmsSvcMstVo(tbIpmsSvcMstVo);
	}
	/* 서비스분류 조회 */
	@Transactional(readOnly = true)
	public List<TbIpmsSvcMstVo> selectListSvcMgroup(TbIpmsSvcMstVo tbIpmsSvcMstVo) {
		return tbIpmsSvcMstDao.selectListSvcMgroup(tbIpmsSvcMstVo);
	}
	/* 외부연계 조회 */
	@Transactional(readOnly = true)
	public TbIpmsSvcMstVo selectSexLinkUseTypeCd(TbIpmsSvcMstVo tbIpmsSvcMstVo) {
		return tbIpmsSvcMstDao.selectSexLinkUseTypeCd(tbIpmsSvcMstVo);
	}
	/* 상품명 조회 */
	@Transactional(readOnly = true)
	public List<TbIpmsSvcMstVo> selectListSvcLgroup(TbIpmsSvcMstVo tbIpmsSvcMstVo) {
		return tbIpmsSvcMstDao.selectListSvcLgroup(tbIpmsSvcMstVo);
	}
	/* 상품 들록 */
	@Transactional(propagation = Propagation.REQUIRED)
	public int insertTbIpmsSvcMstVo(TbIpmsSvcMstVo tbIpmsSvcMstVo) {
		return tbIpmsSvcMstDao.insertTbIpmsSvcMstVo(tbIpmsSvcMstVo);
	}
	/* 상품 수정 */
	@Transactional(propagation = Propagation.REQUIRED)
	public int updateTbIpmsSvcMstVo(TbIpmsSvcMstVo tbIpmsSvcMstVo) {
		return tbIpmsSvcMstDao.updateTbIpmsSvcMstVo(tbIpmsSvcMstVo);
	}
	
	/* New LgroupCd 조회 */
	@Transactional(readOnly = true)
	public String selectNewLgroupCd() {
		return tbSvcLgroupCdDao.selectNewLgroupCd();
	}
	
	/* Lgroup 등록 */
	@Transactional(propagation = Propagation.REQUIRED)
	public int insertTbSvcLgroupCdVo(TbSvcLgroupCdVo tbSvcLgroupCdVo) {
		return tbSvcLgroupCdDao.insertTbSvcLgroupCdVo(tbSvcLgroupCdVo);
	}
	/* Lgroup 수정 */
	@Transactional(propagation = Propagation.REQUIRED)
	public int updateTbSvcLgroupCdVo(TbSvcLgroupCdVo tbSvcLgroupCdVo) {
		return tbSvcLgroupCdDao.updateTbSvcLgroupCdVo(tbSvcLgroupCdVo);
	}
	
	
	/* 상품 서비스망 리스트 조회 */
	@Transactional(readOnly = true)
	public List<TbIpmsSvcSubVo> selectListTbIpmsSvcSubVo(TbIpmsSvcSubVo tbIpmsSvcSubVo) {
		return tbIpmsSvcSubDao.selectListTbIpmsSvcSubVo(tbIpmsSvcSubVo);
	}
	/* 상품 서비스망 등록 */
	@Transactional(propagation = Propagation.REQUIRED)
	public int insertTbIpmsSvcSubVo(TbIpmsSvcSubVo tbIpmsSvcSubVo) {
		return tbIpmsSvcSubDao.insertTbIpmsSvcSubVo(tbIpmsSvcSubVo);
	}
	/* 상품 서비스망 삭제 */
	@Transactional(propagation = Propagation.REQUIRED)
	public int deleteTbIpmsSvcSubVo(TbIpmsSvcSubVo tbIpmsSvcSubVo) {
		return tbIpmsSvcSubDao.deleteTbIpmsSvcSubVo(tbIpmsSvcSubVo);
	}
	
	
	/* 서비스 코드 리스트 조회  */
	@Transactional(readOnly = true)
	public List<TbAssignTypeCdVo> selectListPageTbAssignTypeCdVo(TbAssignTypeCdVo tbAssignTypeCdVo) {
		return tbAssignTypeCdDao.selectListPageTbAssignTypeCdVo(tbAssignTypeCdVo);
	}
	
	/* 서비스 코드 리스트 개수  */
	@Transactional(readOnly = true)
	public int countListTbAssignTypeCdVo(TbAssignTypeCdVo tbAssignTypeCdVo) {
		return tbAssignTypeCdDao.countListTbAssignTypeCdVo(tbAssignTypeCdVo);
	}
	
	/* 서비스 코드 조회 */
	@Transactional(readOnly = true)
	public TbAssignTypeCdVo selectTbAssignTypeCdVo(TbAssignTypeCdVo tbAssignTypeCdVo) {
		return tbAssignTypeCdDao.selectTbAssignTypeCdVo(tbAssignTypeCdVo);
	}
	
	/* 서비스 코드 등록 */
	@Transactional(propagation = Propagation.REQUIRED)
	public int insertTbAssignTypeCdVo(TbAssignTypeCdVo tbAssignTypeCdVo) {
		return tbAssignTypeCdDao.insertTbAssignTypeCdVo(tbAssignTypeCdVo);
	}
	
	/* 서비스 코드 수정 */
	@Transactional(propagation = Propagation.REQUIRED)
	public int updateTbAssignTypeCdVo(TbAssignTypeCdVo tbAssignTypeCdVo) {
		return tbAssignTypeCdDao.updateTbAssignTypeCdVo(tbAssignTypeCdVo);
	}
	
}
 