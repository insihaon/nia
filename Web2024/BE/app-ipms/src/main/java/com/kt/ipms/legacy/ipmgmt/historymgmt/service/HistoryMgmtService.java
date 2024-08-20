package com.kt.ipms.legacy.ipmgmt.historymgmt.service;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kt.framework.exception.ServiceException;
import com.kt.ipms.legacy.cmn.util.CommonCodeUtil;
import com.kt.ipms.legacy.cmn.vo.CommonCodeVo;
import com.kt.ipms.legacy.ipmgmt.allocmgmt.service.AllocMgmtTxService;
import com.kt.ipms.legacy.ipmgmt.allocmgmt.vo.TbIpAllocMstVo;
import com.kt.ipms.legacy.ipmgmt.historymgmt.vo.IpHistoryMstListVo;
import com.kt.ipms.legacy.ipmgmt.historymgmt.vo.IpHistoryMstVo;


@Component
@Transactional
public class HistoryMgmtService {
	
	
	@Autowired
	private AllocMgmtTxService allocMgmtTxService;
	
	@Autowired
	private HistoryMgmtTxService historyMgmtTxService;
	
	
	/**
	 * 조직별 서비스 유형 목록 조회
	 * @param ipAllocOperMstVo
	 * @return List<CommonCodeVo>
	 * @throws Exception
	 */
	@Transactional(readOnly = true)
	public List<CommonCodeVo> selectOrgSassignTypeCdList(TbIpAllocMstVo tbIpAllocMstVo){
		List<CommonCodeVo> resultListVo = null;
		try {
			if (tbIpAllocMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			
			resultListVo = allocMgmtTxService.selectOrgSassignTypeCdList(tbIpAllocMstVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"조직기반 서비스목록"});
		}
		return resultListVo;
	}
	
	/**
	 * 이력관리 이력 목록 조회
	 * @param ipHistoryMstVo
	 * @return
	 */
	@Transactional(readOnly = true)
	public IpHistoryMstListVo selectListIpHistMst(IpHistoryMstVo ipHistoryMstVo){
		IpHistoryMstListVo resultListVo = null;
		if (ipHistoryMstVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {
			
			int yyyy = 0;
			
			//Codeeyes-Urgent-String 추가 시 String Buffer 사용
			StringBuffer sb = new StringBuffer(); 
			sb.append("TB_IP_HIST_MST_");
			
			if(ipHistoryMstVo.getYyyy() != 0) {
				yyyy = ipHistoryMstVo.getYyyy();
			} else {
				yyyy = Calendar.getInstance().get(Calendar.YEAR);
				ipHistoryMstVo.setYyyy(yyyy);
			}
			
			sb.append(yyyy);
			String tableNm = sb.toString();
			
			ipHistoryMstVo.setTableNm(tableNm);
			
			List<IpHistoryMstVo> resultList = historyMgmtTxService.selectListPageIpHistoryMst(ipHistoryMstVo);
			int totalCount = historyMgmtTxService.countListPageIpHistoryMst(ipHistoryMstVo);
			resultListVo = new IpHistoryMstListVo();
			resultListVo.setIpHistoryMstVos(resultList);
			resultListVo.setTotalCount(totalCount);
		} catch (ServiceException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("CMN.HIGH.00023", new String[]{"IP이력관리목록"});
		}
		return resultListVo;
	}
	
	/**
	 * 이력관리 목록 엑셀 저장
	 * @param ipHistoryMstVo
	 * @return
	 */
	@Transactional(readOnly = true)
	public IpHistoryMstListVo selectListIpHistMstExcel(IpHistoryMstVo ipHistoryMstVo){
		IpHistoryMstListVo resultListVo = null;
		if (ipHistoryMstVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {
			
			int yyyy = 0;
			
			//Codeeyes-Urgent-String 추가 시 String Buffer 사용
			StringBuffer sb = new StringBuffer();
			sb.append("TB_IP_HIST_MST_");
			
			if(ipHistoryMstVo.getYyyy() != 0) {
				yyyy = ipHistoryMstVo.getYyyy();
			} else {
				yyyy = Calendar.getInstance().get(Calendar.YEAR);
				ipHistoryMstVo.setYyyy(yyyy);
			}
			
			sb.append(yyyy);
			
			String tableNm = sb.toString();
			ipHistoryMstVo.setTableNm(tableNm);
			
			int totalCount = historyMgmtTxService.countListPageIpHistoryMst(ipHistoryMstVo);
			if (totalCount > CommonCodeUtil.MAX_EXCEL_ROW_COUNT) {
				throw new ServiceException("CMN.INFO.00051");
			}
			List<IpHistoryMstVo> resultList = null;
			if (totalCount > 0) {
				ipHistoryMstVo.setFirstIndex(1);
				ipHistoryMstVo.setLastIndex(totalCount);
				resultList = historyMgmtTxService.selectListPageIpHistoryMst(ipHistoryMstVo);	
			}
			resultListVo = new IpHistoryMstListVo();
			resultListVo.setIpHistoryMstVos(resultList);
			resultListVo.setTotalCount(totalCount);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"IP이력관리목록 엑셀저장"});
		}
		return resultListVo;
	}
	
