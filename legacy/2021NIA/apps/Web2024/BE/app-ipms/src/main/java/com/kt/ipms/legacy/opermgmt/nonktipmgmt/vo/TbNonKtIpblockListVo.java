package com.kt.ipms.legacy.opermgmt.nonktipmgmt.vo;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class TbNonKtIpblockListVo extends CommonVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = 4915314413406536566L;
	
	private List<TbNonKtIpblockVo> tbNonKtIpblockVos;
	
	private BigInteger nnonKtSvcMstSeq;
	
	private int totalCount;
	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/
	public List<TbNonKtIpblockVo> getTbNonKtIpblockVos() {
		return this.tbNonKtIpblockVos;
	}
	
	public void setTbNonKtIpblockVos(List<TbNonKtIpblockVo> tbNonKtIpblockVos) {
		this.tbNonKtIpblockVos = tbNonKtIpblockVos;
	}
	
	public int getTotalCount() {
		return this.totalCount;
	}
	
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public BigInteger getNnonKtSvcMstSeq() {
		return nnonKtSvcMstSeq;
	}

	public void setNnonKtSvcMstSeq(BigInteger nnonKtSvcMstSeq) {
		this.nnonKtSvcMstSeq = nnonKtSvcMstSeq;
	}
	

	/** MEMBER METHOD DECLARATION END **/

}