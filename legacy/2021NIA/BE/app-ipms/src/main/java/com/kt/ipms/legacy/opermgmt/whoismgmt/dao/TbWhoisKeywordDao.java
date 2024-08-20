package com.kt.ipms.legacy.opermgmt.whoismgmt.dao;
import java.util.List;
import com.kt.ipms.legacy.opermgmt.whoismgmt.vo.TbWhoisKeywordVo;


/** TB_WHOIS_KEYWORD DAO INTERFACE **/
public interface TbWhoisKeywordDao {
	
	/** TB_WHOIS_KEYWORD insertTbWhoisKeywordVo **/
	public int insertTbWhoisKeywordVo(TbWhoisKeywordVo tbWhoisKeywordVo);

	/** TB_WHOIS_KEYWORD updateTbWhoisKeywordVo **/
	public int updateTbWhoisKeywordVo(TbWhoisKeywordVo tbWhoisKeywordVo);

	/** TB_WHOIS_KEYWORD deleteTbWhoisKeywordVo **/
	public int deleteTbWhoisKeywordVo(TbWhoisKeywordVo tbWhoisKeywordVo);

	/** TB_WHOIS_KEYWORD selectTbWhoisKeywordVo **/
	public TbWhoisKeywordVo selectTbWhoisKeywordVo(TbWhoisKeywordVo tbWhoisKeywordVo);

	/** TB_WHOIS_KEYWORD selectListTbWhoisKeywordVo **/
	public List<TbWhoisKeywordVo> selectListTbWhoisKeywordVo(TbWhoisKeywordVo tbWhoisKeywordVo);
	
	/** TB_WHOIS_KEYWORD countListTbWhoisKeywordVo **/
	public int countListTbWhoisKeywordVo(TbWhoisKeywordVo tbWhoisKeywordVo);
	
	/** TB_WHOIS_KEYWORD selectListPageTbWhoisKeywordVo **/
	public List<TbWhoisKeywordVo> selectListPageTbWhoisKeywordVo(TbWhoisKeywordVo tbWhoisKeywordVo);

	/** TB_WHOIS_KEYWORD countListPageTbWhoisKeywordVo **/
	public int countListPageTbWhoisKeywordVo(TbWhoisKeywordVo tbWhoisKeywordVo);
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/** TB_WHOIS_KEYWORD selectListPageTbWhoisKeywordVo **/
	public List<TbWhoisKeywordVo> selectListPageTbWhoisKeywordNewVo(TbWhoisKeywordVo tbWhoisKeywordVo);
	
	/** TB_WHOIS_KEYWORD countListTbWhoisKeywordVo **/
	public int countListPageTbWhoisKeywordNewVo(TbWhoisKeywordVo tbWhoisKeywordVo);
	
	/** TB_WHOIS_KEYWORD insertTbWhoisKeywordVo **/
	public int insertTbWhoisKeywordNewVo(TbWhoisKeywordVo tbWhoisKeywordVo);
	
	/** TB_WHOIS_KEYWORD countListTbWhoisKeywordVo **/
	public int countListTbWhoisKeywordNewVo(TbWhoisKeywordVo tbWhoisKeywordVo);
	
	/** TB_WHOIS_KEYWORD deleteTbWhoisKeywordVo **/
	public int deleteTbWhoisKeywordNewVo(TbWhoisKeywordVo tbWhoisKeywordVo);
	
}