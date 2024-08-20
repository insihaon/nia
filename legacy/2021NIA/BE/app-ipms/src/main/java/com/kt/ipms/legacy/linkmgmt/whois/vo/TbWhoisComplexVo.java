package com.kt.ipms.legacy.linkmgmt.whois.vo;

import java.io.Serializable;

import com.kt.ipms.legacy.cmn.vo.CommonVo;
import com.kt.ipms.legacy.opermgmt.whoismgmt.vo.TbWhoisUserVo;

public class TbWhoisComplexVo extends CommonVo implements Serializable {

	private static final long serialVersionUID = 7357882924377154721L;

	private TbWhoisVo tbWhoisVo;
	
	private com.kt.ipms.legacy.linkmgmt.whois.vo.TbWhoisUserVo tbWhoisUserVo;
	
	public TbWhoisVo getTbWhoisVo() {
		return tbWhoisVo;
	}

	public void setTbWhoisVo(TbWhoisVo tbWhoisVo) {
		this.tbWhoisVo = tbWhoisVo;
	}

	public com.kt.ipms.legacy.linkmgmt.whois.vo.TbWhoisUserVo getTbWhoisUserVo() {
		return tbWhoisUserVo;
	}

	public void setTbWhoisUserVo(com.kt.ipms.legacy.linkmgmt.whois.vo.TbWhoisUserVo tbWhoisUserVo) {
		this.tbWhoisUserVo = tbWhoisUserVo;
	}

}
