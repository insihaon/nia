package com.kt.ipms.legacy.opermgmt.tacsmgmt.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kt.framework.exception.ServiceException;
import com.kt.ipms.legacy.cmn.util.AESCryptoUtil;
import com.kt.ipms.legacy.cmn.util.PrintLogUtil;
import com.kt.ipms.legacy.ipmgmt.assignmgmt.vo.TbIpAssignMstVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.vo.TbLvlBasVo;
import com.kt.ipms.legacy.opermgmt.tacsmgmt.vo.TacsRequestCmdVo;
import com.kt.ipms.legacy.opermgmt.tacsmgmt.vo.TacsRequestVo;
import com.kt.ipms.legacy.opermgmt.tacsmgmt.vo.TacsRequstListVo;
import com.kt.ipms.legacy.opermgmt.tacsmgmt.vo.TacsResponseListVo;
import com.kt.ipms.legacy.opermgmt.tacsmgmt.vo.TacsResponseVo;
import com.kt.ipms.legacy.opermgmt.tacsmgmt.vo.TbTacsConnBasVo;
import com.kt.ipms.legacy.opermgmt.tacsmgmt.vo.TbTacsConnHistListVo;
import com.kt.ipms.legacy.opermgmt.tacsmgmt.vo.TbTacsConnHistVo;
import com.kt.ipms.legacy.opermgmt.tacsmgmt.vo.TbTacsFcltCmdMstListVo;
import com.kt.ipms.legacy.opermgmt.tacsmgmt.vo.TbTacsFcltCmdMstVo;
import com.kt.ipms.legacy.opermgmt.tacsmgmt.vo.TbTacsFcltMstListVo;
import com.kt.ipms.legacy.opermgmt.tacsmgmt.vo.TbTacsFcltMstVo;


@Component
public class TacsMgmtService {
	
	@Autowired
	private TacsMgmtTxService tacsMgmtTxService;
	
	@Autowired
	private AESCryptoUtil aesCryptoUtil;
	
