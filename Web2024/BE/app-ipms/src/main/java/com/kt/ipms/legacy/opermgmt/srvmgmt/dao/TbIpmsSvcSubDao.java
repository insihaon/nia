package com.kt.ipms.legacy.opermgmt.srvmgmt.dao;
import java.util.List;
import com.kt.ipms.legacy.opermgmt.srvmgmt.vo.TbIpmsSvcSubVo;


/** TB_IPMS_SVC_SUB DAO INTERFACE **/
public interface TbIpmsSvcSubDao {
	
	/** TB_IPMS_SVC_SUB insertTbIpmsSvcSubVo **/
	public int insertTbIpmsSvcSubVo(TbIpmsSvcSubVo tbIpmsSvcSubVo);

	/** TB_IPMS_SVC_SUB updateTbIpmsSvcSubVo **/
	public int updateTbIpmsSvcSubVo(TbIpmsSvcSubVo tbIpmsSvcSubVo);

	/** TB_IPMS_SVC_SUB deleteTbIpmsSvcSubVo **/
	public int deleteTbIpmsSvcSubVo(TbIpmsSvcSubVo tbIpmsSvcSubVo);

	/** TB_IPMS_SVC_SUB selectTbIpmsSvcSubVo **/
	public TbIpmsSvcSubVo selectTbIpmsSvcSubVo(TbIpmsSvcSubVo tbIpmsSvcSubVo);

	/** TB_IPMS_SVC_SUB selectListTbIpmsSvcSubVo **/
	public List<TbIpmsSvcSubVo> selectListTbIpmsSvcSubVo(TbIpmsSvcSubVo tbIpmsSvcSubVo);
	
	/** TB_IPMS_SVC_SUB countListTbIpmsSvcSubVo **/
	public int countListTbIpmsSvcSubVo(TbIpmsSvcSubVo tbIpmsSvcSubVo);
	
	/** TB_IPMS_SVC_SUB selectListPageTbIpmsSvcSubVo **/
	public List<TbIpmsSvcSubVo> selectListPageTbIpmsSvcSubVo(TbIpmsSvcSubVo tbIpmsSvcSubVo);

	/** TB_IPMS_SVC_SUB countListPageTbIpmsSvcSubVo **/
	public int countListPageTbIpmsSvcSubVo(TbIpmsSvcSubVo tbIpmsSvcSubVo);

	
}