package com.kt.ipms.legacy.ipmgmt.ordermgmt.dao;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.kt.ipms.legacy.cmn.vo.CommonCodeVo;
import com.kt.ipms.legacy.ipmgmt.allocmgmt.vo.IpAllocOperMstVo;
import com.kt.ipms.legacy.ipmgmt.ordermgmt.vo.IpAllocOrderMstVo;

/** TB_IP_ALLOC_ORDER_MST DAO INTERFACE **/
@Mapper
public interface IpAllocOrderMstDao {
	
	/** TB_IP_ALLOC_ORDER_MST selectListPage **/
	public List<IpAllocOrderMstVo> selectListPageTbIpAllocOrderMstVo(IpAllocOrderMstVo ipAllocOrderMstVo);

	/** TB_IP_ALLOC_ORDER_MST countListPage **/
	public int countListPageTbIpAllocOrderMstVo(IpAllocOrderMstVo ipAllocOrderMstVo);
	
	/** TB_IPMS_SVC_MST selectPage **/
	public IpAllocOrderMstVo selectNipmsSvcSeqMst(IpAllocOperMstVo ipAllocOperMstVo);
	
	/** TB_IP_ALLOC_ORDER_MST update(Aloc) 할당 처리 수정 **/
	public int updateFinIpAllocOrderMst(IpAllocOrderMstVo ipAllocOrderMstVo);
	
	/** MST.TB_IPMS_SVC_MST 서비스코드, 명칭 조회 **/
	public List<CommonCodeVo> selectListSassignLevelCds(IpAllocOperMstVo ipAllocOperMstVo);
	
	/** TB_IP_ALLOC_ORDER_MST selectSofficeList **/
	public List<CommonCodeVo> selectOrderSofficeList(IpAllocOrderMstVo ipAllocOrderMstVo);
	
	/** TB_IP_ALLOC_ORDER_MST selectSofficeList **/
	public List<IpAllocOrderMstVo> selectSetOrderOfficeList(IpAllocOrderMstVo ipAllocOrderMstVo);

	
}