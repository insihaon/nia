package com.kt.ipms.legacy.opermgmt.srvmgmt.vo;
import java.io.Serializable;
import java.util.List;
import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class TbIpmsSvcMstListVo extends CommonVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = -7707159516725032112L;
	
	private List<TbIpmsSvcMstVo> tbIpmsSvcMstVos;
	
	private int totalCount;
	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/
	public List<TbIpmsSvcMstVo> getTbIpmsSvcMstVos() {
		return this.tbIpmsSvcMstVos;
	}
	
	public void setTbIpmsSvcMstVos(List<TbIpmsSvcMstVo> tbIpmsSvcMstVos) {
		this.tbIpmsSvcMstVos = tbIpmsSvcMstVos;
	}
	
	public int getTotalCount() {
		return this.totalCount;
	}
	
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	/** MEMBER METHOD DECLARATION END **/
}