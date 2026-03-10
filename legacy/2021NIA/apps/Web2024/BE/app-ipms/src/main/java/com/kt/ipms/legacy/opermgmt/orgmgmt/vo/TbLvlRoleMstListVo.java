package com.kt.ipms.legacy.opermgmt.orgmgmt.vo;
import java.io.Serializable;
import java.util.List;
import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class TbLvlRoleMstListVo extends CommonVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = -8946979754549558713L;
	
	private List<TbLvlRoleMstVo> tbLvlRoleMstVos;
	
	private int totalCount;
	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/
	public List<TbLvlRoleMstVo> getTbLvlRoleMstVos() {
		return this.tbLvlRoleMstVos;
	}
	
	public void setTbLvlRoleMstVos(List<TbLvlRoleMstVo> tbLvlRoleMstVos) {
		this.tbLvlRoleMstVos = tbLvlRoleMstVos;
	}
	
	public int getTotalCount() {
		return this.totalCount;
	}
	
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	/** MEMBER METHOD DECLARATION END **/
}