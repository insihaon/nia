package com.kt.ipms.legacy.cmn.dao;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ExcelCheckDao {
	public String selectExcelDown();
	
	public String selectExcelUp();
	
	public int updateExcelDown(String excel_flag);
	
	public int updateExcelup(String excel_flag);
	
	public String selectModifyDt();
}
