package com.kt.ipms.legacy.opermgmt.menumgmt.vo;
import java.io.Serializable;
import java.util.List;
import com.kt.ipms.legacy.cmn.vo.CommonVo;

public class TbMenuBasListVo extends CommonVo implements Serializable {
	/** MEMBER VARIABLE DECLARATION START **/
	private static final long serialVersionUID = -8329729876990800596L;
	
	private List<TbMenuBasVo> tbMenuBasVos;
	
	private List<TbMenuBasVo> firMenuBasVos;
	private List<TbMenuBasVo> secMenuBasVos;
	private List<TbMenuBasVo> thrmenuBasVos;
	
	private int totalCount;
	
	private int fTotalCount;
	private int sTotalConut;
	private int tTotalCount;
	/** MEMBER VARIABLE DECLARATION END **/
	
	/** MEMBER METHOD DECLARATION START **/
	public List<TbMenuBasVo> getTbMenuBasVos() {
		return this.tbMenuBasVos;
	}
	
	public void setTbMenuBasVos(List<TbMenuBasVo> tbMenuBasVos) {
		this.tbMenuBasVos = tbMenuBasVos;
	}
	
	public int getTotalCount() {
		return this.totalCount;
	}
	
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public List<TbMenuBasVo> getFirMenuBasVos() {
		return firMenuBasVos;
	}

	public void setFirMenuBasVos(List<TbMenuBasVo> firMenuBasVos) {
		this.firMenuBasVos = firMenuBasVos;
	}

	public List<TbMenuBasVo> getSecMenuBasVos() {
		return secMenuBasVos;
	}

	public void setSecMenuBasVos(List<TbMenuBasVo> secMenuBasVos) {
		this.secMenuBasVos = secMenuBasVos;
	}

	public List<TbMenuBasVo> getThrmenuBasVos() {
		return thrmenuBasVos;
	}

	public void setThrmenuBasVos(List<TbMenuBasVo> thrmenuBasVos) {
		this.thrmenuBasVos = thrmenuBasVos;
	}

	public int getfTotalCount() {
		return fTotalCount;
	}

	public void setfTotalCount(int fTotalCount) {
		this.fTotalCount = fTotalCount;
	}

	public int getsTotalConut() {
		return sTotalConut;
	}

	public void setsTotalConut(int sTotalConut) {
		this.sTotalConut = sTotalConut;
	}

	public int gettTotalCount() {
		return tTotalCount;
	}

	public void settTotalCount(int tTotalCount) {
		this.tTotalCount = tTotalCount;
	}
	
	
	/** MEMBER METHOD DECLARATION END **/
}