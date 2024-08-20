package com.kt.ipms.legacy.opermgmt.uploadmgmt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.kt.framework.exception.ServiceException;
import com.kt.ipms.legacy.opermgmt.uploadmgmt.vo.TbUploadListVo;
import com.kt.ipms.legacy.opermgmt.uploadmgmt.vo.TbUploadVo;
import com.kt.ipms.legacy.opermgmt.uploadmgmt.vo.TbUploadZipcodeVo;

@Component
@Transactional
public class UploadMgmtService {
	
	@Autowired
	private UploadMgmtTxService uploadMgmtTxService;
	
	public TbUploadListVo selectListPageUpload(TbUploadVo tbUploadVo){
		TbUploadListVo resultListVo = null;
		try {
			if (tbUploadVo == null) {
				throw new ServiceException("CMN.HIGH.00001");
			}
			List<TbUploadVo> resultList = uploadMgmtTxService.selectUpload(tbUploadVo);
			int totalCount = uploadMgmtTxService.countListTbUploadVo(tbUploadVo);
			resultListVo = new TbUploadListVo();
			resultListVo.setTbUploadVo(resultList);
			resultListVo.setTotalCount(totalCount);
		}catch (Exception e) {
			// System.out.println(e); //Codeeyes-Critical-sysout
			e.printStackTrace();
		}
		return resultListVo;
	}
	
	public int insertTbUploadVo(TbUploadVo tbUploadVo){
		return uploadMgmtTxService.insertTbUploadVo(tbUploadVo);
	}
	
	public String selectmaxdate(){
		return uploadMgmtTxService.selectmaxdate();
	}
	public int selectCountTbNewZipcode(TbUploadZipcodeVo tbUploadZipcodeVo){
		return uploadMgmtTxService.selectCountTbNewZipcode(tbUploadZipcodeVo);
	}
	public int insertTbNewZipcode(TbUploadZipcodeVo tbUploadZipcodeVo){
		return uploadMgmtTxService.insertTbNewZipcode(tbUploadZipcodeVo);
	}
	
}
