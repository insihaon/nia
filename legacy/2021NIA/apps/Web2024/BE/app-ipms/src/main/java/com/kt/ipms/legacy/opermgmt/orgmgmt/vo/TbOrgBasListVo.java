package com.kt.ipms.legacy.opermgmt.orgmgmt.vo;
import java.io.Serializable;
import java.util.List;
import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class TbOrgBasListVo extends CommonVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = 7794047901591880287L;
	
	private List<TbOrgBasVo> tbOrgBasVos;
	private String sipmsOrgYn;
	private int totalCount;
	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/
	public List<TbOrgBasVo> getTbOrgBasVos() {
		return this.tbOrgBasVos;
	}
	
	public void setTbOrgBasVos(List<TbOrgBasVo> tbOrgBasVos) {
		this.tbOrgBasVos = tbOrgBasVos;
	}
	
	public String getSipmsOrgYn() {
		return sipmsOrgYn;
	}

	public void setSipmsOrgYn(String sipmsOrgYn) {
		this.sipmsOrgYn = sipmsOrgYn;
	}
	
	public int getTotalCount() {
		return this.totalCount;
	}
	
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	/** MEMBER METHOD DECLARATION END **/

	
}