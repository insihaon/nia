package com.kt.ipms.legacy.opermgmt.nonktipmgmt.dao;
import java.util.List;
import com.kt.ipms.legacy.opermgmt.nonktipmgmt.vo.TbNonKtIpblockVo;


/** TB_NON_KT_IPBLOCK DAO INTERFACE **/
public interface TbNonKtIpblockDao {
	
	/** TB_NON_KT_IPBLOCK insertTbNonKtIpblockVo **/
	public int insertTbNonKtIpblockVo(TbNonKtIpblockVo tbNonKtIpblockVo);

	/** TB_NON_KT_IPBLOCK updateTbNonKtIpblockVo **/
	public int updateTbNonKtIpblockVo(TbNonKtIpblockVo tbNonKtIpblockVo);

	/** TB_NON_KT_IPBLOCK deleteTbNonKtIpblockVo **/
	public int deleteTbNonKtIpblockVo(TbNonKtIpblockVo tbNonKtIpblockVo);

	/** TB_NON_KT_IPBLOCK selectTbNonKtIpblockVo **/
	public TbNonKtIpblockVo selectTbNonKtIpblockVo(TbNonKtIpblockVo tbNonKtIpblockVo);

	/** TB_NON_KT_IPBLOCK selectListTbNonKtIpblockVo **/
	public List<TbNonKtIpblockVo> selectListTbNonKtIpblockVo(TbNonKtIpblockVo tbNonKtIpblockVo);
	
	/** TB_NON_KT_IPBLOCK countListTbNonKtIpblockVo **/
	public int countListTbNonKtIpblockVo(TbNonKtIpblockVo tbNonKtIpblockVo);
	
	/** TB_NON_KT_IPBLOCK selectListPageTbNonKtIpblockVo **/
	public List<TbNonKtIpblockVo> selectListPageTbNonKtIpblockVo(TbNonKtIpblockVo tbNonKtIpblockVo);

	/** TB_NON_KT_IPBLOCK countListPageTbNonKtIpblockVo **/
	public int countListPageTbNonKtIpblockVo(TbNonKtIpblockVo tbNonKtIpblockVo);
	
	/** TB_NON_KT_IPBLOCK selectCheckTbNonKtIpblockVo **/
	public TbNonKtIpblockVo selectCheckTbNonKtIpblockVo(TbNonKtIpblockVo tbNonKtIpblockVo);

}