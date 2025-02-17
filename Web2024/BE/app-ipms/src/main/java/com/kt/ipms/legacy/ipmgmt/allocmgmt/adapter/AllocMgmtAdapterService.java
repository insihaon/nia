package com.kt.ipms.legacy.ipmgmt.allocmgmt.adapter;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kt.ipms.legacy.cmn.vo.CommonCodeVo;
import com.kt.ipms.legacy.ipmgmt.allocmgmt.service.AllocMgmtService;
import com.kt.ipms.legacy.ipmgmt.allocmgmt.vo.IpAllocOperMstListVo;
import com.kt.ipms.legacy.ipmgmt.allocmgmt.vo.IpAllocOperMstVo;
import com.kt.ipms.legacy.ipmgmt.allocmgmt.vo.TbIpAllocMstVo;

@Component
@Transactional
public class AllocMgmtAdapterService {

	@Lazy
	@Autowired
	private AllocMgmtService allocMgmtService;

	@Transactional(readOnly = true)
	public IpAllocOperMstListVo selectListIpAllocDetail(IpAllocOperMstVo ipAllocOperMstVo) {
		return allocMgmtService.selectListIpAllocDetail(ipAllocOperMstVo);
	}

	@Transactional(readOnly = true)
	public IpAllocOperMstListVo selectListIpAllocMst(IpAllocOperMstVo ipAllocOperMstVo) {
		return allocMgmtService.selectListIpAllocMst(ipAllocOperMstVo);
	}

	/* 단건조회(할당) */
	@Transactional(readOnly = true)
	public IpAllocOperMstVo selectIpAllocMst(IpAllocOperMstVo ipAllocOperMstVo) {
		return allocMgmtService.selectIpAllocMst(ipAllocOperMstVo);
	}

	/* 할당 처리 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void insertIPAllocMst(List<TbIpAllocMstVo> tbIpAllocMstVos) {
		allocMgmtService.processInsertIPAllocMst(tbIpAllocMstVos);
	}

	/* 단건조회(메인 IP 상세조회) */
	@Transactional(readOnly = true)
	public IpAllocOperMstVo selectMainIpInfoMst(IpAllocOperMstVo ipAllocOperMstVo) {
		return allocMgmtService.selectMainIpInfoMst(ipAllocOperMstVo);
	}

	/* 조직별 서비스 유형 셋팅(2014.12.04) */
	@Transactional(readOnly = true)
	public List<CommonCodeVo> selectOrgSassignTypeCdList(TbIpAllocMstVo tbIpAllocMstVo) {
		return allocMgmtService.selectOrgSassignTypeCdList(tbIpAllocMstVo);
	}

	/* 조직별 서비스 유형 셋팅(2014.12.04) */
	@Transactional(readOnly = true)
	public List<CommonCodeVo> selectOrgSassignTypeCdList2(TbIpAllocMstVo tbIpAllocMstVo) {
		return allocMgmtService.selectOrgSassignTypeCdList2(tbIpAllocMstVo);
	}

	/* 시설용 IP 서비스 유형 목록 조회 */
	@Transactional(readOnly = true)
	public List<CommonCodeVo> selectFacilitesTypeCdList(TbIpAllocMstVo tbIpAllocMstVo) {
		return allocMgmtService.selectFacilitesTypeCdList(tbIpAllocMstVo);
	}

}
