package com.kt.ipms.legacy.ticketmgmt.configmgmt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kt.ipms.legacy.ipmgmt.allocmgmt.adapter.AllocMgmtAdapterService;
import com.kt.ipms.legacy.ipmgmt.allocmgmt.vo.IpAllocOperMstListVo;
import com.kt.ipms.legacy.ipmgmt.allocmgmt.vo.IpAllocOperMstVo;
import com.kt.ipms.legacy.ticketmgmt.configmgmt.dao.TbConfigInterfaceMstDao;
import com.kt.ipms.legacy.ticketmgmt.configmgmt.dao.TbConfigLinkMstDao;
import com.kt.ipms.legacy.ticketmgmt.configmgmt.dao.TbConfigRouteMstDao;
import com.kt.ipms.legacy.ticketmgmt.configmgmt.vo.TbConfigInterfaceMstVo;
import com.kt.ipms.legacy.ticketmgmt.configmgmt.vo.TbConfigLinkMstVo;
import com.kt.ipms.legacy.ticketmgmt.configmgmt.vo.TbConfigRouteMstVo;

@Component
@Transactional
public class ConfigMgmtTxService {
	
	@Autowired
	private TbConfigInterfaceMstDao tbConfigInterfaceMstDao;
	
	@Autowired
	private TbConfigLinkMstDao tbConfigLinkMstDao;
	
	@Autowired
	private TbConfigRouteMstDao tbConfigRouteMstDao;
	
	@Autowired
	private AllocMgmtAdapterService allocMgmtAdapterService;
	
	@Transactional(readOnly = true)
	public List<TbConfigInterfaceMstVo> selectListTopologyConfigInterfaceMst(TbConfigInterfaceMstVo tbConfigInterfaceMstVo) {
		return tbConfigInterfaceMstDao.selectListTopologyTbConfigInterfaceMstVo(tbConfigInterfaceMstVo);
	}
	
	@Transactional(readOnly = true)
	public int countListTopologyConfigInterfaceMst(TbConfigInterfaceMstVo tbConfigInterfaceMstVo) {
		return tbConfigInterfaceMstDao.countListTopologyTbConfigInterfaceMstVo(tbConfigInterfaceMstVo);
	}
	
	@Transactional(readOnly = true)
	public List<TbConfigInterfaceMstVo> selectListConfigInterfaceMst(TbConfigInterfaceMstVo tbConfigInterfaceMstVo) {
		return tbConfigInterfaceMstDao.selectListTbConfigInterfaceMstVo(tbConfigInterfaceMstVo);
	}
	
	@Transactional(readOnly = true)
	public int countListConfigInterfaceMst(TbConfigInterfaceMstVo tbConfigInterfaceMstVo) {
		return tbConfigInterfaceMstDao.countListTbConfigInterfaceMstVo(tbConfigInterfaceMstVo);
	}
	
	@Transactional(readOnly = true)
	public List<TbConfigLinkMstVo> selectListConfigLinkMst(TbConfigLinkMstVo tbConfigLinkMstVo) {
		return tbConfigLinkMstDao.selectListTbConfigLinkMstVo(tbConfigLinkMstVo);
	}
	
	@Transactional(readOnly = true)
	public int countListConfigLinkMst(TbConfigLinkMstVo tbConfigLinkMstVo) {
		return tbConfigLinkMstDao.countListTbConfigLinkMstVo(tbConfigLinkMstVo);
	}
	
	@Transactional(readOnly = true)
	public List<TbConfigRouteMstVo> selectListConfigRouteMst(TbConfigRouteMstVo tbConfigRouteMstVo) {
		return tbConfigRouteMstDao.selectListTbConfigRouteMstVo(tbConfigRouteMstVo);
	}
	
	@Transactional(readOnly = true)
	public int countListConfigRouteMst(TbConfigRouteMstVo tbConfigRouteMstVo) {
		return tbConfigRouteMstDao.countListTbConfigRouteMstVo(tbConfigRouteMstVo);
	}
	
