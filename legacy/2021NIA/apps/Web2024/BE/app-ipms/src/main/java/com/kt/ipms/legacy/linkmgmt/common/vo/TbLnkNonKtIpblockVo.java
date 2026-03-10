package com.kt.ipms.legacy.linkmgmt.common.vo;

import java.io.Serializable;

import com.kt.ipms.legacy.opermgmt.nonktipmgmt.vo.TbNonKtIpblockVo;

public class TbLnkNonKtIpblockVo extends TbNonKtIpblockVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -9008748006593653384L;
	
	/* User Define Start*/
	private String sFlag;
	
	private String sComment;

	public String getsFlag() {
		return sFlag;
	}

	public void setsFlag(String sFlag) {
		this.sFlag = sFlag;
	}

	public String getsComment() {
		return sComment;
	}

	public void setsComment(String sComment) {
		this.sComment = sComment;
	}
	
	/* User Define End*/
	
	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/
	
	/* User Define Start*/
	
	/* User Define END*/
	
	/** MEMBER METHOD DECLARATION END **/

}
