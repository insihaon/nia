package com.kt.ipms.legacy.opermgmt.privatemgmt.vo;

import java.io.Serializable;
import java.util.List;

import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class TbIpPrivateReqMstComplexVo extends CommonVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6197322746283179651L;
	
	private TbIpPrivateReqMstVo srcIpPrivateReqMstVo;
	
	private List<TbIpPrivateReqMstVo> destIpPrivateReqMstVo;

	public TbIpPrivateReqMstVo getSrcIpPrivateReqMstVo() {
		return srcIpPrivateReqMstVo;
	}

	public void setSrcIpPrivateReqMstVo(TbIpPrivateReqMstVo srcIpPrivateReqMstVo) {
		this.srcIpPrivateReqMstVo = srcIpPrivateReqMstVo;
	}

	public List<TbIpPrivateReqMstVo> getDestIpPrivateReqMstVo() {
		return destIpPrivateReqMstVo;
	}

	public void setDestIpPrivateReqMstVo(List<TbIpPrivateReqMstVo> destIpPrivateReqMstVo) {
		this.destIpPrivateReqMstVo = destIpPrivateReqMstVo;
	}
	
	

}
