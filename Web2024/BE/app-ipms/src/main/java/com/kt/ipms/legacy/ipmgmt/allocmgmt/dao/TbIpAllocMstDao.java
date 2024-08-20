package com.kt.ipms.legacy.ipmgmt.allocmgmt.dao;
import java.math.BigInteger;
import java.util.List;

import com.kt.ipms.legacy.cmn.vo.CommonCodeVo;
import com.kt.ipms.legacy.ipmgmt.allocmgmt.vo.IpAllocOperMstListVo;
import com.kt.ipms.legacy.ipmgmt.allocmgmt.vo.IpAllocOperMstVo;
import com.kt.ipms.legacy.ipmgmt.allocmgmt.vo.IpmsSvcVo;
import com.kt.ipms.legacy.ipmgmt.allocmgmt.vo.TbIpAllocMstVo;


/** TB_IP_ALLOC_MST DAO INTERFACE **/
public interface TbIpAllocMstDao {
	
	/** TB_IP_ALLOC_MST insert **/
	public int insertTbIpAllocMstVo(TbIpAllocMstVo tbIpAllocMstVo);
	
	/** TB_IP_ALLOC_MST select SAID seq **/
	public TbIpAllocMstVo selectSaidSeq(TbIpAllocMstVo tbIpAllocMstVo);
	
	/** TB_IP_ALLOC_MST update **/
	public int updateTbIpAllocMstVo(TbIpAllocMstVo tbIpAllocMstVo);

	/** TB_IP_ALLOC_MST delete **/
	public int deleteTbIpAllocMstVo(TbIpAllocMstVo tbIpAllocMstVo);

	/** TB_IP_ALLOC_MST select **/
	public TbIpAllocMstVo selectTbIpAllocMstVo(TbIpAllocMstVo tbIpAllocMstVo);

	/** TB_IP_ALLOC_MST selectList **/
	public List<TbIpAllocMstVo> selectListTbIpAllocMstVo(TbIpAllocMstVo tbIpAllocMstVo);
	
	/** TB_IP_ALLOC_MST selectListPage **/
	public List<TbIpAllocMstVo> selectListPageTbIpAllocMstVo(TbIpAllocMstVo tbIpAllocMstVo);

	/** TB_IP_ALLOC_MST countListPage **/
	public int countListPageTbIpAllocMstVo(TbIpAllocMstVo tbIpAllocMstVo);
	
	/** 업무 구조용 기능 추가**/
	/** 배정할당 select **/
	public IpAllocOperMstVo selectIpAllocOperMstVo(IpAllocOperMstVo ipAllocOperMstVo);

	/** 배정할당 selectList **/
	public List<IpAllocOperMstVo> selectListIpAllocOperMstVo(IpAllocOperMstVo ipAllocOperMstVo);
	
	/** 배정할당 selectListPage **/
	public List<IpAllocOperMstVo> selectListPageIpAllocOperMstVo(IpAllocOperMstVo ipAllocOperMstVo);

	/** 배정할당 countListPage **/
	public int countListPageIpAllocOperMstVo(IpAllocOperMstVo tbIpAllocOperMstVo);

	/** 할당시설 selectListPage **/
	public List<IpAllocOperMstVo> selectListPageNeMst(IpAllocOperMstVo ipAllocOperMstVo);
	
	/** 할당시설 countListPage **/
	public int countListPageNeMst(IpAllocOperMstVo ipAllocOperMstVo);
	
	/**  IP 할당 정보 조회시설 selectListPage **/
	public List<IpAllocOperMstVo> selectListPageMainIpAssignMst(IpAllocOperMstVo ipAllocOperMstVo);
	
	/**  IP 할당 정보 조회시설 countListPage **/
	public int countListMainIpAssignMst(IpAllocOperMstVo ipAllocOperMstVo);
	
	/**  IP 할당 정보  **/
	public IpAllocOperMstVo selectMainIpInfoMst(IpAllocOperMstVo ipAllocOperMstVo);
	
