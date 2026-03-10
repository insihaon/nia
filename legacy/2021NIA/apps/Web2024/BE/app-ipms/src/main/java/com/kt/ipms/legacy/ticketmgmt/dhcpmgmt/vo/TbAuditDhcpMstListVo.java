package com.kt.ipms.legacy.ticketmgmt.dhcpmgmt.vo;
import java.io.Serializable;
import java.util.List;
import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class TbAuditDhcpMstListVo extends CommonVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = -8124688273284969791L;
	
	private List<TbAuditDhcpMstVo> tbAuditDhcpMstVos;
	
	private int totalCount;
	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/
	public List<TbAuditDhcpMstVo> getTbAuditDhcpMstVos() {
		return this.tbAuditDhcpMstVos;
	}
	
	public void setTbAuditDhcpMstVos(List<TbAuditDhcpMstVo> tbAuditDhcpMstVos) {
		this.tbAuditDhcpMstVos = tbAuditDhcpMstVos;
	}
	
	public int getTotalCount() {
		return this.totalCount;
	}
	
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	/** MEMBER METHOD DECLARATION END **/
}