package com.kt.ipms.legacy.ipmgmt.allocmgmt.service;

import java.math.BigInteger;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kt.framework.exception.ServiceException;
import com.kt.framework.utils.StringUtils;
import com.kt.ipms.legacy.cmn.util.CloneUtil;
import com.kt.ipms.legacy.cmn.util.CommonCodeUtil;
import com.kt.ipms.legacy.cmn.util.PrintLogUtil;
import com.kt.ipms.legacy.cmn.vo.CommonCodeVo;
import com.kt.ipms.legacy.cmn.vo.IpVo;
import com.kt.ipms.legacy.ipmgmt.allocmgmt.dao.TbIpAllocMstDao;
import com.kt.ipms.legacy.ipmgmt.allocmgmt.vo.IpAllocMstComplexVo;
import com.kt.ipms.legacy.ipmgmt.allocmgmt.vo.IpAllocOperMstListVo;
import com.kt.ipms.legacy.ipmgmt.allocmgmt.vo.IpAllocOperMstVo;
import com.kt.ipms.legacy.ipmgmt.allocmgmt.vo.IpmsSvcVo;
import com.kt.ipms.legacy.ipmgmt.allocmgmt.vo.TbIpAllocMstListVo;
import com.kt.ipms.legacy.ipmgmt.allocmgmt.vo.TbIpAllocMstVo;
import com.kt.ipms.legacy.ipmgmt.assignmgmt.adapter.AssignMgmtAdapterService;
import com.kt.ipms.legacy.ipmgmt.assignmgmt.vo.TbIpAssignMstComplexVo;
import com.kt.ipms.legacy.ipmgmt.assignmgmt.vo.TbIpAssignMstVo;
import com.kt.ipms.legacy.ipmgmt.historymgmt.adapter.HistoryMgmtAdapterService;
import com.kt.ipms.legacy.ipmgmt.historymgmt.vo.IpHistoryMstVo;
import com.kt.ipms.legacy.ipmgmt.hostmgmt.adapter.HostMgmtAdapterService;
import com.kt.ipms.legacy.ipmgmt.hostmgmt.vo.TbIpHostMstListVo;
import com.kt.ipms.legacy.ipmgmt.hostmgmt.vo.TbIpHostMstVo;
import com.kt.ipms.legacy.ipmgmt.linemgmt.adapter.LineMgmtAdapterService;
import com.kt.ipms.legacy.ipmgmt.linemgmt.vo.TbIpAssignSubVo;
import com.kt.ipms.legacy.ipmgmt.routmgmt.service.RoutMgmtTxService;
import com.kt.ipms.legacy.ipmgmt.routmgmt.vo.TbRoutChkMstVo;
import com.kt.ipms.legacy.linkmgmt.common.vo.TbIpAllocNeossMstListVo;
import com.kt.ipms.legacy.linkmgmt.common.vo.TbIpAllocNeossMstVo;
import com.kt.ipms.legacy.linkmgmt.consumermgmt.neoss.adapter.NeOSSConsumeAdapterService;
import com.kt.ipms.legacy.linkmgmt.whois.adapter.WhoisAdapterService;
import com.kt.ipms.legacy.linkmgmt.whois.service.WhoisService;
import com.kt.ipms.legacy.opermgmt.srvmgmt.adapter.SvcMgmtAdapterService;
import com.kt.ipms.legacy.opermgmt.srvmgmt.vo.TbIpmsSvcMstVo;
import com.kt.ipms.legacy.ticketmgmt.ticketmgmt.adapter.TicketMgmtAdapterService;
import com.kt.ipms.legacy.ticketmgmt.ticketmgmt.vo.TbTicketMstVo;
import com.kt.ipms.legacy.cmn.service.IpCommonService;

@Component
@Transactional
public class AllocMgmtService {

	@Lazy
	@Autowired
	private AllocMgmtTxService allocMgmtTxService;

	@Lazy
	@Autowired
	private AssignMgmtAdapterService assignMgmtAdapterService;

	@Lazy
	@Autowired
	private LineMgmtAdapterService lineMgmtAdapterService;

	@Lazy
	@Autowired
	private NeOSSConsumeAdapterService neOSSConsumeAdapterService;

	@Lazy
	@Autowired
	private TbIpAllocMstDao tbIpAllocMstDao;

	@Lazy
	@Autowired
	private HostMgmtAdapterService hostMgmtAdapterService;

	@Lazy
	@Autowired
	private TicketMgmtAdapterService ticketMgmtAdapterService;

	@Lazy
	@Autowired
	private SvcMgmtAdapterService svcMgmtAdapterService;

	@Lazy
	@Autowired
	private WhoisAdapterService whoisAdapterService;

	@Lazy
	@Autowired
	private WhoisService whoisService;

	@Lazy
	@Autowired
	private RoutMgmtTxService routMgmtTxService;

	@Lazy
	@Autowired
	private HistoryMgmtAdapterService historyMgmtAdapterService;

	@Lazy
	@Autowired
	private IpCommonService ipCommonService;

	/* 할당 메인 조회 */
	@Transactional(readOnly = true)
	public IpAllocOperMstListVo selectListIpAllocMst(IpAllocOperMstVo ipAllocOperMstVo) {
		IpAllocOperMstListVo resultListVo = null;
		if (ipAllocOperMstVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {
			List<IpAllocOperMstVo> resultList = allocMgmtTxService.selectListPageIpAllocMst(ipAllocOperMstVo);
			int totalCount = allocMgmtTxService.countListPageIpAllocMst(ipAllocOperMstVo);
			resultListVo = new IpAllocOperMstListVo();
			resultListVo.setIpAllocOperMstVos(resultList);
			resultListVo.setTotalCount(totalCount);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[] { "IP할당블록목록" });
		}
		return resultListVo;
	}

	/* 병합가능 리스트 메인 조회 */
	@Transactional(readOnly = true)
	public IpAllocOperMstListVo selectMergeListIpAllocMst(IpAllocOperMstVo ipAllocOperMstVo) {
		IpAllocOperMstListVo resultListVo = null;
		if (ipAllocOperMstVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {
			List<IpAllocOperMstVo> resultList = allocMgmtTxService.selectMergeListPageIpAllocMst(ipAllocOperMstVo);
			int totalCount = allocMgmtTxService.countMergeListPageIpAllocMst(ipAllocOperMstVo);
			resultListVo = new IpAllocOperMstListVo();
			resultListVo.setIpAllocOperMstVos(resultList);
			resultListVo.setTotalCount(totalCount);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[] { "IP병합가능 목록" });
		}
		return resultListVo;
	}

