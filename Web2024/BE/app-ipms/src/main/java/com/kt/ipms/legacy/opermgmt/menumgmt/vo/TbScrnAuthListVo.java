package com.kt.ipms.legacy.opermgmt.menumgmt.vo;
import java.io.Serializable;
import java.util.List;
import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class TbScrnAuthListVo extends CommonVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = -6999200575760743953L;
	
	private List<TbScrnAuthVo> tbScrnAuthVos;
	
	private int totalCount;
	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/
	public List<TbScrnAuthVo> getTbScrnAuthVos() {
		return this.tbScrnAuthVos;
	}
	
	public void setTbScrnAuthVos(List<TbScrnAuthVo> tbScrnAuthVos) {
		this.tbScrnAuthVos = tbScrnAuthVos;
	}
	
	public int getTotalCount() {
		return this.totalCount;
	}
	
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	/** MEMBER METHOD DECLARATION END **/
}