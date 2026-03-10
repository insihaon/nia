package com.kt.ipms.legacy.ipmgmt.routmgmt.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.kt.ipms.legacy.ipmgmt.allocmgmt.vo.IpAllocOperMstVo;
import com.kt.ipms.legacy.ipmgmt.routmgmt.vo.TbRoutChkMstVo;
import com.kt.ipms.legacy.opermgmt.intgrmgmt.vo.TbFcltCmdMstVo;


@Mapper
public interface TbRoutChkMstDao {

	/**
	 * IP주소 라우팅 비교/점검 > 목록 조회
	 * @param tbRoutChkMstVo
	 * @return
	 */
	public List<TbRoutChkMstVo> selectListPageTbRoutChkMstVo(TbRoutChkMstVo tbRoutChkMstVo);
	
	/**
	 * IP주소 라우팅 비교/점검 > 엑셀 조회
	 * @param tbRoutChkMstVo
	 * @return
	 */
	public List<TbRoutChkMstVo> selectListExcelRoutChkMstVo(TbRoutChkMstVo tbRoutChkMstVo);
	
	/**
	 * IP주소 라우팅 비교/점검 > 건수 조회
	 * @param tbRoutChkMstVo
	 * @return
	 */
	public int countListPageTbRoutChkMstVo(TbRoutChkMstVo tbRoutChkMstVo);
	
	/**
	 * IP주소 라우팅 비교/점검 > 건수 조회 (체크박스 기준)
	 * @param tbRoutChkMstVo
	 * @return
	 */
	public int countListPageTbRoutChkMstVo2(TbRoutChkMstVo tbRoutChkMstVo);
	
	/**
	 * IP주소 라우팅 비교/점검 > 건수 조회 (ipms 기준)
	 * @param tbRoutChkMstVo
	 * @return
	 */
	public int countListPageTbRoutChkMstVo3(TbRoutChkMstVo tbRoutChkMstVo);
	
	/**
	 * IP주소 라우팅 비교/점검 > 목록 조회
	 * @param tbRoutChkMstVo
	 * @return
	 */
	public List<TbRoutChkMstVo> selectListTarget(TbRoutChkMstVo tbRoutChkMstVo);

	/**
	 * IP주소 라우팅 비교/점검 > 수정
	 * @param tbRoutChkMstVo
	 * @return
	 */
	public int updateRoutChkMstVo(TbRoutChkMstVo tbRoutChkMstVo);
	
	/**
	 * IP주소 라우팅 비교/점검 > 수정
	 * @param tbRoutChkMstVo
	 * @return
	 */
	public int updateRoutChkExcptMstVo(TbRoutChkMstVo tbRoutChkMstVo);
	
	/**
	 * IP주소 라우팅 비교/점검 > 조직별 장비별 명령어 조회
	 * @param tbRoutChkMstVo
	 * @return
	 */
	public List<TbFcltCmdMstVo> selectListFcltOrgCmdMstVo(TbRoutChkMstVo tbRoutChkMstVo);
	
	/**
	 * IP주소 라우팅 비교/점검 > 예외 처리 관리 팝업 > 예외 처리
	 * @param tbRoutChkMstVo
	 * @return
	 */
	public int insertRoutExcptMstVo(TbRoutChkMstVo tbRoutChkMstVo);
	
	/**
	 * IP주소 라우팅 비교/점검 > 예외 처리 관리 팝업 > 예외 처리 해제
	 * @param tbRoutChkMstVo
	 * @return
	 */
	public int deleteRoutExcptMstVo(TbRoutChkMstVo tbRoutChkMstVo);
	
	/**
	 * IP주소 라우팅 비교/점검 > 예외 처리 관리 팝업 > 예외 처리 상세 조회
	 * @param tbRoutChkMstVo
	 * @return
	 */
	public List<TbRoutChkMstVo> selectListPageRoutExcptMstVo(TbRoutChkMstVo tbRoutChkMstVo);
	
	/**
	 * IP주소 라우팅 비교/점검 > IP블록 현행화 전 서비스 조회
	 * @param tbRoutChkMstVo
	 * @return
	 */
	public List<TbRoutChkMstVo> selectListIpBlockServiceCheck(TbRoutChkMstVo tbRoutChkMstVo);
	
	/**
	 * IP주소 라우팅 비교/점검 > IP블록 현행화 전 서비스 조회
	 * @param tbRoutChkMstVo
	 * @return
	 */
	public List<TbRoutChkMstVo> selectIpBlockServiceCheckList(TbRoutChkMstVo tbRoutChkMstVo);
	
	/**
	 * IP주소 라우팅 비교/점검 > IP블록 현행화 전 서비스 조회
	 * @param tbRoutChkMstVo
	 * @return
	 */
	public int countIpBlockServiceCheckList(TbRoutChkMstVo tbRoutChkMstVo);
	
	/**
	 * IP주소 라우팅 비교/점검 > IP블록 현행화 전 서비스 조회
	 * @param tbRoutChkMstVo
	 * @return
	 */
	public List<TbRoutChkMstVo> selectIpBlockServiceCheckList2(TbRoutChkMstVo tbRoutChkMstVo);
	
	/**
	 * IP주소 라우팅 비교/점검 > IP블록 현행화 전 서비스 조회
	 * @param tbRoutChkMstVo
	 * @return
	 */
	public int countIpBlockServiceCheckList2(TbRoutChkMstVo tbRoutChkMstVo);
	
	public int updateRoutMgmtSeq(TbRoutChkMstVo tbRoutChkMstVo);
	
	/**
	 * IP주소 라우팅 비교/점검 > IP블록 현행화 전 서비스 조회
	 * @param tbRoutChkMstVo
	 * @return
	 */
	public IpAllocOperMstVo selectNextHop(IpAllocOperMstVo ipAllocOperMstVo);
	
	public int countNextHop(IpAllocOperMstVo ipAllocOperMstVo);
	
	public IpAllocOperMstVo selectLinkMst(IpAllocOperMstVo ipAllocOperMstVo);
	
	public int countLinkMst(IpAllocOperMstVo ipAllocOperMstVo);
	
	public IpAllocOperMstVo selectNextHopHost(IpAllocOperMstVo ipAllocOperMstVo);
	
	public int countNextHopHost(IpAllocOperMstVo ipAllocOperMstVo);
	
}
