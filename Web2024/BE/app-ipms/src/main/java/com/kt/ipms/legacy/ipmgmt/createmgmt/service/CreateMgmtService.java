package com.kt.ipms.legacy.ipmgmt.createmgmt.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kt.framework.exception.ServiceException;
import com.kt.ipms.legacy.cmn.service.IpCommonService;
import com.kt.ipms.legacy.cmn.util.CommonCodeUtil;
import com.kt.ipms.legacy.ipmgmt.createmgmt.vo.TbIpBlockMstComplexVo;
import com.kt.ipms.legacy.ipmgmt.createmgmt.vo.TbIpBlockMstListVo;
import com.kt.ipms.legacy.ipmgmt.createmgmt.vo.TbIpBlockMstVo;



@Component
@Transactional
public class CreateMgmtService {
	
@Lazy @Autowired
	private IpCommonService ipCommonService;
	
@Lazy @Autowired
	private CreateMgmtTxService createMgmtTxService;
	
	/**
	 * IP 블록 생성 목록 조회
	 * @param tbIpBlockMstVo
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = true)
	public TbIpBlockMstListVo selectListIpBlockMst(TbIpBlockMstVo tbIpBlockMstVo) {
		TbIpBlockMstListVo resultListVo = null;
		
		try {
			if (tbIpBlockMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			List<TbIpBlockMstVo> resultList = createMgmtTxService.selectListPageIpBlockMst(tbIpBlockMstVo);
			int totalCount = createMgmtTxService.countListPageIpBlockMst(tbIpBlockMstVo);
			resultListVo = new TbIpBlockMstListVo();
			resultListVo.setTbIpBlockMstVos(resultList);
			resultListVo.setTotalCount(totalCount);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"IP생성블록목록"});
		}
		return resultListVo;
	}
	
	@Transactional(readOnly = true)
	public TbIpBlockMstListVo selectListIpBlockMstExcel(TbIpBlockMstVo tbIpBlockMstVo) {
		TbIpBlockMstListVo resultListVo = null;
		try {
			if (tbIpBlockMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			int totalCount = createMgmtTxService.countListPageIpBlockMst(tbIpBlockMstVo);
			if (totalCount > CommonCodeUtil.MAX_EXCEL_ROW_COUNT) {
				throw new ServiceException("CMN.INFO.00051");
			}
			List<TbIpBlockMstVo> resultList = null;
			if (totalCount > 0) {
				tbIpBlockMstVo.setFirstIndex(1);
				tbIpBlockMstVo.setLastIndex(totalCount);
				resultList = createMgmtTxService.selectListPageIpBlockMst(tbIpBlockMstVo);	
			}
			resultListVo = new TbIpBlockMstListVo();
			resultListVo.setTbIpBlockMstVos(resultList);
			resultListVo.setTotalCount(totalCount);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"IP생성블록목록"});
		}
		return resultListVo;
	}
	
	/**
	 * IP 블록 생성 상세 정보 조회
	 * @param tbIpBlockMstVo
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = true)
	public TbIpBlockMstVo selectIpBlockMst(TbIpBlockMstVo tbIpBlockMstVo) {
		TbIpBlockMstVo resultVo = null;
		try {
			if (tbIpBlockMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			resultVo = createMgmtTxService.selectIpBlockMst(tbIpBlockMstVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"IP생성블록"});
		}
		return resultVo;
	}
	
	/**
	 * IP 블록 생성 UI Append 로직 
	 * @param tbIpBlockMstComplexVo
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = true)
	public TbIpBlockMstVo appendIpBlockMst(TbIpBlockMstComplexVo tbIpBlockMstComplexVo) {
		TbIpBlockMstVo resultVo = null;
		try {
			if (tbIpBlockMstComplexVo == null || tbIpBlockMstComplexVo.getSrcIpBlockMstVo() == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			
			TbIpBlockMstVo srcIpBlockMstVo = tbIpBlockMstComplexVo.getSrcIpBlockMstVo();
			ipCommonService.setBaseIpBlockMstInfo(srcIpBlockMstVo);
			
			/** UI 상 APPEND 목록과 Range 체크 **/
			List<TbIpBlockMstVo> destIpBlockMstVos = tbIpBlockMstComplexVo.getDestIpBlockMstVos();
			if (destIpBlockMstVos != null && destIpBlockMstVos.size() > 0) {
				for (TbIpBlockMstVo destIpBlockMstVo : destIpBlockMstVos) {
					ipCommonService.setBaseIpBlockMstInfo(destIpBlockMstVo);
					if (srcIpBlockMstVo.getSipVersionTypeCd().equals(destIpBlockMstVo.getSipVersionTypeCd()) 
						&& !ipCommonService.checkIpRange(srcIpBlockMstVo, destIpBlockMstVo)) {
						throw new ServiceException("APP.INFO.00019", new String[]{srcIpBlockMstVo.getPipPrefix()});
					}
				}
			}
			
