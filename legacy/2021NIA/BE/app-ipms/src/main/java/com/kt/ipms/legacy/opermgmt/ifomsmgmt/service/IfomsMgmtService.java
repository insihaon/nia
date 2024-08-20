package com.kt.ipms.legacy.opermgmt.ifomsmgmt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kt.framework.exception.ServiceException;
import com.kt.ipms.legacy.cmn.util.CommonCodeUtil;
import com.kt.ipms.legacy.cmn.util.IpCalculateUtil;
import com.kt.ipms.legacy.ticketmgmt.configmgmt.vo.TbConfigInterfaceMstListVo;
import com.kt.ipms.legacy.ticketmgmt.configmgmt.vo.TbConfigInterfaceMstVo;

@Component
@Transactional
public class IfomsMgmtService {

	@Autowired
	private IfomsMgmtTxService ifomsMgmtTxService;
	
	@Autowired
	private IpCalculateUtil ipCalculateUtil;
	
	@Transactional(readOnly = true)
	public TbConfigInterfaceMstListVo selectListPageConfigInterfaceMst(TbConfigInterfaceMstVo tbConfigInterfaceMstVo) {
		TbConfigInterfaceMstListVo resultListVo = null;
		try {
			if (tbConfigInterfaceMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			
			resultListVo = ifomsMgmtTxService.selectListPageConfigInterfaceMst(tbConfigInterfaceMstVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"Interface 목록"});
		}
		return resultListVo;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void insertIFomsMst(TbConfigInterfaceMstVo tbConfigInterfaceMstVo) {
		try {
			if (tbConfigInterfaceMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			String shostIpVer = "";
			String sifIpVer = "";
			/** 입력 IP 유효성 체크 **/
			if (ipCalculateUtil.isIpPatternValidation(CommonCodeUtil.IPV4, tbConfigInterfaceMstVo.getShostIp())) {
				shostIpVer = CommonCodeUtil.IPV4;
			} else if (ipCalculateUtil.isIpPatternValidation(CommonCodeUtil.IPV6, tbConfigInterfaceMstVo.getShostIp())) {
				shostIpVer = CommonCodeUtil.IPV6;
			} else {
				throw new ServiceException("CMN.HIGH.00049", new String[]{"장비 IP", "유효하지 않은 IP"});
			}
			
			if (ipCalculateUtil.isIpPatternValidation(CommonCodeUtil.IPV4, tbConfigInterfaceMstVo.getSifIp())) {
				sifIpVer = CommonCodeUtil.IPV4;
			} else if (ipCalculateUtil.isIpPatternValidation(CommonCodeUtil.IPV6, tbConfigInterfaceMstVo.getSifIp())) {
				sifIpVer = CommonCodeUtil.IPV6;
			} else {
				throw new ServiceException("CMN.HIGH.00049", new String[]{"I/F IP", "유효하지 않은 IP"});
			}
			
			if (sifIpVer.equals(CommonCodeUtil.IPV4)) {
				if (tbConfigInterfaceMstVo.getNifIpBitmask() == null || tbConfigInterfaceMstVo.getNifIpBitmask() > 32) {
					throw new ServiceException("CMN.HIGH.00049", new String[]{"I/F IP의 Bitmask", "유효하지 않은 Bitmask"});
				}
			} else {
				if (tbConfigInterfaceMstVo.getNifIpBitmask() == null || tbConfigInterfaceMstVo.getNifIpBitmask() > 128) {
					throw new ServiceException("CMN.HIGH.00049", new String[]{"I/F IP의 Bitmask", "유효하지 않은 Bitmask"});
				}
			}
			
			if (!shostIpVer.equals(sifIpVer)) {
				throw new ServiceException("CMN.HIGH.00049", new String[]{"장비 IP와 I/F IP", "동일하지 않은 IP버전 타입"});
			}
			
			int count = ifomsMgmtTxService.countDuplicateConfigInterfaceMst(tbConfigInterfaceMstVo);
			if (count > 0) {
				throw new ServiceException("CMN.HIGH.00049", new String[]{"장비 IP와 I/F IP", "기 등록된 IP"});
			}
			
			/** 중계 라우터 등록 **/
			ifomsMgmtTxService.insertConfigInterfaceMst(tbConfigInterfaceMstVo);
			
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00020", new String[]{"중계 라우터"});
		}
	}
	
	@Transactional(readOnly = true)
	public TbConfigInterfaceMstVo selectConfigInterfaceMst(TbConfigInterfaceMstVo tbConfigInterfaceMstVo) {
		TbConfigInterfaceMstVo resultVo = null;
		try {
			if (tbConfigInterfaceMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			
			resultVo = ifomsMgmtTxService.selectConfigInterfaceMst(tbConfigInterfaceMstVo);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00023", new String[]{"Interface 상세"});
		}
		return resultVo;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void updateIFomsMst(TbConfigInterfaceMstVo tbConfigInterfaceMstVo) {
		try {
			if (tbConfigInterfaceMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			String shostIpVer = "";
			String sifIpVer = "";
			/** 입력 IP 유효성 체크 **/
			if (ipCalculateUtil.isIpPatternValidation(CommonCodeUtil.IPV4, tbConfigInterfaceMstVo.getShostIp())) {
				shostIpVer = CommonCodeUtil.IPV4;
			} else if (ipCalculateUtil.isIpPatternValidation(CommonCodeUtil.IPV6, tbConfigInterfaceMstVo.getShostIp())) {
				shostIpVer = CommonCodeUtil.IPV6;
			} else {
				throw new ServiceException("CMN.HIGH.00049", new String[]{"장비 IP", "유효하지 않은 IP"});
			}
			
			if (ipCalculateUtil.isIpPatternValidation(CommonCodeUtil.IPV4, tbConfigInterfaceMstVo.getSifIp())) {
				sifIpVer = CommonCodeUtil.IPV4;
			} else if (ipCalculateUtil.isIpPatternValidation(CommonCodeUtil.IPV6, tbConfigInterfaceMstVo.getSifIp())) {
				sifIpVer = CommonCodeUtil.IPV6;
			} else {
				throw new ServiceException("CMN.HIGH.00049", new String[]{"I/F IP", "유효하지 않은 IP"});
			}
			
			if (sifIpVer.equals(CommonCodeUtil.IPV4)) {
				if (tbConfigInterfaceMstVo.getNifIpBitmask() == null || tbConfigInterfaceMstVo.getNifIpBitmask() > 32) {
					throw new ServiceException("CMN.HIGH.00049", new String[]{"I/F IP의 Bitmask", "유효하지 않은 Bitmask"});
				}
			} else {
				if (tbConfigInterfaceMstVo.getNifIpBitmask() == null || tbConfigInterfaceMstVo.getNifIpBitmask() > 128) {
					throw new ServiceException("CMN.HIGH.00049", new String[]{"I/F IP의 Bitmask", "유효하지 않은 Bitmask"});
				}
			}
			
			if (!shostIpVer.equals(sifIpVer)) {
				throw new ServiceException("CMN.HIGH.00049", new String[]{"장비 IP와 I/F IP", "동일하지 않은 IP버전 타입"});
			}
			
			int count = ifomsMgmtTxService.countDuplicateConfigInterfaceMst(tbConfigInterfaceMstVo);
			if (count > 0) {
				throw new ServiceException("CMN.HIGH.00049", new String[]{"장비 IP와 I/F IP", "기 등록된 IP"});
			}
			
			/** 중계 라우터 수정 **/
			ifomsMgmtTxService.updateConfigInterfaceMst(tbConfigInterfaceMstVo);
			
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00021", new String[]{"중계 라우터"});
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteIFomsMst(TbConfigInterfaceMstVo tbConfigInterfaceMstVo) {
		try {
			if (tbConfigInterfaceMstVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			/** 중계 라우터 삭제 **/
			ifomsMgmtTxService.deleteConfigInterfaceMst(tbConfigInterfaceMstVo);
			
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new ServiceException("CMN.HIGH.00022", new String[]{"중계 라우터"});
		}
	}
}
