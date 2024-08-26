package com.kt.ipms.legacy.opermgmt.limitmgmt.vo;
import java.io.Serializable;
import java.util.List;
import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class TbAuditDhcpBasListVo extends CommonVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = -437970642725024548L;
	
	private List<TbAuditDhcpBasVo> tbAuditDhcpBasVos;
	
	private int totalCount;
	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/
	public List<TbAuditDhcpBasVo> getTbAuditDhcpBasVos() {
		return this.tbAuditDhcpBasVos;
	}
	
	public void setTbAuditDhcpBasVos(List<TbAuditDhcpBasVo> tbAuditDhcpBasVos) {
		this.tbAuditDhcpBasVos = tbAuditDhcpBasVos;
	}
	
	public int getTotalCount() {
		return this.totalCount;
	}
	
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	/** MEMBER METHOD DECLARATION END **/
}