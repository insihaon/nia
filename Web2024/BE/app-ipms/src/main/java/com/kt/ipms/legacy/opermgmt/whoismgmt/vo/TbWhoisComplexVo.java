package com.kt.ipms.legacy.opermgmt.whoismgmt.vo;

import java.io.Serializable;

import com.kt.ipms.legacy.cmn.vo.CommonVo;
import com.kt.ipms.legacy.linkmgmt.whois.model.WhoisInfoObj;

public class TbWhoisComplexVo extends CommonVo implements Serializable {

	private static final long serialVersionUID = -1328096141148980150L;
	
	private TbWhoisVo tbWhoisVo;
	
	private TbWhoisUserVo tbWhoisUserVo;
	

	public TbWhoisVo getTbWhoisVo() {
		return tbWhoisVo;
	}

	public void setTbWhoisVo(TbWhoisVo tbWhoisVo) {
		this.tbWhoisVo = tbWhoisVo;
	}

	public TbWhoisUserVo getTbWhoisUserVo() {
		return tbWhoisUserVo;
	}

	public void setTbWhoisUserVo(TbWhoisUserVo tbWhoisUserVo) {
		this.tbWhoisUserVo = tbWhoisUserVo;
	}
	
	public boolean isNullTbWhoisVo() {
		return tbWhoisVo == null;
	}
	
	public boolean isNullTbWhoisUserVo() {
		return tbWhoisUserVo == null;
	}

}
