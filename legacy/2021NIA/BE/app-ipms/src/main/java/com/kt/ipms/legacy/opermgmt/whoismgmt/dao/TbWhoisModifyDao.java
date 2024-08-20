package com.kt.ipms.legacy.opermgmt.whoismgmt.dao;

import java.util.List;

import com.kt.ipms.legacy.linkmgmt.whois.vo.TbWhoisVo;
import com.kt.ipms.legacy.opermgmt.whoismgmt.vo.TbWhoisKeywordVo;
import com.kt.ipms.legacy.opermgmt.whoismgmt.vo.TbWhoisModifyVo;

public interface TbWhoisModifyDao {

	public List<TbWhoisModifyVo> selectListPageTbWhoisModifyVo(TbWhoisModifyVo tbWhoisModifyVo);
	
	public List<TbWhoisModifyVo> selectListTbWhoisModReqIp(TbWhoisModifyVo tbWhoisModifyVo);

	public int countListPageTbWhoisModReqIp(TbWhoisModifyVo tbWhoisModifyVo);
	
	public int countListTbWhoisModifyVo(TbWhoisModifyVo tbWhoisModifyVo);
	
	public int countTbWhoisModifyVo(TbWhoisModifyVo tbWhoisModifyVo);
	
	public int insertTbWhoisModifyVo(TbWhoisModifyVo tbWhoisModifyVo);
	
	public TbWhoisModifyVo selectTbWhoisModifyVo(TbWhoisModifyVo tbWhoisModifyVo);
	
	public int updateWhoisModReqVo(TbWhoisModifyVo tbWhoisModifyVo);
	
	public int deleteWhoisModReqVo(TbWhoisModifyVo tbWhoisModifyVo);
	
	public int updateWhoisModReqAppr(TbWhoisModifyVo tbWhoisModifyVo);
	
	public TbWhoisVo selectNetTbWhoisVo(TbWhoisModifyVo tbWhoisModifyVo);
	
}
