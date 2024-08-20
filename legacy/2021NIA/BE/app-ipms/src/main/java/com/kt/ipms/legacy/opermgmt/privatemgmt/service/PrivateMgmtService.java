package com.kt.ipms.legacy.opermgmt.privatemgmt.service;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kt.framework.exception.ServiceException;
import com.kt.ipms.legacy.cmn.service.IpCommonService;
import com.kt.ipms.legacy.cmn.util.CommonCodeUtil;
import com.kt.ipms.legacy.ipmgmt.allocmgmt.vo.IpAllocOperMstListVo;
import com.kt.ipms.legacy.ipmgmt.allocmgmt.vo.IpAllocOperMstVo;
import com.kt.ipms.legacy.ipmgmt.createmgmt.service.CreateMgmtService;
import com.kt.ipms.legacy.ipmgmt.createmgmt.vo.TbIpBlockMstComplexVo;
import com.kt.ipms.legacy.ipmgmt.createmgmt.vo.TbIpBlockMstListVo;
import com.kt.ipms.legacy.ipmgmt.createmgmt.vo.TbIpBlockMstVo;
import com.kt.ipms.legacy.opermgmt.privatemgmt.vo.TbIpPrivateReqMstComplexVo;
import com.kt.ipms.legacy.opermgmt.privatemgmt.vo.TbIpPrivateReqMstListVo;
import com.kt.ipms.legacy.opermgmt.privatemgmt.vo.TbIpPrivateReqMstVo;
import com.kt.ipms.legacy.opermgmt.whoismgmt.vo.TbWhoisModifyVo;
import com.kt.ipms.legacy.opermgmt.whoismgmt.vo.TbWhoisUserVo;
import com.kt.ipms.legacy.opermgmt.whoismgmt.vo.TbWhoisVo;

@Component
@Transactional
public class PrivateMgmtService {

	@Autowired
	private PrivateMgmtTxService privateMgmtTxService;
	
	@Autowired
	private IpCommonService ipCommonService;
	