	/**
	 * 이력관리 이력 등록
	 * @param tbHistoryMstVo
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public void insertTbIpHistoryMstVo(IpHistoryMstVo ipHistoryMstVo) {
		
		int yyyy = Calendar.getInstance().get(Calendar.YEAR);
		ipHistoryMstVo.setYyyy(yyyy);
		
		//Codeeyes-Urgent-String 추가 시 String Buffer 사용
		StringBuffer sb = new StringBuffer();
		sb.append("TB_IP_HIST_MST_");
		sb.append(yyyy);
		String tableNm = sb.toString();
		
		ipHistoryMstVo.setTableNm(tableNm);
		
		historyMgmtTxService.insertTbIpHistoryMstVo(ipHistoryMstVo);
	}
	
	/**
	 * 이력관리 이력 목록 조회
	 * @param ipHistoryMstVo
	 * @return
	 */
	@Transactional(readOnly = true)
	public IpHistoryMstListVo selectTableYear(IpHistoryMstVo ipHistoryMstVo){
		IpHistoryMstListVo resultListVo = null;
		if (ipHistoryMstVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {
			List<IpHistoryMstVo> resultList = historyMgmtTxService.selectTableYear(ipHistoryMstVo);
			
			resultListVo = new IpHistoryMstListVo();
			resultListVo.setIpHistoryMstVos(resultList);
			
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"조회년도"});
		}
		return resultListVo;
	}

	@Transactional(readOnly = true)
	public List<IpHistoryMstVo> selectAllocIpInfoList(IpHistoryMstVo ipHistoryMstVo) {
		List<IpHistoryMstVo> resultListVo = null;
		try {
			if (ipHistoryMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			
			resultListVo = historyMgmtTxService.selectAllocIpInfoList(ipHistoryMstVo);
			
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"IP이력"});
		}
		return resultListVo;
	}
	
	@Transactional(readOnly = true)
	public IpHistoryMstVo selectAllocIpInfo(IpHistoryMstVo ipHistoryMstVo) {
		IpHistoryMstVo resultVo = null;
		try {
			if (ipHistoryMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			
			resultVo = historyMgmtTxService.selectAllocIpInfo(ipHistoryMstVo);
			
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"IP이력"});
		}
		return resultVo;
	}
	
	@Transactional(readOnly = true)
	public IpHistoryMstVo selectBlockIpInfo(IpHistoryMstVo ipHistoryMstVo) {
		IpHistoryMstVo resultVo = null;
		try {
			if (ipHistoryMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			resultVo = historyMgmtTxService.selectBlockIpInfo(ipHistoryMstVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"IP이력"});
		}
		return resultVo;
	}
	
	@Transactional(readOnly = true)
	public IpHistoryMstVo selectMainIpInfoMst(IpHistoryMstVo ipHistoryMstVo){
		IpHistoryMstVo resultVo = null;
		if (ipHistoryMstVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {
			
			//Codeeyes-Urgent-String 추가 시 String Buffer 사용
			StringBuffer sb = new StringBuffer();
			sb.append("TB_IP_HIST_MST_");
			sb.append(ipHistoryMstVo.getYyyy());
			String tableNm = sb.toString();
			
			ipHistoryMstVo.setTableNm(tableNm);
			
			resultVo = historyMgmtTxService.selectMainIpInfoMst(ipHistoryMstVo);
	
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00000");
		}
		return resultVo;
	}
	
	
}
