package com.kt.ipms.legacy.ipmgmt.hostmgmt.dao;
import java.util.List;

import com.kt.ipms.legacy.cmn.vo.CommonCodeVo;
import com.kt.ipms.legacy.ipmgmt.hostmgmt.vo.TbIpHostMstVo;


/** TB_IP_HOST_MST DAO INTERFACE **/
public interface TbIpHostMstDao {
	
	/** TB_IP_HOST_MST insert **/
	public int insertTbIpHostMstVo(TbIpHostMstVo tbIpHostMstVo);

	/** TB_IP_HOST_MST update **/
	public int updateTbIpHostMstVo(TbIpHostMstVo tbIpHostMstVo);

	/** TB_IP_HOST_MST delete **/
	public int deleteTbIpHostMstVo(TbIpHostMstVo tbIpHostMstVo);

	/** TB_IP_HOST_MST select **/
	public TbIpHostMstVo selectTbIpHostMstVo(TbIpHostMstVo tbIpHostMstVo);

	/** TB_IP_HOST_MST selectList **/
	public List<TbIpHostMstVo> selectListTbIpHostMstVo(TbIpHostMstVo tbIpHostMstVo);
	
	/** TB_IP_HOST_MST selectListPage **/
	public List<TbIpHostMstVo> selectListPageTbIpHostMstVo(TbIpHostMstVo tbIpHostMstVo);

	/** TB_IP_HOST_MST countListPage **/
	public int countListPageTbIpHostMstVo(TbIpHostMstVo tbIpHostMstVo);
	
	/** TB_IP_HOST_MST selectListPage **/
	public List<TbIpHostMstVo> selectListPageVirtualIpHostMstVo(TbIpHostMstVo tbIpHostMstVo);

	/** TB_IP_HOST_MST countListPage **/
	public int countListPageVirtualIpHostMstVo(TbIpHostMstVo tbIpHostMstVo);
	
	/** TB_IP_HOST_MST select **/
	
	/***
	 *  이정표  :    Main 검색에서 사용할  호스트  조회 
	 * @param tbIpHostMstVo
	 * @return
	 * @throws Exception
	 */
	public List<TbIpHostMstVo> selectTbIpHostInfoVo(TbIpHostMstVo tbIpHostMstVo);
	
	public List<TbIpHostMstVo> selectOfficeList(TbIpHostMstVo tbIpHostMstVo);
	
	public List<CommonCodeVo> selectLoadOfficeList(TbIpHostMstVo tbIpHostMstVo);

	
}