package com.kt.ipms.legacy.linkmgmt.errormgmt.dao;

import java.util.List;

import com.kt.ipms.legacy.linkmgmt.errormgmt.vo.ErrorMgmtListVo;
import com.kt.ipms.legacy.linkmgmt.errormgmt.vo.ErrorMgmtVo;

public interface ErrorMgmtDao {
	public List<ErrorMgmtVo> selectListErrorMgmtVo(ErrorMgmtVo errorMgmtVo);
	
	public int countListPageErrorMgmtVo(ErrorMgmtVo errorMgmtVo);
	
	public int updateTbNeossErrorManage(ErrorMgmtListVo errorMgmtListVo);
}
