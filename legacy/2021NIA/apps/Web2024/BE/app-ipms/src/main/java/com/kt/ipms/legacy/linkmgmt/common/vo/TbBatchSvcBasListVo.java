package com.kt.ipms.legacy.linkmgmt.common.vo;
import java.io.Serializable;
import java.util.List;
import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class TbBatchSvcBasListVo extends CommonVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = -890370144468513058L;
	
	private List<TbBatchSvcBasVo> tbBatchSvcBasVos;
	
	private int totalCount;
	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/
	public List<TbBatchSvcBasVo> getTbBatchSvcBasVos() {
		return this.tbBatchSvcBasVos;
	}
	
	public void setTbBatchSvcBasVos(List<TbBatchSvcBasVo> tbBatchSvcBasVos) {
		this.tbBatchSvcBasVos = tbBatchSvcBasVos;
	}
	
	public int getTotalCount() {
		return this.totalCount;
	}
	
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	/** MEMBER METHOD DECLARATION END **/
}