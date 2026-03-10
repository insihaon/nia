package com.kt.ipms.legacy.opermgmt.srvmgmt.adapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.kt.ipms.legacy.opermgmt.srvmgmt.service.SvcMgmtService;
import com.kt.ipms.legacy.opermgmt.srvmgmt.vo.TbIpmsSvcMstListVo;
import com.kt.ipms.legacy.opermgmt.srvmgmt.vo.TbIpmsSvcMstVo;

@Component
@Transactional
public class SvcMgmtAdapterService {

	@Lazy @Autowired
	private SvcMgmtService svcMgmtService;

	/* 상품정보 조회 */
	@Transactional(readOnly = true)
	public TbIpmsSvcMstListVo selectListIpmsSvc(TbIpmsSvcMstVo tbIpmsSvcMstVo){
		return svcMgmtService.selectListIpmsSvc(tbIpmsSvcMstVo);
	}
	
	/* 대분류 조회 */
	@Transactional(readOnly = true)
	public TbIpmsSvcMstListVo selectListSvcMgroupNm(TbIpmsSvcMstVo tbIpmsSvcMstVo){
		return svcMgmtService.selectListSvcMgroupNm(tbIpmsSvcMstVo);
	}
	
	/* 소분류 조회  */
	@Transactional(readOnly = true)
	public TbIpmsSvcMstListVo selectListSvcMgroupNm1(TbIpmsSvcMstVo tbIpmsSvcMstVo){
		return svcMgmtService.selectListSvcMgroupNm1(tbIpmsSvcMstVo);
	}
	
	/* 상품세부정보 조회 */
	@Transactional(readOnly = true)
	public TbIpmsSvcMstVo selectTbIpmsSvcMstVo(TbIpmsSvcMstVo tbIpmsSvcMstVo){
		return svcMgmtService.selectTbIpmsSvcMstVo(tbIpmsSvcMstVo);
	}
}
