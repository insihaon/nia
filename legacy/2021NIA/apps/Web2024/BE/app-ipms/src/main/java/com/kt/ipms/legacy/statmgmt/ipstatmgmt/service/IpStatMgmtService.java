package com.kt.ipms.legacy.statmgmt.ipstatmgmt.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.kt.framework.exception.ServiceException;
import com.kt.ipms.legacy.cmn.util.CommonCodeUtil;
import com.kt.ipms.legacy.cmn.vo.CommonCodeVo;
import com.kt.ipms.legacy.ipmgmt.allocmgmt.vo.TbIpAllocMstVo;
import com.kt.ipms.legacy.statmgmt.ipstatmgmt.vo.IpCreateStatListVo;
import com.kt.ipms.legacy.statmgmt.ipstatmgmt.vo.IpCreateStatVo;
import com.kt.ipms.legacy.statmgmt.ipstatmgmt.vo.IpIntgrmSvcStatVo;
import com.kt.ipms.legacy.statmgmt.ipstatmgmt.vo.IpOrgServiceStatListVo;
import com.kt.ipms.legacy.statmgmt.ipstatmgmt.vo.IpOrgServiceStatVo;
import com.kt.ipms.legacy.statmgmt.ipstatmgmt.vo.IpOrgStatListVo;
import com.kt.ipms.legacy.statmgmt.ipstatmgmt.vo.IpOrgStatVo;
import com.kt.ipms.legacy.statmgmt.ipstatmgmt.vo.IpSingleBlockStatListVo;
import com.kt.ipms.legacy.statmgmt.ipstatmgmt.vo.IpSingleBlockStatVo;

@Component
@Transactional
public class IpStatMgmtService {
	
@Lazy @Autowired
	private IpStatMgmtTxService ipStatMgmtTxService;
	
