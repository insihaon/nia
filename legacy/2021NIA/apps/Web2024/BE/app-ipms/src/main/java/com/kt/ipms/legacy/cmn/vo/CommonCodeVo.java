package com.kt.ipms.legacy.cmn.vo;

import java.io.Serializable;

public class CommonCodeVo extends CommonVo implements Serializable {

	private static final long serialVersionUID = 4872545065167788273L;

	private String code;
	private String name;
	private String subCodeStr;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSubCodeStr() {
		return subCodeStr;
	}

	public void setSubCodeStr(String subCodeStr) {
		this.subCodeStr = subCodeStr;
	}

}
