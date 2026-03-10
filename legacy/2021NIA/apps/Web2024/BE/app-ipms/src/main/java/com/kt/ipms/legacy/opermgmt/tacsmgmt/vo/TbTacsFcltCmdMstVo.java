package com.kt.ipms.legacy.opermgmt.tacsmgmt.vo;
import java.io.Serializable;
import com.kt.ipms.legacy.cmn.vo.CommonVo;
import java.math.BigInteger;


public class TbTacsFcltCmdMstVo extends CommonVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = 4060739670536635753L;

	private BigInteger ntacsFcltCmdMstSeq;

	private String sfcltType;

	private String sfcltCmd;
	
	private String sparseContent;
	
	private String savailYn;

	private Integer npriority;

	private String suseYn;

	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/

	public void setNtacsFcltCmdMstSeq(BigInteger ntacsFcltCmdMstSeq) {
		this.ntacsFcltCmdMstSeq = ntacsFcltCmdMstSeq;
	}

	public BigInteger getNtacsFcltCmdMstSeq() {
		return ntacsFcltCmdMstSeq;
	}

	public void setSfcltType(String sfcltType) {
		this.sfcltType = sfcltType;
	}

	public String getSfcltType() {
		return sfcltType;
	}

	public void setSfcltCmd(String sfcltCmd) {
		this.sfcltCmd = sfcltCmd;
	}

	public String getSfcltCmd() {
		return sfcltCmd;
	}

	public String getSparseContent() {
		return sparseContent;
	}

	public void setSparseContent(String sparseContent) {
		this.sparseContent = sparseContent;
	}

	public String getSavailYn() {
		return savailYn;
	}

	public void setSavailYn(String savailYn) {
		this.savailYn = savailYn;
	}

	public void setNpriority(Integer npriority) {
		this.npriority = npriority;
	}

	public Integer getNpriority() {
		return npriority;
	}

	public void setSuseYn(String suseYn) {
		this.suseYn = suseYn;
	}

	public String getSuseYn() {
		return suseYn;
	}

	/** MEMBER METHOD DECLARATION END **/
}