	@Transactional(readOnly = true)
	public List<TbConfigRouteMstVo> selectListPageConfigRouteMst(TbConfigRouteMstVo tbConfigRouteMstVo) {
		return tbConfigRouteMstDao.selectListPageTbConfigRouteMstVo(tbConfigRouteMstVo);
	}
	
	@Transactional(readOnly = true)
	public int countListPageConfigRouteMst(TbConfigRouteMstVo tbConfigRouteMstVo) {
		return tbConfigRouteMstDao.countListPageTbConfigRouteMstVo(tbConfigRouteMstVo);
	}
	
	@Transactional(readOnly = true)
	public TbConfigRouteMstVo selectConfigRouteMst(TbConfigRouteMstVo tbConfigRouteMstVo) {
		return tbConfigRouteMstDao.selectTbConfigRouteMstVo(tbConfigRouteMstVo);
	}
	
	@Transactional(readOnly = true)
	public IpAllocOperMstListVo selectListIpAllocDetail(IpAllocOperMstVo ipAllocOperMstVo) {
		return allocMgmtAdapterService.selectListIpAllocDetail(ipAllocOperMstVo);
	}

	@Transactional(readOnly = true)
	public List<TbConfigInterfaceMstVo> selectListPageConfigInterfaceMst(TbConfigInterfaceMstVo tbConfigInterfaceMstVo) {
		return tbConfigInterfaceMstDao.selectListPageTbConfigInterfaceMstVo(tbConfigInterfaceMstVo);
	}
	
	@Transactional(readOnly = true)
	public int countListPageConfigInterfaceMst(TbConfigInterfaceMstVo tbConfigInterfaceMstVo) {
		return tbConfigInterfaceMstDao.countListPageTbConfigInterfaceMstVo(tbConfigInterfaceMstVo);
	}
	
	@Transactional(readOnly = true)
	public int countDuplicateConfigInterfaceMst(TbConfigInterfaceMstVo tbConfigInterfaceMstVo) {
		return tbConfigInterfaceMstDao.countDuplicateTbConfigInterfaceMstVo(tbConfigInterfaceMstVo);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void insertConfigInterfaceMst(TbConfigInterfaceMstVo tbConfigInterfaceMstVo) {
		tbConfigInterfaceMstDao.insertTbConfigInterfaceMstVo(tbConfigInterfaceMstVo);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void updateConfigInterfaceMst(TbConfigInterfaceMstVo tbConfigInterfaceMstVo) {
		tbConfigInterfaceMstDao.updateTbConfigInterfaceMstVo(tbConfigInterfaceMstVo);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteConfigInterfaceMst(TbConfigInterfaceMstVo tbConfigInterfaceMstVo) {
		tbConfigInterfaceMstDao.deleteTbConfigInterfaceMstVo(tbConfigInterfaceMstVo);
	}
	
	@Transactional(readOnly = true)
	public TbConfigInterfaceMstVo selectConfigInterfaceMst(TbConfigInterfaceMstVo tbConfigInterfaceMstVo) {
		return tbConfigInterfaceMstDao.selectTbConfigInterfaceMstVo(tbConfigInterfaceMstVo);
	}
	
	@Transactional(readOnly = true)
	public List<TbConfigRouteMstVo> selectListMainConfigRouteMst(TbConfigRouteMstVo tbConfigRouteMstVo) {
		return tbConfigRouteMstDao.selectListPageMainTbConfigRouteMstVo(tbConfigRouteMstVo);
	}
	
	@Transactional(readOnly = true)
	public int countListMainConfigRouteMst(TbConfigRouteMstVo tbConfigRouteMstVo) {
		return tbConfigRouteMstDao.countListPageMainTbConfigRouteMstVo(tbConfigRouteMstVo);
	}
}
