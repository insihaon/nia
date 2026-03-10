package com.kt.ipms.legacy.opermgmt.nonktipmgmt.vo;
import java.io.Serializable;
import java.util.List;
import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class TbNonKtSvcMstListVo extends CommonVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = -2945091172697092246L;
	
	private List<TbNonKtSvcMstVo> tbNonKtSvcMstVos;
	
	private int totalCount;
	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/
	public List<TbNonKtSvcMstVo> getTbNonKtSvcMstVos() {
		return this.tbNonKtSvcMstVos;
	}
	
	public void setTbNonKtSvcMstVos(List<TbNonKtSvcMstVo> tbNonKtSvcMstVos) {
		this.tbNonKtSvcMstVos = tbNonKtSvcMstVos;
	}
	
	public int getTotalCount() {
		return this.totalCount;
	}
	
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	
	/** MEMBER METHOD DECLARATION END **/

}