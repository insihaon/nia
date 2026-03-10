package com.kt.ipms.legacy.opermgmt.tacsmgmt.vo;
import java.io.Serializable;
import java.util.List;
import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class TbTacsFcltMstListVo extends CommonVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = 8153168206870168893L;
	
	private List<TbTacsFcltMstVo> tbTacsFcltMstVos;
	
	private int totalCount;
	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/
	public List<TbTacsFcltMstVo> getTbTacsFcltMstVos() {
		return this.tbTacsFcltMstVos;
	}
	
	public void setTbTacsFcltMstVos(List<TbTacsFcltMstVo> tbTacsFcltMstVos) {
		this.tbTacsFcltMstVos = tbTacsFcltMstVos;
	}
	
	public int getTotalCount() {
		return this.totalCount;
	}
	
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	/** MEMBER METHOD DECLARATION END **/
}