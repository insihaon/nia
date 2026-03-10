package com.kt.ipms.legacy.ipmgmt.assignmgmt.adapter;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kt.ipms.legacy.ipmgmt.assignmgmt.service.AssignMgmtService;
import com.kt.ipms.legacy.ipmgmt.assignmgmt.vo.TbIpAssignMstListVo;
import com.kt.ipms.legacy.ipmgmt.assignmgmt.vo.TbIpAssignMstVo;

@Component
@Transactional
public class AssignMgmtAdapterService {

	@Lazy @Autowired
	private AssignMgmtService assignMgmtService;
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void insertIpAssignMst(TbIpAssignMstVo tbIpAssignMstVo) {
		assignMgmtService.insertIpAssignMst(tbIpAssignMstVo);
	}
	
	@Transactional(readOnly = true)
	public int countAssignBlockViaIpAssignMstVo(TbIpAssignMstVo tbIpAssignMstVo) {
		return assignMgmtService.countAssignBlockViaIpAssignMstVo(tbIpAssignMstVo);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteListIpAssignMst(TbIpAssignMstVo tbIpAssignMstVo) {
		assignMgmtService.deleteListIpAssignMst(tbIpAssignMstVo);
	}
	
	@Transactional(readOnly = true)
	public TbIpAssignMstVo selectIpAssignMst(TbIpAssignMstVo tbIpAssignMstVo) {
		return assignMgmtService.selectIpAssignMst(tbIpAssignMstVo);
	}
	
	/*서비스블록 반납 배정 수정*/
	@Transactional(propagation = Propagation.REQUIRED)
	public void updateIpAssignMst(List<TbIpAssignMstVo> tbIpAssignMstVos) {
		assignMgmtService.processUpdateAsgnIPMst(tbIpAssignMstVos);
	}
	
	/*할당,반납 별 배정 수정*/
	@Transactional(propagation = Propagation.REQUIRED)
	public void updateAlocIpAssignMst(List<TbIpAssignMstVo> tbIpAssignMstVos) {
		assignMgmtService.processAlocUpdateAsgnIPMst(tbIpAssignMstVos);
	}
	
	/*비고 수정*/
	@Transactional(propagation = Propagation.REQUIRED)
	public void updateScommentIpAssignMst(List<TbIpAssignMstVo> tbIpAssignMstVos) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		assignMgmtService.processScommentUpdateAsgnIPMst(tbIpAssignMstVos);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void updateSipCreateSeqCdIpAssignMst(TbIpAssignMstVo tbIpAssignMstVo) {
		assignMgmtService.updateSipCreateSeqCdIpAssignMst(tbIpAssignMstVo);
	}
	
	/** 조각 IP 최적화 대상 목록 **/
	@Transactional(readOnly = true)
	public TbIpAssignMstListVo selectListOptimizeIpSource(TbIpAssignMstVo tbIpAssignMstVo) {
		return assignMgmtService.selectListOptimizeIpSource(tbIpAssignMstVo);
	}
	
	/** 조각 IP IP 대체 목록 **/
	@Transactional(readOnly = true)
	public TbIpAssignMstListVo selectListOptimizeIpTarget(TbIpAssignMstVo tbIpAssignMstVo) {
		return assignMgmtService.selectListOptimizeIpTarget(tbIpAssignMstVo);
	}
	
	/** 조각 IP IP 대체 추천 목록 **/
	@Transactional(readOnly = true)
	public TbIpAssignMstListVo selectListOptimizeIpRecommend(TbIpAssignMstVo tbIpAssignMstVo) {
		return assignMgmtService.selectListOptimizeIpRecommend(tbIpAssignMstVo);
	}
	
	/** 티켓 처리 대상 List(생성-수정,삭제) 대상 조회 **/
	@Transactional(readOnly = true)
	public TbIpAssignMstListVo selectListIpAssignMst(TbIpAssignMstVo tbIpAssignMstVo) {
		return assignMgmtService.selectListIpAssignMst(tbIpAssignMstVo);
	}
	
}
