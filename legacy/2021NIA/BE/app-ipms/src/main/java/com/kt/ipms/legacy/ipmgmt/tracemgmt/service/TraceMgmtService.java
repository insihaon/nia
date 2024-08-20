package com.kt.ipms.legacy.ipmgmt.tracemgmt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.kt.framework.exception.ServiceException;
import com.kt.framework.utils.StringUtils;
import com.kt.ipms.legacy.ipmgmt.tracemgmt.vo.TbTraceIPListVo;
import com.kt.ipms.legacy.ipmgmt.tracemgmt.vo.TbTraceIPVo;
import com.kt.ipms.legacy.ipmgmt.tracemgmt.vo.TbTraceIpAssignMstVo;
import com.kt.ipms.legacy.ipmgmt.tracemgmt.vo.TbTraceIpHostMstVo;


@Component
@Transactional
public class TraceMgmtService {
	
	@Autowired
	private TraceMgmtTxService traceMgmtTxService;
	
	/**
	 * @Method	:	excuteTracert
	 * @Date	:	2014. 12. 7.
	 * @Author	:	neoargon
	 * @DESC	:	Traceroute 실행 요청
	 * @변경이력	:
	 * @param inputVo
	 * @return TbTraceIPVo
	 */
	@Transactional(readOnly = true)
	public TbTraceIPListVo excuteTraceRoute(TbTraceIPVo inputVo){
		String sreturn="";
		String sipVersionTypeCd="";
		String sCmdReq =""; 
		
		TbTraceIPListVo parseListVo = null;
		TbTraceIPListVo resultListVo = null;
		
		try{
			if(StringUtils.hasText(inputVo.getSipAddress()) && StringUtils.hasText(inputVo.getSipVersionTypeCd())){
				sipVersionTypeCd = inputVo.getSipVersionTypeCd();
				sCmdReq = traceMgmtTxService.getMakeTraceCmdQuery(inputVo);
			} else {
				throw new ServiceException("LNK.INFO.00002"); // 필수 값 부재 에러
			}
			
			sreturn = traceMgmtTxService.excuteTraceRoute(sCmdReq);
			
//			if(!StringUtils.hasText(sreturn) || sreturn == ""){ //Codeeyes-Urgent-객체 비교시 == , != 사용제한
			if(!StringUtils.hasText(sreturn) || sreturn.equals("")){
				throw new ServiceException("LNK.HIGH.00032", new String[]{"Trace Route"});
			} else {
				
				parseListVo = traceMgmtTxService.parseTraceRouteResult(sipVersionTypeCd, sreturn);
				if (parseListVo == null || parseListVo.getTbTraceIPVos() == null || parseListVo.getTbTraceIPVos().size() == 0) {
					throw new ServiceException("LNK.HIGH.00032", new String[]{"Trace Route"});
				}
				resultListVo = traceMgmtTxService.selectHostInfo(parseListVo);
			}
			
		} catch(ServiceException e){
			throw e;
		} catch (Exception e) {
			throw new ServiceException("LNK.HIGH.00032", new String[]{"Trace Route"});
		} 	
		return resultListVo;
	}	
	
	/**
	 * @Method	:	selectHostDetailInfo
	 * @Date	:	2014. 12. 7.
	 * @Author	:	neoargon
	 * @DESC	:	팝업 화면 host 상세 정보 조회
	 * @변경이력	:
	 * @param inputVo
	 * @return TbTraceIpHostMstListVo
	 */
	@Transactional(readOnly = true)
	public TbTraceIpHostMstVo selectHostDetailInfo(TbTraceIPVo inputVo){
		
		TbTraceIpHostMstVo returnVo =null;
		TbTraceIpHostMstVo searchVo = new TbTraceIpHostMstVo();
		
		try{		
			searchVo.setNipHostMstSeq(inputVo.getNipHostMstSeq());			
			returnVo = traceMgmtTxService.selectHostDetailInfo(searchVo);
		} catch(ServiceException e){
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"Trace Route 호스트정보"}); 
		}
		
		return returnVo;		
	}
	
	/**
	 * @Method	:	selectTraceDetailAssignInfo
	 * @Date	:	2014. 12. 7.
	 * @Author	:	neoargon
	 * @DESC	:	팝업 화면 할당 상세 정보 조회
	 * @변경이력	:
	 * @param inputVo
	 * @return TbTraceIpAssignMstListVo
	 */
	public TbTraceIpAssignMstVo selectAssignDetailInfo(TbTraceIPVo inputVo){
		
		TbTraceIpAssignMstVo returnVo = null;
		TbTraceIpAssignMstVo searchVo = new TbTraceIpAssignMstVo();
		
		try{		
			searchVo.setNipHostMstSeq(inputVo.getNipHostMstSeq());			
			returnVo = traceMgmtTxService.selectAssignDetailInfo(searchVo);
		} catch(ServiceException e){
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"Trace Route 할당정보"});
		}
		return returnVo;
		
	}
	

}