	@Autowired
	private CreateMgmtService createMgmtService;
	
	
	/**
	 * 사설IP신청 목록 조회
	 * @param tbIpPrivateReqMstVo
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = true)
	public TbIpPrivateReqMstListVo selectListTbIpPrivateReqMst(TbIpPrivateReqMstVo tbIpPrivateReqMstVo) {
		TbIpPrivateReqMstListVo resultListVo = null;
		
		try {
			if (tbIpPrivateReqMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			List<TbIpPrivateReqMstVo> resultList = privateMgmtTxService.selectListTbIpPrivateReqMst(tbIpPrivateReqMstVo);
			int totalCount = privateMgmtTxService.countListPageIpPrivateReqMst(tbIpPrivateReqMstVo);
			resultListVo = new TbIpPrivateReqMstListVo();
			resultListVo.setTbIpPrivateReqMstVos(resultList);
			resultListVo.setTotalCount(totalCount);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"사설IP신청목록"});
		}
		return resultListVo;
	}
	
	/**
	 * 사설IP신청 목록 엑셀 다운로드
	 * @param tbIpPrivateReqMstVo
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = true)
	public TbIpPrivateReqMstListVo selectListTbIpPrivateReqMstExcel(TbIpPrivateReqMstVo tbIpPrivateReqMstVo) {
		TbIpPrivateReqMstListVo resultListVo = null;
		
		if (tbIpPrivateReqMstVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {
			int totalCount = privateMgmtTxService.countListPageIpPrivateReqMst(tbIpPrivateReqMstVo);
			if (totalCount > CommonCodeUtil.MAX_EXCEL_ROW_COUNT) {
				throw new ServiceException("CMN.INFO.00051");
			}
			List<TbIpPrivateReqMstVo> resultList = null;
			if (totalCount > 0) {
				tbIpPrivateReqMstVo.setFirstIndex(1);
				tbIpPrivateReqMstVo.setLastIndex(totalCount);
				resultList = privateMgmtTxService.selectListTbIpPrivateReqMst(tbIpPrivateReqMstVo);
			}
			resultListVo = new TbIpPrivateReqMstListVo();
			resultListVo.setTbIpPrivateReqMstVos(resultList);
			resultListVo.setTotalCount(totalCount);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"사설IP신청목록 엑셀저장"});
		}
		
		
		return resultListVo;
	}
	
	/**
	 * 사설IP신청 상세 정보 조회
	 * @param tbIpPrivateReqMstVo
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = true)
	public TbIpPrivateReqMstVo selectPrivateIPMst(TbIpPrivateReqMstVo tbIpPrivateReqMstVo) {
		TbIpPrivateReqMstVo resultVo = null;
		try {
			if (tbIpPrivateReqMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			resultVo = privateMgmtTxService.selectPrivateIPMst(tbIpPrivateReqMstVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"사설IP신청"});
		}
		return resultVo;
	}
	
	/**
	 * 사설IP신청 UI Append 로직 
	 * @param tbIpPrivateReqMstComplexVo
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = true)
	public TbIpPrivateReqMstVo appendIpBlockMst(TbIpPrivateReqMstComplexVo tbIpPrivateReqMstComplexVo) {
		TbIpPrivateReqMstVo resultVo = null;
		try {
			if (tbIpPrivateReqMstComplexVo == null || tbIpPrivateReqMstComplexVo.getSrcIpPrivateReqMstVo() == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			
			TbIpPrivateReqMstVo srcIpBlockMstVo = tbIpPrivateReqMstComplexVo.getSrcIpPrivateReqMstVo();
			ipCommonService.setBaseIpBlockMstInfo(srcIpBlockMstVo);
			
			/** UI 상 APPEND 목록과 Range 체크 **/
			List<TbIpPrivateReqMstVo> destIpBlockMstVos = tbIpPrivateReqMstComplexVo.getDestIpPrivateReqMstVo();
			if (destIpBlockMstVos != null && destIpBlockMstVos.size() > 0) {
				for (TbIpPrivateReqMstVo destIpBlockMstVo : destIpBlockMstVos) {
					ipCommonService.setBaseIpBlockMstInfo(destIpBlockMstVo);
					if (srcIpBlockMstVo.getSipVersionTypeCd().equals(destIpBlockMstVo.getSipVersionTypeCd()) 
						&& !ipCommonService.checkIpRange(srcIpBlockMstVo, destIpBlockMstVo)) {
						throw new ServiceException("APP.INFO.00019", new String[]{srcIpBlockMstVo.getPipPrefix()});  // 입력하신 {}은 등록하실 생성블록 범위내에 존재합니다.
					}
				}
			}                                                                                                                                                                                                                                                                                                                            
			
			/* 사설 IP 신청시 유효성 체크 */
			srcIpBlockMstVo.setSrequestTypeCd(CommonCodeUtil.REQUEST_TYPE01); // 신규신청
			String sMessage = privateMgmtTxService.checkPrivateIpReqValid(srcIpBlockMstVo);
			
			if(!sMessage.equals("SUCCESS")) {
				throw new ServiceException(sMessage);
			}
			
