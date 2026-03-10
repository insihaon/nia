package com.kt.ipms.legacy.opermgmt.menumgmt.vo;
import java.io.Serializable;
import java.util.List;

import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class TbScrnBasListVo extends CommonVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = 5466043836768537778L;
	
	private List<TbScrnBasVo> tbScrnBasVos;
	
	private int totalCount;
	private String sscrnUseYn;
	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/
	public List<TbScrnBasVo> getTbScrnBasVos() {
		return this.tbScrnBasVos;
	}
	
	public void setTbScrnBasVos(List<TbScrnBasVo> tbScrnBasVos) {
		this.tbScrnBasVos = tbScrnBasVos;
	}
	
	public int getTotalCount() {
		return this.totalCount;
	}
	
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	
	public String getSscrnUseYn() {
		return sscrnUseYn;
	}

	public void setSscrnUseYn(String sscrnUseYn) {
		this.sscrnUseYn = sscrnUseYn;
	}
	/** MEMBER METHOD DECLARATION END **/
}