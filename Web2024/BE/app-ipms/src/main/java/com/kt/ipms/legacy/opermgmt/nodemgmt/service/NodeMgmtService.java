package com.kt.ipms.legacy.opermgmt.nodemgmt.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kt.framework.exception.ServiceException;
import com.kt.ipms.legacy.cmn.util.CommonCodeUtil;
import com.kt.ipms.legacy.ipmgmt.allocmgmt.service.AllocMgmtService;
import com.kt.ipms.legacy.ipmgmt.allocmgmt.vo.IpAllocMstComplexVo;
import com.kt.ipms.legacy.ipmgmt.allocmgmt.vo.IpAllocOperMstListVo;
import com.kt.ipms.legacy.ipmgmt.allocmgmt.vo.IpAllocOperMstVo;
import com.kt.ipms.legacy.ipmgmt.assignmgmt.dao.TbIpAssignMstDao;
import com.kt.ipms.legacy.ipmgmt.assignmgmt.vo.TbIpAssignMstVo;
import com.kt.ipms.legacy.opermgmt.nodemgmt.dao.NodeMgmtDao;
import com.kt.ipms.legacy.opermgmt.nodemgmt.vo.NodeMgmtListVo;
import com.kt.ipms.legacy.opermgmt.nodemgmt.vo.NodeMgmtVo;
import com.kt.ipms.legacy.opermgmt.orgmgmt.adapter.OrgMgmtAdapterService;

@Component
public class NodeMgmtService {
	
	@Lazy @Autowired
	private NodeMgmtDao nodeMgmtDao; 
	
@Lazy @Autowired
	private AllocMgmtService allocMgmtService;
	
@Lazy @Autowired
	private OrgMgmtAdapterService orgMgmtAdapterService;
	
	@Lazy @Autowired
	private TbIpAssignMstDao tbIpAssignMstDao;

