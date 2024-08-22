package com.kt.ipms.legacy.opermgmt.uploadmgmt.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.kt.ipms.legacy.opermgmt.uploadmgmt.vo.TbUploadVo;
import com.kt.ipms.legacy.opermgmt.uploadmgmt.vo.TbUploadZipcodeVo;

@Mapper
public interface TbUploadDao {
	public List<TbUploadVo> selectTbUploadVo(TbUploadVo tbUploadVo);
	
	public int countListTbUploadVo(TbUploadVo tbUploadVo);
	
	public int insertTbUploadVo(TbUploadVo tbUploadVo);
	
	public String selectmaxdate();
	
	public int selectCountTbNewZipcode(TbUploadZipcodeVo tbUploadZipcodeVo);
	
	public int insertTbNewZipcode(TbUploadZipcodeVo tbUploadZipcodeVo);
	
}
