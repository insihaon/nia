package com.kt.ipms.legacy.opermgmt.orgmgmt.vo;
import java.io.Serializable;
import java.util.List;
import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class TbLvlRoleListVo extends CommonVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = 5854977164341887458L;
	
	private List<TbLvlRoleVo> tbLvlRoleVos;
	
	private int totalCount;
	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/
	public List<TbLvlRoleVo> getTbLvlRoleVos() {
		return this.tbLvlRoleVos;
	}
	
	public void setTbLvlRoleVos(List<TbLvlRoleVo> tbLvlRoleVos) {
		this.tbLvlRoleVos = tbLvlRoleVos;
	}
	
	public int getTotalCount() {
		return this.totalCount;
	}
	
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	/** MEMBER METHOD DECLARATION END **/
}