package com.kt.ipms.legacy.opermgmt.usermgmt.vo;
import java.io.Serializable;
import java.util.List;
import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class TbUserHndsetApyTxnListVo extends CommonVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = 8984038403576382343L;
	
	private List<TbUserHndsetApyTxnVo> tbUserHndsetApyTxnVos;
	
	private int totalCount;
	
	private String sComment;
	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/
	public List<TbUserHndsetApyTxnVo> getTbUserHndsetApyTxnVos() {
		return this.tbUserHndsetApyTxnVos;
	}
	
	public void setTbUserHndsetApyTxnVos(List<TbUserHndsetApyTxnVo> tbUserHndsetApyTxnVos) {
		this.tbUserHndsetApyTxnVos = tbUserHndsetApyTxnVos;
	}
	
	public int getTotalCount() {
		return this.totalCount;
	}
	
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public String getsComment() {
		return sComment;
	}

	public void setsComment(String sComment) {
		this.sComment = sComment;
	}
	
	
	/** MEMBER METHOD DECLARATION END **/
}