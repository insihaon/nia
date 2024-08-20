package com.kt.ipms.legacy.opermgmt.nonktipmgmt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kt.framework.exception.ServiceException;
import com.kt.ipms.legacy.cmn.vo.CommonCodeVo;
import com.kt.ipms.legacy.opermgmt.nonktipmgmt.dao.TbNonKtIpblockDao;
import com.kt.ipms.legacy.opermgmt.nonktipmgmt.dao.TbNonKtSvcMstDao;
import com.kt.ipms.legacy.opermgmt.nonktipmgmt.vo.TbNonKtIpblockVo;
import com.kt.ipms.legacy.opermgmt.nonktipmgmt.vo.TbNonKtSvcMstVo;

@Component
public class NonKtIpMgmtTxService {
	@Autowired
	private TbNonKtSvcMstDao tbNonKtSvcMstDao;
	@Autowired
	private TbNonKtIpblockDao tbNonKtIpblockDao;
	
	/* Non-KT IP Mst 목록 리스트 조회 */
	@Transactional(readOnly = true)
	public List<TbNonKtSvcMstVo> selectListPageTbNonKtSvcMstVo(TbNonKtSvcMstVo tbNonKtSvcMstVo) {
		return tbNonKtSvcMstDao.selectListPageTbNonKtSvcMstVo(tbNonKtSvcMstVo);
	}
	
	/* Non-KT IP Mst 천체항목 개수 */
	@Transactional(readOnly = true)
	public int countListTbNonKtSvcMstVo(TbNonKtSvcMstVo tbNonKtSvcMstVo) {
		return tbNonKtSvcMstDao.countListTbNonKtSvcMstVo(tbNonKtSvcMstVo);
	}
	
	/* Non-KT IP 상세조회 */
	@Transactional(readOnly = true)
	public TbNonKtSvcMstVo selectTbNonKtSvcMstVo(TbNonKtSvcMstVo tbNonKtSvcMstVo) {
		return tbNonKtSvcMstDao.selectTbNonKtSvcMstVo(tbNonKtSvcMstVo);
	}
	
	/* 노드국  리스트 조회 */
	@Transactional(readOnly = true)
	public List<CommonCodeVo> selectIcisOfficesCodeList(TbNonKtSvcMstVo tbNonKtSvcMstVo) {
		return tbNonKtSvcMstDao.selectIcisOfficesCodeList(tbNonKtSvcMstVo);
	}
	
	/* 수용국 리스트 조회 */
	@Transactional(readOnly = true)
	public List<CommonCodeVo> selectsOfficeCodeList(TbNonKtSvcMstVo tbNonKtSvcMstVo) {
		return tbNonKtSvcMstDao.selectsOfficeCodeList(tbNonKtSvcMstVo);
	}
	
	/* Non-Kt IP Mst 수정 */
	@Transactional(propagation = Propagation.REQUIRED)
	public int updateTbNonKtSvcMstVo(TbNonKtSvcMstVo tbNonKtSvcMstVo) {
		return tbNonKtSvcMstDao.updateTbNonKtSvcMstVo(tbNonKtSvcMstVo);
	}
	
	/* Non-KT IP Block 목록 리스트 조회 */
	@Transactional(readOnly = true)
	public List<TbNonKtIpblockVo> selectListTbNonKtIpblockVo(TbNonKtIpblockVo tbNonKtIpblockVo) {
		return tbNonKtIpblockDao.selectListTbNonKtIpblockVo(tbNonKtIpblockVo);
	}
	
	/* Non-KT IP Block 전체항목 개수 */
	@Transactional(readOnly = true)
	public int countListTbNonKtIpblockVo(TbNonKtIpblockVo tbNonKtIpblockVo) {
		return tbNonKtIpblockDao.countListTbNonKtIpblockVo(tbNonKtIpblockVo);
	}
	
	
	/* Non_KT IP블록 단건 조회 */ 
	@Transactional(readOnly = true)
	public TbNonKtIpblockVo selectCheckTbNonKtIpblockVo(TbNonKtIpblockVo tbNonKtIpblockVo) {
		return tbNonKtIpblockDao.selectCheckTbNonKtIpblockVo(tbNonKtIpblockVo);
	}
	
	/* Non_KT IP블록 등록 */
	@Transactional(propagation = Propagation.REQUIRED)
	public int insertTbNonKtIpblockVo(TbNonKtIpblockVo tbNonKtIpblockVo) {
		return tbNonKtIpblockDao.insertTbNonKtIpblockVo(tbNonKtIpblockVo);
	}
	
	/* Non KT Ip블록 수정 */
	@Transactional(propagation = Propagation.REQUIRED)
	public int updateTbNonKtIpblockVo(TbNonKtIpblockVo tbNonKtIpblockVo) {
		return tbNonKtIpblockDao.updateTbNonKtIpblockVo(tbNonKtIpblockVo);
	}
	
	/* Non_KT IP블록s 등록 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void insertTbNonKtIpblockListVo(List<TbNonKtIpblockVo> tbNonKtIpblockVos) {
		try {
			int insertCnt = 0;
			for(TbNonKtIpblockVo tbNonKtIpblockVo : tbNonKtIpblockVos) {
				insertCnt += tbNonKtIpblockDao.insertTbNonKtIpblockVo(tbNonKtIpblockVo);
			}
			if (insertCnt != tbNonKtIpblockVos.size()) {
				throw new ServiceException("CMN.HIGH.00020", new String[]{"IP블록"});
			}
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00020", new String[]{"IP블록"});
		}
		
	}
	
	/* Non_KT IP블록s 수정 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void updateTbNonKtIpblockListVo(List<TbNonKtIpblockVo> tbNonKtIpblockVos) {
		try {
			int updateCnt = 0;
			for(TbNonKtIpblockVo tbNonKtIpblockVo : tbNonKtIpblockVos) {
				updateCnt += tbNonKtIpblockDao.updateTbNonKtIpblockVo(tbNonKtIpblockVo);
			}
			if (updateCnt != tbNonKtIpblockVos.size()) {
				throw new ServiceException("CMN.HIGH.00021", new String[]{"IP블록"});
			}
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00021", new String[]{"IP블록"});
		}
	}

}
