package com.kt.ipms.legacy.opermgmt.tacsmgmt.vo;

import java.io.Serializable;

public class TacsRequestCmdVo implements Serializable {

	private static final long serialVersionUID = -8169640985478125301L;
	
	private String cmd;
	
	private String parse;
	
	private String availYn;
	
	private String flag;

	public String getCmd() {
		return cmd;
	}

	public void setCmd(String cmd) {
		this.cmd = cmd;
	}

	public String getParse() {
		return parse;
	}

	public void setParse(String parse) {
		this.parse = parse;
	}

	public String getAvailYn() {
		return availYn;
	}

	public void setAvailYn(String availYn) {
		this.availYn = availYn;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

}
