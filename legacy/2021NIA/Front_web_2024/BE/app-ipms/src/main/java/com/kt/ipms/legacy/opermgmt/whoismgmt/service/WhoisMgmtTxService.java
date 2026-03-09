package com.kt.ipms.legacy.opermgmt.whoismgmt.service;

import java.math.BigInteger;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kt.ipms.legacy.cmn.vo.CommonCodeVo;
import com.kt.ipms.legacy.opermgmt.whoismgmt.dao.TbNewZipcodeDao;
import com.kt.ipms.legacy.opermgmt.whoismgmt.dao.TbWhoisComplexDao;
import com.kt.ipms.legacy.opermgmt.whoismgmt.dao.TbWhoisDao;
import com.kt.ipms.legacy.opermgmt.whoismgmt.dao.TbWhoisKeywordDao;
import com.kt.ipms.legacy.opermgmt.whoismgmt.dao.TbWhoisModifyDao;
import com.kt.ipms.legacy.opermgmt.whoismgmt.dao.TbWhoisUserDao;
import com.kt.ipms.legacy.opermgmt.whoismgmt.dao.TbZipcodeDao;
import com.kt.ipms.legacy.opermgmt.whoismgmt.vo.TbNewZipcodeVo;
import com.kt.ipms.legacy.opermgmt.whoismgmt.vo.TbWhoisComplexVo;
import com.kt.ipms.legacy.opermgmt.whoismgmt.vo.TbWhoisKeywordVo;
import com.kt.ipms.legacy.opermgmt.whoismgmt.vo.TbWhoisModifyVo;
import com.kt.ipms.legacy.opermgmt.whoismgmt.vo.TbWhoisUserVo;
import com.kt.ipms.legacy.opermgmt.whoismgmt.vo.TbWhoisVo;
import com.kt.ipms.legacy.opermgmt.whoismgmt.vo.TbZipcodeVo;
import com.kt.ipms.legacy.opermgmt.whoismgmt.vo.WhoisStatusVo;

@Component
@Transactional
public class WhoisMgmtTxService {

	@Lazy @Autowired
	private TbWhoisDao tbWhoisDao;

	@Lazy @Autowired
	private TbWhoisUserDao tbWhoisUserDao;

	@Lazy @Autowired
	private TbZipcodeDao tbZipcodeDao;

	@Lazy @Autowired
	private TbNewZipcodeDao tbNewZipcodeDao;

	@Lazy @Autowired
	private TbWhoisComplexDao tbWhoisComplexDao;

	@Lazy @Autowired
	private TbWhoisKeywordDao tbWhoisKeywordDao;

	@Lazy @Autowired
	private TbWhoisModifyDao tbWhoisModifyDao;
	
	@Transactional(propagation = Propagation.REQUIRED)
	public int updateWhois(TbWhoisVo tbWhoisVo) {
		return tbWhoisDao.updateTbWhoisVo(tbWhoisVo);
	}
	
	@Transactional(readOnly = true)
	public List<TbWhoisVo> selectListPageWhois(TbWhoisVo tbWhoisVo) {
		return tbWhoisDao.selectListPageTbWhoisVo(tbWhoisVo);
	}
	
	@Transactional(readOnly = true)
	public int countListPageWhois(TbWhoisVo tbWhoisVo) {
		return tbWhoisDao.countListPageTbWhoisVo(tbWhoisVo);
	}
	
	@Transactional(readOnly = true)
	public List<TbWhoisVo> selectListWhois(TbWhoisVo tbWhoisVo) {
		return tbWhoisDao.selectListTbWhoisVo(tbWhoisVo);
	}
	
	@Transactional(readOnly = true)
	public int countListWhois(TbWhoisVo tbWhoisVo) {
		return tbWhoisDao.countListTbWhoisVo(tbWhoisVo);
	}
	
	@Transactional(readOnly = true)
	public WhoisStatusVo countWhoisByStatus() {
		return tbWhoisDao.countWhoisByStatus();
	}

	@Transactional(readOnly = true)
	public TbWhoisUserVo selectWhoisUser(TbWhoisUserVo tbWhoisUserVo) {
		return tbWhoisUserDao.selectTbWhoisUserVo(tbWhoisUserVo);
	}

	@Transactional(readOnly = true)
	public TbWhoisVo selectWhois(TbWhoisVo tbWhoisVo) {
		// TODO Auto-generated method stub
		return tbWhoisDao.selectTbWhoisVo(tbWhoisVo);
	}
	
	@Transactional(readOnly = true)
	public List<CommonCodeVo> selectListVTbWhoisTransStatusCd() {
		return tbWhoisDao.selectListVTbWhoisTransStatusCd();
	}
	
