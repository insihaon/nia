package com.nia.engine.vo;

import java.io.Serializable;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "prototype")
public class DomainCodeVo implements Serializable{
	private static final long serialVersionUID = 2319776561744516086L;

	private String domainGb;
	private String domainId;
	private String remark;

	public String getDomainGb() {
		return domainGb;
	}
	public void setDomainGb(String domainGb) {
		this.domainGb = domainGb;
	}
	public String getDomainId() {
		return domainId;
	}
	public void setDomainId(String domainId) {
		this.domainId = domainId;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	@Override
	public String toString() {

		return "DomainCodeVo [domainGb=" + domainGb + ", domainId=" + domainId + ", remark=" + remark + "]";
	}
}