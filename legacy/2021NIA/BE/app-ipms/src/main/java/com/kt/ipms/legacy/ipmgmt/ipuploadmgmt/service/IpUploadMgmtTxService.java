package com.kt.ipms.legacy.ipmgmt.ipuploadmgmt.service;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kt.ipms.legacy.ipmgmt.allocmgmt.vo.IpAllocOperMstVo;
import com.kt.ipms.legacy.ipmgmt.ipuploadmgmt.dao.TbIpUploadDao;
import com.kt.ipms.legacy.ipmgmt.ipuploadmgmt.vo.TbIpUploadSubListVo;
import com.kt.ipms.legacy.ipmgmt.ipuploadmgmt.vo.TbIpUploadSubVo;
import com.kt.ipms.legacy.ipmgmt.ipuploadmgmt.vo.TbIpUploadVo;

@Component
@Transactional
public class IpUploadMgmtTxService {

	@Autowired
	private TbIpUploadDao tbIpUploadDao; 
	
	@Transactional(readOnly = true)
	public List<TbIpUploadVo> selectListPageIpUpload(TbIpUploadVo searchVo) {
		return tbIpUploadDao.selectListPageIpUpload(searchVo);
	}

	@Transactional(readOnly = true)
	public int countListPageIpUpload(TbIpUploadVo searchVo) {
		return tbIpUploadDao.countListPageIpUpload(searchVo);
	}