	/* 병합가능 리스트 스케줄러 > group id update */
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED)
	public List<IpAllocOperMstVo> validateMrgAsgnIPMstTest(IpAllocOperMstVo searchVo) {
		IpAllocOperMstListVo ipAllocOperMstListVo = new IpAllocOperMstListVo();
		List<IpAllocOperMstVo> destIpAssignMstVos = new ArrayList<IpAllocOperMstVo>();
		try {
			long start = System.currentTimeMillis();
			destIpAssignMstVos = allocMgmtTxService.selectListTbIpAssignMstVoContinuityList(searchVo);
			if (destIpAssignMstVos != null && destIpAssignMstVos.size() > 0) {
				ArrayList<IpAllocOperMstVo> bind = (ArrayList<IpAllocOperMstVo>) destIpAssignMstVos;
				List<IpAllocOperMstVo> copyIpAssignMstVos = (List<IpAllocOperMstVo>) bind.clone();

				while (copyIpAssignMstVos.size() > 1) {
					List<IpAllocOperMstVo> validateCheckList = new ArrayList<>();
					IpAllocOperMstVo ipVo1 = copyIpAssignMstVos.get(0);
					IpAllocOperMstVo ipVo2 = copyIpAssignMstVos.get(1);
					validateCheckList.add(ipVo1);
					validateCheckList.add(ipVo2);

					if (!ipVo1.getSassignTypeCd().equals(ipVo2.getSassignTypeCd())) {
						String ipVo1Network = ipCommonService.calculateNetworkAddress(ipVo1.getSipBlock(),
								ipVo1.getNbitmask() - 1);
						String ipVo2Network = ipCommonService.calculateNetworkAddress(ipVo2.getSipBlock(),
								ipVo2.getNbitmask() - 1);
						copyIpAssignMstVos.remove(ipVo1);
						if (ipVo1Network.equals(ipVo2Network)) {
							copyIpAssignMstVos.remove(ipVo2);
						}
					} else {
						boolean isMergeSuccess = ipCommonService.getMergeIpBlockMstInfo(validateCheckList);
						if (isMergeSuccess) {
							/* 메모리 최적화 검토 필요 => IpAllocOperMstVo를 return하는 getMergeIpBlockMstInfo 메소드 추가 */
							IpAllocOperMstVo mergedAllocVo = new IpAllocOperMstVo();
							IpVo mergedVo = validateCheckList.get(0);
							/*
							 * 1. 병합된 vo에 병합 전 vo의 index를 indexList에 추가
							 * 2. vo의 indexList 값을 최초 조회 리스트index에 setGroupId(pipPrefix가)
							 */
							List<Integer> indexList = new ArrayList<Integer>();
							if (ipVo1.getIndexList() != null && ipVo2.getIndexList() != null) {
								indexList.addAll(
										Stream.concat(ipVo1.getIndexList().stream(), ipVo2.getIndexList().stream())
												.collect(Collectors.toList()));
							} else {
								indexList.add((Integer) ipVo1.getIndex());
								indexList.add(ipVo2.getIndex());
							}
							mergedAllocVo.setIndexList(indexList);

							/* 찾은 기존vo에 병합된 vo의 pipprefix를 group id로 set */
							// IpAllocOperMstVo mergedVo = (IpAllocOperMstVo)validateCheckList.get(0);

							/* copyIpAssignMstVos에 추가할 mergedAllocVo(ipVo1, ipVo2가 병합된 결과) setting */
							mergedAllocVo.setPipPrefix(mergedVo.getPipPrefix());
							mergedAllocVo.setSipBlock(mergedVo.getSipBlock());
							mergedAllocVo.setNbitmask(mergedVo.getNbitmask());
							mergedAllocVo.setSfirstAddr(mergedVo.getSfirstAddr());
							mergedAllocVo.setNfirstAddr(mergedVo.getNfirstAddr());
							mergedAllocVo.setNlastAddr(mergedVo.getNlastAddr());
							mergedAllocVo.setSipVersionTypeCd(mergedVo.getSipVersionTypeCd());
							mergedAllocVo.setSassignTypeCd(ipVo1.getSassignTypeCd());
							/*
							 * IpAllocOperMstVo 프로퍼티 추가 : index(rownum), group id, indexList
							 * destIpAssignMstVos 에 0, 1에 해당하는 vo찾아서 group id set
							 * mergedAllocVo의 indexList의 값으로 base리스트의 groupid를 update
							 */
							if (mergedAllocVo.getIndexList().size() > 0) {
								for (int index : mergedAllocVo.getIndexList()) {
									destIpAssignMstVos.get(index - 1).setGroupId(mergedAllocVo.getPipPrefix());
								}
							}
							copyIpAssignMstVos.remove(ipVo1);
							copyIpAssignMstVos.remove(ipVo2);
							copyIpAssignMstVos.add(mergedAllocVo);
						} else {
							copyIpAssignMstVos.remove(ipVo1);
						}
					}
					validateCheckList.clear();
				}
				/* 1000건씩 */
				int batchSize = 1000;
				int totalRecords = destIpAssignMstVos.size();
				for (int i = 0; i < totalRecords; i += batchSize) {
					int end = Math.min(i + batchSize, totalRecords);
					List<IpAllocOperMstVo> batchList = destIpAssignMstVos.subList(i, end);
					ipAllocOperMstListVo.setIpAllocOperMstVos(batchList);
					allocMgmtTxService.updateTbIpAssignMstVoGroupId(ipAllocOperMstListVo);
					System.out.println("Processed batch: " + (i / batchSize + 1));
				}
				// allocMgmtTxService.insertTempMergeTable(ipAllocOperMstListVo);
				// allocMgmtTxService.bulkUpdateTbIpAssignMstVoGroupId(ipAllocOperMstListVo);
			} else {
				throw new ServiceException("CMN.HIGH.00001");
			}
			long end = System.currentTimeMillis();
			long elapsedTimeMillis = end - start;

			long seconds = elapsedTimeMillis / 1000;
			long minutes = elapsedTimeMillis / (1000 * 60);

			System.out.println("Exeution time: " + minutes + " minutes " + seconds + " seconds");
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[] { "IP병합가능블록목록" });
		}

		// return destIpAssignMstVos;
		return new ArrayList<IpAllocOperMstVo>();
	}

	/* 할당 메인 조회 Excel */
	@Transactional(readOnly = true)
	public IpAllocOperMstListVo selectListIpAllocMstExcel(IpAllocOperMstVo ipAllocOperMstVo) {
		IpAllocOperMstListVo resultListVo = null;
		if (ipAllocOperMstVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {
			int totalCount = allocMgmtTxService.countListPageIpAllocMst(ipAllocOperMstVo);
			if (totalCount > CommonCodeUtil.MAX_EXCEL_ROW_COUNT) {
				throw new ServiceException("CMN.INFO.00051");
			}
			List<IpAllocOperMstVo> resultList = null;
			if (totalCount > 0) {
				ipAllocOperMstVo.setFirstIndex(1);
				ipAllocOperMstVo.setLastIndex(totalCount);
				resultList = allocMgmtTxService.selectListPageIpAllocMst(ipAllocOperMstVo);
			}
			resultListVo = new IpAllocOperMstListVo();
			resultListVo.setIpAllocOperMstVos(resultList);
			resultListVo.setTotalCount(totalCount);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[] { "IP할당블록목록 엑셀저장" });
		}
		return resultListVo;
	}

	/* 할당 정보 조회 */
	@Transactional(readOnly = true)
	public IpAllocOperMstVo selectIpAllocMst(IpAllocOperMstVo ipAllocOperMstVo) {
		IpAllocOperMstVo resultVo = null;
		if (ipAllocOperMstVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {
			resultVo = allocMgmtTxService.selectIpAllocMst(ipAllocOperMstVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[] { "IP할당정보" });
		}
		return resultVo;
	}

	/* 시설 정보 조회 */
	@Transactional(readOnly = true)
	public IpAllocOperMstListVo selectListNeMst(IpAllocOperMstVo ipAllocOperMstVo) {
		IpAllocOperMstListVo resultListVo = null;
		if (ipAllocOperMstVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {

			int totalCount = 0;
			List<IpAllocOperMstVo> resultList = null;
			String sSrchTypeCd = ipAllocOperMstVo.getSneSrchTypeCd();
			/* 시설조회 시 할당 기준인지 호스트 기준인지의 처리 */
			if (sSrchTypeCd.equals("1")) {// 1. ALLOC 할당기준, 2.HOST 수용국
				totalCount = allocMgmtTxService.countListPageNeMst(ipAllocOperMstVo);
			} else {
				totalCount = allocMgmtTxService.countListPageTbIpHostMst(ipAllocOperMstVo);
			}

			ipAllocOperMstVo.setPageUnit(totalCount);

			if (sSrchTypeCd.equals("1")) {
				resultList = allocMgmtTxService.selectListPageNeMst(ipAllocOperMstVo);
			} else {
				resultList = allocMgmtTxService.selectListPageTbIpHostMst(ipAllocOperMstVo);
			}

			resultListVo = new IpAllocOperMstListVo();
			resultListVo.setIpAllocOperMstVos(resultList);
			resultListVo.setTotalCount(totalCount);

		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[] { "시설정보" });
		}
		return resultListVo;
	}

	/* IP 할당정보 상세 조회 */
	@Transactional(readOnly = true)
	public IpAllocOperMstListVo selectListIpAllocDetail(IpAllocOperMstVo ipAllocOperMstVo) {
		IpAllocOperMstListVo resultListVo = null;
		if (ipAllocOperMstVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {
			List<IpAllocOperMstVo> resultList = allocMgmtTxService.selectListIpAllocDetail(ipAllocOperMstVo);
			int totalCount = allocMgmtTxService.countListIpAllocDetail(ipAllocOperMstVo);
			resultListVo = new IpAllocOperMstListVo();
			resultListVo.setIpAllocOperMstVos(resultList);
			resultListVo.setTotalCount(totalCount);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[] { "IP할당상세정보" });
		}
		return resultListVo;
	}

	/* IP 할당정보 회선 조회 */
	@Transactional(readOnly = true)
	public TbIpAllocMstVo selectIpAllocDetail(IpAllocOperMstVo ipAllocOperMstVo) {
		TbIpAllocMstVo resultVo = null;
		if (ipAllocOperMstVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {
			resultVo = allocMgmtTxService.selectIpAllocDetail(ipAllocOperMstVo);
			if (resultVo == null) {
				if (ipAllocOperMstVo.getNipAllocMstSeq() != null) {
					throw new ServiceException("CMN.HIGH.00023", new String[] { "(원인: 할당 회선 정보 미존재) 회선상세정보" });
				} else {
					throw new ServiceException("CMN.HIGH.00023", new String[] { "(원인: 대상 노드국 회선 정보 미존재) 회선상세정보" });
				}
			}
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[] { "회선상세정보" });
		}
		return resultVo;
	}

	/* IP 할당정보 링크 조회 */
	@Transactional(readOnly = true)
	public TbIpAllocMstVo selectIpAllocDetailLink(IpAllocOperMstVo ipAllocOperMstVo) {
		TbIpAllocMstVo resultVo = null;
		if (ipAllocOperMstVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {
			resultVo = allocMgmtTxService.selectIpAllocDetailLink(ipAllocOperMstVo);
			if (resultVo == null) {
				if (ipAllocOperMstVo.getNipAllocMstSeq() != null) {
					throw new ServiceException("CMN.HIGH.00023", new String[] { "(원인: 할당 회선 정보 미존재) 회선상세정보" });
				} else {
					throw new ServiceException("CMN.HIGH.00023", new String[] { "(원인: 대상 노드국 회선 정보 미존재) 회선상세정보" });
				}
			}
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[] { "회선상세정보" });
		}
		return resultVo;
	}

	/* 할당 대상 정보 조회 */
	@Transactional(readOnly = true)
	public IpAllocOperMstListVo selectListAlcIPMstViaInMstSeq(IpAllocOperMstListVo ipAllocOperMstListVo) {
		IpAllocOperMstListVo resultListVo = null;
		if (ipAllocOperMstListVo == null || ipAllocOperMstListVo.getIpAllocOperMstVos() == null
				|| ipAllocOperMstListVo.getIpAllocOperMstVos().size() == 0) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {
			List<IpAllocOperMstVo> resultList = allocMgmtTxService.selectListAlcIPMstViaInMstSeq(ipAllocOperMstListVo);
			resultListVo = new IpAllocOperMstListVo();
			resultListVo.setIpAllocOperMstVos(resultList);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[] { "IP할당대상정보" });
		}
		return resultListVo;
	}

	/* 할당 대상 정보 조회 */
	@Transactional(readOnly = true)
	public IpAllocOperMstListVo selectListAlcIPMstViaInMstSeq2(IpAllocOperMstListVo ipAllocOperMstListVo) {
		IpAllocOperMstListVo resultListVo = null;
		if (ipAllocOperMstListVo == null || ipAllocOperMstListVo.getIpAllocOperMstVos() == null
				|| ipAllocOperMstListVo.getIpAllocOperMstVos().size() == 0) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {
			List<IpAllocOperMstVo> resultList = allocMgmtTxService.selectListAlcIPMstViaInMstSeq2(ipAllocOperMstListVo);
			resultListVo = new IpAllocOperMstListVo();
			resultListVo.setIpAllocOperMstVos(resultList);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[] { "IP할당대상정보" });
		}
		return resultListVo;
	}

	//
	/* 회선 정보 조회 */
	@Transactional(readOnly = true)
	public IpAllocOperMstListVo selectDetailSubSvcMstList(IpAllocOperMstVo ipAllocOperMstVo) {
		IpAllocOperMstListVo resultListVo = null;
		if (ipAllocOperMstVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {

			List<IpAllocOperMstVo> resultList = null;
			resultList = allocMgmtTxService.selectListSvcMst(ipAllocOperMstVo);
			resultListVo = new IpAllocOperMstListVo();
			resultListVo.setIpAllocOperMstVos(resultList);

		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[] { "회선정보" });
		}
		return resultListVo;
	}

	/* 블록 할당 처리 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void insertListAllocIPMst(IpAllocMstComplexVo ipAllocMstComplexVo) {
		try {
			if (ipAllocMstComplexVo == null || ipAllocMstComplexVo.getSrcIpAllocMstVo() == null
					|| ipAllocMstComplexVo.getDestIpAllocMstVos() == null
					|| ipAllocMstComplexVo.getDestIpAllocMstVos().size() == 0) {
				throw new ServiceException("CMN.HIGH.00001");
			}

			IpAllocOperMstVo srcIpAllocMstVo = ipAllocMstComplexVo.getSrcIpAllocMstVo();
			IpAllocOperMstVo updateIpAllocMstVo = new IpAllocOperMstVo(); // 배정테이블
			List<IpAllocOperMstVo> destIpAllocMstVos = ipAllocMstComplexVo.getDestIpAllocMstVos();

			List<TbIpAllocMstVo> insertIpAllocMstVos = new ArrayList<TbIpAllocMstVo>(); // 할당 처리 관련
			List<TbIpAssignMstVo> destIpAssignMstVos = new ArrayList<TbIpAssignMstVo>(); // 배정 처리 관련
			List<TbIpAssignSubVo> insertIpAssignSubVos = new ArrayList<TbIpAssignSubVo>(); // 선번장 처리 관련
			List<TbIpAllocNeossMstVo> linkIpAllocNeossVos = new ArrayList<TbIpAllocNeossMstVo>(); // 연동 처리 관련

			// 연동용 VO 생성
			TbIpAllocNeossMstListVo tbIpAllocNeossMstListVo = new TbIpAllocNeossMstListVo();

			String sNote = srcIpAllocMstVo.getScomment();
			String sLinkFlg = "N"; // 연동유형 체크(Y: 할당호출, O: 할당예약호출 N: 연동호출 안함.)

			String sUserId = srcIpAllocMstVo.getScreateId();

			String ssvcLineTypeCd = null;
			String sipCreateTypeCd = null;

			// 블록에 해당번호(회선일 경우 해당 회선 정보를 조회 후 값을 셋팅함.
			if (srcIpAllocMstVo.getNipAllocMstSeq() != null) {

				// 회선정보 읽기
				TbIpAllocMstVo preSrcIpAllocMstVo = allocMgmtTxService.selectIpAllocDetail(srcIpAllocMstVo);

				// 회선정보의 연동유형 셋팅
				sLinkFlg = preSrcIpAllocMstVo.getSregyn();

				CloneUtil.copyObjectInformation(preSrcIpAllocMstVo, srcIpAllocMstVo);

				for (IpAllocOperMstVo ipAllocOperMstVo : destIpAllocMstVos) {

					String sGubun = ipAllocOperMstVo.getsGubun(); // 라우팅점검에서 할당
					ssvcLineTypeCd = ipAllocOperMstVo.getSsvcLineTypeCd();
					sipCreateTypeCd = ipAllocOperMstVo.getSipCreateTypeCd();

					IpAllocOperMstVo searchIpAllocMstVo = new IpAllocOperMstVo(); // 배정테이블

					// 배정정보 확인을 위한 기준값 복사
					searchIpAllocMstVo.setSsvcLineTypeCd(ipAllocOperMstVo.getSsvcLineTypeCd());
					searchIpAllocMstVo.setSipCreateTypeCd(ipAllocOperMstVo.getSipCreateTypeCd());
					searchIpAllocMstVo.setNipAssignMstSeq(ipAllocOperMstVo.getNipAssignMstSeq());
					searchIpAllocMstVo.setSassignTypeCd(ipAllocOperMstVo.getSassignTypeCd());
					searchIpAllocMstVo.setSgatewayip(ipAllocOperMstVo.getSgatewayip());

					/* 중복 회선 처리 */
					BigInteger iAllcnt = BigInteger.ZERO;
					iAllcnt = ipAllocOperMstVo.getNipAllocMstCnt();
					iAllcnt = iAllcnt.add(BigInteger.ONE);
					searchIpAllocMstVo.setNipAllocMstCnt(iAllcnt);

					// 상품정보 조회
					TbIpAllocMstVo searchIpmsSvcMstVo = new TbIpAllocMstVo();
					searchIpmsSvcMstVo.setSassignTypeCd(ipAllocOperMstVo.getSassignTypeCd());
					searchIpmsSvcMstVo.setSsvcUseTypeCd(preSrcIpAllocMstVo.getSsvcUseTypeCd());
					searchIpmsSvcMstVo.setSexSvcCd(preSrcIpAllocMstVo.getSexSvcCd());
					BigInteger nipmsSvcMstSeq = allocMgmtTxService.selectIpmsSvcMstSeq(searchIpmsSvcMstVo);
					if (nipmsSvcMstSeq == null) {
						throw new ServiceException("APP.HIGH.00033", new String[] { " (원인 : 상품정보 미존재) IP블록 할당" });
					}

					IpmsSvcVo searchIpmsSvc = new IpmsSvcVo();
					searchIpmsSvc.setNipmsSvcSeq(nipmsSvcMstSeq);
					List<IpmsSvcVo> resultListIpmsSvc = allocMgmtTxService.selectIpmsSvc(searchIpmsSvc);
					if (resultListIpmsSvc == null || resultListIpmsSvc.size() == 0) {
						throw new ServiceException("APP.HIGH.00033", new String[] { " (원인 : 상품정보 미존재) IP블록 할당" });
					}
					if (preSrcIpAllocMstVo.getSsvcLineTypeCd().equals("CL0001")
							|| preSrcIpAllocMstVo.getSsvcLineTypeCd().equals("CL0002")) {
						boolean isAvail = false;
						for (IpmsSvcVo ipmsSvcVo : resultListIpmsSvc) {
							if (ipmsSvcVo.getSsvcLineTypeCd().equals("CL0001")
									|| ipmsSvcVo.getSsvcLineTypeCd().equals("CL0002")) {
								isAvail = true;
								break;
							}
						}
						if (!isAvail) {
							throw new ServiceException("LNK.WARN.00047", new String[] { "해당 회선은 KORNET과 PREMIUM망" });
						}

					} else if (preSrcIpAllocMstVo.getSsvcLineTypeCd().equals("CL0005")) {
						boolean isAvail = false;
						for (IpmsSvcVo ipmsSvcVo : resultListIpmsSvc) {
							if (ipmsSvcVo.getSsvcLineTypeCd().equals(preSrcIpAllocMstVo.getSsvcLineTypeCd())) {
								isAvail = true;
								break;
							}
						}
						if (!isAvail) {
							throw new ServiceException("LNK.WARN.00047", new String[] { "해당 회선은 VPN망" });
						}
					}

					// 입력 대상 정보에 회선정보 매핑
					TbIpAllocMstVo mapIpAllocMstVo = new TbIpAllocMstVo();
					CloneUtil.copyObjectInformation(preSrcIpAllocMstVo, mapIpAllocMstVo);
					mapIpAllocMstVo.setNipmsSvcSeq(resultListIpmsSvc.get(0).getNipmsSvcSeq());
					mapIpAllocMstVo.setSsvcUseTypeCd(resultListIpmsSvc.get(0).getSsvcUseTypeCd());
					mapIpAllocMstVo.setSassignTypeCd(resultListIpmsSvc.get(0).getSassignTypeCd());
					mapIpAllocMstVo.setSexSvcCd(resultListIpmsSvc.get(0).getSexSvcCd());

					CloneUtil.copyObjectInformation(mapIpAllocMstVo, ipAllocOperMstVo);

					// 입력 대상 정보에 배정정보 매핑 (IP블록 기본정보)
					updateIpAllocMstVo = allocMgmtTxService.selectIpAllocMst(searchIpAllocMstVo);
					CloneUtil.copyIpVoInfomation(updateIpAllocMstVo, ipAllocOperMstVo);

					ipAllocOperMstVo.setSsvcLineTypeCd(searchIpAllocMstVo.getSsvcLineTypeCd());
					ipAllocOperMstVo.setSipCreateTypeCd(searchIpAllocMstVo.getSipCreateTypeCd());
					ipAllocOperMstVo.setNipAssignMstSeq(searchIpAllocMstVo.getNipAssignMstSeq());
					ipAllocOperMstVo.setSassignTypeCd(searchIpAllocMstVo.getSassignTypeCd());
					ipAllocOperMstVo.setNipAllocMstCnt(searchIpAllocMstVo.getNipAllocMstCnt());
					ipAllocOperMstVo.setSgatewayip(searchIpAllocMstVo.getSgatewayip());
					ipAllocOperMstVo.setSexPushYn("Y"); // insert 시점에 고정(회선 - 연동정보 IPMS=> NEOSS)
					ipAllocOperMstVo.setNticketActSeq(BigInteger.ZERO); // insert 시점에 고정
					ipAllocOperMstVo.setScreateId(sUserId);
					ipAllocOperMstVo.setSmodifyId(sUserId);
					ipAllocOperMstVo.setScomment(sNote);

					if (sLinkFlg.equals("O")) { // O준공대기 => 할당예약 호출
						ipAllocOperMstVo.setSassignLevelCd("IA0005"); // updateTbIpAllocMstVo Neoss 연동시 할당예약
					} else {
						ipAllocOperMstVo.setSassignLevelCd("IA0006"); // updateTbIpAllocMstVo Neoss 연동시 할당
					}

					// 등록 할당 데이터 복제
					TbIpAllocMstVo insertIpAllocMstVo = new TbIpAllocMstVo();
					CloneUtil.copyObjectInformation(ipAllocOperMstVo, insertIpAllocMstVo);

					// ip 할당(회선 정보) 추가 복사
					insertIpAllocMstVo.setNipAllocNeossSeq(mapIpAllocMstVo.getNipAllocNeossSeq());
					insertIpAllocMstVo.setSordernum(mapIpAllocMstVo.getSordernum());
					insertIpAllocMstVo.setSodrCloseTypeCd(mapIpAllocMstVo.getSodrCloseTypeCd());
					insertIpAllocMstVo.setSlacpsid(mapIpAllocMstVo.getSlacpsid()); //
					insertIpAllocMstVo.setSsubscnescode(mapIpAllocMstVo.getSsubscnescode());
					insertIpAllocMstVo.setSsubsclgipportseq(mapIpAllocMstVo.getSsubsclgipportseq());
					insertIpAllocMstVo.setSsubsclgipportdescription(mapIpAllocMstVo.getSsubsclgipportdescription());
					insertIpAllocMstVo.setSsubsclgipportip(mapIpAllocMstVo.getSsubsclgipportip());
					insertIpAllocMstVo.setSsubscrouterserialip(mapIpAllocMstVo.getSsubscrouterserialip());
					insertIpAllocMstVo.setSconnalias(mapIpAllocMstVo.getSconnalias());
					insertIpAllocMstVo.setScustname(mapIpAllocMstVo.getScustname());
					insertIpAllocMstVo.setSresultCd(mapIpAllocMstVo.getSresultCd());
					insertIpAllocMstVo.setSresultMsg(mapIpAllocMstVo.getSresultMsg());
					insertIpAllocMstVo.setSinstalladdress(mapIpAllocMstVo.getSinstalladdress());
					insertIpAllocMstVo.setSinstallroadaddress(mapIpAllocMstVo.getSinstallroadaddress());
					insertIpAllocMstVo.setSodrhopedt(mapIpAllocMstVo.getSodrhopedt());
					insertIpAllocMstVo.setSodrregdt(mapIpAllocMstVo.getSodrregdt());
					insertIpAllocMstVo.setSreportOpinion(mapIpAllocMstVo.getSreportOpinion());
					insertIpAllocMstVo.setsGubun(sGubun);

					insertIpAllocMstVos.add(insertIpAllocMstVo);

					// 등록 선번장 데이터 복제
					TbIpAssignSubVo insertIpAssignSubVo = new TbIpAssignSubVo();
					CloneUtil.copyObjectInformation(ipAllocOperMstVo, insertIpAssignSubVo);
					insertIpAssignSubVos.add(insertIpAssignSubVo);

					// 수정 배정 데이터 복제
					TbIpAssignMstVo updateIpAssignMstVo = new TbIpAssignMstVo();
					CloneUtil.copyObjectInformation(ipAllocOperMstVo, updateIpAssignMstVo);
					updateIpAssignMstVo.setSipAllocExTypeCd("AE0003"); // AE0000 (강제할당처리), AE0003(웹서비스)
					updateIpAssignMstVo.setSortType("I"); // 등록 예외 처리를 위한 구분자
					destIpAssignMstVos.add(updateIpAssignMstVo);

					// 연동 대상 데이터 복제
					TbIpAllocNeossMstVo linkAllocNeossMstVo = new TbIpAllocNeossMstVo();

					linkAllocNeossMstVo.setSordernum(insertIpAllocMstVo.getSordernum()); // sordernum NeOSS 오더번호
					linkAllocNeossMstVo.setSaid(insertIpAllocMstVo.getSsaid()); // ssaid 서비스계약ID
					linkAllocNeossMstVo.setRegyn("Y"); // 할당요청/취소 구분 [ 예약 = Y, 취소 = N ]
					linkAllocNeossMstVo.setIpmsSvcSeq(insertIpAllocMstVo.getNipmsSvcSeq()); // nipms_svc_seq  IPMS 상품MST
																							// Seq
					linkAllocNeossMstVo.setAssignTypeCd(insertIpAllocMstVo.getSassignTypeCd()); // sassign_type_cd 
																								// IP할당유형
					linkAllocNeossMstVo.setExSvcCd(insertIpAllocMstVo.getSexSvcCd()); // sex_svc_cd
					linkAllocNeossMstVo.setSvcUseTypeCd(insertIpAllocMstVo.getSsvcUseTypeCd()); // ssvc_use_type_cd 서비스
																								// 이용목정 [사업용 = SU0001 /
																								// 일반용 = SU0002 ]
					linkAllocNeossMstVo.setIpVersionTypeCd(insertIpAllocMstVo.getSipVersionTypeCd()); // sip_version_type_cd
																										// IPv6 =
																										// CV0002/ IPv4
																										// CV0001
					linkAllocNeossMstVo.setIpCreateTypeCd(insertIpAllocMstVo.getSipCreateTypeCd()); // sip_create_type_cd
																									//   CT0001 = 공인 
					linkAllocNeossMstVo.setIpBlock(insertIpAllocMstVo.getSipBlock()); // sip_block     fe01:1:1::
					linkAllocNeossMstVo.setIpbitmask(insertIpAllocMstVo.getNbitmask()); // nbitmask      64
					linkAllocNeossMstVo.setSipaddr(insertIpAllocMstVo.getSfirstAddr()); // sfirst_addr  fe01:1:1::
					linkAllocNeossMstVo.setEipaddr(insertIpAllocMstVo.getSlastAddr()); // slast_addr
																						// fe01:1:1:0:ffff:ffff:ffff:ffff
					linkAllocNeossMstVo.setNipAssignMstSeq(insertIpAllocMstVo.getNipAssignMstSeq()); // nip_assign_mst_seq
																										// 48
					linkAllocNeossMstVo.setGatewayip(insertIpAllocMstVo.getSgatewayip()); // sgatewayip
																							// fe01:1:1:0:ffff:ffff:ffff:ffff
					// 2014.11.13 연동 변경에 따른 추가(이중회선, 망코드 추가)
					if (ipAllocOperMstVo.getNipAllocMstCnt().equals(BigInteger.ONE)) {
						linkAllocNeossMstVo.setSlacpBlockYN("N");
					} else {
						linkAllocNeossMstVo.setSlacpBlockYN("Y");
					}
					linkAllocNeossMstVo.setssvcLineTypeCd(insertIpAllocMstVo.getSsvcLineTypeCd());

					linkIpAllocNeossVos.add(linkAllocNeossMstVo);

				}

			} else {// 시설일 경우 해당 정보를 입력함. // 링크할당일 경우도 포함 (2021.05.07 추가)

				// 미연동(시설)의 연동유형 셋팅
				sLinkFlg = "N";

				// 장비정보 셋팅
				for (IpAllocOperMstVo ipAllocOperMstVo : destIpAllocMstVos) {

					String sGubun = ipAllocOperMstVo.getsGubun(); // 라우팅점검에서 할당

					IpAllocOperMstVo searchIpAllocMstVo = new IpAllocOperMstVo(); // 배정테이블

					ssvcLineTypeCd = ipAllocOperMstVo.getSsvcLineTypeCd();
					sipCreateTypeCd = ipAllocOperMstVo.getSipCreateTypeCd();

					// 배정정보 확인을 위한 기준값 복사
					searchIpAllocMstVo.setSsvcLineTypeCd(ipAllocOperMstVo.getSsvcLineTypeCd());
					searchIpAllocMstVo.setSipCreateTypeCd(ipAllocOperMstVo.getSipCreateTypeCd());
					searchIpAllocMstVo.setNipAssignMstSeq(ipAllocOperMstVo.getNipAssignMstSeq());
					searchIpAllocMstVo.setSassignTypeCd(ipAllocOperMstVo.getSassignTypeCd());
					searchIpAllocMstVo.setSgatewayip(ipAllocOperMstVo.getSgatewayip());

					/* 중복 회선 처리 */
					BigInteger iAllcnt = BigInteger.ZERO;
					iAllcnt = ipAllocOperMstVo.getNipAllocMstCnt();
					iAllcnt = iAllcnt.add(BigInteger.ONE);
					searchIpAllocMstVo.setNipAllocMstCnt(iAllcnt);

					// 입력 대상 정보에 배정정보 매핑 (IP블록 기본정보)
					updateIpAllocMstVo = allocMgmtTxService.selectIpAllocMst(searchIpAllocMstVo);
					CloneUtil.copyIpVoInfomation(updateIpAllocMstVo, ipAllocOperMstVo);

					ipAllocOperMstVo.setSsvcLineTypeCd(searchIpAllocMstVo.getSsvcLineTypeCd());
					ipAllocOperMstVo.setSipCreateTypeCd(searchIpAllocMstVo.getSipCreateTypeCd());
					ipAllocOperMstVo.setNipAssignMstSeq(searchIpAllocMstVo.getNipAssignMstSeq());
					ipAllocOperMstVo.setSassignTypeCd(searchIpAllocMstVo.getSassignTypeCd());
					ipAllocOperMstVo.setNipAllocMstCnt(searchIpAllocMstVo.getNipAllocMstCnt());

					ipAllocOperMstVo.setSregyn("Y");
					// ipAllocOperMstVo.setSsaId(ssaid); //insert 시점에 체크
					ipAllocOperMstVo.setSicisofficescode(srcIpAllocMstVo.getSicisofficescode());
					ipAllocOperMstVo.setSofficecode(srcIpAllocMstVo.getSofficecode()); /* 수용국 복사 */
					// ipAllocOperMstVo.setSlacpsid(slacpsid); //insert 시점에 체크
					ipAllocOperMstVo.setSsubscnnescode(srcIpAllocMstVo.getSsubscnnescode());
					ipAllocOperMstVo.setSsubscmstip(srcIpAllocMstVo.getSsubscmstip());
					ipAllocOperMstVo.setSsubscnealias(srcIpAllocMstVo.getSsubscnealias());
					ipAllocOperMstVo.setSmodelname(srcIpAllocMstVo.getSmodelname());
					ipAllocOperMstVo.setSllnum(srcIpAllocMstVo.getSllnum());
					ipAllocOperMstVo.setNipmsSvcSeq(BigInteger.ZERO); // insert 시점에 고정
					ipAllocOperMstVo.setSsvcUseTypeCd("SU0000");
					ipAllocOperMstVo.setSgatewayip(searchIpAllocMstVo.getSgatewayip());
					ipAllocOperMstVo.setSexPushYn("O");
					ipAllocOperMstVo.setNticketActSeq(BigInteger.ZERO); // insert 시점에 고정
					ipAllocOperMstVo.setScreateId(srcIpAllocMstVo.getScreateId());
					ipAllocOperMstVo.setSmodifyId(srcIpAllocMstVo.getSmodifyId());
					ipAllocOperMstVo.setScomment(sNote);
					ipAllocOperMstVo.setSsubsclgipportdescription(srcIpAllocMstVo.getSsubsclgipportdescription()); // IF명

					ipAllocOperMstVo.setSassignLevelCd("IA0006"); // updateTbIpAllocMstVo

					if (null != srcIpAllocMstVo.getNipLinkMstSeq()) {
						ipAllocOperMstVo.setNipLinkMstSeq(srcIpAllocMstVo.getNipLinkMstSeq()); // 링크마스터seq
						ipAllocOperMstVo.setSsubsclgipportdescription(srcIpAllocMstVo.getSsubsclgipportdescription()); // 자국IF명
						ipAllocOperMstVo.setSofficecode(srcIpAllocMstVo.getSicisofficescode()); /* 수용국 복사 */
						ipAllocOperMstVo.setSconnalias(srcIpAllocMstVo.getSconnAlias()); // 수용회선명
					}

					// 등록 할당 데이터 복제
					TbIpAllocMstVo insertIpAllocMstVo = new TbIpAllocMstVo();
					CloneUtil.copyObjectInformation(ipAllocOperMstVo, insertIpAllocMstVo);
					insertIpAllocMstVo.setsGubun(sGubun);
					insertIpAllocMstVos.add(insertIpAllocMstVo);

					// 등록 선번장 데이터 복제
					TbIpAssignSubVo insertIpAssignSubVo = new TbIpAssignSubVo();
					CloneUtil.copyObjectInformation(ipAllocOperMstVo, insertIpAssignSubVo);
					insertIpAssignSubVos.add(insertIpAssignSubVo);

					// 수정 배정 데이터 복제
					TbIpAssignMstVo updateIpAssignMstVo = new TbIpAssignMstVo();
					CloneUtil.copyObjectInformation(ipAllocOperMstVo, updateIpAssignMstVo);
					updateIpAssignMstVo.setNipmsSvcSeq(null);
					updateIpAssignMstVo.setSexSvcCd(null);
					updateIpAssignMstVo.setSipAllocExTypeCd("AE0000"); // AE0000 (강제할당처리), AE0003(웹서비스)
					updateIpAssignMstVo.setSortType("I"); // 등록 예외 처리를 위한 구분자
					destIpAssignMstVos.add(updateIpAssignMstVo);

				}
			}

			/* 스쿨넷은 외부 연동 없음 */
			if (!ssvcLineTypeCd.equals("CL0008")) {

				/* 사설IP 는 연동 없음 */
				if (!sipCreateTypeCd.equals("CT0005")) {
					// 1. 연동 처리 정보 호출부(연동 결과에 따른 처리는 연동 추가 후 진행예정)
					if (sLinkFlg.equals("O")) { // 할당예약 웹서비스 호출
						tbIpAllocNeossMstListVo.setTbIpAllocNeossMstVos(linkIpAllocNeossVos);
						neOSSConsumeAdapterService.insertReservedIPList(tbIpAllocNeossMstListVo);
					} else if (sLinkFlg.equals("Y")) { // 할당 웹서비스 호출
						tbIpAllocNeossMstListVo.setTbIpAllocNeossMstVos(linkIpAllocNeossVos);
						neOSSConsumeAdapterService.insertFixedIPList(tbIpAllocNeossMstListVo);
					}

				}

			}

			// 2. 할당 처리
			allocMgmtTxService.processInsertIPAllocMst(insertIpAllocMstVos);

			// 할당 처리 key Mapping(
			for (TbIpAllocMstVo keyIpAllocOperMstVo : insertIpAllocMstVos) {
				for (TbIpAssignSubVo desIpAllocOperMstVo : insertIpAssignSubVos) {
					if (keyIpAllocOperMstVo.getPipPrefix().equals(desIpAllocOperMstVo.getPipPrefix())) {
						desIpAllocOperMstVo.setNipAllocMstSeq(keyIpAllocOperMstVo.getNipAllocMstSeq());
					}
				}
			}

			// 3. 할당 서브(선번장) 처리
			lineMgmtAdapterService.insertIpAssignSub(insertIpAssignSubVos);

			// 4. 배정 변경(할당) 처리
			assignMgmtAdapterService.updateAlocIpAssignMst(destIpAssignMstVos);

			// 5. Ticket정보 처리(할당)
			for (TbIpAssignMstVo destTicketTbIpAssignMstVo : destIpAssignMstVos) {
				/*
				 * Step 1. 배정블록 조회
				 * - Method 호출 및 리턴 셋팅
				 */
				TbIpAssignMstVo srchTicketIpAssignMstVo = new TbIpAssignMstVo();
				TbIpAssignMstVo insertTicketTbIpAssignMstVo = new TbIpAssignMstVo();

				TbTicketMstVo tbTicketMstVo = new TbTicketMstVo();

				TbIpmsSvcMstVo srchTicketTbIpmsSvcMstVo = new TbIpmsSvcMstVo();
				TbIpmsSvcMstVo ssvcTicketTbIpmsSvcMstVo = new TbIpmsSvcMstVo();

				srchTicketIpAssignMstVo.setNipAssignMstSeq(destTicketTbIpAssignMstVo.getNipAssignMstSeq());
				insertTicketTbIpAssignMstVo = assignMgmtAdapterService.selectIpAssignMst(srchTicketIpAssignMstVo);

				/*
				 * Step 2. 변경 Param Setting (회선, 시설)
				 * - Step 1의 정보에 상단 셋팅 값 input
				 * - tbTicketMstVo에 블록 정보 (배정) 셋팅 및 기본값 셋팅
				 */
				insertTicketTbIpAssignMstVo.setSassignLevelCd(destTicketTbIpAssignMstVo.getSassignLevelCd());
				insertTicketTbIpAssignMstVo.setNipmsSvcSeq(destTicketTbIpAssignMstVo.getNipmsSvcSeq());
				insertTicketTbIpAssignMstVo.setSexSvcCd(destTicketTbIpAssignMstVo.getSexSvcCd());

				CloneUtil.copyObjectInformation(insertTicketTbIpAssignMstVo, tbTicketMstVo);
				tbTicketMstVo.setNticketTypeSeq(CommonCodeUtil.TICKET_TYPE_IP_ALLOC_ALLOC);
				tbTicketMstVo.setScreateId(destTicketTbIpAssignMstVo.getSmodifyId());

				/*
				 * Step 3. 상품정보 셋팅
				 * - 상단 정보에 대하여 상품코드 존재시 해당 정보 셋팅
				 */
				if (destTicketTbIpAssignMstVo.getNipmsSvcSeq() != null) {
					srchTicketTbIpmsSvcMstVo.setNipmsSvcSeq(destTicketTbIpAssignMstVo.getNipmsSvcSeq());
					ssvcTicketTbIpmsSvcMstVo = svcMgmtAdapterService.selectTbIpmsSvcMstVo(srchTicketTbIpmsSvcMstVo);
					tbTicketMstVo.setSipmsSvcNm(ssvcTicketTbIpmsSvcMstVo.getSipmsSvcNm());
					tbTicketMstVo.setSsvcUseTypeCd(ssvcTicketTbIpmsSvcMstVo.getSsvcUseTypeCd());

				}

				/* Step 4. 티켓 처리 */
				ticketMgmtAdapterService.insertTicketMst(tbTicketMstVo);

			}

			/* 스쿨넷은 외부 연동 없음 */
			if (!ssvcLineTypeCd.equals("CL0008")) {

				/* 공인IP 만 Whois 연동 */
				if (sipCreateTypeCd.equals("CT0001")) {

					/***************************
					 * Whois 전송
					 ***************************/
					if (insertIpAllocMstVos.size() > 0) {
						BigInteger allocMstSeq = null;
						String sessionId = null;
						for (int i = 0; i < insertIpAllocMstVos.size(); i++) {
							allocMstSeq = insertIpAllocMstVos.get(i).getNipAllocMstSeq();
							PrintLogUtil.printLog("#####ALLOC MST SEQ: " + allocMstSeq);
							sessionId = ipAllocMstComplexVo.getSmodifyId();

							// Whois 전송 예외
							boolean rtnExceptVal = true;
							rtnExceptVal = whoisAdapterService.exceptWhois("NEW", allocMstSeq, null);
							if (rtnExceptVal) {
								whoisAdapterService.sendToWhoisWithDbNoRtn(sessionId, allocMstSeq, "NEW");
							}
						}
					}

				}

				/* 사설IP 는 라우팅 점검 연동 없음 */
				if (!sipCreateTypeCd.equals("CT0005")) {
					/***************************
					 * 라우팅 점검 업데이트
					 ***************************/
					for (int i = 0; i < insertIpAllocMstVos.size(); i++) {
						// if(insertIpAllocMstVos.get(i).getsGubun() == null ||
						// insertIpAllocMstVos.get(i).getsGubun() == "") { //Codeeyes-Urgent-객체 비교시 == ,
						// != 사용제한
						if (insertIpAllocMstVos.get(i).getsGubun() == null
								|| insertIpAllocMstVos.get(i).getsGubun().equals("")) {
							PrintLogUtil.printLog(
									"#####ASSIGN MST SEQ: " + insertIpAllocMstVos.get(i).getNipAssignMstSeq());
							TbRoutChkMstVo tbRoutChkMstVo = new TbRoutChkMstVo();
							tbRoutChkMstVo.setNipAssignMstSeq(insertIpAllocMstVos.get(i).getNipAssignMstSeq());
							routMgmtTxService.updateRoutMgmtSeq(tbRoutChkMstVo);
						}
					}
				}

			}

			/* 이력관리 이력 등록 */
			for (TbIpAssignMstVo destHistoryTbIpAssignMstVo : destIpAssignMstVos) {

				IpHistoryMstVo insertHistoryTbIpAllocMstVo = new IpHistoryMstVo();
				IpHistoryMstVo srchHistoryTbIpAllocMstVo = new IpHistoryMstVo();

				IpHistoryMstVo ipHistoryMstVo = new IpHistoryMstVo();

				if (insertIpAllocMstVos != null && insertIpAllocMstVos.size() > 0) {

					for (TbIpAllocMstVo keyIpAllocOperMstVo : insertIpAllocMstVos) {

						if (keyIpAllocOperMstVo.getNipAssignMstSeq()
								.equals(destHistoryTbIpAssignMstVo.getNipAssignMstSeq())) {

							srchHistoryTbIpAllocMstVo
									.setNipAssignMstSeq(destHistoryTbIpAssignMstVo.getNipAssignMstSeq());
							srchHistoryTbIpAllocMstVo.setNipAllocMstSeq(keyIpAllocOperMstVo.getNipAllocMstSeq());
							insertHistoryTbIpAllocMstVo = historyMgmtAdapterService
									.selectAllocIpInfo(srchHistoryTbIpAllocMstVo);

							CloneUtil.copyObjectInformation(insertHistoryTbIpAllocMstVo, ipHistoryMstVo);

							if (!StringUtils.hasText(ipAllocMstComplexVo.getMenuType())) {
								ipHistoryMstVo.setsMenuHistCd("Aloc");
								// ipHistoryMstVo.setSmenuNm("IP 할당");
								// ipHistoryMstVo.setSmenuId("M00009"); // IP 할당
							} else if (ipAllocMstComplexVo.getMenuType().equals("Rout")) {
								ipHistoryMstVo.setsMenuHistCd("Rout");
								// ipHistoryMstVo.setSmenuNm("IP주소 라우팅 비교/점검");
								// ipHistoryMstVo.setSmenuId("M00118"); // IP주소 라우팅 비교/점검
							} else if (ipAllocMstComplexVo.getMenuType().equals("Upload")) {
								ipHistoryMstVo.setsMenuHistCd("Upload"); // 업로드관리
							} else {
								ipHistoryMstVo.setsMenuHistCd("Aloc");
								// ipHistoryMstVo.setSmenuNm("IP 할당");
								// ipHistoryMstVo.setSmenuId("M00009"); // IP 할당
							}

							if (sLinkFlg.equals("O")) { // 할당예약
								ipHistoryMstVo.setNipHistTaskCd(CommonCodeUtil.HIST_TASK_ALLOC_RESV); // 할당예약
							} else { // 할당
								ipHistoryMstVo.setNipHistTaskCd(CommonCodeUtil.HIST_TASK_ALLOC); // 할당
							}

							ipHistoryMstVo.setScreateId(destHistoryTbIpAssignMstVo.getScreateId());
							historyMgmtAdapterService.insertHistoryMst(ipHistoryMstVo);
						}
					}
				}

			}

		} catch (ServiceException e) {
			e.printStackTrace();
			PrintLogUtil.printLogInfo(e.toString());
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			PrintLogUtil.printLogInfo(e.toString());
			throw new ServiceException("APP.HIGH.00033", new String[] { "IP블록 할당" });
		}
	}

	/* 블록 해지 처리 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteListAllocIPMst(IpAllocMstComplexVo ipAllocMstComplexVo) {
		try {
			if (ipAllocMstComplexVo == null || ipAllocMstComplexVo.getSrcIpAllocMstVo() == null
					|| ipAllocMstComplexVo.getDestIpAllocMstVos() == null
					|| ipAllocMstComplexVo.getDestIpAllocMstVos().size() == 0) {
				throw new ServiceException("CMN.HIGH.00001");
			}

			IpAllocOperMstVo srcIpAllocMstVo = ipAllocMstComplexVo.getSrcIpAllocMstVo();
			// IpAllocOperMstVo updateIpAllocMstVo = new IpAllocOperMstVo(); //배정테이블
			List<IpAllocOperMstVo> destIpAllocMstVos = ipAllocMstComplexVo.getDestIpAllocMstVos();

			List<TbIpAssignSubVo> deleteIpAssignSubVos = new ArrayList<TbIpAssignSubVo>();
			List<TbIpAllocMstVo> deleteIpAllocMstVos = new ArrayList<TbIpAllocMstVo>();
			List<IpHistoryMstVo> deleteIpHistMstVos = new ArrayList<IpHistoryMstVo>();
			List<TbIpAssignMstVo> updateIpAssignMstVos = new ArrayList<TbIpAssignMstVo>();
			List<TbIpAllocNeossMstVo> linkIpAllocNeossVos = new ArrayList<TbIpAllocNeossMstVo>(); // 연동 처리 관련

			// 연동용 VO 생성
			TbIpAllocNeossMstListVo tbIpAllocNeossMstListVo = new TbIpAllocNeossMstListVo();

			String sLinkFlg = "N"; // 연동유형 체크(Y: 할당해지호출, O: 할당예약해지호출 N: 연동호출 안함.)
			String sLinkFlgExPush = "O"; // 연동유형 체크(Y: IPMS=> NEOSS, O: 수동 N: NEOSS=> IPMS.)
			String sLinkFlgSreg = "N"; // 연동유형 체크(Y: 할당, O: 할당예약 N: 없음.)

			String ssvcLineTypeCd = null;
			String sipCreateTypeCd = null;

			// 블록에 해당번호(회선일 경우 해당 회선 정보를 조회 후 값을 셋팅함.
			if (srcIpAllocMstVo.getNipAssignMstSeq() != null && srcIpAllocMstVo.getNipAllocMstSeq() != null) {

				// 배정정보 읽기
				IpAllocOperMstVo preSrcIpAllocMstVo = allocMgmtTxService.selectIpAllocMst(srcIpAllocMstVo);

				/* 중복 회선 처리 */
				BigInteger iAllcnt = BigInteger.ZERO;
				iAllcnt = preSrcIpAllocMstVo.getNipAllocMstCnt();

				ssvcLineTypeCd = preSrcIpAllocMstVo.getSsvcLineTypeCd();
				sipCreateTypeCd = preSrcIpAllocMstVo.getSipCreateTypeCd();

				for (IpAllocOperMstVo ipAllocOperMstVo : destIpAllocMstVos) {

					// 연동 정보 처리를 위한 IP정보 복제
					CloneUtil.copyIpVoInfomation(preSrcIpAllocMstVo, ipAllocOperMstVo);

					// 회선정보 읽기
					TbIpAllocMstVo preDtlSrcIpAllocMstVo = allocMgmtTxService.selectIpAllocDetail(srcIpAllocMstVo);

					// 회선정보의 연동유형 셋팅
					// sLinkFlg = preDtlSrcIpAllocMstVo.getSexPushYn();
					sLinkFlgExPush = preDtlSrcIpAllocMstVo.getSexPushYn();
					sLinkFlgSreg = preDtlSrcIpAllocMstVo.getSregyn();

					if (sLinkFlgExPush.endsWith("O")) {
						sLinkFlg = "N";
					} else {
						sLinkFlg = sLinkFlgSreg;
					}

					iAllcnt = iAllcnt.subtract(BigInteger.ONE);

					if (iAllcnt.equals(BigInteger.ZERO)) { // 배정블럭의 해당 값 초기화
						ipAllocOperMstVo.setSassignLevelCd("IA0004"); // updateTbIpAllocMstVo (0일 경우 서비스배정으로 변경)
						ipAllocOperMstVo.setNipmsSvcSeq(BigInteger.ZERO);
						ipAllocOperMstVo.setSexSvcCd(null);
						ipAllocOperMstVo.setSipAllocExTypeCd("AE0000");
					}

					ipAllocOperMstVo.setNipAllocMstCnt(iAllcnt);
					ipAllocOperMstVo.setSmodifyId(srcIpAllocMstVo.getSmodifyId());

					// 삭제대상 선번장 데이터 복제
					TbIpAssignSubVo deleteIpAssignSubVo = new TbIpAssignSubVo();
					CloneUtil.copyObjectInformation(ipAllocOperMstVo, deleteIpAssignSubVo);
					deleteIpAssignSubVos.add(deleteIpAssignSubVo);

					// 삭제대상 할당 데이터 복제
					TbIpAllocMstVo deleteIpAllocMstVo = new TbIpAllocMstVo();
					CloneUtil.copyObjectInformation(ipAllocOperMstVo, deleteIpAllocMstVo);
					deleteIpAllocMstVos.add(deleteIpAllocMstVo);

					// 수정 배정 데이터 복제
					TbIpAssignMstVo updateIpAssignMstVo = new TbIpAssignMstVo();
					CloneUtil.copyObjectInformation(ipAllocOperMstVo, updateIpAssignMstVo);
					updateIpAssignMstVo.setSortType("D"); // 삭제 예외 처리를 위한 구분자
					updateIpAssignMstVos.add(updateIpAssignMstVo);

					// 이력 데이터 복제 (이력관리)
					IpHistoryMstVo deleteIpHistMstVo = new IpHistoryMstVo();
					CloneUtil.copyObjectInformation(ipAllocOperMstVo, deleteIpHistMstVo);

					deleteIpHistMstVo.setNipAllocMstSeq(deleteIpAllocMstVo.getNipAllocMstSeq());
					deleteIpHistMstVo.setNipAllocNeossSeq(preDtlSrcIpAllocMstVo.getNipAllocNeossSeq());
					deleteIpHistMstVo.setSordernum(preDtlSrcIpAllocMstVo.getSordernum());
					deleteIpHistMstVo.setSregyn(preDtlSrcIpAllocMstVo.getSregyn());
					deleteIpHistMstVo.setSodrCloseTypeCd(preDtlSrcIpAllocMstVo.getSodrCloseTypeCd());
					deleteIpHistMstVo.setSsaid(preDtlSrcIpAllocMstVo.getSsaid());
					deleteIpHistMstVo.setSllnum(preDtlSrcIpAllocMstVo.getSllnum());
					deleteIpHistMstVo.setSicisofficescode(preDtlSrcIpAllocMstVo.getSicisofficescode());
					deleteIpHistMstVo.setSlacpsid(preDtlSrcIpAllocMstVo.getSlacpsid());
					deleteIpHistMstVo.setSsubscnescode(preDtlSrcIpAllocMstVo.getSsubscnescode());
					deleteIpHistMstVo.setSsubscmstip(preDtlSrcIpAllocMstVo.getSsubscmstip());
					deleteIpHistMstVo.setSsubsclgipportseq(preDtlSrcIpAllocMstVo.getSsubsclgipportseq());
					deleteIpHistMstVo
							.setSsubsclgipportdescription(preDtlSrcIpAllocMstVo.getSsubsclgipportdescription());
					deleteIpHistMstVo.setSsubsclgipportip(preDtlSrcIpAllocMstVo.getSsubsclgipportip());
					deleteIpHistMstVo.setSsubscrouterserialip(preDtlSrcIpAllocMstVo.getSsubscrouterserialip());
					deleteIpHistMstVo.setSsubscnealias(preDtlSrcIpAllocMstVo.getSsubscnealias());
					deleteIpHistMstVo.setSconnalias(preDtlSrcIpAllocMstVo.getSconnalias());
					deleteIpHistMstVo.setSmodelname(preDtlSrcIpAllocMstVo.getSmodelname());
					deleteIpHistMstVo.setScustname(preDtlSrcIpAllocMstVo.getScustname());
					deleteIpHistMstVo.setNcnt(preDtlSrcIpAllocMstVo.getNcnt());
					deleteIpHistMstVo.setNclassCnt(preDtlSrcIpAllocMstVo.getNclassCnt());

					deleteIpHistMstVo.setNipmsSvcSeq(deleteIpAllocMstVo.getNipmsSvcSeq());
					deleteIpHistMstVo.setSsvcUseTypeCd(preDtlSrcIpAllocMstVo.getSsvcUseTypeCd());
					deleteIpHistMstVo.setSexSvcCd(preDtlSrcIpAllocMstVo.getSexSvcCd());

					deleteIpHistMstVos.add(deleteIpHistMstVo);

					// 연동 대상 데이터 복제
					TbIpAllocNeossMstVo linkAllocNeossMstVo = new TbIpAllocNeossMstVo();

					if (sLinkFlg.equals("O") || sLinkFlg.equals("Y")) {
						linkAllocNeossMstVo.setSordernum(preDtlSrcIpAllocMstVo.getSordernum()); // sordernum NeOSS 오더번호
						linkAllocNeossMstVo.setSaid(preDtlSrcIpAllocMstVo.getSsaid()); // ssaid 서비스계약ID
						linkAllocNeossMstVo.setRegyn("N"); // 할당요청/취소 구분 [ 예약 = Y, 취소 = N ]
						linkAllocNeossMstVo.setIpmsSvcSeq(preDtlSrcIpAllocMstVo.getNipmsSvcSeq()); // nipms_svc_seq 
																									// IPMS 상품MST Seq
						linkAllocNeossMstVo.setAssignTypeCd(preDtlSrcIpAllocMstVo.getSassignTypeCd()); // sassign_type_cd 
																										// IP할당유형
						linkAllocNeossMstVo.setExSvcCd(preDtlSrcIpAllocMstVo.getSexSvcCd()); // sex_svc_cd
						linkAllocNeossMstVo.setSvcUseTypeCd(preDtlSrcIpAllocMstVo.getSsvcUseTypeCd()); // ssvc_use_type_cd 서비스
																										// 이용목정 [사업용 =
																										// SU0001 / 일반용
																										// = SU0002 ]
						linkAllocNeossMstVo.setIpVersionTypeCd(preDtlSrcIpAllocMstVo.getSipVersionTypeCd()); // sip_version_type_cd
																												// IPv6
																												// =
																												// CV0002/
																												// IPv4
																												// CV0001
						linkAllocNeossMstVo.setIpCreateTypeCd(preDtlSrcIpAllocMstVo.getSipCreateTypeCd()); // sip_create_type_cd
																											//   CT0001
																											// = 공인 
						linkAllocNeossMstVo.setIpBlock(preDtlSrcIpAllocMstVo.getSipBlock()); // sip_block     fe01:1:1::
						linkAllocNeossMstVo.setIpbitmask(preDtlSrcIpAllocMstVo.getNbitmask()); // nbitmask      64
						linkAllocNeossMstVo.setSipaddr(preDtlSrcIpAllocMstVo.getSfirstAddr()); // sfirst_addr  fe01:1:1::
						linkAllocNeossMstVo.setEipaddr(preDtlSrcIpAllocMstVo.getSlastAddr()); // slast_addr
																								// fe01:1:1:0:ffff:ffff:ffff:ffff
						linkAllocNeossMstVo.setNipAssignMstSeq(preDtlSrcIpAllocMstVo.getNipAssignMstSeq()); // nip_assign_mst_seq
																											// 48
						linkAllocNeossMstVo.setGatewayip(preDtlSrcIpAllocMstVo.getSgatewayip()); // sgatewayip
																									// fe01:1:1:0:ffff:ffff:ffff:ffff

						// 2014.11.13 연동 변경에 따른 추가(이중회선, 망코드 추가)
						if (ipAllocOperMstVo.getNipAllocMstCnt().equals(BigInteger.ZERO)) { // 단일회선 해지시 0임으로 이중화 회선아님.
							linkAllocNeossMstVo.setSlacpBlockYN("N");
						} else {
							linkAllocNeossMstVo.setSlacpBlockYN("Y");
						}
						linkAllocNeossMstVo.setssvcLineTypeCd(preDtlSrcIpAllocMstVo.getSsvcLineTypeCd());

						linkIpAllocNeossVos.add(linkAllocNeossMstVo);

					}
				}

				/* 스쿨넷은 외부 연동 없음 */
				if (!ssvcLineTypeCd.equals("CL0008")) {

					/* 사설IP 는 연동 없음 */
					if (!sipCreateTypeCd.equals("CT0005")) {

						// 1. 연동 처리 정보 호출부(연동 결과에 따른 처리는 연동 추가 후 진행예정)
						if (sLinkFlg.equals("O")) { // 할당예약 해지 웹서비스 호출
							// bLinkResultFlg = true; 할당예약
							tbIpAllocNeossMstListVo.setTbIpAllocNeossMstVos(linkIpAllocNeossVos);
							neOSSConsumeAdapterService.insertReservedIPList(tbIpAllocNeossMstListVo);
						} else if (sLinkFlg.equals("Y")) { // 할당 해지 웹서비스 호출
							// bLinkResultFlg = true; 할당콜
							tbIpAllocNeossMstListVo.setTbIpAllocNeossMstVos(linkIpAllocNeossVos);
							neOSSConsumeAdapterService.insertFixedIPList(tbIpAllocNeossMstListVo);
						}
					}

				}

				// 2. 할당 서브(선번장) 삭제 처리
				lineMgmtAdapterService.deleteIpAssignSub(deleteIpAssignSubVos);
				// 3. 할당 삭제 처리
				allocMgmtTxService.processDeleteIPAllocMst(deleteIpAllocMstVos);
				// 5. Ticket정보 처리(할당) - 배정처리전 데이터 확인을 위해 처리 함.
				for (TbIpAssignMstVo destTicketTbIpAssignMstVo : updateIpAssignMstVos) {
					/*
					 * Step 1. 배정블록 조회
					 * - Method 호출 및 리턴 셋팅
					 */
					TbIpAssignMstVo srchTicketIpAssignMstVo = new TbIpAssignMstVo();
					TbIpAssignMstVo insertTicketTbIpAssignMstVo = new TbIpAssignMstVo();

					TbTicketMstVo tbTicketMstVo = new TbTicketMstVo();

					TbIpmsSvcMstVo srchTicketTbIpmsSvcMstVo = new TbIpmsSvcMstVo();
					TbIpmsSvcMstVo ssvcTicketTbIpmsSvcMstVo = new TbIpmsSvcMstVo();

					srchTicketIpAssignMstVo.setNipAssignMstSeq(destTicketTbIpAssignMstVo.getNipAssignMstSeq());
					insertTicketTbIpAssignMstVo = assignMgmtAdapterService.selectIpAssignMst(srchTicketIpAssignMstVo);

					/*
					 * Step 2. 변경 Param Setting (회선, 시설)
					 * - Step 1의 정보에 상단 셋팅 값 input
					 * - tbTicketMstVo에 블록 정보 (배정) 셋팅 및 기본값 셋팅
					 */
					CloneUtil.copyObjectInformation(insertTicketTbIpAssignMstVo, tbTicketMstVo);
					tbTicketMstVo.setNticketTypeSeq(CommonCodeUtil.TICKET_TYPE_IP_ALLOC_CANCEL);
					tbTicketMstVo.setScreateId(destTicketTbIpAssignMstVo.getSmodifyId());

					/*
					 * Step 3. 상품정보 셋팅
					 * - 상단 정보에 대하여 상품코드 존재시 해당 정보 셋팅
					 */
					if (destTicketTbIpAssignMstVo.getNipmsSvcSeq() != null) {
						srchTicketTbIpmsSvcMstVo.setNipmsSvcSeq(tbTicketMstVo.getNipmsSvcSeq());
						ssvcTicketTbIpmsSvcMstVo = svcMgmtAdapterService.selectTbIpmsSvcMstVo(srchTicketTbIpmsSvcMstVo);
						tbTicketMstVo.setSipmsSvcNm(ssvcTicketTbIpmsSvcMstVo.getSipmsSvcNm());
						tbTicketMstVo.setSsvcUseTypeCd(ssvcTicketTbIpmsSvcMstVo.getSsvcUseTypeCd());

					}
					/* Step 4. 티켓 처리 */
					ticketMgmtAdapterService.insertTicketMst(tbTicketMstVo);
				}

				// 6. 이력관리 이력 등록(할당) - 배정처리전 데이터 확인을 위해 처리 함.
				for (IpHistoryMstVo destTicketTbIpAssignMstVo : deleteIpHistMstVos) {

					/* 이력관리 이력 등록 */
					TbIpAssignMstVo srchTicketIpAssignMstVo = new TbIpAssignMstVo();
					TbIpAssignMstVo insertTicketTbIpAssignMstVo = new TbIpAssignMstVo();

					srchTicketIpAssignMstVo.setNipAssignMstSeq(destTicketTbIpAssignMstVo.getNipAssignMstSeq());
					insertTicketTbIpAssignMstVo = assignMgmtAdapterService.selectIpAssignMst(srchTicketIpAssignMstVo);

					IpHistoryMstVo ipHistoryMstVo = new IpHistoryMstVo();
					CloneUtil.copyObjectInformation(insertTicketTbIpAssignMstVo, ipHistoryMstVo);

					ipHistoryMstVo.setNipAllocMstSeq(destTicketTbIpAssignMstVo.getNipAllocMstSeq());
					ipHistoryMstVo.setNipAllocNeossSeq(destTicketTbIpAssignMstVo.getNipAllocNeossSeq());
					ipHistoryMstVo.setSordernum(destTicketTbIpAssignMstVo.getSordernum());
					ipHistoryMstVo.setSregyn(destTicketTbIpAssignMstVo.getSregyn());
					ipHistoryMstVo.setSodrCloseTypeCd(destTicketTbIpAssignMstVo.getSodrCloseTypeCd());
					ipHistoryMstVo.setSsaid(destTicketTbIpAssignMstVo.getSsaid());
					ipHistoryMstVo.setSllnum(destTicketTbIpAssignMstVo.getSllnum());
					ipHistoryMstVo.setSicisofficescode(destTicketTbIpAssignMstVo.getSicisofficescode());
					ipHistoryMstVo.setSlacpsid(destTicketTbIpAssignMstVo.getSlacpsid());
					ipHistoryMstVo.setSsubscnescode(destTicketTbIpAssignMstVo.getSsubscnescode());
					ipHistoryMstVo.setSsubscmstip(destTicketTbIpAssignMstVo.getSsubscmstip());
					ipHistoryMstVo.setSsubsclgipportseq(destTicketTbIpAssignMstVo.getSsubsclgipportseq());
					ipHistoryMstVo
							.setSsubsclgipportdescription(destTicketTbIpAssignMstVo.getSsubsclgipportdescription());
					ipHistoryMstVo.setSsubsclgipportip(destTicketTbIpAssignMstVo.getSsubsclgipportip());
					ipHistoryMstVo.setSsubscrouterserialip(destTicketTbIpAssignMstVo.getSsubscrouterserialip());
					ipHistoryMstVo.setSsubscnealias(destTicketTbIpAssignMstVo.getSsubscnealias());
					ipHistoryMstVo.setSconnalias(destTicketTbIpAssignMstVo.getSconnalias());
					ipHistoryMstVo.setSmodelname(destTicketTbIpAssignMstVo.getSmodelname());
					ipHistoryMstVo.setScustname(destTicketTbIpAssignMstVo.getScustname());

					ipHistoryMstVo.setNipmsSvcSeq(insertTicketTbIpAssignMstVo.getNipmsSvcSeq());
					ipHistoryMstVo.setSsvcUseTypeCd(destTicketTbIpAssignMstVo.getSsvcUseTypeCd());
					ipHistoryMstVo.setSexSvcCd(destTicketTbIpAssignMstVo.getSexSvcCd());

					ipHistoryMstVo.setSassignLevelCd("IA0004"); // 할당상태에서 해지를 하면 서비스배정(미할당)으로 돌아감

					if (!StringUtils.hasText(ipAllocMstComplexVo.getMenuType())) {
						ipHistoryMstVo.setsMenuHistCd("Aloc");
						// ipHistoryMstVo.setSmenuNm("IP 할당");
						// ipHistoryMstVo.setSmenuId("M00009"); // IP 할당
					} else if (ipAllocMstComplexVo.getMenuType().equals("Rout")) {
						ipHistoryMstVo.setsMenuHistCd("Rout");
						// ipHistoryMstVo.setSmenuNm("IP주소 라우팅 비교/점검");
						// ipHistoryMstVo.setSmenuId("M00118"); // IP주소 라우팅 비교/점검
					} else if (ipAllocMstComplexVo.getMenuType().equals("NodeReq")) {
						ipHistoryMstVo.setsMenuHistCd("NodeReq"); // IP 주소 노드 이전 신청
					} else if (ipAllocMstComplexVo.getMenuType().equals("Upload")) {
						ipHistoryMstVo.setsMenuHistCd("Upload"); // 업로드관리
					} else {
						ipHistoryMstVo.setsMenuHistCd("Aloc");
						// ipHistoryMstVo.setSmenuNm("IP 할당");
						// ipHistoryMstVo.setSmenuId("M00009"); // IP 할당
					}

					if (sLinkFlg.equals("O")) { // 할당예약
						ipHistoryMstVo.setNipHistTaskCd(CommonCodeUtil.HIST_TASK_CANCEL_RESV); // 할당예약해지
					} else { // 할당
						ipHistoryMstVo.setNipHistTaskCd(CommonCodeUtil.HIST_TASK_CANCEL); // 해지
					}

					ipHistoryMstVo.setScreateId(ipAllocMstComplexVo.getScreateId());

					/* Step 4. 티켓 처리 */
					historyMgmtAdapterService.insertHistoryMst(ipHistoryMstVo);

				}

				// 4. 배정 변경(해지) 처리
				assignMgmtAdapterService.updateAlocIpAssignMst(updateIpAssignMstVos);

				/* 스쿨넷은 외부 연동 없음 */
				if (!ssvcLineTypeCd.equals("CL0008")) {

					/* 공인IP 만 Whois 연동 */
					if (sipCreateTypeCd.equals("CT0001")) {

						/***************************
						 * Whois 전송
						 ***************************/
						if (deleteIpAllocMstVos.size() > 0) {
							BigInteger allocMstSeq = null;
							BigInteger nwhoisSeq = null;
							String sessionId = null;
							// Whois 전송 예외
							boolean rtnExceptVal = true;
							for (int i = 0; i < deleteIpAllocMstVos.size(); i++) {
								allocMstSeq = deleteIpAllocMstVos.get(i).getNipAllocMstSeq();
								nwhoisSeq = deleteIpAllocMstVos.get(i).getNwhoisSeq();
								if (nwhoisSeq != null) {
									int nipAllocMstCnt = whoisAdapterService.selectNipAllocMstCnt(nwhoisSeq);
									if (nipAllocMstCnt <= 1) {
										// 하나의 결과를 바라는데 2개가 나옴
										sessionId = ipAllocMstComplexVo.getSrcIpAllocMstVo().getSmodifyId();
										PrintLogUtil.printLogInfo("#####ALLOC MST SEQ: " + allocMstSeq);
										PrintLogUtil.printLogInfo("#####WHOIS SEQ: " + nwhoisSeq);
										rtnExceptVal = whoisAdapterService.exceptWhois("DEL", null, nwhoisSeq);
										// rtnExceptVal = whoisAdapterService.exceptWhois("DEL", allocMstSeq,
										// nwhoisSeq);
										if (rtnExceptVal) {
											whoisAdapterService.sendToWhoisWithDbNoRtn(sessionId, nwhoisSeq, "DEL");
										}
									}
								} else {
									rtnExceptVal = whoisAdapterService.exceptWhois("DEL", null, nwhoisSeq);
									// rtnExceptVal = whoisAdapterService.exceptWhois("DEL", allocMstSeq,
									// nwhoisSeq);
									if (rtnExceptVal) {
										whoisAdapterService.sendToWhoisWithDbNoRtn(sessionId, nwhoisSeq, "DEL");
									}
								}
							}
						}

					}

					/* 사설IP 는 라우팅 점검 연동 없음 */
					if (!sipCreateTypeCd.equals("CT0005")) {

						/***************************
						 * 라우팅 점검 업데이트
						 ***************************/
						for (int i = 0; i < deleteIpAllocMstVos.size(); i++) {
							// if(deleteIpAllocMstVos.get(i).getsGubun() == null ||
							// deleteIpAllocMstVos.get(i).getsGubun() == "") { //Codeeyes-Urgent-객체 비교시 == ,
							// != 사용제한
							if (deleteIpAllocMstVos.get(i).getsGubun() == null
									|| deleteIpAllocMstVos.get(i).getsGubun().equals("")) {
								PrintLogUtil.printLog(
										"#####ASSIGN MST SEQ: " + deleteIpAllocMstVos.get(i).getNipAssignMstSeq());
								TbRoutChkMstVo tbRoutChkMstVo = new TbRoutChkMstVo();
								tbRoutChkMstVo.setNipAssignMstSeq(deleteIpAllocMstVos.get(i).getNipAssignMstSeq());
								routMgmtTxService.updateRoutMgmtSeq(tbRoutChkMstVo);

							}
						}
					}

				}

			} else {
				// 해지정보가 없습니다.
				throw new ServiceException("CMN.HIGH.00001");
			}

		} catch (ServiceException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			// 해지처리시 오류 발생
			throw new ServiceException("APP.HIGH.00033", new String[] { "IP블록 해지처리" });
		}
	}

	/* 반납 처리 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void updateListAsgnIPMst(TbIpAssignMstComplexVo tbIpAssignMstComplexVo) {
		try {
			if (tbIpAssignMstComplexVo == null || tbIpAssignMstComplexVo.getSrcIpAssignMstVo() == null
					|| tbIpAssignMstComplexVo.getDestIpAssignMstVos() == null
					|| tbIpAssignMstComplexVo.getDestIpAssignMstVos().size() == 0) {
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
				tbIpAssignMstVo.setMenuType(tbIpAssignMstVo.getMenuType());

			}

			// 반납(배정) 처리
			assignMgmtAdapterService.updateIpAssignMst(destIpAssignMstVos);

			/***************************
			 * 라우팅 점검 업데이트
			 ***************************/
			for (int i = 0; i < destIpAssignMstVos.size(); i++) {
				// if(destIpAssignMstVos.get(i).getsGubun() == null ||
				// destIpAssignMstVos.get(i).getsGubun() == "") { //Codeeyes-Urgent-객체 비교시 == ,
				// != 사용제한
				if (destIpAssignMstVos.get(i).getsGubun() == null || destIpAssignMstVos.get(i).getsGubun().equals("")) {
					PrintLogUtil.printLog("#####ASSIGN MST SEQ: " + destIpAssignMstVos.get(i).getNipAssignMstSeq());
					TbRoutChkMstVo tbRoutChkMstVo = new TbRoutChkMstVo();
					tbRoutChkMstVo.setNipAssignMstSeq(destIpAssignMstVos.get(i).getNipAssignMstSeq());
					routMgmtTxService.updateRoutMgmtSeq(tbRoutChkMstVo);
				}
			}

		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00021", new String[] { "반납 IP배정블록" });
		}
	}

	/* 비고 처리 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void updateListScommentAllocIPMst(TbIpAssignMstComplexVo tbIpAssignMstComplexVo) {
		try {
			if (tbIpAssignMstComplexVo == null || tbIpAssignMstComplexVo.getSrcIpAssignMstVo() == null
					|| tbIpAssignMstComplexVo.getDestIpAssignMstVos() == null
					|| tbIpAssignMstComplexVo.getDestIpAssignMstVos().size() == 0) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			TbIpAssignMstVo srcIpAssignMstVo = tbIpAssignMstComplexVo.getSrcIpAssignMstVo();
			List<TbIpAssignMstVo> destIpAssignMstVos = tbIpAssignMstComplexVo.getDestIpAssignMstVos();
			for (TbIpAssignMstVo tbIpAssignMstVo : destIpAssignMstVos) {
				tbIpAssignMstVo.setScomment(srcIpAssignMstVo.getScomment());
				tbIpAssignMstVo.setSmodifyId(srcIpAssignMstVo.getSmodifyId());
				tbIpAssignMstVo.setMenuType(tbIpAssignMstComplexVo.getMenuType());
			}

			// 비고(할당) 처리
			allocMgmtTxService.processScommentUpdateAllocIPMst(destIpAssignMstVos);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00021", new String[] { "비고 IP할당블록" });
		}
	}

	@Transactional(readOnly = true)
	public IpAllocOperMstListVo selectListPageMainIpAssignMst(IpAllocOperMstVo ipAllocOperMstVo) {
		IpAllocOperMstListVo resultListVo = null;
		if (ipAllocOperMstVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {

			// ipAllocOperMstVo.setPageUnit(totalCount);
			List<IpAllocOperMstVo> resultList = tbIpAllocMstDao.selectListPageMainIpAssignMst(ipAllocOperMstVo);
			int totalCount = tbIpAllocMstDao.countListMainIpAssignMst(ipAllocOperMstVo);
			resultListVo = new IpAllocOperMstListVo();
			resultListVo.setIpAllocOperMstVos(resultList);
			resultListVo.setTotalCount(totalCount);

		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00000");
		}
		return resultListVo;
	}

	@Transactional(readOnly = true)
	public IpAllocOperMstListVo selectListPageMainIpAssignMstExcel(IpAllocOperMstVo ipAllocOperMstVo) {
		IpAllocOperMstListVo resultListVo = null;
		if (ipAllocOperMstVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {
			int totalCount = tbIpAllocMstDao.countListMainIpAssignMst(ipAllocOperMstVo);
			if (totalCount > CommonCodeUtil.MAX_EXCEL_ROW_COUNT) {
				throw new ServiceException("CMN.INFO.00051");
			}
			List<IpAllocOperMstVo> resultList = null;
			if (totalCount > 0) {
				ipAllocOperMstVo.setFirstIndex(1);
				ipAllocOperMstVo.setLastIndex(totalCount);
				resultList = tbIpAllocMstDao.selectListPageMainIpAssignMst(ipAllocOperMstVo);
			}
			resultListVo = new IpAllocOperMstListVo();
			resultListVo.setIpAllocOperMstVos(resultList);
			resultListVo.setTotalCount(totalCount);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00000");
		}
		return resultListVo;
	}

	@Transactional(readOnly = true)
	public IpAllocOperMstVo selectMainIpInfoMst(IpAllocOperMstVo ipAllocOperMstVo) {
		IpAllocOperMstVo resultVo = null;
		if (ipAllocOperMstVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {

			resultVo = tbIpAllocMstDao.selectMainIpInfoMst(ipAllocOperMstVo);

		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00000");
		}
		return resultVo;
	}

	@Transactional(readOnly = true)
	public TbIpHostMstListVo selectTbIpHostInfoVo(TbIpHostMstVo tbIpHostMstVo) {
		TbIpHostMstListVo resultListVo = null;
		try {
			resultListVo = this.hostMgmtAdapterService.selectTbIpHostInfoVo(tbIpHostMstVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00000");
		}
		return resultListVo;

	}

	/* 할당 등록(외부) */
	@Transactional(propagation = Propagation.REQUIRED)
	public void processInsertIPAllocMst(List<TbIpAllocMstVo> tbIpAllocMstVos) {

		if (tbIpAllocMstVos == null || tbIpAllocMstVos.size() == 0) {
			throw new ServiceException("CMN.HIGH.00020", new String[] { "IP선번장블록" });
		} else {
			allocMgmtTxService.processInsertIPAllocMst(tbIpAllocMstVos);
		}
	}

	/**
	 * 수용국 정보 조회(초기)
	 * 
	 * @param tbIpAssignSubVo
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = true)
	public List<CommonCodeVo> selectLoadOfficeList(IpAllocOperMstVo ipAllocOperMstVo) {
		List<CommonCodeVo> resultListVo = null;

		try {
			if (ipAllocOperMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}

			TbIpAssignSubVo tbIpAssignSubVo = new TbIpAssignSubVo();
			CloneUtil.copyObjectInformation(ipAllocOperMstVo, tbIpAssignSubVo);

			resultListVo = lineMgmtAdapterService.selectLoadOfficeList(tbIpAssignSubVo);

		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[] { "할당정보 기반 수용국 목록" });
		}
		return resultListVo;
	}

	/**
	 * 수용국 정보 조회(시설기반)
	 * 
	 * @param ipAllocOperMstVo
	 * @return List<CommonCodeVo>
	 * @throws Exception
	 */
	@Transactional(readOnly = true)
	public List<CommonCodeVo> selectHostSofficeList(IpAllocOperMstVo ipAllocOperMstVo) {
		List<CommonCodeVo> resultListVo = null;
		try {
			if (ipAllocOperMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}

			resultListVo = allocMgmtTxService.selectHostSofficeList(ipAllocOperMstVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[] { "시설기반 수용국 목록" });
		}
		return resultListVo;
	}

	@Transactional(readOnly = true)
	public IpAllocOperMstListVo selectListMainSearchMultiIp(IpAllocOperMstVo ipAllocOperMstVo) {
		IpAllocOperMstListVo resultListVo = null;
		if (ipAllocOperMstVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {
			List<IpAllocOperMstVo> resultList = allocMgmtTxService.selectListMainSearchMultiIp(ipAllocOperMstVo);
			int totalCount = allocMgmtTxService.countListMainSearchMultiIp(ipAllocOperMstVo);
			resultListVo = new IpAllocOperMstListVo();
			resultListVo.setIpAllocOperMstVos(resultList);
			resultListVo.setTotalCount(totalCount);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[] { "멀티 IP 조회" });
		}
		return resultListVo;
	}

	@Transactional(readOnly = true)
	public IpAllocOperMstListVo selectListMainSearchMultiIpExcel(IpAllocOperMstVo ipAllocOperMstVo) {
		IpAllocOperMstListVo resultListVo = null;
		if (ipAllocOperMstVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {
			int totalCount = allocMgmtTxService.countListMainSearchMultiIp(ipAllocOperMstVo);
			if (totalCount > CommonCodeUtil.MAX_EXCEL_ROW_COUNT) {
				throw new ServiceException("CMN.INFO.00051");
			}
			List<IpAllocOperMstVo> resultList = null;
			if (totalCount > 0) {
				resultList = allocMgmtTxService.selectListMainSearchMultiIp(ipAllocOperMstVo);
			}

			resultListVo = new IpAllocOperMstListVo();
			resultListVo.setIpAllocOperMstVos(resultList);
			resultListVo.setTotalCount(totalCount);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[] { "멀티 IP 조회" });
		}
		return resultListVo;
	}

	/* VPN IP 현황 조회 */
	@Transactional(readOnly = true)
	public TbIpAllocMstListVo selectListVpnIPStat(TbIpAllocMstVo tbIpAllocMstVo) {
		TbIpAllocMstListVo resultListVo = null;
		if (tbIpAllocMstVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {
			List<TbIpAllocMstVo> resultList = allocMgmtTxService.selectListPageVpnIPStat(tbIpAllocMstVo);
			int totalCount = allocMgmtTxService.countListPageVpnIPStat(tbIpAllocMstVo);
			resultListVo = new TbIpAllocMstListVo();
			resultListVo.setTbIpAllocMstVos(resultList);
			resultListVo.setTotalCount(totalCount);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[] { "VPN IP현황 목록" });
		}
		return resultListVo;
	}

	/* VPN IP 현황 조회 */
	@Transactional(readOnly = true)
	public TbIpAllocMstListVo selectListVpnIPStatExcel(TbIpAllocMstVo tbIpAllocMstVo) {
		TbIpAllocMstListVo resultListVo = null;
		if (tbIpAllocMstVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {
			int totalCount = allocMgmtTxService.countListPageVpnIPStat(tbIpAllocMstVo);
			if (totalCount > CommonCodeUtil.MAX_EXCEL_ROW_COUNT) {
				throw new ServiceException("CMN.INFO.00051");
			}
			List<TbIpAllocMstVo> resultList = null;
			if (totalCount > 0) {
				tbIpAllocMstVo.setFirstIndex(1);
				tbIpAllocMstVo.setLastIndex(totalCount);
				resultList = allocMgmtTxService.selectListPageVpnIPStat(tbIpAllocMstVo);
			}
			resultListVo = new TbIpAllocMstListVo();
			resultListVo.setTbIpAllocMstVos(resultList);
			resultListVo.setTotalCount(totalCount);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[] { "VPN IP현황 목록 엑셀저장" });
		}
		return resultListVo;
	}

	/**
	 * 조직별 서비스 유형 목록 조회
	 * 
	 * @param ipAllocOperMstVo
	 * @return List<CommonCodeVo>
	 * @throws Exception
	 */
	@Transactional(readOnly = true)
	public List<CommonCodeVo> selectOrgSassignTypeCdList(TbIpAllocMstVo tbIpAllocMstVo) {
		List<CommonCodeVo> resultListVo = null;
		try {
			if (tbIpAllocMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}

			resultListVo = allocMgmtTxService.selectOrgSassignTypeCdList(tbIpAllocMstVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[] { "조직기반 서비스목록" });
		}
		return resultListVo;
	}

	/**
	 * 조직별 서비스 유형 목록 조회
	 * 
	 * @param ipAllocOperMstVo
	 * @return List<CommonCodeVo>
	 * @throws Exception
	 */
	@Transactional(readOnly = true)
	public List<CommonCodeVo> selectOrgSassignTypeCdList2(TbIpAllocMstVo tbIpAllocMstVo) {
		List<CommonCodeVo> resultListVo = null;
		try {
			if (tbIpAllocMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}

			resultListVo = allocMgmtTxService.selectOrgSassignTypeCdList2(tbIpAllocMstVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[] { "조직기반 서비스목록" });
		}
		return resultListVo;
	}

	/*
	 * 시설용 IP 서비스 유형 목록 조회
	 */
	@Transactional(readOnly = true)
	public List<CommonCodeVo> selectFacilitesTypeCdList(TbIpAllocMstVo tbIpAllocMstVo) {
		List<CommonCodeVo> resultListVo = null;
		try {
			if (tbIpAllocMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}

			resultListVo = allocMgmtTxService.selectFacilitesTypeCdList(tbIpAllocMstVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[] { "시설용 서비스목록" });
		}
		return resultListVo;
	}

	/* VPN IP현황 상세 */
	@Transactional(readOnly = true)
	public TbIpAllocMstVo selectVpnIPStatDetail(TbIpAllocMstVo tbIpAllocMstVo) {
		TbIpAllocMstVo resultVo = null;
		if (tbIpAllocMstVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {
			resultVo = allocMgmtTxService.selectVpnIPStatDetail(tbIpAllocMstVo);
			if (resultVo == null) {
				throw new ServiceException("CMN.HIGH.00023", new String[] { "(원인: 세부 정보 미존재) VPN IP현황  상세정보" });
			}
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[] { "VPN IP현황  상세정보" });
		}
		return resultVo;
	}

	/**
	 * 링크 정보 조회
	 * 
	 * @param ipAllocOperMstVo
	 * @return
	 */
	@Transactional(readOnly = true)
	public IpAllocOperMstListVo selectListLnMst(IpAllocOperMstVo ipAllocOperMstVo) {
		IpAllocOperMstListVo resultListVo = null;
		if (ipAllocOperMstVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {

			int totalCount = 0;
			List<IpAllocOperMstVo> resultList = null;
			String sSrchTypeCd = ipAllocOperMstVo.getSneSrchTypeCd();
			/* 시설조회 시 할당 기준인지 호스트 기준인지의 처리 */
			totalCount = allocMgmtTxService.countListPageTbIpLinkMst(ipAllocOperMstVo);

			ipAllocOperMstVo.setPageUnit(totalCount);

			resultList = allocMgmtTxService.selectListPageTbIpLinkMst(ipAllocOperMstVo);

			resultListVo = new IpAllocOperMstListVo();
			resultListVo.setIpAllocOperMstVos(resultList);
			resultListVo.setTotalCount(totalCount);

		} catch (ServiceException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("CMN.HIGH.00023", new String[] { "링크정보" });
		}
		return resultListVo;
	}

	/**
	 * 수용국 정보 조회(링크기반)
	 * 
	 * @param ipAllocOperMstVo
	 * @return List<CommonCodeVo>
	 * @throws Exception
	 */
	@Transactional(readOnly = true)
	public List<CommonCodeVo> selectLinkSofficeList(IpAllocOperMstVo ipAllocOperMstVo) {
		List<CommonCodeVo> resultListVo = null;
		try {
			if (ipAllocOperMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}

			resultListVo = allocMgmtTxService.selectLinkSofficeList(ipAllocOperMstVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[] { "링크기반 수용국 목록" });
		}
		return resultListVo;
	}

}
