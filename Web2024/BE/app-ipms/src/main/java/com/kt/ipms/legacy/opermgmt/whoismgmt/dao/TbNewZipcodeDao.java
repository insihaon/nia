package com.kt.ipms.legacy.opermgmt.whoismgmt.dao;

import java.util.List;

import com.kt.ipms.legacy.opermgmt.whoismgmt.vo.TbNewZipcodeVo;
import com.kt.ipms.legacy.opermgmt.whoismgmt.vo.TbWhoisVo;

public interface TbNewZipcodeDao {
	public String selectEaddrDetail(String addrDetail);
	
	public List<TbNewZipcodeVo> selectListPageTbNewZipcode(TbNewZipcodeVo tbNewZipcodeVo);
	
	public int countListPageTbNewZipcode(TbNewZipcodeVo tbNewZipcodeVo);
	
}
