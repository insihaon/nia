package com.kt.ipms.legacy.opermgmt.tacsmgmt.vo;
import java.io.Serializable;
import java.util.List;
import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class TbTacsConnHistListVo extends CommonVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = -7530629724606674240L;
	
	private List<TbTacsConnHistVo> tbTacsConnHistVos;
	
	private int totalCount;
	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/
	public List<TbTacsConnHistVo> getTbTacsConnHistVos() {
		return this.tbTacsConnHistVos;
	}
	
	public void setTbTacsConnHistVos(List<TbTacsConnHistVo> tbTacsConnHistVos) {
		this.tbTacsConnHistVos = tbTacsConnHistVos;
	}
	
	public int getTotalCount() {
		return this.totalCount;
	}
	
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	/** MEMBER METHOD DECLARATION END **/
}