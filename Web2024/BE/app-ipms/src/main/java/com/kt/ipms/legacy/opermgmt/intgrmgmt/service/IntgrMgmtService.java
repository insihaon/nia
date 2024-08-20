package com.kt.ipms.legacy.opermgmt.intgrmgmt.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import com.kt.framework.exception.ServiceException;
import com.kt.ipms.legacy.cmn.service.ConfigPropertieService;
import com.kt.ipms.legacy.cmn.util.CommonCodeUtil;
import com.kt.ipms.legacy.cmn.util.PrintLogUtil;
import com.kt.ipms.legacy.ipmgmt.routmgmt.vo.TbRoutChkMstVo;
import com.kt.ipms.legacy.opermgmt.intgrmgmt.vo.TbDefaultSvcMstListVo;
import com.kt.ipms.legacy.opermgmt.intgrmgmt.vo.TbDefaultSvcMstVo;
import com.kt.ipms.legacy.opermgmt.intgrmgmt.vo.TbFcltCmdMstListVo;
import com.kt.ipms.legacy.opermgmt.intgrmgmt.vo.TbFcltCmdMstVo;
import com.kt.ipms.legacy.opermgmt.intgrmgmt.vo.TbFcltMstListVo;
import com.kt.ipms.legacy.opermgmt.intgrmgmt.vo.TbFcltMstVo;
import com.kt.ipms.legacy.opermgmt.intgrmgmt.vo.TbMobileMstListVo;
import com.kt.ipms.legacy.opermgmt.intgrmgmt.vo.TbMobileMstVo;
import com.kt.ipms.legacy.opermgmt.intgrmgmt.vo.TbMobileSummMstListVo;
import com.kt.ipms.legacy.opermgmt.intgrmgmt.vo.TbMobileSummMstVo;
import com.kt.ipms.legacy.opermgmt.intgrmgmt.vo.TbRoutHistMstListVo;
import com.kt.ipms.legacy.opermgmt.intgrmgmt.vo.TbRoutHistMstVo;
import com.kt.ipms.legacy.opermgmt.intgrmgmt.vo.TbWireMstListVo;
import com.kt.ipms.legacy.opermgmt.intgrmgmt.vo.TbWireMstVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlBasVo;
import com.kt.ipms.legacy.opermgmt.tacsmgmt.vo.TbTacsConnHistListVo;
import com.kt.ipms.legacy.opermgmt.tacsmgmt.vo.TbTacsConnHistVo;
import com.kt.ipms.legacy.opermgmt.whoismgmt.vo.TbWhoisVo;
import com.kt.log4kt.utils.StringUtil;

@Component
public class IntgrMgmtService {

	@Autowired
	private IntgrMgmtTxService intgrMgmtTxService;
	
	@Autowired
	private ConfigPropertieService configPropertieService;
	
	/****************************************************************************************
	 * 조직별 장비 정보관리
	 ****************************************************************************************/
	
