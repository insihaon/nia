package com.kt.ipms.legacy.ipmgmt.hostmgmt.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kt.framework.exception.ServiceException;
import com.kt.ipms.legacy.cmn.service.IpCommonService;
import com.kt.ipms.legacy.cmn.util.CommonCodeUtil;
import com.kt.ipms.legacy.cmn.vo.CommonCodeVo;
import com.kt.ipms.legacy.ipmgmt.hostmgmt.vo.TbIpHostMstListVo;
import com.kt.ipms.legacy.ipmgmt.hostmgmt.vo.TbIpHostMstVo;

@Component
@Transactional
public class HostMgmtService {

	@Lazy @Autowired
	private HostMgmtTxService hostMgmtTxService;

	@Lazy @Autowired
	private IpCommonService ipCommonService;

	@Transactional(readOnly = true)	
	public TbIpHostMstListVo selectTbIpHostInfoVo(TbIpHostMstVo tbIpHostMstVo){
		TbIpHostMstListVo resultListVo = null;
		try{
			if(tbIpHostMstVo == null){
				throw new ServiceException("CMN.HIGH.00001");
			}
			List<TbIpHostMstVo>	resultVos = hostMgmtTxService.selectTbIpHostInfoVo(tbIpHostMstVo);
			if (resultVos !=null)
			{
				resultListVo = new TbIpHostMstListVo();
				int totalCnt = resultVos.size();
				resultListVo.setTbIpHostMstVos(resultVos);
				resultListVo.setTotalCount(totalCnt);
				
			}
			else
			{				
				resultListVo = new TbIpHostMstListVo();
				resultListVo.setTotalCount(0);
			}
		} catch(ServiceException e){
			throw e;
		} catch(Exception e){
			throw new ServiceException("CMN.HIGH.00000");
		}
		return resultListVo;
	}
	
	@Transactional(readOnly = true)
	public TbIpHostMstListVo selectListIpHostMst(TbIpHostMstVo tbIpHostMstVo){
		TbIpHostMstListVo resultListVo = null;
		try {
			if (tbIpHostMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			List<TbIpHostMstVo> resultList = hostMgmtTxService.selectListPageIpHostMst(tbIpHostMstVo);
			int totalCount = hostMgmtTxService.countListPageIpHostMst(tbIpHostMstVo);
			resultListVo = new TbIpHostMstListVo();
			resultListVo.setTbIpHostMstVos(resultList);
			resultListVo.setTotalCount(totalCount);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"IP HOST 목록"});
		}
		