			resultVo = srcIpBlockMstVo;
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00000");
		}
		
		return resultVo;
	}
	
	/**
	 * 사설IP신청 신규신청 등록
	 * @param tbIpPrivateReqMstListVo
	 * @return
	 * @throws Exception
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public void insertListIpPrivateReqMst(TbIpPrivateReqMstListVo tbIpPrivateReqMstListVo, BigInteger nlvlMstSeq) {
		try {
			if (tbIpPrivateReqMstListVo == null || tbIpPrivateReqMstListVo.getTbIpPrivateReqMstVos() == null
				|| tbIpPrivateReqMstListVo.getTbIpPrivateReqMstVos().size() == 0 || nlvlMstSeq == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			
			List<TbIpPrivateReqMstVo> tbIpPrivateReqMstVos = tbIpPrivateReqMstListVo.getTbIpPrivateReqMstVos();
			
			String sipPrivateReqMstSeqCd = null;
			
			if (tbIpPrivateReqMstVos != null && tbIpPrivateReqMstVos.size() > 0) {
				TbIpPrivateReqMstVo tbIpPrivateReqMstSeqVo = tbIpPrivateReqMstVos.get(0);
				if (tbIpPrivateReqMstSeqVo.getSipPrivateReqMstSeqCd() == null || tbIpPrivateReqMstSeqVo.getSipPrivateReqMstSeqCd().equals("")
											|| tbIpPrivateReqMstSeqVo.getSipPrivateReqMstSeqCd().equals("&nbsp;")) {

					sipPrivateReqMstSeqCd = privateMgmtTxService.selectNipPrivateReqMstSeqCd(tbIpPrivateReqMstSeqVo);
				}
			}
			
			
			for (TbIpPrivateReqMstVo tbIpPrivateReqMstVo : tbIpPrivateReqMstVos) {
				/** IP블럭 기본 정보 **/
				ipCommonService.setBaseIpBlockMstInfo(tbIpPrivateReqMstVo);
				tbIpPrivateReqMstVo.setNfreeIpCnt(tbIpPrivateReqMstVo.getNcnt());
				tbIpPrivateReqMstVo.setNuseIpCnt(BigInteger.ZERO);
					
				/* 사설 IP 신청시 유효성 체크 */
				tbIpPrivateReqMstVo.setNlvlMstSeq(nlvlMstSeq);
				String sMessage = privateMgmtTxService.checkPrivateIpReqValid(tbIpPrivateReqMstVo);
				
				if(!sMessage.equals("SUCCESS")) {
					throw new ServiceException(sMessage);
				}
			
				/* 사설 IP 신청 등록 */
				tbIpPrivateReqMstVo.setNlvlMstSeq(nlvlMstSeq);
				tbIpPrivateReqMstVo.setSipPrivateReqMstSeqCd(sipPrivateReqMstSeqCd);
				privateMgmtTxService.insertListIpPrivateReqMstVo(tbIpPrivateReqMstVo);
			}
			
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00020", new String[]{"사설IP블록 신청"});
		}
	}

	/**
	 * 사설IP신청 목록 삭제
	 * @param tbIpPrivateReqMstVo
	 * @return
	 */
	public TbIpPrivateReqMstVo deletePrivateIPMst(TbIpPrivateReqMstVo deleteVo) {
		if(deleteVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {
			privateMgmtTxService.deletePrivateIPMstVo(deleteVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00022", new String[]{"사설IP블록 신청"});
		}
		
		return deleteVo;
	}
	
	/**
	 *사설IP신청 목록 승인/반려
	 * @param tbIpPrivateReqMstVo
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public int updatePrivateIPMstAppr(TbIpPrivateReqMstVo tbIpPrivateReqMstVo) {
		int resultCount = 0;
		
		if(tbIpPrivateReqMstVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		
		try {
			
			
			List<TbIpBlockMstVo> tbIpBlockMstVos = new ArrayList<TbIpBlockMstVo>();
			TbIpBlockMstListVo tbIpBlockMstListVo = new TbIpBlockMstListVo();
			TbIpBlockMstVo tbIpBlockMstVo = new TbIpBlockMstVo();
			BigInteger nlvlMstSeq = null;
			TbIpPrivateReqMstVo selVo = null;
			
			// 신청건 상세 조회
			selVo = privateMgmtTxService.selectPrivateIPMst(tbIpPrivateReqMstVo);
			
			if(selVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			
			
			/* IP블록 생성 등록 */
			/////////////////////////////////////////////////////////////////////////////////////////////////////  승인
			if(CommonCodeUtil.REQUEST_STAT02.equals(tbIpPrivateReqMstVo.getSrequestStatCd())) {	
				
				nlvlMstSeq = selVo.getNlvlMstSeq();
				tbIpBlockMstVo.setPipPrefix(selVo.getPipPrefix());
				tbIpBlockMstVo.setSsvcLineTypeCd(selVo.getSsvcLineTypeCd());
				tbIpBlockMstVo.setSsvcGroupCd(selVo.getSsvcGroupCd());
				tbIpBlockMstVo.setSsvcObjCd(selVo.getSsvcObjCd());
				tbIpBlockMstVo.setNlvlMstSeq(nlvlMstSeq);
				tbIpBlockMstVo.setSipVersionTypeCd(selVo.getSipVersionTypeCd());
				tbIpBlockMstVo.setSipCreateTypeCd(selVo.getSipCreateTypeCd());
				tbIpBlockMstVo.setScreateId(selVo.getScreateId());
				tbIpBlockMstVo.setSmodifyId(selVo.getScreateId());
				tbIpBlockMstVo.setScomment(selVo.getScomment());
				tbIpBlockMstVo.setsMenuHistCd("PrivateReq");
				//tbIpBlockMstVo.setSmenuNm("사설IP신청");
				
				////////////////////////////////// 신규신청
				if(CommonCodeUtil.REQUEST_TYPE01.equals(selVo.getSrequestTypeCd())) { 
					
					
					tbIpBlockMstVos.add(tbIpBlockMstVo);
					tbIpBlockMstListVo.setTbIpBlockMstVos(tbIpBlockMstVos);
					tbIpBlockMstListVo.setsMenuType("PrivateReq"); // 사설IP신청
					createMgmtService.insertListIpBlockMst(tbIpBlockMstListVo, nlvlMstSeq);
					
					// IP블록 등록 성공 시 
					TbIpPrivateReqMstVo updateVo = privateMgmtTxService.selectIpBlockMst(tbIpBlockMstVo);
					tbIpPrivateReqMstVo.setNipBlockMstSeq(updateVo.getNipBlockMstSeq());
					tbIpPrivateReqMstVo.setSipCreateSeqCd(updateVo.getSipCreateSeqCd());
					
					if(updateVo.getNipBlockMstSeq() != null) {
						resultCount = 1;		// Success
					}
				} 
				////////////////////////////////// 삭제신청
				else if(CommonCodeUtil.REQUEST_TYPE02.equals(selVo.getSrequestTypeCd())) {
					
					tbIpBlockMstVo.setSsvcLineTypeCd(selVo.getSsvcLineTypeCd());
					tbIpBlockMstVo.setSsvcGroupCd(selVo.getSsvcGroupCd());
					tbIpBlockMstVo.setSsvcObjCd(selVo.getSsvcObjCd());
					tbIpBlockMstVo.setNipBlockMstSeq(selVo.getNipBlockMstSeq());
					tbIpBlockMstVo.setScreateId(selVo.getScreateId());
					tbIpBlockMstVo.setSmodifyId(selVo.getScreateId());
					tbIpBlockMstVo.setsMenuHistCd("PrivateReq");
					//tbIpBlockMstVo.setSmenuNm("사설IP신청");
					createMgmtService.deleteIpBlockMst(tbIpBlockMstVo);
					
					// IP블록 삭제 성공 시 
					TbIpPrivateReqMstVo resultVo = privateMgmtTxService.selectIpBlockMst(tbIpBlockMstVo);
					
					if(resultVo == null || resultVo.getNipBlockMstSeq() == null) {
						resultCount = 1;		// Success
					}
				}
				
			} 
			/////////////////////////////////////////////////////////////////////////////////////////////////////  반려
			else if(CommonCodeUtil.REQUEST_STAT03.equals(tbIpPrivateReqMstVo.getSrequestStatCd())) {
				
				resultCount = 1;
			} 


			if(resultCount == 0) {
				throw new ServiceException("CMN.HIGH.00000");
			} 
			
			/* 신청 테이블 반영 */
			privateMgmtTxService.updatePrivateIPMstAppr(tbIpPrivateReqMstVo);
			
			
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00020", new String[]{"사설IP블록 승인/반려"});
		}
		
		return resultCount;
	}
	
	/**
	 * 사설IP신청 목록 일괄 승인/반려
	 * @param tbIpPrivateReqMstVo
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public int updatePrivateIPMstApprAll(TbIpPrivateReqMstVo tbIpPrivateReqMstVo) {
		int resultCount = 0;
		List<String> schkList = null;
		List<BigInteger> nchkList = null;
		
		if(tbIpPrivateReqMstVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		
		try {
			
			schkList = tbIpPrivateReqMstVo.getsChkList(); 
			nchkList = tbIpPrivateReqMstVo.getnChkList();
			
			/* IP블록 생성 등록 */
			/////////////////////////////////////////////////////////////////////////////////////////////////////  승인
			if(CommonCodeUtil.REQUEST_STAT02.equals(tbIpPrivateReqMstVo.getSrequestStatCd())) {
				
				for(String strRequestSeqCd : schkList) {
					
					tbIpPrivateReqMstVo.setSipPrivateReqMstSeqCd(strRequestSeqCd);
					
					List<TbIpPrivateReqMstVo> listVo = privateMgmtTxService.selectPrivateIPMstAll(tbIpPrivateReqMstVo);
					
					
					if(listVo == null) {
						throw new ServiceException("CMN.HIGH.00001");
					}
					
					
					BigInteger nlvlMstSeq = null;
					
					List<TbIpBlockMstVo> tbIpBlockMstVos = new ArrayList<TbIpBlockMstVo>();
					TbIpBlockMstListVo tbIpBlockMstListVo = new TbIpBlockMstListVo();
					
					for(TbIpPrivateReqMstVo vo : listVo) {
						
						TbIpBlockMstVo tbIpBlockMstVo = new TbIpBlockMstVo();
						nlvlMstSeq = listVo.get(0).getNlvlMstSeq();
						tbIpBlockMstVo.setPipPrefix(vo.getPipPrefix());
						tbIpBlockMstVo.setSsvcLineTypeCd(vo.getSsvcLineTypeCd());
						tbIpBlockMstVo.setSsvcGroupCd(vo.getSsvcGroupCd());
						tbIpBlockMstVo.setSsvcObjCd(vo.getSsvcObjCd());
						tbIpBlockMstVo.setNlvlMstSeq(nlvlMstSeq);
						tbIpBlockMstVo.setSipVersionTypeCd(vo.getSipVersionTypeCd());
						tbIpBlockMstVo.setSipCreateTypeCd(vo.getSipCreateTypeCd());
						tbIpBlockMstVo.setScreateId(vo.getScreateId());
						tbIpBlockMstVo.setSmodifyId(vo.getScreateId());
						tbIpBlockMstVo.setScomment(vo.getScomment());
						tbIpBlockMstVo.setsMenuHistCd("PrivateReq");
						//tbIpBlockMstVo.setSmenuNm("사설IP신청");
						
						tbIpBlockMstVos.add(tbIpBlockMstVo);
					}
					

					////////////////////////////////// 신규신청
					if(CommonCodeUtil.REQUEST_TYPE01.equals(listVo.get(0).getSrequestTypeCd())) { 

					
						tbIpBlockMstListVo.setTbIpBlockMstVos(tbIpBlockMstVos);
						tbIpBlockMstListVo.setsMenuType("PrivateReq"); // 사설IP신청
						createMgmtService.insertListIpBlockMst(tbIpBlockMstListVo, nlvlMstSeq);
						
						// IP블록 등록 성공 시 
						for(TbIpPrivateReqMstVo vo : listVo) {
							
							TbIpBlockMstVo selVo = new TbIpBlockMstVo();
							selVo.setPipPrefix(vo.getPipPrefix());
							selVo.setSsvcLineTypeCd(vo.getSsvcLineTypeCd());
							selVo.setSipVersionTypeCd(vo.getSipVersionTypeCd());
							selVo.setSipCreateTypeCd(vo.getSipCreateTypeCd());
							selVo.setNlvlMstSeq(vo.getNlvlMstSeq());
							
							TbIpPrivateReqMstVo updateVo = privateMgmtTxService.selectIpBlockMst(selVo);
							tbIpPrivateReqMstVo.setNipBlockMstSeq(updateVo.getNipBlockMstSeq());
							tbIpPrivateReqMstVo.setSipCreateSeqCd(updateVo.getSipCreateSeqCd());
							
							if(updateVo.getNipBlockMstSeq() != null) {
								resultCount = 1;		// Success
							}
						}
						
						
					} 
					////////////////////////////////// 삭제신청
					else if(CommonCodeUtil.REQUEST_TYPE02.equals(listVo.get(0).getSrequestTypeCd())) {
						
						for(TbIpPrivateReqMstVo vo : listVo) {

							TbIpBlockMstVo delVo = new TbIpBlockMstVo();
							delVo.setSsvcLineTypeCd(vo.getSsvcLineTypeCd());
							delVo.setSsvcGroupCd(vo.getSsvcGroupCd());
							delVo.setSsvcObjCd(vo.getSsvcObjCd());
							delVo.setNipBlockMstSeq(vo.getNipBlockMstSeq());
							delVo.setScreateId(vo.getScreateId());
							delVo.setSmodifyId(vo.getScreateId());
							delVo.setsMenuHistCd("PrivateReq");
							//delVo.setSmenuNm("사설IP신청");
							createMgmtService.deleteIpBlockMst(delVo);
							
							// IP블록 삭제 성공 시 
							TbIpPrivateReqMstVo resultVo = privateMgmtTxService.selectIpBlockMst(delVo);
							
							if(resultVo == null || resultVo.getNipBlockMstSeq() == null) {
								resultCount = 1;		// Success
							}
						}
						 
					}
					
				}
				
				for(BigInteger seq : nchkList) {
					
					/* 신청 테이블 반영 */
					tbIpPrivateReqMstVo.setNipPrivateReqMstSeq(seq);
					privateMgmtTxService.updatePrivateIPMstAppr(tbIpPrivateReqMstVo);
				}
				
			}
			/////////////////////////////////////////////////////////////////////////////////////////////////////  반려
			else if(CommonCodeUtil.REQUEST_STAT03.equals(tbIpPrivateReqMstVo.getSrequestStatCd())) {
				
				for(BigInteger seq : nchkList) {
					
					/* 신청 테이블 반영 */
					tbIpPrivateReqMstVo.setNipPrivateReqMstSeq(seq);
					privateMgmtTxService.updatePrivateIPMstAppr(tbIpPrivateReqMstVo);
				}

				resultCount = 1;
				
			}
			
			
			
			
		} catch (ServiceException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("CMN.HIGH.00020", new String[]{"사설IP블록 승인/반려"});
		}
		
		return resultCount;
	}
	
	/**
	 * 사설IP신청 삭제신청 - 조회
	 * @param tbIpPrivateReqMstVo
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = true)
	public TbIpPrivateReqMstListVo selectListTbIpPrivateDelReq(TbIpPrivateReqMstVo tbIpPrivateReqMstVo) {
		TbIpPrivateReqMstListVo resultListVo = null;
		
		try {
			if (tbIpPrivateReqMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			List<TbIpPrivateReqMstVo> resultList = privateMgmtTxService.selectListTbIpPrivateDelReq(tbIpPrivateReqMstVo);
			int totalCount = privateMgmtTxService.countListTbIpPrivateDelReq(tbIpPrivateReqMstVo);
			resultListVo = new TbIpPrivateReqMstListVo();
			resultListVo.setTbIpPrivateReqMstVos(resultList);
			resultListVo.setTotalCount(totalCount);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"사설IP삭제신청"});
		}
		return resultListVo;
	}
	
	/**
	 * 사설IP신청 삭제신청 등록
	 * @param tbIpPrivateReqMstVo
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public void insertListIpPrivateDelReqMst(TbIpPrivateReqMstVo tbIpPrivateReqMstVo) {
		try {
			if (tbIpPrivateReqMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			
			String sipPrivateReqMstSeqCd = null;
			
			if (tbIpPrivateReqMstVo.getSipPrivateReqMstSeqCd() == null || tbIpPrivateReqMstVo.getSipPrivateReqMstSeqCd().equals("")
										|| tbIpPrivateReqMstVo.getSipPrivateReqMstSeqCd().equals("&nbsp;")) {

				sipPrivateReqMstSeqCd = privateMgmtTxService.selectNipPrivateReqMstSeqCd(tbIpPrivateReqMstVo);
			}
			
			
			List<BigInteger> delList = tbIpPrivateReqMstVo.getnChkList();
			
			for (BigInteger seq : delList) {

				tbIpPrivateReqMstVo.setNipBlockMstSeq(seq);
				TbIpPrivateReqMstVo delVo = privateMgmtTxService.selectPrivateDelIPMst(tbIpPrivateReqMstVo);
				
				/* 사설 IP 신청시 유효성 체크 */
				tbIpPrivateReqMstVo.setSsvcLineTypeCd(delVo.getSsvcLineTypeCd());
				tbIpPrivateReqMstVo.setNlvlMstSeq(delVo.getNlvlMstSeq());
				tbIpPrivateReqMstVo.setSipVersionTypeCd(delVo.getSipVersionTypeCd());
				tbIpPrivateReqMstVo.setPipPrefix(delVo.getPipPrefix());
				String sMessage = privateMgmtTxService.checkPrivateIpReqValid(tbIpPrivateReqMstVo);
				
				
				if(!sMessage.equals("SUCCESS")) {
					throw new ServiceException(sMessage);
				}
				
				/* 사설 IP 신청 등록 */
				delVo.setSipPrivateReqMstSeqCd(sipPrivateReqMstSeqCd);
				delVo.setSrequestTypeCd(tbIpPrivateReqMstVo.getSrequestTypeCd());
				delVo.setSrequestTypeNm(tbIpPrivateReqMstVo.getSrequestTypeNm());
				delVo.setSrequestStatCd(tbIpPrivateReqMstVo.getSrequestStatCd());
				delVo.setScreateId(tbIpPrivateReqMstVo.getScreateId());
				delVo.setSrequestId(tbIpPrivateReqMstVo.getScreateId());
				
				privateMgmtTxService.insertListIpPrivateReqMstVo(delVo);
			}
			
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00020", new String[]{"사설IP블록 신청"});
		}
	}
	
	/**
	 * 사설IP신청 상세조회(신청차수별)
	 * @param tbIpPrivateReqMstVo
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = true)
	public TbIpPrivateReqMstListVo selectPrivateIPMstSeqCd(TbIpPrivateReqMstVo tbIpPrivateReqMstVo) {
		TbIpPrivateReqMstListVo resultListVo = null;
		
		try {
			if (tbIpPrivateReqMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			List<TbIpPrivateReqMstVo> resultList = privateMgmtTxService.selectPrivateIPMstSeqCd(tbIpPrivateReqMstVo);
			
			resultListVo = new TbIpPrivateReqMstListVo();
			resultListVo.setTbIpPrivateReqMstVos(resultList);
			
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"사설IP신청"});
		}
		return resultListVo;
	}
}
