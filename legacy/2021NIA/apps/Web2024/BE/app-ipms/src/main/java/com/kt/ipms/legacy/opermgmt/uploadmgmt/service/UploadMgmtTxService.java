package com.kt.ipms.legacy.opermgmt.uploadmgmt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.kt.ipms.legacy.opermgmt.uploadmgmt.dao.TbUploadDao;
import com.kt.ipms.legacy.opermgmt.uploadmgmt.vo.TbUploadVo;
import com.kt.ipms.legacy.opermgmt.uploadmgmt.vo.TbUploadZipcodeVo;

@Component
public class UploadMgmtTxService {
	
	@Lazy @Autowired
	private TbUploadDao tbUploadDao;
	
	@Transactional(readOnly = true)
	public List<TbUploadVo> selectUpload(TbUploadVo tbUploadVo){
		return tbUploadDao.selectTbUploadVo(tbUploadVo);
	}
	
	@Transactional(readOnly = true)
	public int countListTbUploadVo(TbUploadVo tbUploadVo){
		return tbUploadDao.countListTbUploadVo(tbUploadVo);
	}
	
	public int insertTbUploadVo(TbUploadVo tbUploadVo){
		return tbUploadDao.insertTbUploadVo(tbUploadVo);
	}
	
	public String selectmaxdate(){
		return tbUploadDao.selectmaxdate();
	}
	
	public int selectCountTbNewZipcode(TbUploadZipcodeVo tbUploadZipcodeVo){
		return tbUploadDao.selectCountTbNewZipcode(tbUploadZipcodeVo);
	}
	
	public int insertTbNewZipcode(TbUploadZipcodeVo tbUploadZipcodeVo){
		return tbUploadDao.insertTbNewZipcode(tbUploadZipcodeVo);
	}
}