	@Transactional(readOnly = true)
	public TbTacsConnBasVo selectTbTacsConnBas() {
		TbTacsConnBasVo resultVo = null;
		try {
			resultVo = tacsMgmtTxService.selectTbTacsConnBasVo();
			if (resultVo != null) {
				resultVo.setConnId(aesCryptoUtil.decrypt(resultVo.getConnId()));
				resultVo.setConnPw(aesCryptoUtil.decrypt(resultVo.getConnPw()));
			}
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"TACS연동정보"});
		}
		return resultVo;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void updateTbTacsConnBas(TbTacsConnBasVo tbTacsConnBasVo) {
		try {
			if (tbTacsConnBasVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			tbTacsConnBasVo.setConnId(aesCryptoUtil.encrypt(tbTacsConnBasVo.getConnId()));
			tbTacsConnBasVo.setConnPw(aesCryptoUtil.encrypt(tbTacsConnBasVo.getConnPw()));
			tacsMgmtTxService.updateTbTacsConnBasVo(tbTacsConnBasVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00021", new String[]{"TACS연동정보"});
		}
	}
	
	@Transactional(readOnly = true)
	public TbTacsFcltMstListVo selectListTacsFcltMst(TbTacsFcltMstVo tbTacsFcltMstVo) {
		TbTacsFcltMstListVo resultListVo = null;
		try {
			if (tbTacsFcltMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			List<TbTacsFcltMstVo> resultList = tacsMgmtTxService.selectListPageTacsFcltMstVo(tbTacsFcltMstVo);
			int totalCount = tacsMgmtTxService.countListPageTacsFcltMstVo(tbTacsFcltMstVo);
			resultListVo = new TbTacsFcltMstListVo();
			resultListVo.setTbTacsFcltMstVos(resultList);
			resultListVo.setTotalCount(totalCount);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"조직별장비목록"});
		}
		return resultListVo;
	}
	
	@Transactional(readOnly = true)
	public TbTacsFcltMstVo selectTacsFcltMst(TbTacsFcltMstVo tbTacsFcltMstVo) {
		TbTacsFcltMstVo resultVo = null;
		try {
			if (tbTacsFcltMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			resultVo = tacsMgmtTxService.selectTacsFcltMstVo(tbTacsFcltMstVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"조직 장비"});
		}
		return resultVo;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void insertTacsFcltMst(TbTacsFcltMstVo tbTacsFcltMstVo) {
		try {
			if (tbTacsFcltMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			TbLvlBasVo searchLvlBasVo = new TbLvlBasVo();
			searchLvlBasVo.setSsvcLineTypeCd(tbTacsFcltMstVo.getSsvcLineTypeCd());
			searchLvlBasVo.setSsvcGroupCd(tbTacsFcltMstVo.getSsvcGroupCd());
			searchLvlBasVo.setSsvcObjCd(tbTacsFcltMstVo.getSsvcObjCd());
			TbLvlBasVo resultLvlBasVo = tacsMgmtTxService.selectTbLvlBas(searchLvlBasVo);
			if (resultLvlBasVo == null) {
				throw new ServiceException("CMN.HIGH.00023", new String[]{"계위 정보"});
			}
			tbTacsFcltMstVo.setNlvlBasSeq(resultLvlBasVo.getNlvlBasSeq());
			TbTacsFcltMstVo searchVo = new TbTacsFcltMstVo();
			searchVo.setNlvlBasSeq(tbTacsFcltMstVo.getNlvlBasSeq());
			List<TbTacsFcltMstVo> resultList = tacsMgmtTxService.selectListTacsFcltMstVo(searchVo);
			for (TbTacsFcltMstVo item : resultList) {
				if (item.getPipFcltInet().equals(tbTacsFcltMstVo.getPipFcltInet())) {
					throw new ServiceException("CMN.HIGH.00049", new String[]{"조직 장비","해당 조직에 등록된 장비IP"});
				}
			}
			if (tbTacsFcltMstVo.getSsvcLineTypeCd().equals("CL0001")) {
				tbTacsFcltMstVo.setStacsServerId("KN");
			} else if (tbTacsFcltMstVo.getSsvcLineTypeCd().equals("CL0002")) {
				tbTacsFcltMstVo.setStacsServerId("PM");
			} else if (tbTacsFcltMstVo.getSsvcLineTypeCd().equals("CL0003")) {
				tbTacsFcltMstVo.setStacsServerId("WL");
			}
			tacsMgmtTxService.insertTacsFcltMstVo(tbTacsFcltMstVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00020", new String[]{"조직 장비"});
		}
	}
	
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteTacsFcltMst(TbTacsFcltMstVo tbTacsFcltMstVo) {
		try {
			if (tbTacsFcltMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			tacsMgmtTxService.deleteTacsFcltMstVo(tbTacsFcltMstVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00020", new String[]{"조직 장비"});
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void updateTacsFcltMst(TbTacsFcltMstVo tbTacsFcltMstVo) {
		try {
			if (tbTacsFcltMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			TbTacsFcltMstVo searchVo = new TbTacsFcltMstVo();
			searchVo.setNlvlBasSeq(tbTacsFcltMstVo.getNlvlBasSeq());
			List<TbTacsFcltMstVo> resultList = tacsMgmtTxService.selectListTacsFcltMstVo(searchVo);
			if (tbTacsFcltMstVo.getNtacsFcltMstSeq() != null) {
				for (TbTacsFcltMstVo item : resultList) {
					if (item.getNtacsFcltMstSeq().compareTo(tbTacsFcltMstVo.getNtacsFcltMstSeq()) != 0 
						&& item.getPipFcltInet().equals(tbTacsFcltMstVo.getPipFcltInet())) {
						throw new ServiceException("CMN.HIGH.00049", new String[]{"조직 장비","해당 조직에 등록된 장비IP"});
					}
				}
				tacsMgmtTxService.updateTacsFcltMstVo(tbTacsFcltMstVo);
			} else {
				for (TbTacsFcltMstVo item : resultList) {
					if (item.getPipFcltInet().equals(tbTacsFcltMstVo.getPipFcltInet())) {
						throw new ServiceException("CMN.HIGH.00049", new String[]{"조직 장비","해당 조직에 등록된 장비IP"});
					}
				}
				if (tbTacsFcltMstVo.getSsvcLineTypeCd().equals("CL0001")) {
					tbTacsFcltMstVo.setStacsServerId("KN");
				} else if (tbTacsFcltMstVo.getSsvcLineTypeCd().equals("CL0002")) {
					tbTacsFcltMstVo.setStacsServerId("PM");
				} else if (tbTacsFcltMstVo.getSsvcLineTypeCd().equals("CL0003")) {
					tbTacsFcltMstVo.setStacsServerId("WL");
				}
				tbTacsFcltMstVo.setScreateId(tbTacsFcltMstVo.getSmodifyId());
				tacsMgmtTxService.insertTacsFcltMstVo(tbTacsFcltMstVo);
			}
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00021", new String[]{"조직 장비"});
		}
	}
	
	@Transactional(readOnly = true)
	public TbTacsFcltCmdMstListVo selectListTacsFcltCmdMst(TbTacsFcltCmdMstVo tbTacsFcltCmdMstVo) {
		TbTacsFcltCmdMstListVo resultListVo = null;
		try {
			if (tbTacsFcltCmdMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			List<TbTacsFcltCmdMstVo> resultList = tacsMgmtTxService.selectListPageTacsFcltCmdMstVo(tbTacsFcltCmdMstVo);
			int totalCount = tacsMgmtTxService.countListPageTacsFcltCmdMstVo(tbTacsFcltCmdMstVo);
			resultListVo = new TbTacsFcltCmdMstListVo();
			resultListVo.setTbTacsFcltCmdMstVos(resultList);
			resultListVo.setTotalCount(totalCount);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"장비별명령어목록"});
		}
		return resultListVo;
		
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void insertTacsFcltCmdMst(TbTacsFcltCmdMstVo tbTacsFcltCmdMstVo) {
		try {
			if (tbTacsFcltCmdMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			TbTacsFcltCmdMstVo searchVo = new TbTacsFcltCmdMstVo();
			searchVo.setSfcltType(tbTacsFcltCmdMstVo.getSfcltType());
			List<TbTacsFcltCmdMstVo> resultList = tacsMgmtTxService.selectListTacsFcltCmdMstVo(searchVo);
			int npriority = 0;
			for (TbTacsFcltCmdMstVo item : resultList) {
				if (item.getNpriority() > npriority) {
					npriority = item.getNpriority();
				}
				if (item.getSfcltCmd().equals(tbTacsFcltCmdMstVo.getSfcltCmd())) {
					throw new ServiceException("CMN.HIGH.00049", new String[]{"장비명령어","해당 장비에 등록된 명령어"});
				}
			}
			tbTacsFcltCmdMstVo.setNpriority(npriority + 1);
			tacsMgmtTxService.insertTacsFcltCmdMstVo(tbTacsFcltCmdMstVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00020", new String[]{"장비명령어"});
		}
	}
	
	@Transactional(readOnly = true)
	public TbTacsFcltCmdMstVo selectTacsFcltCmdMst(TbTacsFcltCmdMstVo tbTacsFcltCmdMstVo) {
		TbTacsFcltCmdMstVo resultVo = null;
		try {
			if (tbTacsFcltCmdMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			resultVo = tacsMgmtTxService.selectTacsFcltCmdMstVo(tbTacsFcltCmdMstVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"장비명령어"});
		}
		return resultVo;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void updateTacsFcltCmdMst(TbTacsFcltCmdMstVo tbTacsFcltCmdMstVo) {
		try {
			if (tbTacsFcltCmdMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			TbTacsFcltCmdMstVo searchVo = new TbTacsFcltCmdMstVo();
			searchVo.setSfcltType(tbTacsFcltCmdMstVo.getSfcltType());
			List<TbTacsFcltCmdMstVo> resultList = tacsMgmtTxService.selectListTacsFcltCmdMstVo(searchVo);
			for (TbTacsFcltCmdMstVo item : resultList) {
				if (item.getNtacsFcltCmdMstSeq().compareTo(tbTacsFcltCmdMstVo.getNtacsFcltCmdMstSeq()) != 0 
					&& item.getSfcltCmd().equals(tbTacsFcltCmdMstVo.getSfcltCmd())) {
					throw new ServiceException("CMN.HIGH.00049", new String[]{"장비명령어","해당 장비에 등록된 명령어"});
				}
			}
			tacsMgmtTxService.updateTacsFcltCmdMstVo(tbTacsFcltCmdMstVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"장비명령어"});
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public TacsResponseListVo getCheckTacsRouteList(TbIpAssignMstVo tbIpAssignMstVo, String userId) {
		TacsResponseListVo resultListVo = null;
		try {
			TbLvlBasVo searchLvlBasVo = new TbLvlBasVo();
			searchLvlBasVo.setSsvcLineTypeCd(tbIpAssignMstVo.getSsvcLineTypeCd());
			searchLvlBasVo.setSsvcGroupCd(tbIpAssignMstVo.getSsvcGroupCd());
			searchLvlBasVo.setSsvcObjCd(tbIpAssignMstVo.getSsvcObjCd());
			TbLvlBasVo resultLvlBasVo = tacsMgmtTxService.selectTbLvlBas(searchLvlBasVo);
			if (resultLvlBasVo == null) {
				throw new ServiceException("CMN.HIGH.00023", new String[]{"계위 정보"});
			}

			TbTacsFcltMstVo searchFcltVo = new TbTacsFcltMstVo();
			searchFcltVo.setNlvlBasSeq(resultLvlBasVo.getNlvlBasSeq());
			List<TbTacsFcltMstVo> resultFcltVos = tacsMgmtTxService.selectListTacsFcltMstVo(searchFcltVo);
			if (resultFcltVos == null || resultFcltVos.size() == 0) {
				// 등록된 장비가 없는 경우
				throw new ServiceException("CMN.INFO.00053", new String[]{"해당 계위에", "등록된 장비가 없습니다."});
			}
			
			TbTacsConnBasVo resultConnVo = tacsMgmtTxService.selectTbTacsConnBasVo();
			
			// 보낼 데이터 가공
			TacsRequstListVo requestListVo = new TacsRequstListVo();
			List<TacsRequestVo> requestList = new ArrayList<TacsRequestVo>();
			requestListVo.setTacsRequestVos(requestList);
			for (TbTacsFcltMstVo itemVo : resultFcltVos) {
				String sfcltType = itemVo.getSfcltType();
				if (sfcltType == null || sfcltType.equals("")) {
					// 등록된 장비타입이 없는 경우
					throw new ServiceException("CMN.INFO.00053", new String[]{"해당 계위에", "등록된 장비타입가 없습니다."});
				}
				TbTacsFcltCmdMstVo searchFcltCmdVo = new TbTacsFcltCmdMstVo();
				searchFcltCmdVo.setSfcltType(sfcltType);
				searchFcltCmdVo.setSuseYn("Y");
				List<TbTacsFcltCmdMstVo> resultFcltCmdList = tacsMgmtTxService.selectListTacsFcltCmdMstVo(searchFcltCmdVo);
				if (resultFcltCmdList == null || resultFcltCmdList.size() == 0) {
					// 등록된 장비별 명령어가 없는 경우
					throw new ServiceException("CMN.INFO.00053", new String[]{"해당 장비에", "등록된 명령어가 없습니다."});
				}
				boolean isSubConn = false;
				int subConnCnt = 0;
				TacsRequestVo requestVo = new TacsRequestVo();
				requestVo.setOgwIp(resultConnVo.getConnOgwIp());
				requestVo.setOgwPort(resultConnVo.getConnOgwPort());
				requestVo.setMacaddr(resultConnVo.getConnMacAddr());
				requestVo.setUserId(resultConnVo.getConnId());
				requestVo.setUserPwd(resultConnVo.getConnPw());
				requestVo.setTargetIp(itemVo.getPipFcltInet());
				requestVo.setTargetPort(itemVo.getNportFclt());
				requestVo.setServerId(itemVo.getStacsServerId());
				requestVo.setPromptNm(itemVo.getSfcltPromptNm());
				requestVo.setIsSubConn(isSubConn);
				requestVo.setSubConnCnt(subConnCnt);
				requestVo.setPipPrefix(tbIpAssignMstVo.getPipPrefix());
				List<TacsRequestCmdVo> cmdList = new ArrayList<TacsRequestCmdVo>();
				requestVo.setCmdList(cmdList);
				for (int i=0; i < resultFcltCmdList.size(); i++) {
					TbTacsFcltCmdMstVo itemCmdVo = resultFcltCmdList.get(i);
					TacsRequestCmdVo cmdVo = new TacsRequestCmdVo();
					String cmd = itemCmdVo.getSfcltCmd();
					if (cmd.startsWith("telnet") || cmd.startsWith("TELNET")) {
						isSubConn = true;
						subConnCnt++;
						cmdVo.setFlag("SUB");
					} else {
						cmdVo.setFlag("CMD");
					}
					cmd = cmd.replace("[CIDR]", tbIpAssignMstVo.getPipPrefix());
					cmd = cmd.replace("[IP]", tbIpAssignMstVo.getSfirstAddr());
					cmd = cmd.replace("[SUBNET]", tbIpAssignMstVo.getSnetmask());
					cmdVo.setCmd(cmd);
					cmdVo.setAvailYn(itemCmdVo.getSavailYn());
					cmdVo.setParse(itemCmdVo.getSparseContent());
					cmdList.add(cmdVo);
				}
				requestVo.setIsSubConn(isSubConn);
				requestVo.setSubConnCnt(subConnCnt);
				requestList.add(requestVo);
			}
			requestListVo.setTotalCount(requestList.size());
			resultListVo = tacsMgmtTxService.getCheckTacsRouteList(requestListVo, userId);
			List<TacsResponseVo> resultList = resultListVo.getTacsResponseVos();
			if (resultList == null || resultList.size() == 0) {
				resultListVo.setTypeFlag("N");
			} else {
				int flagCnt = 0;
				for (TacsResponseVo tacsResponseVo : resultList) {
					for (TbTacsFcltMstVo itemVo : resultFcltVos) {
						if (tacsResponseVo.getTargetIp().equals(itemVo.getPipFcltInet())) {
							tacsResponseVo.setTargetType(itemVo.getSfcltType());
							break;
						}
					}
					if (tacsResponseVo.getFlag().equals("Y")) {
						flagCnt++;
					}
				}
				if (flagCnt == resultList.size()) {
					resultListVo.setTypeFlag("Y");
				} else {
					resultListVo.setTypeFlag("N");
				}
			}
			
		} catch (ServiceException e) {
			PrintLogUtil.printLogInfo("e =="+e.toString());
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"TACS 연동 정보"});
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
			resultVo = tacsMgmtTxService.selectIpAssignMst(tbIpAssignMstVo);
			if (resultVo == null) {
				throw new ServiceException("CMN.HIGH.00023", new String[]{"IP 할당정보"});
			}
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"IP 할당정보"});
		}
		return resultVo;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void insertTacsConnHist(TbTacsConnHistVo tbTacsConnHistVo) {
		try {
			if (tbTacsConnHistVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			tacsMgmtTxService.insertTacsConnHistVo(tbTacsConnHistVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00020", new String[]{"TACS 연동 이력"});
		}
	}
	
	@Transactional(readOnly = true)
	public TbTacsConnHistListVo selectListTacsConnHist(TbTacsConnHistVo tbTacsConnHistVo) {
		TbTacsConnHistListVo resultListVo = null;
		try {
			if (tbTacsConnHistVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			List<TbTacsConnHistVo> resultList = tacsMgmtTxService.selectListPageTbTacsConnHistVo(tbTacsConnHistVo);
			int totalCount = tacsMgmtTxService.countListTbTacsConnHistVo(tbTacsConnHistVo);
			resultListVo = new TbTacsConnHistListVo();
			resultListVo.setTbTacsConnHistVos(resultList);
			resultListVo.setTotalCount(totalCount);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"TACS 연동 이력관리"});
		}
		return resultListVo;
	}
	@Transactional(readOnly = true)
	public List<?> selectSresultMsg(){
		List<?> sresultMsg = tacsMgmtTxService.selectSresultMsg();
		return sresultMsg;
	}
}
