package com.kt.ipms.legacy.opermgmt.whoismgmt.vo;
import java.io.Serializable;
import java.util.List;
import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class TbWhoisKeywordListVo extends CommonVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = -6563566417756009668L;
	
	private List<TbWhoisKeywordVo> tbWhoisKeywordVos;
	
	private int totalCount;
	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/
	public List<TbWhoisKeywordVo> getTbWhoisKeywordVos() {
		return this.tbWhoisKeywordVos;
	}
	
	public void setTbWhoisKeywordVos(List<TbWhoisKeywordVo> tbWhoisKeywordVos) {
		this.tbWhoisKeywordVos = tbWhoisKeywordVos;
	}
	
	public int getTotalCount() {
		return this.totalCount;
	}
	
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	/** MEMBER METHOD DECLARATION END **/
}