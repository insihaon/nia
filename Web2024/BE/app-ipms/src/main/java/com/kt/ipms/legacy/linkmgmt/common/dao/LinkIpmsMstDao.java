package com.kt.ipms.legacy.linkmgmt.common.dao;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;

import com.kt.ipms.legacy.linkmgmt.common.vo.TbIpAlloLinkOperMstVo;
import com.kt.ipms.legacy.linkmgmt.common.vo.TbIpAllocNeossMstVo;
import com.kt.ipms.legacy.linkmgmt.common.vo.TbLnkIpmsSvcMstvo;
import com.kt.ipms.legacy.opermgmt.whoismgmt.vo.TbWhoisVo;

public interface LinkIpmsMstDao {

	/**
	 * @MEthod 	: selectIpmsSvcInfo
	 * @Date	: 2014. 9. 15.
	 * @Author	: neoargon
	 * @DESC	: Nes_IPM_WS_0001 selectIpmsSvcInfo(): IPMS 상품정보 조회
	 * @변경이력 	:
	 * @param nes0001Request
	 * @return
	 * @throws Exception
	 */
	public List<TbLnkIpmsSvcMstvo> selectIpmsSvcInfo(TbLnkIpmsSvcMstvo tbLnkIpmsSvcMstvo);
	
	/**
	 * @MEthod 	: selectBatchIPMSSVCMST
	 * @Date	: 2014. 9. 24.
	 * @Author	: neoargon
	 * @DESC	: IPM_Nef_WS_0001 insertIpmsSvcInfoList() : IPMS 상품정보 연동 
	 * @변경이력 	:
	 * @return
	 * @throws Exception
	 */
	public List<TbLnkIpmsSvcMstvo> selectBatchIPMSSVCMST(TbLnkIpmsSvcMstvo tbLnkIpmsSvcMstvo);

	/**
	 * @MEthod 	: selectSuggestIPList
	 * @Date	: 2014. 9. 15.
	 * @Author	: neoargon
	 * @DESC	: Nes_IPM_WS_0003 selectSuggestIPList(): 가용 IPBlock 추천 List 연동
	 * @변경이력 	:
	 * @param TbIpAlloLinkOperMstVo
	 * @return
	 * @throws Exception
	 */
	public List<TbIpAlloLinkOperMstVo> selectSuggestIPList(TbIpAllocNeossMstVo tbIpAllocNeossMstVo);
	
	/**
	 * @MEthod 	: getdivIpBlockhash
	 * @Date	: 2014. 9. 15.
	 * @Author	: neoargon
	 * @DESC	: 추천 list 연동 관련 블럭 분할 기능
	 * 			  -1 : BIZ-ERROR : Bitmask not over 64 bit!!!
	 * 			  -2 : BIZ-ERROR : 이전 데이터 있음
	 * 			  -3 : Error : PK_tb_ticket_mst_nticket_mst_seq  / Duplication
	 * 			  0 : OK
	 * 			  999 : DB ERROR
	 * @변경이력 	:
	 * @param hparam
	 * @return
	 * @throws Exception
	 */
	public int getdivIpBlock(HashMap<String, Object> hparam);
	
	/**
	 * @MEthod 	: insertReservedIpList
	 * @Date	: 2014. 9. 15.
	 * @Author	: neoargon
	 * @DESC	: Nes_IPM_WS_0004 insertReservedIPList(): IP Block 할당 예약 정보 연동
	 * 			  -1 : ERROR : Deleted was cancled, Not found on tb_ip_alloc_mst by nip_assign_mst_seq :
	 * 			  -2 : Error : ak_tb_ip_alloc_mst_nip_assign_mst_seq  / Duplication
	 * 			  0  : OK
	 * 			  999 : DB ERROR
	 * @변경이력 	:
	 * @param hparam
	 * @return
	 * @throws Exception
	 */
	public int insertReservedIpList(HashMap<String, Object> hparam);
	
	/**
	 * @MEthod 	: insertFixIpList
	 * @Date	: 2014. 9. 15.
	 * @Author	: neoargon
	 * @DESC	:  Nes_IPM_WS_0005 insertFixIPList(): IP Block 확정 정보 연동
	 * 			 0  : OK
	 * 			 999 : DB ERROR
	 * @변경이력 	:
	 * @param hparam
	 * @return
	 * @throws Exception
	 */
	public int insertFixIpList(HashMap<String, Object> hparam);
	
	/**
	 * @Method	:	insertReservedVpnIpList
	 * @Date	:	2014. 12. 3.
	 * @Author	:	neoargon
	 * @DESC	:	Nes_IPM_WS_0008 insertVPNFixIPList() : VPN IP Block 정보를 예약/취소
	 * 				0  : OK
	 * 			 	999 : DB ERROR
	 * @변경이력	:
	 * @param hparam
	 * @return int
	 */
	public int insertReservedVpnIpList(HashMap<String, Object> hparam);
	
	/**
	 * @Method	:	insertFixVpnIpList
	 * @Date	:	2014. 12. 3.
	 * @Author	:	neoargon
	 * @DESC	:	Nes_IPM_WS_0008 insertVPNFixIPList() : VPN IP Block 정보를 확정/취소
	 * 				0  : OK
	 * 			 	999 : DB ERROR
	 * @변경이력	:
	 * @param hparam
	 * @return int
	 */
	public int insertFixVpnIpList(HashMap<String, Object> hparam);
	
