package com.kt.ipms.legacy.opermgmt.uploadmgmt.vo;

import java.math.BigInteger;
import java.util.Date;

import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class TbUploadVo  extends CommonVo{
	private BigInteger seq;

	private String file_name;
	
	private Date regist_date;
	
	private String regist_id;
	
	private String status;
	
	private Date max_date;
	
	
	
	public Date getMax_date() {
		return max_date;
	}

	public void setMax_date(Date max_date) {
		this.max_date = max_date;
	}

	public BigInteger getSeq() {
		return seq;
	}

	public void setSeq(BigInteger seq) {
		this.seq = seq;
	}

	public String getFile_name() {
		return file_name;
	}

	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}

	public Date getRegist_date() {
		return regist_date;
	}

	public void setRegist_date(Date regist_date) {
		this.regist_date = regist_date;
	}

	public String getRegist_id() {
		return regist_id;
	}

	public void setRegist_id(String regist_id) {
		this.regist_id = regist_id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
