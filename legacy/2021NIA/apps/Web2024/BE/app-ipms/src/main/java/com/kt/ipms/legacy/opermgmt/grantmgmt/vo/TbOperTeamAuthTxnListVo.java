package com.kt.ipms.legacy.opermgmt.grantmgmt.vo;
import java.io.Serializable;
import java.util.List;
import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class TbOperTeamAuthTxnListVo extends CommonVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = -413131639694539216L;
	
	private List<TbOperTeamAuthTxnVo> tbOperTeamAuthTxnVos;
	
	private int totalCount;
	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/
	public List<TbOperTeamAuthTxnVo> getTbOperTeamAuthTxnVos() {
		return this.tbOperTeamAuthTxnVos;
	}
	
	public void setTbOperTeamAuthTxnVos(List<TbOperTeamAuthTxnVo> tbOperTeamAuthTxnVos) {
		this.tbOperTeamAuthTxnVos = tbOperTeamAuthTxnVos;
	}
	
	public int getTotalCount() {
		return this.totalCount;
	}
	
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	/** MEMBER METHOD DECLARATION END **/
}