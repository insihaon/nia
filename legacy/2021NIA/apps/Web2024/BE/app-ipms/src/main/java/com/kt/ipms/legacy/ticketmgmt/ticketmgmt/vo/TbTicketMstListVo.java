package com.kt.ipms.legacy.ticketmgmt.ticketmgmt.vo;
import java.io.Serializable;
import java.util.List;
import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class TbTicketMstListVo extends CommonVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = -5759523999687488804L;
	
	private List<TbTicketMstVo> tbTicketMstVos;
	
	private int totalCount;
	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/
	public List<TbTicketMstVo> getTbTicketMstVos() {
		return this.tbTicketMstVos;
	}
	
	public void setTbTicketMstVos(List<TbTicketMstVo> tbTicketMstVos) {
		this.tbTicketMstVos = tbTicketMstVos;
	}
	
	public int getTotalCount() {
		return this.totalCount;
	}
	
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	/** MEMBER METHOD DECLARATION END **/
}