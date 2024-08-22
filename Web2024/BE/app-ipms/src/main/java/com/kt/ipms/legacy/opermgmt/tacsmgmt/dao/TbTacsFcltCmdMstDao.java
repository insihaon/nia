package com.kt.ipms.legacy.opermgmt.tacsmgmt.dao;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.kt.ipms.legacy.opermgmt.tacsmgmt.vo.TbTacsFcltCmdMstVo;


/** TB_TACS_FCLT_CMD_MST DAO INTERFACE **/
@Mapper
public interface TbTacsFcltCmdMstDao {
	
	/** TB_TACS_FCLT_CMD_MST insertTbTacsFcltCmdMstVo **/
	public int insertTbTacsFcltCmdMstVo(TbTacsFcltCmdMstVo tbTacsFcltCmdMstVo);

	/** TB_TACS_FCLT_CMD_MST updateTbTacsFcltCmdMstVo **/
	public int updateTbTacsFcltCmdMstVo(TbTacsFcltCmdMstVo tbTacsFcltCmdMstVo);

	/** TB_TACS_FCLT_CMD_MST deleteTbTacsFcltCmdMstVo **/
	public int deleteTbTacsFcltCmdMstVo(TbTacsFcltCmdMstVo tbTacsFcltCmdMstVo);

	/** TB_TACS_FCLT_CMD_MST selectTbTacsFcltCmdMstVo **/
	public TbTacsFcltCmdMstVo selectTbTacsFcltCmdMstVo(TbTacsFcltCmdMstVo tbTacsFcltCmdMstVo);

	/** TB_TACS_FCLT_CMD_MST selectListTbTacsFcltCmdMstVo **/
	public List<TbTacsFcltCmdMstVo> selectListTbTacsFcltCmdMstVo(TbTacsFcltCmdMstVo tbTacsFcltCmdMstVo);
	
	/** TB_TACS_FCLT_CMD_MST countListTbTacsFcltCmdMstVo **/
	public int countListTbTacsFcltCmdMstVo(TbTacsFcltCmdMstVo tbTacsFcltCmdMstVo);
	
	/** TB_TACS_FCLT_CMD_MST selectListPageTbTacsFcltCmdMstVo **/
	public List<TbTacsFcltCmdMstVo> selectListPageTbTacsFcltCmdMstVo(TbTacsFcltCmdMstVo tbTacsFcltCmdMstVo);

	/** TB_TACS_FCLT_CMD_MST countListPageTbTacsFcltCmdMstVo **/
	public int countListPageTbTacsFcltCmdMstVo(TbTacsFcltCmdMstVo tbTacsFcltCmdMstVo);

	
}