package com.kt.ipms.legacy.linkmgmt.common.dao;
import java.util.List;
import com.kt.ipms.legacy.linkmgmt.common.vo.TbLnkSvcBasVo;
import com.kt.ipms.legacy.linkmgmt.common.vo.TbWsSvcBasVo;
import com.kt.ipms.legacy.linkmgmt.common.vo.TbBatchSvcBasVo;


/** TB_LNK_SVC_BAS DAO INTERFACE **/
public interface TbLnkSvcBasDao {
	
	public List<TbBatchSvcBasVo> selectListBatchConf();
	
	public TbLnkSvcBasVo getWSURIConfig(TbLnkSvcBasVo tbLnkSvcBasVo);
	
	/** TB_LNK_SVC_BAS insert **/
	public int insertTbLnkSvcBasVo(TbLnkSvcBasVo tbLnkSvcBasVo);

	/** TB_LNK_SVC_BAS update **/
	public int updateTbLnkSvcBasVo(TbLnkSvcBasVo tbLnkSvcBasVo);

	/** TB_LNK_SVC_BAS delete **/
	public int deleteTbLnkSvcBasVo(TbLnkSvcBasVo tbLnkSvcBasVo);

	/** TB_LNK_SVC_BAS select **/
	public TbLnkSvcBasVo selectTbLnkSvcBasVo(TbLnkSvcBasVo tbLnkSvcBasVo);

	/** TB_LNK_SVC_BAS selectList **/
	public List<TbLnkSvcBasVo> selectListTbLnkSvcBasVo(TbLnkSvcBasVo tbLnkSvcBasVo);
	
	/** TB_LNK_SVC_BAS countListPage **/
	public int countListPageTbLnkSvcBasVo(TbLnkSvcBasVo tbLnkSvcBasVo);
	

	
	/** TB_WS_SVC_BAS insert **/
	public int insertTbWsSvcBasVo(TbWsSvcBasVo tbWsSvcBasVo);

	/** TB_WS_SVC_BAS update **/
	public int updateTbWsSvcBasVo(TbWsSvcBasVo tbWsSvcBasVo);

	/** TB_WS_SVC_BAS delete **/
	public int deleteTbWsSvcBasVo(TbWsSvcBasVo tbWsSvcBasVo);

	/** TB_WS_SVC_BAS select **/
	public TbWsSvcBasVo selectTbWsSvcBasVo(TbWsSvcBasVo tbWsSvcBasVo);

	/** TB_WS_SVC_BAS selectList **/
	public List<TbWsSvcBasVo> selectListTbWsSvcBasVo(TbWsSvcBasVo tbWsSvcBasVo);
	
	/** TB_WS_SVC_BAS countListPage **/
	public int countListPageTbWsSvcBasVo(TbWsSvcBasVo tbWsSvcBasVo);
	
	/** TB_BATCH_SVC_BAS insert **/
	public int insertTbBatchSvcBasVo(TbBatchSvcBasVo tbBatchSvcBasVo);

	/** TB_BATCH_SVC_BAS update **/
	public int updateTbBatchSvcBasVo(TbBatchSvcBasVo tbBatchSvcBasVo);

	/** TB_BATCH_SVC_BAS delete **/
	public int deleteTbBatchSvcBasVo(TbBatchSvcBasVo tbBatchSvcBasVo);

	/** TB_BATCH_SVC_BAS select **/
	public TbBatchSvcBasVo selectTbBatchSvcBasVo(TbBatchSvcBasVo tbBatchSvcBasVo);

	/** TB_BATCH_SVC_BAS selectList **/
	public List<TbBatchSvcBasVo> selectListTbBatchSvcBasVo(TbBatchSvcBasVo tbBatchSvcBasVo);
	
	/** TB_BATCH_SVC_BAS selectListPage **/
	public List<TbBatchSvcBasVo> selectListPageTbBatchSvcBasVo(TbBatchSvcBasVo tbBatchSvcBasVo);

	/** TB_BATCH_SVC_BAS countListPage **/
	public int countListPageTbBatchSvcBasVo(TbBatchSvcBasVo tbBatchSvcBasVo);

	
}