	@Transactional(readOnly = true)
	public List<IpAllocOperMstVo> selectListTbIpHostMst(TbIpUploadVo searchVo) {
		return tbIpUploadDao.selectListTbIpHostMst(searchVo);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void insertTbIpUploadMst(TbIpUploadVo insertVo) {
		tbIpUploadDao.insertTbIpUploadMst(insertVo);
	}

	@Transactional(readOnly = true)
	public BigInteger selectAssignMstSeq(Map<String, Object> validMap) {
		return tbIpUploadDao.selectAssignMstSeq(validMap);
	}

	@Transactional(readOnly = true)
	public BigInteger selectNlvlMstSeq(Map<String, Object> retMap) {
		return tbIpUploadDao.selectNlvlMstSeq(retMap);
	}

	// ipUploadSub 등록
	@Transactional(propagation = Propagation.REQUIRED)
	public void insertIpUploadSub(Map<String, Object> retMap) {
		tbIpUploadDao.insertIpUploadSub(retMap);
	}

	// 비교대상 목록 추출
	@Transactional(readOnly = true)
	public List<Map<String, Object>> selectCompareList(TbIpUploadVo insertVo) {
		return tbIpUploadDao.selectCompareList(insertVo);
	}

	// 빈캅채우기
	public void insertIpUploadSubBlank(Map<String, Object> tempMap) {
		tbIpUploadDao.insertIpUploadSubBlank(tempMap);
	}

	// 성공여부 업데이트
	public void updateIpUploadMst(TbIpUploadVo insertVo) {
		tbIpUploadDao.updateIpUploadMst(insertVo);
	}

	@Transactional(readOnly = true)
	public List<String> selectAllocTargetList(TbIpUploadVo insertVo) {
		return tbIpUploadDao.selectAllocTargetList(insertVo);
	}

	@Transactional(readOnly = true)
	public List<TbIpUploadSubVo> selectUploadDetail(TbIpUploadVo searchVo) {
		return tbIpUploadDao.selectUploadDetail(searchVo);
	}

	public void updateIpUploadSub(Map<String, Object> retMap) {
		tbIpUploadDao.updateIpUploadSub(retMap);
	}
	
	@Transactional(readOnly = true)
	public List<Map<String, Object>> selectListMigIpBlock1(TbIpUploadVo searchVo) {
		return tbIpUploadDao.selectListMigIpBlock1(searchVo);
	}

	@Transactional(readOnly = true)
	public List<Map<String, Object>> selectListMigIpBlock2(TbIpUploadVo searchVo) {
		

		
		/**
		 * 
			CL0004	20330	369807	IP운용센터	Z00002	혜화센터	27
			CL0004	20332	383040	(구)강남네트워크운용본부	R00437	구로국사	34
			CL0004	20333	383040	(구)강남네트워크운용본부	R00476	남수원국사	4
			CL0004	20334	383040	(구)강남네트워크운용본부	R00454	양재국사	4
			CL0004	20336	382973	(구)강북네트워크운용본부	R03856	동의정부국사	2
			CL0004	20337	382973	(구)강북네트워크운용본부	R03424	원주국사	4
			CL0004	20338	382973	(구)강북네트워크운용본부	R00441	혜화국사	4
			
			///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			
			CL0001	20491	369819	미디어운용센터	VV0093	미디어인프라팀	15
			CL0001	20494	369819	미디어운용센터	VV0096	모바일TV플랫폼팀	567
			CL0001	20495	369819	미디어운용센터	VV0097	기가지니플랫폼팀	18
			
			///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			 
			CL0002	20501	369819	미디어운용센터	VV0093	미디어인프라팀	299
			CL0002	20502	369819	미디어운용센터	VV0094	VOD플랫폼팀	1454
			CL0002	20503	369819	미디어운용센터	VV0095	채널제어플랫폼팀	21
			CL0002	20504	369819	미디어운용센터	VV0096	모바일TV플랫폼팀	26
			CL0002	20505	369819	미디어운용센터	VV0097	기가지니플랫폼팀	2611
			CL0002	20506	369819	미디어운용센터	VV0098	신사업운용팀	287
			
			///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			
			
			CL0003	20305	VV0010	DATA망	VV0014	DATA망(광주)	34
			CL0003	20306	VV0010	DATA망	VV0015	DATA망(구로)	430
			CL0003	20309	VV0010	DATA망	VV0015	DATA망(구로)	15
			CL0003	20307	VV0010	DATA망	VV0016	DATA망(대구)	35
			CL0003	20308	VV0010	DATA망	VV0017	DATA망(대전)	46
			CL0003	20310	VV0010	DATA망	VV0019	DATA망(부산)	31
			CL0003	20311	VV0010	DATA망	VV0020	DATA망(용인)	119
			CL0003	20313	VV0010	DATA망	VV0022	DATA망(혜화)	275
			CL0003	20452	VV0010	DATA망	VV0022	DATA망(혜화)	13

			CL0003	20462	VV0065	5G DATA망	VV0067	5G DATA망(구로)	49
			CL0003	20464	VV0065	5G DATA망	VV0068	5G DATA망(혜화)	44
			CL0003	20465	VV0065	5G DATA망	VV0069	5G DATA망(대전)	68
			CL0003	20466	VV0065	5G DATA망	VV0070	5G DATA망(광주)	84
			CL0003	20467	VV0065	5G DATA망	VV0071	5G DATA망(대구)	46
			CL0003	20468	VV0065	5G DATA망	VV0072	5G DATA망(부산)	36
			CL0003	20477	VV0065	5G DATA망	VV0081	5G DATA망(용인)	39
			
			
			CL0003	20315	VV0009	SG망	VV0023	SG망(광주)	12
			CL0003	20316	VV0009	SG망	VV0024	SG망(구로)	112
			CL0003	20319	VV0009	SG망	VV0024	SG망(구로)	2
			CL0003	20317	VV0009	SG망	VV0025	SG망(대구)	93
			CL0003	20318	VV0009	SG망	VV0026	SG망(대전)	29
			CL0003	20320	VV0009	SG망	VV0028	SG망(부산)	43
			CL0003	20321	VV0009	SG망	VV0029	SG망(용인)	217
			CL0003	20323	VV0009	SG망	VV0031	SG망(혜화)	16
			
			CL0003	20471	VV0066	5G ACCESS망	VV0075	5G ACCESS망(대전)	6
			CL0003	20473	VV0066	5G ACCESS망	VV0077	5G ACCESS망(대구)	2
			
			CL0003	20449	VV0041	ACCESS	VV0043	ACCESS(강남)	88
			CL0003	20405	VV0041	ACCESS	VV0044	ACCESS(강북)	584
			CL0003	20406	VV0041	ACCESS	VV0045	ACCESS(충청)	506
			CL0003	20407	VV0041	ACCESS	VV0046	ACCESS(호남)	599
			CL0003	20408	VV0041	ACCESS	VV0047	ACCESS(대구)	695
			CL0003	20409	VV0041	ACCESS	VV0048	ACCESS(부산)	422
			CL0003	20450	VV0041	ACCESS	VV0057	ACCESS(강서)	238
			CL0003	20460	VV0041	ACCESS	VV0064	ACCESS(강원)	18
			
		 * 
		 */
		if("CL0004".equals(searchVo.getSsvcLineTypeCd()) && "Z00002".equals(searchVo.getSsvcObjCd())) {				// GNS 혜화센터
			return tbIpUploadDao.selectListMigIpBlock_CL0004_Z00002(searchVo);
		} else if("CL0004".equals(searchVo.getSsvcLineTypeCd()) && "R00437".equals(searchVo.getSsvcObjCd())) {		// GNS 구로국사
			return tbIpUploadDao.selectListMigIpBlock_CL0004_R00437(searchVo);
		} else if("CL0004".equals(searchVo.getSsvcLineTypeCd()) && "R00476".equals(searchVo.getSsvcObjCd())) {		// GNS 남수원국사
			return tbIpUploadDao.selectListMigIpBlock_CL0004_R00476(searchVo);
		} else if("CL0004".equals(searchVo.getSsvcLineTypeCd()) && "R00454".equals(searchVo.getSsvcObjCd())) {		// GNS 양재국사
			return tbIpUploadDao.selectListMigIpBlock_CL0004_R00454(searchVo);
		} else if("CL0004".equals(searchVo.getSsvcLineTypeCd()) && "R03856".equals(searchVo.getSsvcObjCd())) {		// GNS 동의정부국사
			return tbIpUploadDao.selectListMigIpBlock_CL0004_R03856(searchVo);
		} else if("CL0004".equals(searchVo.getSsvcLineTypeCd()) && "R03424".equals(searchVo.getSsvcObjCd())) {		// GNS 원주국사
			return tbIpUploadDao.selectListMigIpBlock_CL0004_R03424(searchVo);
		} else if("CL0004".equals(searchVo.getSsvcLineTypeCd()) && "R00441".equals(searchVo.getSsvcObjCd())) {		// GNS 혜화국사
			return tbIpUploadDao.selectListMigIpBlock_CL0004_R00441(searchVo);
		} 
		
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		else if("CL0001".equals(searchVo.getSsvcLineTypeCd()) && "VV0093".equals(searchVo.getSsvcObjCd())) {	// KORNET 미디어인프라팀
			return tbIpUploadDao.selectListMigIpBlock_CL0001_VV0093(searchVo);
		} else if("CL0001".equals(searchVo.getSsvcLineTypeCd()) && "VV0096".equals(searchVo.getSsvcObjCd())) {	// KORNET 모바일TV플랫폼팀
			return tbIpUploadDao.selectListMigIpBlock_CL0001_VV0096(searchVo);
		} else if("CL0001".equals(searchVo.getSsvcLineTypeCd()) && "VV0097".equals(searchVo.getSsvcObjCd())) {	// KORNET 기가지니플랫폼팀
			return tbIpUploadDao.selectListMigIpBlock_CL0001_VV0097(searchVo);
		}
		
		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		else if("CL0002".equals(searchVo.getSsvcLineTypeCd()) && "VV0093".equals(searchVo.getSsvcObjCd())) {	// PREMIUM 미디어인프라팀
			return tbIpUploadDao.selectListMigIpBlock_CL0002_VV0093(searchVo);
		} else if("CL0002".equals(searchVo.getSsvcLineTypeCd()) && "VV0094".equals(searchVo.getSsvcObjCd())) {	// PREMIUM VOD플랫폼팀
			return tbIpUploadDao.selectListMigIpBlock_CL0002_VV0094(searchVo);
		} else if("CL0002".equals(searchVo.getSsvcLineTypeCd()) && "VV0095".equals(searchVo.getSsvcObjCd())) {	// PREMIUM 채널제어플랫폼팀
			return tbIpUploadDao.selectListMigIpBlock_CL0002_VV0095(searchVo);
		} else if("CL0002".equals(searchVo.getSsvcLineTypeCd()) && "VV0096".equals(searchVo.getSsvcObjCd())) {	// PREMIUM 모바일TV플랫폼팀
			return tbIpUploadDao.selectListMigIpBlock_CL0002_VV0096(searchVo);
		} else if("CL0002".equals(searchVo.getSsvcLineTypeCd()) && "VV0097".equals(searchVo.getSsvcObjCd())) {	// PREMIUM 기가지니플랫폼팀
			return tbIpUploadDao.selectListMigIpBlock_CL0002_VV0097(searchVo);
		} else if("CL0002".equals(searchVo.getSsvcLineTypeCd()) && "VV0098".equals(searchVo.getSsvcObjCd())) {	// PREMIUM 신사업운용팀
			return tbIpUploadDao.selectListMigIpBlock_CL0002_VV0098(searchVo);
		}
		
		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		else if("CL0003".equals(searchVo.getSsvcLineTypeCd())) {	// MOBILE
			return tbIpUploadDao.selectListMigIpBlock_CL0003(searchVo);
		} 
		
		else {
			return tbIpUploadDao.selectListMigIpBlock2(searchVo);
		}
		
	}
}
