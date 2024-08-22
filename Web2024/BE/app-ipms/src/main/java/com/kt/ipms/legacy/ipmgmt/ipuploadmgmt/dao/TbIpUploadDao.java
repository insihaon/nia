package com.kt.ipms.legacy.ipmgmt.ipuploadmgmt.dao;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.kt.ipms.legacy.ipmgmt.allocmgmt.vo.IpAllocOperMstVo;
import com.kt.ipms.legacy.ipmgmt.ipuploadmgmt.vo.TbIpUploadSubVo;
import com.kt.ipms.legacy.ipmgmt.ipuploadmgmt.vo.TbIpUploadVo;

@Mapper
public interface TbIpUploadDao {

	/* 기본 페이지 로드 */
	List<TbIpUploadVo> selectListPageIpUpload(TbIpUploadVo searchVo);

	/* 기본 페이지 카운트 */
	int countListPageIpUpload(TbIpUploadVo searchVo);

	/* 장비 목록 로드 */
	List<IpAllocOperMstVo> selectListTbIpHostMst(TbIpUploadVo searchVo);

	/* IP 업로드 mst등록 */
	void insertTbIpUploadMst(TbIpUploadVo insertVo);

	BigInteger selectAssignMstSeq(Map<String, Object> validMap);

	BigInteger selectNlvlMstSeq(Map<String, Object> retMap);

	void insertIpUploadSub(Map<String, Object> retMap);

	List<Map<String, Object>> selectCompareList(TbIpUploadVo insertVo);

	void insertIpUploadSubBlank(Map<String, Object> tempMap);

	void updateIpUploadMst(TbIpUploadVo insertVo);

	List<String> selectAllocTargetList(TbIpUploadVo insertVo);

	List<TbIpUploadSubVo> selectUploadDetail(TbIpUploadVo searchVo);

	void updateIpUploadSub(Map<String, Object> retMap);

	List<Map<String, Object>> selectListMigIpBlock1(TbIpUploadVo searchVo);
	
	List<Map<String, Object>> selectListMigIpBlock2(TbIpUploadVo searchVo);
	
	List<Map<String, Object>> selectListMigIpBlock_CL0004_Z00002(TbIpUploadVo searchVo);
	List<Map<String, Object>> selectListMigIpBlock_CL0004_R00437(TbIpUploadVo searchVo);
	List<Map<String, Object>> selectListMigIpBlock_CL0004_R00476(TbIpUploadVo searchVo);
	List<Map<String, Object>> selectListMigIpBlock_CL0004_R00454(TbIpUploadVo searchVo);
	List<Map<String, Object>> selectListMigIpBlock_CL0004_R03856(TbIpUploadVo searchVo);
	List<Map<String, Object>> selectListMigIpBlock_CL0004_R03424(TbIpUploadVo searchVo);
	List<Map<String, Object>> selectListMigIpBlock_CL0004_R00441(TbIpUploadVo searchVo);
	
	List<Map<String, Object>> selectListMigIpBlock_CL0001_VV0093(TbIpUploadVo searchVo);
	List<Map<String, Object>> selectListMigIpBlock_CL0001_VV0096(TbIpUploadVo searchVo);
	List<Map<String, Object>> selectListMigIpBlock_CL0001_VV0097(TbIpUploadVo searchVo);
	
	List<Map<String, Object>> selectListMigIpBlock_CL0002_VV0093(TbIpUploadVo searchVo);
	List<Map<String, Object>> selectListMigIpBlock_CL0002_VV0094(TbIpUploadVo searchVo);
	List<Map<String, Object>> selectListMigIpBlock_CL0002_VV0095(TbIpUploadVo searchVo);
	List<Map<String, Object>> selectListMigIpBlock_CL0002_VV0096(TbIpUploadVo searchVo);
	List<Map<String, Object>> selectListMigIpBlock_CL0002_VV0097(TbIpUploadVo searchVo);
	List<Map<String, Object>> selectListMigIpBlock_CL0002_VV0098(TbIpUploadVo searchVo);
	
	
	List<Map<String, Object>> selectListMigIpBlock_CL0003(TbIpUploadVo searchVo);
	
	
}