	/** 할당시설(호스트기반) selectListPage **/
	public List<IpAllocOperMstVo> selectListPageTbIpHostMst(IpAllocOperMstVo ipAllocOperMstVo);
	
	/** 할당시설(호스트기반) countListPage **/
	public int countListPageTbIpHostMst(IpAllocOperMstVo ipAllocOperMstVo);
	
	/** 할당 상세정보 selectList **/
	public List<IpAllocOperMstVo> selectListIpAllocDetail(IpAllocOperMstVo ipAllocOperMstVo);
	
	/** 할당 상세정보 countList **/
	public int countListIpAllocDetail(IpAllocOperMstVo ipAllocOperMstVo);
	
	/** 회선 상세정보 select **/
	public TbIpAllocMstVo selectIpAllocDetail(IpAllocOperMstVo ipAllocOperMstVo);
	
	/** 회선 상세정보 select **/
	public TbIpAllocMstVo selectIpAllocDetailLink(IpAllocOperMstVo ipAllocOperMstVo);
	
	/** 할당 대상 목록 select **/ 
	public List<IpAllocOperMstVo> selectListAlcIPMstViaInMstSeq(IpAllocOperMstListVo ipAllocOperMstListVo);
	
	/** 할당 대상 목록 select **/ 
	public List<IpAllocOperMstVo> selectListAlcIPMstViaInMstSeq2(IpAllocOperMstListVo ipAllocOperMstListVo);
	
	/** 할당 목록 selectPage **/
	public List<IpAllocOperMstVo> selectListSvcMst(IpAllocOperMstVo ipAllocOperMstVo);
	
	/** TB_IP_HOST_MST selectHostSofficeList **/
	public List<CommonCodeVo> selectHostSofficeList(IpAllocOperMstVo ipAllocOperMstVo);
	
	
	/** 멀티 IP  조회 **/
	public List<IpAllocOperMstVo> selectListMainSearchMultiIp(IpAllocOperMstVo ipAllocOperMstVo);
	
	/** 멀티 IP  조회 CNT  **/
	public int countListMainSearchMultiIp(IpAllocOperMstVo ipAllocOperMstVo);
	
	
	/** VPN IP현황 selectListPage **/
	public List<TbIpAllocMstVo> selectListPageVpnIPStat(TbIpAllocMstVo tbIpAllocMstVo);

	/** VPN IP현황 countListPage **/
	public int countListPageVpnIPStat(TbIpAllocMstVo tbIpAllocMstVo);
	
	/** 조직별 서비스 유형 목록 조회 select **/
	public List<CommonCodeVo> selectOrgSassignTypeCdList(TbIpAllocMstVo tbIpAllocMstVo);
	
	/** 조직별 서비스 유형 목록 조회 select **/
	public List<CommonCodeVo> selectOrgSassignTypeCdList2(TbIpAllocMstVo tbIpAllocMstVo);
	
	/**  VPN IP현황 select **/
	public TbIpAllocMstVo selectVpnIPStat(TbIpAllocMstVo tbIpAllocMstVo);
	
	/** IPMS SVC MST Seq 조회 **/
	public BigInteger selectIpmsSvcMstSeq(TbIpAllocMstVo tbIpAllocMstVo);
	
	/** IPMS SVC 조회 **/
	public List<IpmsSvcVo> selectIpmsSvc(IpmsSvcVo ipmsSvcVo);
	
	/** TB_IP_HOST_MST selectHostSofficeList **/
	public List<CommonCodeVo> selectLinkSofficeList(IpAllocOperMstVo ipAllocOperMstVo);
	
	/** 링크정보 selectListPage **/
	public List<IpAllocOperMstVo> selectListPageTbIpLinkMst(IpAllocOperMstVo ipAllocOperMstVo);
	
	/** 링크정보 countListPage **/
	public int countListPageTbIpLinkMst(IpAllocOperMstVo ipAllocOperMstVo);
	
}