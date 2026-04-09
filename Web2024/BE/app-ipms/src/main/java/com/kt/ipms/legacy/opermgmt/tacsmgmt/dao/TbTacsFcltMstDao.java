package com.kt.ipms.legacy.opermgmt.tacsmgmt.dao;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.kt.ipms.legacy.opermgmt.tacsmgmt.vo.TbTacsFcltMstVo;


/** TB_TACS_FCLT_MST DAO INTERFACE **/
@Mapper
public interface TbTacsFcltMstDao {
	
	/** TB_TACS_FCLT_MST insertTbTacsFcltMstVo **/
	public int insertTbTacsFcltMstVo(TbTacsFcltMstVo tbTacsFcltMstVo);

	/** TB_TACS_FCLT_MST updateTbTacsFcltMstVo **/
	public int updateTbTacsFcltMstVo(TbTacsFcltMstVo tbTacsFcltMstVo);

	/** TB_TACS_FCLT_MST deleteTbTacsFcltMstVo **/
	public int deleteTbTacsFcltMstVo(TbTacsFcltMstVo tbTacsFcltMstVo);

	/** TB_TACS_FCLT_MST selectTbTacsFcltMstVo **/
	public TbTacsFcltMstVo selectTbTacsFcltMstVo(TbTacsFcltMstVo tbTacsFcltMstVo);

	/** TB_TACS_FCLT_MST selectListTbTacsFcltMstVo **/
	public List<TbTacsFcltMstVo> selectListTbTacsFcltMstVo(TbTacsFcltMstVo tbTacsFcltMstVo);
	
	/** TB_TACS_FCLT_MST countListTbTacsFcltMstVo **/
	public int countListTbTacsFcltMstVo(TbTacsFcltMstVo tbTacsFcltMstVo);
	
	/** TB_TACS_FCLT_MST selectListPageTbTacsFcltMstVo **/
	public List<TbTacsFcltMstVo> selectListPageTbTacsFcltMstVo(TbTacsFcltMstVo tbTacsFcltMstVo);

	/** TB_TACS_FCLT_MST countListPageTbTacsFcltMstVo **/
	public int countListPageTbTacsFcltMstVo(TbTacsFcltMstVo tbTacsFcltMstVo);

	
}