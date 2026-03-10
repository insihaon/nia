package com.kt.ipms.legacy.opermgmt.operstdmgmt.vo;
import java.io.Serializable;
import com.kt.ipms.legacy.cmn.vo.CommonVo;
import java.math.BigInteger;


public class TbCmnMsgMstVo extends CommonVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = 2965799341286311443L;

	private String msgTypeCd;

	private BigInteger msgNum;

	private String msgLvlCd;

	private String msgCd;

	private String msgDesc;

	private String msgRemark;

	private String msgUseYn;

	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/

	public void setMsgTypeCd(String msgTypeCd) {
		this.msgTypeCd = msgTypeCd;
	}

	public String getMsgTypeCd() {
		return msgTypeCd;
	}

	public void setMsgNum(BigInteger msgNum) {
		this.msgNum = msgNum;
	}

	public BigInteger getMsgNum() {
		return msgNum;
	}

	public void setMsgLvlCd(String msgLvlCd) {
		this.msgLvlCd = msgLvlCd;
	}

	public String getMsgLvlCd() {
		return msgLvlCd;
	}

	public void setMsgCd(String msgCd) {
		this.msgCd = msgCd;
	}

	public String getMsgCd() {
		return msgCd;
	}

	public void setMsgDesc(String msgDesc) {
		this.msgDesc = msgDesc;
	}

	public String getMsgDesc() {
		return msgDesc;
	}

	public void setMsgRemark(String msgRemark) {
		this.msgRemark = msgRemark;
	}

	public String getMsgRemark() {
		return msgRemark;
	}

	public void setMsgUseYn(String msgUseYn) {
		this.msgUseYn = msgUseYn;
	}

	public String getMsgUseYn() {
		return msgUseYn;
	}

	/** MEMBER METHOD DECLARATION END **/
}