			/** 예약 블록 정보 체크 : 사설이 아닐 경우 체크.**/
			/* 기존 사설(CT0004) 은 유/무선공용으로 사용, 신규 사설(CT0005) 을 사설로 사용  */
			if(!(srcIpBlockMstVo.getSipCreateTypeCd().equals("CT0005") || srcIpBlockMstVo.getSipCreateTypeCd().equals("CT0004"))){ //사설블록
				int iChekcCnt = createMgmtTxService.checkCountIPBlockMst(srcIpBlockMstVo);
				if ( iChekcCnt > 0 ){
					throw new ServiceException("APP.HIGH.00048", new String[]{srcIpBlockMstVo.getPipPrefix()});
				}
			}
			
			/** DB 에 등록될 수 있는 여부 체크 
			 * 
			 * 1. 공인 : 계위 상관 없이 IP 중복 불가
				2. 유/무선공용 : 서비스망(1계위)별로 IP 중복 가능
				3. 사설 : 국사(3계위)별로 IP 중복 가능 
				4. 공인, 유/무선공용 IP 중복 불가
			 * 
			 * */
			if((srcIpBlockMstVo.getSipCreateTypeCd().equals("CT0005") || srcIpBlockMstVo.getSipCreateTypeCd().equals("CT0004"))){ // 사설
				int cnt = createMgmtTxService.countDuplicateTbIpBlockMstVo2(srcIpBlockMstVo);
				if (cnt > 0) {
					throw new ServiceException("APP.INFO.00018", new String[]{srcIpBlockMstVo.getPipPrefix()});
				}
			} else {

				/* 공인, 유/무선공용 IP 중복 불가 체크 */
				TbIpBlockMstVo tempVo = new TbIpBlockMstVo();
				tempVo.setNfirstAddr(srcIpBlockMstVo.getNfirstAddr());
				tempVo.setNlastAddr(srcIpBlockMstVo.getNlastAddr());
				tempVo.setSipVersionTypeCd(srcIpBlockMstVo.getSipVersionTypeCd());
				tempVo.setSipCreateTypeCd(null);
				int cnt2 = createMgmtTxService.countDuplicateTbIpBlockMstVo(tempVo);
				
				TbIpBlockMstVo tempVo2 = new TbIpBlockMstVo();
				tempVo2.setNfirstAddr(srcIpBlockMstVo.getNfirstAddr());
				tempVo2.setNlastAddr(srcIpBlockMstVo.getNlastAddr());
				tempVo2.setSipVersionTypeCd(srcIpBlockMstVo.getSipVersionTypeCd());
				tempVo2.setSipCreateTypeCd("CT0004X");
				int cnt3 = createMgmtTxService.countDuplicateTbIpBlockMstVo(tempVo2);
				
				int cnt = createMgmtTxService.countDuplicateTbIpBlockMstVo(srcIpBlockMstVo);
				
				if("CT0004".equals(srcIpBlockMstVo.getSipCreateTypeCd())) {
					if (cnt3 > 0 || cnt > 0) { 
						throw new ServiceException("APP.INFO.00018", new String[]{srcIpBlockMstVo.getPipPrefix()});
					}	
				} else {
					if (cnt2 > 0) {
						throw new ServiceException("APP.INFO.00018", new String[]{srcIpBlockMstVo.getPipPrefix()});
					}
				}
				
			}
			
