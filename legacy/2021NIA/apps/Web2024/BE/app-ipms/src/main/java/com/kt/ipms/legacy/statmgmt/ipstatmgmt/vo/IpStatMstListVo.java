package com.kt.ipms.legacy.statmgmt.ipstatmgmt.vo;
import java.io.Serializable;
import java.util.List;

public class IpStatMstListVo extends IpStatMstVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = -1712728943925889360L;
	
	private List<IpStatMstVo> ipStatMstVos;
	
	private int totalCount;
	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/
	public void setIpStatMstVos(List<IpStatMstVo> ipStatMstVos) {
		this.ipStatMstVos = ipStatMstVos;
	}

	public List<IpStatMstVo> getIpStatMstVos() {
		return ipStatMstVos;
	}
	
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	
	public int getTotalCount() {
		return this.totalCount;
	}
	
	/** MEMBER METHOD DECLARATION END **/
}