	@Transactional(readOnly = true)
	public List<CommonCodeVo> selectListVTbWhoisReqTypeCd() {
		return tbWhoisDao.selectListVTbWhoisReqTypeCd();
	}
	
	@Transactional(readOnly = true)
	public List<TbZipcodeVo> selectListTbZipcode(TbZipcodeVo tbZipcodeVo) {
		return tbZipcodeDao.selectListPageTbZipcode(tbZipcodeVo);
	}
	
	@Transactional(readOnly = true)
	public List<TbNewZipcodeVo> selectListPageTbNewZipcode(TbNewZipcodeVo tbNewZipcodeVo) {
		return tbNewZipcodeDao.selectListPageTbNewZipcode(tbNewZipcodeVo);
	}
	
	@Transactional(readOnly = true)
	public int countListPageTbNewZipcode(TbNewZipcodeVo tbNewZipcodeVo) {
		return tbNewZipcodeDao.countListPageTbNewZipcode(tbNewZipcodeVo);
	}
	
	@Transactional(readOnly = true)
	public List<TbWhoisComplexVo> selectListTbWhoisComplexVo(TbWhoisComplexVo tbWhoisComplexVo) {
		return tbWhoisComplexDao.selectListTbWhoisComplexVo(tbWhoisComplexVo);
	}
	
	@Transactional(readOnly = true)
	public int countListTbWhoisComplexVo(TbWhoisComplexVo tbWhoisComplexVo) {
		return tbWhoisComplexDao.countListTbWhoisComplexVo(tbWhoisComplexVo);
	}
	
	@Transactional(readOnly = true)
	public List<TbWhoisComplexVo> selectListBatchWhoisComplexVo(TbWhoisComplexVo tbWhoisComplexVo) {
		return tbWhoisComplexDao.selectListBatchWhoisComplexVo(tbWhoisComplexVo);
	}
	
	@Transactional(readOnly = true)
	public int countListBatchWhoisComplexVo(TbWhoisComplexVo tbWhoisComplexVo) {
		return tbWhoisComplexDao.countListBatchWhoisComplexVo(tbWhoisComplexVo);
	}
	