			resultVo = srcIpBlockMstVo;
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
	 * IP 블럭 생성 목록 등록
	 * @param tbIpBlockMstListVo
	 * @return
	 * @throws Exception
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public void insertListIpBlockMst(TbIpBlockMstListVo tbIpBlockMstListVo, BigInteger nlvlMstSeq) {
		try {
			if (tbIpBlockMstListVo == null || tbIpBlockMstListVo.getTbIpBlockMstVos() == null
				|| tbIpBlockMstListVo.getTbIpBlockMstVos().size() == 0 || nlvlMstSeq == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			
			List<TbIpBlockMstVo> tbIpBlockMstVos = tbIpBlockMstListVo.getTbIpBlockMstVos();
			String sipCreateSeqCd = null;
			if (tbIpBlockMstVos != null && tbIpBlockMstVos.size() > 0) {
				TbIpBlockMstVo tbIpBlockMstVo = tbIpBlockMstVos.get(0);
				if (tbIpBlockMstVo.getSipCreateSeqCd() == null || tbIpBlockMstVo.getSipCreateSeqCd().equals("") || tbIpBlockMstVo.getSipCreateSeqCd().equals("&nbsp;")) {
					sipCreateSeqCd = createMgmtTxService.selectNewSipCreateSeqCd(tbIpBlockMstVo);
					for (TbIpBlockMstVo itemVo : tbIpBlockMstVos) {
						itemVo.setSipCreateSeqCd(sipCreateSeqCd);
					}
				}
			}
			for (TbIpBlockMstVo tbIpBlockMstVo : tbIpBlockMstVos) {
				/** IP블럭 기본 정보 **/
				ipCommonService.setBaseIpBlockMstInfo(tbIpBlockMstVo);
				tbIpBlockMstVo.setNfreeIpCnt(tbIpBlockMstVo.getNcnt());
				tbIpBlockMstVo.setNuseIpCnt(BigInteger.ZERO);
				/** 예약 블록 정보 체크 : 사설이 아닐 경우 체크.**/
				/* 기존 사설(CT0004) 은 유/무선공용으로 사용, 신규 사설(CT0005) 을 사설로 사용  */
				if(!(tbIpBlockMstVo.getSipCreateTypeCd().equals("CT0005") || tbIpBlockMstVo.getSipCreateTypeCd().equals("CT0004"))){ //사설블록
					
					int iChekcCnt = createMgmtTxService.checkCountIPBlockMst(tbIpBlockMstVo);
					
					if ( iChekcCnt > 0 ){
						throw new ServiceException("CMN.HIGH.00020", new String[]{"(원인 : 해당 블록은 사설로 예약된 영역입니다.) IP블록 생성 등록"});
					}
				}
				
				
				/** DB 에 등록될 수 있는 여부 체크 
				 * 
				 * 1. 공인 : 계위 상관 없이 IP 중복 불가
					2. 유/무선공용 : 서비스망(1계위)별로 IP 중복 가능
					3. 사설 : 국사(3계위)별로 IP 중복 가능 
					4. 공인, 유/무선공용 IP 중복 불가
				 * 
				 * */
				if((tbIpBlockMstVo.getSipCreateTypeCd().equals("CT0005") || tbIpBlockMstVo.getSipCreateTypeCd().equals("CT0004"))){ // 사설
					int cnt = createMgmtTxService.countDuplicateTbIpBlockMstVo2(tbIpBlockMstVo);
					if (cnt > 0) {
						throw new ServiceException("APP.INFO.00018", new String[]{tbIpBlockMstVo.getPipPrefix()});
					}
				} else {

					/* 공인, 유/무선공용 IP 중복 불가 체크 */
					TbIpBlockMstVo tempVo = new TbIpBlockMstVo();
					tempVo.setNfirstAddr(tbIpBlockMstVo.getNfirstAddr());
					tempVo.setNlastAddr(tbIpBlockMstVo.getNlastAddr());
					tempVo.setSipVersionTypeCd(tbIpBlockMstVo.getSipVersionTypeCd());
					tempVo.setSipCreateTypeCd(null);
					int cnt2 = createMgmtTxService.countDuplicateTbIpBlockMstVo(tempVo);
					
					TbIpBlockMstVo tempVo2 = new TbIpBlockMstVo();
					tempVo2.setNfirstAddr(tbIpBlockMstVo.getNfirstAddr());
					tempVo2.setNlastAddr(tbIpBlockMstVo.getNlastAddr());
					tempVo2.setSipVersionTypeCd(tbIpBlockMstVo.getSipVersionTypeCd());
					tempVo2.setSipCreateTypeCd("CT0004X");
					int cnt3 = createMgmtTxService.countDuplicateTbIpBlockMstVo(tempVo2);
					
					int cnt = createMgmtTxService.countDuplicateTbIpBlockMstVo(tbIpBlockMstVo);
					
					
					if("CT0004".equals(tbIpBlockMstVo.getSipCreateTypeCd())) {
						if (cnt3 > 0 || cnt > 0) { 
							throw new ServiceException("APP.INFO.00018", new String[]{tbIpBlockMstVo.getPipPrefix()});
						}	
					} else {
						if (cnt2 > 0) {
							throw new ServiceException("APP.INFO.00018", new String[]{tbIpBlockMstVo.getPipPrefix()});
						}
					}
					
				}
				
				
				/** IP블럭 등록 **/
				tbIpBlockMstVo.setsMenuType(tbIpBlockMstListVo.getsMenuType());
				createMgmtTxService.processInsertIpBlockMst(tbIpBlockMstVo, nlvlMstSeq);
			}
			
		} catch (ServiceException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("CMN.HIGH.00020", new String[]{"IP블록 생성 등록"});
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public TbIpBlockMstVo deleteIpBlockMst(TbIpBlockMstVo tbIpBlockMstVo) {
		TbIpBlockMstVo resultVo = null;
		try {
			int totalCount = createMgmtTxService.countAssignBlockViaIpAssignMstVo(tbIpBlockMstVo);
			if (totalCount > 0) {
				throw new ServiceException("APP.INFO.00024");
			}
			createMgmtTxService.processDeleteIpBlockMst(tbIpBlockMstVo);
			resultVo = tbIpBlockMstVo;
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00022", new String[]{"해당 IP블록"});
		}
		return resultVo;
	}
		
	@Transactional(readOnly = true)
	public List<String> selectListSipCreateSeqCd(String searchSipCreateSeqCd) {
		List<String> resultList = null;
		try {
			resultList = createMgmtTxService.selectListSipCreateSeqCd(searchSipCreateSeqCd);
		} catch (Exception e) {
			resultList = new ArrayList<String>();
		}
		return resultList;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public TbIpBlockMstVo updateIpBlockMst(TbIpBlockMstVo tbIpBlockMstVo) {
		TbIpBlockMstVo resultVo = null;
		try {
			List<String> sipCreateSeqCds = createMgmtTxService.selectListSipCreateSeqCd(tbIpBlockMstVo.getSipCreateSeqCd());
			if (sipCreateSeqCds == null || sipCreateSeqCds.size() == 0) {
				throw new ServiceException("APP.INFO.00025", new String[]{tbIpBlockMstVo.getSipCreateSeqCd()}); // 차수 정보가 없음
			}
			createMgmtTxService.processUpdateIpBlockMst(tbIpBlockMstVo);
			resultVo = tbIpBlockMstVo;
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00021", new String[]{"해당 IP블록"});
		}
		return resultVo;
	}
	
	
}
