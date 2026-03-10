package com.kt.ipms.legacy.linkmgmt.common.vo;
import java.io.Serializable;
import java.util.List;
import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class TbWsSvcBasListVo extends CommonVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = -9123977814907271135L;
	
	private List<TbWsSvcBasVo> tbWsSvcBasVos;
	
	private int totalCount;
	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/
	public List<TbWsSvcBasVo> getTbWsSvcBasVos() {
		return this.tbWsSvcBasVos;
	}
	
	public void setTbWsSvcBasVos(List<TbWsSvcBasVo> tbWsSvcBasVos) {
		this.tbWsSvcBasVos = tbWsSvcBasVos;
	}
	
	public int getTotalCount() {
		return this.totalCount;
	}
	
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	/** MEMBER METHOD DECLARATION END **/
}