	@Transactional(readOnly = true)
	public List<String> selectListScity() {
		return tbWhoisDao.selectListScity();
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public int updateWhoisUser(TbWhoisUserVo ktInfoVo) {
		return tbWhoisUserDao.updateKtInfoVo(ktInfoVo);
	}

	@Transactional(readOnly = true)
	public String selectEaddrDetail(String addrDetail) {
		return tbZipcodeDao.selectEaddrDetail(addrDetail);
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public int updateResultCd(TbWhoisVo tbWhoisVo) {
		return tbWhoisDao.updateResultCd(tbWhoisVo);
	}

	@Transactional(readOnly=true)
	public BigInteger selectTbWhoisVoSeq(TbWhoisVo tbWhoisVo) {
		return tbWhoisDao.selectTbWhoisVoSeq(tbWhoisVo);
	}
	
	@Transactional(readOnly=true)
	public List<TbWhoisKeywordVo> selectListTbWhoisKeyword(TbWhoisKeywordVo tbWhoisKeywordVo) {
		return tbWhoisKeywordDao.selectListPageTbWhoisKeywordVo(tbWhoisKeywordVo);
	}
	
	@Transactional(readOnly=true)
	public int countListPageTbWhoisKeyword(TbWhoisKeywordVo tbWhoisKeywordVo) {
		return tbWhoisKeywordDao.countListPageTbWhoisKeywordVo(tbWhoisKeywordVo);
	}
	
	@Transactional(readOnly=true)
	public int countListTbWhoisKeyword(TbWhoisKeywordVo tbWhoisKeywordVo) {
		return tbWhoisKeywordDao.countListTbWhoisKeywordVo(tbWhoisKeywordVo);
	}

	@Transactional(propagation=Propagation.REQUIRED)
	public int insertTbWhoisKeyword(TbWhoisKeywordVo tbWhoisKeywordVo) {
		return tbWhoisKeywordDao.insertTbWhoisKeywordVo(tbWhoisKeywordVo);
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public int deleteWhoisKeyword(TbWhoisKeywordVo tbWhoisKeywordVo) {
		return tbWhoisKeywordDao.deleteTbWhoisKeywordVo(tbWhoisKeywordVo);
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public int insertWhois(TbWhoisVo tbWhoisVo) {
		return tbWhoisDao.insertTbWhoisVo(tbWhoisVo);
	}
	
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public int updateResultCdTransactionNew(TbWhoisVo tbWhoisVo) {
		return tbWhoisDao.updateResultCd(tbWhoisVo);
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 *  WHOIS 대체 키워드 목록 조회
	 * @param tbWhoisKeywordVo
	 * @return
	 */
	@Transactional(readOnly=true)
	public List<TbWhoisKeywordVo> selectListTbWhoisKeywordNew(TbWhoisKeywordVo tbWhoisKeywordVo) {
		return tbWhoisKeywordDao.selectListPageTbWhoisKeywordNewVo(tbWhoisKeywordVo);
	}
	
	/**
	 * WHOIS 대체 키워드 목록 건수 조회
	 * @param tbWhoisKeywordVo
	 * @return
	 */
	@Transactional(readOnly=true)
	public int countListPageTbWhoisKeywordNew(TbWhoisKeywordVo tbWhoisKeywordVo) {
		return tbWhoisKeywordDao.countListPageTbWhoisKeywordNewVo(tbWhoisKeywordVo);
	}
	
	/**
	 * WHOIS 대체 키워드 등록
	 * @param tbWhoisKeywordVo
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public int insertTbWhoisKeywordNew(TbWhoisKeywordVo tbWhoisKeywordVo) {
		return tbWhoisKeywordDao.insertTbWhoisKeywordNewVo(tbWhoisKeywordVo);
	}
	
	/**
	 * WHOIS 대체 키워드 등록 건수 조회
	 * @param tbWhoisKeywordVo
	 * @return
	 */
	@Transactional(readOnly=true)
	public int countListTbWhoisKeywordNew(TbWhoisKeywordVo tbWhoisKeywordVo) {
		return tbWhoisKeywordDao.countListPageTbWhoisKeywordNewVo(tbWhoisKeywordVo);
	}
	
	/**
	 * WHOIS 대체 키워드 삭제
	 * @param tbWhoisKeywordVo
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public int deleteWhoisKeywordNew(TbWhoisKeywordVo tbWhoisKeywordVo) {
		return tbWhoisKeywordDao.deleteTbWhoisKeywordNewVo(tbWhoisKeywordVo);
	}
	
	/**
	 * WHOIS 정보수정 팝업
	 * @param tbWhoisVo
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public int updateWhoisNew(TbWhoisVo tbWhoisVo) {
		return tbWhoisDao.updateTbWhoisNewVo(tbWhoisVo);
	}
	
	/**
	 * WHOIS 정보수정 팝업
	 * @param tbWhoisVo
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public int updateWhoisUserNew(TbWhoisUserVo tbWhoisUserVo) {
		return tbWhoisUserDao.updateTbWhoisUserNewVo(tbWhoisUserVo);
	}
	
	
	/**
	 * WHOIS정보변경신청 - 목록 조회
	 * @param tbWhoisModifyVo
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<TbWhoisModifyVo> selectListPageTbWhoisModifyVo(TbWhoisModifyVo tbWhoisModifyVo) {
		return tbWhoisModifyDao.selectListPageTbWhoisModifyVo(tbWhoisModifyVo);
	}
	
	/**
	 * WHOIS정보변경신청 - WHOIS 정보 조회
	 * @param tbWhoisModifyVo
	 * @return
	 */
	@Transactional(readOnly=true)
	public List<TbWhoisModifyVo> selectListTbWhoisModReqIp(TbWhoisModifyVo tbWhoisModifyVo) {
		return tbWhoisModifyDao.selectListTbWhoisModReqIp(tbWhoisModifyVo);
	}
	
	/**
	 * WHOIS정보변경신청 - WHOIS 정보 조회 list count
	 * @param tbWhoisModifyVo
	 * @return
	 */
	@Transactional(readOnly=true)
	public int countListPageTbWhoisModReqIp(TbWhoisModifyVo tbWhoisModifyVo) {
		return tbWhoisModifyDao.countListPageTbWhoisModReqIp(tbWhoisModifyVo);
	}

	/**
	 * WHOIS정보변경신청 - WHOIS 변경신청 조회 list count
	 * @param tbWhoisModifyVo
	 * @return
	 */
	@Transactional(readOnly=true)
	public int countListTbWhoisModifyVo(TbWhoisModifyVo tbWhoisModifyVo) {
		return tbWhoisModifyDao.countListTbWhoisModifyVo(tbWhoisModifyVo);
	}

	/**
	 * WHOIS정보변경신청 - WHOIS 변경신청 조회 count
	 * @param tbWhoisModifyVo
	 * @return
	 */
	@Transactional(readOnly=true)
	public int countTbWhoisModifyVo(TbWhoisModifyVo tbWhoisModifyVo) {
		return tbWhoisModifyDao.countTbWhoisModifyVo(tbWhoisModifyVo);
	}	
	
	/**
	 * WHOIS정보변경신청 - 등록(변경신청)
	 * @param tbWhoisModifyVo
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public int insertTbWhoisModifyVo(TbWhoisModifyVo tbWhoisModifyVo) {
		return tbWhoisModifyDao.insertTbWhoisModifyVo(tbWhoisModifyVo);
	}
	
	/**
	 * WHOIS 정보 변경 신청 상세조회
	 * @param tbWhoisModifyVo
	 * @return
	 */
	@Transactional(readOnly = true)
	public TbWhoisModifyVo selectTbWhoisModifyVo(TbWhoisModifyVo tbWhoisModifyVo) {
		return tbWhoisModifyDao.selectTbWhoisModifyVo(tbWhoisModifyVo);
	}
	
	/**
	 * WHOIS정보변경신청 - 수정
	 * @param tbWhoisModifyVo
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public int updateWhoisModReqVo(TbWhoisModifyVo tbWhoisModifyVo) {
		return tbWhoisModifyDao.updateWhoisModReqVo(tbWhoisModifyVo);
	}
	
	/**
	 * WHOIS정보변경신청 - 수정(변경신청취소)
	 * @param tbWhoisModifyVo
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public int deleteWhoisModReqVo(TbWhoisModifyVo tbWhoisModifyVo) {
		return tbWhoisModifyDao.deleteWhoisModReqVo(tbWhoisModifyVo);
	}
	
	/**
	 * WHOIS정보변경신청 - 승인/반려
	 * @param tbWhoisModifyVo
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public int updateWhoisModReqAppr(TbWhoisModifyVo tbWhoisModifyVo) {
		return tbWhoisModifyDao.updateWhoisModReqAppr(tbWhoisModifyVo);
	}
	
	/**
	 * WHOIS Network 정보 조회
	 * @param tbWhoisModifyVo
	 * @return
	 */
	@Transactional(readOnly = true)
	public com.kt.ipms.legacy.linkmgmt.whois.vo.TbWhoisVo selectNetTbWhoisVo(TbWhoisModifyVo tbWhoisModifyVo) {
		return tbWhoisModifyDao.selectNetTbWhoisVo(tbWhoisModifyVo);
	}
	
	/**
	 * WHOIS정보공개관리 - 삭제
	 * @param tbWhoisVo
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public int deleteTbWhoisVo(TbWhoisVo tbWhoisVo) {
		return tbWhoisDao.deleteTbWhoisNewVo(tbWhoisVo);
	}
	
	/**
	 * WHOIS정보공개관리 - 삭제
	 * @param tbWhoisVo
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public int deleteTbWhoisUserVo(TbWhoisVo tbWhoisVo) {
		return tbWhoisDao.deleteTbWhoisUserNewVo(tbWhoisVo);
	}

	/**
	 * WHOIS정보공개관리 - DB 현행화
	 * @param tbWhoisVo
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public String matchTbWhoisVo(TbWhoisVo tbWhoisVo) {
		return tbWhoisDao.matchTbWhoisVo(tbWhoisVo);
	}
	
	/**
	 * TB_WHOIS  Update
	 * @param tbWhoisModifyVo
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public int updateTbWhoisTrans(TbWhoisVo updateVo) {
		return tbWhoisDao.updateTbWhoisTrans(updateVo);
	}
	
	/**
	 * TB_WHOIS_USER  Update
	 * @param tbWhoisModifyVo
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public int updateTbWhoisUserTrans(TbWhoisUserVo updateVo) {
		return tbWhoisUserDao.updateTbWhoisUserNewVo(updateVo);
	}
	
	/**
	 * whois kisa 정보 조회 및 관리 - IP주소 조회
	 * @param tbWhoisModifyVo
	 * @return
	 */
	@Transactional(readOnly=true)
	public TbWhoisComplexVo selectListTbWhoisControlIp(TbWhoisVo tbWhoisVo) {
		return tbWhoisDao.selectListTbWhoisControlIp(tbWhoisVo);
	}
	
	/**
	 * Whois 반송 건 조회
	 * @param tbWhoisVo
	 * @return
	 */
	@Transactional(readOnly=true)
	public String selectWhoisComplexNew(TbWhoisVo tbWhoisVo) {
		return tbWhoisDao.selectWhoisComplexNew(tbWhoisVo);
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public TbWhoisComplexVo selectWhoisAlloc(TbWhoisVo tbWhoisVo) {
		return tbWhoisDao.selectWhoisAlloc(tbWhoisVo);
	}
	
	@Transactional(readOnly = true)
	public List<TbWhoisVo> selectListDbMatch(TbWhoisVo tbWhoisVo) {
		return tbWhoisDao.selectListDbMatch(tbWhoisVo);
	}
	
	@Transactional(readOnly = true)
	public int countListDbMatch(TbWhoisVo tbWhoisVo) {
		return tbWhoisDao.countListDbMatch(tbWhoisVo);
	}
}
