package com.kt.ipms.legacy.ticketmgmt.configmgmt.vo;
import java.io.Serializable;
import java.util.List;
import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class TbConfigInterfaceMstListVo extends CommonVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = 1267805660502901697L;
	
	private List<TbConfigInterfaceMstVo> tbConfigInterfaceMstVos;
	
	private int totalCount;
	
	private String treeJsonString;
	
	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/
	public List<TbConfigInterfaceMstVo> getTbConfigInterfaceMstVos() {
		return this.tbConfigInterfaceMstVos;
	}
	
	public void setTbConfigInterfaceMstVos(List<TbConfigInterfaceMstVo> tbConfigInterfaceMstVos) {
		this.tbConfigInterfaceMstVos = tbConfigInterfaceMstVos;
	}
	
	public int getTotalCount() {
		return this.totalCount;
	}
	
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public String getTreeJsonString() {
		return treeJsonString;
	}

	public void setTreeJsonString(String treeJsonString) {
		this.treeJsonString = treeJsonString;
	}
	
	/** MEMBER METHOD DECLARATION END **/
}