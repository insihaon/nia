package com.kt.ipms.legacy.ticketmgmt.configmgmt.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.kt.framework.exception.ServiceException;
import com.kt.ipms.legacy.cmn.util.CommonCodeUtil;
import com.kt.ipms.legacy.ipmgmt.allocmgmt.vo.IpAllocOperMstListVo;
import com.kt.ipms.legacy.ipmgmt.allocmgmt.vo.IpAllocOperMstVo;
import com.kt.ipms.legacy.ticketmgmt.configmgmt.vo.TbConfigInterfaceMstListVo;
import com.kt.ipms.legacy.ticketmgmt.configmgmt.vo.TbConfigInterfaceMstVo;
import com.kt.ipms.legacy.ticketmgmt.configmgmt.vo.TbConfigLinkMstVo;
import com.kt.ipms.legacy.ticketmgmt.configmgmt.vo.TbConfigRouteMstListVo;
import com.kt.ipms.legacy.ticketmgmt.configmgmt.vo.TbConfigRouteMstVo;

@Component
@Transactional
public class ConfigMgmtService {
	
	@Autowired
	private ConfigMgmtTxService configMgmtTxService;
	
	@Transactional(readOnly = true)
	public TbConfigInterfaceMstListVo selectListFirstStepTopology(TbConfigInterfaceMstVo tbConfigInterfaceMstVo) {
		TbConfigInterfaceMstListVo resultListVo = null;
		try {
			if (tbConfigInterfaceMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			List<TbConfigInterfaceMstVo> resultList = configMgmtTxService.selectListTopologyConfigInterfaceMst(tbConfigInterfaceMstVo);
			int totalCount = configMgmtTxService.countListTopologyConfigInterfaceMst(tbConfigInterfaceMstVo);
			resultListVo = new TbConfigInterfaceMstListVo();
			resultListVo.setTbConfigInterfaceMstVos(resultList);
			resultListVo.setTotalCount(totalCount);
			if (totalCount > 0) {
				List<Map<String, Object>> firstListMap = new ArrayList<Map<String,Object>>();
				for (TbConfigInterfaceMstVo itemVo : resultList) {
					Map<String, Object> itemMap = new HashMap<String, Object>();
					StringBuilder title = new StringBuilder();
					title.append(itemVo.getShostNm());
					title.append(" (");
					title.append(itemVo.getShostIp());
					title.append(")");
					itemMap.put("title", title.toString());
					itemMap.put("isFolder", true);
					itemMap.put("upperFolder", "/");
					itemMap.put("isLazy", true);
					itemMap.put("nLevel", 1);
					itemMap.put("shostIp", itemVo.getShostIp());
					itemMap.put("shostNm", itemVo.getShostNm());
					firstListMap.add(itemMap);
				}
				ObjectMapper mapper = new ObjectMapper();
				resultListVo.setTreeJsonString(mapper.writeValueAsString(firstListMap));
			} else {
				List<Map<String, Object>> firstListMap = new ArrayList<Map<String,Object>>();
				ObjectMapper mapper = new ObjectMapper();
				resultListVo.setTreeJsonString(mapper.writeValueAsString(firstListMap));
			}
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"TOPOLOGY목록"});
		}
		return resultListVo;
	}
	
	@Transactional(readOnly = true)
	public List<Map<String, Object>> selectListTopologyTree(Map<String, Object> paramMap) {
		List<Map<String, Object>> resultListMap = null;
		try {
			if (paramMap == null || paramMap.get("nLevel") == null || paramMap.get("searchIp") == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			int nLevel = Integer.parseInt(paramMap.get("nLevel").toString());
			String searchIp = (String) paramMap.get("searchIp");
			String selectKey = (String) paramMap.get("selectKey");
			resultListMap = new ArrayList<Map<String,Object>>();
			if (nLevel == 1 || nLevel == 2) {
				TbConfigLinkMstVo searchVo = new TbConfigLinkMstVo();
				if (nLevel == 1) {
					searchVo.setNaFlagLevel(1000);
				} else {
					searchVo.setNaFlagLevel(2000);
				}
				searchVo.setSaHostIp(searchIp);
				List<TbConfigLinkMstVo> resultList = configMgmtTxService.selectListConfigLinkMst(searchVo);
				for (TbConfigLinkMstVo itemVo : resultList) {
					Map<String, Object> itemMap = new HashMap<String, Object>();
					StringBuilder title = new StringBuilder();
					if (nLevel == 1) {
						itemMap.put("nLevel", 2);
					} else {
						itemMap.put("nLevel", 3);
					}
					title.append(itemVo.getSgHostNm());
					title.append(" (");
					title.append(itemVo.getSgHostIp());
					title.append(")");
					itemMap.put("title", title.toString());
					itemMap.put("isFolder", true);
					itemMap.put("upperFolder", selectKey);
					itemMap.put("isLazy", true);
					itemMap.put("nSeq", itemVo.getNconfigLinkMstSeq());
					itemMap.put("saHostIp", itemVo.getSaHostIp());
					itemMap.put("saHostNm", itemVo.getSaHostNm());
					itemMap.put("saIfIp", itemVo.getSaIfIp());
					itemMap.put("saIfNm", itemVo.getSaIfNm());
					itemMap.put("saIfDesc", itemVo.getSaIfDescription());
					itemMap.put("sgHostIp", itemVo.getSgHostIp());
					itemMap.put("sgHostNm", itemVo.getSgHostNm());
					itemMap.put("sgIfIp", itemVo.getSgIfIp());
					itemMap.put("sgIfNm", itemVo.getSgIfNm());
					itemMap.put("sgIfDesc", itemVo.getSgIfDescription());
					resultListMap.add(itemMap);
				} 
			} else if (nLevel == 3) {
				TbConfigInterfaceMstVo searchVo = new TbConfigInterfaceMstVo();
				searchVo.setNflagLevel(3000);
				searchVo.setShostIp(searchIp);
				List<TbConfigInterfaceMstVo> resultList = configMgmtTxService.selectListConfigInterfaceMst(searchVo);
				for (TbConfigInterfaceMstVo itemVo : resultList) {
					Map<String, Object> itemMap = new HashMap<String, Object>();
					StringBuilder title = new StringBuilder();
					title.append(itemVo.getSifNm());
					title.append(" (");
					title.append(itemVo.getSifIp());
					title.append(")");
					itemMap.put("title", title.toString());
					itemMap.put("isFolder", true);
					itemMap.put("upperFolder", selectKey);
					itemMap.put("isLazy", true);
					itemMap.put("nLevel", 4);
					itemMap.put("nSeq", itemVo.getNconfigInterfaceSeq());
					itemMap.put("shostIp", itemVo.getShostIp());
					itemMap.put("shostNm", itemVo.getShostNm());
					itemMap.put("sifIp", itemVo.getSifIp());
					itemMap.put("sifNm", itemVo.getSifNm());
					itemMap.put("sifDesc", itemVo.getSifDescription());
					resultListMap.add(itemMap);
				}
			} else if (nLevel == 4) {
				TbConfigRouteMstVo searchVo = new TbConfigRouteMstVo();
				searchVo.setNflagLevel(3000);
				searchVo.setSrouteGwIfIp(searchIp);
				List<TbConfigRouteMstVo> resultList = configMgmtTxService.selectListConfigRouteMst(searchVo);
				for (TbConfigRouteMstVo itemVo : resultList) {
					Map<String, Object> itemMap = new HashMap<String, Object>();
					itemMap.put("title", itemVo.getSrouteIpBlock());
					itemMap.put("isFolder", false);
					itemMap.put("upperFolder", selectKey);
					itemMap.put("isLazy", false);
					itemMap.put("nLevel", 5);
					itemMap.put("nSeq", itemVo.getNconfigRouteSeq());
					itemMap.put("srouteIpBlock", itemVo.getSrouteIpBlock());
					resultListMap.add(itemMap);
				}
			}
			
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"TOPOLOGY목록"});
		}
		return resultListMap;
	}
	
	@Transactional(readOnly = true)
	public TbConfigRouteMstListVo selectListPageRouteMst(TbConfigRouteMstVo tbConfigRouteMstVo) {
		TbConfigRouteMstListVo resultListVo = null;
		try {
			if (tbConfigRouteMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			if (!StringUtils.hasText(tbConfigRouteMstVo.getSortType())) {
				tbConfigRouteMstVo.setSortType(CommonCodeUtil.SORT_TYPE_SHOST_IP);
			}
			if (!StringUtils.hasText(tbConfigRouteMstVo.getSortOrdr())) {
				tbConfigRouteMstVo.setSortOrdr(CommonCodeUtil.SORT_ORDR_ASC);
			}
			List<TbConfigRouteMstVo> resultList = configMgmtTxService.selectListPageConfigRouteMst(tbConfigRouteMstVo);
			int totalCount = configMgmtTxService.countListPageConfigRouteMst(tbConfigRouteMstVo);
			resultListVo = new TbConfigRouteMstListVo();
			resultListVo.setTbConfigRouteMstVos(resultList);
			resultListVo.setTotalCount(totalCount);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"Route 목록"});
		}
		return resultListVo;
	}
	
	@Transactional(readOnly = true)
	public TbConfigRouteMstListVo selectListPageRouteMstExcel(TbConfigRouteMstVo tbConfigRouteMstVo) {
		TbConfigRouteMstListVo resultListVo = null;
		try {
			if (tbConfigRouteMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			if (!StringUtils.hasText(tbConfigRouteMstVo.getSortType())) {
				tbConfigRouteMstVo.setSortType(CommonCodeUtil.SORT_TYPE_SHOST_IP);
			}
			if (!StringUtils.hasText(tbConfigRouteMstVo.getSortOrdr())) {
				tbConfigRouteMstVo.setSortOrdr(CommonCodeUtil.SORT_ORDR_ASC);
			}
			int totalCount = configMgmtTxService.countListPageConfigRouteMst(tbConfigRouteMstVo);
			if (totalCount > CommonCodeUtil.MAX_EXCEL_ROW_COUNT) {
				throw new ServiceException("CMN.INFO.00051");
			}
			List<TbConfigRouteMstVo> resultList = null;
			if(totalCount > 0)
			{
				tbConfigRouteMstVo.setFirstIndex(1);
				tbConfigRouteMstVo.setLastIndex(totalCount);
				resultList = configMgmtTxService.selectListPageConfigRouteMst(tbConfigRouteMstVo);
			}
			
			resultList = configMgmtTxService.selectListPageConfigRouteMst(tbConfigRouteMstVo);
			resultListVo = new TbConfigRouteMstListVo();
			resultListVo.setTbConfigRouteMstVos(resultList);
			resultListVo.setTotalCount(totalCount);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"Route 목록 엑셀"});
		}
		return resultListVo;
	}
	
	@Transactional(readOnly = true)
	public TbConfigRouteMstVo selectConfigRouteMst(TbConfigRouteMstVo tbConfigRouteMstVo) {
		TbConfigRouteMstVo resultVo = null;
		try {
			if (tbConfigRouteMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			resultVo = configMgmtTxService.selectConfigRouteMst(tbConfigRouteMstVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"Route 상세"});
		}
		return resultVo;
	}
	
	@Transactional(readOnly = true)
	public IpAllocOperMstListVo selectListIpAllocDetail(TbConfigRouteMstVo tbConfigRouteMstVo) {
		IpAllocOperMstListVo resultListVo = null;
		try {
			if (tbConfigRouteMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			IpAllocOperMstVo searchVo = new IpAllocOperMstVo();
			searchVo.setPipPrefix(tbConfigRouteMstVo.getSrouteIpBlock());
			resultListVo = configMgmtTxService.selectListIpAllocDetail(searchVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"Route 상세(IP블록)"});
		}
		return resultListVo;
	}
	
	@Transactional(readOnly = true)
	public TbConfigInterfaceMstListVo selectListPageConfigInterfaceMst(TbConfigInterfaceMstVo tbConfigInterfaceMstVo) {
		TbConfigInterfaceMstListVo resultListVo = null;
		try {
			if (tbConfigInterfaceMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			List<TbConfigInterfaceMstVo> resultList = configMgmtTxService.selectListPageConfigInterfaceMst(tbConfigInterfaceMstVo);
			int totalCount = configMgmtTxService.countListPageConfigInterfaceMst(tbConfigInterfaceMstVo);
			resultListVo = new TbConfigInterfaceMstListVo();
			resultListVo.setTbConfigInterfaceMstVos(resultList);
			resultListVo.setTotalCount(totalCount);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"Interface 목록"});
		}
		return resultListVo;
	}
	
	@Transactional(readOnly = true)
	public int countDuplicateConfigInterfaceMst(TbConfigInterfaceMstVo tbConfigInterfaceMstVo) {
		int count = 0;
		try {
			if (tbConfigInterfaceMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			count = configMgmtTxService.countDuplicateConfigInterfaceMst(tbConfigInterfaceMstVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"Interface 중복"});
		}
		return count;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void insertConfigInterfaceMst(TbConfigInterfaceMstVo tbConfigInterfaceMstVo) {
		configMgmtTxService.insertConfigInterfaceMst(tbConfigInterfaceMstVo);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void updateConfigInterfaceMst(TbConfigInterfaceMstVo tbConfigInterfaceMstVo) {
		configMgmtTxService.updateConfigInterfaceMst(tbConfigInterfaceMstVo);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteConfigInterfaceMst(TbConfigInterfaceMstVo tbConfigInterfaceMstVo) {
		configMgmtTxService.deleteConfigInterfaceMst(tbConfigInterfaceMstVo);
	}
	
	@Transactional(readOnly = true)
	public TbConfigInterfaceMstVo selectConfigInterfaceMst(TbConfigInterfaceMstVo tbConfigInterfaceMstVo) {
		TbConfigInterfaceMstVo resultVo = null;
		try {
			if (tbConfigInterfaceMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			resultVo = configMgmtTxService.selectConfigInterfaceMst(tbConfigInterfaceMstVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"Interface 중복"});
		}
		return resultVo;
	}
	
	
	@Transactional(readOnly = true)
	public TbConfigRouteMstListVo selectListMainRouteMst(TbConfigRouteMstVo tbConfigRouteMstVo) {
		TbConfigRouteMstListVo resultListVo = null;
		try {
			if (tbConfigRouteMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			List<TbConfigRouteMstVo> resultList = configMgmtTxService.selectListMainConfigRouteMst(tbConfigRouteMstVo);
			int totalCount = configMgmtTxService.countListMainConfigRouteMst(tbConfigRouteMstVo);
			resultListVo = new TbConfigRouteMstListVo();
			resultListVo.setTbConfigRouteMstVos(resultList);
			resultListVo.setTotalCount(totalCount);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"Route 목록"});
		}
		return resultListVo;
	}
}