		return resultListVo;
	}
	
	@Transactional(readOnly = true)
	public TbIpHostMstListVo selectListIpHostMstExcel(TbIpHostMstVo tbIpHostMstVo){
		TbIpHostMstListVo resultListVo = null;
		try {
			if (tbIpHostMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			int totalCount = hostMgmtTxService.countListPageIpHostMst(tbIpHostMstVo);
			if (totalCount > CommonCodeUtil.MAX_EXCEL_ROW_COUNT) {
				throw new ServiceException("CMN.INFO.00051");
			}
			List<TbIpHostMstVo> resultList = null;
			if(totalCount > 0 ){
				tbIpHostMstVo.setFirstIndex(1);
				tbIpHostMstVo.setLastIndex(totalCount);
				resultList = hostMgmtTxService.selectListPageIpHostMst(tbIpHostMstVo);
			}			 
			resultListVo = new TbIpHostMstListVo();
			resultListVo.setTbIpHostMstVos(resultList);
			resultListVo.setTotalCount(totalCount);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"IP HOST 목록 엑셀"});
		}
		
		return resultListVo;
	}
	
	@Transactional(readOnly = true)
	public TbIpHostMstListVo selectListVirtualIpHostMst(TbIpHostMstVo tbIpHostMstVo){
		TbIpHostMstListVo resultListVo = null;
		try {
			if (tbIpHostMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			List<TbIpHostMstVo> resultList = hostMgmtTxService.selectListPageVirtualIpHostMstVo(tbIpHostMstVo);
			int totalCount = hostMgmtTxService.countListPageVirtualIpHostMstVo(tbIpHostMstVo);
			resultListVo = new TbIpHostMstListVo();
			resultListVo.setTbIpHostMstVos(resultList);
			resultListVo.setTotalCount(totalCount);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"IP HOST 목록"});
		}
		
		return resultListVo;
	}
	
	@Transactional(readOnly = true)
	public TbIpHostMstListVo selectListVirtualIpHostExcel(TbIpHostMstVo tbIpHostMstVo){
		TbIpHostMstListVo resultListVo = null;
		try {
			if (tbIpHostMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			int totalCount = hostMgmtTxService.countListPageVirtualIpHostMstVo(tbIpHostMstVo);
			if (totalCount > CommonCodeUtil.MAX_EXCEL_ROW_COUNT) {
				throw new ServiceException("CMN.INFO.00051");
			}
			List<TbIpHostMstVo> resultList = null;
			if(totalCount > 0 ){
				tbIpHostMstVo.setFirstIndex(1);
				tbIpHostMstVo.setLastIndex(totalCount);
				resultList = hostMgmtTxService.selectListPageVirtualIpHostMstVo(tbIpHostMstVo);
			}			 
			resultListVo = new TbIpHostMstListVo();
			resultListVo.setTbIpHostMstVos(resultList);
			resultListVo.setTotalCount(totalCount);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"가상 IP HOST 목록 엑셀"});
		}
		
		return resultListVo;
	}
	
	@Transactional(readOnly = true)
	public TbIpHostMstListVo selectListIpHostMstViaIpInfo(TbIpHostMstVo tbIpHostMstVo){
		TbIpHostMstListVo resultListVo = null;
		try {
			if (tbIpHostMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			List<TbIpHostMstVo> resultList = hostMgmtTxService.selectListIpHostMst(tbIpHostMstVo);
			int totalCount = hostMgmtTxService.countListPageIpHostMst(tbIpHostMstVo);
			resultListVo = new TbIpHostMstListVo();
			resultListVo.setTbIpHostMstVos(resultList);
			resultListVo.setTotalCount(totalCount);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"HOST 상세 정보"});
		}
		return resultListVo;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void updateListIpHostMst(TbIpHostMstListVo tbIpHostMstListVo){
		try {
			if (tbIpHostMstListVo == null || tbIpHostMstListVo.getTbIpHostMstVos() == null
				|| tbIpHostMstListVo.getTbIpHostMstVos().size() == 0) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			hostMgmtTxService.processUpdateListIpHostMst(tbIpHostMstListVo.getTbIpHostMstVos());
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00021", new String[]{"HOST 정보"});
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void insertIpHostMst(TbIpHostMstVo tbIpHostMstVo){
		try {	
			int checkCnt = 0;
			
			TbIpHostMstVo checkVo = new TbIpHostMstVo();
			checkVo.setSipBlock(tbIpHostMstVo.getSipBlock());
			checkCnt = hostMgmtTxService.countListPageIpHostMst(checkVo);
			if(checkCnt > 0){
				throw new ServiceException("APP.INFO.00046", new String[]{tbIpHostMstVo.getSipBlock()});
			}
			
			ipCommonService.setBaseIpBlockMstInfo(tbIpHostMstVo);			
			hostMgmtTxService.insertIpHostMst(tbIpHostMstVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00021", new String[]{"HOST 정보 등록"});
		}
	}
	
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteIpHostMst(TbIpHostMstVo tbIpHostMstVo){
		try {				
			hostMgmtTxService.deleteIpHostMst(tbIpHostMstVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00021", new String[]{"HOST 정보 삭제"});
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void updateIpHostMst(TbIpHostMstVo tbIpHostMstVo){
		try {				
			
			if(tbIpHostMstVo == null){
				throw new ServiceException("CMN.HIGH.00001");
			}			
			
			TbIpHostMstVo selectVo = new TbIpHostMstVo();
			selectVo.setPipHostInet(tbIpHostMstVo.getPipHostInet());
			List<TbIpHostMstVo> resultList = hostMgmtTxService.selectListIpHostMst(selectVo);
			
			if(resultList != null && resultList.size() != 0 ){
				int rCnt = resultList.size();
				for(int i=0; i< rCnt; i++){
					TbIpHostMstVo  tempVo = resultList.get(i);
					TbIpHostMstVo updateVo = new TbIpHostMstVo();
					
					updateVo.setNipHostMstSeq(tempVo.getNipHostMstSeq());
					updateVo.setScomment(tbIpHostMstVo.getScomment());
					
					if(tbIpHostMstVo.getSprorityYn().equals("Y")){
						
						if(tempVo.getNipHostMstSeq().equals(tbIpHostMstVo.getNipHostMstSeq())){
							updateVo.setSprorityYn("Y");
							updateVo.setSipHostNm(tbIpHostMstVo.getSipHostNm());
							updateVo.setSmodelname(tbIpHostMstVo.getSmodelname());
							updateVo.setSrssofficescode(tbIpHostMstVo.getSrssofficescode());
						}
						else
						{
							updateVo.setSprorityYn("N");
						}
					}else {
						if(tempVo.getNipHostMstSeq().equals(tbIpHostMstVo.getNipHostMstSeq())){
							updateVo.setSprorityYn("N");
							updateVo.setSipHostNm(tbIpHostMstVo.getSipHostNm());
							updateVo.setSmodelname(tbIpHostMstVo.getSmodelname());
							updateVo.setSrssofficescode(tbIpHostMstVo.getSrssofficescode());
						}
					}
					
					resultList.set(i, updateVo);
				}	
				hostMgmtTxService.processUpdateListIpHostMst(resultList);
			}		
			
			
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00021", new String[]{"HOST 정보 삭제"});
		}
	}
	
	/**
	 * 수용국 정보 조회(초기)
	 * @param tbIpAssignSubVo
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = true)
	public List<CommonCodeVo> selectLoadOfficeList(TbIpHostMstVo tbIpHostMstVo){
		List<CommonCodeVo> resultListVo = null;
		
		try {
			if (tbIpHostMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			
			resultListVo = hostMgmtTxService.selectLoadOfficeList(tbIpHostMstVo);

		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"수용국 목록"});
		}
		return resultListVo;
	}
	
	@Transactional(readOnly = true)
	public TbIpHostMstListVo selectOfficeList(TbIpHostMstVo tbIpHostMstVo){
		TbIpHostMstListVo resultListVo = null;
		try {
			if (tbIpHostMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			List<TbIpHostMstVo> resultList = hostMgmtTxService.selectOfficeList(tbIpHostMstVo);
			resultListVo = new TbIpHostMstListVo();
			resultListVo.setTbIpHostMstVos(resultList);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"수용국 목록"});
		}
		return resultListVo;
	}
	
	@Transactional(readOnly = true)
	public TbIpHostMstVo selectTbIpHostMstVo(TbIpHostMstVo tbIpHostMstVo){
		TbIpHostMstVo resultVo = null;
		try {
			if (tbIpHostMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			 resultVo = hostMgmtTxService.selectTbIpHostMstVo(tbIpHostMstVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"수용국 목록"});
		}
		return resultVo;
	}

}
