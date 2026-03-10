package com.kt.ipms.legacy.opermgmt.tacsmgmt.vo;

import java.io.Serializable;
import java.util.List;

public class TacsResponseVo implements Serializable {

	private static final long serialVersionUID = 7063426409584347202L;
	
	private Integer responseCd;
	
	private String responseMsg;
	
	private String targetIp;
	
	private String targetType;
	
	private String flag;
	
	private List<TacsResponseCmdVo> responseList;
	

	public Integer getResponseCd() {
		return responseCd;
	}

	public void setResponseCd(Integer responseCd) {
		this.responseCd = responseCd;
	}

	public String getResponseMsg() {
		return responseMsg;
	}

	public void setResponseMsg(String responseMsg) {
		this.responseMsg = responseMsg;
	}

	public String getTargetIp() {
		return targetIp;
	}

	public void setTargetIp(String targetIp) {
		this.targetIp = targetIp;
	}

	public String getTargetType() {
		return targetType;
	}

	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public List<TacsResponseCmdVo> getResponseList() {
		return responseList;
	}

	public void setResponseList(List<TacsResponseCmdVo> responseList) {
		this.responseList = responseList;
	}
}
