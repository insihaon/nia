package com.kt.ipms.legacy.opermgmt.asmgmt.vo;
import java.io.Serializable;
import java.util.List;
import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class TbRequestAsMstListVo extends CommonVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = 5320006635327275175L;
	
	private List<TbRequestAsMstVo> tbRequestAsMstVos;
	
	private int totalCount;
	
	private int useCount;
	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/
	public List<TbRequestAsMstVo> getTbRequestAsMstVos() {
		return this.tbRequestAsMstVos;
	}
	
	public void setTbRequestAsMstVos(List<TbRequestAsMstVo> tbRequestAsMstVos) {
		this.tbRequestAsMstVos = tbRequestAsMstVos;
	}
	
	public int getTotalCount() {
		return this.totalCount;
	}
	
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	
	public int getUseCount() {
		return useCount;
	}

	public void setUseCount(int useCount) {
		this.useCount = useCount;
	}
	
	/** MEMBER METHOD DECLARATION END **/
	
}