	/*1.생성차수별 IP현황 메인 조회 : 수정한것 */
	@Transactional(readOnly = true)
	public IpCreateStatListVo selectListIpCreateStat(IpCreateStatVo ipCreateStatVo) {
		IpCreateStatListVo resultListVo = null;
		if (ipCreateStatVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {
			List<IpCreateStatVo> resultList = ipStatMgmtTxService.selectListPageIpCreateStatVo(ipCreateStatVo);
			int totalCount = ipStatMgmtTxService.countListPageIpCreateStatVo(ipCreateStatVo);
			resultListVo = new IpCreateStatListVo();
			resultListVo.setIpCreateStatVos(resultList);
			resultListVo.setTotalCount(totalCount);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"생성차수별 IP현황 목록"});
		}
		return resultListVo;
	}
	
	/*1.생성차수별 IP현황 메인 조회 Excel : 수정한것 */
	@Transactional(readOnly = true)
	public IpCreateStatListVo selectListIpCreateStatExcel(IpCreateStatVo ipCreateStatVo) {
		IpCreateStatListVo resultListVo = null;
		if (ipCreateStatVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {
			int totalCount = ipStatMgmtTxService.countListPageIpCreateStatVo(ipCreateStatVo);
			if (totalCount > CommonCodeUtil.MAX_EXCEL_ROW_COUNT) {
				throw new ServiceException("CMN.INFO.00051");
			}
			List<IpCreateStatVo> resultList = null; 
			if (totalCount > 0) {
				ipCreateStatVo.setFirstIndex(1);
				ipCreateStatVo.setLastIndex(totalCount);
				resultList = ipStatMgmtTxService.selectListPageIpCreateStatVo(ipCreateStatVo);
			}
			resultListVo = new IpCreateStatListVo();
			resultListVo.setIpCreateStatVos(resultList);
			resultListVo.setTotalCount(totalCount);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"생성차수별 IP현황 목록"});
		}
		return resultListVo;
	}
	
	/*2.조직별 IP현황 메인 조회*/
	@Transactional(readOnly = true)
	public IpOrgStatListVo selectListOrgIpStatMst(IpOrgStatVo ipOrgStatVo){
		IpOrgStatListVo resultListVo = null;
		if (ipOrgStatVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {
			List<IpOrgStatVo> resultList = ipStatMgmtTxService.selectListPageIpOrgStatVo(ipOrgStatVo);
			int totalCount = ipStatMgmtTxService.countListPageIpOrgStatVo(ipOrgStatVo);
			resultListVo = new IpOrgStatListVo();
			resultListVo.setIpOrgStatVos(resultList);
			resultListVo.setTotalCount(totalCount);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"조직별 IP현황 목록"});
		}
		return resultListVo;
	}
	
	/*2.조직별 IP현황 메인 조회 Excel*/
	@Transactional(readOnly = true)
	public IpOrgStatListVo selectListOrgIpStatMstExcel(IpOrgStatVo ipOrgStatVo){
		IpOrgStatListVo resultListVo = null;
		if (ipOrgStatVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {
			
			int totalCount = ipStatMgmtTxService.countListPageIpOrgStatVo(ipOrgStatVo);
			if (totalCount > CommonCodeUtil.MAX_EXCEL_ROW_COUNT) {
				throw new ServiceException("CMN.INFO.00051");
			}
			List<IpOrgStatVo> resultList = null;
			if (totalCount > 0) {
				ipOrgStatVo.setFirstIndex(1);
				ipOrgStatVo.setLastIndex(totalCount);
				resultList = ipStatMgmtTxService.selectListPageIpOrgStatVo(ipOrgStatVo);	
			}
			
			resultListVo = new IpOrgStatListVo();
			resultListVo.setIpOrgStatVos(resultList);
			resultListVo.setTotalCount(totalCount);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"조직별 IP현황 목록"});
		}
		return resultListVo;
	}
	
	/*3.조직서비스별 IP현황 메인 조회*/
	@Transactional(readOnly = true)
	public IpOrgServiceStatListVo selectListOrgSvcIpStatMst(IpOrgServiceStatVo ipOrgServiceStatVo){
		IpOrgServiceStatListVo resultListVo = null;
		if (ipOrgServiceStatVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {
			List<IpOrgServiceStatVo> resultList = ipStatMgmtTxService.selectListPageIpOrgServiceStatVo(ipOrgServiceStatVo);
			int totalCount = ipStatMgmtTxService.countListPageIpOrgServiceStatVo(ipOrgServiceStatVo);
			resultListVo = new IpOrgServiceStatListVo();
			resultListVo.setIpOrgServiceStatVos(resultList);
			resultListVo.setTotalCount(totalCount);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"조직서비스별 IP현황 목록"});
		}
		return resultListVo;
	}
	
	/*3.조직서비스별 IP현황 메인 조회 Excel*/
	@Transactional(readOnly = true)
	public IpOrgServiceStatListVo selectListOrgSvcIpStatMstExcel(IpOrgServiceStatVo ipOrgServiceStatVo){
		IpOrgServiceStatListVo resultListVo = null;
		if (ipOrgServiceStatVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {
			
			int totalCount = ipStatMgmtTxService.countListPageIpOrgServiceStatVo(ipOrgServiceStatVo);
			if (totalCount > CommonCodeUtil.MAX_EXCEL_ROW_COUNT) {
				throw new ServiceException("CMN.INFO.00051");
			}
			List<IpOrgServiceStatVo> resultList = null;
			if (totalCount > 0) {
				resultList = ipStatMgmtTxService.selectListPageIpOrgServiceStatVo(ipOrgServiceStatVo);	
			}
			
			resultListVo = new IpOrgServiceStatListVo();
			resultListVo.setIpOrgServiceStatVos(resultList);
			resultListVo.setTotalCount(totalCount);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"조직서비스별 IP현황 목록"});
		}
		return resultListVo;
	}
	
	/** 조직별 서비스 유형 셋팅 **/
	@Transactional(readOnly = true)
	public List<CommonCodeVo> selectOrgSassignTypeCdList(TbIpAllocMstVo tbIpAllocMstVo) {
		List<CommonCodeVo> resultVo = null;
		try {
			if (tbIpAllocMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			resultVo = ipStatMgmtTxService.selectOrgSassignTypeCdList(tbIpAllocMstVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"조직기반 서비스목록"});
		}
		return resultVo;
	}
	
	/** 조직별 서비스 유형 셋팅 **/
	@Transactional(readOnly = true)
	public List<CommonCodeVo> selectOrgSassignTypeCdList2(TbIpAllocMstVo tbIpAllocMstVo) {
		List<CommonCodeVo> resultVo = null;
		try {
			if (tbIpAllocMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			resultVo = ipStatMgmtTxService.selectOrgSassignTypeCdList2(tbIpAllocMstVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"조직기반 서비스목록"});
		}
		return resultVo;
	}
	
	/*4.단위블록별 IP현황 메인 조회*/
	@Transactional(readOnly = true) 
	public IpSingleBlockStatListVo selectListSingleBlockIpStatMst(IpSingleBlockStatVo ipSingleBlockStatVo){
		IpSingleBlockStatListVo resultListVo = null; 
		if (ipSingleBlockStatVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {
			List<IpSingleBlockStatVo> resultList = ipStatMgmtTxService.selectListPageSingleBlockIpStatMst(ipSingleBlockStatVo);
			int totalCount = ipStatMgmtTxService.countListPageSingleBlockIpStatMst(ipSingleBlockStatVo);
			resultListVo = new IpSingleBlockStatListVo();
			resultListVo.setIpSingleBlockStatVos(resultList);
			resultListVo.setTotalCount(totalCount);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"단위블록별 IP현황 목록"});
		}
		return resultListVo;
	}
	
	/*4.단위블록별 IP현황 메인 조회 Excel*/
	@Transactional(readOnly = true)
	public IpSingleBlockStatListVo selectListSingleBlockIpStatMstExcel(IpSingleBlockStatVo ipSingleBlockStatVo){
		IpSingleBlockStatListVo resultListVo = null;
		
		if (ipSingleBlockStatVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {
			
			int totalCount = ipStatMgmtTxService.countListPageSingleBlockIpStatMst(ipSingleBlockStatVo);
			if (totalCount > CommonCodeUtil.MAX_EXCEL_ROW_COUNT) {
				throw new ServiceException("CMN.INFO.00051");
			}
			List<IpSingleBlockStatVo> resultList = null;
			if (totalCount > 0) {
				ipSingleBlockStatVo.setFirstIndex(1);
				ipSingleBlockStatVo.setLastIndex(totalCount);
				resultList = ipStatMgmtTxService.selectListPageSingleBlockIpStatMst(ipSingleBlockStatVo);	
			}
			
			resultListVo = new IpSingleBlockStatListVo();
			resultListVo.setIpSingleBlockStatVos(resultList);
			resultListVo.setTotalCount(totalCount);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"단위블록별 IP현황 목록 Excel"});
		}
		return resultListVo;
	}
	
	/**
	 * IP 통계 > 서비스 유형 조회
	 * @param ipIntgrmSvcStatVo
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<String> selectIntgrmSassignTypeCdList(IpIntgrmSvcStatVo ipIntgrmSvcStatVo) {
		List<String> resultList = null;
		try {
			if (ipIntgrmSvcStatVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			resultList = ipStatMgmtTxService.selectIntgrmSassignTypeCdList(ipIntgrmSvcStatVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"조직기반 서비스목록"});
		}
		return resultList;
	}
	
	/**
	 * IP 통계 > IP주소 라우팅 비교/점검 통계 현황 목록
	 * @param ipIntgrmSvcStatVo
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<Map<String,String>> selectListIntgrmSvcStatMst(IpIntgrmSvcStatVo ipIntgrmSvcStatVo){
		List<Map<String,String>>  resultList = null;
		if (ipIntgrmSvcStatVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {
			
			resultList = ipStatMgmtTxService.selectListPageIntgrmSvcStatVo(ipIntgrmSvcStatVo);
		
		} catch (ServiceException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("CMN.HIGH.00023", new String[]{"IP주소 라우팅 비교/점검 통계 현황 목록"});
		}
		return resultList;
	}
	
	/**
	 * IP 통계 > IP 조직서비스별 통계 현황 목록
	 * @param ipIntgrmSvcStatVo
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<Map<String,String>> selectListOrgSvcStatMst(IpIntgrmSvcStatVo ipIntgrmSvcStatVo){
		List<Map<String,String>>  resultList = null;
		if (ipIntgrmSvcStatVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {
			
			resultList = ipStatMgmtTxService.selectListPageOrgSvcStatVo(ipIntgrmSvcStatVo);
		
		} catch (ServiceException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("CMN.HIGH.00023", new String[]{"조직서비스별 IP현황 목록"});
		}
		return resultList;
	}

	/**
	 * IP 통계 > IP 서비스별 통계 현황 목록
	 * @param ipIntgrmSvcStatVo
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<Map<String, String>> selectListSvcStatMst(IpIntgrmSvcStatVo ipIntgrmSvcStatVo) {
		List<Map<String,String>>  resultList = null;
		if (ipIntgrmSvcStatVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {
			
			resultList = ipStatMgmtTxService.selectListPageSvcStatVo(ipIntgrmSvcStatVo);
		
		} catch (ServiceException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("CMN.HIGH.00023", new String[]{"서비스별 IP현황 목록"});
		}
		return resultList;
	}

	/**
	 * IP 통계 > IP 블록별 통계 현황 목록
	 * @param ipIntgrmSvcStatVo
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<Map<String, String>> selectListBlockSizeStatMst(IpIntgrmSvcStatVo ipIntgrmSvcStatVo) {
		List<Map<String,String>>  resultList = null;
		if (ipIntgrmSvcStatVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {
			
			resultList = ipStatMgmtTxService.selectListBlockSizeStatMst(ipIntgrmSvcStatVo);
		
		} catch (ServiceException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("CMN.HIGH.00023", new String[]{"서비스별 IP현황 목록"});
		}
		return resultList;
	}

	public List<Map<String, String>> selectListSvcLineList() {
		List<Map<String,String>>  resultList = null;
		try {
			resultList = ipStatMgmtTxService.selectListSvcLineList();
		} catch (ServiceException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("CMN.HIGH.00023", new String[]{"서비스별 IP현황 목록"});
		}
		return resultList;
	}
	
}
