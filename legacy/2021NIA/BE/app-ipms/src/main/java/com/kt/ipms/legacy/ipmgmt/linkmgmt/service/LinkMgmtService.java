package com.kt.ipms.legacy.ipmgmt.linkmgmt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kt.framework.exception.ServiceException;
import com.kt.ipms.legacy.cmn.service.IpCommonService;
import com.kt.ipms.legacy.cmn.vo.CommonCodeVo;
import com.kt.ipms.legacy.ipmgmt.hostmgmt.vo.TbIpHostMstVo;
import com.kt.ipms.legacy.ipmgmt.linkmgmt.vo.TbIpLinkMstListVo;
import com.kt.ipms.legacy.ipmgmt.linkmgmt.vo.TbIpLinkMstVo;

@Component
@Transactional
public class LinkMgmtService {

	@Autowired
	private LinkMgmtTxService linkMgmtTxService;

	@Autowired
	private IpCommonService ipCommonService;
	
	/**
	 * 운용정보관리(링크) > 목록 조회
	 * @param tbIpLinkMstVo
	 * @return
	 */
	@Transactional(readOnly = true)
	public TbIpLinkMstListVo selectListIpLinkMst(TbIpLinkMstVo tbIpLinkMstVo){
		TbIpLinkMstListVo resultListVo = null;
		try {
			if (tbIpLinkMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			List<TbIpLinkMstVo> resultList = linkMgmtTxService.selectListPageIpLinkMst(tbIpLinkMstVo);
			int totalCount = linkMgmtTxService.countListPageIpLinkMst(tbIpLinkMstVo);
			resultListVo = new TbIpLinkMstListVo();
			resultListVo.setTbIpLinkMstVos(resultList);
			resultListVo.setTotalCount(totalCount);
		} catch (ServiceException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("CMN.HIGH.00023", new String[]{"IP LINK 목록"});
		}
		
		return resultListVo;
	}
	
	/**
	 * 운용정보관리(링크) > 수용국정보 조회
	 * @param tbIpLinkMstVo
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<CommonCodeVo> selectLoadOfficeList(TbIpLinkMstVo tbIpLinkMstVo){
		List<CommonCodeVo> resultListVo = null;
		
		try {
			if (tbIpLinkMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			
			resultListVo = linkMgmtTxService.selectLoadOfficeList(tbIpLinkMstVo);

		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"수용국 목록"});
		}
		return resultListVo;
	}
	
	/**
	 * 운용정보관리(링크) > 수용국정보 조회
	 * @param tbIpLinkMstVo
	 * @return
	 */
	@Transactional(readOnly = true)
	public TbIpLinkMstListVo selectOfficeList(TbIpLinkMstVo tbIpLinkMstVo){
		TbIpLinkMstListVo resultListVo = null;
		try {
			if (tbIpLinkMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			List<TbIpLinkMstVo> resultList = linkMgmtTxService.selectOfficeList(tbIpLinkMstVo);
			resultListVo = new TbIpLinkMstListVo();
			resultListVo.setTbIpLinkMstVos(resultList);
		} catch (ServiceException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("CMN.HIGH.00023", new String[]{"수용국 목록"});
		}
		return resultListVo;
	}
	
	/**
	 *  운용정보관리(링크) > 상세 조회
	 * @param tbIpLinkMstVo
	 * @return
	 */
	@Transactional(readOnly = true)
	public TbIpLinkMstVo selectTbIpLinkMstVo(TbIpLinkMstVo tbIpLinkMstVo){
		TbIpLinkMstVo resultVo = null;
		try {
			if (tbIpLinkMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			 resultVo = linkMgmtTxService.selectTbIpLinkMstVo(tbIpLinkMstVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"수용국 목록"});
		}
		return resultVo;
	}
	
	/**
	 * 운용정보관리(링크) > 등록
	 * @param tbIpLinkMstVo
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void insertIpLinkMst(TbIpLinkMstVo tbIpLinkMstVo){
		try {	
			int checkCnt = 0;
			
			TbIpLinkMstVo checkVo = new TbIpLinkMstVo();
			checkVo.setPifSerialIp(tbIpLinkMstVo.getPifSerialIp());
			checkCnt = linkMgmtTxService.countTbIpLinkMstVo(checkVo);
			if(checkCnt > 0){
				throw new ServiceException("APP.INFO.00046", new String[]{tbIpLinkMstVo.getPifSerialIp()});
			}
			
			tbIpLinkMstVo.setPipPrefix(tbIpLinkMstVo.getPifSerialIp());
			tbIpLinkMstVo.setSipVersionTypeCd("CV0001");
			ipCommonService.setBaseIpBlockMstInfo(tbIpLinkMstVo);			
			linkMgmtTxService.insertIpLinkMst(tbIpLinkMstVo);
			
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00020", new String[]{"링크 정보"});
		}
	}
	
	/**
	 * 운용정보관리(링크) > 수정
	 * @param tbIpLinkMstVo
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void updateTbIpLinkMstVo(TbIpLinkMstVo tbIpLinkMstVo){
		try {				
			
			if(tbIpLinkMstVo == null){
				throw new ServiceException("CMN.HIGH.00001");
			}			
					
			linkMgmtTxService.updateTbIpLinkMstVo(tbIpLinkMstVo);	
			
			// 할당 테이블 링크 정보 수정
			linkMgmtTxService.upateTbIpAllocMstVo(tbIpLinkMstVo);	
			
		} catch (ServiceException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("CMN.HIGH.00021", new String[]{"링크 정보"});
		}
	}
	
	/**
	 * 운용정보관리(링크) > 삭제
	 * @param tbIpLinkMstVo
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteIpLinkMst(TbIpLinkMstVo tbIpLinkMstVo){
		try {				
			linkMgmtTxService.deleteTbIpLinkMstVo(tbIpLinkMstVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00022", new String[]{"링크 정보"});
		}
	}
}
