package com.kt.ipms.legacy.opermgmt.menumgmt.vo;
import java.io.Serializable;
import java.util.List;
import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class TbMenuAuthListVo extends CommonVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = 6747397836516312040L;
	
	private List<TbMenuAuthVo> tbMenuAuthVos;
	
	private int totalCount;
	private String menuAutUseYn;
	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/
	public List<TbMenuAuthVo> getTbMenuAuthVos() {
		return this.tbMenuAuthVos;
	}
	
	public void setTbMenuAuthVos(List<TbMenuAuthVo> tbMenuAuthVos) {
		this.tbMenuAuthVos = tbMenuAuthVos;
	}
	
	public int getTotalCount() {
		return this.totalCount;
	}
	
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public String getMenuAutUseYn() {
		return menuAutUseYn;
	}

	public void setMenuAutUseYn(String menuAutUseYn) {
		this.menuAutUseYn = menuAutUseYn;
	}
	
	/** MEMBER METHOD DECLARATION END **/
}