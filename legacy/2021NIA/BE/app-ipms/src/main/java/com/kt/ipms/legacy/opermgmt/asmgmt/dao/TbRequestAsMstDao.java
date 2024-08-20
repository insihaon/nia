package com.kt.ipms.legacy.opermgmt.asmgmt.dao;
import java.util.List;
import com.kt.ipms.legacy.opermgmt.asmgmt.vo.TbRequestAsMstVo;


/** TB_REQUEST_AS_MST DAO INTERFACE **/
public interface TbRequestAsMstDao {
	
	/** TB_REQUEST_AS_MST insertTbRequestAsMstVo **/
	public int insertTbRequestAsMstVo(TbRequestAsMstVo tbRequestAsMstVo);

	/** TB_REQUEST_AS_MST updateTbRequestAsMstVo **/
	public int updateTbRequestAsMstVo(TbRequestAsMstVo tbRequestAsMstVo);

	/** TB_REQUEST_AS_MST deleteTbRequestAsMstVo **/
	public int deleteTbRequestAsMstVo(TbRequestAsMstVo tbRequestAsMstVo);

	/** TB_REQUEST_AS_MST selectTbRequestAsMstVo **/
	public TbRequestAsMstVo selectTbRequestAsMstVo(TbRequestAsMstVo tbRequestAsMstVo);

	/** TB_REQUEST_AS_MST selectListTbRequestAsMstVo **/
	public List<TbRequestAsMstVo> selectListTbRequestAsMstVo(TbRequestAsMstVo tbRequestAsMstVo);
	
	/** TB_REQUEST_AS_MST countListTbRequestAsMstVo **/
	public int countListTbRequestAsMstVo(TbRequestAsMstVo tbRequestAsMstVo);
	
	/** TB_REQUEST_AS_MST useCountListTbRequestAsMstVo **/
	public int useCountListTbRequestAsMstVo(TbRequestAsMstVo tbRequestAsMstVo);
	
	/** TB_REQUEST_AS_MST selectListPageTbRequestAsMstVo **/
	public List<TbRequestAsMstVo> selectListPageTbRequestAsMstVo(TbRequestAsMstVo tbRequestAsMstVo);

	/** TB_REQUEST_AS_MST countListPageTbRequestAsMstVo **/
	public int countListPageTbRequestAsMstVo(TbRequestAsMstVo tbRequestAsMstVo);
	
	/** TB_REQUEST_AS_MST selectMinSrequestAsTyCdN **/
	public TbRequestAsMstVo selectMinNrequestAsSeq(TbRequestAsMstVo tbRequestAsMstVo);

	
}