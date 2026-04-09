package com.kt.ipms.legacy.opermgmt.usermgmt.vo;
import java.io.Serializable;
import java.util.List;
import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class TbUserConnHistListVo extends CommonVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = -598778669540687379L;
	
	private List<TbUserConnHistVo> tbUserConnHistVos;
	
	private int totalCount;
	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/
	public List<TbUserConnHistVo> getTbUserConnHistVos() {
		return this.tbUserConnHistVos;
	}
	
	public void setTbUserConnHistVos(List<TbUserConnHistVo> tbUserConnHistVos) {
		this.tbUserConnHistVos = tbUserConnHistVos;
	}
	
	public int getTotalCount() {
		return this.totalCount;
	}
	
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	/** MEMBER METHOD DECLARATION END **/
}