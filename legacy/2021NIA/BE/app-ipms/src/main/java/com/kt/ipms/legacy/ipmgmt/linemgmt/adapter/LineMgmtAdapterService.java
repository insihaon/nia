package com.kt.ipms.legacy.ipmgmt.linemgmt.adapter;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kt.ipms.legacy.cmn.vo.CommonCodeVo;
import com.kt.ipms.legacy.ipmgmt.linemgmt.service.LineMgmtService;
import com.kt.ipms.legacy.ipmgmt.linemgmt.vo.TbIpAssignSubVo;

@Component
@Transactional
public class LineMgmtAdapterService {

	@Autowired
	private LineMgmtService lineMgmtService;
	
	/*선번장 데이터 생성(등록)*/
	@Transactional(propagation = Propagation.REQUIRED)
	public void insertIpAssignSub(List<TbIpAssignSubVo> tbIpAssignSubVos){
		lineMgmtService.processInsertIPAssignSub(tbIpAssignSubVos);
	}
	
	/*선번장 데이터 삭제*/
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteIpAssignSub(List<TbIpAssignSubVo> tbIpAssignSubVos){
		lineMgmtService.processDeleteIPAssignSub(tbIpAssignSubVos);
	}
	
	/*수용국 데이터 조회*/
	@Transactional(readOnly = true)
	public List<CommonCodeVo> selectLoadOfficeList(TbIpAssignSubVo tbIpAssignSubVo){
		return lineMgmtService.selectLoadOfficeList(tbIpAssignSubVo);
	}
	
}
