package com.kt.ipms.legacy.opermgmt.usermgmt.vo;
import java.io.Serializable;
import java.util.List;
import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class TbUserBasListVo extends CommonVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = -2687234060385828175L;
	
	private List<TbUserBasVo> tbUserBasVos;
	
	private int totalCount;
	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/
	public List<TbUserBasVo> getTbUserBasVos() {
		return this.tbUserBasVos;
	}
	
	public void setTbUserBasVos(List<TbUserBasVo> tbUserBasVos) {
		this.tbUserBasVos = tbUserBasVos;
	}
	
	public int getTotalCount() {
		return this.totalCount;
	}
	
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	/** MEMBER METHOD DECLARATION END **/
}