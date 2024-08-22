package com.kt.ipms.legacy.opermgmt.whoismgmt.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.kt.ipms.legacy.opermgmt.whoismgmt.vo.TbZipcodeVo;

@Mapper
public interface TbZipcodeDao {
	public List<TbZipcodeVo> selectListPageTbZipcode(TbZipcodeVo tbZipcodeVo);

	public String selectEaddrDetail(String addrDetail);
}
