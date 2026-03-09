package com.kt.ipms.legacy.opermgmt.whoismgmt.dao;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.kt.ipms.legacy.opermgmt.whoismgmt.vo.TbWhoisUserVo;


/** TB_WHOIS_USER DAO INTERFACE **/
@Mapper
public interface TbWhoisUserDao {
	
	/** TB_WHOIS_USER insertTbWhoisUserVo **/
	public int insertTbWhoisUserVo(TbWhoisUserVo tbWhoisUserVo);

	/** TB_WHOIS_USER updateTbWhoisUserVo **/
	public int updateTbWhoisUserVo(TbWhoisUserVo tbWhoisUserVo);

	/** TB_WHOIS_USER deleteTbWhoisUserVo **/
	public int deleteTbWhoisUserVo(TbWhoisUserVo tbWhoisUserVo);

	/** TB_WHOIS_USER selectTbWhoisUserVo **/
	public TbWhoisUserVo selectTbWhoisUserVo(TbWhoisUserVo tbWhoisUserVo);

	/** TB_WHOIS_USER selectListTbWhoisUserVo **/
	public List<TbWhoisUserVo> selectListTbWhoisUserVo(TbWhoisUserVo tbWhoisUserVo);
	
	/** TB_WHOIS_USER countListTbWhoisUserVo **/
	public int countListTbWhoisUserVo(TbWhoisUserVo tbWhoisUserVo);
	
	/** TB_WHOIS_USER selectListPageTbWhoisUserVo **/
	public List<TbWhoisUserVo> selectListPageTbWhoisUserVo(TbWhoisUserVo tbWhoisUserVo);

	/** TB_WHOIS_USER countListPageTbWhoisUserVo **/
	public int countListPageTbWhoisUserVo(TbWhoisUserVo tbWhoisUserVo);

	/** TB_WHOIS_USER updateTbWhoisUserVo **/
	public int updateTbWhoisUserNewVo(TbWhoisUserVo tbWhoisUserVo);
	
	/** TB_WHOIS_USER updateKtInfoVo **/
	public int updateKtInfoVo(TbWhoisUserVo tbWhoisUserVo);

	
}