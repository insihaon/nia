package com.kt.ipms.legacy.linkmgmt.common.vo;
import java.io.Serializable;
import java.util.List;
import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class TbLnkSvcBasListVo extends CommonVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = -6056322822267123949L;
	
	private List<TbLnkSvcBasVo> tbLnkSvcBasVos;
	
	private int totalCount;
	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/
	public List<TbLnkSvcBasVo> getTbLnkSvcBasVos() {
		return this.tbLnkSvcBasVos;
	}
	
	public void setTbLnkSvcBasVos(List<TbLnkSvcBasVo> tbLnkSvcBasVos) {
		this.tbLnkSvcBasVos = tbLnkSvcBasVos;
	}
	
	public int getTotalCount() {
		return this.totalCount;
	}
	
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	/** MEMBER METHOD DECLARATION END **/
}