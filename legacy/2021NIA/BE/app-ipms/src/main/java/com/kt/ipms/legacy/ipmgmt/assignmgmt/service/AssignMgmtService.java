package com.kt.ipms.legacy.ipmgmt.assignmgmt.service;

import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kt.framework.exception.ServiceException;
import com.kt.ipms.legacy.cmn.service.IpCommonService;
import com.kt.ipms.legacy.cmn.util.CloneUtil;
import com.kt.ipms.legacy.cmn.util.CommonCodeUtil;
import com.kt.ipms.legacy.cmn.util.PrintLogUtil;
import com.kt.ipms.legacy.cmn.vo.CommonCodeVo;
import com.kt.ipms.legacy.ipmgmt.allocmgmt.vo.TbIpAllocMstVo;
import com.kt.ipms.legacy.ipmgmt.assignmgmt.vo.TbIpAssignMstComplexVo;
import com.kt.ipms.legacy.ipmgmt.assignmgmt.vo.TbIpAssignMstListVo;
import com.kt.ipms.legacy.ipmgmt.assignmgmt.vo.TbIpAssignMstVo;
import com.kt.ipms.legacy.ipmgmt.routmgmt.vo.TbRoutChkMstVo;

@Component
@Transactional
public class AssignMgmtService {
	
	@Autowired
	private AssignMgmtTxService assignMgmtTxService;
	
	@Autowired
	private IpCommonService ipCommonService;
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void insertIpAssignMst(TbIpAssignMstVo tbIpAssignMstVo) {
		assignMgmtTxService.insertIpAssignMst(tbIpAssignMstVo);
	}
	