	/**
	 * 조직별 장비 정보관리 > 조회
	 * @param tbFcltMstVo
	 * @return
	 */
	@Transactional(readOnly = true)
	public TbFcltMstListVo selectListFcltMst(TbFcltMstVo tbFcltMstVo) {
		TbFcltMstListVo resultListVo = null;
		try {
			if (tbFcltMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			List<TbFcltMstVo> resultList = intgrMgmtTxService.selectListPageFcltMstVo(tbFcltMstVo);
			int totalCount = intgrMgmtTxService.countListPageFcltMstVo(tbFcltMstVo);
			resultListVo = new TbFcltMstListVo();
			resultListVo.setTbFcltMstVos(resultList);
			resultListVo.setTotalCount(totalCount);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"조직별장비목록"});
		}
		return resultListVo;
	}
	
	/**
	 * 조직별 장비 정보관리 > 등록
	 * @param tbFcltMstVo
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void insertFcltMst(TbFcltMstVo tbFcltMstVo) {
		try {
			if (tbFcltMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			TbLvlBasVo searchLvlBasVo = new TbLvlBasVo();
			searchLvlBasVo.setSsvcLineTypeCd(tbFcltMstVo.getSsvcLineTypeCd());
			searchLvlBasVo.setSsvcGroupCd(tbFcltMstVo.getSsvcGroupCd());
			searchLvlBasVo.setSsvcObjCd(tbFcltMstVo.getSsvcObjCd());
			TbLvlBasVo resultLvlBasVo = intgrMgmtTxService.selectTbLvlBas(searchLvlBasVo);
			if (resultLvlBasVo == null) {
				throw new ServiceException("CMN.HIGH.00023", new String[]{"계위 정보"});
			}
			tbFcltMstVo.setNlvlBasSeq(resultLvlBasVo.getNlvlBasSeq());
			TbFcltMstVo searchVo = new TbFcltMstVo();
			searchVo.setNlvlBasSeq(tbFcltMstVo.getNlvlBasSeq());
			List<TbFcltMstVo> resultList = intgrMgmtTxService.selectListFcltMstVo(searchVo);
			for (TbFcltMstVo item : resultList) {
				if (item.getPtelnetIp().equals(tbFcltMstVo.getPtelnetIp())) {
					throw new ServiceException("CMN.HIGH.00049", new String[]{"조직 장비","해당 조직에 등록된 장비IP"});
				}
			}
			
			intgrMgmtTxService.insertFcltMstVo(tbFcltMstVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00020", new String[]{"조직 장비"});
		}
	}
	
	/**
	 * 조직별 장비 정보관리 > 상세조회
	 * @param tbFcltMstVo
	 * @return
	 */
	@Transactional(readOnly = true)
	public TbFcltMstVo selectFcltMst(TbFcltMstVo tbFcltMstVo) {
		TbFcltMstVo resultVo = null;
		try {
			if (tbFcltMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			resultVo = intgrMgmtTxService.selectFcltMstVo(tbFcltMstVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"조직 장비"});
		}
		return resultVo;
	}
	
	/**
	 * 조직별 장비 정보관리 > 저장
	 * @param tbFcltMstVo
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void updateFcltMst(TbFcltMstVo tbFcltMstVo) {
		try {
			if (tbFcltMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			TbFcltMstVo searchVo = new TbFcltMstVo();
			searchVo.setNlvlBasSeq(tbFcltMstVo.getNlvlBasSeq());
			List<TbFcltMstVo> resultList = intgrMgmtTxService.selectListFcltMstVo(searchVo);
			if (tbFcltMstVo.getNfcltMstSeq() != null) {
				for (TbFcltMstVo item : resultList) {
					if (item.getNfcltMstSeq().compareTo(tbFcltMstVo.getNfcltMstSeq()) != 0 
						&& item.getPtelnetIp().equals(tbFcltMstVo.getPtelnetIp())) {
						throw new ServiceException("CMN.HIGH.00049", new String[]{"조직 장비","해당 조직에 등록된 장비IP"});
					}
				}
				intgrMgmtTxService.updateFcltMstVo(tbFcltMstVo);
			} else {
				for (TbFcltMstVo item : resultList) {
					if (item.getPtelnetIp().equals(tbFcltMstVo.getPtelnetIp())) {
						throw new ServiceException("CMN.HIGH.00049", new String[]{"조직 장비","해당 조직에 등록된 장비IP"});
					}
				}
				
				tbFcltMstVo.setScreateId(tbFcltMstVo.getSmodifyId());
				intgrMgmtTxService.insertFcltMstVo(tbFcltMstVo);
			}
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00021", new String[]{"조직 장비"});
		}
	}
	
	/**
	 * 조직별 장비 정보관리 > 삭제
	 * @param tbFcltCmdMstVo
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteFcltMst(TbFcltMstVo tbFcltMstVo) {
		
		List<BigInteger> delList = tbFcltMstVo.getDelList();
		try {

			if (tbFcltMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			
			for (BigInteger seq : delList) {
				tbFcltMstVo.setNfcltMstSeq(seq);
				intgrMgmtTxService.deleteFcltMstVo(tbFcltMstVo);
			}
			
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00020", new String[]{"조직 장비"});
		}
	}
	
	/**
	 * 조직별 장비 정보관리 > 엑셀 다운로드
	 * @param tbFcltMstVo
	 * @return
	 */
	public TbFcltMstListVo selectListFcltMstExcel (TbFcltMstVo tbFcltMstVo) {
		
		TbFcltMstListVo resultListVo = null;
		
		try {
			if (tbFcltMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			List<TbFcltMstVo> resultList = null; 
			int totalCount = intgrMgmtTxService.countListPageFcltMstVo(tbFcltMstVo);
			if (totalCount > CommonCodeUtil.MAX_EXCEL_ROW_COUNT) {
				throw new ServiceException("CMN.INFO.00051");
			}
			
			if(totalCount > 0){
				tbFcltMstVo.setFirstIndex(1);
				tbFcltMstVo.setLastIndex(totalCount);
				resultList = intgrMgmtTxService.selectListPageFcltMstVo(tbFcltMstVo);
			}

			resultListVo = new TbFcltMstListVo();
			resultListVo.setTbFcltMstVos(resultList);
			resultListVo.setTotalCount(totalCount);
			
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"조직별 장비 정보관리 내역 엑셀저장"});
		}
		return resultListVo;
		
	}
	
	/****************************************************************************************
	 * 장비별 명령어 정보관리
	 ****************************************************************************************/
	
	/**
	 * 장비별 명령어 정보관리 > 조회
	 * @param tbFcltCmdMstVo
	 * @return
	 */
	@Transactional(readOnly = true)
	public TbFcltCmdMstListVo selectListFcltCmdMst(TbFcltCmdMstVo tbFcltCmdMstVo) {
		TbFcltCmdMstListVo resultListVo = null;
		try {
			if (tbFcltCmdMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			List<TbFcltCmdMstVo> resultList = intgrMgmtTxService.selectListPageFcltCmdMstVo(tbFcltCmdMstVo);
			int totalCount = intgrMgmtTxService.countListPageFcltCmdMstVo(tbFcltCmdMstVo);
			resultListVo = new TbFcltCmdMstListVo();
			resultListVo.setTbFcltCmdMstVos(resultList);
			resultListVo.setTotalCount(totalCount);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"장비별명령어목록"});
		}
		return resultListVo;
		
	}
	
	/**
	 * 장비별 명령어 정보관리 > 등록
	 * @param tbFcltCmdMstVo
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void insertFcltCmdMst(TbFcltCmdMstVo tbFcltCmdMstVo) {
		try {
			if (tbFcltCmdMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			TbFcltCmdMstVo searchVo = new TbFcltCmdMstVo();
			searchVo.setSfcltType(tbFcltCmdMstVo.getSfcltType());
			List<TbFcltCmdMstVo> resultList = intgrMgmtTxService.selectListFcltCmdMstVo(searchVo);
			int npriority = 0;
			for (TbFcltCmdMstVo item : resultList) {
				/*if (item.getNpriority() > npriority) {
					npriority = item.getNpriority();
				}*/
				if (item.getNpriority().equals(tbFcltCmdMstVo.getNpriority())) {
					throw new ServiceException("CMN.HIGH.00049", new String[]{"장비명령어","해당 장비에 등록된 명령어 순서"});
				}
				
				if (item.getSfcltCmd().equals(tbFcltCmdMstVo.getSfcltCmd())) {
					throw new ServiceException("CMN.HIGH.00049", new String[]{"장비명령어","해당 장비에 등록된 명령어"});
				}
			}
			//tbFcltCmdMstVo.setNpriority(npriority + 1);
			intgrMgmtTxService.insertFcltCmdMstVo(tbFcltCmdMstVo);
		} catch (ServiceException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00020", new String[]{"장비명령어"});
		}
	}
	
	/**
	 * 장비별 명령어 정보관리 > 상세조회
	 * @param tbFcltCmdMstVo
	 * @return
	 */
	@Transactional(readOnly = true)
	public TbFcltCmdMstVo selectFcltCmdMst(TbFcltCmdMstVo tbFcltCmdMstVo) {
		TbFcltCmdMstVo resultVo = null;
		try {
			if (tbFcltCmdMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			resultVo = intgrMgmtTxService.selectFcltCmdMstVo(tbFcltCmdMstVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"장비명령어"});
		}
		return resultVo;
	}
	
	/**
	 * 장비별 명령어 정보관리 > 수정
	 * @param tbFcltCmdMstVo
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void updateFcltCmdMst(TbFcltCmdMstVo tbFcltCmdMstVo) {
		try {
			if (tbFcltCmdMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			TbFcltCmdMstVo searchVo = new TbFcltCmdMstVo();
			searchVo.setSfcltType(tbFcltCmdMstVo.getSfcltType());
			List<TbFcltCmdMstVo> resultList = intgrMgmtTxService.selectListFcltCmdMstVo(searchVo);
			for (TbFcltCmdMstVo item : resultList) {
				if (item.getNfcltCmdMstSeq().compareTo(tbFcltCmdMstVo.getNfcltCmdMstSeq()) != 0 
					&& item.getSfcltCmd().equals(tbFcltCmdMstVo.getSfcltCmd())) {
					throw new ServiceException("CMN.HIGH.00049", new String[]{"장비명령어","해당 장비에 등록된 명령어"});
				}
				if (item.getNfcltCmdMstSeq().compareTo(tbFcltCmdMstVo.getNfcltCmdMstSeq()) != 0 
					&& item.getNpriority().equals(tbFcltCmdMstVo.getNpriority())) {
					throw new ServiceException("CMN.HIGH.00049", new String[]{"장비명령어","해당 장비에 등록된 명령어 순서"});
				}
			}
			intgrMgmtTxService.updateFcltCmdMstVo(tbFcltCmdMstVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"장비명령어"});
		}
	}
	
	/**
	 * 장비별 명령어 정보관리 > 삭제
	 * @param tbFcltCmdMstVo
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteFcltCmdMst(TbFcltCmdMstVo tbFcltCmdMstVo) {
		
		List<BigInteger> delList = tbFcltCmdMstVo.getDelList();
		try {

			if (tbFcltCmdMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			
			for (BigInteger seq : delList) {
				tbFcltCmdMstVo.setNfcltCmdMstSeq(seq);
				intgrMgmtTxService.deleteFcltCmdMstVo(tbFcltCmdMstVo);
			}
			
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00020", new String[]{"장비명령어"});
		}
	}
	
	/**
	 * 장비별 명령어 정보관리 > 엑셀 다운로드
	 * @param tbFcltMstVo
	 * @return
	 */
	public TbFcltCmdMstListVo selectListFcltCmdMstExcel (TbFcltCmdMstVo tbFcltCmdMstVo) {
		
		TbFcltCmdMstListVo resultListVo = null;
		
		try {
			if (tbFcltCmdMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			List<TbFcltCmdMstVo> resultList = null; 
			int totalCount = intgrMgmtTxService.countListPageFcltCmdMstVo(tbFcltCmdMstVo);
			if (totalCount > CommonCodeUtil.MAX_EXCEL_ROW_COUNT) {
				throw new ServiceException("CMN.INFO.00051");
			}
			
			if(totalCount > 0){
				tbFcltCmdMstVo.setFirstIndex(1);
				tbFcltCmdMstVo.setLastIndex(totalCount);
				resultList = intgrMgmtTxService.selectListPageFcltCmdMstVo(tbFcltCmdMstVo);
			}

			resultListVo = new TbFcltCmdMstListVo();
			resultListVo.setTbFcltCmdMstVos(resultList);
			resultListVo.setTotalCount(totalCount);
			
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"장비별 명령어 정보관리 내역 엑셀저장"});
		}
		return resultListVo;
		
	}
	
	/****************************************************************************************
	 * 무선IP 사전 정보관리
	 ****************************************************************************************/
	
	/**
	 * 무선IP 사전 정보관리 > 조회
	 * @param tbMobileMstVo
	 * @return
	 */
	@Transactional(readOnly = true)
	public TbMobileMstListVo selectListMobileMst(TbMobileMstVo tbMobileMstVo) {
		TbMobileMstListVo resultListVo = null;
		try {
			if (tbMobileMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			
			List<TbMobileMstVo> resultList = intgrMgmtTxService.selectListPageMobileMstVo(tbMobileMstVo);
			int totalCount = intgrMgmtTxService.countListPageMobileMstVo(tbMobileMstVo);
			resultListVo = new TbMobileMstListVo();
			resultListVo.setTbMobileMstVos(resultList);
			resultListVo.setTotalCount(totalCount);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"무선IP사전정보목록"});
		}
		return resultListVo;
	}

	/**
	 * 무선IP 사전 정보관리 > 등록
	 * @param tbMobileMstVo
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void insertMobileMst(TbMobileMstVo tbMobileMstVo) {
		try {
			if (tbMobileMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			
			TbMobileMstVo searchVo = new TbMobileMstVo();
			searchVo.setSkindCd(tbMobileMstVo.getSkindCd());
			if (tbMobileMstVo.getNmobileIpCommuSeq() != null) {
				searchVo.setNmobileIpCommuSeq(tbMobileMstVo.getNmobileIpCommuSeq());
			}
			
			
			if (searchVo.getSkindCd().equals("COMMU")) {
				searchVo.setScommu(tbMobileMstVo.getScommu());
				searchVo.setPipPrefix(null);
			}
			
			if (searchVo.getSkindCd().equals("NOCOMMU")) {
				searchVo.setPipPrefix(tbMobileMstVo.getPipPrefix());
				searchVo.setScommu(null);
			}
			
			List<TbMobileMstVo> resultList = intgrMgmtTxService.selectListMobileMstVo(searchVo);
			
			for (TbMobileMstVo item : resultList) {
				
				if (item.getSkindCd().equals("COMMU")) {
					if (item.getScommu().equals(tbMobileMstVo.getScommu())) {
						throw new ServiceException("CMN.HIGH.00049", new String[]{"Community 정보","이미 등록된 Community 정보"});
					}
				}
				
				if (item.getSkindCd().equals("NOCOMMU")) {
					if (item.getPipPrefix().equals(tbMobileMstVo.getPipPrefix())) {
						throw new ServiceException("CMN.HIGH.00049", new String[]{"IP블록 정보","이미 등록된 IP블록 정보"});
					}
				}
			}
	
			
			if (tbMobileMstVo.getSkindCd().equals("COMMU")) {
				tbMobileMstVo.setPipPrefix(null);
			}
			
			if (tbMobileMstVo.getSkindCd().equals("NOCOMMU")) {
				tbMobileMstVo.setScommu(null);
			}
			
			intgrMgmtTxService.insertMobileMstVo(tbMobileMstVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00020", new String[]{"무선IP사전정보"});
		}
	}

	/**
	 * 무선IP 사전 정보관리 > 상세조회
	 * @param tbMobileMstVo
	 * @return
	 */
	@Transactional(readOnly = true)
	public TbMobileMstVo selectMobileMst(TbMobileMstVo tbMobileMstVo) {
		TbMobileMstVo resultVo = null;
		try {
			if (tbMobileMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			
			resultVo = intgrMgmtTxService.selectMobileMstVo(tbMobileMstVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"무선IP사전정보"});
		}
		return resultVo;
	}
		
	/**
	 * 무선IP 사전 정보관리 > 수정
	 * @param tbMobileMstVo
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void updateMobileMst(TbMobileMstVo tbMobileMstVo) {
		try {
			if (tbMobileMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			
			/*TbMobileMstVo searchVo = new TbMobileMstVo();
			searchVo.setSkindCd(tbMobileMstVo.getSkindCd());
			searchVo.setNmobileIpCommuSeq(tbMobileMstVo.getNmobileIpCommuSeq());
			
			if (searchVo.getSkindCd().equals("COMMU")) {
				searchVo.setScommu(tbMobileMstVo.getScommu());
				searchVo.setPipPrefix(null);
			}
			
			if (searchVo.getSkindCd().equals("NOCOMMU")) {
				searchVo.setPipPrefix(tbMobileMstVo.getPipPrefix());
				searchVo.setScommu(null);
			}
			
			List<TbMobileMstVo> resultList = intgrMgmtTxService.selectListMobileMstVo(searchVo);
			
			for (TbMobileMstVo item : resultList) {
				
				if (item.getSkindCd().equals("COMMU")) {
					if (item.getScommu().equals(tbMobileMstVo.getScommu())) {
						throw new ServiceException("CMN.HIGH.00049", new String[]{"Community 정보","이미 등록된 Community 정보"});
					}
				}
				
				if (item.getSkindCd().equals("NOCOMMU")) {
					if (item.getPipPrefix().equals(tbMobileMstVo.getPipPrefix())) {
						throw new ServiceException("CMN.HIGH.00049", new String[]{"IP블록 정보","이미 등록된 IP블록 정보"});
					}
				}
			}*/
			
			if (tbMobileMstVo.getSkindCd().equals("COMMU")) {
				tbMobileMstVo.setPipPrefix(null);
			}
			
			if (tbMobileMstVo.getSkindCd().equals("NOCOMMU")) {
				tbMobileMstVo.setScommu(null);
			}
			
			intgrMgmtTxService.updateMobileMstVo(tbMobileMstVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"무선IP사전정보"});
		}
	}
	 
	 
	/**
	 * 무선IP 사전 정보관리 > 삭제
	 * @param tbFcltCmdMstVo
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteMobileMst(TbMobileMstVo tbMobileMstVo) {
		
		List<BigInteger> delList = tbMobileMstVo.getDelList();
		try {

			if (tbMobileMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			
			for (BigInteger seq : delList) {
				tbMobileMstVo.setNmobileIpCommuSeq(seq);
				intgrMgmtTxService.deleteMobileMstVo(tbMobileMstVo);
			}
			
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00020", new String[]{"무선IP사전정보"});
		}
	}
	 
	 /**
	 * 무선IP 사전 정보관리 > 엑셀 다운로드
	 * @param tbFcltMstVo
	 * @return
	 */
	public TbMobileMstListVo selectListMobileMstExcel (TbMobileMstVo tbMobileMstVo) {
		
		TbMobileMstListVo resultListVo = null;
		
		try {
			if (tbMobileMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			List<TbMobileMstVo> resultList = null; 
			int totalCount = intgrMgmtTxService.countListPageMobileMstVo(tbMobileMstVo);
			if (totalCount > CommonCodeUtil.MAX_EXCEL_ROW_COUNT) {
				throw new ServiceException("CMN.INFO.00051");
			}
			
			if(totalCount > 0){
				tbMobileMstVo.setFirstIndex(1);
				tbMobileMstVo.setLastIndex(totalCount);
				resultList = intgrMgmtTxService.selectListPageMobileMstVo(tbMobileMstVo);
			}

			resultListVo = new TbMobileMstListVo();
			resultListVo.setTbMobileMstVos(resultList);
			resultListVo.setTotalCount(totalCount);
			
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"무선IP사전정보관리 내역 엑셀저장"});
		}
		return resultListVo;
		
	}
	
	/**
	 * 무선 전체 라우팅 수집
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
			
			// Make Json Data
			ObjectMapper oReq = new ObjectMapper();
			Map<String,String> mapReq = new HashMap<String,String>();
			String reqData = "";
			
			reqData = oReq.writerWithDefaultPrettyPrinter().writeValueAsString(mapReq);
			
			// Request
			String requestUrl = "http://147.6.97.116/batch/mobiledailybatch";
			int timeout = configPropertieService.getInt("IpmsCheck.timeout");
			
			URL url = new URL(requestUrl);
			
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
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
	
	/****************************************************************************************
	 * 무선IP 사전 정보관리 > Summary 등록
	 ****************************************************************************************/
	
	/**
	 * 무선IP 사전 정보관리 > Summary 조회
	 * @param tbMobileMstVo
	 * @return
	 */
	@Transactional(readOnly = true)
	public TbMobileSummMstListVo selectListMobileSummMst (TbMobileSummMstVo tbMobileSummMstVo) {
		TbMobileSummMstListVo resultListVo = null;
		try {
			if (tbMobileSummMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			List<TbMobileSummMstVo> resultList = intgrMgmtTxService.selectListPageMobileSummMstVo(tbMobileSummMstVo);
			
			int totalCount = intgrMgmtTxService.countListPageMobileSummMstVo(tbMobileSummMstVo);
			resultListVo = new TbMobileSummMstListVo();
			resultListVo.setTbMobileSummMstVos(resultList);
			resultListVo.setTotalCount(totalCount);

		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"무선IPSummary정보목록"});
		}
		return resultListVo;
	}

	/**
	 * 무선IP 사전 정보관리 > Summary 등록
	 * @param tbMobileMstVo
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void insertMobileSummMst(TbMobileSummMstVo tbMobileSummMstVo) {
		try {
			if (tbMobileSummMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			
			TbMobileSummMstVo searchVo = new TbMobileSummMstVo();
			searchVo.setSkindCd(tbMobileSummMstVo.getSkindCd());
			searchVo.setPipPrefix(tbMobileSummMstVo.getPipPrefix());
			
			List<TbMobileSummMstVo> resultList = intgrMgmtTxService.selectListMobileSummMstVo(searchVo);
			
			if(resultList.size() > 0) {
				for (TbMobileSummMstVo item : resultList) {
					if (item.getPipPrefix().equals(tbMobileSummMstVo.getPipPrefix())) {
						throw new ServiceException("CMN.HIGH.00049", new String[]{"IP블록 정보","이미 등록된 IP블록 정보"});
					}
				}
			}
			
			intgrMgmtTxService.insertMobileSummMstVo(tbMobileSummMstVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00020", new String[]{"무선IPSummary정보"});
		}
	}
	 
	/**
	 * 무선IP 사전 정보관리 > Summary 삭제
	 * @param tbFcltCmdMstVo
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteMobileSummMst(TbMobileSummMstVo tbMobileSummMstVo) {
		
		List<BigInteger> delList = tbMobileSummMstVo.getDelList();
		try {

			if (tbMobileSummMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			
			for (BigInteger seq : delList) {
				tbMobileSummMstVo.setNmobileIpSummSeq(seq);
				intgrMgmtTxService.deleteMobileSummMstVo(tbMobileSummMstVo);
			}
			
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00020", new String[]{"무선IPSummary정보"});
		}
	}
	
	/****************************************************************************************
	 * 무선IP 사전 정보관리 > 텍스트 파일 업로드
	 ****************************************************************************************/
	/**
	 * 무선IP 사전 정보관리 > 텍스트 파일 업로드
	 * @param file
	 * @param sKindCd
	 * @param userId
	 * @throws IOException 
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void uploadMobileMst(MultipartFile file, String sKindCd, String userId) throws IOException {
		String fileName = file.getOriginalFilename();
		/* Sparrow - PATH_TRAVERSAL Start */
		fileName = fileName.replaceAll("/","");
		fileName = fileName.replaceAll("\\\\","");
		fileName = fileName.replaceAll("&","");
		/* Sparrow - PATH_TRAVERSAL End */
		File convFile = new File(file.getOriginalFilename());
		BufferedReader bufReader = null;
		
		try {
			
			file.transferTo(convFile);
            bufReader = new BufferedReader(new InputStreamReader(new FileInputStream(convFile),"UTF-8"));
            String line = "";
            String lines = "";
            
            // 삭제 
            if(sKindCd.equals("COMMU") || sKindCd.equals("NOCOMMU")) {
            	TbMobileMstVo delVo = new TbMobileMstVo();
                delVo.setSkindCd(sKindCd);
    			intgrMgmtTxService.deleteMobileMstAllVo(delVo);
            }
            
            while((line = bufReader.readLine()) != null){
        		if(!line.toString().equals("")){
        			
        			String[] line_array = null;
        			
        			if (sKindCd.equals("COMMU")) {		            					// Community
        				
        				line_array = line.toString().split("\\,");
        				
        				TbMobileMstVo tbMobileMstVo = new TbMobileMstVo();
        				
        				if (line_array.length > 0) {
        					
        					if (line_array[0].lastIndexOf(":") == -1) {
        						throw new ServiceException("CMN.INFO.00054", new String[]{"파일 형식이 맞지 않습니다."});
        					}
        					
        					tbMobileMstVo.setSkindCd(sKindCd); // 구분코드
            				tbMobileMstVo.setSkindNm("Community"); // 구분명	
        					tbMobileMstVo.setScommu(line_array[0]);		//  Community
        					tbMobileMstVo.setPipPrefix(null);		//  IP 블록
        					tbMobileMstVo.setSserviceNm(line_array[1]); // 서비스 명
        					tbMobileMstVo.setScreateId(userId);
        					tbMobileMstVo.setSmodifyId(userId);
        					
        					intgrMgmtTxService.insertMobileMstVo(tbMobileMstVo);
        				}
        				
        			} else if (sKindCd.equals("NOCOMMU")) {		            			// No-Community
        				
        				line_array = line.toString().split("\\,");
        				
        				TbMobileMstVo tbMobileMstVo = new TbMobileMstVo();
        				
        				if (line_array.length > 0) {
        					
        					if (line_array[0].lastIndexOf("/") == -1) {
        						throw new ServiceException("CMN.INFO.00054", new String[]{"파일 형식이 맞지 않습니다."});
        					}
        					
        					line_array[0] = line_array[0].replaceAll(" ", "");
        					
        					tbMobileMstVo.setSkindCd(sKindCd); // 구분코드
            				tbMobileMstVo.setSkindNm("No-Community"); // 구분명	
            				tbMobileMstVo.setScommu(null);		//  Community
        					tbMobileMstVo.setPipPrefix(line_array[0]);		//  IP 블록
        					tbMobileMstVo.setSserviceNm(line_array[1]); // 서비스 명
        					tbMobileMstVo.setScreateId(userId);
        					tbMobileMstVo.setSmodifyId(userId);
        					
        					intgrMgmtTxService.insertMobileMstVo(tbMobileMstVo);
        					
        				}
        				
        			} else if (sKindCd.equals("SUMMARY")) {		            			// Summary
        			
        				if(!line.isEmpty()) {
        					lines += line.toString();	
        				}
        			
        			}
        			
        		}
            }            
            
            ArrayNode arr1 = null;
        	ArrayNode arr2 = null;
        	ArrayNode arr3 = null;
            
//            if ( lines != "" && sKindCd.equals("SUMMARY") ) { //Codeeyes-Urgent-객체 비교시 == , != 사용제한
        	if ( !lines.equals("") && sKindCd.equals("SUMMARY") ) {
            	
            	TbMobileSummMstVo tbMobileSummMstVo = new TbMobileSummMstVo();
            	intgrMgmtTxService.deleteMobileSummMstAllVo(tbMobileSummMstVo);
    			
            	ObjectMapper o = new ObjectMapper();
            	arr1 = (ArrayNode) o.readTree(lines).get("public");
            	arr2 = (ArrayNode) o.readTree(lines).get("private");
            	arr3 = (ArrayNode) o.readTree(lines).get("both");
            	
            	if (null == arr1 || null == arr2 || null == arr3) {
            		//if (arr1.isArray() || arr2.isArray() || arr3.isArray()) {
            			throw new ServiceException("CMN.INFO.00054", new String[]{"파일 형식이 맞지 않습니다."});
            		//}
            	} 
            	
            	for(JsonNode json1 : arr1) {
        			tbMobileSummMstVo.setSkindCd("public");
                	tbMobileSummMstVo.setSkindNm("public");
        			tbMobileSummMstVo.setPipPrefix(json1.toString().replaceAll("\\\"", ""));
        			tbMobileSummMstVo.setScreateId(userId);
        			tbMobileSummMstVo.setSmodifyId(userId);
        			
        			intgrMgmtTxService.insertMobileSummMstVo(tbMobileSummMstVo);
        		}
            	
            	for(JsonNode json2 : arr2) {
        			tbMobileSummMstVo.setSkindCd("private");
                	tbMobileSummMstVo.setSkindNm("private");
        			tbMobileSummMstVo.setPipPrefix(json2.toString().replaceAll("\\\"", ""));
        			tbMobileSummMstVo.setScreateId(userId);
        			tbMobileSummMstVo.setSmodifyId(userId);
        			
        			intgrMgmtTxService.insertMobileSummMstVo(tbMobileSummMstVo);
        		}
            	
            	for(JsonNode json3 : arr3) {
        			
        			tbMobileSummMstVo.setSkindCd("both");
                	tbMobileSummMstVo.setSkindNm("both");
        			tbMobileSummMstVo.setPipPrefix(json3.toString().replaceAll("\\\"", ""));
        			tbMobileSummMstVo.setScreateId(userId);
        			tbMobileSummMstVo.setSmodifyId(userId);
        			
        			intgrMgmtTxService.insertMobileSummMstVo(tbMobileSummMstVo);
        		}
            	
            }
			
		} catch (IllegalStateException e) {
			e.printStackTrace();
			throw new ServiceException("CMN.INFO.00054", new String[]{"파일 형식이 맞지 않습니다."});
		} catch (IOException e) {
			e.printStackTrace();
			throw new ServiceException("CMN.INFO.00054", new String[]{"파일 형식이 맞지 않습니다."});
		}catch (ServiceException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("CMN.HIGH.00020", new String[]{"무선IP사전정보"});
		} finally {
			 bufReader.close();
	         convFile.delete();
		}
	}
	
	/****************************************************************************************
	 * 서비스망별 서비스 정보관리
	 ****************************************************************************************/
	
	/**
	 * 서비스망별 서비스 정보관리 > 조회
	 * @param tbFcltMstVo
	 * @return
	 */
	@Transactional(readOnly = true)
	public TbDefaultSvcMstListVo selectListDefaultSvcMst(TbDefaultSvcMstVo tbDefaultSvcMstVo) {
		TbDefaultSvcMstListVo resultListVo = null;
		try {
			if (tbDefaultSvcMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			List<TbDefaultSvcMstVo> resultList = intgrMgmtTxService.selectListPageDefaultSvcMstVo(tbDefaultSvcMstVo);
			int totalCount = intgrMgmtTxService.countListPageDefaultSvcMstVo(tbDefaultSvcMstVo);
			resultListVo = new TbDefaultSvcMstListVo();
			resultListVo.setTbDefaultSvcMstVos(resultList);
			resultListVo.setTotalCount(totalCount);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"서비스망별서비스목록"});
		}
		return resultListVo;
	}
	
	/**
	 * 서비스망별 서비스 정보관리 > 등록
	 * @param tbFcltMstVo
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void insertDefaultSvcMst(TbDefaultSvcMstVo tbDefaultSvcMstVo) {
		try {
			if (tbDefaultSvcMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			
			TbDefaultSvcMstVo searchVo = new TbDefaultSvcMstVo();
			searchVo.setSsvcLineTypeCd(tbDefaultSvcMstVo.getSsvcLineTypeCd());
			List<TbDefaultSvcMstVo> resultList = intgrMgmtTxService.selectListPageDefaultSvcMstVo(searchVo);
			
			if(resultList.size() > 0) {
				for (TbDefaultSvcMstVo item : resultList) {
					if (item.getSsvcLineTypeCd().equals(tbDefaultSvcMstVo.getSsvcLineTypeCd())) {
						intgrMgmtTxService.updateDefaultSvcMstVo(tbDefaultSvcMstVo);
					} 
				}
			} else {
				intgrMgmtTxService.insertDefaultSvcMstVo(tbDefaultSvcMstVo);
			}
			
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00020", new String[]{"서비스망별서비스목록"});
		}
	}
	
	/****************************************************************************************
	 * 라우팅 연동 이력관리
	 ****************************************************************************************/
	/**
	 * 라우팅 연동 이력관리 > 목록 조회
	 * @param tbRoutHistMstVo
	 * @return
	 */
	@Transactional(readOnly = true)
	public TbRoutHistMstListVo selectListRoutHistMst(TbRoutHistMstVo tbRoutHistMstVo) {
		TbRoutHistMstListVo resultListVo = null;
		try {
			if (tbRoutHistMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			List<TbRoutHistMstVo> resultList = intgrMgmtTxService.selectListPageTbRoutHistVo(tbRoutHistMstVo);
			int totalCount = intgrMgmtTxService.countListTbRoutHistVo(tbRoutHistMstVo);
			resultListVo = new TbRoutHistMstListVo();
			resultListVo.setTbRoutHistMstVos(resultList);
			resultListVo.setTotalCount(totalCount);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"라우팅 연동 이력관리"});
		}
		return resultListVo;
	}
	
	/**
	 * 라우팅 연동 이력관리 > 수정
	 * @param tbRoutHistMstVo
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void updateRoutHistMst(TbRoutHistMstVo tbRoutHistMstVo) {
		try {
			if (tbRoutHistMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			
			intgrMgmtTxService.updateRoutHistMstVo(tbRoutHistMstVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"라우팅 연동"});
		}
	}

	/**
	 * 유선IP 사전 정보관리 > 목록
	 * @param tbWireMstVo
	 * @return
	 */
	public TbWireMstListVo selectListWireMst(TbWireMstVo tbWireMstVo) {
		TbWireMstListVo resultListVo = null;
		
		try {
			if (tbWireMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			
			List<TbWireMstVo> resultList = intgrMgmtTxService.selectListPageWireMstVo(tbWireMstVo);
			int totalCount = intgrMgmtTxService.countListPageWireMstVo(tbWireMstVo);
			resultListVo = new TbWireMstListVo();
			resultListVo.setTbWireMstVos(resultList);
			resultListVo.setTotalCount(totalCount);
		} catch (ServiceException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"무선IP사전정보목록"});
		}
		return resultListVo;
	}
	
	/**
	 * 유선IP 사전 정보관리 > 상세정보
	 * @param insertVo
	 */
	public TbWireMstVo selectWireMst(TbWireMstVo tbWireMstVo) {
		
		TbWireMstVo resultVo = null;
		try {
			if (tbWireMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			List<TbWireMstVo> resultList = (List<TbWireMstVo>) intgrMgmtTxService.selectWireMst(tbWireMstVo);
			resultVo = resultList.get(0);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"유선IP사전정보 "});
		}
		return resultVo;
	}

	/**
	 * 유선IP 사전 정보관리 > 등록
	 * @param insertVo
	 */
	public void insertWireMst(TbWireMstVo tbWireMstVo) {
		
		try {
			
			if (tbWireMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			
			TbLvlBasVo searchLvlBasVo = new TbLvlBasVo();
			searchLvlBasVo.setSsvcLineTypeCd(tbWireMstVo.getSsvcLineTypeCd());
			searchLvlBasVo.setSsvcGroupCd(tbWireMstVo.getSsvcGroupCd());
			searchLvlBasVo.setSsvcObjCd(tbWireMstVo.getSsvcObjCd());
			
			TbLvlBasVo resultLvlBasVo = intgrMgmtTxService.selectTbLvlBas(searchLvlBasVo);
			
			if (resultLvlBasVo == null) {
				throw new ServiceException("CMN.HIGH.00023", new String[]{"유선IP사전정보"});
			}
			
			tbWireMstVo.setNlvlBasSeq(resultLvlBasVo.getNlvlBasSeq());
			
			TbWireMstVo searchVo = new TbWireMstVo();
			searchVo.setNlvlBasSeq(tbWireMstVo.getNlvlBasSeq());
			List<TbWireMstVo> resultList = intgrMgmtTxService.selectListPageWireMstVo(tbWireMstVo);
			/**
			 * 중복 체크 로직 추가
			 */
			
			intgrMgmtTxService.insertWireMst(tbWireMstVo);
			
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00020", new String[]{"유선IP사전정보"});
		}
		
	}
	
	/**
	 * 유선IP 사전 정보관리 > 수정
	 * @param insertVo
	 */
	public void updateWireMst(TbWireMstVo tbWireMstVo) {
		
		try {
			
			if (tbWireMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
						
			List<TbWireMstVo> resultList = intgrMgmtTxService.selectListPageWireMstVo(tbWireMstVo);
			/**
			 * 중복 체크 로직 추가
			 */
						
			intgrMgmtTxService.updateWireMst(tbWireMstVo);
			
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00020", new String[]{"유선IP사전정보"});
		}
		
	}
	
	/**
	 * 유선IP 사전 정보관리 > 삭제
	 * @param insertVo
	 */
	public void deleteWireMst(TbWireMstVo tbWireMstVo) {
		
		try {
			
			if (tbWireMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}

			List<BigInteger> delList = tbWireMstVo.getDelList();
			for(int i=0;i<delList.size();i++){
				TbWireMstVo tempWireMstVo = new TbWireMstVo();
				tempWireMstVo.setNwireIpCommuSeq(delList.get(i));
				intgrMgmtTxService.deleteWireMst(tempWireMstVo);
			}
					
			
			
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00020", new String[]{"유선IP사전정보"});
		}
		
	}

	public TbWireMstListVo selectListWireMstExcel(TbWireMstVo tbWireMstVo) {
		TbWireMstListVo resultListVo = null;
		
		try {
			if (tbWireMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			
			List<TbWireMstVo> resultList = intgrMgmtTxService.selectListWireMstExcel(tbWireMstVo);
			resultListVo = new TbWireMstListVo();
			resultListVo.setTbWireMstVos(resultList);
		} catch (ServiceException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"무선IP사전정보목록"});
		}
		return resultListVo;
	}
	
	@Transactional(readOnly = true)
	public List<?> selectSresultMsg(){
		List<?> sresultMsg = intgrMgmtTxService.selectSresultMsg();
		return sresultMsg;
	}
	
	
}
