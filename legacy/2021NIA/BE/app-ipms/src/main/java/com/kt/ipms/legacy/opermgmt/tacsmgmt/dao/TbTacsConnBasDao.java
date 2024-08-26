package com.kt.ipms.legacy.opermgmt.tacsmgmt.dao;

import org.apache.ibatis.annotations.Mapper;

import com.kt.ipms.legacy.opermgmt.tacsmgmt.vo.TbTacsConnBasVo;


/** TB_TACS_CONN_BAS DAO INTERFACE **/
@Mapper
public interface TbTacsConnBasDao {
	
	/** TB_TACS_CONN_BAS selectTbTacsConnBasVo **/
	public TbTacsConnBasVo selectTbTacsConnBasVo();
	
	/** TB_TACS_CONN_BAS updateTbTacsConnBasVo **/
	public int updateTbTacsConnBasVo(TbTacsConnBasVo tbTacsConnBasVo);

}