	@Transactional(readOnly = true)
	public int countAssignBlockViaIpAssignMstVo(TbIpAssignMstVo tbIpAssignMstVo) {
		return assignMgmtTxService.countAssignBlockViaIpAssignMstVo(tbIpAssignMstVo);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteListIpAssignMst(TbIpAssignMstVo tbIpAssignMstVo) {
		tbIpAssignMstVo.setSmodifyId(null);
		assignMgmtTxService.deleteListIpAssignMst(tbIpAssignMstVo);
	}
	
	@Transactional(readOnly = true)
	public TbIpAssignMstListVo selectListIpAssignMst(TbIpAssignMstVo tbIpAssignMstVo) {
		TbIpAssignMstListVo resultListVo = null;
		try {
			if (tbIpAssignMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			List<TbIpAssignMstVo> resultList = assignMgmtTxService.selectListPageIpAssignMst(tbIpAssignMstVo);
			int totalCount = assignMgmtTxService.countListPageIpAssignMst(tbIpAssignMstVo);
			resultListVo = new TbIpAssignMstListVo();
			resultListVo.setTbIpAssignMstVos(resultList);
			resultListVo.setTotalCount(totalCount);
		} catch (ServiceException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("CMN.HIGH.00023", new String[]{"IP배정블록목록"});
		}
		return resultListVo;
	}
	
	@Transactional(readOnly = true)
	public TbIpAssignMstListVo selectListIpAssignMstExcel(TbIpAssignMstVo tbIpAssignMstVo) {
		TbIpAssignMstListVo resultListVo = null;
		try {
			if (tbIpAssignMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			int totalCount = assignMgmtTxService.countListPageIpAssignMst(tbIpAssignMstVo);
			if (totalCount > CommonCodeUtil.MAX_EXCEL_ROW_COUNT) {
				throw new ServiceException("CMN.INFO.00051");
			}
			List<TbIpAssignMstVo> resultList = null;
			if (totalCount > 0) {
				tbIpAssignMstVo.setFirstIndex(1);
				tbIpAssignMstVo.setLastIndex(totalCount);
				resultList = assignMgmtTxService.selectListPageIpAssignMst(tbIpAssignMstVo);	
			}
			resultListVo = new TbIpAssignMstListVo();
			resultListVo.setTbIpAssignMstVos(resultList);
			resultListVo.setTotalCount(totalCount);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"IP배정블록목록"});
		}
		return resultListVo;
	}
	
	@Transactional(readOnly = true)
	public TbIpAssignMstVo selectIpAssignMst(TbIpAssignMstVo tbIpAssignMstVo) {
		TbIpAssignMstVo resultVo = null;
		try {
			if (tbIpAssignMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			resultVo = assignMgmtTxService.selectIpAssignMst(tbIpAssignMstVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"IP배정블록"});
		}
		return resultVo;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public TbIpAssignMstListVo appendDivIpAssignMst(TbIpAssignMstVo tbIpAssignMstVo) {
		TbIpAssignMstListVo resultListVo = null;
		try {
			
			List<TbIpAssignMstVo> resultList = (List<TbIpAssignMstVo>)ipCommonService.getSubnetIpBlockMstInfo(tbIpAssignMstVo);
			resultListVo = new TbIpAssignMstListVo();
			resultListVo.setTbIpAssignMstVos(resultList);
			
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("APP.HIGH.00026");
		}
		return resultListVo;
	}
	
	@Transactional(readOnly = true)
	public TbIpAssignMstVo appendMergeDivIpAssignMst(TbIpAssignMstListVo tbIpAssignMstListVo) {
		TbIpAssignMstVo resultVo = null;
		try {
			if (tbIpAssignMstListVo == null || tbIpAssignMstListVo.getTbIpAssignMstVos() == null ||tbIpAssignMstListVo.getTbIpAssignMstVos().size() == 0) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			List<TbIpAssignMstVo> paramList = tbIpAssignMstListVo.getTbIpAssignMstVos();
			for (TbIpAssignMstVo tbIpAssignMstVo : paramList) {
				ipCommonService.setBaseIpBlockMstInfo(tbIpAssignMstVo);
			}
			
			boolean isMergeSuccess = ipCommonService.getMergeIpBlockMstInfo(paramList);
			if (isMergeSuccess) {
				resultVo = paramList.get(0);
			} else {
				throw new ServiceException("APP.INFO.00028");
			}
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("APP.HIGH.00027");
		}
		return resultVo;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void insertListDivAsgnIPMst(TbIpAssignMstComplexVo tbIpAssignMstComplexVo) {
		try {
			if (tbIpAssignMstComplexVo == null || tbIpAssignMstComplexVo.getSrcIpAssignMstVo() == null
				|| tbIpAssignMstComplexVo.getDestIpAssignMstVos() == null || tbIpAssignMstComplexVo.getDestIpAssignMstVos().size() == 0) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			
			/** 기준 정보 DB 재조회 **/
			TbIpAssignMstVo paramVo = tbIpAssignMstComplexVo.getSrcIpAssignMstVo();
			TbIpAssignMstVo searchVo = new TbIpAssignMstVo();
			searchVo.setNipAssignMstSeq(paramVo.getNipAssignMstSeq());
			
			TbIpAssignMstVo orgVo = assignMgmtTxService.selectIpAssignMst(searchVo);
			if (orgVo == null) {
				throw new ServiceException("APP.HIGH.00029", new String[]{"분할할 IP블록의"});
			} else {
				if (!orgVo.getNlvlMstSeq().equals(paramVo.getNlvlMstSeq())) {
					throw new ServiceException("APP.HIGH.00029", new String[]{"분할할 IP블록의"});
				} else if (!orgVo.getSassignLevelCd().equals(paramVo.getSassignLevelCd())) {
					throw new ServiceException("APP.HIGH.00029", new String[]{"분할할 IP블록의"});
				}
			}
			List<TbIpAssignMstVo> destIpAssignMstVos = tbIpAssignMstComplexVo.getDestIpAssignMstVos();
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
			
			assignMgmtTxService.processInsertDivAsgnIPMst(tbIpAssignMstComplexVo);
			
		} catch (ServiceException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("CMN.HIGH.00020", new String[]{"IP블록 분할 확정"});
		}
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public TbIpAssignMstComplexVo validateMrgAsgnIPMst(TbIpAssignMstListVo tbIpAssignMstListVo) {
		TbIpAssignMstComplexVo resultComplexVo = null;
		try {
			List<TbIpAssignMstVo> destIpAssignMstVos = assignMgmtTxService.selectListAsgnIPMstViaInMstSeq(tbIpAssignMstListVo);
			if (destIpAssignMstVos != null && destIpAssignMstVos.size() > 0) {
				List<TbIpAssignMstVo> dummyIpAssignMstVos = (ArrayList<TbIpAssignMstVo>)((ArrayList<TbIpAssignMstVo>)destIpAssignMstVos).clone();
				boolean isMergeSuccess = ipCommonService.getMergeIpBlockMstInfo(dummyIpAssignMstVos);
				if (isMergeSuccess) {
					TbIpAssignMstVo srcIpAssignMstVo = dummyIpAssignMstVos.get(0);
					TbIpAssignMstVo destIpAssignMstVo = new TbIpAssignMstVo();
					CloneUtil.copyObjectInformation(destIpAssignMstVos.get(0), destIpAssignMstVo);
					String sipVersionTypeCd = destIpAssignMstVo.getSipVersionTypeCd();
					String sipVersionTypeNm = destIpAssignMstVo.getSipVersionTypeNm();
					CloneUtil.copyIpVoInfomation(srcIpAssignMstVo, destIpAssignMstVo);
					srcIpAssignMstVo = destIpAssignMstVo;
					srcIpAssignMstVo.setSipVersionTypeCd(sipVersionTypeCd);
					srcIpAssignMstVo.setSipVersionTypeNm(sipVersionTypeNm);
					resultComplexVo = new TbIpAssignMstComplexVo();
					resultComplexVo.setSrcIpAssignMstVo(srcIpAssignMstVo);
					resultComplexVo.setDestIpAssignMstVos(destIpAssignMstVos);
				} else {
					throw new ServiceException("APP.INFO.00028");
				}
			} else {
				throw new ServiceException("CMN.HIGH.00001");	
			}
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("APP.HIGH.00027");
		}
		return resultComplexVo;
	}
	
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void insertMrgAsgnIPMst(TbIpAssignMstComplexVo tbIpAssignMstComplexVo) {
		try {
			if (tbIpAssignMstComplexVo == null || tbIpAssignMstComplexVo.getSrcIpAssignMstVo() == null 
				|| tbIpAssignMstComplexVo.getDestIpAssignMstVos() == null || tbIpAssignMstComplexVo.getDestIpAssignMstVos().size() == 0) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			List<TbIpAssignMstVo> destVos = tbIpAssignMstComplexVo.getDestIpAssignMstVos();
			
			TbIpAssignMstListVo searchListVo = new TbIpAssignMstListVo();
			searchListVo.setTbIpAssignMstVos(destVos);
			
			List<TbIpAssignMstVo> orgVos = assignMgmtTxService.selectListAsgnIPMstViaInMstSeq(searchListVo);
			if (orgVos == null || destVos.size() != orgVos.size()) {
				throw new ServiceException("APP.HIGH.00029", new String[]{"병합할 IP블록의"});
			}
			//String sassignLevelCd = destVos.get(0).getSassignLevelCd();
			//배정블록 비교하기 (대상 블록들의 화면 vs 데이터 상태값 비교하기)
			TbIpAssignMstVo srcIpAssignMstVo = tbIpAssignMstComplexVo.getSrcIpAssignMstVo();
			for (TbIpAssignMstVo orgVo : orgVos) {
				if (orgVo.getSsvcLineTypeCd().compareTo(srcIpAssignMstVo.getSsvcLineTypeCd()) != 0
					|| orgVo.getSsvcGroupCd().compareTo(srcIpAssignMstVo.getSsvcGroupCd()) != 0
					|| orgVo.getSsvcObjCd().compareTo(srcIpAssignMstVo.getSsvcObjCd()) != 0
					|| !orgVo.getSassignLevelCd().equals(srcIpAssignMstVo.getSassignLevelCd())
					|| !orgVo.getSassignTypeCd().equals(srcIpAssignMstVo.getSassignTypeCd())) {
					
					throw new ServiceException("APP.HIGH.00029", new String[]{"병합할 IP블록의"});
				}
			}
			ipCommonService.setBaseIpBlockMstInfo(srcIpAssignMstVo);
			srcIpAssignMstVo.setNfreeIpCnt(srcIpAssignMstVo.getNcnt());
			srcIpAssignMstVo.setNuseIpCnt(BigInteger.ZERO);
			
			srcIpAssignMstVo.setNipBlockMstSeq(orgVos.get(0).getNipBlockMstSeq());
			srcIpAssignMstVo.setSipCreateSeqCd(orgVos.get(0).getSipCreateSeqCd());
			srcIpAssignMstVo.setSipCreateTypeCd(orgVos.get(0).getSipCreateTypeCd());
			
			srcIpAssignMstVo.setNipmsSvcSeq(BigInteger.ZERO);
			srcIpAssignMstVo.setSipAllocExTypeCd("AE0000");
			
			assignMgmtTxService.processInsertMrgAsgnIPMst(tbIpAssignMstComplexVo);
			
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00020", new String[]{"IP블록 병합 확정"});
		}
	}
	
	@Transactional(readOnly = true)
	public TbIpAssignMstListVo selectListAsgnIPMstViaInMstSeq(TbIpAssignMstListVo tbIpAssignMstListVo) {
		TbIpAssignMstListVo resultListVo = null;
		
		try {
			if (tbIpAssignMstListVo == null || tbIpAssignMstListVo.getTbIpAssignMstVos() == null || tbIpAssignMstListVo.getTbIpAssignMstVos().size() == 0) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			List<TbIpAssignMstVo> resultList = assignMgmtTxService.selectListAsgnIPMstViaInMstSeq(tbIpAssignMstListVo);
			resultListVo = new TbIpAssignMstListVo();
			resultListVo.setTbIpAssignMstVos(resultList);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"IP배정블록"});
		}
		return resultListVo;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void updateListAsgnIPMst(TbIpAssignMstComplexVo tbIpAssignMstComplexVo) {
		try {
			int iChekcCnt = 0;
			
			if (tbIpAssignMstComplexVo == null || tbIpAssignMstComplexVo.getSrcIpAssignMstVo() == null 
				|| tbIpAssignMstComplexVo.getDestIpAssignMstVos() == null || tbIpAssignMstComplexVo.getDestIpAssignMstVos().size() == 0) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			
			TbIpAssignMstVo srcIpAssignMstVo = tbIpAssignMstComplexVo.getSrcIpAssignMstVo();
			List<TbIpAssignMstVo> destIpAssignMstVos = tbIpAssignMstComplexVo.getDestIpAssignMstVos();
			for (TbIpAssignMstVo tbIpAssignMstVo : destIpAssignMstVos) {
				tbIpAssignMstVo.setSsvcLineTypeCd(srcIpAssignMstVo.getSsvcLineTypeCd());
				tbIpAssignMstVo.setSsvcGroupCd(srcIpAssignMstVo.getSsvcGroupCd());
				tbIpAssignMstVo.setSsvcObjCd(srcIpAssignMstVo.getSsvcObjCd());
				tbIpAssignMstVo.setNlvlMstSeq(srcIpAssignMstVo.getNlvlMstSeq());
				tbIpAssignMstVo.setSassignLevelCd(srcIpAssignMstVo.getSassignLevelCd());
				tbIpAssignMstVo.setSassignTypeCd(srcIpAssignMstVo.getSassignTypeCd());
				tbIpAssignMstVo.setSmodifyId(srcIpAssignMstVo.getSmodifyId());
				tbIpAssignMstVo.setScomment(srcIpAssignMstVo.getScomment());
				
				/* 망변경일 경우 블록 변경 대상 확인 로직 추가 */
				if (tbIpAssignMstComplexVo.getSrcIpAssignMstVo().getTypeFlag().equals("chgLine")){
					iChekcCnt = assignMgmtTxService.checkCountAsgnIPMst(tbIpAssignMstVo);
					
					if ( iChekcCnt > 0 ){
						throw new ServiceException("CMN.HIGH.00021", new String[]{"(원인 : 망변경 대상의 배정 블록 대역 정보가 존재함.) IP배정블록"});
					}
				}
				
			}
			assignMgmtTxService.processUpdateAsgnIPMst(destIpAssignMstVos);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00021", new String[]{"IP배정블록"});
		}
	}
	
	/*반납 별 배정 수정*/
	@Transactional(propagation = Propagation.REQUIRED)
	public void processUpdateAsgnIPMst(List<TbIpAssignMstVo> tbIpAssignMstVos) {
		if (tbIpAssignMstVos == null || tbIpAssignMstVos.size() == 0) {
			throw new ServiceException("CMN.HIGH.00021", new String[]{"반납 IP배정블록"});
		} else {
			assignMgmtTxService.processUpdateAsgnIPMst(tbIpAssignMstVos);
		}
	}
	
	/*할당,해지 별 배정 수정*/
	@Transactional(propagation = Propagation.REQUIRED)
	public void processAlocUpdateAsgnIPMst(List<TbIpAssignMstVo> tbIpAssignMstVos) {
		if (tbIpAssignMstVos == null || tbIpAssignMstVos.size() == 0) {
			throw new ServiceException("CMN.HIGH.00021", new String[]{"할당 IP배정블록"});
		} else {
			assignMgmtTxService.processAlocUpdateAsgnIPMst(tbIpAssignMstVos);
		}
	}
	
	/*비고 수정*/
	@Transactional(propagation = Propagation.REQUIRED)
	public void processScommentUpdateAsgnIPMst(List<TbIpAssignMstVo> tbIpAssignMstVos) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		if (tbIpAssignMstVos == null || tbIpAssignMstVos.size() == 0) {
			throw new ServiceException("CMN.HIGH.00021", new String[]{"IP배정블록"});
		} else {
			assignMgmtTxService.processScommentUpdateAsgnIPMst(tbIpAssignMstVos);
		}
	}
	
	/*배정 상세 비고 처리*/
	@Transactional(propagation = Propagation.REQUIRED)
	public void updateListScommentAsgnIPMst(TbIpAssignMstComplexVo tbIpAssignMstComplexVo) {
		try {
			if (tbIpAssignMstComplexVo == null || tbIpAssignMstComplexVo.getSrcIpAssignMstVo() == null 
				|| tbIpAssignMstComplexVo.getDestIpAssignMstVos() == null || tbIpAssignMstComplexVo.getDestIpAssignMstVos().size() == 0) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			TbIpAssignMstVo srcIpAssignMstVo = tbIpAssignMstComplexVo.getSrcIpAssignMstVo();
			List<TbIpAssignMstVo> destIpAssignMstVos = tbIpAssignMstComplexVo.getDestIpAssignMstVos();
			for (TbIpAssignMstVo tbIpAssignMstVo : destIpAssignMstVos) {
				tbIpAssignMstVo.setScomment(srcIpAssignMstVo.getScomment());
				tbIpAssignMstVo.setSmodifyId(srcIpAssignMstVo.getSmodifyId());
			}
			
			//비고(배정) 처리
			assignMgmtTxService.processScommentUpdateAsgnIPMst(destIpAssignMstVos);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00021", new String[]{"비고 IP배정블록"});
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void updateSipCreateSeqCdIpAssignMst(TbIpAssignMstVo tbIpAssignMstVo) {
		assignMgmtTxService.updateSipCreateSeqCdTbIpAssignMstVo(tbIpAssignMstVo);
	}
	
	@Transactional(readOnly = true)
	public TbIpAssignMstListVo selectListUnAssignBlock(TbIpAssignMstVo tbIpAssignMstVo) {
		TbIpAssignMstListVo resultListVo = null;
		try {
			if (tbIpAssignMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			List<TbIpAssignMstVo> resultList = assignMgmtTxService.selectListUnAssignBlock(tbIpAssignMstVo);
			int totalCount = assignMgmtTxService.countSelectListUnAssignBlock(tbIpAssignMstVo);
			resultListVo = new TbIpAssignMstListVo();
			resultListVo.setTbIpAssignMstVos(resultList);
			resultListVo.setTotalCount(totalCount);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"IP 미배정블록목록"});
		}
		return resultListVo;
	}
	
	@Transactional(readOnly = true)
	public TbIpAssignMstListVo selectListUnAssignBlockExcel(TbIpAssignMstVo tbIpAssignMstVo) {
		TbIpAssignMstListVo resultListVo = null;
		try {
			if (tbIpAssignMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			int totalCount = assignMgmtTxService.countSelectListUnAssignBlock(tbIpAssignMstVo);
			if (totalCount > CommonCodeUtil.MAX_EXCEL_ROW_COUNT) {
				throw new ServiceException("CMN.INFO.00051");
			}
			List<TbIpAssignMstVo> resultList = null;
			if (totalCount > 0) {
				tbIpAssignMstVo.setFirstIndex(1);
				tbIpAssignMstVo.setLastIndex(totalCount);
				resultList = assignMgmtTxService.selectListUnAssignBlock(tbIpAssignMstVo);
			}
			resultListVo = new TbIpAssignMstListVo();
			resultListVo.setTbIpAssignMstVos(resultList);
			resultListVo.setTotalCount(totalCount);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"IP 미배정블록목록"});
		}
		return resultListVo;
	}
	
	@Transactional(readOnly = true)
	public TbIpAssignMstListVo selectListOptimizeIpSource(TbIpAssignMstVo tbIpAssignMstVo) {
		TbIpAssignMstListVo resultListVo = null;
		try {
			if (tbIpAssignMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			List<TbIpAssignMstVo> resultList = assignMgmtTxService.selectListPageOptimizeIpSource(tbIpAssignMstVo);
			int totalCount = assignMgmtTxService.countListPageOptimizeIpSource(tbIpAssignMstVo);
			resultListVo = new TbIpAssignMstListVo();
			resultListVo.setTbIpAssignMstVos(resultList);
			resultListVo.setTotalCount(totalCount);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"최적화대상목록"});
		}
		return resultListVo;
	}
	
	@Transactional(readOnly = true)
	public TbIpAssignMstListVo selectListOptimizeIpTarget(TbIpAssignMstVo tbIpAssignMstVo) {
		TbIpAssignMstListVo resultListVo = null;
		try {
			if (tbIpAssignMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			List<TbIpAssignMstVo> resultList = assignMgmtTxService.selectListOptimizeIpTarget(tbIpAssignMstVo);
			int totalCount = assignMgmtTxService.countListOptimizeIpTarget(tbIpAssignMstVo);
			resultListVo = new TbIpAssignMstListVo();
			resultListVo.setTbIpAssignMstVos(resultList);
			resultListVo.setTotalCount(totalCount);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"IP대체목록"});
		}
		return resultListVo;
	}
	
