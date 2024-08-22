package com.kt.ipms.legacy.opermgmt.whoismgmt.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.kt.ipms.legacy.opermgmt.whoismgmt.vo.TbNewZipcodeVo;

@Mapper
public interface TbNewZipcodeDao {
	public String selectEaddrDetail(String addrDetail);
	
	public List<TbNewZipcodeVo> selectListPageTbNewZipcode(TbNewZipcodeVo tbNewZipcodeVo);
	
	public int countListPageTbNewZipcode(TbNewZipcodeVo tbNewZipcodeVo);
	
}
