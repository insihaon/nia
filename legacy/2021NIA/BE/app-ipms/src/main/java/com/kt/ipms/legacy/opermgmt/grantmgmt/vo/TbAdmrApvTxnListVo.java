package com.kt.ipms.legacy.opermgmt.grantmgmt.vo;
import java.io.Serializable;
import java.util.List;
import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class TbAdmrApvTxnListVo extends CommonVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = -7567307830991696130L;
	
	private List<TbAdmrApvTxnVo> tbAdmrApvTxnVos;
	
	private int totalCount;
	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/
	public List<TbAdmrApvTxnVo> getTbAdmrApvTxnVos() {
		return this.tbAdmrApvTxnVos;
	}
	
	public void setTbAdmrApvTxnVos(List<TbAdmrApvTxnVo> tbAdmrApvTxnVos) {
		this.tbAdmrApvTxnVos = tbAdmrApvTxnVos;
	}
	
	public int getTotalCount() {
		return this.totalCount;
	}
	
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	/** MEMBER METHOD DECLARATION END **/
}