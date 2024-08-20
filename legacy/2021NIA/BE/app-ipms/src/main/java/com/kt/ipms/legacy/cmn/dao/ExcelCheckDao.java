package com.kt.ipms.legacy.cmn.dao;

public interface ExcelCheckDao {
	public String selectExcelDown();
	
	public String selectExcelUp();
	
	public int updateExcelDown(String excel_flag);
	
	public int updateExcelup(String excel_flag);
	
	public String selectModifyDt();
}
