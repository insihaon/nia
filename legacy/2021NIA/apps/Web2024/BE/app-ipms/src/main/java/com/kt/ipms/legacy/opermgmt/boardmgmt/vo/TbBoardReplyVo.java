package com.kt.ipms.legacy.opermgmt.boardmgmt.vo;

import java.io.Serializable;
import java.math.BigInteger;

import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class TbBoardReplyVo extends CommonVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = 2494101614180214832L;
	
	private BigInteger seq;
	
	private BigInteger replySeq;
	
	private String replyContents;
	
	private String sDcreate;
	
	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/
	
	public BigInteger getSeq() {
		return seq;
	}
	
	public void setSeq(BigInteger seq) {
		this.seq = seq;
	}

	public BigInteger getReplySeq() {
		return replySeq;
	}

	public void setReplySeq(BigInteger replySeq) {
		this.replySeq = replySeq;
	}

	public String getReplyContents() {
		return replyContents;
	}

	public void setReplyContents(String replyContents) {
		this.replyContents = replyContents;
	}

	public String getsDcreate() {
		return sDcreate;
	}

	public void setsDcreate(String sDcreate) {
		this.sDcreate = sDcreate;
	}
	
	
	
}
