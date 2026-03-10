package com.kt.ipms.legacy.opermgmt.nonktipmgmt.dao;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.kt.ipms.legacy.cmn.vo.CommonCodeVo;
import com.kt.ipms.legacy.opermgmt.nonktipmgmt.vo.TbNonKtSvcMstVo;


/** TB_NON_KT_SVC_MST DAO INTERFACE **/
@Mapper
public interface TbNonKtSvcMstDao {
	
	/** TB_NON_KT_SVC_MST insertTbNonKtSvcMstVo **/
	public int insertTbNonKtSvcMstVo(TbNonKtSvcMstVo tbNonKtSvcMstVo);

	/** TB_NON_KT_SVC_MST updateTbNonKtSvcMstVo **/
	public int updateTbNonKtSvcMstVo(TbNonKtSvcMstVo tbNonKtSvcMstVo);

	/** TB_NON_KT_SVC_MST deleteTbNonKtSvcMstVo **/
	public int deleteTbNonKtSvcMstVo(TbNonKtSvcMstVo tbNonKtSvcMstVo);

	/** TB_NON_KT_SVC_MST selectTbNonKtSvcMstVo **/
	public TbNonKtSvcMstVo selectTbNonKtSvcMstVo(TbNonKtSvcMstVo tbNonKtSvcMstVo);

	/** TB_NON_KT_SVC_MST selectListTbNonKtSvcMstVo **/
	public List<TbNonKtSvcMstVo> selectListTbNonKtSvcMstVo(TbNonKtSvcMstVo tbNonKtSvcMstVo);
	
	/** TB_NON_KT_SVC_MST countListTbNonKtSvcMstVo **/
	public int countListTbNonKtSvcMstVo(TbNonKtSvcMstVo tbNonKtSvcMstVo);
	
	/** TB_NON_KT_SVC_MST selectListPageTbNonKtSvcMstVo **/
	public List<TbNonKtSvcMstVo> selectListPageTbNonKtSvcMstVo(TbNonKtSvcMstVo tbNonKtSvcMstVo);

	/** TB_NON_KT_SVC_MST countListPageTbNonKtSvcMstVo **/
	public int countListPageTbNonKtSvcMstVo(TbNonKtSvcMstVo tbNonKtSvcMstVo);
	
	/** TB_NON_KT_SVC_MST selectIcisOfficesCodeList **/
	public List<CommonCodeVo> selectIcisOfficesCodeList(TbNonKtSvcMstVo tbNonKtSvcMstVo);
	
	/** TB_NON_KT_SVC_MST selectsOfficeCodeList **/
	public List<CommonCodeVo> selectsOfficeCodeList(TbNonKtSvcMstVo tbNonKtSvcMstVo);
	
}