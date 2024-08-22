package com.kt.ipms.legacy.linkmgmt.errormgmt.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.kt.ipms.legacy.linkmgmt.errormgmt.vo.ErrorMgmtListVo;
import com.kt.ipms.legacy.linkmgmt.errormgmt.vo.ErrorMgmtVo;

@Mapper
public interface ErrorMgmtDao {
	public List<ErrorMgmtVo> selectListErrorMgmtVo(ErrorMgmtVo errorMgmtVo);
	
	public int countListPageErrorMgmtVo(ErrorMgmtVo errorMgmtVo);
	
	public int updateTbNeossErrorManage(ErrorMgmtListVo errorMgmtListVo);
}
