package com.kt.ipms.legacy.cmn.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kt.ipms.legacy.cmn.dao.ExcelCheckDao;

@Component
@Transactional
public class ExcelCheckService {

	@Lazy @Autowired
	private ExcelCheckDao excelDao;
	
	@Transactional(readOnly = true)
	public String selectIpExcelDownCheck() {
		return excelDao.selectExcelDown();
	}
	
	@Transactional(readOnly = true)
	public String selectIpExcelUpCheck() {
		return excelDao.selectExcelUp();
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public int updateExcelDown(String excel_flag){
		return excelDao.updateExcelDown(excel_flag);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public int updateExcelUp(String excel_flag){
		return excelDao.updateExcelup(excel_flag);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public String selectModifyDt(){
		return excelDao.selectModifyDt();
	}
}
