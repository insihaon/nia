package com.kt.ipms.legacy.ipmgmt.routmgmt.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kt.framework.exception.ServiceException;
import com.kt.ipms.legacy.cmn.service.ConfigPropertieService;
import com.kt.ipms.legacy.cmn.service.IpCommonService;
import com.kt.ipms.legacy.cmn.util.CommonCodeUtil;
import com.kt.ipms.legacy.cmn.util.PrintLogUtil;
import com.kt.ipms.legacy.ipmgmt.allocmgmt.service.AllocMgmtService;
import com.kt.ipms.legacy.ipmgmt.allocmgmt.vo.IpAllocMstComplexVo;
import com.kt.ipms.legacy.ipmgmt.allocmgmt.vo.IpAllocOperMstVo;
import com.kt.ipms.legacy.ipmgmt.assignmgmt.service.AssignMgmtService;
import com.kt.ipms.legacy.ipmgmt.assignmgmt.service.AssignMgmtTxService;
import com.kt.ipms.legacy.ipmgmt.assignmgmt.vo.TbIpAssignMstComplexVo;
import com.kt.ipms.legacy.ipmgmt.assignmgmt.vo.TbIpAssignMstListVo;
import com.kt.ipms.legacy.ipmgmt.assignmgmt.vo.TbIpAssignMstVo;
import com.kt.ipms.legacy.ipmgmt.routmgmt.vo.TbRoutChkMstListVo;
import com.kt.ipms.legacy.ipmgmt.routmgmt.vo.TbRoutChkMstVo;
import com.kt.ipms.legacy.linkmgmt.common.util.LinkUtil;
import com.kt.ipms.legacy.opermgmt.intgrmgmt.service.IntgrMgmtTxService;
import com.kt.ipms.legacy.opermgmt.intgrmgmt.vo.TbFcltCmdMstListVo;
import com.kt.ipms.legacy.opermgmt.intgrmgmt.vo.TbFcltCmdMstVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.adapter.OrgMgmtAdapterService;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlBasVo;

@Component
public class RoutMgmtService {

	@Lazy @Autowired
	private RoutMgmtTxService routMgmtTxService;

	@Lazy @Autowired
	private AssignMgmtTxService assignMgmtTxService;

	@Lazy @Autowired
	private IpCommonService ipCommonService;

	@Lazy @Autowired
	private AllocMgmtService allocMgmtService;

	@Lazy @Autowired
	private OrgMgmtAdapterService orgMgmtAdapterService;

	@Lazy @Autowired
	private ConfigPropertieService configPropertieService;

	@Lazy @Autowired
	private AssignMgmtService assignMgmtService;

	@Lazy @Autowired
	private IntgrMgmtTxService intgrMgmtTxService;

	@Autowired
	private LinkUtil linkUtil;
	
	
	/****************************************************************************************
	 * IP주소 라우팅 비교/점검
	 ****************************************************************************************/
	
