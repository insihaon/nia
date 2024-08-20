package com.kt.ipms.legacy.ticketmgmt.dhcpmgmt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kt.framework.exception.ServiceException;
import com.kt.ipms.legacy.cmn.util.CommonCodeUtil;
import com.kt.ipms.legacy.cmn.vo.CommonCodeVo;
import com.kt.ipms.legacy.opermgmt.limitmgmt.vo.TbAuditDhcpBasVo;
import com.kt.ipms.legacy.ticketmgmt.dhcpmgmt.vo.TbAuditDhcpMstListVo;
import com.kt.ipms.legacy.ticketmgmt.dhcpmgmt.vo.TbAuditDhcpMstVo;






@Component
public class TbAuditDhcpMstService {
	
	@Autowired
	private TbAuditDhcpMstTxService tbAuditDhcpMstTxService;
	
	//2014. 10. 24 dhcp 관리 리스트조회
	public TbAuditDhcpMstListVo selectListTbAuditDhcpMstVo(TbAuditDhcpMstVo searchVo)   {
		
		TbAuditDhcpMstListVo resultListVo = new TbAuditDhcpMstListVo();
	    int totalCount;
	    
		try{
			
			totalCount = tbAuditDhcpMstTxService.countListPageTbAuditDhcpMstVo(searchVo);
					
			if(totalCount != 0){
				//List<TbAuditDhcpMstVo> resultList = tbAuditDhcpMstTxService.selectListTbAuditDhcpMstVo(searchVo);
				List<TbAuditDhcpMstVo> resultList = tbAuditDhcpMstTxService.selectListPageTbAuditDhcpMstVo(searchVo);
				
				resultListVo.setTotalCount(totalCount);
				resultListVo.setTbAuditDhcpMstVos(resultList);
			}
 		} catch (ServiceException e) {
 			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"신인증DHCP관리"});
		}
		return resultListVo;
	}
	
	/* dhcp 관리 리스트조회 Excel*/
	public TbAuditDhcpMstListVo selectListTbAuditDhcpMstVoExcel(TbAuditDhcpMstVo searchVo)   {
		
		TbAuditDhcpMstListVo resultListVo = new TbAuditDhcpMstListVo();
	    int totalCount;
	    
		try{
			
			totalCount = tbAuditDhcpMstTxService.countListPageTbAuditDhcpMstVo(searchVo);
			if (totalCount > CommonCodeUtil.MAX_EXCEL_ROW_COUNT) {
				throw new ServiceException("CMN.INFO.00051");
			}		
			if(totalCount != 0){
				searchVo.setFirstIndex(1);
				searchVo.setLastIndex(totalCount);
				
				List<TbAuditDhcpMstVo> resultList = tbAuditDhcpMstTxService.selectListPageTbAuditDhcpMstVo(searchVo);
				
				resultListVo.setTotalCount(totalCount);
				resultListVo.setTbAuditDhcpMstVos(resultList);
			}
 		} catch (ServiceException e) {
 			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"신인증DHCP관리 엑셀저장"});
		}
		return resultListVo;
	}
	
	//2014. 10. 24 dhcp 관리 팝업조회
	public TbAuditDhcpMstVo selectTbAuditDhcpMstVo(TbAuditDhcpMstVo searchVo)   {

		TbAuditDhcpMstVo resultVo = null;
	    
		try{
		
			resultVo = tbAuditDhcpMstTxService.selectTbAuditDhcpMstVo(searchVo);
		 
 		} catch (ServiceException e) {
 			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"신인증DHCP관리팝업"});
		}
		return resultVo;
	}
	
	
	//2014 10, 25 dhcp 수동관리 임계치 업데이트
	public void updateTbAuditDhcpMstVo(TbAuditDhcpMstVo searchVo){
		    
		try{
			
			String bfalgYn= searchVo.getBfalg();
			
			if(bfalgYn.equals("Y")){
				tbAuditDhcpMstTxService.updateTbAuditDhcpMstBflagY(searchVo);
			}else{
				tbAuditDhcpMstTxService.updateTbAuditDhcpMstVo(searchVo);
			}
			
	 	} catch (ServiceException e) {
	 		throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00020", new String[]{"신인증DHCP관리팝업"});
		}
	}
	
	//2014. 11. 12 dhcp bas 임계치 조회 
	public TbAuditDhcpBasVo selectTbAuditDhcpBasVo(TbAuditDhcpBasVo searchVo)   {

		TbAuditDhcpBasVo resultVo = null;
	    
		try{
		
			resultVo = tbAuditDhcpMstTxService.selectTbAuditDhcpBasVo(searchVo);
		 
 		} catch (ServiceException e) {
 			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"신인증DHCP"});
		}
		return resultVo;
	}

	public List<CommonCodeVo> selectOrderSofficeList(TbAuditDhcpMstVo searchVo){
		List<CommonCodeVo> resultListVo = null;
		try {
			if (searchVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			
			resultListVo = tbAuditDhcpMstTxService.selectOrderSofficeList(searchVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"수용국 목록"});
		}
		return resultListVo;
	}
	
	
	public List<TbAuditDhcpMstVo> selectSetOrderOfficeList(TbAuditDhcpMstVo searchVo){
		List<TbAuditDhcpMstVo> resultListVo = null;
		try {
			if (searchVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			
			resultListVo = tbAuditDhcpMstTxService.selectSetOrderOfficeList(searchVo);
			
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"수용국 목록"});
		}
		return resultListVo;
	}
	
	
	
	
	


}
