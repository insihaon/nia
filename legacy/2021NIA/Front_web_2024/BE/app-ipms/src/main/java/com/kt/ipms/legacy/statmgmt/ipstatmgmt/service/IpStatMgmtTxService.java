package com.kt.ipms.legacy.statmgmt.ipstatmgmt.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.kt.ipms.legacy.cmn.vo.CommonCodeVo;
import com.kt.ipms.legacy.ipmgmt.allocmgmt.adapter.AllocMgmtAdapterService;
import com.kt.ipms.legacy.ipmgmt.allocmgmt.vo.TbIpAllocMstVo;
import com.kt.ipms.legacy.statmgmt.ipstatmgmt.dao.IpStatMstDao;
import com.kt.ipms.legacy.statmgmt.ipstatmgmt.vo.IpCreateStatVo;
import com.kt.ipms.legacy.statmgmt.ipstatmgmt.vo.IpIntgrmSvcStatVo;
import com.kt.ipms.legacy.statmgmt.ipstatmgmt.vo.IpOrgServiceStatVo;
import com.kt.ipms.legacy.statmgmt.ipstatmgmt.vo.IpOrgStatVo;
import com.kt.ipms.legacy.statmgmt.ipstatmgmt.vo.IpSingleBlockStatVo;

@Component
@Transactional
public class IpStatMgmtTxService {
	
	@Lazy @Autowired
	private IpStatMstDao ipStatMstDao;
	
@Lazy @Autowired
	private AllocMgmtAdapterService allocMgmtAdapterService;
	
	/* 통계-생성차수별 IP현황 Select Page */
	@Transactional(readOnly = true)
	public List<IpCreateStatVo> selectListPageIpCreateStatVo(IpCreateStatVo ipCreateStatVo) {
		return ipStatMstDao.selectListPageIpCreateStatVo(ipCreateStatVo);
	}
	
	/* 통계-생성차수별 IP현황 Select Total count */
	@Transactional(readOnly = true)
	public int countListPageIpCreateStatVo(IpCreateStatVo ipCreateStatVo) {
		return ipStatMstDao.countListPageIpCreateStatVo(ipCreateStatVo);
	}

	/*통계-조직별 IP현황 Select Page*/
	@Transactional(readOnly = true)
	public List<IpOrgStatVo> selectListPageIpOrgStatVo(IpOrgStatVo ipOrgStatVo){
		return ipStatMstDao.selectListPageIpOrgStatVo(ipOrgStatVo);
	}
	
	/*통계-조직별 IP현황 Select Total count*/
	@Transactional(readOnly = true)
	public int countListPageIpOrgStatVo(IpOrgStatVo ipOrgStatVo){
		return ipStatMstDao.countListPageIpOrgStatVo(ipOrgStatVo);
	}
	
	/* 통계-조직서비스별 IP현황 Select Page */
	@Transactional(readOnly = true)
	public List<IpOrgServiceStatVo> selectListPageIpOrgServiceStatVo(IpOrgServiceStatVo ipOrgServiceStatVo){
		return ipStatMstDao.selectListPageIpOrgServiceStatVo(ipOrgServiceStatVo);
	}
	
	/* 통계-조직서비스별 IP현황 Select Total count */
	@Transactional(readOnly = true)
	public int countListPageIpOrgServiceStatVo(IpOrgServiceStatVo ipOrgServiceStatVo){
		return ipStatMstDao.countListPageIpOrgServiceStatVo(ipOrgServiceStatVo);
	}
	
	/* 조직별 서비스 유형 셋팅 */
	@Transactional(readOnly = true)
	public List<CommonCodeVo> selectOrgSassignTypeCdList(TbIpAllocMstVo tbIpAllocMstVo) {
		return allocMgmtAdapterService.selectOrgSassignTypeCdList(tbIpAllocMstVo);
	}
	
	/* 조직별 서비스 유형 셋팅 */
	@Transactional(readOnly = true)
	public List<CommonCodeVo> selectOrgSassignTypeCdList2(TbIpAllocMstVo tbIpAllocMstVo) {
		return allocMgmtAdapterService.selectOrgSassignTypeCdList2(tbIpAllocMstVo);
	}
	
	/*통계-단위블록별 IP현황 Select Page*/
	@Transactional(readOnly = true)
	public List<IpSingleBlockStatVo> selectListPageSingleBlockIpStatMst(IpSingleBlockStatVo ipSingleBlockStatVo){
		return ipStatMstDao.selectListPageSingleBlockIpStatMstVo(ipSingleBlockStatVo);
	}
	
	/*통계-단위블록별 IP현황 Select Total count*/
	@Transactional(readOnly = true)
	public int countListPageSingleBlockIpStatMst(IpSingleBlockStatVo ipSingleBlockStatVo){
		return ipStatMstDao.countListPageSingleBlockIpStatMstVo(ipSingleBlockStatVo);
	}
	
	/**
	 * IP 통계 > IP 현행화 통계 현황 목록
	 * @param ipIntgrmSvcStatVo
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<Map<String,String>> selectListPageIntgrmSvcStatVo(IpIntgrmSvcStatVo ipIntgrmSvcStatVo){
		return ipStatMstDao.selectListPageIntgrmSvcStatVo(ipIntgrmSvcStatVo);
	}
	
	/**
	 * IP 통계 > IP 현행화 통계 현황 목록
	 * @param ipIntgrmSvcStatVo
	 * @return
	 */
	@Transactional(readOnly = true)
	public int countListPageIntgrmSvcStatVo(IpIntgrmSvcStatVo ipIntgrmSvcStatVo){
		return ipStatMstDao.countListPageIntgrmSvcStatVo(ipIntgrmSvcStatVo);
	}
	
	/**
	 * IP 통계 > 서비스 유형 조회 
	 * @param ipIntgrmSvcStatVo
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<String> selectIntgrmSassignTypeCdList(IpIntgrmSvcStatVo ipIntgrmSvcStatVo) {
		return ipStatMstDao.selectIntgrmSassignTypeCdList(ipIntgrmSvcStatVo);
	}
	
	/**
	 * IP 통계 > IP 조직서비스별 통계 현황 목록
	 * @param ipIntgrmSvcStatVo
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<Map<String,String>> selectListPageOrgSvcStatVo(IpIntgrmSvcStatVo ipIntgrmSvcStatVo){
		return ipStatMstDao.selectListPageOrgSvcStatVo(ipIntgrmSvcStatVo);
	}

	/**
	 * IP 통계 > IP 서비스별 통계 현황 목록
	 * @param ipIntgrmSvcStatVo
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<Map<String, String>> selectListPageSvcStatVo(IpIntgrmSvcStatVo ipIntgrmSvcStatVo) {
		return ipStatMstDao.selectListPageSvcStatVo(ipIntgrmSvcStatVo);
	}

	/**
	 * IP 통계 > IP 블록별 통계 현황 목록
	 * @param ipIntgrmSvcStatVo
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<Map<String, String>> selectListBlockSizeStatMst(IpIntgrmSvcStatVo ipIntgrmSvcStatVo) {
		return ipStatMstDao.selectListBlockSizeStatMst(ipIntgrmSvcStatVo);
	}

	public List<Map<String, String>> selectListSvcLineList() {
		return ipStatMstDao.selectListSvcLineList();
	}
	
}