	@Transactional(readOnly = true)
	public NodeMgmtListVo selectListNodeMgmt(NodeMgmtVo searchVo){
		NodeMgmtListVo resultListVo = null;
		if(searchVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try{
			List<NodeMgmtVo> resultList = nodeMgmtDao.selectListNodeMgmtVo(searchVo);
			int totalCount = nodeMgmtDao.countListPageNodeMgmtVo(searchVo);
			resultListVo = new NodeMgmtListVo();
			resultListVo.setNodeMgmtVos(resultList);//Codeeyes-Urgent-내용 없는 문장 제한
			resultListVo.setTotalCount(totalCount);
		}catch(ServiceException e){
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
			throw new ServiceException("CMN.HIGH.00023", new String[]{"요구사항리스트정보"});
		}
		return resultListVo;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public NodeMgmtVo insertNode(NodeMgmtVo nodeMgmtVo) {
		if(nodeMgmtVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		int resultCode;
		try{
			nodeMgmtDao.insertNode(nodeMgmtVo);
		} catch(ServiceException e) {
			throw e;
		} catch(Exception e) {
			throw new ServiceException("CMN.HIGH.00000");
		}
		return nodeMgmtVo;
	}
	
	@Transactional(readOnly = true)
	public NodeMgmtVo selectNode(NodeMgmtVo searchVo){
		NodeMgmtVo resultVo = null;
		if(searchVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try{
			resultVo = nodeMgmtDao.selectNode(searchVo);
		}catch(ServiceException e){
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
			throw new ServiceException("CMN.HIGH.00023", new String[]{"계위이전 상세 정보"});
		}
		return resultVo;
	}
	
	@Transactional(readOnly = true)
	public int selectNodeCount(NodeMgmtVo searchVo){
		int result = 0;
		if(searchVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try{
			result = nodeMgmtDao.countListPageNodeMgmtVo(searchVo);
		}catch(ServiceException e){
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
			throw new ServiceException("CMN.HIGH.00023", new String[]{"계위이전 상세 정보"});
		}
		return result;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public int deleteNode(NodeMgmtVo searchVo){
		if(searchVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		int resultCode;
		try{
			resultCode = nodeMgmtDao.deleteNode(searchVo);
		} catch(ServiceException e) {
			throw e;
		} catch(Exception e) {
			throw new ServiceException("CMN.HIGH.00000");
		}
		return resultCode;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public int cancelNode(NodeMgmtVo searchVo){
		if(searchVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		int resultCode;
		try{
			resultCode = nodeMgmtDao.cancelNode(searchVo);
		} catch(ServiceException e) {
			throw e;
		} catch(Exception e) {
			throw new ServiceException("CMN.HIGH.00000");
		}
		return resultCode;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public int comfirmDcompleteDate(NodeMgmtVo searchVo){
		if(searchVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		int resultCode;
		try{
			resultCode = nodeMgmtDao.comfirmDcompleteDate(searchVo);
		} catch(ServiceException e) {
			throw e;
		} catch(Exception e) {
			throw new ServiceException("CMN.HIGH.00000");
		}
		return resultCode;
	}
	
	@Transactional(readOnly = true)
	public NodeMgmtListVo selectListNodeMgmtExcel(NodeMgmtVo searchVo){
		NodeMgmtListVo resultListVo = null;
		if(searchVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try{
			int totalCount = nodeMgmtDao.countListPageNodeMgmtVo(searchVo);
			if (totalCount > CommonCodeUtil.MAX_EXCEL_ROW_COUNT) {
				throw new ServiceException("CMN.INFO.00051");
			}
			List<NodeMgmtVo> resultList = null;
			if (totalCount > 0) {
				searchVo.setFirstIndex(1);
				searchVo.setLastIndex(totalCount);
				resultList = nodeMgmtDao.selectListNodeMgmtVo(searchVo);
			}

			resultListVo = new NodeMgmtListVo();
			resultListVo.setNodeMgmtVos(resultList);
			resultListVo.setTotalCount(totalCount);
		}catch(ServiceException e){
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
			throw new ServiceException("CMN.HIGH.00023", new String[]{"요구사항리스트정보"});
		}
		return resultListVo;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteListAllocIPMst(IpAllocOperMstListVo resultListVo, IpAllocMstComplexVo tempObject, String userId){
		try{
			for(int i=0; i < resultListVo.getTotalCount(); i++){
				List<IpAllocOperMstVo> destIpAllocMstVos = new ArrayList<IpAllocOperMstVo>();
				IpAllocOperMstVo tempVo = new IpAllocOperMstVo();
				tempVo.setNipAllocMstSeq(resultListVo.getIpAllocOperMstVos().get(i).getNipAllocMstSeq());
				tempObject.getSrcIpAllocMstVo().setNipAllocMstSeq(resultListVo.getIpAllocOperMstVos().get(i).getNipAllocMstSeq());
				tempVo.setNipAssignMstSeq(resultListVo.getIpAllocOperMstVos().get(i).getNipAssignMstSeq());
				tempVo.setNwhoisSeq(resultListVo.getIpAllocOperMstVos().get(i).getNwhoisSeq());
				destIpAllocMstVos.add(tempVo);
				tempObject.setDestIpAllocMstVos(destIpAllocMstVos);
				tempObject.getSrcIpAllocMstVo().setSmodifyId(userId);
				IpAllocOperMstVo srcIpAllocMstVo = tempObject.getSrcIpAllocMstVo();
				srcIpAllocMstVo.setSmodifyId(userId);
				allocMgmtService.deleteListAllocIPMst(tempObject);
			}
		}catch(ServiceException e){
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
			throw new ServiceException("CMN.HIGH.00023", new String[]{"해지"});
		}
		
	}
	
	@Transactional(readOnly = true)
	public TbIpAssignMstVo selectAssignMst(TbIpAssignMstVo srcIpAssignMstVo){
		TbIpAssignMstVo resultVo = null;
		if(srcIpAssignMstVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try{
			resultVo = tbIpAssignMstDao.selectTbIpAssignMstVo(srcIpAssignMstVo);
		}catch(ServiceException e){
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
			throw new ServiceException("CMN.HIGH.00023", new String[]{"계위이전 상세 정보"});
		}
		return resultVo;
	}
}
