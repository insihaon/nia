package com.kt.ipms.legacy.opermgmt.whoismgmt.dao;
import java.math.BigInteger;
import java.util.List;

import com.kt.ipms.legacy.cmn.vo.CommonCodeVo;
import com.kt.ipms.legacy.opermgmt.whoismgmt.vo.TbWhoisComplexVo;
import com.kt.ipms.legacy.opermgmt.whoismgmt.vo.TbWhoisUserVo;
import com.kt.ipms.legacy.opermgmt.whoismgmt.vo.TbWhoisVo;
import com.kt.ipms.legacy.opermgmt.whoismgmt.vo.WhoisStatusVo;


/** TB_WHOIS DAO INTERFACE **/
public interface TbWhoisDao {
	
	/** TB_WHOIS insertTbWhoisVo **/
	public int insertTbWhoisVo(TbWhoisVo tbWhoisVo);

	/** TB_WHOIS updateTbWhoisVo **/
	public int updateTbWhoisVo(TbWhoisVo tbWhoisVo);

	/** TB_WHOIS deleteTbWhoisVo **/
	public int deleteTbWhoisVo(TbWhoisVo tbWhoisVo);

	/** TB_WHOIS selectTbWhoisVo **/
	public TbWhoisVo selectTbWhoisVo(TbWhoisVo tbWhoisVo);

	/** TB_WHOIS selectListTbWhoisVo **/
	public List<TbWhoisVo> selectListTbWhoisVo(TbWhoisVo tbWhoisVo);
	
	/** TB_WHOIS countListTbWhoisVo **/
	public int countListTbWhoisVo(TbWhoisVo tbWhoisVo);
	
	/** TB_WHOIS selectListPageTbWhoisVo **/
	public List<TbWhoisVo> selectListPageTbWhoisVo(TbWhoisVo tbWhoisVo);

	/** TB_WHOIS countListPageTbWhoisVo **/
	public int countListPageTbWhoisVo(TbWhoisVo tbWhoisVo);

	public WhoisStatusVo countWhoisByStatus();
	
	public List<CommonCodeVo> selectListVTbWhoisReqTypeCd();
	
	public List<CommonCodeVo> selectListVTbWhoisTransStatusCd();
	
	public List<String> selectListScity();
	
	public int updateResultCd(TbWhoisVo tbWhoisVo);
	
	public BigInteger selectTbWhoisVoSeq(TbWhoisVo tbWhoisVo);
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/** TB_WHOIS updateTbWhoisVo **/
	public int updateTbWhoisNewVo(TbWhoisVo tbWhoisVo);
	
	public int deleteTbWhoisNewVo(TbWhoisVo tbWhoisVo);
	
	public int deleteTbWhoisUserNewVo(TbWhoisVo tbWhoisVo);
	
	public int updateTbWhoisTrans(TbWhoisVo tbWhoisVo);
	
	public TbWhoisComplexVo selectListTbWhoisControlIp(TbWhoisVo tbWhoisVo);
	
	public int countListTbWhoisControlIp(TbWhoisVo tbWhoisVo);

	public String selectWhoisComplexNew(TbWhoisVo tbWhoisVo);
	
	public String matchTbWhoisVo(TbWhoisVo tbWhoisVo);
	
	public TbWhoisComplexVo selectWhoisAlloc(TbWhoisVo tbWhoisVo);
	
	public List<TbWhoisVo> selectListDbMatch(TbWhoisVo tbWhoisVo);
	
	public int countListDbMatch(TbWhoisVo tbWhoisVo);
	
}