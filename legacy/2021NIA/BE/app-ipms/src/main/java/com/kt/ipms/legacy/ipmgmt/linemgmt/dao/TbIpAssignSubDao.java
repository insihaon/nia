package com.kt.ipms.legacy.ipmgmt.linemgmt.dao;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.kt.ipms.legacy.cmn.vo.CommonCodeVo;
import com.kt.ipms.legacy.ipmgmt.linemgmt.vo.TbIpAssignSubListVo;
import com.kt.ipms.legacy.ipmgmt.linemgmt.vo.TbIpAssignSubVo;


/** TB_IP_ASSIGN_SUB DAO INTERFACE **/
@Mapper
public interface TbIpAssignSubDao {
	
	/** TB_IP_ASSIGN_SUB insert **/
	public int insertTbIpAssignSubVo(TbIpAssignSubVo tbIpAssignSubVo);

	/** TB_IP_ASSIGN_SUB update **/
	public int updateTbIpAssignSubVo(TbIpAssignSubVo tbIpAssignSubVo);

	/** TB_IP_ASSIGN_SUB delete **/
	public int deleteTbIpAssignSubVo(TbIpAssignSubVo tbIpAssignSubVo);

	/** TB_IP_ASSIGN_SUB select **/
	public TbIpAssignSubVo selectTbIpAssignSubVo(TbIpAssignSubVo tbIpAssignSubVo);

	/** TB_IP_ASSIGN_SUB selectList **/
	public List<TbIpAssignSubVo> selectListTbIpAssignSubVo(TbIpAssignSubVo tbIpAssignSubVo);
	
	/** TB_IP_ASSIGN_SUB selectListPage **/
	public List<TbIpAssignSubVo> selectListPageTbIpAssignSubVo(TbIpAssignSubVo tbIpAssignSubVo);

	/** TB_IP_ASSIGN_SUB countListPage **/
	public int countListPageTbIpAssignSubVo(TbIpAssignSubVo tbIpAssignSubVo);

	/** TB_IP_ASSIGN_SUB selectListTbIpAssignSubVoViaInSubSeq **/
	public List<TbIpAssignSubVo> selectListTbIpAssignSubVoViaInSubSeq(TbIpAssignSubListVo tbIpAssignSubListVo);
	
	/** TB_IP_ALLOC_MST selectOfficeList **/
	public List<TbIpAssignSubVo> selectOfficeList(TbIpAssignSubVo tbIpAssignSubVo);
	
	/** TB_IP_ALLOC_MST selectLoadOfficeList **/
	public List<CommonCodeVo> selectLoadOfficeList(TbIpAssignSubVo tbIpAssignSubVo);
	
}