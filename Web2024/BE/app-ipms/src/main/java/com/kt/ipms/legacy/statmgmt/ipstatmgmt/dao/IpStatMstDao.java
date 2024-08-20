package com.kt.ipms.legacy.statmgmt.ipstatmgmt.dao;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.kt.ipms.legacy.cmn.vo.CommonCodeVo;
import com.kt.ipms.legacy.ipmgmt.allocmgmt.vo.TbIpAllocMstVo;
import com.kt.ipms.legacy.statmgmt.ipstatmgmt.vo.IpCreateStatVo;
import com.kt.ipms.legacy.statmgmt.ipstatmgmt.vo.IpIntgrmSvcStatVo;
import com.kt.ipms.legacy.statmgmt.ipstatmgmt.vo.IpOrgStatVo;
import com.kt.ipms.legacy.statmgmt.ipstatmgmt.vo.IpOrgServiceStatVo;
import com.kt.ipms.legacy.statmgmt.ipstatmgmt.vo.IpSingleBlockStatVo;



/** IP_STAT_MST DAO INTERFACE **/
public interface IpStatMstDao {
	
	/** Create Per IP STAT selectListPageIpCreateStatVo **/
	public List<IpCreateStatVo> selectListPageIpCreateStatVo(IpCreateStatVo ipCreateStatVo);
	
	/** Create Per IP STAT countListPageIpCreateStatVo **/
	public int countListPageIpCreateStatVo(IpCreateStatVo ipCreateStatVo);
	
	/** Organic(lvl) Per IP STAT selectListPage **/
	public List<IpOrgStatVo> selectListPageIpOrgStatVo(IpOrgStatVo ipOrgStatVo);

	/** Organic(lvl) Per IP STAT countListPage **/
	public int countListPageIpOrgStatVo(IpOrgStatVo ipOrgStatVo);

	/** Organic(lvlSvc) Per IP STAT selectListPageIpOrgServiceStatVo **/
	public List<IpOrgServiceStatVo> selectListPageIpOrgServiceStatVo(IpOrgServiceStatVo ipStatMstVo);

	/** Organic(lvlSvc) Per IP STAT countListPageIpOrgServiceStatVo **/
	public int countListPageIpOrgServiceStatVo(IpOrgServiceStatVo ipStatMstVo);

	/** SingleBlock Per IP STAT selectListPage **/
	public List<IpSingleBlockStatVo> selectListPageSingleBlockIpStatMstVo(IpSingleBlockStatVo ipSingleBlockStatVo);

	/** SingleBlock Per IP STAT countListPage **/
	public int countListPageSingleBlockIpStatMstVo(IpSingleBlockStatVo ipSingleBlockStatVo);
	
	
	public List<Map<String,String>> selectListPageIntgrmSvcStatVo(IpIntgrmSvcStatVo ipIntgrmSvcStatVo);

	public int countListPageIntgrmSvcStatVo(IpIntgrmSvcStatVo ipIntgrmSvcStatVo);
	
	public List<String> selectIntgrmSassignTypeCdList(IpIntgrmSvcStatVo ipIntgrmSvcStatVo);
	
	/** IP 조직 서비스별 통계 현황 목록 **/
	public List<Map<String,String>> selectListPageOrgSvcStatVo(IpIntgrmSvcStatVo ipIntgrmSvcStatVo);

	/** IP 서비스별 통계 현황 목록 **/
	public List<Map<String, String>> selectListPageSvcStatVo(IpIntgrmSvcStatVo ipIntgrmSvcStatVo);

	/** IP 블록별 통계 현황 목록 **/
	public List<Map<String, String>> selectListBlockSizeStatMst(IpIntgrmSvcStatVo ipIntgrmSvcStatVo);

	public List<Map<String, String>> selectListSvcLineList();
	
} 