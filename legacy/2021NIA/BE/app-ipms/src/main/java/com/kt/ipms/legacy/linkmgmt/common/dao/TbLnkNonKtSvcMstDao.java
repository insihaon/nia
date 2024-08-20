package com.kt.ipms.legacy.linkmgmt.common.dao;

import java.util.List;

import com.kt.ipms.legacy.linkmgmt.common.vo.TbLnkNonKtIpblockVo;
import com.kt.ipms.legacy.linkmgmt.common.vo.TbLnkNonKtSvcMstVo;

public interface TbLnkNonKtSvcMstDao {
	/** TB_NON_KT_IPBLOCK insertTbLnkNonKtIpblockVo **/
	public int insertTbLnkNonKtIpblockVo(TbLnkNonKtIpblockVo tbLnkNonKtIpblockVo);

	/** TB_NON_KT_IPBLOCK updateTbLnkNonKtIpblockVo **/
	public int updateTbLnkNonKtIpblockVo(TbLnkNonKtIpblockVo tbLnkNonKtIpblockVo);

	/** TB_NON_KT_IPBLOCK deleteTbLnkNonKtIpblockVo **/
	public int deleteTbLnkNonKtIpblockVo(TbLnkNonKtIpblockVo tbLnkNonKtIpblockVo);

	/** TB_NON_KT_IPBLOCK selectTbLnkNonKtIpblockVo **/
	public TbLnkNonKtIpblockVo selectTbLnkNonKtIpblockVo(TbLnkNonKtIpblockVo tbLnkNonKtIpblockVo);

	/** TB_NON_KT_IPBLOCK selectListTbLnkNonKtIpblockVo **/
	public List<TbLnkNonKtIpblockVo> selectListTbLnkNonKtIpblockVo(TbLnkNonKtIpblockVo tbLnkNonKtIpblockVo);
	
	/** TB_NON_KT_IPBLOCK countListTbLnkNonKtIpblockVo **/
	public int countListTbLnkNonKtIpblockVo(TbLnkNonKtIpblockVo tbLnkNonKtIpblockVo);
	
	/** TB_NON_KT_IPBLOCK selectListPageTbLnkNonKtIpblockVo **/
	public List<TbLnkNonKtIpblockVo> selectListPageTbLnkNonKtIpblockVo(TbLnkNonKtIpblockVo tbLnkNonKtIpblockVo);

	/** TB_NON_KT_IPBLOCK countListPageTbLnkNonKtIpblockVo **/
	public int countListPageTbLnkNonKtIpblockVo(TbLnkNonKtIpblockVo tbLnkNonKtIpblockVo);
	
	/** TB_NON_KT_SVC_MST insertTbLnkNonKtSvcMstVo **/
	public int insertTbLnkNonKtSvcMstVo(TbLnkNonKtSvcMstVo tbLnkNonKtSvcMstVo);

	/** TB_NON_KT_SVC_MST updateTbLnkNonKtSvcMstVo **/
	public int updateTbLnkNonKtSvcMstVo(TbLnkNonKtSvcMstVo tbLnkNonKtSvcMstVo);

	/** TB_NON_KT_SVC_MST deleteTbLnkNonKtSvcMstVo **/
	public int deleteTbLnkNonKtSvcMstVo(TbLnkNonKtSvcMstVo tbLnkNonKtSvcMstVo);

	/** TB_NON_KT_SVC_MST selectTbLnkNonKtSvcMstVo **/
	public TbLnkNonKtSvcMstVo selectTbLnkNonKtSvcMstVo(TbLnkNonKtSvcMstVo tbLnkNonKtSvcMstVo);

	/** TB_NON_KT_SVC_MST selectListTbLnkNonKtSvcMstVo **/
	public List<TbLnkNonKtSvcMstVo> selectListTbLnkNonKtSvcMstVo(TbLnkNonKtSvcMstVo tbLnkNonKtSvcMstVo);
	
	/** TB_NON_KT_SVC_MST countListTbLnkNonKtSvcMstVo **/
	public int countListTbLnkNonKtSvcMstVo(TbLnkNonKtSvcMstVo tbLnkNonKtSvcMstVo);
	
	/** TB_NON_KT_SVC_MST selectListPageTbLnkNonKtSvcMstVo **/
	public List<TbLnkNonKtSvcMstVo> selectListPageTbLnkNonKtSvcMstVo(TbLnkNonKtSvcMstVo tbLnkNonKtSvcMstVo);

	/** TB_NON_KT_SVC_MST countListPageTbLnkNonKtSvcMstVo **/
	public int countListPageTbLnkNonKtSvcMstVo(TbLnkNonKtSvcMstVo tbLnkNonKtSvcMstVo);

}