	/**
	 * IP주소 라우팅 비교/점검 > 조회
	 * @param tbRoutChkMstVo
	 * @return
	 */
	@Transactional(readOnly = true)
	public TbRoutChkMstListVo selectListRoutChkMst(TbRoutChkMstVo tbRoutChkMstVo) {
		TbRoutChkMstListVo resultListVo = null;
		try {
			if (tbRoutChkMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			List<TbRoutChkMstVo> resultList = routMgmtTxService.selectListPageRoutChkMstVo(tbRoutChkMstVo);
			int totalCount = routMgmtTxService.countListPageRoutChkMstVo(tbRoutChkMstVo);
			int totalCount2 = routMgmtTxService.countListPageRoutChkMstVo2(tbRoutChkMstVo);
			int totalCount3 = routMgmtTxService.countListPageRoutChkMstVo3(tbRoutChkMstVo);
			
			resultListVo = new TbRoutChkMstListVo();
			resultListVo.setTbRoutChkMstVos(resultList);
			resultListVo.setTotalCount(totalCount);
			resultListVo.setTotalCount2(totalCount2);
			resultListVo.setTotalCount3(totalCount3);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("CMN.HIGH.00023", new String[]{"IP주소 라우팅 비교/점검 목록"});
		}
		return resultListVo;
	}
	
	/**
	 * IP주소 라우팅 비교/점검 > 엑셀 다운로드
	 * @param tbRoutChkMstVo
	 * @return
	 */
	public TbRoutChkMstListVo selectListRoutChkMstExcel (TbRoutChkMstVo tbRoutChkMstVo) {
		
		TbRoutChkMstListVo resultListVo = null;
		
		try {
			if (tbRoutChkMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			List<TbRoutChkMstVo> resultList = null; 
			int totalCount = routMgmtTxService.countListPageRoutChkMstVo(tbRoutChkMstVo);
			if (totalCount > CommonCodeUtil.MAX_EXCEL_ROW_COUNT) {
				throw new ServiceException("CMN.INFO.00051");
			}
			
			if(totalCount > 0){
				tbRoutChkMstVo.setFirstIndex(1);
				tbRoutChkMstVo.setLastIndex(totalCount);
				resultList = routMgmtTxService.selectListExcelRoutChkMstVo(tbRoutChkMstVo);
			}

			resultListVo = new TbRoutChkMstListVo();
			resultListVo.setTbRoutChkMstVos(resultList);
			resultListVo.setTotalCount(totalCount);
			
		} catch (ServiceException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("CMN.HIGH.00023", new String[]{"IP주소 라우팅 비교/점검 내역 엑셀저장"});
		}
		return resultListVo;
		
	}
	
	/**
	 * IP주소 라우팅 비교/점검 > IP블록 현행화
	 * @param tbRoutChkMstVo
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void insertListIpBlockMatchMst(TbRoutChkMstVo tbRoutChkMstVo) {
		
		try {
			if (tbRoutChkMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
	
			String sipPrefixGubun = tbRoutChkMstVo.getSipPrefixGubun();
			
			// 구분값에 따라 분할, 병합 Operation
			if("PIPMS".equals(sipPrefixGubun)) {
				
				insertListIpDivMst(tbRoutChkMstVo);  // 분할
				
			} else if("PROUTING".equals(sipPrefixGubun)) {
				
				insertListIpMergetMst(tbRoutChkMstVo);  // 병합 
				
			} else {
				throw new ServiceException("CMN.HIGH.00001");
			}
			
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00020", new String[]{"IP주소 라우팅 비교/점검"});
		}
		
	}
	
	
	/**
	 * 분할 Operation
	 * @param tbRoutChkMstVo
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void insertListIpDivMst(TbRoutChkMstVo tbRoutChkMstVo) {
		
		boolean isValid = false;
		boolean isCanecle = false;
		
		try {
			// 1. 선택건 기준 [라우팅_비교_결과_MST] 테이블에서 필요 정보 조회
			TbRoutChkMstVo searchVo = new TbRoutChkMstVo();
			searchVo.setScollectDtOrigin(tbRoutChkMstVo.getScollectDtOrigin());
			searchVo.setNlvlMstSeq(tbRoutChkMstVo.getNlvlMstSeq());
			searchVo.setNroutingChkMstSeq(tbRoutChkMstVo.getNroutingChkMstSeq());
			searchVo.setSipPrefixGubun(tbRoutChkMstVo.getSipPrefixGubun());
					
			List<TbRoutChkMstVo> resultList = routMgmtTxService.selectListTarget(searchVo);
			PrintLogUtil.printLogInfo("########### RoutingMgmtService insertListIpDivMst selectListTarget : " +  resultList.size());
			if(resultList.size() == 0) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			
			// 2. IP블록 상태가 할당, 할당예약일 경우 할당해지 후 IP블록 분할 진행
			TbRoutChkMstVo cancelVo = resultList.get(0);
			cancelVo.setScreateId(tbRoutChkMstVo.getScreateId());
			cancelVo.setSmodifyId(tbRoutChkMstVo.getScreateId());
			
			// IPMS IP 블록 상태가 할당, 할당예약 일 경우
			if (null != cancelVo.getNipAllocMstSeq()) {
				isCanecle = cancelIpBlock("PIPMS", cancelVo, resultList);
			} else {
				isCanecle = true;
			}
			
			if (isCanecle) {
				// 3. 분할을 위한 값 세팅
				TbIpAssignMstComplexVo tbIpAssignMstComplexVo = new TbIpAssignMstComplexVo();
				TbIpAssignMstVo srcIpAssignMstVo = new TbIpAssignMstVo();
				List<TbIpAssignMstVo> destIpAssignMstVos = new ArrayList<TbIpAssignMstVo>();
				
				srcIpAssignMstVo.setNipAssignMstSeq(resultList.get(0).getNipAssignMstSeq());
				srcIpAssignMstVo.setNlvlMstSeq(resultList.get(0).getNlvlMstSeq());
				srcIpAssignMstVo.setSipVersionTypeCd(resultList.get(0).getSipVersionTypeCd());
				srcIpAssignMstVo.setSassignLevelCd(resultList.get(0).getSassignLevelCd());
				srcIpAssignMstVo.setNbitmask(resultList.get(0).getNipmsIpBitmask());
				
				for(TbRoutChkMstVo vo : resultList) {
					TbIpAssignMstVo tbAssignMstVo = new TbIpAssignMstVo();
					PrintLogUtil.printLogInfo("########### vo.getPipPrefix() : " +  vo.getPipPrefix());
					tbAssignMstVo.setPipPrefix(vo.getPipPrefix());
					tbAssignMstVo.setNbitmask(vo.getNbitmask());
					tbAssignMstVo.setSipVersionTypeCd(vo.getSipVersionTypeCd());
					tbAssignMstVo.setScreateId(tbRoutChkMstVo.getScreateId());
					tbAssignMstVo.setSmodifyId(tbRoutChkMstVo.getScreateId());
					
					destIpAssignMstVos.add(tbAssignMstVo);
				}
				
				tbIpAssignMstComplexVo.setSrcIpAssignMstVo(srcIpAssignMstVo);
				tbIpAssignMstComplexVo.setDestIpAssignMstVos(destIpAssignMstVos);
				
				// 4. 분할 Validation Check
				isValid = checkValidation("PIPMS", srcIpAssignMstVo, destIpAssignMstVos);
				
				if(isValid) {
					// 5. 분할 Operation 진행						
					
					/** 기준 정보 DB 재조회 **/
					TbIpAssignMstVo paramVo = tbIpAssignMstComplexVo.getSrcIpAssignMstVo();
					TbIpAssignMstVo searchIpAssignMstVo = new TbIpAssignMstVo();
					searchIpAssignMstVo.setNipAssignMstSeq(paramVo.getNipAssignMstSeq());
					
					TbIpAssignMstVo orgVo = assignMgmtTxService.selectIpAssignMst(searchIpAssignMstVo);
					if (orgVo == null) {
						throw new ServiceException("APP.HIGH.00029", new String[]{"분할할 IP블록의"});
					} else {
						if (!orgVo.getNlvlMstSeq().equals(paramVo.getNlvlMstSeq())) {
							throw new ServiceException("APP.HIGH.00029", new String[]{"분할할 IP블록의"});
						} /*else if (!orgVo.getSassignLevelCd().equals(paramVo.getSassignLevelCd())) {
							throw new ServiceException("APP.HIGH.00029", new String[]{"분할할 IP블록의"});
						} */
					}
					
					for (TbIpAssignMstVo tbIpAssignMstVo : destIpAssignMstVos) {
						/** IP블럭 기준 정보 설정 **/
						ipCommonService.setBaseIpBlockMstInfo(tbIpAssignMstVo);
						tbIpAssignMstVo.setNfreeIpCnt(tbIpAssignMstVo.getNcnt());
						tbIpAssignMstVo.setNuseIpCnt(BigInteger.ZERO);
						
						/** IP 배정 정보 설정  **/
						tbIpAssignMstVo.setNipBlockMstSeq(orgVo.getNipBlockMstSeq());
						tbIpAssignMstVo.setSipCreateSeqCd(orgVo.getSipCreateSeqCd());
						tbIpAssignMstVo.setSipCreateTypeCd(orgVo.getSipCreateTypeCd());
						tbIpAssignMstVo.setSsvcLineTypeCd(orgVo.getSsvcLineTypeCd());

						tbIpAssignMstVo.setNlvlMstSeq(orgVo.getNlvlMstSeq());
						tbIpAssignMstVo.setSassignLevelCd(orgVo.getSassignLevelCd());
						tbIpAssignMstVo.setNipmsSvcSeq(orgVo.getNipmsSvcSeq());
						tbIpAssignMstVo.setSassignTypeCd(orgVo.getSassignTypeCd());
						tbIpAssignMstVo.setSipAllocExTypeCd(orgVo.getSipAllocExTypeCd());
					}
					
					tbIpAssignMstComplexVo.setScreateId(tbRoutChkMstVo.getScreateId());
					tbIpAssignMstComplexVo.setMenuType(tbRoutChkMstVo.getMenuType());
					assignMgmtTxService.processInsertDivAsgnIPMst(tbIpAssignMstComplexVo);
					
					// 6. DB 현행화 결과 상태 Update
					for(TbRoutChkMstVo vo : resultList) {
						
						TbRoutChkMstVo updateVo = new TbRoutChkMstVo();
						updateVo.setScollectDtOrigin(vo.getScollect_dt());
						updateVo.setNlvlMstSeq(vo.getNlvlMstSeq());
						updateVo.setNroutingChkMstSeq(vo.getNroutingChkMstSeq());
						updateVo.setSipPrefixGubun(tbRoutChkMstVo.getSipPrefixGubun());
						updateVo.setScreateId(tbRoutChkMstVo.getScreateId());
						updateVo.setSmodifyId(tbRoutChkMstVo.getScreateId());
						
						if(vo.getPipPrefix().equals(vo.getProutingIpPrefix())) {
							
							if(vo.getSroutingUseYn().equals("Y")) {
								updateVo.setSdbIntgrmRsltCd(CommonCodeUtil.INTGRM_DB_RESULT02);	//IP블록 현행화 성공	
							} else if(vo.getSroutingUseYn().equals("N")) {
								updateVo.setSdbIntgrmRsltCd(CommonCodeUtil.INTGRM_DB_RESULT04);	// DB 현행화 성공	
							} else {
								updateVo.setSdbIntgrmRsltCd(CommonCodeUtil.INTGRM_DB_RESULT01);	//IP블록 현행화 실패
							}
						} else {
							
							updateVo.setSdbIntgrmRsltCd(CommonCodeUtil.INTGRM_DB_RESULT01);	//IP블록 현행화 실패
						} 
						routMgmtTxService.updateRoutChkMstVo(updateVo);
					} 
					
				}
				
			}
			
		} catch (ServiceException e) {
			linkUtil.setSystemERR(e);
			throw e;
		} catch (Exception e) {
			linkUtil.setSystemERR(e);
			throw new ServiceException("CMN.HIGH.00020", new String[]{"IP주소 라우팅 비교/점검"});
		}
	}
	
	/**
	 * 병합 Operation
	 * @param tbRoutChkMstVo
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void insertListIpMergetMst(TbRoutChkMstVo tbRoutChkMstVo) {
		
		boolean isValid = false;
		boolean isCanecle = false;
		
		try {
			
			//  1. IP블록 상태가 할당, 할당예약일 경우 할당해지 후 IP블록 병합 진행
			TbRoutChkMstVo searchVo = new TbRoutChkMstVo();
			searchVo.setScollectDtOrigin(tbRoutChkMstVo.getScollectDtOrigin());
			searchVo.setNlvlMstSeq(tbRoutChkMstVo.getNlvlMstSeq());
			searchVo.setNroutingChkMstSeq(tbRoutChkMstVo.getNroutingChkMstSeq());
			searchVo.setSipPrefixGubun(tbRoutChkMstVo.getSipPrefixGubun());
			searchVo.setSassignLevelCd("IA0005");		
			List<TbRoutChkMstVo> cancelList = routMgmtTxService.selectListTarget(searchVo);
			
			if(cancelList.size() > 0) {
				for(TbRoutChkMstVo tmpVo : cancelList) {
					
					tmpVo.setScreateId(tbRoutChkMstVo.getScreateId());
					tmpVo.setSmodifyId(tbRoutChkMstVo.getScreateId());
					
					if (null != tmpVo.getNipAllocMstSeq()) {
						List<TbRoutChkMstVo> tmpListVo = new ArrayList<TbRoutChkMstVo>();
						tmpListVo.add(tmpVo);
						
						isCanecle = cancelIpBlock("PROUTING", tmpVo, tmpListVo); 					// PIPMS 분할, PROUTING 병합
					} 
				}
			} else {
				isCanecle = true;
			}
			
			// 2. 선택건 기준 [라우팅_비교_결과_MST] 테이블에서 필요 정보 조회
			searchVo = new TbRoutChkMstVo();
			searchVo.setScollectDtOrigin(tbRoutChkMstVo.getScollectDtOrigin());
			searchVo.setNlvlMstSeq(tbRoutChkMstVo.getNlvlMstSeq());
			searchVo.setNroutingChkMstSeq(tbRoutChkMstVo.getNroutingChkMstSeq());
			searchVo.setSipPrefixGubun(tbRoutChkMstVo.getSipPrefixGubun());
					
			List<TbRoutChkMstVo> resultList = routMgmtTxService.selectListTarget(searchVo);
			PrintLogUtil.printLogInfo("########### RoutingMgmtService insertListIpMergetMst selectListTarget : " +  resultList.size());
			if(resultList.size() == 0) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			
			if (isCanecle) {
				// 3. 병합을 위한 값 세팅
				TbIpAssignMstComplexVo tbIpAssignMstComplexVo = new TbIpAssignMstComplexVo();
				TbIpAssignMstVo srcIpAssignMstVo = new TbIpAssignMstVo();
				List<TbIpAssignMstVo> destIpAssignMstVos = new ArrayList<TbIpAssignMstVo>();
				
				srcIpAssignMstVo.setNipAssignMstSeq(resultList.get(0).getNipAssignMstSeq());
				srcIpAssignMstVo.setNlvlMstSeq(resultList.get(0).getNlvlMstSeq());
				srcIpAssignMstVo.setSipVersionTypeCd(resultList.get(0).getSipVersionTypeCd());
				srcIpAssignMstVo.setSassignLevelCd(resultList.get(0).getSassignLevelCd());
				srcIpAssignMstVo.setPipPrefix(resultList.get(0).getPipPrefix());
				srcIpAssignMstVo.setNbitmask(resultList.get(0).getNbitmask());
				srcIpAssignMstVo.setSassignTypeCd(resultList.get(0).getSassignTypeCd());
				srcIpAssignMstVo.setSsvcLineTypeCd(resultList.get(0).getSsvcLineTypeCd());
				srcIpAssignMstVo.setScreateId(tbRoutChkMstVo.getScreateId());
				srcIpAssignMstVo.setSmodifyId(tbRoutChkMstVo.getScreateId());
				
				for(TbRoutChkMstVo vo : resultList) {
					TbIpAssignMstVo tbAssignMstVo = new TbIpAssignMstVo();
				    
					tbAssignMstVo.setNipAssignMstSeq(vo.getNipAssignMstSeq());
					
					tbAssignMstVo.setNipBlockMstSeq(vo.getNipBlockMstSeq());
					tbAssignMstVo.setSassignLevelCd(vo.getSassignLevelCd());
					tbAssignMstVo.setSassignTypeCd(vo.getSassignTypeCd());
					tbAssignMstVo.setNlvlMstSeq(vo.getNlvlMstSeq());
					
					tbAssignMstVo.setScreateId(tbRoutChkMstVo.getScreateId());
					tbAssignMstVo.setSmodifyId(tbRoutChkMstVo.getScreateId());
					
					destIpAssignMstVos.add(tbAssignMstVo);
				}
				
				tbIpAssignMstComplexVo.setSrcIpAssignMstVo(srcIpAssignMstVo);			// 병합 ip	
				tbIpAssignMstComplexVo.setDestIpAssignMstVos(destIpAssignMstVos);	// 병합될 기존 ip
				
				// 4. 병합 Validation Check
				isValid = checkValidation("PROUTING", srcIpAssignMstVo, destIpAssignMstVos);
				
				if(isValid) {
					// 5. 병합 Operation 진행						
					List<TbIpAssignMstVo> destVos = tbIpAssignMstComplexVo.getDestIpAssignMstVos();
					
					TbIpAssignMstListVo searchListVo = new TbIpAssignMstListVo();
					searchListVo.setTbIpAssignMstVos(destVos);
					List<TbIpAssignMstVo> orgVos = assignMgmtTxService.selectListAsgnIPMstViaInMstSeq(searchListVo);
					if (orgVos == null || destVos.size() != orgVos.size()) {
						throw new ServiceException("APP.HIGH.00029", new String[]{"병합할 IP블록의"});
					}

					//배정블록 비교하기 (대상 블록들의 화면 vs 데이터 상태값 비교하기)
					TbIpAssignMstVo srcIpAssignMstVo2 = tbIpAssignMstComplexVo.getSrcIpAssignMstVo();
					for (TbIpAssignMstVo orgVo : orgVos) {
						if (orgVo.getNlvlMstSeq().compareTo(srcIpAssignMstVo2.getNlvlMstSeq()) != 0
							|| !orgVo.getSassignLevelCd().equals(srcIpAssignMstVo2.getSassignLevelCd())
							|| !orgVo.getSassignTypeCd().equals(srcIpAssignMstVo2.getSassignTypeCd())) {
							throw new ServiceException("APP.HIGH.00029", new String[]{"병합할 IP블록의"});
						}
					}
					ipCommonService.setBaseIpBlockMstInfo(srcIpAssignMstVo2);
					srcIpAssignMstVo2.setNfreeIpCnt(srcIpAssignMstVo2.getNcnt());
					srcIpAssignMstVo2.setNuseIpCnt(BigInteger.ZERO);
					
					srcIpAssignMstVo2.setNipBlockMstSeq(orgVos.get(0).getNipBlockMstSeq());
					srcIpAssignMstVo2.setSipCreateSeqCd(orgVos.get(0).getSipCreateSeqCd());
					srcIpAssignMstVo2.setSipCreateTypeCd(orgVos.get(0).getSipCreateTypeCd());
					
					srcIpAssignMstVo2.setNipmsSvcSeq(BigInteger.ZERO);
					srcIpAssignMstVo2.setSipAllocExTypeCd("AE0000");
					
					tbIpAssignMstComplexVo.setScreateId(tbRoutChkMstVo.getScreateId());
					tbIpAssignMstComplexVo.setMenuType(tbRoutChkMstVo.getMenuType());
					assignMgmtTxService.processInsertMrgAsgnIPMst(tbIpAssignMstComplexVo);
					
					// 6. DB 현행화 결과 상태 Update
					for(TbRoutChkMstVo vo : resultList) {
						
						TbRoutChkMstVo updateVo = new TbRoutChkMstVo();
						updateVo.setScollectDtOrigin(vo.getScollect_dt());
						updateVo.setNlvlMstSeq(vo.getNlvlMstSeq());
						updateVo.setNroutingChkMstSeq(vo.getNroutingChkMstSeq());
						updateVo.setSipPrefixGubun(tbRoutChkMstVo.getSipPrefixGubun());
						updateVo.setScreateId(tbRoutChkMstVo.getScreateId());
						updateVo.setSmodifyId(tbRoutChkMstVo.getScreateId());
						
						if(vo.getPipPrefix().equals(vo.getProutingIpPrefix())) {
							if(vo.getSroutingUseYn().equals("Y")) {
								updateVo.setSdbIntgrmRsltCd(CommonCodeUtil.INTGRM_DB_RESULT02);	//IP블록 현행화 성공	
							} else if(vo.getSroutingUseYn().equals("N")) {
								updateVo.setSdbIntgrmRsltCd(CommonCodeUtil.INTGRM_DB_RESULT04);	// DB 현행화 성공	
							} else {
								updateVo.setSdbIntgrmRsltCd(CommonCodeUtil.INTGRM_DB_RESULT01);	//IP블록 현행화 실패
							}
							
						} else {
							
							updateVo.setSdbIntgrmRsltCd(CommonCodeUtil.INTGRM_DB_RESULT01);	//IP블록 현행화 실패
						}
						
						routMgmtTxService.updateRoutChkMstVo(updateVo);
					}
					
				}
				
			}
			
			
		} catch (ServiceException e) {
			linkUtil.setSystemERR(e);
			throw e;
		} catch (Exception e) {			
			linkUtil.setSystemERR(e);
			throw new ServiceException("CMN.HIGH.00020", new String[]{"IP주소 라우팅 비교/점검"});
		}
	}
	

	/**
	 * 할당, 할당예약 IP 해지
	 * @param sipPrefixGubun
	 * @param tbRoutchkMstVo
	 * @param tbRoutChkMstVos
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean cancelIpBlock(String sipPrefixGubun, TbRoutChkMstVo tbRoutchkMstVo, List<TbRoutChkMstVo> tbRoutChkMstVos) {
		boolean result = false;
		try {
			// 구분값에 따라 분할, 병합 Operation
			if("PIPMS".equals(sipPrefixGubun)) {			// 분할
				
				IpAllocMstComplexVo ipAllocMstComplexVo = new IpAllocMstComplexVo();
				IpAllocOperMstVo srcIpAllocMstVo = new IpAllocOperMstVo();
				List<IpAllocOperMstVo> destIpAllocMstVos  = new ArrayList<IpAllocOperMstVo>();
				IpAllocOperMstVo destIpAllocMstVo = new IpAllocOperMstVo();
				
				srcIpAllocMstVo.setNipAssignMstSeq(tbRoutChkMstVos.get(0).getNipAssignMstSeq());
				srcIpAllocMstVo.setNipAllocMstSeq(tbRoutChkMstVos.get(0).getNipAllocMstSeq());
				srcIpAllocMstVo.setScreateId(tbRoutchkMstVo.getScreateId());
				srcIpAllocMstVo.setSmodifyId(tbRoutchkMstVo.getScreateId());
				
				destIpAllocMstVo.setNipAssignMstSeq(tbRoutChkMstVos.get(0).getNipAssignMstSeq());
				destIpAllocMstVo.setNipAllocMstSeq(tbRoutChkMstVos.get(0).getNipAllocMstSeq());
				destIpAllocMstVo.setNwhoisSeq(tbRoutChkMstVos.get(0).getNwhoisseq());
				destIpAllocMstVo.setScreateId(tbRoutchkMstVo.getScreateId());
				destIpAllocMstVo.setSmodifyId(tbRoutchkMstVo.getScreateId());
				destIpAllocMstVo.setsGubun("ROUTMGMT");
				
				destIpAllocMstVos.add(destIpAllocMstVo);
				
				ipAllocMstComplexVo.setSrcIpAllocMstVo(srcIpAllocMstVo);
				ipAllocMstComplexVo.setDestIpAllocMstVos(destIpAllocMstVos);
				ipAllocMstComplexVo.setScreateId(tbRoutchkMstVo.getScreateId());
				ipAllocMstComplexVo.setMenuType(tbRoutchkMstVo.getMenuType());
				
				allocMgmtService.deleteListAllocIPMst(ipAllocMstComplexVo);
				
			} else if("PROUTING".equals(sipPrefixGubun)) { // 병합
				
				for(TbRoutChkMstVo vo : tbRoutChkMstVos) {
				
					IpAllocMstComplexVo ipAllocMstComplexVo = new IpAllocMstComplexVo();
					IpAllocOperMstVo srcIpAllocMstVo = new IpAllocOperMstVo();
					List<IpAllocOperMstVo> destIpAllocMstVos  = new ArrayList<IpAllocOperMstVo>();
					IpAllocOperMstVo destIpAllocMstVo = new IpAllocOperMstVo();
					
					srcIpAllocMstVo.setNipAssignMstSeq(vo.getNipAssignMstSeq());
					srcIpAllocMstVo.setNipAllocMstSeq(vo.getNipAllocMstSeq());
					srcIpAllocMstVo.setScreateId(tbRoutchkMstVo.getScreateId());
					srcIpAllocMstVo.setSmodifyId(tbRoutchkMstVo.getScreateId());
					
					destIpAllocMstVo.setNipAssignMstSeq(vo.getNipAssignMstSeq());
					destIpAllocMstVo.setNipAllocMstSeq(vo.getNipAllocMstSeq());
					destIpAllocMstVo.setNwhoisSeq(vo.getNwhoisseq());
					destIpAllocMstVo.setScreateId(tbRoutchkMstVo.getScreateId());
					destIpAllocMstVo.setSmodifyId(tbRoutchkMstVo.getScreateId());
					
					destIpAllocMstVo.setsGubun("ROUTMGMT");
					destIpAllocMstVos.add(destIpAllocMstVo);
					
					ipAllocMstComplexVo.setSrcIpAllocMstVo(srcIpAllocMstVo);
					ipAllocMstComplexVo.setDestIpAllocMstVos(destIpAllocMstVos);
					ipAllocMstComplexVo.setScreateId(tbRoutchkMstVo.getScreateId());
					ipAllocMstComplexVo.setMenuType(tbRoutchkMstVo.getMenuType());
					
					allocMgmtService.deleteListAllocIPMst(ipAllocMstComplexVo);
				}   
				
			} else {
				throw new ServiceException("CMN.HIGH.00001");
			}
			
			result = true;
		} catch (ServiceException e) {
			linkUtil.setSystemERR(e);
			throw e;
		} catch (Exception e) {
			linkUtil.setSystemERR(e);
			throw new ServiceException("CMN.HIGH.00020", new String[]{"IP주소 라우팅 비교/점검"});
		}
		
		return result;
	}
	
	/**
	 * 분할, 병합 Validation Check
	 * @param gubun
	 * @param tbRoutChkMstVo
	 * @param destIpAssignMstVos
	 * @return
	 */
	public boolean checkValidation(String gubun, TbIpAssignMstVo tbIpAssignMstVo, List<TbIpAssignMstVo> destIpAssignMstVos) {
		
		boolean result = false;
		
		if(tbIpAssignMstVo == null || destIpAssignMstVos == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		
		int bitmask;
		int nsubnetmask;
		String sipVersionTypeCd;
		
		//////////////////////////////////////////////////////////////////////////////////////////////////////////// 분할
		if("PIPMS".equals(gubun)) {
			bitmask = tbIpAssignMstVo.getNbitmask();
			for(TbIpAssignMstVo vo : destIpAssignMstVos) {
				nsubnetmask = vo.getNbitmask();
				sipVersionTypeCd = vo.getSipVersionTypeCd();
				
				if(nsubnetmask <= bitmask) {
					throw new ServiceException("CMN.INFO.00054", new String[]{"SubNet이 BitMask 보다 작거나 같을 수 없습니다."});
				} else {
					
					if ((bitmask + 8) < nsubnetmask) {
						throw new ServiceException("CMN.INFO.00054", new String[]{"SubNet이 기준정보의 BitMask 보다 8Bit 이상 분할이 불가합니다."});	
//					} else if (sipVersionTypeCd == "CV0001") { //Codeeyes-Urgent-객체 비교시 == , != 사용제한
					} 
//					else if (sipVersionTypeCd.equals("CV0001")) {
//						if (nsubnetmask > 24) {
//							throw new ServiceException("CMN.INFO.00054", new String[]{"IPv4는 24Bit 이상 배정분할 할 수 없습니다."});
//						}
//					} else if (sipVersionTypeCd == "CV0002") { //Codeeyes-Urgent-객체 비교시 == , != 사용제한
//					} 
					else if (sipVersionTypeCd.equals("CV0002")) {
						if (nsubnetmask > 64) {
							throw new ServiceException("CMN.INFO.00054", new String[]{"IPv6는 64Bit 이상 배정분할 할 수 없습니다."});
						}
					}
				}
			}
 			
			result = true;
			
		//////////////////////////////////////////////////////////////////////////////////////////////////////////// 병합			
		} else if("PROUTING".equals(gubun)) {
			
			if( destIpAssignMstVos.size() == 0) {
				throw new ServiceException("CMN.INFO.00054", new String[]{"병합할 대상이 없습니다."});
			}
			
			if( destIpAssignMstVos.size() == 1) {
				throw new ServiceException("CMN.INFO.00054", new String[]{"병합할 대상은 최소 2개이상 선택해 주시기 바랍니다."});
			}			
			
			for(TbIpAssignMstVo vo : destIpAssignMstVos) {
				
				BigInteger nipBlockSeq = destIpAssignMstVos.get(0).getNipBlockMstSeq();
				String sassignLevelCd = destIpAssignMstVos.get(0).getSassignLevelCd();
				String sassignTypeCd = destIpAssignMstVos.get(0).getSassignTypeCd();
				BigInteger nlvlMstSeq = destIpAssignMstVos.get(0).getNlvlMstSeq();
				
				if (!nipBlockSeq.equals(vo.getNipBlockMstSeq())) {
					throw new ServiceException("CMN.INFO.00054", new String[]{"병합할 대상 정보들의 생성 유형이 동일하지 않습니다."});	
				} 
				if(!nlvlMstSeq.equals(vo.getNlvlMstSeq())) {
					throw new ServiceException("CMN.INFO.00054", new String[]{"병합할 대상 정보들의 계위 정보가 동일하지 않습니다."});	
				}
				if (!sassignLevelCd.equals(vo.getSassignLevelCd())) {
					throw new ServiceException("CMN.INFO.00054", new String[]{"병합할 대상 정보들의 배정 상태가 동일하지 않습니다."});
				}
				if (!sassignTypeCd.equals(vo.getSassignTypeCd())) {
					throw new ServiceException("CMN.INFO.00054", new String[]{"병합할 대상 정보들의 서비스가 동일하지 않습니다."});
				}
			}
			
			// 기존 ipms 시스템에서 체크하는 병합 validation check
			TbIpAssignMstListVo tbIpAssignMstListVo = new TbIpAssignMstListVo();
			tbIpAssignMstListVo.setTbIpAssignMstVos(destIpAssignMstVos);
			TbIpAssignMstComplexVo tbIpAssignMstComplexVo = assignMgmtService.validateMrgAsgnIPMst(tbIpAssignMstListVo);
 			if(tbIpAssignMstComplexVo == null) {
 				throw new ServiceException("APP.HIGH.00027");
 			}
			
			result = true;
		} 
		
		return result;
	}
	
	/**
	 * IP주소 라우팅 비교/점검 > 수정
	 * @param tbRoutChkMstVo
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void updateRoutChkMstVo(TbRoutChkMstVo tbRoutChkMstVo) {
		try {
			if (tbRoutChkMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			
			routMgmtTxService.updateRoutChkMstVo(tbRoutChkMstVo);
			
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00021", new String[]{"IP주소 라우팅 비교/점검 상태 수정"});
		}
	}
	
	/**
	 * IP주소 라우팅 비교/점검 > 실제 라우팅 DB 수집
	 * @param tbRoutChkMstVo
	 * @throws IOException 
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public Map<String,String> insertListRoutChkMst(TbRoutChkMstVo tbRoutChkMstVo) throws IOException {
		
		Map<String,String> result = new HashMap<String, String>();
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
		HttpURLConnection conn = null;
		OutputStreamWriter out = null;
		
		try {
			if (tbRoutChkMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			
			// nlvlBasSeq 조회
			TbLvlBasVo searchVo = new TbLvlBasVo();
			searchVo.setSsvcLineTypeCd(tbRoutChkMstVo.getSsvcLineTypeCd());
			searchVo.setSsvcGroupCd(tbRoutChkMstVo.getSsvcGroupCd());
			searchVo.setSsvcObjCd(tbRoutChkMstVo.getSsvcObjCd());
			
			TbLvlBasVo tbLvlBasVo = orgMgmtAdapterService.selectTbLvlBas(searchVo);
			BigInteger nlvlBasSeq = tbLvlBasVo.getNlvlBasSeq();
			BigInteger nlvlMstSeq = tbLvlBasVo.getNmaxSeq();
			String ssvcLineTypeCd = tbLvlBasVo.getSsvcLineTypeCd();
			String ssvcGroupCd = tbLvlBasVo.getSsvcGroupCd();
			String ssvcObjCd = tbLvlBasVo.getSsvcObjCd();
			
			
			// Make Json Data
			ObjectMapper oReq = new ObjectMapper();
			Map<String,String> mapReq = new HashMap<String,String>();
			String reqData = "";
			
			mapReq.put("nlvlBasSeq", nlvlBasSeq.toString());					// 계위 bas seq
			mapReq.put("nlvlMstSeq", nlvlMstSeq.toString());					// 계위 mat seq (= nmaxseq)
			mapReq.put("ssvcLineTypeCd", ssvcLineTypeCd.toString());		// 서비스망
			mapReq.put("ssvcGroupCd", ssvcGroupCd.toString());		// 그룹
			mapReq.put("ssvcObjCd", ssvcObjCd.toString());		// 노드
			mapReq.put("userId", tbRoutChkMstVo.getSmodifyId());
			
			reqData = oReq.writerWithDefaultPrettyPrinter().writeValueAsString(mapReq);
			
			// Request
			String requestUrl = configPropertieService.getString("IpmsCheck.connRoutingUrl");
			int timeout = configPropertieService.getInt("IpmsCheck.timeout");
			
			URL url = new URL(requestUrl);
			
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept", "applicaton/json");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setConnectTimeout(timeout);
			
			
			out = new OutputStreamWriter(conn.getOutputStream());
			out.write(reqData);
			out.flush();
			
			// Response
			int responseCode = conn.getResponseCode();
			
			if(responseCode == HttpURLConnection.HTTP_OK) {
				br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
				
				String line;
				while((line = br.readLine()) != null) {
					sb.append(line).append("\n");
				}
				br.close();
			} else{
				throw new ServiceException("CMN.INFO.00054", new String[]{"연결에 실패하였습니다."});	
			}

			String resData = sb.toString();
			ObjectMapper oRes = new ObjectMapper();
			Map<String,String> mapRes = oRes.readValue(resData, Map.class);
			
			if(mapRes.isEmpty()) {
				throw new ServiceException("CMN.HIGH.00021", new String[]{"라우팅 수집"});
			}
			
			result = mapRes;
			
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00021", new String[]{"라우팅 수집"});
		} finally {
	        try {
	            if (br != null) br.close();
	            if (out != null) out.close();
	            if (conn != null ) conn.disconnect();
	            
	            // 라우팅 연동 이력관리 수정
	            //TbRoutHistMstVo tbRoutHistMstVo = new TbRoutHistMstVo();
	            //intgrMgmtTxService.updateRoutHistMstVo(tbRoutHistMstVo);
	            
	        } catch (IOException e) {
	        	throw new ServiceException("CMN.HIGH.00021", new String[]{"라우팅 수집"});
	        }
	        
	    }
		
		return result;
	}
	
	/**
	 * IP주소 라우팅 비교/점검 > 조직별 장비별 명령어 조회
	 * @param tbRoutChkMstVo
	 * @return
	 */
	@Transactional(readOnly = true)
	public TbFcltCmdMstListVo selectListFcltOrgCmdMst(TbRoutChkMstVo tbRoutChkMstVo) {
		TbFcltCmdMstListVo resultListVo = null;
		try {
			if (tbRoutChkMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			List<TbFcltCmdMstVo> resultList = routMgmtTxService.selectListFcltOrgCmdMstVo(tbRoutChkMstVo);
			
			resultListVo = new TbFcltCmdMstListVo();
			resultListVo.setTbFcltCmdMstVos(resultList);
			
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"조직별장비별목록"});
		}
		return resultListVo;
	}
	
	/**
	 * IP주소 라우팅 비교/점검 > 예외 처리 팝업 > 예외 처리 
	 * @param tbRoutChkMstVo
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void insertListExcptMst(TbRoutChkMstVo tbRoutChkMstVo) {
		
		int chkSize = 0;
		List<String> chkListStr = new ArrayList<String>();
		
		try {
			if (tbRoutChkMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			
			// 1. 체크된건 값 구분
			chkListStr = tbRoutChkMstVo.getChkListStr();
			
			String scollectDt = "";							// 수집일자
			BigInteger nlvlMstSeq = null;					// 계위_MST_SEQ
			BigInteger nroutingChkMstSeq = null;		// 라우팅_비교_결과_MSTSEQ
			String sipPrefixGubun = "";						// 비교_구분(PIPMS/PROUTING)
			chkSize = chkListStr.size();
			
			if (chkSize == 0) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			
			for(String tmp : chkListStr) {		// 체크한 건
				
				TbRoutChkMstVo vo = new TbRoutChkMstVo();	

				if(tmp.contains("_")) {
					scollectDt = tmp.split("_")[0];
					nlvlMstSeq = new BigInteger(tmp.split("_")[1]);
					nroutingChkMstSeq = new BigInteger(tmp.split("_")[2]);
					sipPrefixGubun = tmp.split("_")[3];
				} 
				
				vo.setScollectDtOrigin(scollectDt);
				vo.setNlvlMstSeq(nlvlMstSeq);
				vo.setNroutingChkMstSeq(nroutingChkMstSeq);
				vo.setSipPrefixGubun(sipPrefixGubun);
				vo.setScreateId(tbRoutChkMstVo.getScreateId());
				vo.setSmodifyId(tbRoutChkMstVo.getSmodifyId());
				
				vo.setSexcpt_cd(tbRoutChkMstVo.getSexcpt_cd());
				vo.setSexcpt_rsn(tbRoutChkMstVo.getSexcpt_rsn());
				
				// 2. 예외 처리 (라우팅_비교_예외_MST)
				routMgmtTxService.insertRoutExcptMstVo(vo);

				// 3. 예외 처리 (라우팅_비교_점검_MST)
				vo.setSexcpt_yn("Y");
				routMgmtTxService.updateRoutChkExcptMstVo(vo);
			}
			
		} catch (ServiceException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("CMN.HIGH.00020", new String[]{"IP주소 라우팅 비교/점검 예외 처리"});
		}
	}
	
	/**
	 * IP주소 라우팅 비교/점검 > 예외 처리 팝업 > 예외 처리 해제
	 * @param tbRoutChkMstVo
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void insertListExcptCancelMst(TbRoutChkMstVo tbRoutChkMstVo) {
		
		int chkSize = 0;
		List<String> chkListStr = new ArrayList<String>();
		
		try {
			if (tbRoutChkMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			
			// 1. 체크된건 값 구분
			chkListStr = tbRoutChkMstVo.getChkListStr();
			
			String scollectDt = "";							// 수집일자
			BigInteger nlvlMstSeq = null;					// 계위_MST_SEQ
			BigInteger nroutingChkMstSeq = null;		// 라우팅_비교_결과_MSTSEQ
			String sipPrefixGubun = "";						// 비교_구분(PIPMS/PROUTING)
			chkSize = chkListStr.size();
			
			if (chkSize == 0) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			
			for(String tmp : chkListStr) {		// 체크한 건
				
				TbRoutChkMstVo vo = new TbRoutChkMstVo();	

				if(tmp.contains("_")) {
					scollectDt = tmp.split("_")[0];
					nlvlMstSeq = new BigInteger(tmp.split("_")[1]);
					nroutingChkMstSeq = new BigInteger(tmp.split("_")[2]);
					sipPrefixGubun = tmp.split("_")[3];
				} 
				
				vo.setScollectDtOrigin(scollectDt);
				vo.setNlvlMstSeq(nlvlMstSeq);
				vo.setNroutingChkMstSeq(nroutingChkMstSeq);
				vo.setSipPrefixGubun(sipPrefixGubun);
				vo.setScreateId(tbRoutChkMstVo.getScreateId());
				vo.setSmodifyId(tbRoutChkMstVo.getSmodifyId());
				
				vo.setSexcpt_cd(tbRoutChkMstVo.getSexcpt_cd());
				vo.setSexcpt_rsn(tbRoutChkMstVo.getSexcpt_rsn());
				
				// 2. 예외 처리 해제(라우팅_비교_예외_MST)
				routMgmtTxService.deleteRoutExcptMstVo(vo);

				// 3. 예외 처리 해제(라우팅_비교_점검_MST)
				vo.setSexcpt_yn("N");
				routMgmtTxService.updateRoutChkExcptMstVo(vo);
			}
			
		} catch (ServiceException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("CMN.HIGH.00020", new String[]{"IP주소 라우팅 비교/점검 예외 처리 해제"});
		}
	}
	
	/**
	 * IP주소 라우팅 비교/점검 > 예외 처리 관리 팝업 > 예외 처리 상세 조회
	 * @param tbRoutChkMstVo
	 * @return
	 */
	@Transactional(readOnly = true)
	public TbRoutChkMstListVo selectListPageRoutExcptMstVo(TbRoutChkMstVo tbRoutChkMstVo) {
		TbRoutChkMstListVo resultListVo = null;
		try {
			if (tbRoutChkMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			List<TbRoutChkMstVo> resultList = routMgmtTxService.selectListPageRoutExcptMstVo(tbRoutChkMstVo);
			
			resultListVo = new TbRoutChkMstListVo();
			resultListVo.setTbRoutChkMstVos(resultList);
			
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"예외 처리 상세 조회"});
		}
		return resultListVo;
	}
	
	/**
	 * IP주소 라우팅 비교/점검 > IP블록 현행화 전 서비스 조회 
	 * @param tbRoutChkMstVo
	 * @return
	 */
	@Transactional(readOnly = true)
	public TbRoutChkMstListVo selectListIpBlockServiceCheck(TbRoutChkMstVo tbRoutChkMstVo) {
		TbRoutChkMstListVo resultListVo = null;
		try {
			if (tbRoutChkMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			
			List<TbRoutChkMstVo> resultList = routMgmtTxService.selectListIpBlockServiceCheck(tbRoutChkMstVo);
			
			resultListVo = new TbRoutChkMstListVo();
			resultListVo.setTbRoutChkMstVos(resultList);
			
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"서비스 조회"});
		}
		return resultListVo;
	}
	
	/**
	 * IP주소 라우팅 비교/점검 > IP블록 현행화 전 서비스 조회 
	 * @param tbRoutChkMstVo
	 * @return
	 */
	@Transactional(readOnly = true)
	public TbRoutChkMstListVo selectIpBlockServiceCheckList(TbRoutChkMstVo tbRoutChkMstVo) {
		TbRoutChkMstListVo resultListVo = null;
		try {
			if (tbRoutChkMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}

			List<TbRoutChkMstVo> resultList = routMgmtTxService.selectIpBlockServiceCheckList(tbRoutChkMstVo);
			int totalCount = routMgmtTxService.countIpBlockServiceCheckList(tbRoutChkMstVo);
			resultListVo = new TbRoutChkMstListVo();
			resultListVo.setTbRoutChkMstVos(resultList);
			resultListVo.setTotalCount(totalCount);
			
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"서비스 조회"});
		}
		return resultListVo;
	}
	
	/**
	 * IP주소 라우팅 비교/점검 > IP블록 현행화 전 서비스 조회(IP범위 같을때) 
	 * @param tbRoutChkMstVo
	 * @return
	 */
	@Transactional(readOnly = true)
	public TbRoutChkMstListVo selectIpBlockServiceCheckList2(TbRoutChkMstVo tbRoutChkMstVo) {
		TbRoutChkMstListVo resultListVo = null;
		try {
			if (tbRoutChkMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}

			List<TbRoutChkMstVo> resultList = routMgmtTxService.selectIpBlockServiceCheckList2(tbRoutChkMstVo);
			int totalCount = routMgmtTxService.countIpBlockServiceCheckList2(tbRoutChkMstVo);
			resultListVo = new TbRoutChkMstListVo();
			resultListVo.setTbRoutChkMstVos(resultList);
			resultListVo.setTotalCount(totalCount);
			
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"서비스 조회"});
		}
		return resultListVo;
	}
	
	/**
	 * IP주소 라우팅 비교/점검 > IP블록 현행화 전 서비스 변경 
	 * @param tbRoutChkMstVo
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void updateServiceMst(TbRoutChkMstVo tbRoutChkMstVo) {
		
		boolean isCanecle = false;
		
		try {
			
			if (tbRoutChkMstVo == null ) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			
			if(null != tbRoutChkMstVo.getSpageType()) {
				// 미배정, 배정(미할당)
				PrintLogUtil.printLog(""); //Codeeyes-Urgent-빈 If문 사용 제한
			} else {
				List<TbRoutChkMstVo> cancelList = routMgmtTxService.selectIpBlockServiceCheckList(tbRoutChkMstVo);
				
				if (cancelList.size() == 0) {
					throw new ServiceException("CMN.HIGH.00001");
				}
				
				// 1. IPMS IP 블록 상태가 할당, 할당예약 일 경우 해지
				for(TbRoutChkMstVo tmpVo : cancelList) {
					
					tmpVo.setScreateId(tbRoutChkMstVo.getScreateId());
					tmpVo.setSmodifyId(tbRoutChkMstVo.getScreateId());
					
					if (null != tmpVo.getNipAllocMstSeq()) {
						List<TbRoutChkMstVo> tmpListVo = new ArrayList<TbRoutChkMstVo>();
						tmpListVo.add(tmpVo);
						String sipPrefixGubun = tmpVo.getSipPrefixGubun();
						tmpVo.setScreateId(tbRoutChkMstVo.getScreateId());
						tmpVo.setMenuType(tbRoutChkMstVo.getMenuType());
						cancelIpBlock(sipPrefixGubun, tmpVo, tmpListVo); 					// PIPMS 분할, PROUTING 병합
					} 
				}
			}
			
			
			List<TbRoutChkMstVo> resultList = null;
			// 2. 서비스 변경
			if(null != tbRoutChkMstVo.getSpageType()) {
				resultList = routMgmtTxService.selectIpBlockServiceCheckList2(tbRoutChkMstVo);
			} else {
				resultList = routMgmtTxService.selectIpBlockServiceCheckList(tbRoutChkMstVo);
			}
			
			if (resultList.size() == 0) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			
			List<TbIpAssignMstVo> destIpAssignMstVos = new ArrayList<TbIpAssignMstVo>();
			for(TbRoutChkMstVo vo : resultList) {
				
				TbIpAssignMstVo tbIpAssignMstVo = new TbIpAssignMstVo();
				tbIpAssignMstVo.setNipAssignMstSeq(vo.getNipAssignMstSeq());
				tbIpAssignMstVo.setSassignLevelCd("IA0004");		// 서비스배정(미할당)
				tbIpAssignMstVo.setPipPrefix(vo.getPipmsIpPrefix());
				tbIpAssignMstVo.setSipVersionTypeCd(vo.getSipVersionTypeCd());
				tbIpAssignMstVo.setScreateId(tbRoutChkMstVo.getScreateId());
				tbIpAssignMstVo.setSmodifyId(tbRoutChkMstVo.getSmodifyId());
				tbIpAssignMstVo.setSassignTypeCd(tbRoutChkMstVo.getSassignTypeCd());
				
				tbIpAssignMstVo.setTypeFlag("svcassign"); 		// 배정-서비스배정
				
				/*if(vo.getSassignLevelCd().equals("IA0004")) {
					tbIpAssignMstVo.setTypeFlag("svcassign"); 		// 배정-서비스배정
				} else {
					tbIpAssignMstVo.setTypeFlag("assign"); 		// 배정-일반배정				
				}*/
				
				
				tbIpAssignMstVo.setsGubun("ROUTMGMT");		// 일반 배정과 비교하기 위함
				tbIpAssignMstVo.setMenuType("Rout");
				tbIpAssignMstVo.setScreateId(tbRoutChkMstVo.getScreateId());
				
				destIpAssignMstVos.add(tbIpAssignMstVo);
				
			}
			
			assignMgmtTxService.processUpdateAsgnIPMst(destIpAssignMstVos);
			
		} catch (ServiceException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("CMN.HIGH.00021", new String[]{"서비스 변경"});
		}
	}
	
	/**
	 * IP주소 라우팅 비교/점검 > NextHop 상세 정보 조회
	 * @param tbRoutChkMstVo
	 * @return
	 */
	@Transactional(readOnly = true)
	public IpAllocOperMstVo selectNextHop(IpAllocOperMstVo ipAllocOperMstVo) {
		IpAllocOperMstVo resultVo = new IpAllocOperMstVo();
		int totalCount = 0;
		int totalCountHost = 0;
		try {
			if (ipAllocOperMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			ipAllocOperMstVo.setsNextHop(ipAllocOperMstVo.getsNextHop().replace(" ", "").replace(">to", ""));
			totalCount = routMgmtTxService.countNextHop(ipAllocOperMstVo);
			
			if(totalCount > 0) {
				resultVo = routMgmtTxService.selectNextHop(ipAllocOperMstVo);	
			} else {
				totalCountHost = routMgmtTxService.countNextHopHost(ipAllocOperMstVo);
				if(totalCountHost > 0) {
					resultVo = routMgmtTxService.selectNextHopHost(ipAllocOperMstVo);
					totalCount = totalCountHost;
				}
			}

			resultVo.setTotalCount(totalCount);
			
		} catch (ServiceException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("CMN.HIGH.00000");
		}
		return resultVo;
	}
	
	/**
	 * IP주소 라우팅 비교/점검 > NextHop 상세 정보 조회
	 * @param tbRoutChkMstVo
	 * @return
	 */
	@Transactional(readOnly = true)
	public IpAllocOperMstVo selectLinkMst(IpAllocOperMstVo ipAllocOperMstVo) {
		IpAllocOperMstVo resultVo = new IpAllocOperMstVo();
		int totalCount = 0;
		try {
			if (ipAllocOperMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
		
			totalCount = routMgmtTxService.countLinkMst(ipAllocOperMstVo);
			
			if(totalCount > 0) {
				resultVo = routMgmtTxService.selectLinkMst(ipAllocOperMstVo);	
			}
			
			resultVo.setTotalCount(totalCount);
			
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00000");
		}
		return resultVo;
	}
		
}