	@Transactional(readOnly = true)
	public TbIpAssignMstListVo selectListOptimizeIpRecommend(TbIpAssignMstVo tbIpAssignMstVo) {
		TbIpAssignMstListVo resultListVo = null;
		try {
			if (tbIpAssignMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			List<TbIpAssignMstVo> resultList = assignMgmtTxService.selectListOptimizeIpRecommend(tbIpAssignMstVo);
			int totalCount = assignMgmtTxService.countListOptimizeIpRecommend(tbIpAssignMstVo);
			resultListVo = new TbIpAssignMstListVo();
			resultListVo.setTbIpAssignMstVos(resultList);
			resultListVo.setTotalCount(totalCount);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"IP대체추천목록"});
		}
		return resultListVo;
	}
	
	/** 조직별 서비스 유형 셋팅(2014.12.04) **/
	@Transactional(readOnly = true)
	public List<CommonCodeVo> selectOrgSassignTypeCdList(TbIpAllocMstVo tbIpAllocMstVo) {
		List<CommonCodeVo> resultVo = null;
		try {
			if (tbIpAllocMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			resultVo = assignMgmtTxService.selectOrgSassignTypeCdList(tbIpAllocMstVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"조직기반 서비스목록"});
		}
		return resultVo;
	}
	
	/**
	 * Summary 상세조회 - KORNET
	 * @param tbIpAssignMstVo
	 * @return
	 */
	@Transactional(readOnly = true)
	public TbIpAssignMstListVo selectSummaryDetailKornet(TbIpAssignMstVo tbIpAssignMstVo) {
		TbIpAssignMstListVo resultListVo = null;
		try {
			if (tbIpAssignMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			List<TbIpAssignMstVo> resultList = assignMgmtTxService.selectSummaryDetailKornet(tbIpAssignMstVo);
			int totalCount = assignMgmtTxService.countListPageSummaryDetailKornet(tbIpAssignMstVo);
			resultListVo = new TbIpAssignMstListVo();
			resultListVo.setTbIpAssignMstVos(resultList);
			resultListVo.setTotalCount(totalCount);
			
		} catch (ServiceException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("CMN.HIGH.00023", new String[]{"Summary 정보"});
		}
		return resultListVo;
	}
	
	/**
	 * Summary 상세조회 - PREMIUM
	 * @param tbIpAssignMstVo
	 * @return
	 */
	@Transactional(readOnly = true)
	public TbIpAssignMstListVo selectSummaryDetailPremium(TbIpAssignMstVo tbIpAssignMstVo) {
		TbIpAssignMstListVo resultListVo = null;
		try {
			if (tbIpAssignMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			List<TbIpAssignMstVo> resultList = assignMgmtTxService.selectSummaryDetailPremium(tbIpAssignMstVo);
			int totalCount = assignMgmtTxService.countListPageSummaryDetailPremium(tbIpAssignMstVo);
			resultListVo = new TbIpAssignMstListVo();
			resultListVo.setTbIpAssignMstVos(resultList);
			resultListVo.setTotalCount(totalCount);
			
		} catch (ServiceException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("CMN.HIGH.00023", new String[]{"Summary 정보"});
		}
		return resultListVo;
	}
}
