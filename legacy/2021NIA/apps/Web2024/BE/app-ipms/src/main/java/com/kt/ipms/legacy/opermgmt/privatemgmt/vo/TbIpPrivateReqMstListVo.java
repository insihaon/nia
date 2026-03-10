package com.kt.ipms.legacy.opermgmt.privatemgmt.vo;

import java.io.Serializable;
import java.util.List;


public class TbIpPrivateReqMstListVo extends TbIpPrivateReqMstVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4684543194280628048L;
	
	private List<TbIpPrivateReqMstVo> tbIpPrivateReqMstVos;
	
	private int totalCount;
	
	private String sRequestTypeCd;
	
	private String sRequestTypeNm;
	
	private String sRequestStatCd;

	private String sreject_rsn;

	public List<TbIpPrivateReqMstVo> getTbIpPrivateReqMstVos() {
		return tbIpPrivateReqMstVos;
	}

	public void setTbIpPrivateReqMstVos(List<TbIpPrivateReqMstVo> tbIpPrivateReqMstVos) {
		this.tbIpPrivateReqMstVos = tbIpPrivateReqMstVos;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public String getsRequestStatCd() {
		return sRequestStatCd;
	}

	public void setsRequestStatCd(String sRequestStatCd) {
		this.sRequestStatCd = sRequestStatCd;
	}

	public String getsRequestTypeCd() {
		return sRequestTypeCd;
	}

	public void setsRequestTypeCd(String sRequestTypeCd) {
		this.sRequestTypeCd = sRequestTypeCd;
	}

	public String getsRequestTypeNm() {
		return sRequestTypeNm;
	}

	public void setsRequestTypeNm(String sRequestTypeNm) {
		this.sRequestTypeNm = sRequestTypeNm;
	}

	public String getSreject_rsn() {
		return sreject_rsn;
	}

	public void setSreject_rsn(String sreject_rsn) {
		this.sreject_rsn = sreject_rsn;
	}
}
