package com.kt.ipms.legacy.opermgmt.whoismgmt.dao;

import java.util.List;

import com.kt.ipms.legacy.opermgmt.whoismgmt.vo.TbZipcodeVo;

public interface TbZipcodeDao {
	public List<TbZipcodeVo> selectListPageTbZipcode(TbZipcodeVo tbZipcodeVo);

	public String selectEaddrDetail(String addrDetail);
}
