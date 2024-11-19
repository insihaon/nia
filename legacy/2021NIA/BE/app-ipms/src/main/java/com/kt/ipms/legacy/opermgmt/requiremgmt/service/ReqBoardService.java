package com.kt.ipms.legacy.opermgmt.requiremgmt.service;

import java.io.File;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.codej.base.utils.FileUtil;
import com.kt.framework.exception.ServiceException;
import com.kt.ipms.legacy.cmn.util.CommonCodeUtil;
import com.kt.ipms.legacy.opermgmt.requiremgmt.dao.ReqBoardDao;
import com.kt.ipms.legacy.opermgmt.requiremgmt.vo.ReqAdminEmailVo;
import com.kt.ipms.legacy.opermgmt.requiremgmt.vo.ReqBoardListVo;
import com.kt.ipms.legacy.opermgmt.requiremgmt.vo.ReqBoardSubVo;
import com.kt.ipms.legacy.opermgmt.requiremgmt.vo.ReqBoardVo;

@Component
public class ReqBoardService {

	@Lazy @Autowired
	private ReqBoardDao reqBoardDao;
	
	@Transactional(readOnly = true)
	public ReqBoardListVo selectListReqBoard(ReqBoardVo searchVo){
		ReqBoardListVo resultListVo = null;
		if(searchVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try{
			List<ReqBoardVo> resultList = reqBoardDao.selectListPageReqBoardVo(searchVo);
			int totalCount = reqBoardDao.countListPageReqBoardVo(searchVo);
			resultListVo = new ReqBoardListVo();
			resultListVo.setReqBoardVos(resultList);
			resultListVo.setTotalCount(totalCount);
		}catch(ServiceException e){
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
			throw new ServiceException("CMN.HIGH.00023", new String[]{"요구사항리스트정보"});
		}
		return resultListVo;
	}
	
	@Transactional(readOnly = true)
	public List<ReqBoardSubVo> selectListReqBoardSub(ReqBoardSubVo reqBoardSubVo){
		List<ReqBoardSubVo> resultListVo = null;
		if(reqBoardSubVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try{
			resultListVo = reqBoardDao.selectListReqSubVo(reqBoardSubVo);
			// System.out.println(resultListVo);  //Codeeyes-Critical-sysout
		}catch(ServiceException e){
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
			throw new ServiceException("CMN.HIGH.00023", new String[]{"요구사항리스트정보"});
		}
		return resultListVo;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public ReqBoardVo selectReqBoard(ReqBoardVo searchVo){
		ReqBoardVo resultVo = null;
		if(searchVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try{
			resultVo = reqBoardDao.selectReqBoard(searchVo);
		}catch(ServiceException e){
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
			throw new ServiceException("CMN.HIGH.00023", new String[]{"요구사항리스트정보"});
		}
		return resultVo;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public ReqBoardVo insertReq(ReqBoardVo insertVo, MultipartFile file){
		if (insertVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {
			reqBoardDao.insertReqBoardVo(insertVo);
			
			insertVo.setRboardSeq(insertVo.getSeq());
			
			reqBoardDao.insertReqBoardReply(insertVo);
			
			//file upload
			if(file != null){
				// System.out.println(file.getOriginalFilename());  //Codeeyes-Critical-sysout
				String originName = file.getOriginalFilename();
				// String uploadPath = insertVo.getRboardFilePath();
				// String extension = originName.substring(originName.lastIndexOf("."), originName.length());
				// UUID uuid = UUID.randomUUID();
				// String fileName = uuid.toString() + extension;
				// File t_file = new File(uploadPath);
				// file.transferTo(t_file);
				
				insertVo.setRboardFileOriginName(originName);
				insertVo.setRboardFileSaveName(originName);
				reqBoardDao.insertReqBoardUpload(insertVo);
			}
			
		} catch (ServiceException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("CMN.HIGH.00023", new String[]{"요구사항등록"});
		}
		return insertVo;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public ReqBoardVo deleteReq(ReqBoardVo deleteVo){
		if (deleteVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {
			reqBoardDao.deleteReqBoardVo(deleteVo);
			reqBoardDao.deleteReqBoardReply(deleteVo);
			deleteVo.setRboardSeq(deleteVo.getSeq());
			int count = reqBoardDao.selectReqBoardUploadCount(deleteVo);
			if(count != 0){
				ReqBoardVo tempVo = reqBoardDao.selectReqBoardUpload(deleteVo);
				// File temp_file = new File(tempVo.getRboardFilePath());
				// File temp_file = new File(tempVo.getRboardDownloadPath());
				// temp_file.delete();
				String path = FileUtil.getDownloadPathToFilePath(tempVo.getRboardDownloadPath());
				if(path != null && FileUtil.existFile(path)) {
					FileUtil.deleteFile(path);
				}
			}
			reqBoardDao.deleteReqBoardUpload(deleteVo);
		} catch (ServiceException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("CMN.HIGH.00023", new String[]{"요구사항삭제"});
		}
		return deleteVo;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public ReqBoardVo updateReq(ReqBoardVo updateVo, MultipartFile file, String adminYn){
		if (updateVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try {
			reqBoardDao.updateReqBoardVo(updateVo);
			
//			if(adminYn == "Y"){ //Codeeyes-Urgent-객체 비교시 == , != 사용제한
			if(adminYn.equals("Y")){
				reqBoardDao.updateReqBoardReply(updateVo);
			}
			int count = reqBoardDao.selectReqBoardUploadCount(updateVo);
			if(file != null){
				String originName = file.getOriginalFilename();
				// String uploadPath = updateVo.getRboardFilePath();
				// String extension = originName.substring(originName.lastIndexOf("."), originName.length());
				// UUID uuid = UUID.randomUUID();
				// String fileName = uuid.toString() + extension;
				// File t_file = new File(uploadPath);
				// file.transferTo(t_file); 
				updateVo.setRboardFileOriginName(originName);
				updateVo.setRboardFileSaveName(originName);
				
				if(count == 1){
					ReqBoardVo tempVo = reqBoardDao.selectReqBoardUpload(updateVo);
					// File temp_file = new File(tempVo.getRboardFilePath());
					// File temp_file = new File(tempVo.getRboardDownloadPath());
					// temp_file.delete();
					String path = FileUtil.getDownloadPathToFilePath(tempVo.getRboardDownloadPath());
					if(path != null && FileUtil.existFile(path)) {
						FileUtil.deleteFile(path);
					}
					reqBoardDao.updateReqBoardUpload(updateVo);
				}else{
					reqBoardDao.insertReqBoardUpload(updateVo);
				}
				
			} else {
				if(count ==1) {
					ReqBoardVo tempVo = reqBoardDao.selectReqBoardUpload(updateVo);
					String path = FileUtil.getDownloadPathToFilePath(tempVo.getRboardDownloadPath());
					if(path != null && FileUtil.existFile(path)) {
						FileUtil.deleteFile(path);
					}
					reqBoardDao.deleteReqBoardUpload(updateVo);
				}
			}
		} catch (ServiceException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("CMN.HIGH.00023", new String[]{"요구사항수정"});
		}
		return updateVo;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public ReqBoardVo selectReqBoardUpload(ReqBoardVo reqBoardVo){
		ReqBoardVo resultVo = null;
		if(reqBoardVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try{
			resultVo = reqBoardDao.selectReqBoardUpload(reqBoardVo);
		}catch(ServiceException e){
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
			throw new ServiceException("CMN.HIGH.00023", new String[]{"요구사항업로드정보"});
		}
		return resultVo;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public ReqBoardVo selectEmailContent(ReqBoardVo reqBoardVo){
		ReqBoardVo resultVo = null;
		if(reqBoardVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try{
			resultVo = reqBoardDao.selectEmailContent(reqBoardVo);
		}catch(ServiceException e){
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
			throw new ServiceException("CMN.HIGH.00023", new String[]{"요구사항이메일내용"});
		}
		return resultVo;
	}
	
	@Transactional(readOnly = true)
	public ReqBoardListVo selectListReqBoardExcel(ReqBoardVo searchVo){
		ReqBoardListVo resultListVo = null;
		if(searchVo == null) {
			throw new ServiceException("CMN.HIGH.00001");
		}
		try{
			
			int totalCount = reqBoardDao.countListPageReqBoardVo(searchVo);
			if (totalCount > CommonCodeUtil.MAX_EXCEL_ROW_COUNT) {
				throw new ServiceException("CMN.INFO.00051");
			}
			List<ReqBoardVo> resultList = null;
			if (totalCount > 0) {
				searchVo.setFirstIndex(1);
				searchVo.setLastIndex(totalCount);
				resultList = reqBoardDao.selectListPageReqBoardVo(searchVo);
			}
			
			resultListVo = new ReqBoardListVo();
			resultListVo.setReqBoardVos(resultList);
			resultListVo.setTotalCount(totalCount);
		}catch(ServiceException e){
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
			throw new ServiceException("CMN.HIGH.00023", new String[]{"요구사항엑셀"});
		}
		return resultListVo;
	}
	
	public List<ReqAdminEmailVo> selectAdminEmailList(){
		List<ReqAdminEmailVo> resultList = null;
		try{
			resultList = reqBoardDao.selectAdminEmailList();
		}catch(ServiceException e){
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
			throw new ServiceException("CMN.HIGH.00023", new String[]{"관리자 이메일"});
		}
		return resultList;
	}
}
