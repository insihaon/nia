package com.kt.ipms.legacy.ipmgmt.createmgmt.dao;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.kt.ipms.legacy.ipmgmt.createmgmt.vo.TbIpBlockMstVo;


/** TB_IP_BLOCK_MST DAO INTERFACE **/
@Mapper
public interface TbIpBlockMstDao {
	
	/** TB_IP_BLOCK_MST insert **/
	public int insertTbIpBlockMstVo(TbIpBlockMstVo tbIpBlockMstVo);

	/** TB_IP_BLOCK_MST update **/
	public int updateTbIpBlockMstVo(TbIpBlockMstVo tbIpBlockMstVo);

	/** TB_IP_BLOCK_MST delete **/
	public int deleteTbIpBlockMstVo(TbIpBlockMstVo tbIpBlockMstVo);

	/** TB_IP_BLOCK_MST select **/
	public TbIpBlockMstVo selectTbIpBlockMstVo(TbIpBlockMstVo tbIpBlockMstVo);

	/** TB_IP_BLOCK_MST selectList **/
	public List<TbIpBlockMstVo> selectListTbIpBlockMstVo(TbIpBlockMstVo tbIpBlockMstVo);
	
	/** TB_IP_BLOCK_MST selectListPage **/
	public List<TbIpBlockMstVo> selectListPageTbIpBlockMstVo(TbIpBlockMstVo tbIpBlockMstVo);

	/** TB_IP_BLOCK_MST countListPage **/
	public int countListPageTbIpBlockMstVo(TbIpBlockMstVo tbIpBlockMstVo);
	
	/** TB_IP_BLOCK_MST selectListIpCreateSeqCd **/
	public List<String> selectListSipCreateSeqCd(String sipCreateSeqCd);
	
	/** TB_IP_BLOCK_MST selectDuplicateTbIpBlockMstVo **/
	public int countDuplicateTbIpBlockMstVo(TbIpBlockMstVo tbIpBlockMstVo);
	
	/** TB_IP_BLOCK_MST selectDuplicateTbIpBlockMstVo **/
	public int countDuplicateTbIpBlockMstVo2(TbIpBlockMstVo tbIpBlockMstVo);
	
	/** TB_IP_BLOCK_MST selectNewSipCreateSeqCd **/
	public String selectNewSipCreateSeqCd(TbIpBlockMstVo tbIpBlockMstVo);
	
	/** TB_IP_BLOCK_MST checkCountAsgnIPMst **/
	public int checkCountIPBlockMst(TbIpBlockMstVo tbIpBlockMstVo);
}