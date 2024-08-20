package com.kt.ipms.legacy.cmn.vo;

import java.io.Serializable;

public class FileVo extends CommonVo implements Serializable {

	private static final long serialVersionUID = 3797520129749420324L;
	
	private String fileName;
	
	private String filePath;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

}
