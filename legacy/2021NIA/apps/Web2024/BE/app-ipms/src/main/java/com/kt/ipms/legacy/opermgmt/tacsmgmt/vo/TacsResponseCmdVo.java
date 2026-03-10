package com.kt.ipms.legacy.opermgmt.tacsmgmt.vo;

import java.io.Serializable;

public class TacsResponseCmdVo implements Serializable {

	private static final long serialVersionUID = 6224759895653808957L;
	
	private String cmd;
	
	private String result;
	
	private String flag;

	public String getCmd() {
		return cmd;
	}

	public void setCmd(String cmd) {
		this.cmd = cmd;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

}