	/**
	 * @MEthod 	: selectFixedIPList
	 * @Date	: 2014. 9. 17.
	 * @Author	: neoargon
	 * @DESC	: Nes_IPM_WS_0006 selectFixedIPList(): IPMS IP 할당 정보 조회
	 * @변경이력 	:
	 * @param paramVo
	 * @return
	 * @throws Exception
	 */
	public List<TbIpAlloLinkOperMstVo> selectFixedIPList(TbIpAlloLinkOperMstVo paramVo);
	
	/**
	 * @MEthod 	: selectFixedLacpIPList
	 * @Date	: 2014. 9. 17.
	 * @Author	: neoargon
	 * @DESC	: Nes_IPM_WS_0006 selectFixedIPList(): IPMS LACP IP 할당 정보 조회
	 * @변경이력 	:
	 * @param paramVo
	 * @return
	 * @throws Exception
	 */
	public List<TbIpAlloLinkOperMstVo> selectFixedLacpIPList(TbIpAlloLinkOperMstVo paramVo);
	
	/**
	 * SDN_IPM_WS_0001 selectSuggestIPListSdn(): 가용 IP Block 조회
	 * @param paramVo
	 * @return
	 */
	public List<TbIpAlloLinkOperMstVo> selectSuggestIPListSdn(TbIpAllocNeossMstVo tbIpAllocNeossMstVo);
	
	public String countSelectSuggestIPListSdn(TbIpAllocNeossMstVo tbIpAllocNeossMstVo);
	
	public List<TbIpAlloLinkOperMstVo> selectDivSuggestIPListSdn(TbIpAllocNeossMstVo tbIpAllocNeossMstVo);
	/**
	 * SDN_IPM_WS_0001 selectMstVo(): IPMS 확정/회수 IP 정보 조회
	 * @param paramVo
	 * @return
	 */
	public TbIpAlloLinkOperMstVo selectMstVo(TbIpAlloLinkOperMstVo tbIpAlloLinkOperMstVo);
	
	/**
	 * SDN_IPM_WS_0001 selectMstAllocVo(): IPMS 할당 테이블 조회
	 * @param paramVo
	 * @return
	 */
	public TbIpAlloLinkOperMstVo selectMstAllocVo(TbIpAllocNeossMstVo tbIpAllocNeossMstVo);
	
	public List<TbIpAlloLinkOperMstVo> selectMstAllocVoList(TbIpAllocNeossMstVo tbIpAllocNeossMstVo);
	
	public int countAlloc(TbIpAllocNeossMstVo tbIpAllocNeossMstVo); 
	
	/**
	 * @MEthod 	: insertFixIpListSdn
	 * @DESC	:  SDN_IPM_WS_0002 insertFixIpListSdn(): IP Block 확정 정보 연동
	 * 			 0  : OK
	 * 			 999 : DB ERROR
	 * @변경이력 	:
	 * @param hparam
	 * @return
	 * @throws Exception
	 */
	public int insertFixIpListSdn(HashMap<String, Object> hparam);
	
	/**
	 * @MEthod 	: selectFixedIPListSdn
	 * @DESC	: SDN_IPM_WS_0003 selectFixedIPListSdn(): IPMS IP 할당 정보 조회
	 * @변경이력 	:
	 * @param paramVo
	 * @return
	 * @throws Exception
	 */
	public List<TbIpAlloLinkOperMstVo> selectFixedIPListSdn(TbIpAlloLinkOperMstVo paramVo);
	
	/**
	 * @MEthod 	: selectFixedIPSdn
	 * @DESC	: SDN_IPM_WS_0004 selectFixedIPSdn(): IPMS IP 할당 정보 조회
	 * @변경이력 	:
	 * @param paramVo
	 * @return
	 * @throws Exception
	 */
	public TbIpAlloLinkOperMstVo selectFixedIPSdn(TbIpAlloLinkOperMstVo paramVo);
	
	/**
	 * TbAllocMst Ssaid 조회
	 * @param tbIpAllocNeossMstVo
	 * @return
	 */
	public List<TbIpAlloLinkOperMstVo> selectSsaidVo (TbIpAllocNeossMstVo tbIpAllocNeossMstVo);
	
	/**
	 * 라우팅 정보 조회
	 * @param pipPrefix
	 * @return
	 */
	public TbIpAlloLinkOperMstVo selectRoutingResult(TbIpAllocNeossMstVo tbIpAllocNeossMstVo);
	
	/**
	 * @MEthod 	: insertReservedIPListSdn
	 * @DESC	:  SDN_IPM_WS_0005 insertReservedIPListSdn(): IP Block 할당 예약 정보 연동
	 * 			 0  : OK
	 * 			 999 : DB ERROR
	 * @변경이력 	:
	 * @param hparam
	 * @return
	 * @throws Exception
	 */
	public int insertReservedIPListSdn(HashMap<String, Object> hparam);
	
	public int insertTbNeossErrorManage(TbIpAllocNeossMstVo tbIpAllocNeossMstVo);
}
