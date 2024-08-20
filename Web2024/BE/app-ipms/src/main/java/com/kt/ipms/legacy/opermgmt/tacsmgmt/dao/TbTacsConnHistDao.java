package com.kt.ipms.legacy.opermgmt.tacsmgmt.dao;
import java.util.List;
import com.kt.ipms.legacy.opermgmt.tacsmgmt.vo.TbTacsConnHistVo;


/** TB_TACS_CONN_HIST DAO INTERFACE **/
public interface TbTacsConnHistDao {
	
	/** TB_TACS_CONN_HIST insertTbTacsConnHistVo **/
	public int insertTbTacsConnHistVo(TbTacsConnHistVo tbTacsConnHistVo);

	/** TB_TACS_CONN_HIST updateTbTacsConnHistVo **/
	public int updateTbTacsConnHistVo(TbTacsConnHistVo tbTacsConnHistVo);

	/** TB_TACS_CONN_HIST deleteTbTacsConnHistVo **/
	public int deleteTbTacsConnHistVo(TbTacsConnHistVo tbTacsConnHistVo);

	/** TB_TACS_CONN_HIST selectTbTacsConnHistVo **/
	public TbTacsConnHistVo selectTbTacsConnHistVo(TbTacsConnHistVo tbTacsConnHistVo);

	/** TB_TACS_CONN_HIST selectListTbTacsConnHistVo **/
	public List<TbTacsConnHistVo> selectListTbTacsConnHistVo(TbTacsConnHistVo tbTacsConnHistVo);
	
	/** TB_TACS_CONN_HIST countListTbTacsConnHistVo **/
	public int countListTbTacsConnHistVo(TbTacsConnHistVo tbTacsConnHistVo);
	
	/** TB_TACS_CONN_HIST selectListPageTbTacsConnHistVo **/
	public List<TbTacsConnHistVo> selectListPageTbTacsConnHistVo(TbTacsConnHistVo tbTacsConnHistVo);

	/** TB_TACS_CONN_HIST countListPageTbTacsConnHistVo **/
	public int countListPageTbTacsConnHistVo(TbTacsConnHistVo tbTacsConnHistVo);
	
	public List<?> selectSresultMsg();
	
}